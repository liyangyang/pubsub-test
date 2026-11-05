package org.yangli.test.interview.v2.handler.client;


import org.yangli.test.interview.v2.model.BaseEvent;
import org.yangli.test.interview.v2.model.EventType;
import org.yangli.test.interview.v2.model.RequestMessage;
import org.yangli.test.interview.v2.model.RequestSentEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestSender implements Runnable {

    private final ConcurrentLinkedQueue<RequestMessage> requestQueue;
    private final ConcurrentLinkedQueue<BaseEvent> eventBus;

    public RequestSender(ConcurrentLinkedQueue<RequestMessage> requestQueue,
                         ConcurrentLinkedQueue<BaseEvent> eventBus) {
        this.requestQueue = requestQueue;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(Duration.ofSeconds(2));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            RequestMessage requestMessage = buildRequestMessage();
            sendRequestMessage(requestMessage);
            publishRequestSentEvent(requestMessage);
        }
    }

    private RequestMessage buildRequestMessage() {
        return new RequestMessage(UUID.randomUUID().toString(), null);
    }

    private void sendRequestMessage(RequestMessage requestMessage) {
        requestQueue.offer(requestMessage);
        System.out.printf("%s - RequestSender - Complete to send request message with ID %s\n",
                Instant.now(), requestMessage.getId());
    }

    private void publishRequestSentEvent(RequestMessage requestMessage) {
        RequestSentEvent event = toRequestSentEvent(requestMessage);
        eventBus.offer(event);
        System.out.printf("%s - RequestSender - Complete to publish RequestSentEvent with message ID %s\n",
                Instant.now(), requestMessage.getId());
    }

    private RequestSentEvent toRequestSentEvent(RequestMessage requestMessage) {
        RequestSentEvent event = new RequestSentEvent();
        event.setType(EventType.REQUEST_SENT_EVENT.getType());
        event.setMessageId(requestMessage.getId());
        event.setTriggerTime(Instant.now());
        return event;
    }

}
