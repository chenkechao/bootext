package com.keke.bootext.rocketmq.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import com.keke.bootext.rocketmq.annotation.EnableMQConfiguration;
import com.keke.bootext.rocketmq.base.AbstractRocketMQProducer;
import com.keke.bootext.rocketmq.base.AbstractRocketMQPushConsumer;

@Configuration
@ConditionalOnBean(annotation = EnableMQConfiguration.class)
@AutoConfigureAfter({AbstractRocketMQProducer.class, AbstractRocketMQPushConsumer.class})
@EnableConfigurationProperties(RocketMQProperties.class)
public class RocketMQBaseAutoConfiguration implements ApplicationContextAware {

    @Autowired
    protected RocketMQProperties mqProperties;


    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

