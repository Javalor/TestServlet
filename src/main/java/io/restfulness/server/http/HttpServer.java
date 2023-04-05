package io.restfulness.server.http;

public interface HttpServer {

    int DEFAULT_PORT = 80;

    void start();
    void stop();
    void await();

    int getPort();
}
