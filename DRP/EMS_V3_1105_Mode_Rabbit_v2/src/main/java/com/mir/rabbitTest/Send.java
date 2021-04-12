package com.mir.rabbitTest;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class Send {

    private final static String QUEUE_NAME = "test1";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.173");
        factory.setPort(5672);  
        factory.setUsername("username");  
        factory.setPassword("password");  
        factory.setVirtualHost("client");  
        
        
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}