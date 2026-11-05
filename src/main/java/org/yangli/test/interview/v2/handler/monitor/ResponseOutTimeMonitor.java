package org.yangli.test.interview.v2.handler.monitor;


import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseOutTimeMonitor implements Runnable {

    private final ConcurrentHashMap<String, Instant> messageRequestTimes;

    public ResponseOutTimeMonitor(ConcurrentHashMap<String, Instant> messageRequestTimes) {
        this.messageRequestTimes = messageRequestTimes;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(Duration.ofMillis(500));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            for (Map.Entry<String, Instant> entry : messageRequestTimes.entrySet()) {
                Instant currentTime = Instant.now();
                Instant deadline = currentTime.minus(5, ChronoUnit.SECONDS);
                if (entry.getValue().isBefore(deadline)) {
                    String messageId = entry.getKey();
                    if (messageRequestTimes.containsKey(messageId)) {
                        System.out.printf("%s - ResponseOutTimeMonitor - Request message with ID %s didn't get response in less than 5 seconds\n",
                                Instant.now(), entry.getKey());
                    }
                    messageRequestTimes.remove(messageId);
                }
            }
        }
    }

}
