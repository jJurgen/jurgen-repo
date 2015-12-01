package com.jurgen.distributing.server.classes;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.jurgen.distributing.server.frames.MainFrame;

public class FileSender {

	private MainFrame mainFrame;
	private Integer PORT;

	public FileSender() {

	}

	public synchronized void sendFile(String filePath){
		try(ServerSocket serverSocket = new ServerSocket(PORT);
				Socket fileSocket = serverSocket.accept();
				DataOutputStream dOut = new DataOutputStream(fileSocket.getOutputStream());
				FileInputStream fIn = new FileInputStream(new File(filePath))){			
			byte[] buffer = new byte[8192];
			int count = 0;
			while((count = fIn.read(buffer)) != -1){
				dOut.write(buffer, 0, count);
			}
			dOut.flush();
			mainFrame.addToLog("File sended to " + fileSocket.getInetAddress());			
		} catch (IOException e) {
			mainFrame.addToLog("ERROR: " + e.getMessage());
		}
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public Integer getPORT() {
		return PORT;
	}

	public void setPORT(Integer pORT) {
		PORT = pORT;
	}

}
