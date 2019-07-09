package com.sun.rabbitmq.productor;

import com.rabbitmq.client.*;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author ken
 * @date 2019/7/6  11:48
 * @description
 */
public class test {


    //发送消息
    @Test
    public void test() {
        ConnectionFactory connectionFactory = ConnectionUnit.getConnectionFactory();
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel(1);
            //channel.exchangeDeclare();
            channel.queueDeclare("queue",true,false,false,null);
            channel.exchangeDeclare("exchang", BuiltinExchangeType.DIRECT);
            channel.queueBind("queue", "exchang", "key");
            channel.basicPublish("exchang", "key",
                    (new AMQP.BasicProperties().builder().
                            contentType("text/plain").//设置内容
                            contentEncoding("utf-8").//设置编码
                            deliveryMode(2).//设置投递的模式
                            priority(1).//设置优先度
                            build()),
                    "你好".getBytes());


            HashMap<String,Object> hedeas=new HashMap<>();
            channel.basicPublish("exchang", "key",
                    (new AMQP.BasicProperties().builder().
                            expiration("6000").
                            build()),
                    "你好2".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    //接受消息
    public static void recevice()
    {
        ConnectionFactory connectionFactory = ConnectionUnit.getConnectionFactory();
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            Channel   channel = connection.createChannel(1);
            //获取指定队列的消息,第二个参数表示是否自动签收,这里表示否,最后一个表示接受到消息后的处理方法
            channel.basicConsume("queue", false,"",new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //super.handleDelivery(consumerTag, envelope, properties, body);
                    System.out.println(envelope.getRoutingKey());  //获取RoutingKey
                    System.out.println(properties.getContentType());//获取内容的类型
                    System.out.println(properties.getContentEncoding());//获取内容的编码
                    System.out.println(new String(body,"utf-8"));//输出消息
                    //向系统确认签收这个消息
             /*       long deliveryTag = envelope.getDeliveryTag();
                    channel.basicAck(deliveryTag,false);*/
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }


    //拉消息
    public static void get()
    {
        ConnectionFactory connectionFactory = ConnectionUnit.getConnectionFactory();
        Connection connection = null;


        try {
            connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel(1);
            GetResponse queue = channel.basicGet("queue", false);
            //输出消息
            System.out.println(new String(queue.getBody(),"utf-8"));
            Envelope envelope = queue.getEnvelope();
            //签收
            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
      //   test.get();
    //    recevice();

    }
}
