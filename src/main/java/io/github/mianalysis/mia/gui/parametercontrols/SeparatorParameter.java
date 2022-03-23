package io.github.mianalysis.mia.gui.parametercontrols;

import io.github.mianalysis.mia.object.Colours;
import io.github.mianalysis.mia.object.parameters.SeparatorP;

import javax.swing.*;
import java.awt.*;

public class SeparatorParameter extends ParameterControl {
    protected JPanel control;

    public SeparatorParameter(SeparatorP parameter) {
        super(parameter);

        control = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;

        JSeparator separatorLeft = new JSeparator();
        separatorLeft.setForeground(Colours.DARK_BLUE);
        c.weightx = 1;
        c.gridx++;
        c.insets = new Insets(0,0,0,5);
        control.add(separatorLeft,c);

        JLabel label = new JLabel();
        label.setText(parameter.getNickname());
        label.setForeground(Colours.DARK_BLUE);
        c.weightx = 0;
        c.gridx++;
        c.insets = new Insets(0,0,0,0);
        control.add(label,c);

        JSeparator separatorRight = new JSeparator();
        separatorRight.setForeground(Colours.DARK_BLUE);
        c.weightx = 1;
        c.gridx++;
        c.insets = new Insets(0,5,0,0);
        control.add(separatorRight,c);

    }

    @Override
    public JComponent getComponent() {
        return control;
    }

    @Override
    public void updateControl() {

    }
}
