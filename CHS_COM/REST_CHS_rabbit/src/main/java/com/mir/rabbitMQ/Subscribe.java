package com.mir.rabbitMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import com.mir.rest.clent.*;

public class Subscribe extends Thread {
	
	
	final String EXCHANGE_NAME;

	public Subscribe(String exchange_name) {
		
		EXCHANGE_NAME=exchange_name;
	}

	public void run() {

		
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
				
				
				
				try {
					
					if(message.contains("MDMS")) {
						
						REST_AMI_CLIENT_GET ami=new REST_AMI_CLIENT_GET();
						ami.parsing(message);
					}
					else if(message.contains("DRP")) {
						REST_CLIENT_GET parse=new REST_CLIENT_GET();
						parse.parsing(message);
					}
					
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
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
