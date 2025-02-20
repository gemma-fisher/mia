package io.github.mianalysis.mia.module.objects.measure.spatial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ome.units.UNITS;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.module.objects.measure.spatial.MeasureObjectOverlap;
import io.github.mianalysis.mia.object.Obj;
import io.github.mianalysis.mia.object.Objs;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.Workspaces;
import io.github.sjcross.sjcommon.object.volume.PointOutOfRangeException;
import io.github.sjcross.sjcommon.object.volume.SpatCal;
import io.github.sjcross.sjcommon.object.volume.VolumeType;


public class MeasureObjectOverlapTest extends ModuleTest {

    private double tolerance = 1E-2;

    @BeforeAll
    public static void setVerbose() {
        Module.setVerbose(false);
    }

    @Override
    public void testGetHelp() {
        assertNotNull(new MeasureObjectOverlap(null).getDescription());
    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetNOverlappingPointsNoOverlap(VolumeType volumeType) throws PointOutOfRangeException {
        // Setting object parameters
        String objectsName1 = "Test objects 1";
        String objectsName2 = "Test objects 2";

        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";
        SpatCal calibration = new SpatCal(dppXY,dppZ,calibratedUnits,30,50,50);

        // Creating a single test object
        Objs objects1 = new Objs(objectsName1,calibration,1,0.02,UNITS.SECOND);
        Obj object1_1 = objects1.createAndAddNewObject(volumeType,1);
        object1_1.add(10,12,32);
        object1_1.add(11,12,32);
        object1_1.add(10,13,32);
        object1_1.add(10,14,32);
        object1_1.add(11,13,32);
        object1_1.add(10,12,33);
        object1_1.add(10,13,33);
        object1_1.add(11,12,33);
        object1_1.add(11,13,34);
        object1_1.add(10,12,34);

        // Creating a collection of multiple objects to test against
        Objs objects2 = new Objs(objectsName2,calibration,1,0.02,UNITS.SECOND);
        Obj object2_1 = objects2.createAndAddNewObject(volumeType,1);
        object2_1.add(20,12,32);
        object2_1.add(20,11,32);
        object2_1.add(20,12,33);
        object2_1.add(19,12,32);

        Obj object2_2 = objects2.createAndAddNewObject(volumeType,2);
        object2_2.add(20,22,32);
        object2_2.add(20,21,32);
        object2_2.add(20,22,33);
        object2_2.add(19,22,32);

        Obj object2_3 = objects2.createAndAddNewObject(volumeType,3);
        object2_3.add(10,22,32);
        object2_3.add(10,21,32);
        object2_3.add(10,22,33);
        object2_3.add(9,22,32);

        int actual = MeasureObjectOverlap.getNOverlappingPoints(object1_1,objects2,false);
        int expected = 0;

        assertEquals(expected,actual);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetNOverlappingPointsPartialSingleObjectOverlap(VolumeType volumeType) throws PointOutOfRangeException {
        // Setting object parameters
        String objectsName1 = "Test objects 1";
        String objectsName2 = "Test objects 2";

        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";
        SpatCal calibration = new SpatCal(dppXY,dppZ,calibratedUnits,30,50,50);

        // Creating a single test object
        Objs objects1 = new Objs(objectsName1,calibration,1,0.02,UNITS.SECOND);
        Obj object1_1 = objects1.createAndAddNewObject(volumeType,1);
        object1_1.add(10,12,32);
        object1_1.add(11,12,32);
        object1_1.add(10,13,32);
        object1_1.add(10,14,32);
        object1_1.add(11,13,32);
        object1_1.add(10,12,33);
        object1_1.add(10,13,33);
        object1_1.add(11,12,33);
        object1_1.add(11,13,34);
        object1_1.add(10,12,34);

        // Creating a collection of multiple objects to test against
        Objs objects2 = new Objs(objectsName2,calibration,1,0.02,UNITS.SECOND);
        Obj object2_1 = objects2.createAndAddNewObject(volumeType,1);
        object2_1.add(20,12,32);
        object2_1.add(20,11,32);
        object2_1.add(20,12,33);
        object2_1.add(19,12,32);

        Obj object2_2 = objects2.createAndAddNewObject(volumeType,2);
        object2_2.add(9,13,32);
        object2_2.add(9,14,32);
        object2_2.add(10,14,32);
        object2_2.add(11,13,32);
        object2_2.add(10,12,33);
        object2_2.add(10,13,33);
        object2_2.add(9,13,33);

        Obj object2_3 = objects2.createAndAddNewObject(volumeType,3);
        object2_3.add(10,22,32);
        object2_3.add(10,21,32);
        object2_3.add(10,22,33);
        object2_3.add(9,22,32);

        int actual = MeasureObjectOverlap.getNOverlappingPoints(object1_1,objects2,false);
        int expected = 4;

        assertEquals(expected,actual);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetNOverlappingPointsPartialMultipleObjectOverlap(VolumeType volumeType) throws PointOutOfRangeException {
        // Setting object parameters
        String objectsName1 = "Test objects 1";
        String objectsName2 = "Test objects 2";

        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";
        SpatCal calibration = new SpatCal(dppXY,dppZ,calibratedUnits,30,50,50);

        // Creating a single test object
        Objs objects1 = new Objs(objectsName1, calibration, 1, 0.02, UNITS.SECOND);
        Obj object1_1 = objects1.createAndAddNewObject(volumeType,1);
        object1_1.add(10,12,32);
        object1_1.add(11,12,32);
        object1_1.add(10,13,32);
        object1_1.add(10,14,32);
        object1_1.add(11,13,32);
        object1_1.add(10,12,33);
        object1_1.add(10,13,33);
        object1_1.add(11,12,33);
        object1_1.add(11,13,34);
        object1_1.add(10,12,34);

        // Creating a collection of multiple objects to test against
        Objs objects2 = new Objs(objectsName2,calibration,1,0.02,UNITS.SECOND);
        Obj object2_1 = objects2.createAndAddNewObject(volumeType,1);
        object2_1.add(20,12,32);
        object2_1.add(20,11,32);
        object2_1.add(20,12,33);
        object2_1.add(19,12,32);

        Obj object2_2 = objects2.createAndAddNewObject(volumeType,2);
        object2_2.add(9,13,32);
        object2_2.add(9,14,32);
        object2_2.add(10,14,32);
        object2_2.add(11,13,32);
        object2_2.add(10,12,33);
        object2_2.add(10,13,33);
        object2_2.add(9,13,33);

        Obj object2_3 = objects2.createAndAddNewObject(volumeType,3);
        object2_3.add(9,13,33);
        object2_3.add(11,12,33);
        object2_3.add(11,13,34);
        object2_3.add(10,12,34);
        object2_3.add(10,12,35);

        int actual = MeasureObjectOverlap.getNOverlappingPoints(object1_1,objects2,false);
        int expected = 7;

        assertEquals(expected,actual);

    }

    /**
     * In this test, two of the test objects share the same pixel.  This shouldn't lead to an increase in the overlap
     * volume of the main object.
     * @throws PointOutOfRangeException
     */
    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetNOverlappingPointsPartialMultipleObjectOverlapWithInternalClash(VolumeType volumeType) throws PointOutOfRangeException {
        // Setting object parameters
        String objectsName1 = "Test objects 1";
        String objectsName2 = "Test objects 2";

        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";
        SpatCal calibration = new SpatCal(dppXY,dppZ,calibratedUnits,30,50,50);

        // Creating a single test object
        Objs objects1 = new Objs(objectsName1,calibration,1,0.02,UNITS.SECOND);
        Obj object1_1 = objects1.createAndAddNewObject(volumeType,1);
        object1_1.add(10,12,32);
        object1_1.add(11,12,32);
        object1_1.add(10,13,32);
        object1_1.add(10,14,32);
        object1_1.add(11,13,32);
        object1_1.add(10,12,33);
        object1_1.add(10,13,33);
        object1_1.add(11,12,33);
        object1_1.add(11,13,34);
        object1_1.add(10,12,34);

        // Creating a collection of multiple objects to test against
        Objs objects2 = new Objs(objectsName2,calibration,1,0.02,UNITS.SECOND);
        Obj object2_1 = objects2.createAndAddNewObject(volumeType,1);
        object2_1.add(20,12,32);
        object2_1.add(20,11,32);
        object2_1.add(20,12,33);
        object2_1.add(19,12,32);

        Obj object2_2 = objects2.createAndAddNewObject(volumeType,2);
        object2_2.add(9,13,32);
        object2_2.add(9,14,32);
        object2_2.add(10,14,32);
        object2_2.add(11,13,32);
        object2_2.add(10,12,33);
        object2_2.add(10,13,33);
        object2_2.add(9,13,33);

        Obj object2_3 = objects2.createAndAddNewObject(volumeType,3);
        object2_3.add(9,13,33);
        object2_3.add(10,13,33);
        object2_3.add(11,12,33);
        object2_3.add(11,13,34);
        object2_3.add(10,12,34);
        object2_3.add(10,12,35);

        int actual = MeasureObjectOverlap.getNOverlappingPoints(object1_1,objects2,false);
        int expected = 7;

        assertEquals(expected,actual);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testGetNOverlappingPointsTotalOverlap(VolumeType volumeType) throws PointOutOfRangeException {
// Setting object parameters
        String objectsName1 = "Test objects 1";
        String objectsName2 = "Test objects 2";

        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";
        SpatCal calibration = new SpatCal(dppXY,dppZ,calibratedUnits,30,50,50);

        // Creating a single test object
        Objs objects1 = new Objs(objectsName1,calibration,1,0.02,UNITS.SECOND);
        Obj object1_1 = objects1.createAndAddNewObject(volumeType,1);
        object1_1.add(10,12,32);
        object1_1.add(11,12,32);
        object1_1.add(10,13,32);
        object1_1.add(10,14,32);
        object1_1.add(11,13,32);
        object1_1.add(10,12,33);
        object1_1.add(10,13,33);
        object1_1.add(11,12,33);
        object1_1.add(11,13,34);
        object1_1.add(10,12,34);

        // Creating a collection of multiple objects to test against
        Objs objects2 = new Objs(objectsName2,calibration,1,0.02,UNITS.SECOND);
        Obj object2_1 = objects2.createAndAddNewObject(volumeType,1);
        object2_1.add(20,12,32);
        object2_1.add(20,11,32);
        object2_1.add(20,12,33);
        object2_1.add(19,12,32);

        Obj object2_2 = objects2.createAndAddNewObject(volumeType,2);
        object2_2.add(10,12,32);
        object2_2.add(11,12,32);
        object2_2.add(10,13,32);
        object2_2.add(9,13,32);
        object2_2.add(9,14,32);
        object2_2.add(10,14,32);
        object2_2.add(11,13,32);
        object2_2.add(10,12,33);
        object2_2.add(10,13,33);
        object2_2.add(9,13,33);

        Obj object2_3 = objects2.createAndAddNewObject(volumeType,3);
        object2_3.add(9,13,33);
        object2_3.add(10,13,33);
        object2_3.add(11,12,33);
        object2_3.add(11,13,34);
        object2_3.add(10,12,34);
        object2_3.add(10,12,35);

        int actual = MeasureObjectOverlap.getNOverlappingPoints(object1_1,objects2,false);
        int expected = 10;

        assertEquals(expected,actual);
    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testRunNoOverlap(VolumeType volumeType) throws PointOutOfRangeException {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting object parameters
        String objectsName1 = "Test objects 1";
        String objectsName2 = "Test objects 2";

        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";
        SpatCal calibration = new SpatCal(dppXY,dppZ,calibratedUnits,30,50,50);
        
        // Creating a single test object
        Objs objects1 = new Objs(objectsName1,calibration,1,0.02,UNITS.SECOND);
        Obj object1_1 = objects1.createAndAddNewObject(volumeType,1);
        object1_1.add(10,12,32);
        object1_1.add(11,12,32);
        object1_1.add(10,13,32);
        object1_1.add(10,14,32);
        object1_1.add(11,13,32);
        object1_1.add(10,12,33);
        object1_1.add(10,13,33);
        object1_1.add(11,12,33);
        object1_1.add(11,13,34);
        object1_1.add(10,12,34);

        // Creating a collection of multiple objects to test against
        Objs objects2 = new Objs(objectsName2,calibration,1,0.02,UNITS.SECOND);
        Obj object2_1 = objects2.createAndAddNewObject(volumeType,1);
        object2_1.add(20,12,32);
        object2_1.add(20,11,32);
        object2_1.add(20,12,33);
        object2_1.add(19,12,32);

        Obj object2_2 = objects2.createAndAddNewObject(volumeType,2);
        object2_2.add(20,22,32);
        object2_2.add(20,21,32);
        object2_2.add(20,22,33);
        object2_2.add(19,22,32);

        Obj object2_3 = objects2.createAndAddNewObject(volumeType,3);
        object2_3.add(10,22,32);
        object2_3.add(10,21,32);
        object2_3.add(10,22,33);
        object2_3.add(9,22,32);

        workspace.addObjects(objects1);
        workspace.addObjects(objects2);

        // Initialising MeasureObjectOverlap
        MeasureObjectOverlap measureObjectOverlap = new MeasureObjectOverlap(null);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_1,objectsName1);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_2,objectsName2);

        // Running MeasureObjectOverlap
        measureObjectOverlap.execute(workspace);

        // Getting the measurement for each object and checking it is as expected
        String measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_1);
        double actual = object1_1.getMeasurement(measurementName).getValue();
        double expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_VOX_1);
        actual = object1_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testRunPartialSingleObjectOverlap(VolumeType volumeType) throws PointOutOfRangeException {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting object parameters
        String objectsName1 = "Test objects 1";
        String objectsName2 = "Test objects 2";

        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";
        SpatCal calibration = new SpatCal(dppXY,dppZ,calibratedUnits,30,50,50);

        // Creating a single test object
        Objs objects1 = new Objs(objectsName1,calibration,1,0.02,UNITS.SECOND);
        Obj object1_1 = objects1.createAndAddNewObject(volumeType,1);
        object1_1.add(10,12,32);
        object1_1.add(11,12,32);
        object1_1.add(10,13,32);
        object1_1.add(10,14,32);
        object1_1.add(11,13,32);
        object1_1.add(10,12,33);
        object1_1.add(10,13,33);
        object1_1.add(11,12,33);
        object1_1.add(11,13,34);
        object1_1.add(10,12,34);

        // Creating a collection of multiple objects to test against
        Objs objects2 = new Objs(objectsName2,calibration,1,0.02,UNITS.SECOND);
        Obj object2_1 = objects2.createAndAddNewObject(volumeType,1);
        object2_1.add(20,12,32);
        object2_1.add(20,11,32);
        object2_1.add(20,12,33);
        object2_1.add(19,12,32);

        Obj object2_2 = objects2.createAndAddNewObject(volumeType,2);
        object2_2.add(9,13,32);
        object2_2.add(9,14,32);
        object2_2.add(10,14,32);
        object2_2.add(11,13,32);
        object2_2.add(10,12,33);
        object2_2.add(10,13,33);
        object2_2.add(9,13,33);

        Obj object2_3 = objects2.createAndAddNewObject(volumeType,3);
        object2_3.add(10,22,32);
        object2_3.add(10,21,32);
        object2_3.add(10,22,33);
        object2_3.add(9,22,32);

        workspace.addObjects(objects1);
        workspace.addObjects(objects2);

        // Initialising MeasureObjectOverlap
        MeasureObjectOverlap measureObjectOverlap = new MeasureObjectOverlap(null);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_1,objectsName1);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_2,objectsName2);

        // Running MeasureObjectOverlap
        measureObjectOverlap.execute(workspace);

        // Getting the measurement for each object and checking it is as expected
        String measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_1);
        double actual = object1_1.getMeasurement(measurementName).getValue();
        double expected = 40;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_VOX_1);
        actual = object1_1.getMeasurement(measurementName).getValue();
        expected = 4;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 57.14;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 4;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testRunPartialMultipleObjectOverlap(VolumeType volumeType) throws PointOutOfRangeException {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting object parameters
        String objectsName1 = "Test objects 1";
        String objectsName2 = "Test objects 2";

        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";
        SpatCal calibration = new SpatCal(dppXY,dppZ,calibratedUnits,30,50,50);

        // Creating a single test object
        Objs objects1 = new Objs(objectsName1,calibration,1,0.02,UNITS.SECOND);
        Obj object1_1 = objects1.createAndAddNewObject(volumeType,1);
        object1_1.add(10,12,32);
        object1_1.add(11,12,32);
        object1_1.add(10,13,32);
        object1_1.add(10,14,32);
        object1_1.add(11,13,32);
        object1_1.add(10,12,33);
        object1_1.add(10,13,33);
        object1_1.add(11,12,33);
        object1_1.add(11,13,34);
        object1_1.add(10,12,34);

        // Creating a collection of multiple objects to test against
        Objs objects2 = new Objs(objectsName2,calibration,1,0.02,UNITS.SECOND);
        Obj object2_1 = objects2.createAndAddNewObject(volumeType,1);
        object2_1.add(20,12,32);
        object2_1.add(20,11,32);
        object2_1.add(20,12,33);
        object2_1.add(19,12,32);

        Obj object2_2 = objects2.createAndAddNewObject(volumeType,2);
        object2_2.add(9,13,32);
        object2_2.add(9,14,32);
        object2_2.add(10,14,32);
        object2_2.add(11,13,32);
        object2_2.add(10,12,33);
        object2_2.add(10,13,33);
        object2_2.add(9,13,33);

        Obj object2_3 = objects2.createAndAddNewObject(volumeType,3);
        object2_3.add(9,13,33);
        object2_3.add(11,12,33);
        object2_3.add(11,13,34);
        object2_3.add(10,12,34);
        object2_3.add(10,12,35);

        workspace.addObjects(objects1);
        workspace.addObjects(objects2);

        // Initialising MeasureObjectOverlap
        MeasureObjectOverlap measureObjectOverlap = new MeasureObjectOverlap(null);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_1,objectsName1);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_2,objectsName2);

        // Running MeasureObjectOverlap
        measureObjectOverlap.execute(workspace);

        // Getting the measurement for each object and checking it is as expected
        String measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_1);
        double actual = object1_1.getMeasurement(measurementName).getValue();
        double expected = 70;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_VOX_1);
        actual = object1_1.getMeasurement(measurementName).getValue();
        expected = 7;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 57.14;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 4;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 60;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 3;
        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testRunPartialMultipleObjectOverlapWithInternalClash(VolumeType volumeType) throws PointOutOfRangeException {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting object parameters
        String objectsName1 = "Test objects 1";
        String objectsName2 = "Test objects 2";

        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";
        SpatCal calibration = new SpatCal(dppXY,dppZ,calibratedUnits,30,50,50);

        // Creating a single test object
        Objs objects1 = new Objs(objectsName1,calibration,1,0.02,UNITS.SECOND);
        Obj object1_1 = objects1.createAndAddNewObject(volumeType,1);
        object1_1.add(10,12,32);
        object1_1.add(11,12,32);
        object1_1.add(10,13,32);
        object1_1.add(10,14,32);
        object1_1.add(11,13,32);
        object1_1.add(10,12,33);
        object1_1.add(10,13,33);
        object1_1.add(11,12,33);
        object1_1.add(11,13,34);
        object1_1.add(10,12,34);

        // Creating a collection of multiple objects to test against
        Objs objects2 = new Objs(objectsName2,calibration,1,0.02,UNITS.SECOND);
        Obj object2_1 = objects2.createAndAddNewObject(volumeType,1);
        object2_1.add(20,12,32);
        object2_1.add(20,11,32);
        object2_1.add(20,12,33);
        object2_1.add(19,12,32);

        Obj object2_2 = objects2.createAndAddNewObject(volumeType,2);
        object2_2.add(9,13,32);
        object2_2.add(9,14,32);
        object2_2.add(10,14,32);
        object2_2.add(11,13,32);
        object2_2.add(10,12,33);
        object2_2.add(10,13,33);
        object2_2.add(9,13,33);

        Obj object2_3 = objects2.createAndAddNewObject(volumeType,3);
        object2_3.add(9,13,33);
        object2_3.add(10,13,33);
        object2_3.add(11,12,33);
        object2_3.add(11,13,34);
        object2_3.add(10,12,34);
        object2_3.add(10,12,35);

        workspace.addObjects(objects1);
        workspace.addObjects(objects2);

        // Initialising MeasureObjectOverlap
        MeasureObjectOverlap measureObjectOverlap = new MeasureObjectOverlap(null);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_1,objectsName1);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_2,objectsName2);

        // Running MeasureObjectOverlap
        measureObjectOverlap.execute(workspace);

        // Getting the measurement for each object and checking it is as expected
        String measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_1);
        double actual = object1_1.getMeasurement(measurementName).getValue();
        double expected = 70;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_VOX_1);
        actual = object1_1.getMeasurement(measurementName).getValue();
        expected = 7;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 57.14;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 4;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 66.67;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 4;
        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testRunTotalOverlap(VolumeType volumeType) throws PointOutOfRangeException {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting object parameters
        String objectsName1 = "Test objects 1";
        String objectsName2 = "Test objects 2";

        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";
        SpatCal calibration = new SpatCal(dppXY,dppZ,calibratedUnits,30,50,50);

        // Creating a single test object
        Objs objects1 = new Objs(objectsName1,calibration,1,0.02,UNITS.SECOND);
        Obj object1_1 = objects1.createAndAddNewObject(volumeType,1);
        object1_1.add(10,12,32);
        object1_1.add(11,12,32);
        object1_1.add(10,13,32);
        object1_1.add(10,14,32);
        object1_1.add(11,13,32);
        object1_1.add(10,12,33);
        object1_1.add(10,13,33);
        object1_1.add(11,12,33);
        object1_1.add(11,13,34);
        object1_1.add(10,12,34);

        // Creating a collection of multiple objects to test against
        Objs objects2 = new Objs(objectsName2,calibration,1,0.02,UNITS.SECOND);
        Obj object2_1 = objects2.createAndAddNewObject(volumeType,1);
        object2_1.add(20,12,32);
        object2_1.add(20,11,32);
        object2_1.add(20,12,33);
        object2_1.add(19,12,32);

        Obj object2_2 = objects2.createAndAddNewObject(volumeType,2);
        object2_2.add(10,12,32);
        object2_2.add(11,12,32);
        object2_2.add(10,13,32);
        object2_2.add(9,13,32);
        object2_2.add(9,14,32);
        object2_2.add(10,14,32);
        object2_2.add(11,13,32);
        object2_2.add(10,12,33);
        object2_2.add(10,13,33);
        object2_2.add(9,13,33);

        Obj object2_3 = objects2.createAndAddNewObject(volumeType,3);
        object2_3.add(9,13,33);
        object2_3.add(10,13,33);
        object2_3.add(11,12,33);
        object2_3.add(11,13,34);
        object2_3.add(10,12,34);
        object2_3.add(10,12,35);

        workspace.addObjects(objects1);
        workspace.addObjects(objects2);

        // Initialising MeasureObjectOverlap
        MeasureObjectOverlap measureObjectOverlap = new MeasureObjectOverlap(null);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_1,objectsName1);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_2,objectsName2);

        // Running MeasureObjectOverlap
        measureObjectOverlap.execute(workspace);

        // Getting the measurement for each object and checking it is as expected
        String measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_1);
        double actual = object1_1.getMeasurement(measurementName).getValue();
        double expected = 100;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_VOX_1);
        actual = object1_1.getMeasurement(measurementName).getValue();
        expected = 10;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 70;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 7;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 66.67;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 4;
        assertEquals(expected,actual,tolerance);

    }

    @ParameterizedTest
    @EnumSource(VolumeType.class)
    public void testRunPartialMultipleObjectOverlapMultipleTimepoints(VolumeType volumeType) throws PointOutOfRangeException {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Setting object parameters
        String objectsName1 = "Test objects 1";
        String objectsName2 = "Test objects 2";

        double dppXY = 0.02;
        double dppZ = 0.1;
        String calibratedUnits = "µm";
        SpatCal calibration = new SpatCal(dppXY,dppZ,calibratedUnits,30,50,50);

        // Creating a single test object
        Objs objects1 = new Objs(objectsName1,calibration,1,0.02,UNITS.SECOND);
        Obj object1_1 = objects1.createAndAddNewObject(volumeType,1);
        object1_1.setT(2);
        object1_1.add(10,12,32);
        object1_1.add(11,12,32);
        object1_1.add(10,13,32);
        object1_1.add(10,14,32);
        object1_1.add(11,13,32);
        object1_1.add(10,12,33);
        object1_1.add(10,13,33);
        object1_1.add(11,12,33);
        object1_1.add(11,13,34);
        object1_1.add(10,12,34);

        // Creating a collection of multiple objects to test against
        Objs objects2 = new Objs(objectsName2,calibration,1,0.02,UNITS.SECOND);
        Obj object2_1 = objects2.createAndAddNewObject(volumeType,1);
        object2_1.setT(2);
        object2_1.add(20,12,32);
        object2_1.add(20,11,32);
        object2_1.add(20,12,33);
        object2_1.add(19,12,32);

        Obj object2_2 = objects2.createAndAddNewObject(volumeType,2);
        object2_2.setT(3);
        object2_2.add(9,13,32);
        object2_2.add(9,14,32);
        object2_2.add(10,14,32);
        object2_2.add(11,13,32);
        object2_2.add(10,12,33);
        object2_2.add(10,13,33);
        object2_2.add(9,13,33);

        Obj object2_3 = objects2.createAndAddNewObject(volumeType,3);
        object2_3.setT(2);
        object2_3.add(9,13,33);
        object2_3.add(11,12,33);
        object2_3.add(11,13,34);
        object2_3.add(10,12,34);
        object2_3.add(10,12,35);

        workspace.addObjects(objects1);
        workspace.addObjects(objects2);

        // Initialising MeasureObjectOverlap
        MeasureObjectOverlap measureObjectOverlap = new MeasureObjectOverlap(null);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_1,objectsName1);
        measureObjectOverlap.updateParameterValue(MeasureObjectOverlap.OBJECT_SET_2,objectsName2);

        // Running MeasureObjectOverlap
        measureObjectOverlap.execute(workspace);

        // Getting the measurement for each object and checking it is as expected
        String measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_1);
        double actual = object1_1.getMeasurement(measurementName).getValue();
        double expected = 30;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName2,MeasureObjectOverlap.Measurements.OVERLAP_VOX_1);
        actual = object1_1.getMeasurement(measurementName).getValue();
        expected = 3;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_1.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_2.getMeasurement(measurementName).getValue();
        expected = 0;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_PERCENT_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 60;
        assertEquals(expected,actual,tolerance);

        measurementName = MeasureObjectOverlap.getFullName(objectsName1,MeasureObjectOverlap.Measurements.OVERLAP_VOX_2);
        actual = object2_3.getMeasurement(measurementName).getValue();
        expected = 3;
        assertEquals(expected,actual,tolerance);

    }
}