package com.jurgen.distributing.client.classes;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser {

    private static final String fileName = "\\message.xml";
    private DocumentBuilder docBuilder;
    private Transformer transformer;

    public XMLParser() {
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getHeader(String folder) {
        try {
            File xmlFile = new File(folder.concat(fileName));
            Document doc = docBuilder.parse(xmlFile);
            doc.normalize();
            NodeList rootNodes = doc.getElementsByTagName("message");
            Node rootNode = rootNodes.item(0);
            Element rootEl = (Element) rootNode;
            Element header = (Element) rootEl.getElementsByTagName("header").item(0);
            return header.getTextContent();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFileName(String folder) {
        try {
            File xmlFile = new File(folder.concat(fileName));
            Document doc = docBuilder.parse(xmlFile);
            doc.normalize();
            NodeList rootNodes = doc.getElementsByTagName("message");
            Node rootNode = rootNodes.item(0);
            Element rootEl = (Element) rootNode;
            Element fileName = (Element) rootEl.getElementsByTagName("fileName").item(0);
            return fileName.getTextContent();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Range getTask(String folder) {
        try {
            File xmlFile = new File(folder.concat(fileName));
            Document doc = docBuilder.parse(xmlFile);
            doc.normalize();
            NodeList rootNodes = doc.getElementsByTagName("message");
            Node rootNode = rootNodes.item(0);
            Element rootEl = (Element) rootNode;
            Element task = (Element) rootEl.getElementsByTagName("task").item(0);
            Element start = (Element) task.getElementsByTagName("start").item(0);
            Element finish = (Element) task.getElementsByTagName("finish").item(0);
            Range r = new Range(start.getTextContent(), finish.getTextContent());
            return r;
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getXML(String folder, String message) {
        try (StringReader strReader = new StringReader(message)) {
            InputSource inpSource = new InputSource(strReader);
            Document xmlDoc = docBuilder.parse(inpSource);
            DOMSource source = new DOMSource(xmlDoc);
            StreamResult result = new StreamResult(new File(folder.concat(fileName)));
            transformer.transform(source, result);
        } catch (SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

}
