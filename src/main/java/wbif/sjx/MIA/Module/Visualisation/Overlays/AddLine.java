package wbif.sjx.MIA.Module.Visualisation.Overlays;

import java.awt.Color;
import java.util.HashMap;

import ij.ImagePlus;
import ij.gui.Line;
import ij.plugin.Duplicator;
import wbif.sjx.MIA.Module.ModuleCollection;
import wbif.sjx.MIA.Module.PackageNames;
import wbif.sjx.MIA.Object.Image;
import wbif.sjx.MIA.Object.Obj;
import wbif.sjx.MIA.Object.ObjCollection;
import wbif.sjx.MIA.Object.Workspace;
import wbif.sjx.MIA.Object.Parameters.BooleanP;
import wbif.sjx.MIA.Object.Parameters.ChoiceP;
import wbif.sjx.MIA.Object.Parameters.DoubleP;
import wbif.sjx.MIA.Object.Parameters.ImageMeasurementP;
import wbif.sjx.MIA.Object.Parameters.InputImageP;
import wbif.sjx.MIA.Object.Parameters.InputObjectsP;
import wbif.sjx.MIA.Object.Parameters.ObjectMeasurementP;
import wbif.sjx.MIA.Object.Parameters.OutputImageP;
import wbif.sjx.MIA.Object.Parameters.ParamSeparatorP;
import wbif.sjx.MIA.Object.Parameters.ParameterCollection;
import wbif.sjx.MIA.Object.References.ImageMeasurementRefCollection;
import wbif.sjx.MIA.Object.References.MetadataRefCollection;
import wbif.sjx.MIA.Object.References.ObjMeasurementRefCollection;
import wbif.sjx.MIA.Object.References.RelationshipRefCollection;
import wbif.sjx.MIA.Process.ColourFactory;
import wbif.sjx.common.Object.Point;

public class AddLine extends Overlay {
    public static final String INPUT_SEPARATOR = "Image and object input";
    public static final String INPUT_IMAGE = "Input image";
    public static final String INPUT_OBJECTS = "Input objects";
    
    public static final String OUTPUT_SEPARATOR = "Image output";
    public static final String APPLY_TO_INPUT = "Apply to input image";
    public static final String ADD_OUTPUT_TO_WORKSPACE = "Add output image to workspace";
    public static final String OUTPUT_IMAGE = "Output image";
    
    public static final String REFERENCE_SEPARATOR = "Reference selection";
    public static final String REFERENCE_MODE_1 = "Reference mode 1";
    public static final String REFERENCE_IMAGE_1 = "Reference image 1";
    public static final String X_POSITION_MEASUREMENT_IM_1 = "X-pos. image meas. 1 (px)";
    public static final String Y_POSITION_MEASUREMENT_IM_1 = "Y-pos. image meas. 1 (px)";
    // public static final String Z_POSITION_MEASUREMENT_IM_1 = "Z-pos. image meas. 1 (slice)";
    public static final String X_POSITION_MEASUREMENT_OBJ_1 = "X-pos. object meas. 1 (px)";
    public static final String Y_POSITION_MEASUREMENT_OBJ_1 = "Y-pos. object meas. 1 (px)";
    // public static final String Z_POSITION_MEASUREMENT_OBJ_1 = "Z-pos. object meas. 1 (slice)";
    public static final String REFERENCE_MODE_2 = "Reference mode 2";
    public static final String REFERENCE_IMAGE_2 = "Reference image 2";
    public static final String X_POSITION_MEASUREMENT_IM_2 = "X-pos. image meas. 2 (px)";
    public static final String Y_POSITION_MEASUREMENT_IM_2 = "Y-pos. image meas. 2 (px)";
    // public static final String Z_POSITION_MEASUREMENT_IM_2 = "Z-pos. image meas. 2 (slice)";
    public static final String X_POSITION_MEASUREMENT_OBJ_2 = "X-pos. object meas. 2 (px)";
    public static final String Y_POSITION_MEASUREMENT_OBJ_2 = "Y-pos. object meas. 2 (px)";
    // public static final String Z_POSITION_MEASUREMENT_OBJ_2 = "Z-pos. object meas. 2 (slice)";
    
