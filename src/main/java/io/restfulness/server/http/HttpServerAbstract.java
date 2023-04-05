package io.restfulness.server.http;

import jakarta.servlet.http.HttpServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;

public abstract class HttpServerAbstract {

    private final Tomcat tomcat;
    private final Connector connector;
    private final String protocol;
    private final HttpServlet httpServlet;
    private int port;
    private final File baseDir;

    protected HttpServerAbstract(final HttpServlet httpServlet, final String protocol, final int port) {
        connector = new Connector(this.protocol = protocol);
        connector.setPort(port);

        this.baseDir = createTempDir("io.restfulness.server.http");

        String docBase = baseDir.getAbsolutePath();
        String contextPath = "";
        String servletName = "HttpServlet";
        String mappingPattern = "/";

        this.tomcat = new Tomcat();
        Context context = tomcat.addContext(contextPath, docBase);

        this.tomcat.setBaseDir(docBase);
        this.tomcat.setPort(port);
        this.tomcat.getService().addConnector(connector);
        this.tomcat.setConnector(connector);


        tomcat.addServlet(contextPath, servletName, this.httpServlet = httpServlet);
        context.addServletMappingDecoded(mappingPattern, servletName);
    }

    private File createTempDir(String prefix) {
        try {
            File tempFolder = File.createTempFile(prefix + ".", "." + getPort()+".tmpdir");
            tempFolder.delete();
            tempFolder.mkdir();
            tempFolder.deleteOnExit();
            System.out.println("Temp file "+tempFolder.getAbsolutePath());
            return tempFolder;
        }
        catch (IOException ex) {
            throw new IllegalStateException("Unable to create temp directory", ex);
        }
    }

    public void start() {
        try {
            tomcat.start();
            this.port = tomcat.getConnector().getLocalPort();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    public void await() {
        tomcat.getServer().await();
    }

    protected Tomcat getTomcat() {
        return tomcat;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getPort() {
        return port;
    }

    protected Connector getConnector() {
        return connector;
    }

    protected HttpServlet getHttpServlet() {
        return httpServlet;
    }

    protected File getBaseDir() {
        return baseDir;
    }
}
