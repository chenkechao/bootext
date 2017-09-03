package com.keke.bootext.rocketmq;

import com.keke.bootext.rocketmq.annotation.RocketMQProducer;
import com.keke.bootext.rocketmq.base.AbstractRocketMQProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BootextRocketmqApplication.class)
public class BootextRocketmqApplicationTest {

    @Autowired
    private TestProduer testProducer;

    @Test
    public void testSend(){
        testProducer.sendMessage();
    }

}

