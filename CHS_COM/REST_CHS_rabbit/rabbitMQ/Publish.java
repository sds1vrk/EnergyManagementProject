package com.mir.rabbitMQ;

import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.mir.rabbitMQ.*;

public class Publish extends Thread {

	final static String EXCHANGE_NAME = RabbitGlobal.pub_exhcangeName;

	public Publish() {

	}

	public void run() {
		super.run();
		Timer timer = new Timer();
		timer.schedule(new UpdateTask(), 0, 3000);

	}

	private class UpdateTask extends TimerTask {
		@Override
		public void run() {

			try {
				send();

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}

	}

	public static void send() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitGlobal.host);
		factory.setPort(RabbitGlobal.port);
		factory.setUsername(RabbitGlobal.userName);
		factory.setPassword(RabbitGlobal.password);
		factory.setVirtualHost(RabbitGlobal.virtualHost);

		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(RabbitGlobal.pub_exhcangeName, "fanout");

			String message = "AMIInformation";

			channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
//            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//            channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));

			System.out.println(" [x] Sent '" + message + "'");
		}
	}

}
