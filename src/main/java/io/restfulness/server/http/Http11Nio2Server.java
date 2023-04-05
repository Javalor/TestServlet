package io.restfulness.server.http;

import jakarta.servlet.http.HttpServlet;

public final class Http11Nio2Server extends HttpServerAbstract implements HttpServer {

    private static final String protocol = HttpServerProtocol.HTTP11_NIO2;

    private Http11Nio2Server(HttpServlet httpServlet, int port) {
        super(httpServlet, protocol, port);
    }

    public static HttpServer getInstance(HttpServlet httpServlet, int port) {
        return new Http11Nio2Server(httpServlet, port);
    }

}
