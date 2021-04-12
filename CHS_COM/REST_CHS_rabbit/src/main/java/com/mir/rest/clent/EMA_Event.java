package com.mir.rest.clent;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.ResourceLeakDetector;
import scala.util.parsing.json.JSON;
import io.netty.*;

import org.apache.http.client.ResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;

import com.lowagie.text.pdf.ByteBuffer;
import com.mir.ems.globalVar.global;
import com.mir.ven.HTTPRequest;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * A simple HTTP ECHO application
 */
public class EMA_Event extends Thread {

	String ip;

	REST_Structure rs;
	
	int port;
	
	String post_rest;
	
	event_structure ev;
//	public ArrayList<String>arr;

//	EchoApplication ea;
	public EMA_Event(String ip,int port) {
		this.ip = ip;
//		this.arr=new ArrayList<>();
		System.out.println("IP?!" + ip);
		this.port=port;

	}
	
	
	public void ems_event() {

		System.out.println("계속 run?!");

//		try {
//			sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// I find while learning Netty to keep resource leak detecting at Paranoid,
		// however *DO NOT* ship with this level.
		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
	
//	       EventLoopGroup serverWorkgroup = new NioEventLoopGroup();
		EventLoopGroup clientWorkgroup = new NioEventLoopGroup();
//	       EchoApplication app = new EchoApplication();
		try {

//				Bootstrap clientBootstrap = Bootstrap.client();
//				Bootstrap clientBootstrap=ea.client();
			
			//get
			Bootstrap clientBootstrap = this.client();
//	           final ByteBuf content = Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8);
			clientBootstrap.connect(ip, port).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					// Prepare the HTTP request.
					
//					HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
//							"/EMA/topology");
//					
//					
//					
//					future.channel().writeAndFlush(request);
					
					
					//여기부터 Post
					HttpRequest request_post = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
							"/events");
					
					request_post.headers().add(HttpHeaderNames.CONTENT_TYPE,"application/json");
					
					request_post.headers().add(HttpHeaderNames.CONNECTION,"keep-alive");
					
					request_post.headers().add(HttpHeaderNames.HOST,ip);
					
				
//					if(global.rest_reportType.contains("Expli")) {
//						 post_rest=REST_Structure.explicit_rest();
//						
//					}
//					else if(global.rest_reportType.contains("Impli")){
//						 post_rest=REST_Structure.implicit_rest();
//						
//					}
					
					
					JSONObject json=new JSONObject();
					//venID
					json.put("EMAID", global.ema_name);
					json.put("CEMAID", global.target_name);
					
					//payload value
					json.put("value", global.value);
					json.put("event_period", 1);
					json.put("event_count", 60);
					json.put("event_time", 20190628);
//					json.put("time", "현재시간");
					
//					json.put("start_TIME", "2018-08-07 18:51:16 KST");
//					json.put("duration", "1");
//					json.put(contextID, value)
					
					
					
					
					
					
					
//					post_rest=ev.getEMAID()+ev.getThreshold();
					
//					post_rest=global.ema_name+global.value;
					post_rest=json.toString();
					
					
					 System.out.println("post Message"+post_rest);
					
					ByteBuf test=Unpooled.copiedBuffer(post_rest,StandardCharsets.UTF_8);
					request_post.headers().set(HttpHeaderNames.CONTENT_LENGTH,test.readableBytes());
					
					
					 ((DefaultFullHttpRequest) request_post).content().clear().writeBytes(test);
					
					 future.channel().writeAndFlush(request_post);
			
				}
			});
			
			

			

					
			
					
					
			
