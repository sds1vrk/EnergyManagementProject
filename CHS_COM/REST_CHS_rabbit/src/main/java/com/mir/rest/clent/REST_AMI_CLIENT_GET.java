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
public class REST_AMI_CLIENT_GET extends Thread {

	String ip;

	REST_Structure rs;

	int port;

	String post_rest;
//	public ArrayList<String>arr;

//	EchoApplication ea;

	public REST_AMI_CLIENT_GET() {

	}

	public REST_AMI_CLIENT_GET(String ip, int port) {
		this.ip = ip;
//		this.arr=new ArrayList<>();
//		System.out.println("IP?!" + ip);
		this.port = port;

	}

	public void rest_topology() {
		TimerTask chartUpdaterTask = new TimerTask() {

			@Override
			public void run() {

//				System.out.println("계속 run?!");

//				try {
//					sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

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
									"/AMI/topology");

							future.channel().writeAndFlush(request);

//							System.err.println(global.rest_meterProfile.size()+"meter SIZE");
//							if (global.rest_meterProfile.size()==20) {
//								REST_CLEINT_POST ea5=new REST_CLEINT_POST("192.168.1.171",7777);
//								ea5.rest_topology();
//								
////								REST_CLEINT_POST ea6=new REST_CLEINT_POST("192.168.1.172",7777);
////								ea6.rest_topology();
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

//								log.info("Response: {}", rest_msg);
								System.err.println("get_REST_MSG LOG" + rest_msg);

								parsing(rest_msg);

							}
						});
					}
				});
				return b;

			}

		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 1000, 5000);

	}

	public void parsing(String message) throws JSONException {

		// 공백 제거 파싱
//		message=message.replace(" ", "");

		JSONObject json = new JSONObject(message);

//		System.out.println("Parsing Result:" + json.toString());

		if (json.has("EMSID")) {
			UpdateEMSInfo(json);
		} else if (json.has("DCUID")) {
			UpdateCEMAInfo(json, null);
		} else if (json.has("METERID")) {
			UpdateDEVICEInfo(json, null);
		}

	}

	public void UpdateEMSInfo(JSONObject json) throws JSONException {

		String EMSID = json.getString("EMSID");
		REST_EMSProfile rems = new REST_EMSProfile(EMSID);
		global.rest_MDMS_EMSProfile.put(EMSID, rems);

		if (json.has("DCUIDs")) {

			JSONArray json_array = json.getJSONArray("DCUIDs");
			for (int i = 0; i < json_array.length(); i++) {
				JSONObject json_object = json_array.getJSONObject(i);
				UpdateCEMAInfo(json_object, EMSID);

			}

		}

	}

	public void UpdateCEMAInfo(JSONObject json, String SEMAID) throws JSONException {
		String CEMAID = json.getString("DCUID");
		String deviceType = json.getString("DEVICETYPE");

		REST_DcuProfile cema = new REST_DcuProfile(SEMAID, CEMAID, deviceType);

//		if (global.rest_CemaProfile.isEmpty()) {
//			global.rest_CemaProfile.put(CEMAID, cema);
//		} 

//		else {
		REST_DcuProfile r = global.rest_DCUProfile.putIfAbsent(CEMAID, cema);

//		System.out.println("CEMA Collection" + global.rest_CemaProfile.values());

		if (r == null) {
			global.rest_DCUProfile.put(CEMAID, cema);
		}

		else if (cema.getEmaID() != null && !(cema.getEmaID().equals(r.getEmaID()))) {
			global.rest_DCUProfile.replace(CEMAID, cema);
		}
//		}

//		System.out.println("여기 CEMA의 값은?!" + cema.getEmaID() + cema.getDeviceEMAID());

		// Update CEMA info using SEMAID, CEMAID

//		if (json.has("DEVICEs")) {
		if (json.has("METERIDs")) {
			JSONArray json_array = json.getJSONArray("METERIDs");
			for (int i = 0; i < json_array.length(); i++) {
				JSONObject json_object = json_array.getJSONObject(i);
				UpdateDEVICEInfo(json_object, CEMAID);
			}
		}
	}

	public void UpdateDEVICEInfo(JSONObject json, String CEMAID) throws JSONException {
		String DEVICEID = json.getString("METERID");
		String deviceType = json.getString("DEVICETYPE");

		REST_MeterProfile device = new REST_MeterProfile(CEMAID, DEVICEID, deviceType);

		REST_MeterProfile r = global.rest_meterProfile.putIfAbsent(DEVICEID, device);

//		System.out.println("DEVICE Collection" + global.rest_deviceProfile.values().toString());

//		System.out.println("device" + device);

//		System.out.println("r" + r);

		if (r == null) {
			global.rest_meterProfile.put(DEVICEID, device);
		} else if (device.getEmaID() != null && !(device.getEmaID().equals(r.getEmaID()))) {
			global.rest_meterProfile.replace(DEVICEID, device);
		}
//		}

//		System.out.println("여기 DEVICE의 값은?!" + device.getEmaID() + device.getDeviceEMAID());

		// Update SEMA info using CEMAID, DEVICEID
	}

	private static final Logger log = LoggerFactory.getLogger(REST_AMI_CLIENT_GET.class);

}