    public static final String RENDERING_SEPARATOR = "Overlay rendering";
    public static final String LINE_WIDTH = "Line width";
    
    public static final String EXECUTION_SEPARATOR = "Execution controls";
    public static final String ENABLE_MULTITHREADING = "Enable multithreading";
    
    
    public interface ReferenceModes {
        String CENTROID = "Object centroid";
        String IMAGE_MEASUREMENT = "Image measurement";
        String OBJECT_MEASUREMENT = "Object measurement";
        
        String[] ALL = new String[]{CENTROID,IMAGE_MEASUREMENT,OBJECT_MEASUREMENT};
        
    }
    
    
    public AddLine(final ModuleCollection modules) {
        super("Add line", modules);
    }
    
    @Override
    public String getPackageName() {
        return PackageNames.VISUALISATION_OVERLAYS;
    }
    
    
    public static Point<Double> getCentroid(final Obj obj) {
        final double xMeas = obj.getXMean(true);
        final double yMeas = obj.getYMean(true);
        final double zMeas = obj.getZMean(true,true);
        
        return new Point<Double>(xMeas,yMeas,zMeas);
        
    }
    
    public static Point<Double> getImageReference(final Image image, final String xMeasName, final String yMeasName) {        
        final double xMeas = image.getMeasurement(xMeasName).getValue();
        final double yMeas = image.getMeasurement(yMeasName).getValue();

        return new Point<Double>(xMeas,yMeas,0d);
        
    }
    
    public static Point<Double> getObjectReference(final Obj obj, final String xMeasName, final String yMeasName) {       
        final double xMeas = obj.getMeasurement(xMeasName).getValue();
        final double yMeas = obj.getMeasurement(yMeasName).getValue();
        
        return new Point<Double>(xMeas,yMeas,0d);
        
    }
    
