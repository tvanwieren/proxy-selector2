package com.payneteasy.proxy.selector;

import com.payneteasy.proxy.selector.HttpProxyConfigFileWatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpProxyConfigFileWatcherTest {

    private File                       configFile;
    private HttpProxyConfigFileWatcher watcher;

    @Before
    public void setup() throws IOException {
        configFile = File.createTempFile("proxy-", ".yaml");
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
        );
        out.close();
        watcher = new HttpProxyConfigFileWatcher(configFile, () -> System.out.println("File changed"));
    }

    @After
    public void teardown() {
        configFile.delete();
    }

    @Test
    @Ignore
    public void watch() throws IOException, InterruptedException {
        System.out.println("configFile = " + configFile.getAbsolutePath());
        watcher.watchFile();
    }

}