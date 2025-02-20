package io.github.mianalysis.mia.module.images.process;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URLDecoder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.module.ModuleTest;
import io.github.mianalysis.mia.object.Workspace;
import io.github.mianalysis.mia.object.Workspaces;
import io.github.mianalysis.mia.object.image.Image;
import io.github.mianalysis.mia.object.image.ImageFactory;

/**
 * Created by sc13967 on 26/03/2018.
 */

public class InvertIntensityTest extends ModuleTest {
    @BeforeAll
    public static void setVerbose() {
        Module.setVerbose(false);
    }

    @Override
    public void testGetHelp() {
        assertNotNull(new InvertIntensity(null).getDescription());
    }

    @Test
    public void testRun3D8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/imagemath/NoisyGradient3D_Invert_8bit.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising InvertIntensity
        InvertIntensity invertIntensity = new InvertIntensity(null);
        invertIntensity.initialiseParameters();
        invertIntensity.updateParameterValue(ImageMath.INPUT_IMAGE,"Test_image");
        invertIntensity.updateParameterValue(ImageMath.OUTPUT_IMAGE,"Test_output");
        invertIntensity.updateParameterValue(ImageMath.APPLY_TO_INPUT,false);

        // Running Module
        invertIntensity.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRun3DApplyToInput8bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_8bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/imagemath/NoisyGradient3D_Invert_8bit.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising InvertIntensity
        InvertIntensity invertIntensity = new InvertIntensity(null);
        invertIntensity.initialiseParameters();
        invertIntensity.updateParameterValue(ImageMath.INPUT_IMAGE,"Test_image");
        invertIntensity.updateParameterValue(ImageMath.OUTPUT_IMAGE,"Test_output");
        invertIntensity.updateParameterValue(ImageMath.APPLY_TO_INPUT,true);

        // Running Module
        invertIntensity.execute(workspace);

        // Checking the images in the workspace
        assertEquals(1,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_image");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRun3D16bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_16bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/imagemath/NoisyGradient3D_Invert_16bit.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising InvertIntensity
        InvertIntensity invertIntensity = new InvertIntensity(null);
        invertIntensity.initialiseParameters();
        invertIntensity.updateParameterValue(ImageMath.INPUT_IMAGE,"Test_image");
        invertIntensity.updateParameterValue(ImageMath.OUTPUT_IMAGE,"Test_output");
        invertIntensity.updateParameterValue(ImageMath.APPLY_TO_INPUT,false);

        // Running Module
        invertIntensity.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        assertEquals(expectedImage,outputImage);

    }

    @Test
    public void testRun3D32bit() throws Exception {
        // Creating a new workspace
        Workspaces workspaces = new Workspaces();
        Workspace workspace = workspaces.getNewWorkspace(null,1);

        // Loading the test image and adding to workspace
        String pathToImage = URLDecoder.decode(this.getClass().getResource("/images/noisygradient/NoisyGradient3D_32bit.zip").getPath(),"UTF-8");
        ImagePlus ipl = IJ.openImage(pathToImage);
        Image image = ImageFactory.createImage("Test_image",ipl);
        workspace.addImage(image);

        pathToImage = URLDecoder.decode(this.getClass().getResource("/images/imagemath/NoisyGradient3D_Invert_32bit.zip").getPath(),"UTF-8");
        Image expectedImage = ImageFactory.createImage("Expected", IJ.openImage(pathToImage));

        // Initialising InvertIntensity
        InvertIntensity invertIntensity = new InvertIntensity(null);
        invertIntensity.initialiseParameters();
        invertIntensity.updateParameterValue(ImageMath.INPUT_IMAGE,"Test_image");
        invertIntensity.updateParameterValue(ImageMath.OUTPUT_IMAGE,"Test_output");
        invertIntensity.updateParameterValue(ImageMath.APPLY_TO_INPUT,false);

        // Running Module
        invertIntensity.execute(workspace);

        // Checking the images in the workspace
        assertEquals(2,workspace.getImages().size());
        assertNotNull(workspace.getImage("Test_image"));
        assertNotNull(workspace.getImage("Test_output"));

        // Checking the output image has the expected calibration
        Image outputImage = workspace.getImage("Test_output");
        
        assertEquals(expectedImage,outputImage);

    }
}