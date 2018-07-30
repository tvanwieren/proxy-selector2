package com.payneteasy.proxy.selector;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpProxySampleConfig {


    private final File configFile;

    public HttpProxySampleConfig() throws IOException {
        configFile = File.createTempFile("proxy-", ".yaml");
        PrintWriter out = new PrintWriter(configFile);
        out.write(
                "proxies:"
                        + "\n- host: squid-1"
                        + "\n  port: 3128"
                        + "\n  urls:"
                        + "\n  - http://payneteasy.com"
                        + "\n  - host1"

                        + "\n- host: host2"
                        + "\n  port: 3128"
                        + "\n  urls:"
                        + "\n  - http://url"
                        + "\n  - host1"

                        + "\n- host: 10.0.0.115"
                        + "\n  port: 2345"
                        + "\n  type: SOCKS"
                        + "\n  urls:"
                        + "\n  - 10.2.2.2"
        );
        out.close();
    }

    public File getFile() {
        return configFile;
    }

    public void delete() {
        configFile.delete();
    }
}
