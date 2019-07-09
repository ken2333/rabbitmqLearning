package com.sun.rabbitmq.productor;

import com.rabbitmq.client.ConnectionFactory;

/**
 * @author ken
 * @date 2019/7/2  20:38
 * @description
 */
public class ConnectionUnit {

    public static ConnectionFactory getConnectionFactory()
    {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //   connectionFactory.setVirtualHost("");
        connectionFactory.setHost("192.168.100.2");
        connectionFactory.setPort(5672);
        return  connectionFactory;
    }
}
