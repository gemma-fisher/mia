package io.github.mianalysis.mia.gui.regions.parameterspanel;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import io.github.mianalysis.mia.MIA;
import io.github.mianalysis.mia.gui.GUI;
import io.github.mianalysis.mia.object.refs.abstrakt.ExportableRef;
import io.github.mianalysis.mia.object.refs.abstrakt.SummaryRef;
import io.github.mianalysis.mia.object.refs.collections.Refs;

public class DisableRefsButton extends JButton implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = -93923037075226123L;

    private static final ImageIcon blackIcon = new ImageIcon(
            DisableRefsButton.class.getResource("/icons/delete-2_black_12px.png"), "");
    private static final ImageIcon greyIcon = new ImageIcon(
            DisableRefsButton.class.getResource("/icons/delete-2_lightgrey_12px.png"), "");

    private Refs<SummaryRef> refs;

    // CONSTRUCTOR

    public DisableRefsButton(Refs<SummaryRef> refs) {
        this.refs = refs;

        setMargin(new Insets(0, 0, 0, 0));
        setFocusPainted(false);
        setSelected(false);
        setName("DisableAllMeasurements");
        setToolTipText("Disable all measurements");
        addActionListener(this);
        if (MIA.preferences.darkThemeEnabled()) {
            setIcon(greyIcon);
        } else {
            setIcon(blackIcon);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GUI.addUndo();

        for (ExportableRef ref : refs.values())
            ref.setExportGlobal(false);

        GUI.updateParameters();

    }
}
