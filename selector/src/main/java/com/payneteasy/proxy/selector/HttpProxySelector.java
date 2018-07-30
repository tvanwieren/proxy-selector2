package com.payneteasy.proxy.selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public class HttpProxySelector extends ProxySelector {

    private static final Logger LOG = LoggerFactory.getLogger(HttpProxySelector.class);

    private final HttpProxies proxies;

    public HttpProxySelector(HttpProxies proxies) {
        this.proxies = proxies;
    }

    @Override
    public List<Proxy> select(URI uri) {
        List<Proxy> proxy = proxies.getProxy(uri);
        if(LOG.isDebugEnabled()) {
            LOG.debug("Proxy for {} is {}", uri, proxy);
        }
        return proxy;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        LOG.error("Cannot process {} via {}", uri, sa, ioe);
    }

    public static Optional<Proxy> getFirstProxy(String aUrl) {
        List<Proxy> proxies = ProxySelector.getDefault().select(URI.create(aUrl));
        if (proxies != null && proxies.size() > 0) {
            return Optional.ofNullable(proxies.get(0));
        } else {
            return Optional.empty();
        }
    }
}