    public static void addOverlay(Obj object, ImagePlus ipl, Color colour, double lineWidth, Point<Double> pos1, Point<Double> pos2) {
        if (ipl.getOverlay() == null) ipl.setOverlay(new ij.gui.Overlay());
        ij.gui.Overlay ovl = ipl.getOverlay();
        
        // Creating the line
        Line line = new Line(pos1.x,pos1.y,pos2.x,pos2.y);
        
        int t = object.getT()+1;
        if (ipl.isHyperStack()) {
            ipl.setPosition(1,1,t+1);
            line.setPosition(1,1, t+1);
        } else {
            int pos = Math.max(1,t+1);
            ipl.setPosition(pos);
            line.setPosition(pos);
        }
        
        line.setStrokeWidth(lineWidth);
        line.setStrokeColor(colour);
        ovl.addElement(line);
        
    }
    
    
    @Override
    protected boolean process(final Workspace workspace) {
        // Getting parameters
        boolean applyToInput = parameters.getValue(APPLY_TO_INPUT);
        boolean addOutputToWorkspace = parameters.getValue(ADD_OUTPUT_TO_WORKSPACE);
        String outputImageName = parameters.getValue(OUTPUT_IMAGE);
        
        // Getting input objects
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        ObjCollection inputObjects = workspace.getObjects().get(inputObjectsName);
        
        // Getting input image
        String inputImageName = parameters.getValue(INPUT_IMAGE);
        Image inputImage = workspace.getImages().get(inputImageName);
        ImagePlus ipl = inputImage.getImagePlus();
        
        final String refMode1 = parameters.getValue(REFERENCE_MODE_1);
        final String referenceImageName1 = parameters.getValue(REFERENCE_IMAGE_1);
        final String xPosMeasIm1 = parameters.getValue(X_POSITION_MEASUREMENT_IM_1);
        final String yPosMeasIm1 = parameters.getValue(Y_POSITION_MEASUREMENT_IM_1);
        // final String zPosMeasIm1 = parameters.getValue(Z_POSITION_MEASUREMENT_IM_1);
        final String xPosMeasObj1 = parameters.getValue(X_POSITION_MEASUREMENT_OBJ_1);
        final String yPosMeasObj1 = parameters.getValue(Y_POSITION_MEASUREMENT_OBJ_1);
        // final String zPosMeasObj1 = parameters.getValue(Z_POSITION_MEASUREMENT_OBJ_1);
        
        final String refMode2 = parameters.getValue(REFERENCE_MODE_2);
        final String referenceImageName2 = parameters.getValue(REFERENCE_IMAGE_2);
        final String xPosMeasIm2 = parameters.getValue(X_POSITION_MEASUREMENT_IM_2);
        final String yPosMeasIm2 = parameters.getValue(Y_POSITION_MEASUREMENT_IM_2);
        // final String zPosMeasIm2 = parameters.getValue(Z_POSITION_MEASUREMENT_IM_2);
        final String xPosMeasObj2 = parameters.getValue(X_POSITION_MEASUREMENT_OBJ_2);
        final String yPosMeasObj2 = parameters.getValue(Y_POSITION_MEASUREMENT_OBJ_2);
        // final String zPosMeasObj2 = parameters.getValue(Z_POSITION_MEASUREMENT_OBJ_2);
        
        double opacity = parameters.getValue(OPACITY);
        double lineWidth = parameters.getValue(LINE_WIDTH);
        boolean multithread = parameters.getValue(ENABLE_MULTITHREADING);
        
        Image referenceImage1 = null;    
        if (refMode1.equals(ReferenceModes.IMAGE_MEASUREMENT)) {
            referenceImage1 = workspace.getImage(referenceImageName1);
        }
        
        Image referenceImage2 = null;
        if (refMode2.equals(ReferenceModes.IMAGE_MEASUREMENT)) {
            referenceImage2 = workspace.getImage(referenceImageName2);
        }
        
        // Only add output to workspace if not applying to input
        if (applyToInput) addOutputToWorkspace = false;
        
        // Duplicating the image, so the original isn't altered
        if (!applyToInput) ipl = new Duplicator().run(ipl);
        
        // Generating colours for each object
        HashMap<Integer,Float> hues = getHues(inputObjects);
        
        for (final Obj inputObject:inputObjects.values()) {
            final Point<Double> pos1;
            final Point<Double> pos2;
            
            // Getting reference points
            switch (refMode1) {
                case ReferenceModes.CENTROID:
                default:
                pos1 = getCentroid(inputObject);
                break;
                case ReferenceModes.IMAGE_MEASUREMENT:
                pos1 = getImageReference(referenceImage1, xPosMeasIm1, yPosMeasIm1);
                break;
                case ReferenceModes.OBJECT_MEASUREMENT:
                pos1 = getObjectReference(inputObject, xPosMeasObj1, yPosMeasObj1);
                break;
            }
            
            switch (refMode2) {
                case ReferenceModes.CENTROID:
                default:
                pos2 = getCentroid(inputObject);
                break;
                case ReferenceModes.IMAGE_MEASUREMENT:
                pos2 = getImageReference(referenceImage2, xPosMeasIm2, yPosMeasIm2);
                break;
                case ReferenceModes.OBJECT_MEASUREMENT:
                pos2 = getObjectReference(inputObject, xPosMeasObj2, yPosMeasObj2);
                break;
            }    
            
            // Adding the overlay
            float hue = hues.get(inputObject.getID());
            Color colour = ColourFactory.getColour(hue,opacity);
            addOverlay(inputObject, ipl, colour, lineWidth, pos1, pos2);

        }
        
        Image outputImage = new Image(outputImageName,ipl);
        
        // If necessary, adding output image to workspace.  This also allows us to show it.
        if (addOutputToWorkspace) workspace.addImage(outputImage);
        if (showOutput) outputImage.showImage();
        
        return true;
        
    }
    
