package com.keke.bootext.dubbo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 */
@Data
@ConfigurationProperties(prefix = "dubbo.protocol")
public class DubboProtocol {

    /**
     * 接口协议
     */
    private String name = "dubbo";
    /**
     * 暴露服务的端口
     */
    private int port = 20880;

    /**
     * dubbo thread count, default 200
     */
    private int threads = 200;

    /**
     * 是否记录接口日志
     */
    private boolean accessLog = true;
}
