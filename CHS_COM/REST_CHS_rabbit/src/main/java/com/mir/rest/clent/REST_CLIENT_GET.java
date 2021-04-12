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
import org.apache.tomcat.jni.Time;
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
public class REST_CLIENT_GET extends Thread {

	String ip;

	REST_Structure rs;

	int port;

	String post_rest;
//	public ArrayList<String>arr;

//	EchoApplication ea;
	public REST_CLIENT_GET(String ip, int port) {
		this.ip = ip;
//		this.arr=new ArrayList<>();
//		System.out.println("IP?!" + ip);
		this.port = port;

	}
	
	
	public REST_CLIENT_GET() {
		
	}

	public void rest_topology() {
		
		TimerTask chartUpdaterTask = new TimerTask() {

			@Override
			public void run() {


				ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);

//			       EventLoopGroup serverWorkgroup = new NioEventLoopGroup();
				EventLoopGroup clientWorkgroup = new NioEventLoopGroup();
//			       EchoApplication app = new EchoApplication();
				try {

//						Bootstrap clientBootstrap = Bootstrap.client();
//						Bootstrap clientBootstrap=ea.client();

					// get
					Bootstrap clientBootstrap = this.client();
//			           final ByteBuf content = Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8);
					clientBootstrap.connect(ip, port).addListener(new ChannelFutureListener() {
						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							// Prepare the HTTP request.

							HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
									"/EMA/topology");

							future.channel().writeAndFlush(request);

							
//							
//							System.err.println(global.rest_deviceProfile.size()+"DEVICEPROFILE SIZE");
//							if (global.rest_deviceProfile.size()==20) {
//								REST_CLEINT_POST ea5=new REST_CLEINT_POST("192.168.1.171",7778);
//								ea5.rest_topology();
								
								
//								REST_CLEINT_POST ea5=new REST_CLEINT_POST("192.168.1.171",7778);
//								ea5.rest_topology();
								
//								REST_CLEINT_POST ea6=new REST_CLEINT_POST("192.168.1.172",7777);
//								ea6.rest_topology();
//							}
//							
//							if (global.rest_meterProfile.size()==20) {
//								REST_AMI_CLIENT_GET ami=new REST_AMI_CLIENT_GET("192.168.1.171",7778);
//								ami.rest_topology();
//							}
							

						}
					});

//			           serverChannel.closeFuture().sync();
				} finally {
					// Gracefully shutdown both event loop groups
//			           serverWorkgroup.shutdownGracefully();
					clientWorkgroup.shutdownGracefully();
				}

			}

			// get
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
								System.err.println("get_REST_MSG LOG" + rest_msg);
//								HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
//										"/EMA/topology");

//								HttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpMethod.GET);

//								if(!(rest_msg.contains("201")&& rest_msg!=null)) {
//									parsing(rest_msg);
//								}

								if (rest_msg.contains("EMA") && rest_msg != null) {
									parsing(rest_msg);
								}

								// policy
								if (rest_msg.contains("recvTy") && rest_msg != null) {
									policy_parsing(rest_msg);
								}

							}
						});
					}
				});
				return b;

			}

		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 10000, 3000);

	}

	public void policy_parsing(String message) throws JSONException {

		// 공백 제거 파싱
//		message=message.replace(" ", "");

		JSONObject json = new JSONObject(message);

//		System.out.println("Parsing Result:" + json.toString());

		if (json.has("recvTy")) {
			UpdatePolicyInfo(json);
		} 
		

	}

	public void UpdatePolicyInfo(JSONObject json) throws JSONException {

		if (json.has("recvOptn")) {
			String Policy = "Policy";
			REST_EMSProfile rems = new REST_EMSProfile(Policy);
			global.rest_EMSProfile.put(Policy, rems);

			String recvOptnArray = json.getString("recvOptn");

			JSONObject recvOptnObject = new JSONObject(recvOptnArray);

			if (recvOptnObject.has("switchs")) {

//				System.out.println("## Location : : SEMA ");

				JSONArray switchArray = recvOptnObject.getJSONArray("switchs");
//				System.out.println("##### swtich NUMBER : : :  " + switchArray.length());

				for (int i = 0; i < 3; i++) {

//		               JSONObject obj = switchArray.getJSONObject(i);

					String switchID = "SWITCH" + String.valueOf(i + 1);

					REST_SemaProfile sema = new REST_SemaProfile(Policy, switchID);

					// r
					global.rest_SemaProfile.put(switchID, sema);

				}

			}

		}

	}

	public void parsing(String message) throws JSONException {

		// 공백 제거 파싱
//		message=message.replace(" ", "");

		JSONObject json = new JSONObject(message);

		System.out.println("Parsing Result:" + json.toString());

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
		String DEVICETYPE=json.getString("DEVICETYPE");
		
//		String EMAID=json.getString("EMAID");
		
		
		REST_EMAProfile rems = new REST_EMAProfile(EMSID,DEVICETYPE);
		global.rest_EMAProfile.put(EMSID, rems);

		System.out.println("EMS Collection" + global.rest_EMSProfile.values().toString());

		
		
		if (json.has("SEMAIDs")) {

			JSONArray json_array = json.getJSONArray("SEMAIDs");
			for (int i = 0; i < json_array.length(); i++) {
				JSONObject json_object = json_array.getJSONObject(i);
				UpdateSEMAInfo(json_object, EMSID);

			}

		}
		
		
	
		
		


	}
	
	
