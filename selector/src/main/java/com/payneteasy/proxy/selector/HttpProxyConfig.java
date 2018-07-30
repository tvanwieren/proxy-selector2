package com.payneteasy.proxy.selector;

import java.net.Proxy;
import java.util.List;

public class HttpProxyConfig {

    public List<ProxyInfo> proxies;

    static class ProxyInfo {
        public String     host;
        public int        port;
        public Proxy.Type type;

        public List<String> urls;

        public ProxyInfo(String host, int port, List<String> aUrls) {
            this.host = host;
            this.port = port;
            urls = aUrls;
        }

        public ProxyInfo() {
        }

        @Override
        public String toString() {
            return "ProxyInfo{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    ", type=" + type +
                    ", urls=" + urls +
                    '}';
        }

        public Proxy.Type getProxyType() {
            return type != null ? type : Proxy.Type.HTTP;
        }
    }

    @Override
    public String toString() {
        return "HttpProxyConfig{" +
                "proxies=" + proxies +
                '}';
    }


}
