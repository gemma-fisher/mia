package io.github.mianalysis.MIA.Process.AnalysisHandling;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.mianalysis.MIA.MIA;
import io.github.mianalysis.MIA.Module.Module;
import io.github.mianalysis.MIA.Module.ModuleCollection;
import io.github.mianalysis.MIA.Object.Parameters.ParameterCollection;
import io.github.mianalysis.MIA.Object.Parameters.Abstract.Parameter;
import io.github.mianalysis.MIA.Object.References.Abstract.Ref;
import io.github.mianalysis.MIA.Object.References.Collections.ImageMeasurementRefCollection;
import io.github.mianalysis.MIA.Object.References.Collections.MetadataRefCollection;
import io.github.mianalysis.MIA.Object.References.Collections.ObjMeasurementRefCollection;
import io.github.mianalysis.MIA.Object.References.Collections.RefCollection;

/**
 * Created by Stephen on 22/06/2018.
 */
public class AnalysisWriter {
    public static void saveAnalysisAs(Analysis analysis, String outputFileName) throws IOException, ParserConfigurationException, TransformerException {
        if (outputFileName == null || outputFileName.equals("")) {
            saveAnalysis(analysis);
            return;
        }

        // Updating the analysis filename
        analysis.setAnalysisFilename(new File(outputFileName).getAbsolutePath());

        // Creating the document to save
        Document doc = prepareAnalysisDocument(analysis);

        // Preparing the target file for
        FileOutputStream outputStream = new FileOutputStream(outputFileName);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(outputStream));
        outputStream.close();

        MIA.log.writeStatus("File saved ("+ FilenameUtils.getName(outputFileName)+")");

    }

    public static void saveAnalysis(Analysis analysis) throws IOException, ParserConfigurationException, TransformerException {
        FileDialog fileDialog = new FileDialog(new Frame(), "Select file to save", FileDialog.SAVE);
        fileDialog.setMultipleMode(false);
        fileDialog.setFile(analysis.getAnalysisFilename());
        fileDialog.setVisible(true);

        // If no file was selected quit the method
        if (fileDialog.getFiles().length==0) return;

        // Updating the analysis filename
        String outputFileName = fileDialog.getFiles()[0].getAbsolutePath();
        analysis.setAnalysisFilename(outputFileName);

        if (!FilenameUtils.getExtension(outputFileName).equals("mia")) {
            outputFileName = FilenameUtils.removeExtension(outputFileName)+".mia";
        }
        saveAnalysisAs(analysis,outputFileName);

    }

    public static Document prepareAnalysisDocument(Analysis analysis) throws ParserConfigurationException {
        // Adding an XML formatted summary of the modules and their values
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement("ROOT");

        // Adding MIA version number as an attribute
        Attr version = doc.createAttribute("MIA_VERSION");
        version.appendChild(doc.createTextNode(MIA.getVersion()));
        root.setAttributeNode(version);

        // Creating a module collection holding the single-instance modules (input, output and global variables)
        ModuleCollection singleModules = new ModuleCollection();
        singleModules.add(analysis.getModules().getInputControl());
        singleModules.add(analysis.getModules().getOutputControl());

        // Adding module elements
        root.appendChild(prepareModulesXML(doc,singleModules));
        root.appendChild(prepareModulesXML(doc,analysis.getModules()));
        doc.appendChild(root);

        return doc;

    }

    public static Element prepareModulesXML(Document doc, ModuleCollection modules) {
        Element modulesElement = doc.createElement("MODULES");

        // Running through each parameter set (one for each module)
        for (Module module:modules) {
            Element moduleElement = doc.createElement("MODULE");

            // Adding module details
            module.appendXMLAttributes(moduleElement);

            // Adding parameters from this module
            Element paramElement = doc.createElement("PARAMETERS");
            ParameterCollection paraRefs = module.getAllParameters();
            paramElement = prepareRefsXML(doc, paramElement,paraRefs,"PARAMETER");
            moduleElement.appendChild(paramElement);

            // Adding measurement references from this module
            Element imageMeasurementsElement = doc.createElement("IMAGE_MEASUREMENTS");
            ImageMeasurementRefCollection imageReferences = module.updateAndGetImageMeasurementRefs();
            imageMeasurementsElement = prepareRefsXML(doc, imageMeasurementsElement,imageReferences,"MEASUREMENT");
            moduleElement.appendChild(imageMeasurementsElement);

            Element objectMeasurementsElement = doc.createElement("OBJECT_MEASUREMENTS");
            ObjMeasurementRefCollection objectReferences = module.updateAndGetObjectMeasurementRefs();
            objectMeasurementsElement = prepareRefsXML(doc, objectMeasurementsElement,objectReferences,"MEASUREMENT");
            moduleElement.appendChild(objectMeasurementsElement);

            // Adding metadata references from this module
            Element metadataElement = doc.createElement("METADATA");
            MetadataRefCollection metadataRefs = module.updateAndGetMetadataReferences();
            metadataElement = prepareRefsXML(doc, metadataElement,metadataRefs,"METADATUM");
            moduleElement.appendChild(metadataElement);

            // Adding current module to modules
            modulesElement.appendChild(moduleElement);

        }

        return modulesElement;

    }

    public static Element prepareRefsXML(Document doc, Element refsElement, RefCollection<? extends Ref> refs, String groupName) {
        if (refs == null) return refsElement;

        for (Ref ref:refs.values()) {
            if (ref instanceof Parameter) {
                if (!((Parameter) ref).isExported()) continue;
            }

            Element element = doc.createElement(groupName);
            ref.appendXMLAttributes(element);
            refsElement.appendChild(element);

        }

        return refsElement;

    }
}
