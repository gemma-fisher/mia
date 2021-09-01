package io.github.mianalysis.MIA.Module.Deprecated;

import ij.IJ;
import ij.ImagePlus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.github.mianalysis.MIA.Module.Module;
import io.github.mianalysis.MIA.Module.ModuleTest;
import io.github.mianalysis.MIA.Module.ImageProcessing.Stack.MergeChannels;
import io.github.mianalysis.MIA.Object.Image;

import java.net.URLDecoder;

import static org.junit.jupiter.api.Assertions.*;

public class MergeChannelsTest extends ModuleTest {

    @BeforeAll
    public static void setVerbose() {
        Module.setVerbose(false);
    }

    @Override
    public void testGetHelp() {
        assertNotNull(new MergeChannels<>(null).getDescription());
    }

    @Test
    public void testRun2D8Bit2D8Bit() throws Exception {
        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/LabelledObjects/LabelledObjects2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = new Image("Test_image_1",ipl1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/NoisyGradient/NoisyGradient2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = new Image("Test_image_2",ipl2);

        String pathToExpected = URLDecoder.decode(this.getClass().getResource("/images/MergeChannels/LabObj2D8bit_NoisyGrad2D8bit.tif").getPath(),"UTF-8");
        ImagePlus expectedImagePlus = IJ.openImage(pathToExpected);
        Image expectedImage = new Image("Expected_image",expectedImagePlus);

        // Running the channel merge
        MergeChannels mergeChannels = new MergeChannels(null);
        Image actualImage = mergeChannels.combineImages(image1,image2,"Output_image");

        assertNotNull(actualImage);
        assertEquals(expectedImage,actualImage);

    }

    @Test
    public void testRun3DMultiCh8Bit2D8Bit() throws Exception {
        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/MergeChannels/LabObj2D8bit_NoisyGrad2D8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = new Image("Test_image_1",ipl1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/MeasureColocalisation/ColocalisationChannel1_2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = new Image("Test_image_2",ipl2);

        String pathToExpected = URLDecoder.decode(this.getClass().getResource("/images/MergeChannels/LabObj2D8bit_NoisyGrad2D8bit_ColocCh1_2D_8bit.tif").getPath(),"UTF-8");
        ImagePlus expectedImagePlus = IJ.openImage(pathToExpected);
        Image expectedImage = new Image("Expected_image",expectedImagePlus);

        // Running the channel merge
        MergeChannels mergeChannels = new MergeChannels(null);
        Image actualImage = mergeChannels.combineImages(image1,image2,"Output_image");

        assertNotNull(actualImage);
        assertEquals(expectedImage,actualImage);

    }

    @Test
    public void testRun3D8Bit3D8Bit() throws Exception {
        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/LabelledObjects/LabelledObjects3D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = new Image("Test_image_1",ipl1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/NoisyGradient/NoisyGradient3D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = new Image("Test_image_2",ipl2);

        String pathToExpected = URLDecoder.decode(this.getClass().getResource("/images/MergeChannels/LabObj3D8bit_NoisyGrad3D8bit.tif").getPath(),"UTF-8");
        ImagePlus expectedImagePlus = IJ.openImage(pathToExpected);
        Image expectedImage = new Image("Expected_image",expectedImagePlus);

        // Running the channel merge
        MergeChannels mergeChannels = new MergeChannels(null);
        Image actualImage = mergeChannels.combineImages(image1,image2,"Output_image");

        assertNotNull(actualImage);
        assertEquals(expectedImage,actualImage);

    }

    @Test
    public void testRun3DMultiCh8Bit3D8Bit() throws Exception {
        // Loading the test images and adding to workspace
        String pathToImage1 = URLDecoder.decode(this.getClass().getResource("/images/LabelledObjects/LabelledObjects3D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl1 = IJ.openImage(pathToImage1);
        Image image1 = new Image("Test_image_1",ipl1);

        String pathToImage2 = URLDecoder.decode(this.getClass().getResource("/images/NoisyGradient/NoisyGradient3D_8bit.tif").getPath(),"UTF-8");
        ImagePlus ipl2 = IJ.openImage(pathToImage2);
        Image image2 = new Image("Test_image_2",ipl2);

        String pathToExpected = URLDecoder.decode(this.getClass().getResource("/images/MergeChannels/LabObj3D8bit_NoisyGrad3D8bit.tif").getPath(),"UTF-8");
        ImagePlus expectedImagePlus = IJ.openImage(pathToExpected);
        Image expectedImage = new Image("Expected_image",expectedImagePlus);

        // Running the channel merge
        MergeChannels mergeChannels = new MergeChannels(null);
        Image actualImage = mergeChannels.combineImages(image1,image2,"Output_image");

        assertNotNull(actualImage);
        assertEquals(expectedImage,actualImage);

    }
}