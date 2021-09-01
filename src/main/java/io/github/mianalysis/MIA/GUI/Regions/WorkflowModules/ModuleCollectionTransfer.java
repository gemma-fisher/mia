package io.github.mianalysis.MIA.GUI.Regions.WorkflowModules;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import io.github.mianalysis.MIA.Module.ModuleCollection;

public class ModuleCollectionTransfer implements Transferable, ClipboardOwner {
    private ModuleCollection modules;

    public ModuleCollectionTransfer(ModuleCollection modules) {
        this.modules = modules;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        try {
            DataFlavor dataFlavor = new ModuleCollectionDataFlavor();
            return new DataFlavor[]{dataFlavor};

        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            return new DataFlavor[0];
        }
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) {
        return modules;
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }
}
