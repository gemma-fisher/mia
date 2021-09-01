package io.github.mianalysis.MIA.Object.Parameters.Objects;

import io.github.mianalysis.MIA.Module.Module;

import com.drew.lang.annotations.NotNull;

public class OutputSkeletonObjectsP extends OutputObjectsP {
    public OutputSkeletonObjectsP(String name, Module module) {
        super(name, module);
    }

    public OutputSkeletonObjectsP(String name, Module module, @NotNull String objectsName) {
        super(name, module, objectsName);
    }

    public OutputSkeletonObjectsP(String name, Module module, @NotNull String objectsName, String description) {
        super(name, module, objectsName, description);
    }
}