    @Override
    protected void initialiseParameters() {
        super.initialiseParameters();
        
        parameters.add(new ParamSeparatorP(INPUT_SEPARATOR,this));
        parameters.add(new InputImageP(INPUT_IMAGE, this));
        parameters.add(new InputObjectsP(INPUT_OBJECTS, this));
        
        parameters.add(new ParamSeparatorP(OUTPUT_SEPARATOR,this));
        parameters.add(new BooleanP(APPLY_TO_INPUT, this,false));
        parameters.add(new BooleanP(ADD_OUTPUT_TO_WORKSPACE, this,false));
        parameters.add(new OutputImageP(OUTPUT_IMAGE, this));
        
        parameters.add(new ParamSeparatorP(REFERENCE_SEPARATOR,this));
        parameters.add(new ChoiceP(REFERENCE_MODE_1,this,ReferenceModes.CENTROID,ReferenceModes.ALL));
        parameters.add(new InputImageP(REFERENCE_IMAGE_1,this));
        parameters.add(new ImageMeasurementP(X_POSITION_MEASUREMENT_IM_1,this));
        parameters.add(new ImageMeasurementP(Y_POSITION_MEASUREMENT_IM_1,this));
        // parameters.add(new ImageMeasurementP(Z_POSITION_MEASUREMENT_IM_1,this));
        parameters.add(new ObjectMeasurementP(X_POSITION_MEASUREMENT_OBJ_1,this));
        parameters.add(new ObjectMeasurementP(Y_POSITION_MEASUREMENT_OBJ_1,this));
        // parameters.add(new ObjectMeasurementP(Z_POSITION_MEASUREMENT_OBJ_1,this));
        parameters.add(new ChoiceP(REFERENCE_MODE_2,this,ReferenceModes.CENTROID,ReferenceModes.ALL));
        parameters.add(new InputImageP(REFERENCE_IMAGE_2,this));
        parameters.add(new ImageMeasurementP(X_POSITION_MEASUREMENT_IM_2,this));
        parameters.add(new ImageMeasurementP(Y_POSITION_MEASUREMENT_IM_2,this));
        // parameters.add(new ImageMeasurementP(Z_POSITION_MEASUREMENT_IM_2,this));
        parameters.add(new ObjectMeasurementP(X_POSITION_MEASUREMENT_OBJ_2,this));
        parameters.add(new ObjectMeasurementP(Y_POSITION_MEASUREMENT_OBJ_2,this));
        // parameters.add(new ObjectMeasurementP(Z_POSITION_MEASUREMENT_OBJ_2,this));
        
        parameters.add(new ParamSeparatorP(RENDERING_SEPARATOR,this));
        parameters.add(new DoubleP(LINE_WIDTH,this,1));
        
        parameters.add(new ParamSeparatorP(EXECUTION_SEPARATOR,this));
        parameters.add(new BooleanP(ENABLE_MULTITHREADING, this, true));
        
    }
    
