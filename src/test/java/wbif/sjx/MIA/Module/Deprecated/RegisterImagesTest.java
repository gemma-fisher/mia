package wbif.sjx.MIA.Module.Deprecated;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import wbif.sjx.MIA.Module.ModuleTest;
import wbif.sjx.MIA.Module.ImageProcessing.Stack.RegisterImages;

public class RegisterImagesTest extends ModuleTest {

    @Override
    public void testGetHelp() {
        assertNotNull(new RegisterImages(null).getDescription());
    }
}