package com.mir.rest_server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class Main {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		NettyRestServer server = context.getBean(NettyRestServer.class);
		
		server.start();
	}
}