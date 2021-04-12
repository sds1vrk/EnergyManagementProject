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
public class REST_CLIENT_AMI_Post extends Thread {

	String ip;

	REST_Structure rs;

	int port;

	String post_rest;
//	public ArrayList<String>arr;

//	EchoApplication ea;
	public REST_CLIENT_AMI_Post(String ip, int port) {
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



							// 여기부터 Post
							HttpRequest request_post = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
									"/AMI/ami");
							
							request_post.headers().add(HttpHeaderNames.CONTENT_TYPE,"application/json");
							
							request_post.headers().add(HttpHeaderNames.CONNECTION,"keep-alive");
							
							request_post.headers().add(HttpHeaderNames.HOST,ip);
							
						
							if(global.rest_reportType.contains("Expli")) {
								 post_rest=REST_Structure.ami_explicit_rest();
								
							}
						
							
							else if(global.rest_reportType.contains("Impli")){
								 post_rest=REST_Structure.implicit_rest();
								
							}
							
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
//								if (rest_msg.contains("EMA") && rest_msg != null) {
//									parsing(rest_msg);
//								}
//
//								// policy
//								if (rest_msg.contains("recvTy") && rest_msg != null) {
//									policy_parsing(rest_msg);
//								}

							}
						});
					}
				});
				return b;

			}
			
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(chartUpdaterTask, 50000, 10000);


	}
}