//	           serverChannel.closeFuture().sync();
		} finally {
			// Gracefully shutdown both event loop groups
//	           serverWorkgroup.shutdownGracefully();
			clientWorkgroup.shutdownGracefully();
		}

	}
	
	
	
	
	
	

	public void rest_event() {

				System.out.println("계속 run?!");

//				try {
//					sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				// I find while learning Netty to keep resource leak detecting at Paranoid,
				// however *DO NOT* ship with this level.
				ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
			
//			       EventLoopGroup serverWorkgroup = new NioEventLoopGroup();
				EventLoopGroup clientWorkgroup = new NioEventLoopGroup();
//			       EchoApplication app = new EchoApplication();
				try {

//						Bootstrap clientBootstrap = Bootstrap.client();
//						Bootstrap clientBootstrap=ea.client();
					
					//get
					Bootstrap clientBootstrap = this.client();
//			           final ByteBuf content = Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8);
					clientBootstrap.connect(ip, port).addListener(new ChannelFutureListener() {
						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							// Prepare the HTTP request.
							
//							HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
//									"/EMA/topology");
//							
//							
//							
//							future.channel().writeAndFlush(request);
							
							
							//여기부터 Post
							HttpRequest request_post = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
									"/EMA/rest_event");
							
							request_post.headers().add(HttpHeaderNames.CONTENT_TYPE,"application/json");
							
							request_post.headers().add(HttpHeaderNames.CONNECTION,"keep-alive");
							
							request_post.headers().add(HttpHeaderNames.HOST,ip);
							
						
//							if(global.rest_reportType.contains("Expli")) {
//								 post_rest=REST_Structure.explicit_rest();
//								
//							}
//							else if(global.rest_reportType.contains("Impli")){
//								 post_rest=REST_Structure.implicit_rest();
//								
//							}
							
							
							JSONObject json=new JSONObject();
							//venID
							json.put("EMAID", global.ema_name);
							json.put("CEMAID", global.target_name);
							
							//payload value
							json.put("value", global.value);
							json.put("event_period", 1);
							json.put("event_count", 60);
							json.put("event_time", 20190628);
//							json.put("time", "현재시간");
							
//							json.put("start_TIME", "2018-08-07 18:51:16 KST");
//							json.put("duration", "1");
//							json.put(contextID, value)
							
							
							
							
							
							
							
//							post_rest=ev.getEMAID()+ev.getThreshold();
							
//							post_rest=global.ema_name+global.value;
							post_rest=json.toString();
							
							
							 System.out.println("post Message"+post_rest);
							
							ByteBuf test=Unpooled.copiedBuffer(post_rest,StandardCharsets.UTF_8);
							request_post.headers().set(HttpHeaderNames.CONTENT_LENGTH,test.readableBytes());
							
							
							 ((DefaultFullHttpRequest) request_post).content().clear().writeBytes(test);
							
							 future.channel().writeAndFlush(request_post);
					
						}
					});
					
					

					
	
							
					
							
							
					
//			           serverChannel.closeFuture().sync();
				} finally {
					// Gracefully shutdown both event loop groups
//			           serverWorkgroup.shutdownGracefully();
					clientWorkgroup.shutdownGracefully();
				}

			}
	
	public void dr_control() {

		System.out.println("계속 run?!");

//		try {
//			sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// I find while learning Netty to keep resource leak detecting at Paranoid,
		// however *DO NOT* ship with this level.
		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
	
//	       EventLoopGroup serverWorkgroup = new NioEventLoopGroup();
		EventLoopGroup clientWorkgroup = new NioEventLoopGroup();
//	       EchoApplication app = new EchoApplication();
		try {

//				Bootstrap clientBootstrap = Bootstrap.client();
//				Bootstrap clientBootstrap=ea.client();
			
			//get
			Bootstrap clientBootstrap = this.client();
//	           final ByteBuf content = Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8);
			clientBootstrap.connect(ip, port).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					// Prepare the HTTP request.
					
//					HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
//							"/EMA/topology");
//					
//					
//					
//					future.channel().writeAndFlush(request);
					
					
					//여기부터 Post
					HttpRequest request_post = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
							"/EMA/rest_configuration");
					
					request_post.headers().add(HttpHeaderNames.CONTENT_TYPE,"application/json");
					
					request_post.headers().add(HttpHeaderNames.CONNECTION,"keep-alive");
					
					request_post.headers().add(HttpHeaderNames.HOST,ip);
					
				
//					if(global.rest_reportType.contains("Expli")) {
//						 post_rest=REST_Structure.explicit_rest();
//						
//					}
//					else if(global.rest_reportType.contains("Impli")){
//						 post_rest=REST_Structure.implicit_rest();
//						
//					}
					
					
					JSONObject json=new JSONObject();
					json.put("EMAID", global.ema_name);
					json.put("DR_MODE", global.dr_control);
					json.put("RDR_MODE", global.rdr_control);
					json.put("report_Time", global.rest_reportTime);
					json.put("poll_Time", global.resT_pollTime);
					
//					json.put("time", "현재시간");
					
					
					
					
//					post_rest=ev.getEMAID()+ev.getThreshold();
					
//					post_rest=global.ema_name+global.value;
					post_rest=json.toString();
					
					
					 System.out.println("post Message"+post_rest);
					
					ByteBuf test=Unpooled.copiedBuffer(post_rest,StandardCharsets.UTF_8);
					request_post.headers().set(HttpHeaderNames.CONTENT_LENGTH,test.readableBytes());
					
					
					 ((DefaultFullHttpRequest) request_post).content().clear().writeBytes(test);
					
					 future.channel().writeAndFlush(request_post);
			
				}
			});
			
			

			

					
			
					
					
			
