package io.github.mianalysis.mia.Object.Parameters.Objects;

import io.github.mianalysis.mia.module.Module;
import io.github.mianalysis.mia.Object.Parameters.Abstract.ObjectNamesType;
import io.github.mianalysis.mia.Object.Parameters.Abstract.Parameter;

import com.drew.lang.annotations.NotNull;

public class RemovedObjectsP extends ObjectNamesType {
    public RemovedObjectsP(String name, Module module) {
        super(name,module);
    }

    public RemovedObjectsP(String name, Module module, @NotNull String choice) {
        super(name,module);
        this.choice = choice;
    }

    public RemovedObjectsP(String name, Module module, @NotNull String choice, String description) {
        super(name,module,description);
        this.choice = choice;
    }

    @Override
    public <T extends Parameter> T duplicate(Module newModule) {
        RemovedObjectsP newParameter = new RemovedObjectsP(name,newModule,choice,getDescription());

        newParameter.setNickname(getNickname());
        newParameter.setVisible(isVisible());
        newParameter.setExported(isExported());

        return (T) newParameter;

    }
}
