package org.yangli.test.interview.v2.model;

import java.time.Instant;

public class BaseEvent {

    private String type;
    private Instant triggerTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Instant triggerTime) {
        this.triggerTime = triggerTime;
    }
}
