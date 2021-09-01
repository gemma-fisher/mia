package io.github.mianalysis.MIA.Macro.General;

import io.github.mianalysis.MIA.Macro.MacroOperationTest;

import static org.junit.jupiter.api.Assertions.*;

public class MIA_ClearWorkspaceTest extends MacroOperationTest {
    @Override
    public void testGetName() {
        assertNotNull(new MIA_ClearWorkspace(null).getName());
    }

    @Override
    public void testGetArgumentsDescription() {
        assertNotNull(new MIA_ClearWorkspace(null).getArgumentsDescription());
    }

    @Override
    public void testGetDescription() {
        assertNotNull(new MIA_ClearWorkspace(null).getDescription());
    }
}