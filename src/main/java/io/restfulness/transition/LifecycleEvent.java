package io.restfulness.transition;

import java.util.EventObject;

public class LifecycleEvent extends EventObject {

    private final Object data;
    private final String type;

    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
        super(lifecycle);
        this.type = type;
        this.data = data;
    }

    public Lifecycle getLifecycle() {
        return (Lifecycle) getSource();
    }

    public String getType() {
        return (this.type);
    }

    public Object getData() {
        return (this.data);
    }
}
