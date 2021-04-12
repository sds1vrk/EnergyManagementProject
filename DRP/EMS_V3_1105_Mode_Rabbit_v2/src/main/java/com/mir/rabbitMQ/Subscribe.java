package com.mir.rabbitMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Subscribe extends Thread {

	public Subscribe() {

	}

	public void run() {

		final String EXCHANGE_NAME = RabbitGlobal.sub_exchangeName;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitGlobal.host);
		factory.setPort(RabbitGlobal.port);
		factory.setUsername(RabbitGlobal.userName);
		factory.setPassword(RabbitGlobal.password);
		factory.setVirtualHost(RabbitGlobal.virtualHost);

		Connection connection;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			String queueName = channel.queueDeclare().getQueue();
			System.out.println("QUE" + queueName);

			channel.queueBind(queueName, EXCHANGE_NAME, "");

			System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			};
			channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
