package com.keke.bootext.dubbo.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import com.keke.bootext.dubbo.properties.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.keke.bootext.dubbo.annotation.EnableDubboAutoConfiguration;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.config.spring.ServiceBean;

/**
 * DubboProviderAutoConfiguration
 *
 * @author fangzhibin
 */
@Configuration
@ConditionalOnClass(Service.class)
@ConditionalOnBean(annotation = EnableDubboAutoConfiguration.class)
@AutoConfigureAfter(DubboAutoConfiguration.class)
@EnableConfigurationProperties(DubboService.class)
public class DubboProviderAutoConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DubboService dubboService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private ProtocolConfig protocolConfig;
    @Autowired
    private RegistryConfig registryConfig;

    @PostConstruct
    public void init() throws Exception {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(Service.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            this.initProviderBean(entry.getKey(), entry.getValue());
        }
    }

    public void initProviderBean(String beanName, Object bean) throws Exception {
        Service service = this.applicationContext.findAnnotationOnBean(beanName, Service.class);
        ServiceBean<Object> serviceConfig = new ServiceBean<Object>(service);
        if (void.class.equals(service.interfaceClass()) && "".equals(service.interfaceName())) {
            if (bean.getClass().getInterfaces().length > 0) {
                serviceConfig.setInterface(bean.getClass().getInterfaces()[0]);
            } else {
                throw new IllegalStateException(
                        "Failed to export remote service class "
                                + bean.getClass().getName()
                                + ", cause: The @Service undefined interfaceClass or interfaceName, and the service class unimplemented any interfaces.");
            }
        }
        String version = service.version();
        if (version == null || "".equals(version)) {
            version = this.dubboService.getVersion();
        }
        if (version != null && !"".equals(version)) {
            serviceConfig.setVersion(version);
        }
        String group = service.group();
        if (group == null || "".equals(group)) {
            group = this.dubboService.getGroup();
        }
        if (group != null && !"".equals(group)) {
            serviceConfig.setGroup(group);
        }
        serviceConfig.setApplicationContext(this.applicationContext);
        serviceConfig.setApplication(this.applicationConfig);
        serviceConfig.setProtocol(this.protocolConfig);
        serviceConfig.setRegistry(this.registryConfig);
        serviceConfig.afterPropertiesSet();
        serviceConfig.setRef(bean);
        serviceConfig.export();
    }

}
