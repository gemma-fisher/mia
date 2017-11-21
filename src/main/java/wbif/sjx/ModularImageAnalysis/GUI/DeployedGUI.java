package wbif.sjx.ModularImageAnalysis.GUI;

import ij.IJ;
import org.xml.sax.SAXException;
import wbif.sjx.ModularImageAnalysis.Exceptions.GenericMIAException;
import wbif.sjx.ModularImageAnalysis.Module.HCModule;
import wbif.sjx.ModularImageAnalysis.Object.ModuleCollection;
import wbif.sjx.ModularImageAnalysis.Object.Parameter;
import wbif.sjx.ModularImageAnalysis.Process.Analysis;
import wbif.sjx.ModularImageAnalysis.Process.AnalysisHandler;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by sc13967 on 21/11/2017.
 */
public class DeployedGUI implements ActionListener {
    private int frameWidth = 300;
    private int frameHeight = 750;
    private int elementHeight = 25;

    private JFrame frame = new JFrame();
    private JPanel basicControlPanel = new JPanel();
    private JPanel basicModulesPanel = new JPanel();
    private JScrollPane basicModulesScrollPane = new JScrollPane(basicModulesPanel);
    private JPanel statusPanel = new JPanel();

    private Analysis analysis;
    private ComponentFactory componentFactory;

    public DeployedGUI(String analysisResourcePath) throws URISyntaxException, SAXException, IllegalAccessException, IOException, InstantiationException, ParserConfigurationException, ClassNotFoundException {
        URL fileURL = ClassLoader.getSystemResource(analysisResourcePath);
        File analaysisFile = new File(fileURL.toURI());
        Analysis analysis = new AnalysisHandler().loadAnalysis(analaysisFile);

        new DeployedGUI(analysis);

    }

    public DeployedGUI(Analysis analysis) throws IllegalAccessException, InstantiationException {
        this.analysis = analysis;

        componentFactory = new ComponentFactory(null, elementHeight);

        // Setting location of panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - frameWidth) / 2, (screenSize.height - frameHeight) / 2);

        frame.setLayout(new GridBagLayout());
        frame.setTitle("Modular image analysis (version " + getClass().getPackage().getImplementationVersion() + ")");

        render();

        frame.setVisible(true);

    }

    void render() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 0, 5);
        c.gridx = 0;
        c.gridy = 0;

        // Initialising the control panel
        initialiseControlPanel();
        frame.add(basicControlPanel,c);

        // Initialising the parameters panel
        initialiseModulesPanel();
        c.gridy++;
        frame.add(basicModulesScrollPane, c);

        // Initialising the status panel
        initialiseStatusPanel(500);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 1;
        c.insets = new Insets(5,5,5,5);
        frame.add(statusPanel,c);

        frame.pack();
        frame.revalidate();
        frame.repaint();

    }

    private void initialiseControlPanel() {
        basicControlPanel = new JPanel();
        int buttonSize = 50;

        basicControlPanel = new JPanel();
        basicControlPanel.setPreferredSize(new Dimension(500, buttonSize + 15));
        basicControlPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        basicControlPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        // Load analysis protocol button
        JButton startAnalysisButton = new JButton("Start");
        startAnalysisButton.addActionListener(this);
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        basicControlPanel.add(startAnalysisButton, c);

        basicControlPanel.validate();
        basicControlPanel.repaint();

    }

    private void initialiseModulesPanel() {
        int elementWidth = 500;

        // Initialising the scroll panel
        basicModulesScrollPane.setPreferredSize(new Dimension(elementWidth, frameHeight - 165));
        basicModulesScrollPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        basicModulesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        basicModulesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Initialising the panel for module buttons
        basicModulesPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0;

        // Adding module buttons
        ModuleCollection modules = analysis.getModules();
        for (HCModule module : modules) {
            int idx = modules.indexOf(module);
            if (idx == modules.size() - 1) c.weighty = 1;

            // Only show if the module is enabled
            if (!module.isEnabled()) continue;

            // Only displaying the module title if it has at least one visible parameter
            boolean hasVisibleParameters = false;
            for (Parameter parameter : module.getActiveParameters().values()) {
                if (parameter.isVisible()) hasVisibleParameters = true;
            }
            if (!hasVisibleParameters) continue;

            JPanel titlePanel = componentFactory.createBasicModuleHeading(module, 460);

            c.gridy++;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            basicModulesPanel.add(titlePanel, c);

            for (Parameter parameter : module.getActiveParameters().values()) {
                if (parameter.isVisible()) {
                    JPanel paramPanel = componentFactory.createParameterControl(parameter, modules, module, 460);

                    c.gridy++;
                    basicModulesPanel.add(paramPanel, c);

                }
            }

            c.gridy++;
            JSeparator separator = new JSeparator();
            separator.setPreferredSize(new Dimension(0, 15));
            basicModulesPanel.add(separator, c);

        }

        c.gridy++;
        c.weighty = 100;
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(10, 15));
        basicModulesPanel.add(separator, c);

        basicModulesPanel.validate();
        basicModulesPanel.repaint();
        basicModulesScrollPane.validate();
        basicModulesScrollPane.repaint();

    }

    private void initialiseStatusPanel(int width) {
        statusPanel = new JPanel();
        statusPanel.setPreferredSize(new Dimension(width, 40));
        statusPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        statusPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JTextField textField = new JTextField();
        textField.setBackground(null);
        textField.setPreferredSize(new Dimension(width - 20, 25));
        textField.setBorder(null);
        textField.setText("Modular image analysis (version " + getClass().getPackage().getImplementationVersion() + ")");
        textField.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        statusPanel.add(textField, c);

        OutputStreamTextField outputStreamTextField = new OutputStreamTextField(textField);
        PrintStream printStream = new PrintStream(outputStreamTextField);
        System.setOut(printStream);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Start")) {
            Thread t = new Thread(() -> {
                try {
                    new AnalysisHandler().startAnalysis(analysis);
                } catch (IOException | InterruptedException e1) {
                    e1.printStackTrace();
                } catch (GenericMIAException e1) {
                    IJ.showMessage(e1.getMessage());
                }
            });
            t.start();
        }
    }
}
