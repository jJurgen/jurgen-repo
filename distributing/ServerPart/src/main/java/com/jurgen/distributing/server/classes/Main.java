package com.jurgen.distributing.server.classes;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static ApplicationContext context;

	public static void main(String[] args) {
		String contextPath = new File("").getAbsolutePath();
		context = new ClassPathXmlApplicationContext("file:".concat(contextPath.concat("\\context.xml")));
	}
}
