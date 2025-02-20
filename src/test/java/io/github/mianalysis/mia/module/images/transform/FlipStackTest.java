package io.github.mianalysis.mia.module.images.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URLDecoder;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ij.IJ;
import ij.ImagePlus;
import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.Workspaces;
import io.github.mianalysis.mia.object.image.Image;
import io.github.mianalysis.mia.object.image.ImageFactory;
import io.github.mianalysis.mia.object.system.Status;

/**
 * Created by Stephen Cross on 07/03/2019.
 */

public class FlipStackTest extends ModuleTest{

    @Override
    public void testGetHelp() {
        assertNotNull(new FlipStack<>(null).getDescription());
    }


    // TESTING 2D STACKS

    @Test
    public void testRunApplyFlip2D8bitX() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient2D_8bit_X.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.X);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip2D8bitY() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient2D_8bit_Y.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.Y);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip2D8bitC() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.CHANNEL);

        // Running Module
        Status status = flipStack.execute(workspace);
        assertEquals(Status.FAIL,status);

        // Checking the images in the workspace
        assertEquals(1,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));

    }

    @Test
    public void testRunApplyFlip2D8bitZ() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.Z);

        // Running Module
        Status status = flipStack.execute(workspace);
        assertEquals(Status.FAIL,status);

        // Checking the images in the workspace
        assertEquals(1,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));

    }

    @Test
    public void testRunApplyFlip2D8bitT() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.TIME);

        // Running Module
        Status status = flipStack.execute(workspace);
        assertEquals(Status.FAIL,status);

        // Checking the images in the workspace
        assertEquals(1,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));

    }


    // TESTING 3D STACKS

    @Test
    public void testRunApplyFlip3D8bitX() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient3D_8bit_X.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.X);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip3D8bitY() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient3D_8bit_Y.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.Y);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip3D8bitC() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.CHANNEL);

        // Running Module
        Status status = flipStack.execute(workspace);
        assertEquals(Status.FAIL,status);

        // Checking the images in the workspace
        assertEquals(1,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));

    }

    @Test
    public void testRunApplyFlip3D8bitZ() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient3D_8bit_Z.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.Z);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip3D8bitT() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.TIME);

        // Running Module
        Status status = flipStack.execute(workspace);
        assertEquals(Status.FAIL,status);

        // Checking the images in the workspace
        assertEquals(1,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));

    }


    // TESTING 4D STACKS

    @Test
    public void testRunApplyFlip4D8bitX() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient4D_CT_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient4D_CT_8bit_X.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.X);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip4D8bitY() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient4D_CT_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient4D_CT_8bit_Y.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.Y);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip4DCT8bitC() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient4D_CT_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient4D_CT_8bit_C.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.CHANNEL);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip4DCZ8bitC() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient4D_CZ_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient4D_CZ_8bit_C.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.CHANNEL);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip4DZT8bitC() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient4D_ZT_8bit_C1.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.CHANNEL);

        // Running Module
        Status status = flipStack.execute(workspace);
        assertEquals(Status.FAIL,status);

        // Checking the images in the workspace
        assertEquals(1,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));

    }

    @Test @Disabled
    public void testRunApplyFlip4DZT8bitZ() throws Exception {
    }

    @Test @Disabled
    public void testRunApplyFlip4DCZ8bitZ() throws Exception {
    }

    @Test @Disabled
    public void testRunApplyFlip4DCT8bitZ() throws Exception {
    }

    @Test @Disabled
    public void testRunApplyFlip4DZT8bitT() throws Exception {
    }

    @Test @Disabled
    public void testRunApplyFlip4DCZ8bitT() throws Exception {
    }

    @Test @Disabled
    public void testRunApplyFlip4DCT8bitT() throws Exception {
    }


    // TESTING 5D STACKS

    @Test
    public void testRunApplyFlip5D8bitX() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient5D_8bit_X.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.X);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip5D8bitY() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient5D_8bit_Y.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.Y);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip5D8bitC() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient5D_8bit_C.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.CHANNEL);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test @Disabled
    public void testRunApplyFlip5D8bitZ() throws Exception {
    }

    @Test @Disabled
    public void testRunApplyFlip5D8bitT() throws Exception {
    }


    // TESTING OTHER BIT DEPTHS

    @Test
    public void testRunApplyFlip5D16bitX() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_16bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient5D_16bit_X.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.X);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRunApplyFlip5D32bitX() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient5D_32bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient5D_32bit_X.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,false);
        flipStack.updateParameterValue(FlipStack.OUTPUT_IMAGE,"Test_output");
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.X);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);
    }


    // TESTING APPLY TO INPUT IMAGE

    @Test
    public void testRunApplyFlip2D8bitXApplyToInput() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient2D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image inputImage = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(inputImage);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/flipstack/NoisyGradient2D_8bit_X.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        FlipStack flipStack = new FlipStack(null);
        flipStack.updateParameterValue(FlipStack.INPUT_IMAGE,"Test_image");
        flipStack.updateParameterValue(FlipStack.APPLY_TO_INPUT,true);
        flipStack.updateParameterValue(FlipStack.AXIS_MODE,FlipStack.AxisModes.X);

        // Running Module
        flipStack.execute(workspace);

        // Checking the images in the workspace
        assertEquals(1,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_image");
        assertEquals(expectedImage,outputImage);
    }

}