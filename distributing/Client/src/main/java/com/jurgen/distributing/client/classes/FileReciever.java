package com.jurgen.distributing.client.classes;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.jurgen.distributing.client.frames.MainFrame;

public class FileReciever {

    private MainFrame mainFrame;
    private Integer PORT;
    private String IP;

    public FileReciever() {

    }

    public synchronized void recieveFile(String fileName) {
        String targetFilePath = mainFrame.getMainFolder().concat(fileName);
        try (Socket socket = new Socket(IP, PORT);
                DataInputStream dIn = new DataInputStream(socket.getInputStream());
                FileOutputStream fOut = new FileOutputStream(new File(targetFilePath))) {
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = dIn.read(buffer)) != -1) {
                fOut.write(buffer, 0, count);
            }
            fOut.flush();
            mainFrame.addToLog("File " + fileName + " recieved");
        } catch (IOException e) {
            mainFrame.addToLog("ERROR: " + e.getMessage());
        }
    }

    public Integer getPORT() {
        return PORT;
    }

    public void setPORT(Integer pORT) {
        PORT = pORT;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String iP) {
        IP = iP;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

}
