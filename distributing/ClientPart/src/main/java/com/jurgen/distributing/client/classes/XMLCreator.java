package com.jurgen.distributing.client.classes;

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

public class XMLCreator {

	public final static String fileName = "\\message.xml";
	private DocumentBuilderFactory dbf;
	private DocumentBuilder docBuilder;

	public XMLCreator() {
		dbf = DocumentBuilderFactory.newInstance();
		try {
			docBuilder = dbf.newDocumentBuilder();
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
	
	public void makeFileRecievedMessage(String folder){
		Document doc = docBuilder.newDocument();
		Element rootEl = doc.createElement("message");
		Element header = doc.createElement("header");
		Text headerText = doc.createTextNode("fileRecieved");
		header.appendChild(headerText);
		rootEl.appendChild(header);
		doc.appendChild(rootEl);
		createFile(doc, folder);
	}
	
	public void makeTrueAnswer(String folder, String pass){
		Document doc = docBuilder.newDocument();
		Element rootEl = doc.createElement("message");
		Element header = doc.createElement("header");
		Text headerText = doc.createTextNode("answer");
		Element answer = doc.createElement("answer");
		Text answerText = doc.createTextNode("true");
		Element password = doc.createElement("password");
		Text passwordText = doc.createTextNode(pass);
		header.appendChild(headerText);
		password.appendChild(passwordText);
		answer.appendChild(answerText);
		rootEl.appendChild(header);
		rootEl.appendChild(answer);
		rootEl.appendChild(password);
		doc.appendChild(rootEl);
		createFile(doc, folder);
	}
	
	public void makeFalseAnswer(String folder){
		Document doc = docBuilder.newDocument();
		Element rootEl = doc.createElement("message");
		Element header = doc.createElement("header");
		Text headerText = doc.createTextNode("answer");
		Element answer = doc.createElement("answer");
		Text answerText = doc.createTextNode("false");		
		header.appendChild(headerText);
		answer.appendChild(answerText);
		rootEl.appendChild(header);
		rootEl.appendChild(answer);
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
