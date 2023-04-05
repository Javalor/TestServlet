package io.restfulness.server.http;

public abstract class HttpServerProtocol {

    public static final String HTTP11_APR = org.apache.coyote.http11.Http11AprProtocol.class.getName();
    public static final String HTTP11_NIO = org.apache.coyote.http11.Http11NioProtocol.class.getName();
    public static final String HTTP11_NIO2 = org.apache.coyote.http11.Http11Nio2Protocol.class.getName();

};
