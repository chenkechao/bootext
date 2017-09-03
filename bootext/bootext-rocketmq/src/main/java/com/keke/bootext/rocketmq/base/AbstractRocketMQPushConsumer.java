package com.keke.bootext.rocketmq.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public abstract class AbstractRocketMQPushConsumer<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractRocketMQPushConsumer.class);

    public AbstractRocketMQPushConsumer() {
    }

    private static Gson gson = new Gson();

    /**
     * 继承这个方法处理消息
     *
     * @param message 消息范型
     * @return
     */
    public abstract boolean process(T message);

    /**
     * 原生dealMessage方法，可以重写此方法自定义序列化和返回消费成功的相关逻辑
     *
     * @param list                       消息列表
     * @param consumeConcurrentlyContext 上下文
     * @return
     */
    public ConsumeConcurrentlyStatus dealMessage(List<MessageExt> list,
                                                 ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        for (MessageExt messageExt : list) {
            T t = parseMessage(messageExt);
            if (null != t && !process(t)) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    /**
     * 反序列化解析消息
     *
     * @param message 消息体
     * @return
     */
    @SuppressWarnings("unchecked")
    private T parseMessage(MessageExt message) {
        if (message == null || message.getBody() == null) {
            return null;
        }
        final Type type = this.getMessageType();
        if (type instanceof Class) {
            Object data = gson.fromJson(new String(message.getBody()), type);
            return (T) data;
        } else {
            log.warn("Parse msg error. {}", message);
            return null;
        }
    }

    /**
     * 解析消息类型
     *
     * @return
     */
    private Type getMessageType() {
        Type superType = this.getClass().getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            return ((ParameterizedType) superType).getActualTypeArguments()[0];
        } else {
            throw new RuntimeException("Unkown parameterized type.");
        }
    }
}

