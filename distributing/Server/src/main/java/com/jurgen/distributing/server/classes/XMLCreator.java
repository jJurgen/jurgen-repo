package com.jurgen.distributing.server.classes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.jurgen.distributing.server.entities.Range;

public class XMLCreator {

    public final static String fileName = "\\message.xml";
    private DocumentBuilder docBuilder;

    public XMLCreator() {
        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void makeReadyMessage(String folder) {
        Document doc = docBuilder.newDocument();
        Element rootEl = doc.createElement("message");
        Element header = doc.createElement("header");
        Text headerText = doc.createTextNode("ready");
        header.appendChild(headerText);
        rootEl.appendChild(header);
        doc.appendChild(rootEl);
        createFile(doc, folder);
    }

    public void makeStopWorkingMessage(String folder) {
        Document doc = docBuilder.newDocument();
        Element rootEl = doc.createElement("message");
        Element header = doc.createElement("header");
        Text headerText = doc.createTextNode("stop");
        header.appendChild(headerText);
        rootEl.appendChild(header);
        doc.appendChild(rootEl);
        createFile(doc, folder);
    }

    public void makeFileMessage(String folder, String filePath) {
        Document doc = docBuilder.newDocument();
        Element rootEl = doc.createElement("message");
        Element header = doc.createElement("header");
        Text headerText = doc.createTextNode("file");
        Element fileName = doc.createElement("fileName");
        Text fileNameText = doc.createTextNode(new File(filePath).getName());
        fileName.appendChild(fileNameText);
        header.appendChild(headerText);
        rootEl.appendChild(header);
        rootEl.appendChild(fileName);
        doc.appendChild(rootEl);
        createFile(doc, folder);
    }

    public void makeTaskMessage(Range range, String folder) {
        Document doc = docBuilder.newDocument();
        Element rootEl = doc.createElement("message");
        Element header = doc.createElement("header");
        Text headerText = doc.createTextNode("task");
        Element task = doc.createElement("task");
        Element start = doc.createElement("start");
        Element finish = doc.createElement("finish");
        Text startText = doc.createTextNode(range.getStart());
        Text finishText = doc.createTextNode(range.getFinish());
        start.appendChild(startText);
        finish.appendChild(finishText);
        task.appendChild(start);
        task.appendChild(finish);
        header.appendChild(headerText);
        rootEl.appendChild(header);
        rootEl.appendChild(task);
        doc.appendChild(rootEl);
        createFile(doc, folder);
    }

    private void createFile(Document doc, String folder) {
        try (FileOutputStream fOutStream = new FileOutputStream(folder.concat(fileName))) {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult sResult = new StreamResult(fOutStream);
            t.transform(domSource, sResult);
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
    }

}
