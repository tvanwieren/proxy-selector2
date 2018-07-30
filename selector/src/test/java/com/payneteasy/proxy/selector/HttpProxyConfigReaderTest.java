package com.payneteasy.proxy.selector;

import com.payneteasy.proxy.selector.HttpProxyConfig;
import com.payneteasy.proxy.selector.HttpProxyConfigReader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

public class HttpProxyConfigReaderTest {

    private File configFile;

    @Before
    public void setup() throws IOException {
        configFile = File.createTempFile("proxy", ".yaml");
        PrintWriter out = new PrintWriter(configFile);
        out.write(
                "proxies:"
                        + "\n- host: host"
                        + "\n  port: 1234"
                        + "\n  urls:"
                        + "\n  - http://url"
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
                        + "\n  - gate.payneteasy.com"
        );
        out.close();
    }

    @After
    public void teardown() {
        configFile.delete();
    }

    @Test
    public void readConfig() throws IOException {
        HttpProxyConfig config = new HttpProxyConfigReader(configFile).readConfig();
        Assert.assertNotNull(config);
        System.out.println("config = " + config);
        Assert.assertEquals(3, config.proxies.size());
        Assert.assertEquals(Proxy.Type.SOCKS, config.proxies.get(2).type);
    }

    @Test
    public void saveConfig() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setIndent(4);
        dumperOptions.setPrettyFlow(true);
        Yaml yaml = new Yaml(dumperOptions);

        HttpProxyConfig config = new HttpProxyConfig();
        config.proxies = new ArrayList<>();
        config.proxies.add(new HttpProxyConfig.ProxyInfo("host", 123, Arrays.asList("http://url", "host1")));
        config.proxies.add(new HttpProxyConfig.ProxyInfo("host", 123, Arrays.asList("http://url", "host1")));
        config.proxies.add(new HttpProxyConfig.ProxyInfo("host", 123, Arrays.asList("http://url", "host1")));
        String dump = yaml.dump(config);
        System.out.println("dump = " + dump);

    }

}