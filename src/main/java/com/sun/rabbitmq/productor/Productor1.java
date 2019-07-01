package com.sun.rabbitmq.productor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ken
 * @date 2019/7/2  0:09
 * @description
 */

@Component
public class Productor1 {
    public static  String queue="user";

    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
     //   connectionFactory.setVirtualHost("");
        connectionFactory.setHost("192.168.100.2");
        connectionFactory.setPort(5672);
        try {
            /*
             * 声明（创建）队列
             * 参数1：队列名称
             * 参数2：为true时server重启队列不会消失
             * 参数3：队列是否是独占的，如果为true只能被一个connection使用，其他连接建立时会抛出异常
             * 参数4：队列不再使用时是否自动删除（没有连接，并且没有未处理的消息)
             * 参数5：建立队列时的其他参数
             */

            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel(1);
            channel.queueDeclare(queue,true,false,false,null);
            String message = "Hello World!";
            for (int i = 0; i < 20; i++) {
                message = message + i;
                channel.basicPublish("",queue,null,message.getBytes());
                Thread.sleep(1000);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
