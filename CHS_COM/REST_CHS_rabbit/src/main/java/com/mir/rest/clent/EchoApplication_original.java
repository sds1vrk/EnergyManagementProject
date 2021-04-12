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
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.ResourceLeakDetector;
import scala.util.parsing.json.JSON;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mir.ems.globalVar.global;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * A simple HTTP ECHO application
 */
public class EchoApplication_original extends Thread{
   
   
   @Override
      public void run() {
      
      
         super.run();
      }
   
   
   
   
   
    private static final Logger log = LoggerFactory.getLogger(EchoApplication_original.class);
    
    

    public Bootstrap client() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //HttpClient codec is a helper ChildHandler that encompasses
                        //both HTTP response decoding and HTTP request encoding
                        ch.pipeline().addLast(new HttpClientCodec());
                        //HttpObjectAggregator helps collect chunked HttpRequest pieces into
                        //a single FullHttpRequest. If you don't make use of streaming, this is
                        //much simpler to work with.
                        ch.pipeline().addLast(new HttpObjectAggregator(1048576));
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpResponse>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
                                final String rest_msg = msg.content().toString(CharsetUtil.UTF_8);
                                
                                ConcurrentHashMap<String, Object> cs=new ConcurrentHashMap<String, Object>();
                                
//                                msg.get
                                
                                
                                
                                log.info("Response: {}", rest_msg);
                                System.err.println(rest_msg);
                                           
                                
                                REST_Structure rs=new REST_Structure();
                                rs.setData(rest_msg);
                               
                                
                                
                            }
                        });
                    }
                });
        return b;
    }

    public EchoApplication_original() {
        //I find while learning Netty to keep resource leak detecting at Paranoid,
        //however *DO NOT* ship with this level.
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
//        EventLoopGroup serverWorkgroup = new NioEventLoopGroup();
        EventLoopGroup clientWorkgroup = new NioEventLoopGroup();
//        EchoApplication app = new EchoApplication();
        try {

           String ip=global.EMAIP;
            Bootstrap clientBootstrap = this.client();
//            final ByteBuf content = Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8);
            clientBootstrap
                    .connect(ip,7777)
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            // Prepare the HTTP request.
                            HttpRequest request = new DefaultFullHttpRequest(
                                    HttpVersion.HTTP_1_1, HttpMethod.GET, "/EMA/topology");
                          
                            future.channel().writeAndFlush(request);
                        }
                    });

//            serverChannel.closeFuture().sync();
            }finally {
            //Gracefully shutdown both event loop groups
//            serverWorkgroup.shutdownGracefully();
            clientWorkgroup.shutdownGracefully();
        }
    }


}