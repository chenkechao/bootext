package com.keke.bootext.dubbo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dubbo.service")
public class DubboService {
    private String group = "group";

    private String version = "version";
}
