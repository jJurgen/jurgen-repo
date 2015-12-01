package com.jurgen.distributing.client;

import com.jurgen.distributing.client.config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    private static ApplicationContext ctx;

    public static void main(String[] args){
        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    }
    
    public static ApplicationContext getCtx() {
        return ctx;
    }

}
