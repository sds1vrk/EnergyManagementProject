package com.mir.rest_server;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.resteasy.annotations.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;


import java.sql.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import com.mir.ems.globalVar.*;
import com.mir.vtn.controller.FrontController;
import com.mir.vtn.global.Global;
import com.mir.vtn.profile.EventDetail;
import com.ning.http.client.Response.ResponseBuilder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import scala.util.parsing.json.JSON;

@Controller
@Path("/EMA")
public class Controller01 {
	@GET
	@Path("/topology")
	@Produces("application/json;charset=utf-8")
	public Object test() throws JSONException {

		EMA_Topology test = new EMA_Topology();

		String content = test.topology4();
		
//		String content="hi test rest server";

		javax.ws.rs.core.Response.ResponseBuilder response = Response.ok(content);
		

		response.header("Content-Length", content.length());

		return response.build();

	}
	
	@POST
	@Path("/rest_event")
	@Consumes("application/json;charset=utf-8")
//	@Produces("text/plain;charset=utf-8")
//	@Consumes("text/plain;charset=utf-8")
	public String consumeJSO2(String test) throws JSONException, UnsupportedEncodingException {

		System.err.println("event_post" + test.toString());

		event_parsing(test.toString());
		
		

		return "201_POST_OK";
	}
	
	
	public void event_parsing(String message) throws JSONException, UnsupportedEncodingException {

	
			
			FrontController fc=new FrontController();
			
			
			byte [] array1=message.getBytes();
			
			
			fc.createStudent(array1);
			
	
		}

	
	
	


}