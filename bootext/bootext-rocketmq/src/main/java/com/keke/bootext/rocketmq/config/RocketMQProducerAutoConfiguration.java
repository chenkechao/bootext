package com.keke.bootext.rocketmq.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.keke.bootext.rocketmq.annotation.RocketMQProducer;
import com.keke.bootext.rocketmq.base.AbstractRocketMQProducer;

@Configuration
@ConditionalOnBean(RocketMQBaseAutoConfiguration.class)
public class RocketMQProducerAutoConfiguration extends RocketMQBaseAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RocketMQProducerAutoConfiguration.class);

    private DefaultMQProducer producer;

    @PostConstruct
    public void init() throws Exception {
        if (producer == null) {
            if (StringUtils.isEmpty(mqProperties.getProducerGroup())) {
                throw new RuntimeException("请在配置文件中指定消息发送方group！");
            }
            producer = new DefaultMQProducer(mqProperties.getProducerGroup());
            producer.setNamesrvAddr(mqProperties.getNameServerAddress());
            producer.start();
        }
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RocketMQProducer.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            publishProducer(entry.getKey(), entry.getValue());
        }
    }


    private void publishProducer(String beanName, Object bean) throws Exception {
        if (!AbstractRocketMQProducer.class.isAssignableFrom(bean.getClass())) {
            throw new RuntimeException(beanName + " - producer未继承AbstractRocketMQProducer");
        }
        AbstractRocketMQProducer abstractMQProducer = (AbstractRocketMQProducer) bean;
        abstractMQProducer.setProducer(producer);
        // begin build producer level topic
        RocketMQProducer mqProducer = applicationContext.findAnnotationOnBean(beanName, RocketMQProducer.class);
        String topic = mqProducer.topic();
        if (!StringUtils.isEmpty(topic)) {
            String transTopic = applicationContext.getEnvironment().getProperty(topic);
            if (StringUtils.isEmpty(transTopic)) {
                abstractMQProducer.setTopic(topic);
            } else {
                abstractMQProducer.setTopic(transTopic);
            }
        }
        // begin build producer level tag
        String tag = mqProducer.tag();
        if (!StringUtils.isEmpty(tag)) {
            String transTag = applicationContext.getEnvironment().getProperty(tag);
            if (StringUtils.isEmpty(transTag)) {
                abstractMQProducer.setTag(tag);
            } else {
                abstractMQProducer.setTag(transTag);
            }
        }
        log.info(String.format("%s is ready to produce message", beanName));
    }
}

