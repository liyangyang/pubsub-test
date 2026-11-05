package org.yangli.test.interview.v1;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProducerThread implements Runnable {

    private final ConcurrentLinkedQueue<RequestMessage> requestQueue;
    private final ConcurrentLinkedQueue<RequestMessage> requestMonitorQueue;

    public ProducerThread(ConcurrentLinkedQueue<RequestMessage> requestQueue,
                          ConcurrentLinkedQueue<RequestMessage> requestMonitorQueue) {
        this.requestQueue = requestQueue;
        this.requestMonitorQueue = requestMonitorQueue;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(Duration.ofSeconds(1));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                Thread.sleep(Duration.ofSeconds(3));

                String messageId = UUID.randomUUID().toString();
                RequestMessage message = new RequestMessage(messageId, null);

                requestQueue.offer(message);
                requestMonitorQueue.offer(message);

                System.out.printf("Producer Thread - Message %s is requested on time %s\n", messageId, Instant.now());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
