package com.payneteasy.proxy.selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.singletonList;

public class HttpProxies {

    private static final Logger LOG = LoggerFactory.getLogger(HttpProxies.class);

    private static final List<Proxy> DIRECT = singletonList(Proxy.NO_PROXY);

    private volatile Map<String, List<Proxy>> proxies;

    public HttpProxies(HttpProxyConfig aConfig) {
        updateConfig(aConfig);
    }

    public List<Proxy> getProxy(URI aUri) {
        List<Proxy> proxy = proxies.get(aUri.getHost());
        return proxy != null ? proxy : DIRECT;
    }

    public void updateConfig(HttpProxyConfig aConfig) {
        Map<String, List<Proxy>> map = new TreeMap<>();
        for (HttpProxyConfig.ProxyInfo info : aConfig.proxies) {
            List<Proxy> javaProxies = singletonList(new Proxy(info.getProxyType(), new InetSocketAddress(info.host, info.port)));
            for (String url : info.urls) {
                if(url.startsWith("http")) {
                    try {
                        map.put(new URL(url).getHost(), javaProxies);
                    } catch (MalformedURLException e) {
                        throw new IllegalStateException("Cannot parse url " + url, e);
                    }
                } else {
                    map.put(url, javaProxies);
                }
            }
        }

        LOG.info("Proxy configuration:");
        for (Map.Entry<String, List<Proxy>> entry : map.entrySet()) {
            LOG.info("    {} -> {}", entry.getKey(), entry.getValue());
        }
        proxies = map;
    }

}
