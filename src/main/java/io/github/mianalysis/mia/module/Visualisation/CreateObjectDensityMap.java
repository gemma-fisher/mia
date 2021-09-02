package io.github.mianalysis.mia.module.Visualisation;

import org.eclipse.sisu.Nullable;

import ij.IJ;
import ij.ImagePlus;
import ij.measure.Calibration;
import ij.process.ImageProcessor;
import io.github.mianalysis.mia.module.Categories;
import io.github.mianalysis.mia.module.Category;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.module.imageprocessing.pixel.FilterImage;
import io.github.mianalysis.mia.module.imageprocessing.pixel.ImageMath;
import io.github.mianalysis.mia.Object.Image;
import io.github.mianalysis.mia.Object.Obj;
import io.github.mianalysis.mia.Object.Objs;
import io.github.mianalysis.mia.Object.Status;
import io.github.mianalysis.mia.Object.Workspace;
import io.github.mianalysis.mia.Object.Parameters.BooleanP;
import io.github.mianalysis.mia.Object.Parameters.ChoiceP;
import io.github.mianalysis.mia.Object.Parameters.InputObjectsP;
import io.github.mianalysis.mia.Object.Parameters.OutputImageP;
import io.github.mianalysis.mia.Object.Parameters.Parameters;
import io.github.mianalysis.mia.Object.Parameters.SeparatorP;
import io.github.mianalysis.mia.Object.Parameters.Text.IntegerP;
import io.github.mianalysis.mia.Object.Refs.Collections.ImageMeasurementRefs;
import io.github.mianalysis.mia.Object.Refs.Collections.MetadataRefs;
import io.github.mianalysis.mia.Object.Refs.Collections.ObjMeasurementRefs;
import io.github.mianalysis.mia.Object.Refs.Collections.ParentChildRefs;
import io.github.mianalysis.mia.Object.Refs.Collections.PartnerRefs;
import io.github.sjcross.common.MathFunc.CumStat;
import io.github.sjcross.common.MathFunc.Indexer;
import io.github.sjcross.common.Object.Point;
import io.github.sjcross.common.Object.Volume.SpatCal;

public class CreateObjectDensityMap extends Module {
    public static final String INPUT_SEPARATOR = "Object input / Image output";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String OUTPUT_IMAGE = "Output image";
    public static final String OBJECT_MODES = "Object modes";

    public static final String MAP_SEPARATOR = "Map options";
    public static final String RANGE = "Range";
    public static final String MERGE_SLICES = "Merge slices";
    public static final String MERGE_TIME = "Merge time";

    public interface ObjectModes {
        String OBJECT_CENTROID = "Object centroid";
        String WHOLE_OBJECT = "Whole object";

        String[] ALL = new String[] { OBJECT_CENTROID, WHOLE_OBJECT };

    }

    public CreateObjectDensityMap(Modules modules) {
        super("Create object density map", modules);
    }

    public static void process(CumStat[] cumStats, Indexer indexer, Objs objects, String objectMode,
            @Nullable String message) {
        // Adding objects
        int count = 0;
        int nTotal = objects.size();
        for (Obj object : objects.values()) {
            switch (objectMode) {
            case ObjectModes.OBJECT_CENTROID:
                Point<Double> centroid = object.getMeanCentroid(true, false);
                int x = (int) Math.round(centroid.getX());
                int y = (int) Math.round(centroid.getY());
                int z = indexer.getDim()[2] == 1 ? 0 : (int) Math.round(centroid.getZ());
                int t = indexer.getDim()[3] == 1 ? 0 : object.getT();
                int idx = indexer.getIndex(new int[] { x, y, z, t });

                // Adding measurement
                cumStats[idx].addMeasure(1);

                break;

            case ObjectModes.WHOLE_OBJECT:
                // Getting all object points
                for (Point<Integer> point : object.getCoordinateSet()) {
                    // Getting index for this point
                    z = indexer.getDim()[2] == 1 ? 0 : point.getZ();
                    t = indexer.getDim()[3] == 1 ? 0 : object.getT();
                    idx = indexer.getIndex(new int[] { point.getX(), point.getY(), z, t });

                    // Adding measurement
                    cumStats[idx].addMeasure(1);

                }
                break;
            }

            if (message != null)
                writeProgressStatus(++count, nTotal, "objects",message);
        }
    }

