package com.jurgen.distributing.server.classes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.jurgen.distributing.server.frames.MainFrame;

public class ServerManager {
	private MainFrame mainFrame;
	private boolean isStop = false;
	private int PORT;
	private ServerSocket serverSocket;
	private List<ConnectionHandler> handlers;

	public ServerManager() {
		
	}

	public ServerManager(int port) throws IOException {
		this.PORT = port;
		handlers = new ArrayList<>();
		serverSocket = new ServerSocket(PORT);
		
	}

	private void searchClients() {
		mainFrame.addToLog("Waiting for clients...");
		while (!isStop) {
			try {
				Socket newClientSocket = serverSocket.accept();
				mainFrame.addToLog("new client: " + newClientSocket.getInetAddress().toString());
				ConnectionHandler conn = (ConnectionHandler) Main.context.getBean("connectionHandler");
				handlers.add(conn);
				conn.setSocket(newClientSocket);
				conn.start();
			} catch (IOException e) {
				mainFrame.addToLog("Error: " + e.getMessage());
			}
		}
	}

	public void startClientsSearching() {
		Thread searchingThread = new Thread(() -> {
			searchClients();
		});
		searchingThread.start();
	}
	
	

	public void stopClientsSearching() {
		isStop = true;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public int getPORT() {
		return PORT;
	}

	public void setPORT(int pORT) {
		PORT = pORT;
	}

}
