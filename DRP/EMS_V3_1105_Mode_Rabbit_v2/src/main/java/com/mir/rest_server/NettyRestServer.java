package com.mir.rest_server;

import java.util.Collection;

import javax.annotation.PreDestroy;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.mir.ems.globalVar.global;

@Component
public class NettyRestServer {
//	private static final Logger logger = LoggerFactory.getLogger(NettyRestServer.class);
	
	@Autowired
	ApplicationContext ac;

	private NettyJaxrsServer netty;

	public void start() {
		ResteasyDeployment dp = new ResteasyDeployment();
		Collection<Object> providers = ac.getBeansWithAnnotation(Provider.class).values();
		Collection<Object> controllers = ac.getBeansWithAnnotation(Controller.class).values();
		Assert.notEmpty(controllers);
		if (providers != null) {
			dp.getProviders().addAll(providers);
		}
		dp.getResources().addAll(controllers);
		netty = new NettyJaxrsServer();
		netty.setDeployment(dp);
		netty.setPort(global.rest_port);
		netty.setRootResourcePath("/");
		netty.start();
//		logger.info("Server started on port 7777");
	}

	@PreDestroy
	public void cleanUp() {
		netty.stop();
	}

	public NettyRestServer setAc(ApplicationContext ac) {
		this.ac = ac;
		return this;
	}
}
