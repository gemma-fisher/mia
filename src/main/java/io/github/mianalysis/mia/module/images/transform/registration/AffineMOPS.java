package io.github.mianalysis.mia.module.images.transform.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ij.process.ImageProcessor;
import mpicbg.ij.MOPS;
import mpicbg.imagefeatures.Feature;
import mpicbg.imagefeatures.FloatArray2DMOPS;
import mpicbg.models.AbstractAffineModel2D;
import mpicbg.models.NotEnoughDataPointsException;
import mpicbg.models.PointMatch;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.module.images.transform.registration.abstrakt.AbstractAffineRegistration;
import io.github.mianalysis.mia.module.Module;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.parameters.Parameters;
import io.github.mianalysis.mia.object.parameters.SeparatorP;
import io.github.mianalysis.mia.object.parameters.text.DoubleP;
import io.github.mianalysis.mia.object.parameters.text.IntegerP;

@Plugin(type = Module.class, priority=Priority.LOW, visible=true)
public class AffineMOPS extends AbstractAffineRegistration {
        public static final String FEATURE_SEPARATOR = "Feature detection";
        public static final String INITIAL_SIGMA = "Initial Gaussian blur (px)";
        public static final String STEPS = "Steps per scale";
        public static final String MINIMUM_IMAGE_SIZE = "Minimum image size (px)";
        public static final String MAXIMUM_IMAGE_SIZE = "Maximum image size (px)";
        public static final String FD_SIZE = "Feature descriptor size";
        public static final String FD_ORIENTATION_BINS = "Feature descriptor orientation bins";
        public static final String ROD = "Closest/next closest ratio";
        public static final String MAX_EPSILON = "Maximal alignment error (px)";
        public static final String MIN_INLIER_RATIO = "Inlier ratio";

        public AffineMOPS(Modules modules) {
                super("Affine (MOPS)", modules);
        }

        @Override
        public String getDescription() {
                return "Apply slice-by-slice (2D) affine-based image registration to a multi-dimensional stack.  Images can be aligned relative to the first frame in the stack, the previous frame or a separate image in the workspace.  The registration transform can also be calculated from a separate stack to the one that it will be applied to.  Registration can be performed along either the time or Z axes.  The non-registered axis (e.g. time axis when registering in Z) can be \"linked\" (all frames given the same registration) or \"independent\" (each stack registered separately)."

                                + "<br><br>This module uses the <a href=\"https://imagej.net/Feature_Extraction\">Feature Extraction</a> plugin and associated MPICBG tools to detect MOPS (\"Multi-Scale Oriented Patches\") features from the input images and calculate and apply the necessary 2D affine transforms."

                                + "<br><br>References:<ul>"

                                + "<li>Brown, Matthew & Szeliski, Richard \"Multi-image feature matching using multi-scale oriented patches\". US Patent 7,382,897 (June 3, 2008). Asignee: Microsoft Corporation.</li></ul>";

        }

        @Override
        public MOPSParam createParameterSet() {
                return new MOPSParam();
        }

        @Override
        public void getParameters(Param param, Workspace workspace) {
                super.getParameters(param, workspace);

                // Setting up the parameters
                MOPSParam mopsParam = (MOPSParam) param;
                mopsParam.initialSigma = (float) (double) parameters.getValue(INITIAL_SIGMA,workspace);
                mopsParam.steps = parameters.getValue(STEPS,workspace);
                mopsParam.minOctaveSize = parameters.getValue(MINIMUM_IMAGE_SIZE,workspace);
                mopsParam.maxOctaveSize = parameters.getValue(MAXIMUM_IMAGE_SIZE,workspace);
                mopsParam.fdSize = parameters.getValue(FD_SIZE,workspace);
                mopsParam.rod = (float) (double) parameters.getValue(ROD,workspace);
                mopsParam.maxEpsilon = (float) (double) parameters.getValue(MAX_EPSILON,workspace);
                mopsParam.minInlierRatio = (float) (double) parameters.getValue(MIN_INLIER_RATIO,workspace);

        }

