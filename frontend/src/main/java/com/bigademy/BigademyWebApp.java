package com.bigademy;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class BigademyWebApp {
    public static void main(String[] args) throws Exception {
        // The simple Jetty config here will serve static content from the webapp directory
        String webappDirLocation = System.getProperty("WEB_APP_DIR");
        webappDirLocation = "src/main/webapp/";


        // The port that we should run on can be set into an environment variable
        // Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getProperty("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "9900";
        }
        Server server = new Server(Integer.valueOf(webPort));

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
        webapp.setResourceBase(webappDirLocation);

        server.setHandler(webapp);
        server.start();
        server.join();
    }
}