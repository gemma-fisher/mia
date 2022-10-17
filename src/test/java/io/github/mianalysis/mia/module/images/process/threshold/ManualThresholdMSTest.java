package io.github.mianalysis.mia.module.images.process.threshold;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ij.IJ;
import ij.ImagePlus;
import io.github.mianalysis.enums.Dimension;
import io.github.mianalysis.enums.Logic;
import io.github.mianalysis.enums.OutputMode;
import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.module.Modules;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.Workspaces;
import io.github.mianalysis.mia.object.image.Image;
import io.github.mianalysis.mia.object.image.ImageFactory;
import io.github.mianalysis.mia.object.image.ImageType;
import io.github.mianalysis.mia.object.system.Status;

/**
 * Created by Gemma and blueberry George on 17/10/2022.
 */
public class ManualThresholdMSTest extends ModuleTest {

    /**
     * Generates all permutations
     */
    public static Stream<Arguments> dimensionLogicInputProvider() {
        Stream.Builder<Arguments> argumentBuilder = Stream.builder();
        for (Dimension dimension : Dimension.values())
                for (Logic logic : Logic.values())
                    for (OutputMode outputMode : OutputMode.values())
                        for (ImageType imageType : ImageType.values())
                            argumentBuilder.add(Arguments.of(dimension, logic, outputMode, imageType));

        return argumentBuilder.build();

    }
    
    /**
     * Parameterized test run with 8-bit bit depth and all dimensions, all threshold algorithms and all logics. 
     * Parameterized test run with 8-bit bit depth and all dimensions, all threshold
     * algorithms and all logics.
     * The reduced testing here is to keep storage requirements down.
     * 
     * @throws UnsupportedEncodingException
     */
    @ParameterizedTest
    @MethodSource("dimensionLogicInputProvider")
    void testThreshold0(Dimension dimension, Logic logic, OutputMode outputMode, ImageType imageType)
            throws UnsupportedEncodingException {
        runTest(dimension, logic, 0, outputMode, imageType);

    }

    /**
     * Performs the test
     * 
     * @throws UnsupportedEncodingException
     */
    public static void runTest(Dimension dimension, Logic logic, int threshold, OutputMode outputMode, ImageType imageType)
            throws UnsupportedEncodingException {
                boolean applyToInput = outputMode.equals(OutputMode.APPLY_TO_INPUT);
        // Checks input image and expected images are available. If not found, the test
        // skips
        String inputName = "/msimages/noisygradient/NoisyGradient_" + dimension + "_B8.zip";
        assumeTrue(ManualThresholdMSTest.class.getResource(inputName) != null);

        String expectedName = "/msimages/manualthreshold/MThreshold_" + dimension + "_B8_" + logic + "_V" + threshold
                + ".zip";
        assumeTrue(ManualThresholdMSTest.class.getResource(expectedName) != null);

        // Doing the main part of the test
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null, 1);

        // Loading the test image and adding to workspace
        String inputPath = URLDecoder.decode(ManualThresholdMSTest.class.getResource(inputName).getPath(), "UTF-8");
        ImagePlus ipl = IJ.openImage(inputPath);
        Image image = ImageFactory.createImage("Test_image", ipl, imageType);
        workspace.addImage(image);

        String expectedPath = URLDecoder.decode(ManualThresholdMSTest.class.getResource(expectedName).getPath(),
                "UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(expectedPath), imageType);

        // Initialising module and setting parameters
        ManualThreshold module = new ManualThreshold(new Modules());
        module.updateParameterValue(ManualThreshold.INPUT_IMAGE, "Test_image");
        module.updateParameterValue(ManualThreshold.APPLY_TO_INPUT, applyToInput);
        module.updateParameterValue(ManualThreshold.OUTPUT_IMAGE, "Test_output");
        module.updateParameterValue(ManualThreshold.THRESHOLD_SOURCE, ManualThreshold.ThresholdSources.FIXED_VALUE);
        module.updateParameterValue(ManualThreshold.THRESHOLD_VALUE, threshold);

        switch (logic) {
            case LB:
                module.updateParameterValue(ManualThreshold.BINARY_LOGIC,
                        ManualThreshold.BinaryLogic.BLACK_BACKGROUND);
                break;
            case LW:
                module.updateParameterValue(ManualThreshold.BINARY_LOGIC,
                        ManualThreshold.BinaryLogic.WHITE_BACKGROUND);
                break;
        }

        // Running Module
        Status status = module.execute(workspace);
        assertEquals(Status.PASS, status);

        // Checking the images in the workspace
        if (applyToInput) {
            assertEquals(1, workspace.getImages().size());
            assertNotNull(workspace.getImage("Test_image"));

            Image outputImage = workspace.getImage("Test_image");
            assertEquals(expectedImage, outputImage);

        } else {
            assertEquals(2, workspace.getImages().size());
            assertNotNull(workspace.getImage("Test_image"));
            assertNotNull(workspace.getImage("Test_output"));

            Image outputImage = workspace.getImage("Test_output");
            assertEquals(expectedImage, outputImage);

        }

    }

    /**
     * Test to check this module has an assigned description
     */
    @Override
    public void testGetHelp() {
        assertNotNull(new ManualThreshold(null).getDescription());
    }

}
