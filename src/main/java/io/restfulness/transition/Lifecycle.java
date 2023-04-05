package io.restfulness.transition;

public interface Lifecycle {

    String BEFORE_INIT_EVENT = "before_init";
    String AFTER_INIT_EVENT = "after_init";
    String BEFORE_START_EVENT = "before_start";
    String START_EVENT = "start";
    String AFTER_START_EVENT = "after_start";
    String BEFORE_STOP_EVENT = "before_stop";
    String STOP_EVENT = "stop";
    String AFTER_STOP_EVENT = "after_stop";
    String BEFORE_DESTROY_EVENT = "before_destroy";
    String AFTER_DESTROY_EVENT = "after_destroy";

    void init();
    void start();
    void stop();
    void destroy();
}