//	           serverChannel.closeFuture().sync();
		} finally {
			// Gracefully shutdown both event loop groups
//	           serverWorkgroup.shutdownGracefully();
			clientWorkgroup.shutdownGracefully();
		}

	}
	
			
			//get
			private Bootstrap client() {
				EventLoopGroup workerGroup = new NioEventLoopGroup();
				Bootstrap b = new Bootstrap();
				b.group(workerGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// HttpClient codec is a helper ChildHandler that encompasses
						// both HTTP response decoding and HTTP request encoding
						ch.pipeline().addLast(new HttpClientCodec());
						// HttpObjectAggregator helps collect chunked HttpRequest pieces into
						// a single FullHttpRequest. If you don't make use of streaming, this is
						// much simpler to work with.
						ch.pipeline().addLast(new HttpObjectAggregator(1048576));
						ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpResponse>() {
							@Override
							protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg)
									throws Exception {
								final String rest_msg = msg.content().toString(CharsetUtil.UTF_8);
								

//		                                msg.get
								
								
								
								log.info("Response: {}", rest_msg);
								System.err.println("get_REST_MSG LOG"+rest_msg);
//								HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
//										"/EMA/topology");
								
								
//								HttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpMethod.GET);
								
								
								
								
								
//								if(!(rest_msg.contains("201")&& rest_msg!=null)) {
//									parsing(rest_msg);
//								}
								
								
								if(rest_msg.contains("EMA")&& rest_msg!=null) {
									parsing(rest_msg);
								}
								
								
								
							}
						});
					}
				});
				return b;

			}
			
