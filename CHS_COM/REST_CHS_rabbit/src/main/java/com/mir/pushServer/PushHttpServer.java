package com.mir.pushServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class PushHttpServer {
	private static final int PORT = 12345;

	private HttpServer server;

	public void start() throws IOException {
		server = HttpServer.create(new InetSocketAddress(PORT), 0);

		
//		for (int i=1; i<=100; i++) {
//			server.createContext("/EventObserve?id=SERVER_EMA"+ i, new HttpServerHandler.EventObserveHandler());
//		}
		
//		for (int i = 1; i <= 100; i++) {
//			
//			server.createContext("/EventObserve/id=CLIENT_EMA" + i, new HttpServerHandler.RootHandler("CLIENT_EMA"+i));
//			server.createContext("/EventObserve/id=SERVER_EMA" + i, new HttpServerHandler.RootHandler("SERVER_EMA"+i));
//
//		}
		
		for(int i=1; i<=100; i++) {
			server.createContext("/EventObserve/id=SERVER_EMA"+i, new HttpServerHandler.EventObserveHandler());
		}
		
		
		for(int i=1; i<=100; i++) {
			server.createContext("/EMAP/EMS1/1.0b/Summary/SERVER_EMA"+i, new HttpServerHandler.SummaryHandler());
			
		}
		server.createContext("/EMAP/EMS1/1.0b/SummaryACK", new HttpServerHandler.EventACK());
		server.createContext("/EMAP/EMS1/1.0b/EventACK", new HttpServerHandler.EventACK());
		
		
		
		

		server.start();
	}

	public void stop() {
		server.stop(0);
	}

}