        @Override
        protected Object[] fitModel(ImageProcessor referenceIpr, ImageProcessor warpedIpr, Param param) {
                MOPSParam p = (MOPSParam) param;

                // Creating SIFT parameter structure
                FloatArray2DMOPS.Param mopsParam = new FloatArray2DMOPS.Param();
                mopsParam.fdSize = p.fdSize;
                mopsParam.initialSigma = p.initialSigma;
                mopsParam.maxOctaveSize = p.maxOctaveSize;
                mopsParam.minOctaveSize = p.minOctaveSize;
                mopsParam.steps = p.steps;

                // Initialising SIFT feature extractor
                MOPS mops = new MOPS(new FloatArray2DMOPS(mopsParam));

                // Extracting features
                ArrayList<Feature> featureList1 = new ArrayList<Feature>();
                ArrayList<Feature> featureList2 = new ArrayList<Feature>();
                try {
                        mops.extractFeatures(referenceIpr, featureList1);
                        mops.extractFeatures(warpedIpr, featureList2);
                } catch (ArrayIndexOutOfBoundsException e) {
                        return null;
                }

                // Running registration
                AbstractAffineModel2D model = getModel(p.transformationMode);
                List<PointMatch> candidates = FloatArray2DMOPS.createMatches(featureList1, featureList2, 1.5f, null,
                                Double.MAX_VALUE, p.rod);
                Vector<PointMatch> inliers = new Vector<PointMatch>();
                Vector<PointMatch> candidatesVector = new Vector<>(candidates);

                try {
                        model.filterRansac(candidates, inliers, 1000, p.maxEpsilon, p.minInlierRatio);
                } catch (NotEnoughDataPointsException e) {
                        return null;
                }

                return new Object[] { model, candidatesVector };

        }

        @Override
        protected void initialiseParameters() {
                super.initialiseParameters();

                parameters.add(new SeparatorP(FEATURE_SEPARATOR, this));
                parameters.add(new DoubleP(INITIAL_SIGMA, this, 1.6));
                parameters.add(new IntegerP(STEPS, this, 3));
                parameters.add(new IntegerP(MINIMUM_IMAGE_SIZE, this, 64));
                parameters.add(new IntegerP(MAXIMUM_IMAGE_SIZE, this, 1024));
                parameters.add(new IntegerP(FD_SIZE, this, 4));
                parameters.add(new IntegerP(FD_ORIENTATION_BINS, this, 8));
                parameters.add(new DoubleP(ROD, this, 0.92));
                parameters.add(new DoubleP(MAX_EPSILON, this, 25.0));
                parameters.add(new DoubleP(MIN_INLIER_RATIO, this, 0.05));

                addParameterDescriptions();

        }

        @Override
        public Parameters updateAndGetParameters() {
Workspace workspace = null;
                Parameters returnedParameters = new Parameters();

                returnedParameters.addAll(super.updateAndGetParameters());

                returnedParameters.add(parameters.getParameter(FEATURE_SEPARATOR));
                returnedParameters.add(parameters.getParameter(INITIAL_SIGMA));
                returnedParameters.add(parameters.getParameter(STEPS));
                returnedParameters.add(parameters.getParameter(MINIMUM_IMAGE_SIZE));
                returnedParameters.add(parameters.getParameter(MAXIMUM_IMAGE_SIZE));
                returnedParameters.add(parameters.getParameter(FD_SIZE));
                returnedParameters.add(parameters.getParameter(FD_ORIENTATION_BINS));
                returnedParameters.add(parameters.getParameter(ROD));
                returnedParameters.add(parameters.getParameter(MAX_EPSILON));
                returnedParameters.add(parameters.getParameter(MIN_INLIER_RATIO));

                return returnedParameters;

        }

