package io.github.mianalysis.mia.module.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import io.github.mianalysis.mia.expectedobjects.ExpectedObjects;
import io.github.mianalysis.mia.expectedobjects.Objects3D;
import io.github.mianalysis.mia.expectedobjects.Spots3D;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.module.system.RemoveObjects;
import io.github.mianalysis.mia.object.Objs;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.Workspaces;
import io.github.mianalysis.mia.object.parameters.Parameters;
import io.github.mianalysis.mia.object.parameters.ParameterGroup;
import io.github.sjcross.sjcommon.object.volume.VolumeType;


public class RemoveObjectsTest extends ModuleTest {
    @BeforeAll
    public static void setVerbose() {
        Module.setVerbose(false);
    }

    @Override
    public void testGetHelp() {
        assertNotNull(new RemoveObjects(null).getDescription());
    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testRunSingleObjs(VolumeType volumeType) throws Exception{
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Getting test objects
        Objs testObjects = new Objects3D(volumeType).getObjects("TestObj", ExpectedObjects.Mode.EIGHT_BIT,dppXY,dppZ,calibratedUnits,true);
        workspace.addObjects(testObjects);

        // Initialising the module
        RemoveObjects removeObjects = new RemoveObjects(null);
        removeObjects.initialiseParameters();
        ParameterGroup group = removeObjects.getParameter(RemoveObjects.REMOVE_ANOTHER_OBJECT_SET);
        Parameters collection = group.addParameters();
        collection.updateValue(RemoveObjects.INPUT_OBJECTS, "TestObj");
        
        // Running the module
        removeObjects.execute(workspace);

        // Checking the objects have been removed
        assertEquals(0,workspace.getObjects().size());

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testRunMultipleObjss(VolumeType volumeType) throws Exception{
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting calibration parameters
        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";

        // Getting test objects
        Objs testObjects = new Objects3D(volumeType).getObjects("TestObj",ExpectedObjects.Mode.EIGHT_BIT,dppXY,dppZ,calibratedUnits,true);
        workspace.addObjects(testObjects);

        Objs spotObjects = new Spots3D(volumeType).getObjects("SpotObj",ExpectedObjects.Mode.EIGHT_BIT,dppXY,dppZ,calibratedUnits,true);
        workspace.addObjects(spotObjects);

        // Initialising the module
        RemoveObjects removeObjects = new RemoveObjects(null);
        removeObjects.initialiseParameters();
        ParameterGroup group = removeObjects.getParameter(RemoveObjects.REMOVE_ANOTHER_OBJECT_SET);
        Parameters collection = group.addParameters();
        collection.updateValue(RemoveObjects.INPUT_OBJECTS, "TestObj");

        // Running the module
        removeObjects.execute(workspace);

        // Checking the objects have been removed
        assertEquals(1,workspace.getObjects().size());

    }
}