//
//			
//		};
//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(chartUpdaterTask, 10000, 3000);

	

	public void parsing(String message) throws JSONException {
		
		//공백 제거 파싱
//		message=message.replace(" ", "");
		
		
		
		JSONObject json = new JSONObject(message);
		
		System.out.println("Parsing Result:"+json.toString());
		
		if (json.has("EMSID")) {
			UpdateEMSInfo(json);
		} else if (json.has("SEMAID")) {
			UpdateSEMAInfo(json, null);
		} else if (json.has("CEMAID")) {
			UpdateCEMAInfo(json, null);
		} else if (json.has("DEVICEID")) {
			UpdateDEVICEInfo(json, null);
		}


	}

	public void UpdateEMSInfo(JSONObject json) throws JSONException {
		String EMSID = json.getString("EMSID");
		REST_EMSProfile rems = new REST_EMSProfile(EMSID);
		global.rest_EMSProfile.put(EMSID, rems);

		System.out.println("EMS Collection" + global.rest_EMSProfile.values().toString());

		if (json.has("SEMAIDs")) {
			System.out.println("여기 로그 확인 + SEMSIDs has ?!");

			JSONArray json_array = json.getJSONArray("SEMAIDs");
			for (int i = 0; i < json_array.length(); i++) {
				JSONObject json_object = json_array.getJSONObject(i);
				UpdateSEMAInfo(json_object, EMSID);

			}

		}

	}

	public void UpdateSEMAInfo(JSONObject json, String EMSID) throws JSONException {
		String SEMAID = json.getString("SEMAID");

		// 추가 : 중복제거, replace
		REST_SemaProfile sema = new REST_SemaProfile(EMSID, SEMAID);

//		if (global.rest_SemaProfile.isEmpty()) {
//			global.rest_SemaProfile.put(SEMAID, sema);
//		}

		REST_SemaProfile r = global.rest_SemaProfile.putIfAbsent(SEMAID, sema);

		System.out.println("SEMA Collection" + global.rest_SemaProfile.values().toString());
		
		if (r==null) {
			global.rest_SemaProfile.put(SEMAID, sema);
		}
		
		else if (sema.getEmaID() != null && !(sema.getEmaID().equals(r.getEmaID())) ) {
			global.rest_SemaProfile.replace(SEMAID, sema);
		}

		System.out.println("여기 SEMA의 값은?!" + sema.getEmaID() + sema.getsubID());

		// Update SEMA info using EMSID, SEMAID

		if (json.has("CEMAIDs")) {
			JSONArray json_array = json.getJSONArray("CEMAIDs");
			for (int i = 0; i < json_array.length(); i++) {
				JSONObject json_object = json_array.getJSONObject(i);
				UpdateCEMAInfo(json_object, SEMAID);
			}
		}
	}

	public void UpdateCEMAInfo(JSONObject json, String SEMAID) throws JSONException {
		String CEMAID = json.getString("CEMAID");

		
		REST_CemaProfile cema = new REST_CemaProfile(SEMAID, CEMAID);

//		if (global.rest_CemaProfile.isEmpty()) {
//			global.rest_CemaProfile.put(CEMAID, cema);
//		} 

//		else {
		REST_CemaProfile r = global.rest_CemaProfile.putIfAbsent(CEMAID, cema);

		System.out.println("CEMA Collection" + global.rest_CemaProfile.values());
		
		if(r==null) {
			global.rest_CemaProfile.put(CEMAID, cema);
		}
		
		else if (cema.getEmaID() != null && !(cema.getEmaID().equals(r.getEmaID())) ) {
			global.rest_CemaProfile.replace(CEMAID, cema);
		}
//		}

		System.out.println("여기 CEMA의 값은?!" + cema.getEmaID() + cema.getDeviceEMAID());

		// Update CEMA info using SEMAID, CEMAID

		if (json.has("DEVICEs")) {
			JSONArray json_array = json.getJSONArray("DEVICEs");
			for (int i = 0; i < json_array.length(); i++) {
				JSONObject json_object = json_array.getJSONObject(i);
				UpdateDEVICEInfo(json_object, CEMAID);
			}
		}
	}

	public void UpdateDEVICEInfo(JSONObject json, String CEMAID) throws JSONException {
		String DEVICEID = json.getString("DEVICEID");

		REST_DeviceProfile device = new REST_DeviceProfile(CEMAID, DEVICEID);

//		if (global.rest_deviceProfile.isEmpty()) {
//			global.rest_deviceProfile.put(DEVICEID, device);
//		} 

//		else {
		REST_DeviceProfile r = global.rest_deviceProfile.putIfAbsent(DEVICEID, device);
		
		System.out.println("DEVICE Collection" + global.rest_deviceProfile.values().toString());
		
		System.out.println("device"+device);
		
		System.out.println("r"+r);
		
		if (r == null) {
			global.rest_deviceProfile.put(DEVICEID, device);
		} else if (device.getEmaID() != null && !(device.getEmaID().equals(r.getEmaID()))) {
			global.rest_deviceProfile.replace(DEVICEID, device);
		}
//		}

		System.out.println("여기 DEVICE의 값은?!" + device.getEmaID() + device.getDeviceEMAID());

		// Update SEMA info using CEMAID, DEVICEID
	}

	private static final Logger log = LoggerFactory.getLogger(REST_CLIENT_GET.class);


}
