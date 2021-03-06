package com.ljw.springcloud.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: qingshan
 * @Date: 2018/10/20 17:04
 * @Description: 咕泡学院，只为更好的你
 */
@Component
@PropertySource("classpath:rabbitMqConfig.properties")
@RabbitListener(queues = "${com.ljw.secondqueue}", containerFactory="rabbitListenerContainerFactory")
public class SecondConsumer {
    @RabbitHandler
    public void process(String msgContent, Channel channel, Message message) throws IOException {
        System.out.println("Second Queue received msg : " + msgContent );
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
