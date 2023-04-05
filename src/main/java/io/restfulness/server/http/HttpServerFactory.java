package io.restfulness.server.http;

import jakarta.servlet.http.HttpServlet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class HttpServerFactory {

    public HttpServer getServer(String servlet,String protocol, int port)  {
        HttpServlet httpServlet = null;
        try {
            Class<?> httpServletClass = Class.forName(servlet);
            Constructor<?> ctor = httpServletClass.getConstructor();
            httpServlet = (HttpServlet) ctor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Unsupported servlet '" + servlet + "'");
        }



        if (protocol.equals(HttpServerProtocol.HTTP11_NIO2)) {
                return Http11Nio2Server.getInstance(httpServlet, port);
        }
        else {
            throw new IllegalArgumentException("Unsupported protocol '" + protocol + "'");
        }


    }
}
