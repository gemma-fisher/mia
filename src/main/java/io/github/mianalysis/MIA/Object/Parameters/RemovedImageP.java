package io.github.mianalysis.MIA.Object.Parameters;

import io.github.mianalysis.MIA.Module.Module;
import io.github.mianalysis.MIA.Object.Parameters.Abstract.ImageNamesType;
import io.github.mianalysis.MIA.Object.Parameters.Abstract.Parameter;

import com.drew.lang.annotations.NotNull;

public class RemovedImageP extends ImageNamesType {
    public RemovedImageP(String name, Module module) {
        super(name,module);
    }

    public RemovedImageP(String name, Module module, @NotNull String choice) {
        super(name,module);
        this.choice = choice;
    }

    public RemovedImageP(String name, Module module, @NotNull String choice, String description) {
        super(name,module,description);
        this.choice = choice;
    }

    @Override
    public <T extends Parameter> T duplicate(Module newModule) {
        RemovedImageP newParameter = new RemovedImageP(name,newModule,choice,getDescription());

        newParameter.setNickname(getNickname());
        newParameter.setVisible(isVisible());
        newParameter.setExported(isExported());

        return (T) newParameter;

    }
}
