package com.keke.bootext.rocketmq;


import com.keke.bootext.rocketmq.annotation.RocketMQProducer;
import com.keke.bootext.rocketmq.base.AbstractRocketMQProducer;

@RocketMQProducer
public class TestProduer extends AbstractRocketMQProducer {


    public void sendMessage() {
        synSend("topic", "tag", "i am test!");
    }


}
