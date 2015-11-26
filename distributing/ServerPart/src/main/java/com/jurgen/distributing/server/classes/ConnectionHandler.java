package com.jurgen.distributing.server.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.jurgen.distributing.server.frames.MainFrame;

public class ConnectionHandler extends Thread {

	private MainFrame mainFrame;
	private boolean isStop = false;
	private Integer clientId;
	private XMLCreator xmlCreator;
	private XMLParser xmlParser;
	private String personalFolder;
	private Socket socket;
	private RangesDealer rangesDealer;
	private FileSender fileSender;

	@Override
	public void run() {
		prepareFolder();
		try (InputStreamReader inpStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader bufReader = new BufferedReader(inpStreamReader)) {
			while (!isStop) {
				String message = bufReader.readLine();
				xmlParser.getXML(personalFolder, message);
				String header = xmlParser.getHeader(personalFolder);
				switch (header) {
				case "ready":
					mainFrame.addToLog("client " + clientId + " is ready.");
					xmlCreator.makeFileMessage(personalFolder, mainFrame.getZipFilePath());
					sendXML(personalFolder.concat(xmlCreator.fileName));
					fileSender.sendFile(mainFrame.getZipFilePath());
					break;

				case "fileRecieved":
					if (!mainFrame.isFound()) {
						xmlCreator.makeTaskMessage(rangesDealer.getNewRange(socket), personalFolder);
						sendXML(personalFolder.concat(xmlCreator.fileName));
						mainFrame.addToLog("Sended new task to " + socket.getInetAddress().toString());
					} else {
						xmlCreator.makeStopWorkingMessage(personalFolder);
						sendXML(personalFolder.concat(xmlCreator.fileName));
					}
					break;

				case "answer":
					if (xmlParser.getAnswer(personalFolder)) {
						mainFrame.setFound(true);
						mainFrame.addToLog("PASSWORD: " + xmlParser.getPassword(personalFolder));
						mainFrame.setAnswer(xmlParser.getPassword(personalFolder));
						xmlCreator.makeStopWorkingMessage(personalFolder);
						sendXML(personalFolder.concat(xmlCreator.fileName));
					} else {
						if (mainFrame.isFound()) {
							xmlCreator.makeStopWorkingMessage(personalFolder);
							sendXML(personalFolder.concat(xmlCreator.fileName));
						} else {
							xmlCreator.makeTaskMessage(rangesDealer.getNewRange(socket), personalFolder);
							sendXML(personalFolder.concat(xmlCreator.fileName));
							mainFrame.addToLog("Sended new task to " + socket.getInetAddress().toString());
						}
					}
					break;
				}

			}
		} catch (IOException e) {
			mainFrame.addToLog("Error: " + e.getMessage());
			isStop = true;
		}
	}

	public void sendXML(String xmlFilePath) {
		try (FileReader fReader = new FileReader(new File(xmlFilePath));
				BufferedReader bufReader = new BufferedReader(fReader)) {
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = bufReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println(stringBuilder.toString());
		} catch (IOException e) {
			mainFrame.addToLog("Error: " + e.getMessage());
		}
	}

	private void prepareFolder() {
		new File(personalFolder).mkdirs();
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
		this.personalFolder = mainFrame.getMainFolder().concat("thread").concat(clientId.toString());
	}

	public XMLCreator getXmlCreator() {
		return xmlCreator;
	}

	public void setXmlCreator(XMLCreator xmlCreator) {
		this.xmlCreator = xmlCreator;
	}

	public XMLParser getXmlParser() {
		return xmlParser;
	}

	public void setXmlParser(XMLParser xmlParser) {
		this.xmlParser = xmlParser;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public RangesDealer getRangesDealer() {
		return rangesDealer;
	}

	public void setRangesDealer(RangesDealer rangesDealer) {
		this.rangesDealer = rangesDealer;
	}

	public FileSender getFileSender() {
		return fileSender;
	}

	public void setFileSender(FileSender fileSender) {
		this.fileSender = fileSender;
	}

}
