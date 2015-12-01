package com.jurgen.distributing.client.config;

import com.jurgen.distributing.client.classes.ConnectionHandler;
import com.jurgen.distributing.client.classes.FileReciever;
import com.jurgen.distributing.client.classes.XMLCreator;
import com.jurgen.distributing.client.classes.XMLParser;
import com.jurgen.distributing.client.classes.ZipWorker;
import com.jurgen.distributing.client.frames.MainFrame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MainFrame mainFrame() {
        MainFrame frame = new MainFrame(mainFolder());
        ConnectionHandler ch = new ConnectionHandler();
        FileReciever reciever = new FileReciever();
        ZipWorker zWorker = new ZipWorker();
        ch.setMainFrame(frame);
        ch.setXmlCreator(xmlCreator());
        ch.setXmlParser(xmlParser());
        ch.setMainFolder(mainFolder());
        reciever.setMainFrame(frame);
        reciever.setPORT(filePort());
        ch.setFileReciever(reciever);
        zWorker.setMainFrame(frame);
        ch.setZipWorker(zWorker);
        frame.setConnectionHandler(ch);
        return frame;
    }

    @Bean
    public String mainFolder() {
        return "D:\\ClientPart\\";
    }

    @Bean
    public Integer PORT() {
        return 5533;
    }

    @Bean
    public Integer filePort() {
        return 5534;
    }

    @Bean
    public XMLCreator xmlCreator() {
        return new XMLCreator();
    }

    @Bean
    public XMLParser xmlParser() {
        return new XMLParser();
    }
}
