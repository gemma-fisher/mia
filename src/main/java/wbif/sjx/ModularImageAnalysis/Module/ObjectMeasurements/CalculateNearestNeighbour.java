package wbif.sjx.ModularImageAnalysis.Module.ObjectMeasurements;

import wbif.sjx.ModularImageAnalysis.Module.HCModule;
import wbif.sjx.ModularImageAnalysis.Object.*;

/**
 * Created by sc13967 on 22/06/2017.
 */
public class CalculateNearestNeighbour extends HCModule {
    public static final String INPUT_OBJECTS = "Input objects";
    public static final String CALCULATE_WITHIN_PARENT = "Only calculate for objects in same parent";
    public static final String PARENT_OBJECTS = "Parent objects";

    private static final String NN_DISTANCE = "NN_DISTANCE";
    private static final String NN_ID = "NN_ID";

    private Reference inputObjects;

    @Override
    public String getTitle() {
        return "Calculate nearest neighbour";
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public void run(Workspace workspace, boolean verbose) {
        // Getting objects to measure
        String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
        ObjSet inputObjects = workspace.getObjects().get(inputObjectsName);

        // Getting parameters
        boolean calculateWithinParent = parameters.getValue(CALCULATE_WITHIN_PARENT);
        String parentObjectsName = parameters.getValue(PARENT_OBJECTS);

        // Running through each object, calculating the nearest neighbour distance
        for (Obj inputObject:inputObjects.values()) {
            double minDist = Double.MAX_VALUE;
            Obj nearestNeighbour = null;

            // Getting the object centroid
            double xCent = inputObject.getXMean(true);
            double yCent = inputObject.getYMean(true);
            double zCent = inputObject.getZMean(true,true);

            if (calculateWithinParent) {
                Obj parentObject = inputObject.getParent(parentObjectsName);

                // Some objects may not have a parent
                if (parentObject != null) {
                    ObjSet neighbourObjects = parentObject.getChildren(inputObjectsName);

                    for (Obj testObject : neighbourObjects.values()) {
                        if (testObject != inputObject) {
                            double xCentTest = testObject.getXMean(true);
                            double yCentTest = testObject.getYMean(true);
                            double zCentTest = testObject.getZMean(true,true);

                            double dist = Math.sqrt((xCentTest - xCent) * (xCentTest - xCent)
                                    + (yCentTest - yCent) * (yCentTest - yCent)
                                    + (zCentTest - zCent) * (zCentTest - zCent));

                            if (dist < minDist) {
                                minDist = dist;
                                nearestNeighbour = testObject;

                            }
                        }
                    }
                }

            } else {
                for (Obj testObject:inputObjects.values()) {
                    if (testObject != inputObject) {
                        double xCentTest = testObject.getXMean(true);
                        double yCentTest = testObject.getYMean(true);
                        double zCentTest = testObject.getZMean(true,true);

                        double dist = Math.sqrt((xCentTest - xCent) * (xCentTest - xCent)
                                + (yCentTest - yCent) * (yCentTest - yCent)
                                + (zCentTest - zCent) * (zCentTest - zCent));

                        if (dist < minDist) {
                            minDist = dist;
                            nearestNeighbour = testObject;

                        }
                    }
                }
            }

            // Adding details of the nearest neighbour to the input object's measurements
            if (nearestNeighbour != null) {
                inputObject.addMeasurement(new MIAMeasurement(NN_ID, nearestNeighbour.getID()));
                inputObject.addMeasurement(new MIAMeasurement(NN_DISTANCE, minDist));

            } else {
                inputObject.addMeasurement(new MIAMeasurement(NN_ID, Double.NaN));
                inputObject.addMeasurement(new MIAMeasurement(NN_DISTANCE, Double.NaN));

            }
        }
    }

    @Override
    public void initialiseParameters() {
        parameters.addParameter(new Parameter(INPUT_OBJECTS, Parameter.INPUT_OBJECTS,null));
        parameters.addParameter(new Parameter(CALCULATE_WITHIN_PARENT, Parameter.BOOLEAN,false));
        parameters.addParameter(new Parameter(PARENT_OBJECTS, Parameter.PARENT_OBJECTS,null,null));

    }

    @Override
    public ParameterCollection getActiveParameters() {
        ParameterCollection returnedParameters = new ParameterCollection();

        returnedParameters.addParameter(parameters.getParameter(INPUT_OBJECTS));
        returnedParameters.addParameter(parameters.getParameter(CALCULATE_WITHIN_PARENT));

        if (parameters.getValue(CALCULATE_WITHIN_PARENT)) {
            returnedParameters.addParameter(parameters.getParameter(PARENT_OBJECTS));

            String inputObjectsName = parameters.getValue(INPUT_OBJECTS);
            parameters.updateValueSource(PARENT_OBJECTS,inputObjectsName);

        }

        return returnedParameters;

    }

    @Override
    public void initialiseReferences() {
        inputObjects = new Reference();
        objectReferences.add(inputObjects);

        inputObjects.addMeasurementReference(new MeasurementReference(NN_DISTANCE));
        inputObjects.addMeasurementReference(new MeasurementReference(NN_ID));

    }

    @Override
    public ReferenceCollection updateAndGetImageReferences() {
        return null;
    }

    @Override
    public ReferenceCollection updateAndGetObjectReferences() {
        return objectReferences;
    }

    @Override
    public void addRelationships(RelationshipCollection relationships) {

    }
}

