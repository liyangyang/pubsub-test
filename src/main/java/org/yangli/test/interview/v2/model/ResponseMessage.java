package org.yangli.test.interview.v2.model;

import java.util.StringJoiner;

public class ResponseMessage {

    private String id;
    private String content;

    public ResponseMessage(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ResponseMessage.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("content='" + content + "'")
                .toString();
    }
}
