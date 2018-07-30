package com.payneteasy.proxy.selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class HttpProxyConfigFileWatcher implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(HttpProxyConfigFileWatcher.class);

    private final File                 configFile;
    private final Thread               thread;
    private final IFileChangedListener listener;

    public HttpProxyConfigFileWatcher(File aConfigFile, IFileChangedListener aFileChangedListener) {
        configFile = aConfigFile;
        listener  = aFileChangedListener;
        thread = new Thread(this);
        thread.setName("proxy-config-watcher-" + aConfigFile.getName());
    }

    public void startWatching() {
        thread.start();
    }

    public void stopWatching() {
        thread.interrupt();
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                watchFile();
            } catch (IOException e) {
                LOG.error("Can't watch file {}", configFile, e);
            } catch (InterruptedException e) {
                LOG.info("File {} watch interrupted", configFile);
                break;
            }
        }
    }

    void watchFile() throws IOException, InterruptedException {

        WatchService watchService = FileSystems.getDefault().newWatchService();
        configFile.getParentFile().toPath().register(watchService, ENTRY_MODIFY);

        LOG.info("Started to watch file {}", configFile);
        long lastModified = configFile.lastModified();

        while(!Thread.currentThread().isInterrupted()) {
            WatchKey key = watchService.poll(10, TimeUnit.SECONDS);
            if(key == null) {
                if(lastModified != configFile.lastModified()) {
                    lastModified = configFile.lastModified();
                    listener.fileChanged();
                }
            } else {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Object context = event.context();
                    if(context instanceof Path) {
                        Path path = (Path) context;
                        if(configFile.getName().equals(path.toFile().getName())) {
                            listener.fileChanged();
                        }
                    }
                }
                key.reset();
            }
        }
    }

    interface IFileChangedListener {
        void fileChanged();
    }
}


