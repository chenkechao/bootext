package com.keke.bootext.rocketmq;

import com.keke.bootext.rocketmq.annotation.EnableMQConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMQConfiguration
public class BootextRocketmqApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootextRocketmqApplication.class, args);
    }
}
