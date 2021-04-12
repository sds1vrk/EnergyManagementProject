package com.mir.rabbitTest;
import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLog {

    private static final String EXCHANGE_NAME = "logs1";
    
    private final static String QUEUE_NAME = "test2";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.173");
        factory.setPort(5672);  
        factory.setUsername("username"); 
        factory.setPassword("password");  
        factory.setVirtualHost("client");  
        
        
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            String message = argv.length < 1 ? "info: Hello World!" :
                    String.join(" ", argv);

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
//            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//            channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            
            System.out.println(" [x] Sent '" + message + "'");
        }
    }

}
