package com.keke.bootext.dubbo.endpoint;

import com.keke.bootext.dubbo.domain.ClassIdBean;
import com.keke.bootext.dubbo.listener.ConsumerSubscribeListener;
import com.keke.bootext.dubbo.listener.ProviderExportListener;
import com.keke.bootext.dubbo.listener.StaticsFilterHelper;
import com.keke.bootext.dubbo.metrics.DubboMetrics;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.keke.bootext.dubbo.properties.DubboApplication;
import com.keke.bootext.dubbo.properties.DubboProtocol;
import com.keke.bootext.dubbo.properties.DubboRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.stereotype.Component;


/**
 * dubbo endpoint for provider and subscriber
 *
 * @author fangzhibin
 *
 */
@Component
public class DubboEndpoint extends AbstractEndpoint<Map<String, Object>> {
    @Autowired
    private DubboApplication dubboApplication;
    @Autowired
    private DubboRegistry dubboRegistry;
    @Autowired
    private DubboProtocol dubboProtocol;

    @Autowired
    private DubboMetrics dubboMetrics;

    @Override
    public Map<String, Object> invoke() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("endpoint", this.buildEndpoint());
        result.put("metrics", this.getMetrics());
        result.put("config", this.getConfig());//TODO
        result.put("runtime", this.getRuntime());
        return result;
    }

    private Object buildEndpoint() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("name", this.getName());
        result.put("version", this.getVersion());
        result.put("authors", this.getAuthors());
        result.put("docs", this.getDocs());
        result.put("scm", this.getScm());
        return result;
    }

    public DubboEndpoint() {
        super("dubbo");
    }

    public String getName() {
        return "dubbo";
    }

    public String getVersion() {
        return "1.0.0";
    }

    public List<String> getAuthors() {
        return Collections.singletonList("jackxiong <xionghui.xh@alibaba-inc.com>");
    }

    public String getDocs() {
        return "http://dubbo.io/Developer+Guide-zh.htm";
    }

    public String getScm() {
        return "git@github.com:xionghuiCoder/spring-boot-starter-dubbo.git";
    }

    public DubboApplication getConfig() {
        return this.dubboApplication;
    }

    public Map<String, Object> getRuntime() {
        Map<String, Object> runtimeMap = new HashMap<String, Object>();

        runtimeMap.put("appname", this.dubboApplication.getName());
        runtimeMap.put("registry", this.dubboRegistry.getAddress());
        runtimeMap.put("protocol", this.dubboProtocol.getName());
        runtimeMap.put("port", this.dubboProtocol.getPort());
        runtimeMap.put("threads", this.dubboProtocol.getThreads());

        // published services
        Map<ClassIdBean, Map<String, Long>> publishedInterfaceList = new HashMap<ClassIdBean, Map<String, Long>>();
        Set<ClassIdBean> publishedInterfaceSet = ProviderExportListener.EXPORTEDINTERFACES_SET;
        for (ClassIdBean classIdBean : publishedInterfaceSet) {
            Class<?> clazz = classIdBean.getClazz();
            String interfaceClassCanonicalName = clazz.getCanonicalName();
            if (!interfaceClassCanonicalName.equals("void")) {
                Map<String, Long> methodNames = new HashMap<String, Long>();
                for (Method method : clazz.getMethods()) {
                    methodNames.put(method.getName(), StaticsFilterHelper.getValue(classIdBean, method.getName()));
                }
                publishedInterfaceList.put(classIdBean, methodNames);
            }
        }
        if (!publishedInterfaceList.isEmpty()) {
            runtimeMap.put("publishedInterfaces", publishedInterfaceList);
        }

        // subscribed services
        Set<ClassIdBean> subscribedInterfaceSet = ConsumerSubscribeListener.SUBSCRIBED_INTERFACES_SET;
        Map<ClassIdBean, Map<String, Long>> subscribedInterfaceList = new HashMap<ClassIdBean, Map<String, Long>>();
        for (ClassIdBean classIdBean : subscribedInterfaceSet) {
            Map<String, Long> methodNames = new HashMap<String, Long>();
            Class<?> clazz = classIdBean.getClazz();
            for (Method method : clazz.getMethods()) {
                methodNames.put(method.getName(), StaticsFilterHelper.getValue(classIdBean, method.getName()));
            }
            subscribedInterfaceList.put(classIdBean, methodNames);
        }
        if (!subscribedInterfaceList.isEmpty()) {
            runtimeMap.put("subscribedInterfaces", subscribedInterfaceList);
        }

        // consumer connections
        runtimeMap.put("connections", ConsumerSubscribeListener.CONNECTION_MAP);
        return runtimeMap;
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metricsMap = new HashMap<String, Object>();
        Collection<Metric<?>> metrics = this.dubboMetrics.metrics();
        for (Metric<?> metric : metrics) {
            metricsMap.put(metric.getName(), metric.toString());
        }
        return metricsMap;
    }
}
