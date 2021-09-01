package io.github.mianalysis.MIA.Macro.ObjectProcessing;

import ij.macro.MacroExtension;
import io.github.mianalysis.MIA.Macro.MacroOperation;
import io.github.mianalysis.MIA.Module.ModuleCollection;
import io.github.mianalysis.MIA.Object.Obj;
import io.github.mianalysis.MIA.Object.ObjCollection;
import io.github.mianalysis.MIA.Object.Workspace;

public class MIA_GetObjectChildIDs extends MacroOperation {
    public MIA_GetObjectChildIDs(MacroExtension theHandler) {
        super(theHandler);
    }

    @Override
    public int[] getArgumentTypes() {
        return new int[]{ARG_STRING,ARG_NUMBER,ARG_STRING};
    }

    @Override
    public String action(Object[] objects, Workspace workspace, ModuleCollection modules) {
        String inputObjectsName = (String) objects[0];
        int objectID = (int) Math.round((Double) objects[1]);
        String childObjectsName = (String) objects[2];

        // Getting the children of the input object
        ObjCollection inputObjects = workspace.getObjectSet(inputObjectsName);
        if (inputObjects == null) return "";
        Obj inputObject = inputObjects.get(objectID);
        ObjCollection childObjects = inputObject.getChildren(childObjectsName);

        StringBuilder sb = new StringBuilder();
        for (Obj childObject:childObjects.values()){
            if (sb.length() == 0) {
                sb.append(childObject.getID());
            } else {
                sb.append(",").append(childObject.getID());
            }
        }

        return sb.toString();

    }

    @Override
    public String getArgumentsDescription() {
        return "String inputObjectsName, int objectID, String childObjectsName";
    }

    @Override
    public String getDescription() {
        return "Returns a comma-delimited list of child object ID numbers for the specified input object.";
    }
}
