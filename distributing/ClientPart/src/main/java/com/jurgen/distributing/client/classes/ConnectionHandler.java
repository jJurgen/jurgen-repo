package com.jurgen.distributing.client.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.jurgen.distributing.client.frames.MainFrame;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class ConnectionHandler extends Thread {

	private Socket socket;
	private MainFrame mainFrame;
	private XMLCreator xmlCreator;
	private XMLParser xmlParser;
	private String mainFolder;
	private boolean isStop = false;
	private FileReciever fileReciever;
	private ZipWorker zipWorker;

	public ConnectionHandler() {
	}

	@Override
	public void run() {
		mainFrame.addToLog("Connection established!");
		xmlCreator.makeReadyMessage(mainFolder);
		sendXML(mainFolder.concat(xmlCreator.fileName));
		try (InputStreamReader inpStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader bufReader = new BufferedReader(inpStreamReader)) {
			while (!isStop) {
				String message = bufReader.readLine();
				xmlParser.getXML(mainFolder, message);
				String header = xmlParser.getHeader(mainFolder);
				switch (header) {

				case "file":
					String fileName = xmlParser.getFileName(mainFolder);
					fileReciever.recieveFile(fileName);
					try {
						zipWorker.setzFile(new ZipFile(mainFolder.concat(fileName)));
						xmlCreator.makeFileRecievedMessage(mainFolder);
						sendXML(mainFolder.concat(xmlCreator.fileName));
					} catch (ZipException e) {
						mainFrame.addToLog("Error: " + e.getMessage());
					}
					break;

				case "task":
					mainFrame.addToLog("Recieved new task");
					Range task = xmlParser.getTask(mainFolder);
					if (zipWorker.SearchPassword(task.getStart(), task.getFinish())) {
						xmlCreator.makeTrueAnswer(mainFolder, zipWorker.getPassword());
					} else {
						xmlCreator.makeFalseAnswer(mainFolder);
					}
					sendXML(mainFolder.concat(xmlCreator.fileName));
					break;
				case "stop":
					mainFrame.addToLog("From server: stop working \n Waiting for messages...");
					break;

				default:
					mainFrame.addToLog("header: " + header);
				}
			}
		} catch (IOException e) {
			mainFrame.addToLog("Error: " + e.getMessage());
			isStop = true;
			try {
				socket.close();
			} catch (IOException ex) {
				mainFrame.addToLog("Error: " + ex.getMessage());
			}
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

	public boolean tryConnecting(int port, String ip) {
		try {
			socket = new Socket(ip, port);
			fileReciever.setIP(ip);
		} catch (IOException e) {
			mainFrame.addToLog("Server is not responding...");
			return false;
		}
		return true;
	}

	public void stopHandler() {
		isStop = true;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
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

	public String getMainFolder() {
		return mainFolder;
	}

	public void setMainFolder(String mainFolder) {
		this.mainFolder = mainFolder;
	}

	public FileReciever getFileReciever() {
		return fileReciever;
	}

	public void setFileReciever(FileReciever fileReciever) {
		this.fileReciever = fileReciever;
	}

	public ZipWorker getZipWorker() {
		return zipWorker;
	}

	public void setZipWorker(ZipWorker zipWorker) {
		this.zipWorker = zipWorker;
	}

}
