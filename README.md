# V2 Implementation
### Main.java
```java
package org.yangli.test.interview.v2;

import org.yangli.test.interview.v2.handler.client.RequestSender;
import org.yangli.test.interview.v2.handler.client.ResponseReceiver;
import org.yangli.test.interview.v2.handler.monitor.RequestResponseMonitor;
import org.yangli.test.interview.v2.handler.monitor.ResponseOutTimeMonitor;
import org.yangli.test.interview.v2.handler.server.RequestHandler;
import org.yangli.test.interview.v2.model.BaseEvent;
import org.yangli.test.interview.v2.model.RequestMessage;
import org.yangli.test.interview.v2.model.ResponseMessage;

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

        ConcurrentLinkedQueue<BaseEvent> eventBus = new ConcurrentLinkedQueue<>();

        RequestSender requestSender = new RequestSender(requestQueue, eventBus);
        ResponseReceiver responseReceiver = new ResponseReceiver(responseQueue, eventBus);
        RequestResponseMonitor requestResponseMonitor = new RequestResponseMonitor(eventBus, messageRequestTimes);
        ResponseOutTimeMonitor responseOutTimeMonitor = new ResponseOutTimeMonitor(messageRequestTimes);

        ThreadPoolExecutor requestSenderExecutor = null;
        ThreadPoolExecutor responseReceiverExecutor = null;
        ThreadPoolExecutor requestHandlerExecutor = null;
        ThreadPoolExecutor requestResponseMonitorExecutor = null;
        ThreadPoolExecutor responseOutTimeMonitorExecutor = null;
        try {
            requestSenderExecutor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(5));
            requestSenderExecutor.submit(requestSender);

            responseReceiverExecutor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(5));
            responseReceiverExecutor.submit(responseReceiver);

            requestHandlerExecutor = new ThreadPoolExecutor(5, 10,
                    0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(10));
            requestHandlerExecutor.submit(new RequestHandler(requestQueue, responseQueue));
            requestHandlerExecutor.submit(new RequestHandler(requestQueue, responseQueue));
            requestHandlerExecutor.submit(new RequestHandler(requestQueue, responseQueue));
            requestHandlerExecutor.submit(new RequestHandler(requestQueue, responseQueue));
            requestHandlerExecutor.submit(new RequestHandler(requestQueue, responseQueue));
            requestHandlerExecutor.submit(new RequestHandler(requestQueue, responseQueue));
            requestHandlerExecutor.submit(new RequestHandler(requestQueue, responseQueue));
            requestHandlerExecutor.submit(new RequestHandler(requestQueue, responseQueue));
            requestHandlerExecutor.submit(new RequestHandler(requestQueue, responseQueue));
            requestHandlerExecutor.submit(new RequestHandler(requestQueue, responseQueue));

            requestResponseMonitorExecutor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(5));
            requestResponseMonitorExecutor.submit(requestResponseMonitor);

            responseOutTimeMonitorExecutor = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(5));
            responseOutTimeMonitorExecutor.submit(responseOutTimeMonitor);
        } catch (Exception exception) {
            System.out.printf("Main - Thread failed with error %s", exception.getMessage());
        } finally {
            if (requestSenderExecutor != null) {
                requestSenderExecutor.close();
            }

            if (responseReceiverExecutor != null) {
                responseReceiverExecutor.close();
            }

            if (requestHandlerExecutor != null) {
                requestHandlerExecutor.close();
            }

            if (requestResponseMonitorExecutor != null) {
                requestResponseMonitorExecutor.close();
            }

            if (responseOutTimeMonitorExecutor != null) {
                responseOutTimeMonitorExecutor.close();
            }
        }

    }
}
```
### RequestMessage.java
```java
package org.yangli.test.interview.v2.model;

import java.util.StringJoiner;

public class RequestMessage {

    private String id;
    private String content;

    public RequestMessage(String id, String content) {
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
        return new StringJoiner(", ", RequestMessage.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("content='" + content + "'")
                .toString();
    }
}
```
### ResponseMessage.java
```java
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

```
### BaseEvent.java
```java
package org.yangli.test.interview.v2.model;

import java.time.Instant;

public class BaseEvent {

    private String type;
    private Instant triggerTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Instant triggerTime) {
        this.triggerTime = triggerTime;
    }
}
```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
### .java
```java

```
