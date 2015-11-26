package com.jurgen.distributing.client.classes;

import java.io.File;

import javax.swing.JOptionPane;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static ApplicationContext context;

	public static void main(String[] args) {
		String contextPath = new File("").getAbsolutePath();
		String path = "file:".concat(contextPath.concat("\\context.xml"));	
		context = new ClassPathXmlApplicationContext(path);
	}
}
