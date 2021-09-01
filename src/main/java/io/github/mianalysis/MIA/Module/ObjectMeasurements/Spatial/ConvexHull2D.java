package io.github.mianalysis.MIA.Module.ObjectMeasurements.Spatial;

import java.awt.Polygon;

import io.github.mianalysis.MIA.Module.Categories;
import io.github.mianalysis.MIA.Module.Category;
import io.github.mianalysis.MIA.Module.Module;
import io.github.mianalysis.MIA.Module.ModuleCollection;
import io.github.mianalysis.MIA.Object.Obj;
import io.github.mianalysis.MIA.Object.ObjCollection;
import io.github.mianalysis.MIA.Object.Status;
import io.github.mianalysis.MIA.Object.Workspace;
import io.github.mianalysis.MIA.Object.Parameters.InputObjectsP;
import io.github.mianalysis.MIA.Object.Parameters.ParameterCollection;
import io.github.mianalysis.MIA.Object.Parameters.SeparatorP;
import io.github.mianalysis.MIA.Object.Parameters.Objects.OutputObjectsP;
import io.github.mianalysis.MIA.Object.References.Collections.ImageMeasurementRefCollection;
import io.github.mianalysis.MIA.Object.References.Collections.MetadataRefCollection;
import io.github.mianalysis.MIA.Object.References.Collections.ObjMeasurementRefCollection;
import io.github.mianalysis.MIA.Object.References.Collections.ParentChildRefCollection;
import io.github.mianalysis.MIA.Object.References.Collections.PartnerRefCollection;
import io.github.sjcross.common.Object.Volume.PointOutOfRangeException;
import io.github.sjcross.common.Object.Volume.VolumeType;

public class ConvexHull2D extends Module {
    public static final String INPUT_SEPARATOR = "Object input/output";
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String OUTPUT_OBJECTS = "Output objects";


    public Obj processObject(Obj inputObject, ObjCollection outputObjects) {
        Polygon polygon = inputObject.getRoi(0).getConvexHull();

        // We have to explicitly define this, as the number of slices is 1 (potentially unlike the input object)
        Obj outputObject = outputObjects.createAndAddNewObject(VolumeType.QUADTREE);
        
        try {outputObject.addPointsFromPolygon(polygon,0);}
        catch (PointOutOfRangeException e) {}

        outputObject.setT(inputObject.getT());

        outputObject.addParent(inputObject);
        inputObject.addChild(outputObject);

        return outputObject;
        
    }

    public ConvexHull2D(ModuleCollection modules) {
        super("Convex hull 2D", modules);
    }

    @Override
    public String getDescription() {
        return "Fit 2D convex hull to a 2D object.  If objects are in 3D, a Z-projection of the object is used.<br><br>" +
                "Uses the ImageJ \"Fit convex hull\" function.";
    }


    @Override
    public Category getCategory() {
        return Categories.OBJECT_PROCESSING_IDENTIFICATION;
    }

    @Override
    protected Status process(Workspace workspace) {
        // Getting input objects
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        ObjCollection inputObjects = workspace.getObjectSet(inputObjectsName);

        // Getting parameters
        String outputObjectsName = parameters.getValue(OUTPUT_OBJECTS);

        // If necessary, creating a new ObjCollection and adding it to the Workspace
        ObjCollection outputObjects = new ObjCollection(outputObjectsName,inputObjects);
        workspace.addObjects(outputObjects);

        for (Obj inputObject:inputObjects.values())
            processObject(inputObject,outputObjects);            

        if (showOutput) outputObjects.convertToImageRandomColours().showImage();

        return Status.PASS;

    }

    @Override
    protected void initialiseParameters() {
        parameters.add(new SeparatorP(INPUT_SEPARATOR,this));
        parameters.add(new InputObjectsP(INPUT_OBJECTS,this));
        parameters.add(new OutputObjectsP(OUTPUT_OBJECTS, this));

        addParameterDescriptions();

    }

    @Override
    public ParameterCollection updateAndGetParameters() {
        ParameterCollection returnedParameters = new ParameterCollection();

        returnedParameters.add(parameters.getParameter(INPUT_SEPARATOR));
        returnedParameters.add(parameters.getParameter(INPUT_OBJECTS));
        returnedParameters.add(parameters.getParameter(OUTPUT_OBJECTS));

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
    public ParentChildRefCollection updateAndGetParentChildRefs() {
        ParentChildRefCollection returnedRelationships = new ParentChildRefCollection();

        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        String outputObjectsName = parameters.getValue(OUTPUT_OBJECTS);
        returnedRelationships.add(parentChildRefs.getOrPut(inputObjectsName,outputObjectsName));

        return returnedRelationships;

    }

    @Override
    public PartnerRefCollection updateAndGetPartnerRefs() {
        return null;
    }

    @Override
    public boolean verify() {
        return true;
    }

    void addParameterDescriptions() {
        parameters.get(INPUT_OBJECTS).setDescription("Input objects to create 2D convex hulls for.  Each convex hull will be a child of its respective input object.");

        parameters.get(OUTPUT_OBJECTS).setDescription("Output convex hull objects will be stored in the workspace with this name.  Each convex hull object will be a child of the input object it was created from.");

    }
}
