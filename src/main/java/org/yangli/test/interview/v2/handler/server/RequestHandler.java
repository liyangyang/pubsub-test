package org.yangli.test.interview.v2.handler.server;


import org.yangli.test.interview.v2.model.RequestMessage;
import org.yangli.test.interview.v2.model.ResponseMessage;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestHandler implements Runnable {

    private final Random random = new Random();

    private final ConcurrentLinkedQueue<RequestMessage> requestQueue;
    private final ConcurrentLinkedQueue<ResponseMessage> responseQueue;

    public RequestHandler(ConcurrentLinkedQueue<RequestMessage> requestQueue,
                          ConcurrentLinkedQueue<ResponseMessage> responseQueue) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }

    @Override
    public void run() {
        while (true) {
            RequestMessage requestMessage = requestQueue.poll();
            if (requestMessage == null) {
                try {
                    Thread.sleep(Duration.ofMillis(500));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                continue;
            }

            hanldeRequest(requestMessage);
        }
    }

    private void hanldeRequest(RequestMessage requestMessage) {
        Instant receiveTime = Instant.now();
        System.out.printf("%s - RequestHandler - Start to handle request with message ID %s\n",
                receiveTime, requestMessage.getId());

        try {
            // Need to set an origin time so that RequestSentEvent can be handled by RequestResponseMonitor
            Thread.sleep(Duration.ofSeconds(random.nextInt(3, 10)));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ResponseMessage responseMessage = buildResponseMessage(requestMessage);
        sendResponseMessage(responseMessage);

        Instant responseTime = Instant.now();
        System.out.printf("%s - RequestHandler - Complete to handle request with message ID %s. Spend %d seconds\n",
                responseTime, requestMessage.getId(), responseTime.getEpochSecond() - receiveTime.getEpochSecond());
    }

    private ResponseMessage buildResponseMessage(RequestMessage requestMessage) {
        return new ResponseMessage(requestMessage.getId(), null);
    }

    private void sendResponseMessage(ResponseMessage responseMessage) {
        responseQueue.offer(responseMessage);
    }

}
