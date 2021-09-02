package io.github.mianalysis.MIA.Module.ImageProcessing.Stack;

import ij.ImagePlus;
import ij.measure.Calibration;
import ij.plugin.Duplicator;
import ij.plugin.HyperStackConverter;
import net.imagej.ImgPlus;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import io.github.mianalysis.MIA.MIA;
import io.github.mianalysis.MIA.Module.Module;
import io.github.mianalysis.MIA.Module.Modules;
import io.github.mianalysis.MIA.Module.Core.InputControl;
import io.github.mianalysis.MIA.Module.Category;
import io.github.mianalysis.MIA.Module.Categories;
import io.github.mianalysis.MIA.Object.Image;
import io.github.mianalysis.MIA.Object.Status;
import io.github.mianalysis.MIA.Object.Workspace;
import io.github.mianalysis.MIA.Object.Parameters.BooleanP;
import io.github.mianalysis.MIA.Object.Parameters.InputImageP;
import io.github.mianalysis.MIA.Object.Parameters.OutputImageP;
import io.github.mianalysis.MIA.Object.Parameters.Parameters;
import io.github.mianalysis.MIA.Object.Parameters.Text.DoubleP;
import io.github.mianalysis.MIA.Object.Refs.Collections.ImageMeasurementRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.MetadataRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.ObjMeasurementRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.ParentChildRefs;
import io.github.mianalysis.MIA.Object.Refs.Collections.PartnerRefs;