//	public void UpdateEMSInfo(JSONObject json, String EMAID) throws JSONException {
//		String EMSID = json.getString("EMAID");
//		String DeviceType=json.getString("DEVICETYPE");
//		
//
//
//		if (json.has("SEMAIDs")) {
////			System.out.println("여기 로그 확인 + SEMSIDs has ?!");
//
//			JSONArray json_array = json.getJSONArray("SEMAIDs");
//			for (int i = 0; i < json_array.length(); i++) {
//				JSONObject json_object = json_array.getJSONObject(i);
//				UpdateSEMAInfo(json_object, EMSID);
//
//			}
//
//		}
//	}

	public void UpdateSEMAInfo(JSONObject json, String EMSID) throws JSONException {
		String SEMAID = json.getString("SEMAID");
		String DeviceType=json.getString("DEVICETYPE");
//		Double threshold = json.getDouble("threshold");
//		Double maxValue = json.getDouble("maxValue");
//		Double minValue = json.getDouble("minValue");
//		int emaCNT = json.getInt("emaCNT");
		
		
		
		// 추가 : 중복제거, replace
//		REST_SemaProfile sema = new REST_SemaProfile(EMSID, SEMAID,DeviceType,maxValue,minValue,threshold,emaCNT);
		REST_SemaProfile sema=new REST_SemaProfile(EMSID,SEMAID);
				
		
		
//		if (global.rest_SemaProfile.isEmpty()) {
//			global.rest_SemaProfile.put(SEMAID, sema);
//		}

		REST_SemaProfile r = global.rest_SemaProfile.putIfAbsent(SEMAID, sema);

//		System.out.println("SEMA Collection" + global.rest_SemaProfile.values().toString());

		if (r == null) {
			global.rest_SemaProfile.put(SEMAID, sema);
		}

		else if (sema.getEmaID() != null && !(sema.getEmaID().equals(r.getEmaID()))) {
			global.rest_SemaProfile.replace(SEMAID, sema);
		}

//		System.out.println("여기 SEMA의 값은?!" + sema.getEmaID() + sema.getsubID());

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
		String deviceType=json.getString("DEVICETYPE");
//		double maxValue=json.getDouble("maxPower");
//		double minValue=json.getDouble("minValue");
//		double threshold=json.getDouble("threshold");
		
		

//		REST_CemaProfile cema = new REST_CemaProfile(SEMAID, CEMAID,deviceType,maxValue,minValue,threshold);
		
		REST_CemaProfile cema = new REST_CemaProfile(SEMAID, CEMAID,deviceType);

		
		
//		if (global.rest_CemaProfile.isEmpty()) {
//			global.rest_CemaProfile.put(CEMAID, cema);
//		} 

//		else {
		REST_CemaProfile r = global.rest_CemaProfile.putIfAbsent(CEMAID, cema);

//		System.out.println("CEMA Collection" + global.rest_CemaProfile.values());

		if (r == null) {
			global.rest_CemaProfile.put(CEMAID, cema);
		}

		else if (cema.getEmaID() != null && !(cema.getEmaID().equals(r.getEmaID()))) {
			global.rest_CemaProfile.replace(CEMAID, cema);
		}
//		}

//		System.out.println("여기 CEMA의 값은?!" + cema.getEmaID() + cema.getDeviceEMAID());

		// Update CEMA info using SEMAID, CEMAID

		if (json.has("DEVICEIDs")) {
			JSONArray json_array = json.getJSONArray("DEVICEIDs");
			for (int i = 0; i < json_array.length(); i++) {
				JSONObject json_object = json_array.getJSONObject(i);
				UpdateDEVICEInfo(json_object, CEMAID);
			}
		}
	}

	public void UpdateDEVICEInfo(JSONObject json, String CEMAID) throws JSONException {
		String DEVICEID = json.getString("DEVICEID");
		String deviceType=json.getString("DEVICETYPE");
		
		REST_DeviceProfile device = new REST_DeviceProfile(CEMAID, DEVICEID,deviceType);


		REST_DeviceProfile r = global.rest_deviceProfile.putIfAbsent(DEVICEID, device);
		

//		System.out.println("DEVICE Collection" + global.rest_deviceProfile.values().toString());

//		System.out.println("device" + device);

//		System.out.println("r" + r);

		if (r == null) {
			global.rest_deviceProfile.put(DEVICEID, device);
		} else if (device.getEmaID() != null && !(device.getEmaID().equals(r.getEmaID()))) {
			global.rest_deviceProfile.replace(DEVICEID, device);
		}
//		}

//		System.out.println("여기 DEVICE의 값은?!" + device.getEmaID() + device.getDeviceEMAID());

		// Update SEMA info using CEMAID, DEVICEID
	}

	private static final Logger log = LoggerFactory.getLogger(REST_CLIENT_GET.class);

}
