package com.payneteasy.proxy.selector.app;

import com.payneteasy.proxy.selector.HttpProxies;
import com.payneteasy.proxy.selector.HttpProxyConfig;
import com.payneteasy.proxy.selector.HttpProxyConfigReader;

import java.io.File;
import java.net.Proxy;
import java.net.URI;
import java.util.List;

public class HttpProxyConfigCheckApplication {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage java -jar proxy-config-check.jar filename.yml");
            System.exit(1);
        }

        String          filename = args[0];
        File            file     = new File(filename);
        try {
            HttpProxyConfig config   = new HttpProxyConfigReader(file).readConfig();
            HttpProxies     proxies  = new HttpProxies(config);

            List<Proxy> proxy = proxies.getProxy(new URI("http://localhost"));
            System.out.println("Proxy for localhost is " + proxy);
            System.out.println("Syntax is OK");
        } catch (Exception e) {
            System.err.println("Cannot check file " + file.getAbsolutePath());
            e.printStackTrace();
            System.exit(2);
        }

    }

}