    @Override
    public ParameterCollection updateAndGetParameters() {
        ParameterCollection returnedParameters = new ParameterCollection();
        
        returnedParameters.add(parameters.getParameter(INPUT_SEPARATOR));
        returnedParameters.add(parameters.getParameter(INPUT_IMAGE));
        returnedParameters.add(parameters.getParameter(INPUT_OBJECTS));
        
        returnedParameters.add(parameters.getParameter(OUTPUT_SEPARATOR));
        returnedParameters.add(parameters.getParameter(APPLY_TO_INPUT));
        if (!(boolean) parameters.getValue(APPLY_TO_INPUT)) {
            returnedParameters.add(parameters.getParameter(ADD_OUTPUT_TO_WORKSPACE));
            
            if ((boolean) parameters.getValue(ADD_OUTPUT_TO_WORKSPACE)) {
                returnedParameters.add(parameters.getParameter(OUTPUT_IMAGE));
            }
        }
        
        returnedParameters.add(parameters.getParameter(REFERENCE_SEPARATOR));
        returnedParameters.add(parameters.getParameter(REFERENCE_MODE_1));
        switch ((String) parameters.getValue(REFERENCE_MODE_1)) {
            case ReferenceModes.IMAGE_MEASUREMENT:
            returnedParameters.add(parameters.getParameter(REFERENCE_IMAGE_1));
            returnedParameters.add(parameters.getParameter(X_POSITION_MEASUREMENT_IM_1));
            returnedParameters.add(parameters.getParameter(Y_POSITION_MEASUREMENT_IM_1));
            // returnedParameters.add(parameters.getParameter(Z_POSITION_MEASUREMENT_IM_1));
            break;
            case ReferenceModes.OBJECT_MEASUREMENT:
            returnedParameters.add(parameters.getParameter(X_POSITION_MEASUREMENT_OBJ_1));
            returnedParameters.add(parameters.getParameter(Y_POSITION_MEASUREMENT_OBJ_1));
            // returnedParameters.add(parameters.getParameter(Z_POSITION_MEASUREMENT_OBJ_1));
            break;            
        }
        
        returnedParameters.add(parameters.getParameter(REFERENCE_MODE_2));
        switch ((String) parameters.getValue(REFERENCE_MODE_2)) {
            case ReferenceModes.IMAGE_MEASUREMENT:
            returnedParameters.add(parameters.getParameter(REFERENCE_IMAGE_2));
            returnedParameters.add(parameters.getParameter(X_POSITION_MEASUREMENT_IM_2));
            returnedParameters.add(parameters.getParameter(Y_POSITION_MEASUREMENT_IM_2));
            // returnedParameters.add(parameters.getParameter(Z_POSITION_MEASUREMENT_IM_2));
            break;
            case ReferenceModes.OBJECT_MEASUREMENT:
            returnedParameters.add(parameters.getParameter(X_POSITION_MEASUREMENT_OBJ_2));
            returnedParameters.add(parameters.getParameter(Y_POSITION_MEASUREMENT_OBJ_2));
            // returnedParameters.add(parameters.getParameter(Z_POSITION_MEASUREMENT_OBJ_2));
            break;            
        }
        
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        ((ObjectMeasurementP) parameters.getParameter(X_POSITION_MEASUREMENT_OBJ_1)).setObjectName(inputObjectsName);
        ((ObjectMeasurementP) parameters.getParameter(Y_POSITION_MEASUREMENT_OBJ_1)).setObjectName(inputObjectsName);
        // ((ObjectMeasurementP) parameters.getParameter(Z_POSITION_MEASUREMENT_OBJ_1)).setObjectName(inputObjectsName);
        ((ObjectMeasurementP) parameters.getParameter(X_POSITION_MEASUREMENT_OBJ_2)).setObjectName(inputObjectsName);
        ((ObjectMeasurementP) parameters.getParameter(Y_POSITION_MEASUREMENT_OBJ_2)).setObjectName(inputObjectsName);
        // ((ObjectMeasurementP) parameters.getParameter(Z_POSITION_MEASUREMENT_OBJ_2)).setObjectName(inputObjectsName);
        
        final String referenceImageName1 = parameters.getValue(REFERENCE_IMAGE_1);
        ((ImageMeasurementP) parameters.getParameter(X_POSITION_MEASUREMENT_IM_1)).setImageName(referenceImageName1);
        ((ImageMeasurementP) parameters.getParameter(Y_POSITION_MEASUREMENT_IM_1)).setImageName(referenceImageName1);
        // ((ImageMeasurementP) parameters.getParameter(Z_POSITION_MEASUREMENT_IM_1)).setImageName(referenceImageName1);
        
        final String referenceImageName2 = parameters.getValue(REFERENCE_IMAGE_2);
        ((ImageMeasurementP) parameters.getParameter(X_POSITION_MEASUREMENT_IM_2)).setImageName(referenceImageName2);
        ((ImageMeasurementP) parameters.getParameter(Y_POSITION_MEASUREMENT_IM_2)).setImageName(referenceImageName2);
        // ((ImageMeasurementP) parameters.getParameter(Z_POSITION_MEASUREMENT_IM_2)).setImageName(referenceImageName2);
        
        returnedParameters.add(parameters.getParameter(RENDERING_SEPARATOR));
        returnedParameters.addAll(super.updateAndGetParameters(inputObjectsName));
        returnedParameters.add(parameters.getParameter(LINE_WIDTH));
        
        returnedParameters.add(parameters.getParameter(EXECUTION_SEPARATOR));
        returnedParameters.add(parameters.getParameter(ENABLE_MULTITHREADING));
        
        return returnedParameters;
        
    }
    
    @Override
    public ImageMeasurementRefCollection updateAndGetImageMeasurementRefs() {
        return null;
    }
    
    @Override
    public ObjMeasurementRefCollection updateAndGetObjectMeasurementRefs() {
        return null;
        
    }
    
    @Override
    public MetadataRefCollection updateAndGetMetadataReferences() {
        return null;
    }
    
    @Override
    public RelationshipRefCollection updateAndGetRelationships() {
        return null;
    }
    
    @Override
    public boolean verify() {
        return true;
        
    }
    
    @Override
    public String getDescription() {
        return "Draws an overlay line between two specified points.<br><br>" +
        "Note: This currently only works in 2D.";
    }
}