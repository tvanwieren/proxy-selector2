package com.payneteasy.proxy.selector;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;

public class HttpProxyConfigReader {

    private final Yaml yaml       = new Yaml();
    private final File configFile;

    public HttpProxyConfigReader(File configFile) {
        this.configFile = configFile;
    }

    public HttpProxyConfig readConfig() {
        try(FileReader in = new FileReader(configFile)) {
            return yaml.loadAs(in, HttpProxyConfig.class);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + configFile.getAbsolutePath(), e);
        }
    }


}
