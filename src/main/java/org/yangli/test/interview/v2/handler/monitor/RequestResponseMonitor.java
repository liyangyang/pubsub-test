package org.yangli.test.interview.v2.handler.monitor;


import org.yangli.test.interview.v2.model.BaseEvent;
import org.yangli.test.interview.v2.model.EventType;
import org.yangli.test.interview.v2.model.RequestSentEvent;
import org.yangli.test.interview.v2.model.ResponseReceivedEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestResponseMonitor implements Runnable {

    private final ConcurrentLinkedQueue<BaseEvent> eventBus;
    private final ConcurrentHashMap<String, Instant> messageRequestTimes;

    public RequestResponseMonitor(ConcurrentLinkedQueue<BaseEvent> eventBus,
                                  ConcurrentHashMap<String, Instant> messageRequestTimes) {
        this.eventBus = eventBus;
        this.messageRequestTimes = messageRequestTimes;
    }

    @Override
    public void run() {
        while (true) {
            BaseEvent baseEvent = eventBus.poll();
            if (baseEvent == null) {
                try {
                    Thread.sleep(Duration.ofMillis(500));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                continue;
            }

            handleEvent(baseEvent);
        }
    }

    private void handleEvent(BaseEvent baseEvent) {
        String type = baseEvent.getType();
        if (EventType.REQUEST_SENT_EVENT.getType().equals(type)) {
            handleRequestSentEvent((RequestSentEvent) baseEvent);
        } else if (EventType.RESPONSE_RECEIVED_EVENT.getType().equals(type)) {
            handleResponseReceivedEvent((ResponseReceivedEvent) baseEvent);
        }
    }

    private void handleRequestSentEvent(RequestSentEvent event) {
        messageRequestTimes.put(event.getMessageId(), event.getTriggerTime());
        System.out.printf("%s - RequestResponseMonitor - Complete to handle RequestSentEvent with message ID %s\n",
                Instant.now(), event.getMessageId());
    }

    private void handleResponseReceivedEvent(ResponseReceivedEvent event) {
        String messageId = event.getMessageId();
        if (messageRequestTimes.containsKey(messageId)) {
            System.out.printf("%s - RequestResponseMonitor - Request message with ID %s get response in less than 5 seconds\n",
                    Instant.now(), event.getMessageId());
            // Get response, remove from map for untracking
            messageRequestTimes.remove(messageId);
        }

        System.out.printf("%s - RequestResponseMonitor - Complete to handle ResponseReceivedEvent with message ID %s\n",
                Instant.now(), event.getMessageId());
    }

}
