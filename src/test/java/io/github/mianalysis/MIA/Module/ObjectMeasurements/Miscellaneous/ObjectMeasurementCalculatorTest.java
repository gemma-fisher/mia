package io.github.mianalysis.MIA.Module.ObjectMeasurements.Miscellaneous;

import io.github.mianalysis.MIA.Module.ModuleTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Stephen Cross on 19/03/2019.
 */
public class ObjectMeasurementCalculatorTest extends ModuleTest {

    @Override
    public void testGetHelp() {
        assertNotNull(new ObjectMeasurementCalculator(null).getDescription());
    }
}