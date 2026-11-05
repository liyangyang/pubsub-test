package org.yangli.test.interview.v2.handler.client;


import org.yangli.test.interview.v2.model.BaseEvent;
import org.yangli.test.interview.v2.model.EventType;
import org.yangli.test.interview.v2.model.ResponseMessage;
import org.yangli.test.interview.v2.model.ResponseReceivedEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ResponseReceiver implements Runnable {

    private final ConcurrentLinkedQueue<ResponseMessage> responseQueue;
    private final ConcurrentLinkedQueue<BaseEvent> eventBus;

    public ResponseReceiver(ConcurrentLinkedQueue<ResponseMessage> responseQueue,
                            ConcurrentLinkedQueue<BaseEvent> eventBus) {
        this.responseQueue = responseQueue;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        while (true) {
            ResponseMessage responseMessage = responseQueue.poll();
            if (responseMessage == null) {
                try {
                    Thread.sleep(Duration.ofMillis(500));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                continue;
            }

            publishResponseReceivedEvent(responseMessage);
        }
    }

    private void publishResponseReceivedEvent(ResponseMessage responseMessage) {
        ResponseReceivedEvent event = toResponseReceivedEvent(responseMessage);
        eventBus.offer(event);
        System.out.printf("%s - ResponseReceiver - Complete to publish ResponseReceivedEvent with message ID %s\n",
                Instant.now(), event.getMessageId());
    }

    private ResponseReceivedEvent toResponseReceivedEvent(ResponseMessage responseMessage) {
        ResponseReceivedEvent event = new ResponseReceivedEvent();
        event.setType(EventType.RESPONSE_RECEIVED_EVENT.getType());
        event.setMessageId(responseMessage.getId());
        event.setTriggerTime(Instant.now());
        return event;
    }

}