public class ApplyOffsetCorrection< T extends RealType< T > & NativeType< T >> extends Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String APPLY_TO_INPUT = "Apply to input image";
    public static final String OUTPUT_IMAGE = "Output image";
    public static final String X_SHIFT = "Shift in x";
    public static final String Y_SHIFT = "Shift in y";
    public static final String Z_SHIFT = "Shift in z";
    public static final String CALIBRATED_UNITS = "Calibrated units";

    public ApplyOffsetCorrection(Modules modules) {
        super("Apply offset correction",modules);
    }


    int[] getPixelShifts(Image image) {
        double xShift = parameters.getValue(X_SHIFT);
        double yShift = parameters.getValue(Y_SHIFT);
        double zShift = parameters.getValue(Z_SHIFT);
        boolean calibratedUnits = parameters.getValue(CALIBRATED_UNITS);

        int[] shifts = new int[3];
        if (calibratedUnits) {
            Calibration calibration = image.getImagePlus().getCalibration();
            shifts[0] = (int) Math.round(xShift/calibration.pixelWidth);
            shifts[1] = (int) Math.round(yShift/calibration.pixelWidth);
            shifts[2] = (int) Math.round(zShift/calibration.pixelDepth);
        } else {
            shifts[0] = (int) Math.round(xShift);
            shifts[1] = (int) Math.round(yShift);
            shifts[2] = (int) Math.round(zShift);
        }

        return shifts;

    }

    void shiftImage(Image inputImage, int[] shifts) {
        Img<T> inputImg = inputImage.getImgPlus();

        // Getting the dimensions of the input image
        long[] dims = new long[inputImg.numDimensions()];
        for (int i=0;i<inputImg.numDimensions();i++) dims[i] = inputImg.dimension(i);

        // Creating the composite image
        T type = inputImg.firstElement();
        final ImgFactory< T > factory = new CellImgFactory<>(type);
        ImgPlus<T> shiftedImg = new ImgPlus<>(factory.create(dims));

        // Getting dimensions for cropped region
        for (int i=0;i<3;i++) dims[i] = dims[i] - Math.abs(shifts[i]);

        // Getting the offset for the first three dimensions.  All others are simply set to 0.
        long[] offsetIn = new long[inputImg.numDimensions()];
        long[] offsetShifted = new long[inputImg.numDimensions()];
        for (int i=0;i<3;i++) {
            if (shifts[i] < 0) {
                offsetShifted[i] = -shifts[i];
                offsetIn[i] = 0;
            } else {
                offsetIn[i] = shifts[i];
                offsetShifted[i] = 0;
            }
        }

        // Copying the pixel information from one image to the other
        Cursor<T> inputCursor = Views.offsetInterval(inputImg, offsetIn, dims).cursor();

        // Setting the zero offsets for the shifted image
        Cursor<T> shiftedCursor = Views.offsetInterval(shiftedImg, offsetShifted, dims).cursor();

        // Copying the data from inputImg to shiftedImg
        while (inputCursor.hasNext()) shiftedCursor.next().set(inputCursor.next());

        if (MIA.isImagePlusMode()) {
            ImagePlus inputIpl = inputImage.getImagePlus();
            ImagePlus ipl = ImageJFunctions.wrap(shiftedImg, inputImage.getName());
            ipl = HyperStackConverter.toHyperStack(ipl, inputIpl.getNChannels(), inputIpl.getNSlices(), inputIpl.getNFrames());
            inputImage.setImagePlus(ipl);
        } else {
            // Putting the Img back into the input Image
            inputImage.setImgPlus(shiftedImg);
        }
    }


    @Override
    public Category getCategory() {
        return Categories.IMAGE_PROCESSING_STACK;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public Status process(Workspace workspace) {
        // Getting input image
        String inputImageName = parameters.getValue(INPUT_IMAGE);
        Image inputImage = workspace.getImages().get(inputImageName);
        ImagePlus inputImagePlus = inputImage.getImagePlus();

        // Getting parameters
        boolean applyToInput = parameters.getValue(APPLY_TO_INPUT);
        String outputImageName = parameters.getValue(OUTPUT_IMAGE);

        // Getting pixel shifts
        int[] shifts = getPixelShifts(inputImage);

        // Processing the input image
        if (!applyToInput) inputImagePlus = new Duplicator().run(inputImagePlus);

        Calibration calibration = inputImagePlus.getCalibration();
        shiftImage(inputImage,shifts);
        inputImage.getImagePlus().setCalibration(calibration);

        if (!applyToInput) {
            writeStatus("Adding image ("+outputImageName+") to workspace");
            Image outputImage = new Image(outputImageName,inputImagePlus);
            workspace.addImage(outputImage);
            if (showOutput) outputImage.showImage();

        } else {
            if (showOutput) inputImage.showImage();

        }

        return Status.PASS;

    }

    @Override
    protected void initialiseParameters() {
        parameters.add(new InputImageP(INPUT_IMAGE,this));
        parameters.add(new BooleanP(APPLY_TO_INPUT,this,true));
        parameters.add(new OutputImageP(OUTPUT_IMAGE,this));
        parameters.add(new DoubleP(X_SHIFT,this,0.0));
        parameters.add(new DoubleP(Y_SHIFT,this,0.0));
        parameters.add(new DoubleP(Z_SHIFT,this,0.0));
        parameters.add(new BooleanP(CALIBRATED_UNITS,this,false,"When selected, spatial values are assumed to be specified in calibrated units (as defined by the \"" + new InputControl(null).getName() + "\" parameter \"" + InputControl.SPATIAL_UNIT + "\").  Otherwise, pixel units are assumed."));

    }

    @Override
    public Parameters updateAndGetParameters() {
        Parameters returnedParameters = new Parameters();
        returnedParameters.add(parameters.getParameter(INPUT_IMAGE));
        returnedParameters.add(parameters.getParameter(APPLY_TO_INPUT));

        if (!(boolean) parameters.getValue(APPLY_TO_INPUT)) {
            returnedParameters.add(parameters.getParameter(OUTPUT_IMAGE));
        }

        returnedParameters.add(parameters.getParameter(X_SHIFT));
        returnedParameters.add(parameters.getParameter(Y_SHIFT));
        returnedParameters.add(parameters.getParameter(Z_SHIFT));
        returnedParameters.add(parameters.getParameter(CALIBRATED_UNITS));

        return returnedParameters;

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
