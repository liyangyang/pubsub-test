package org.yangli.test.interview.v1;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MonitorThead implements Runnable {

    private final ConcurrentHashMap<String, Instant> messageRequestTimes;

    private final ConcurrentLinkedQueue<RequestMessage> requestMonitorQueue;
    private final ConcurrentLinkedQueue<ResponseMessage> responseMonitorQueue;

    public MonitorThead(ConcurrentHashMap<String, Instant> messageRequestTimes,
                        ConcurrentLinkedQueue<RequestMessage> requestMonitorQueue,
                        ConcurrentLinkedQueue<ResponseMessage> responseMonitorQueue) {
        this.messageRequestTimes = messageRequestTimes;
        this.requestMonitorQueue = requestMonitorQueue;
        this.responseMonitorQueue = responseMonitorQueue;
    }

    @Override
    public void run() {
        while (true) {
            while(true) {
                RequestMessage requestMessage = requestMonitorQueue.poll();
                if (requestMessage == null) {
                    break;
                }

                String requestMessageId = requestMessage.getId();
                Instant messageRequestTime = Instant.now();
                messageRequestTimes.put(requestMessageId, messageRequestTime);
                System.out.printf("Monitor Thread - Message %s with request time %s is recorded\n",
                        requestMessageId, messageRequestTime);
            }

            while(true) {
                ResponseMessage responseMessage = responseMonitorQueue.poll();
                if (responseMessage == null) {
                    break;
                }

                String messageId = responseMessage.getId();
                if (messageRequestTimes.containsKey(messageId)) {
                    // message ID exist in map means it's not expired
                    System.out.printf("Monitor Thread - Message %s response is received on time %s\n",
                            messageId, Instant.now());
                    messageRequestTimes.remove(messageId);
                }
            }

            for (Map.Entry<String, Instant> entry : messageRequestTimes.entrySet()) {
                Instant currentTime = Instant.now();
                Instant deadline = currentTime.minus(5, ChronoUnit.SECONDS);
                if (entry.getValue().isBefore(deadline)) {
                    String messageId = entry.getKey();
                    System.out.printf("Monitor Thread - Message %s didn't get response in less than 5 seconds on time: %s\n",
                            messageId, currentTime);
                    messageRequestTimes.remove(messageId);
                }
            }

            try {
                Thread.sleep(Duration.ofSeconds(1));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
