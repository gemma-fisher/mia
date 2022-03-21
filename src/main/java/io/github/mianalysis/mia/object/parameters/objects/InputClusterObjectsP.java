package io.github.mianalysis.mia.object.parameters.objects;

import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.object.parameters.InputObjectsP;

import com.drew.lang.annotations.NotNull;
import java.util.LinkedHashSet;

public class InputClusterObjectsP extends InputObjectsP {
    public InputClusterObjectsP(String name, Module module) {
        super(name, module);
    }

    public InputClusterObjectsP(String name, Module module, @NotNull String choice) {
        super(name, module, choice);
    }

    public InputClusterObjectsP(String name, Module module, @NotNull String choice, String description) {
        super(name, module, choice, description);
    }

    @Override
    public String[] getChoices() {
        LinkedHashSet<OutputObjectsP> objects = module.getModules().getAvailableObjects(module,OutputClusterObjectsP.class);
        return objects.stream().map(OutputObjectsP::getObjectsName).distinct().toArray(String[]::new);
    }
}