    public static Image convertToImage(CumStat[] cumStats, Indexer indexer, String outputImageName,
            Calibration calibration) {
        int[] dim = indexer.getDim();
        int width = dim[0];
        int height = dim[1];
        int nSlices = dim[2];
        int nFrames = dim[3];

        // Creating ImagePlus
        ImagePlus outputIpl = IJ.createHyperStack(outputImageName, width, height, 1, nSlices, nFrames, 32);
        outputIpl.setCalibration(calibration);

        // Iterating over all points in the image
        for (int z = 0; z < nSlices; z++) {
            for (int t = 0; t < nFrames; t++) {
                outputIpl.setPosition(1, z + 1, t + 1);
                ImageProcessor ipr = outputIpl.getProcessor();

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        // Getting relevant index
                        int idx = indexer.getIndex(new int[] { x, y, z, t });
                        CumStat cumStat = cumStats[idx];
                        ipr.setf(x, y, (float) cumStat.getN());
                    }
                }
            }
        }

        return new Image(outputImageName, outputIpl);

    }

    public int getFilterArea(int radius) {
        ImagePlus kernelIpl = IJ.createImage("Kernel", radius * 2 + 3, radius * 2 + 3, 1, 8);
        ImageMath.process(kernelIpl, ImageMath.CalculationTypes.ADD, 255);
        kernelIpl.getProcessor().putPixel(radius + 1, radius + 1, 0);
        FilterImage.apply2DFilter(kernelIpl, FilterImage.FilterModes.MINIMUM2D, radius);

        int nVoxels = 0;
        ImageProcessor ipr = kernelIpl.getProcessor();
        for (int x = 0; x < ipr.getWidth(); x++) {
            for (int y = 0; y < ipr.getHeight(); y++) {
                if (ipr.getPixel(x, y) == 0)
                    nVoxels++;
            }
        }

        return nVoxels;

    }

    public int getFilterVolume(int radius) {
        ImagePlus kernelIpl = IJ.createImage("Kernel", radius * 2 + 3, radius * 2 + 3, radius * 2 + 3, 8);
        ImageMath.process(kernelIpl, ImageMath.CalculationTypes.ADD, 255);
        kernelIpl.getStack().setVoxel(radius + 1, radius + 1, radius + 1, 0);
        FilterImage.apply3DFilter(kernelIpl, FilterImage.FilterModes.MINIMUM3D, radius);

        int nVoxels = 0;
        for (int z = 0; z < kernelIpl.getNSlices(); z++) {
            ImageProcessor ipr = kernelIpl.getStack().getProcessor(z + 1);
            for (int x = 0; x < ipr.getWidth(); x++) {
                for (int y = 0; y < ipr.getHeight(); y++) {
                    if (ipr.getPixel(x, y) == 0)
                        nVoxels++;
                }
            }
        }

        return nVoxels;

    }

    @Override
    public Category getCategory() {
        return Categories.VISUALISATION;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public Status process(Workspace workspace) {
        // Getting input objects
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        Objs inputObjects = workspace.getObjectSet(inputObjectsName);

        // Getting parameters
        String outputImageName = parameters.getValue(OUTPUT_IMAGE);
        String objectMode = parameters.getValue(OBJECT_MODES);
        int range = parameters.getValue(RANGE);
        boolean mergeZ = parameters.getValue(MERGE_SLICES);
        boolean mergeT = parameters.getValue(MERGE_TIME);

        // Initialising stores
        SpatCal calibration = inputObjects.getSpatialCalibration();
        int nFrames = inputObjects.getNFrames();
        CumStat[] cumStats = CreateMeasurementMap.initialiseCumStats(calibration, nFrames, mergeZ, mergeT);
        Indexer indexer = CreateMeasurementMap.initialiseIndexer(calibration, nFrames, mergeZ, mergeT);

        // Compressing relevant measures
        process(cumStats, indexer, inputObjects, objectMode, getName());

        // Converting statistic array to Image
        writeStatus("Creating output image");
        Calibration imageCalibration = calibration.createImageCalibration();
        Image outputImage = convertToImage(cumStats, indexer, outputImageName, imageCalibration);

        // Calculating sum over range
        if (mergeZ) {
            FilterImage.apply2DFilter(outputImage.getImagePlus(), FilterImage.FilterModes.MEAN2D, range);
            int nPixels = getFilterArea(range);
            ImageMath.process(outputImage, ImageMath.CalculationTypes.MULTIPLY, nPixels);
        } else {
            FilterImage.apply3DFilter(outputImage.getImagePlus(), FilterImage.FilterModes.MEAN3D, range);
            int nVoxels = getFilterVolume(range);
            ImageMath.process(outputImage, ImageMath.CalculationTypes.MULTIPLY, nVoxels);
        }

        workspace.addImage(outputImage);
        if (showOutput)
            outputImage.showImage();

        return Status.PASS;

    }

    @Override
    protected void initialiseParameters() {
        parameters.add(new SeparatorP(INPUT_SEPARATOR, this));
        parameters.add(new InputObjectsP(INPUT_OBJECTS, this));
        parameters.add(new OutputImageP(OUTPUT_IMAGE, this));

        parameters.add(new SeparatorP(MAP_SEPARATOR, this));
        parameters.add(new ChoiceP(OBJECT_MODES, this, ObjectModes.WHOLE_OBJECT, ObjectModes.ALL));
        parameters.add(new IntegerP(RANGE, this, 3));
        parameters.add(new BooleanP(MERGE_SLICES, this, true));
        parameters.add(new BooleanP(MERGE_TIME, this, true));

    }

    @Override
    public Parameters updateAndGetParameters() {
        return parameters;

    }

    @Override
    public ImageMeasurementRefs updateAndGetImageMeasurementRefs() {
        return null;
    }

    @Override
    public ObjMeasurementRefs updateAndGetObjectMeasurementRefs() {
        return null;
    }

    @Override
    public MetadataRefs updateAndGetMetadataReferences() {
        return null;
    }

    @Override
    public ParentChildRefs updateAndGetParentChildRefs() {
        return null;
    }

    @Override
    public PartnerRefs updateAndGetPartnerRefs() {
        return null;
    }

    @Override
    public boolean verify() {
        return true;
    }
}
