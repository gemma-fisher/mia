package io.github.mianalysis.MIA.Object.Parameters;

import io.github.mianalysis.MIA.Module.Module;
import io.github.mianalysis.MIA.Object.Parameters.Abstract.ChoiceType;
import io.github.mianalysis.MIA.Object.Parameters.Abstract.Parameter;

import com.drew.lang.annotations.NotNull;

public class MetadataItemP extends ChoiceType {
    public MetadataItemP(String name, Module module) {
        super(name,module);
    }

    public MetadataItemP(String name, Module module, @NotNull String choice) {
        super(name,module);
        this.choice = choice;
    }

    public MetadataItemP(String name, Module module, @NotNull String choice, String description) {
        super(name,module,description);
        this.choice = choice;
    }

    @Override
    public String[] getChoices() {
        return module.getModules().getMetadataRefs(module).getMetadataNames();
    }

    @Override
    public <T extends Parameter> T duplicate(Module newModule) {
        MetadataItemP newParameter = new MetadataItemP(name,newModule,choice,getDescription());

        newParameter.setNickname(getNickname());
        newParameter.setVisible(isVisible());
        newParameter.setExported(isExported());

        return (T) newParameter;

    }
}
