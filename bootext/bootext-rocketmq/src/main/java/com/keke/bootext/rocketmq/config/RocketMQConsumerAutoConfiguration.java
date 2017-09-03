package com.keke.bootext.rocketmq.config;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.keke.bootext.rocketmq.annotation.RocketMQConsumer;
import com.keke.bootext.rocketmq.base.AbstractRocketMQPushConsumer;

@Configuration
@ConditionalOnBean(RocketMQBaseAutoConfiguration.class)
public class RocketMQConsumerAutoConfiguration extends RocketMQBaseAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RocketMQConsumerAutoConfiguration.class);

    @PostConstruct
    public void init() throws Exception {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RocketMQConsumer.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            publishConsumer(entry.getKey(), entry.getValue());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void publishConsumer(String beanName, Object bean) throws Exception {
        RocketMQConsumer mqConsumer = applicationContext.findAnnotationOnBean(beanName, RocketMQConsumer.class);
        if (StringUtils.isEmpty(mqConsumer.consumerGroup())) {
            throw new RuntimeException("consumer's consumerGroup must be defined");
        }
        if (StringUtils.isEmpty(mqConsumer.topic())) {
            throw new RuntimeException("consumer's topic must be defined");
        }
        String consumerGroup = applicationContext.getEnvironment().getProperty(mqConsumer.consumerGroup());
        if (StringUtils.isEmpty(consumerGroup)) {
            consumerGroup = mqConsumer.consumerGroup();
        }
        String topic = applicationContext.getEnvironment().getProperty(mqConsumer.topic());
        if (StringUtils.isEmpty(topic)) {
            topic = mqConsumer.topic();
        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(mqProperties.getNameServerAddress());
        consumer.subscribe(topic, mqConsumer.tag());
        consumer.setInstanceName(UUID.randomUUID().toString());
        consumer.registerMessageListener(
                (List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) -> {
                    if (!AbstractRocketMQPushConsumer.class.isAssignableFrom(bean.getClass())) {
                        throw new RuntimeException(bean.getClass().getName() + " - consumer未实现IMQPushConsumer接口");
                    }
                    AbstractRocketMQPushConsumer abstractMQPushConsumer = (AbstractRocketMQPushConsumer) bean;
                    return abstractMQPushConsumer.dealMessage(list, consumeConcurrentlyContext);
                });
        consumer.start();
        log.info(String.format("%s is ready to subscribe message", bean.getClass().getName()));
    }
}

