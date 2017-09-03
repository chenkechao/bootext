package com.keke.bootext.dubbo.endpoint;

import com.keke.bootext.dubbo.listener.ProviderExportListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.keke.bootext.dubbo.properties.DubboRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;

/**
 * dubbo operation endpoint
 *
 * @author fangzhibin
 *
 */
@RestController
public class DubboOperationEndpoint implements MvcEndpoint {
    @Autowired
    private DubboRegistry dubboRegistry;

    private Registry registry;

    @RequestMapping("/online")
    public List<Map<String, String>> online() {
        List<Map<String, String>> serviceInterfaceList = new LinkedList<Map<String, String>>();
        if (!ProviderExportListener.EXPORTED_URL.isEmpty()) {
            this.init();
            for (URL url : ProviderExportListener.EXPORTED_URL) {
                this.registry.register(url);
                Map<String, String> map = url.toMap();
                serviceInterfaceList.add(map);
            }
        }
        return serviceInterfaceList;
    }

    @RequestMapping("/offline")
    public List<Map<String, String>> offline() {
        List<Map<String, String>> serviceInterfaceList = new LinkedList<Map<String, String>>();
        if (!ProviderExportListener.EXPORTED_URL.isEmpty()) {
            this.init();
            for (URL url : ProviderExportListener.EXPORTED_URL) {
                this.registry.unregister(url);
                Map<String, String> map = url.toMap();
                serviceInterfaceList.add(map);
            }
        }
        return serviceInterfaceList;
    }

    /**
     * 初始化registry
     */
    private void init() {
        if (this.registry == null) {
            synchronized (DubboOperationEndpoint.class) {
                if (this.registry == null) {
                    ExtensionLoader<RegistryFactory> extensionLoader = ExtensionLoader.getExtensionLoader(RegistryFactory.class);
                    URL url = URL.valueOf(this.dubboRegistry.getAddress());
                    RegistryFactory registryFactory = extensionLoader.getDefaultExtension();
                    this.registry = registryFactory.getRegistry(url);
                }
            }
        }
    }

    @Override
    public String getPath() {
        return "dubbo";
    }

    @Override
    public boolean isSensitive() {
        return false;
    }

    @Override
    public Class<? extends Endpoint<?>> getEndpointType() {
        return null;
    }
}
