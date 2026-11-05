package org.yangli.test.interview.v1;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsumerThread implements Runnable {

    private final Random random = new Random();

    private final ConcurrentLinkedQueue<RequestMessage> requestQueue;
    private final ConcurrentLinkedQueue<ResponseMessage> responseQueue;
    private final ConcurrentLinkedQueue<ResponseMessage> responseMonitorQueue;

    public ConsumerThread(ConcurrentLinkedQueue<RequestMessage> requestQueue,
                          ConcurrentLinkedQueue<ResponseMessage> responseQueue,
                          ConcurrentLinkedQueue<ResponseMessage> responseMonitorQueue) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
        this.responseMonitorQueue = responseMonitorQueue;
    }

    @Override
    public void run() {
        while (true) {
            RequestMessage requestMessage = requestQueue.poll();
            if (requestMessage == null) {
                try {
                    Thread.sleep(Duration.ofSeconds(1));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }

            String messageId = requestMessage.getId();
            System.out.printf("Consumer Thread - Message %s is received on time %s\n", messageId, Instant.now());

            try {
                int timeSpend = random.nextInt(10);
                Thread.sleep(Duration.ofSeconds(timeSpend));
                System.out.printf("Consumer Thread - Message %s handing spend %d seconds\n", messageId, timeSpend);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            ResponseMessage responseMessage = new ResponseMessage(messageId, null);
            responseQueue.offer(responseMessage);
            responseMonitorQueue.offer(responseMessage);

            System.out.printf("Consumer Thread - Message %s is responded on time %s\n", messageId, Instant.now());
        }
    }
}
