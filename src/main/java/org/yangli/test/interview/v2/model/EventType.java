package org.yangli.test.interview.v2.model;

public enum EventType {

    REQUEST_SENT_EVENT("RequestSentEvent"),
    RESPONSE_RECEIVED_EVENT("ResponseReceivedEvent"),
    ;


    private String type;

    EventType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

}
