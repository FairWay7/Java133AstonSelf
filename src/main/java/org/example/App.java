package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;

import java.io.File;

public class App {
    private static final int DEFAULT_PORT = 8080;
    private static final String APP_PATH = "/";

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(DEFAULT_PORT);
        tomcat.getConnector();

        Context context = tomcat.addWebapp("",
            new File("src/main/java/org/example").getAbsolutePath()
        );

        ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);

        context.getServletContext().setAttribute(
            "org.apache.tomcat.websocket.server.Constants.BINARY_BUFFER_SIZE",
            8192
        );

        tomcat.start();
        System.out.println("Server started on http://localhost:8080");
        System.out.println("Server started on http://localhost:8080/swagger-ui/index.html");
        tomcat.getServer().await();
    }
}