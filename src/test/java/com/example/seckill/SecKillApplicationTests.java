package com.example.seckill;

import com.example.seckill.utils.UserUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
@SpringBootTest
class SecKillApplicationTests {

    @Autowired
    private UserUtil userUtil;

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateUser() throws Exception {
        userUtil.CreateUser();
        System.out.println("User creation process completed.");
    }

    @Test
    void rabbitMQTest(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.200.141");
        factory.setPort(5673);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");

        try (Connection connection = factory.newConnection()) {
            System.out.println("Connected to RabbitMQ!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