        @Override
        protected void addParameterDescriptions() {
                super.addParameterDescriptions();

                String siteRef = "Description taken from <a href=\"https://imagej.net/Feature_Extraction\">https://imagej.net/Feature_Extraction</a>";

                parameters.get(INITIAL_SIGMA).setDescription(
                                "\"Accurate localization of keypoints requires initial smoothing of the image. If your images are blurred already, you might lower the initial blur σ0 slightly to get more but eventually less stable keypoints. Increasing σ0 increases the computational cost for Gaussian blur, setting it to σ0=3.2px is equivalent to keep σ0=1.6px and use half maximum image size. Tip: Keep the default value σ0=1.6px as suggested by Lowe (2004).\".  "
                                                + siteRef);

                parameters.get(STEPS).setDescription(
                                "\"Keypoint candidates are extracted at all scales between maximum image size and minimum image size. This Scale Space is represented in octaves each covering a fixed number of discrete scale steps from σ0 to 2σ0. More steps result in more but eventually less stable keypoint candidates. Tip: Keep 3 as suggested by Lowe (2004) and do not use more than 10.\".  "
                                                + siteRef);

                parameters.get(MINIMUM_IMAGE_SIZE).setDescription(
                                "\"The Scale Space stops if the size of the octave would be smaller than minimum image size. Tip: Increase the minimum size to discard large features (i.e. those extracted from looking at an image from far, such as the overall shape).\".  "
                                                + siteRef);

                parameters.get(MAXIMUM_IMAGE_SIZE).setDescription(
                                "\"The Scale Space starts with the first octave equal or smaller than the maximum image size. Tip: By reducing the size, fine scaled features will be discarded. Increasing the size beyond that of the actual images has no effect.\".  "
                                                + siteRef);

                parameters.get(FD_SIZE).setDescription(
                                "\"The MOPS-descriptor is simply a nxn intensity patch with normalized intensities. Brown (2005) suggests n=8.  We found larger descriptors with n>16 perform better for Transmission Electron Micrographs from serial sections.\".  "
                                                + siteRef);

                parameters.get(FD_ORIENTATION_BINS).setDescription(
                                "\"For SIFT-descriptors, this is the number of orientation bins b per 4×4px block as described above. Tip: Keep the default value b=8 as suggested by Lowe (2004).\".  "
                                                + siteRef);

                parameters.get(ROD).setDescription(
                                "\"Correspondence candidates from local descriptor matching are accepted only if the Euclidean distance to the nearest neighbour is significantly smaller than that to the next nearest neighbour. Lowe (2004) suggests a ratio of r=0.8 which requires some increase when matching things that appear significantly distorted.\".  "
                                                + siteRef);

                parameters.get(MAX_EPSILON).setDescription(
                                "\"Matching local descriptors gives many false positives, but true positives are consistent with respect to a common transformation while false positives are not. This consistent set and the underlying transformation are identified using RANSAC. This value is the maximal allowed transfer error of a match to be counted as a good one. Tip: Set this to about 10% of the image size.\".  "
                                                + siteRef);

                parameters.get(MIN_INLIER_RATIO).setDescription(
                                "\"The ratio of the number of true matches to the number of all matches including both true and false used by RANSAC. 0.05 means that minimally 5% of all matches are expected to be good while 0.9 requires that 90% of the matches were good. Only transformations with this minimal ratio of true consent matches are accepted. Tip: Do not go below 0.05 (and only if 5% is more than about 7 matches) except with a very small maximal alignment error to avoid wrong solutions.\".  "
                                                + siteRef);

        }

public class MOPSParam extends AffineParam {
                // Fitting parameters
                float rod = 0.92f;
                float maxEpsilon = 25.0f;
                float minInlierRatio = 0.05f;

                // General parameters
                float initialSigma = 1.6f;
                int fdSize = 16;
                int maxOctaveSize = 1024;
                int minOctaveSize = 64;
                int steps = 3;

        }
}
