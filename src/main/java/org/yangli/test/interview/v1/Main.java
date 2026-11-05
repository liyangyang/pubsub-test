package org.yangli.test.interview.v1;

import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    static void main(String[] args) {
        ConcurrentHashMap<String, Instant> messageRequestTimes = new ConcurrentHashMap<>();

        ConcurrentLinkedQueue<RequestMessage> requestQueue = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<ResponseMessage> responseQueue = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<RequestMessage> requestMonitorQueue = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<ResponseMessage> responseMonitorQueue = new ConcurrentLinkedQueue<>();

        ProducerThread producerThread = new ProducerThread(requestQueue, requestMonitorQueue);
        MonitorThead monitorThead = new MonitorThead(messageRequestTimes, requestMonitorQueue, responseMonitorQueue);

        ThreadPoolExecutor producerExecutor = null;
        ThreadPoolExecutor consumerExecutor = null;
        ThreadPoolExecutor monitorExecutor = null;
        try {
            monitorExecutor = new ThreadPoolExecutor(1, 3,
                    0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(10));
            monitorExecutor.submit(monitorThead);

            consumerExecutor = new ThreadPoolExecutor(5, 10,
                    0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(5));
            consumerExecutor.submit(new ConsumerThread(requestQueue, responseQueue, responseMonitorQueue));
            consumerExecutor.submit(new ConsumerThread(requestQueue, responseQueue, responseMonitorQueue));
            consumerExecutor.submit(new ConsumerThread(requestQueue, responseQueue, responseMonitorQueue));
            consumerExecutor.submit(new ConsumerThread(requestQueue, responseQueue, responseMonitorQueue));
            consumerExecutor.submit(new ConsumerThread(requestQueue, responseQueue, responseMonitorQueue));
            consumerExecutor.submit(new ConsumerThread(requestQueue, responseQueue, responseMonitorQueue));
            consumerExecutor.submit(new ConsumerThread(requestQueue, responseQueue, responseMonitorQueue));
            consumerExecutor.submit(new ConsumerThread(requestQueue, responseQueue, responseMonitorQueue));
            consumerExecutor.submit(new ConsumerThread(requestQueue, responseQueue, responseMonitorQueue));
            consumerExecutor.submit(new ConsumerThread(requestQueue, responseQueue, responseMonitorQueue));

            producerExecutor = new ThreadPoolExecutor(1, 3,
                    0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(10));
            producerExecutor.submit(producerThread);
        } catch (Exception exception) {
            System.out.printf("Main - Thread failed with error %s", exception.getMessage());
        } finally {
            if (producerExecutor != null) {
                producerExecutor.close();
            }

            if (consumerExecutor != null) {
                consumerExecutor.close();
            }

            if (monitorExecutor != null) {
                monitorExecutor.close();
            }
        }

    }
}
