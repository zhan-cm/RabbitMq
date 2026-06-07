package com.zhan.mq.test;
  
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest  
public class RabbitMQTest {

    public static final String EXCHANGE_DIRECT = "exchange.direct.order";
    public static final String EXCHANGE_TIMEOUT = "exchange.test.timeout";
    public static final String ROUTING_KEY = "order";
    public static final String ROUTING_KEY_TIMEOUT = "routing.key.test.timeout";

  
    @Autowired  
    private RabbitTemplate rabbitTemplate;
  
    @Test  
    public void testSendMessage1() {
        rabbitTemplate.convertAndSend(  
                EXCHANGE_DIRECT,
                ROUTING_KEY,   
                "Hello Chemic");
    }

    public static final String EXCHANGE_TRANSIENT = "exchange.transient.user";
    public static final String ROUTING_KEY_TRANSIENT = "user";

    @Test
    public void testSendMessageTransient() {
        rabbitTemplate.convertAndSend(
                EXCHANGE_TRANSIENT,
                ROUTING_KEY_TRANSIENT,
                "Hello  user~~~");
    }

    @Test
    public void testSendMessage02() {
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(
                    EXCHANGE_DIRECT,
                    ROUTING_KEY,
                    "Hello atguigu" + i);
        }
    }

    @Test
    public void testSendMessage03() {
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(EXCHANGE_TIMEOUT,ROUTING_KEY_TIMEOUT,"Test timeout"+i);
        }
    }

    @Test
    public void testSendMessageTTL() {

        // 1、创建消息后置处理器对象
        MessagePostProcessor messagePostProcessor = (Message message) -> {

            // 设定 TTL 时间，以毫秒为单位
            message.getMessageProperties().setExpiration("5000");

            return message;
        };

        // 2、发送消息
        rabbitTemplate.convertAndSend(
                EXCHANGE_DIRECT,
                ROUTING_KEY,
                "Hello atguigu", messagePostProcessor);
    }
}