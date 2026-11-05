package org.yangli.test.interview.v2.model;

public class RequestSentEvent extends BaseEvent {

    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
