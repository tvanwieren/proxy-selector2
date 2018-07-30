package com.payneteasy.proxy.selector;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class HttpProxyIntegrationTest {

    public static void main(String[] args) throws IOException {
        File file = new HttpProxySampleConfig().getFile();
        HttpProxySelectorActivator.activateWithConfig(file);
        HttpProxySelectorActivator.shutdown();

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("payneteasy.com", 80), 30_000);
        try {
            socket.getOutputStream().write((
                    "GET / HTTP/1.1\r\n"
                            + "Host: payneteasy.com\r\n"
                            + "\r\n"
            ).getBytes());
            byte[] buf   = new byte[1024];
            int    count = socket.getInputStream().read(buf);
            System.out.println(new String(buf, 0, count));
        } finally {
            socket.close();
        }
    }
}
