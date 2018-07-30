package com.payneteasy.proxy.selector;

import com.payneteasy.proxy.selector.HttpProxyConfigFileWatcher.IFileChangedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.ProxySelector;

public class HttpProxySelectorActivator {

    private static final Logger LOG = LoggerFactory.getLogger(HttpProxySelectorActivator.class);

    private static String CONFIG_FILENAME = getConfigFile();

    private static String getConfigFile() {
        String filename = System.getProperty("http.proxySelectorConfigFile");
        if(filename == null) {
            filename = System.getenv("HTTP_PROXY_SELECTOR_CONFIG_FILE");
        }
        return filename;
    }

    private static volatile HttpProxyConfigFileWatcher WATCHER;

    public static void activate() {
        if(CONFIG_FILENAME == null) {
            LOG.info("http.proxySelectorConfigFile or HTTP_PROXY_SELECTOR_CONFIG_FILE is null, skipping ProxySelector initialisation");
            return;
        }

        File configFile = new File(CONFIG_FILENAME);
        activateWithConfig(configFile);
    }

    static void activateWithConfig(File configFile) {
        if(!configFile.exists()) {
            throw new IllegalStateException("No configFile " + configFile.getAbsolutePath() + " found. Check property http.proxySelectorConfigFile");
        }

        LOG.info("Activating ProxySelector for config {} ...", configFile.getAbsolutePath());
        HttpProxyConfigReader reader  = new HttpProxyConfigReader(configFile);
        HttpProxies           proxies = new HttpProxies(reader.readConfig());
        IFileChangedListener listener = () -> {
            try {
                LOG.info("Updating proxy config ...");
                proxies.updateConfig(reader.readConfig());
            } catch (Exception e) {
                LOG.error("Cannot update proxy config", e);
            }
        };
        WATCHER = new HttpProxyConfigFileWatcher(configFile, listener);
        HttpProxySelector selector = new HttpProxySelector(proxies);
        ProxySelector aDefault = ProxySelector.getDefault();
        System.out.println("aDefault = " + aDefault);
        ProxySelector.setDefault(selector);

        WATCHER.startWatching();
    }

    public  static boolean isActivated() {
        return CONFIG_FILENAME != null;
    }

    public static void shutdown() {
        WATCHER.stopWatching();
    }
}
