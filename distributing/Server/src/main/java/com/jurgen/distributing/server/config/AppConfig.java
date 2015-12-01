package com.jurgen.distributing.server.config;

import com.jurgen.distributing.server.classes.ConnectionHandler;
import com.jurgen.distributing.server.classes.FileSender;
import com.jurgen.distributing.server.classes.IdGenerator;
import com.jurgen.distributing.server.classes.RangesDealer;
import com.jurgen.distributing.server.classes.ServerManager;
import com.jurgen.distributing.server.classes.XMLCreator;
import com.jurgen.distributing.server.classes.XMLParser;
import com.jurgen.distributing.server.frames.MainFrame;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
    
    @Bean
    public String mainFolder() {
        return "D:\\ServerPart\\";
    }
    
    @Bean
    @Scope("prototype")
    public ConnectionHandler connectionHandler() throws IOException {
        ConnectionHandler ch = new ConnectionHandler();
        ch.setMainFrame(mainFrame());
        ch.setClientId(currentId());
        ch.setXmlCreator(xmlCreator());
        ch.setXmlParser(xmlParser());
        ch.setRangesDealer(rangesDealer());
        ch.setFileSender(fileSender());
        return ch;
    }
    
    @Bean
    public MainFrame mainFrame() throws IOException {
        MainFrame frame = new MainFrame("Server", mainFolder());
        ServerManager serverManager = new ServerManager(PORT());
        serverManager.setMainFrame(frame);
        frame.setServerManager(serverManager);
        return frame;
    }
    
    @Bean
    public FileSender fileSender() throws IOException {
        FileSender sender = new FileSender();
        sender.setMainFrame(mainFrame());
        sender.setPORT(filePort());
        return sender;
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
    public RangesDealer rangesDealer() {
        return new RangesDealer();
    }
    
    @Bean
    public XMLCreator xmlCreator() {
        return new XMLCreator();
    }
    
    @Bean
    public XMLParser xmlParser() {
        return new XMLParser();
    }
    
    @Bean
    public IdGenerator idGenerator() {
        return new IdGenerator();
    }
    
    @Bean()
    @Scope("prototype")
    public Integer currentId() {
        return idGenerator().getCurrentId();
    }
    
}
