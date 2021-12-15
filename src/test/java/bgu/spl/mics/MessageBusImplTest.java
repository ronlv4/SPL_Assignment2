package bgu.spl.mics;

import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageBusImplTest {

    private MessageBusImpl messageBus;
    private StudentService studentService;
    private TrainModelEvent trainModelEvent;
    private ExampleEvent exampleEvent;
    private ExampleBroadcast exampleBroadcast;

    @Before
    public void setUp() throws Exception {
        messageBus = MessageBusImpl.getInstance();
//        studentService = new StudentService("Taz");
        exampleEvent = new ExampleEvent("Timon");
        exampleBroadcast = new ExampleBroadcast("123");
        trainModelEvent = new TrainModelEvent();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void subscribeEvent() {
    }

    @Test
    public void subscribeBroadcast() {
    }

    @Test
    public void complete() {
        Future<String> future = messageBus.sendEvent(exampleEvent);
        messageBus.complete(exampleEvent, "test");
        assertEquals("test", future.get());
    }

    @Test
    public void sendBroadcast() {
        messageBus.register(studentService);
        messageBus.subscribeBroadcast(exampleBroadcast.getClass(),studentService);
        messageBus.sendBroadcast(exampleBroadcast);
        try{
            Message message = messageBus.awaitMessage(studentService);
            assertEquals("Broadcast expected", exampleBroadcast, message);
        }
        catch (Exception e){
            fail("test failed because it was interrupted, try rerun without interrupting");
        }
    }

    @Test
    public void testSendEvent(){
        messageBus.register(studentService);
        messageBus.subscribeEvent(trainModelEvent.getClass(), studentService);
        messageBus.sendEvent(trainModelEvent);
        try{
            assertNotNull("A message should be accepted", messageBus.awaitMessage(studentService));
        }
        catch (InterruptedException e){
            System.out.println("Interrupted");

        }
    }

    @Test
    public void testSendEventNoSubscribers() {
        messageBus.register(studentService);
        assertNull("Future should be null because no subscribers available for this type of event", messageBus.sendEvent(trainModelEvent));
    }

    @Test
    public void testSendEventWithSubscribers() {
        messageBus.register(studentService);
        messageBus.subscribeEvent(trainModelEvent.getClass(), studentService);
        assertNotNull("Future should not be null because there is a subscriber for this type of event", messageBus.sendEvent(trainModelEvent));
    }

    @Test
    public void unregister() {
        messageBus.register(studentService);
        messageBus.sendEvent(exampleEvent);
        Message message = null;
        try {
            message = messageBus.awaitMessage(studentService);
        }
        catch (Exception e){
            fail("test failed because it was interrupted, try rerun without interrupting");
        }
        if (message == null){
            fail("Can not check unregister because was unable to verify registration before unregister action");
        }
        message = null;
        messageBus.unregister(studentService);
        messageBus.sendEvent(exampleEvent);
        try {
            message = messageBus.awaitMessage(studentService);
        }
        catch (Exception e){
            fail("test failed because it was interrupted, try rerun without interrupting");
        }
        assertNull("unregister was not successful because message was received after sending an event", message);
    }

    @Test
    public void awaitMessageBroadcastType() {
        messageBus.register(studentService);
        messageBus.subscribeBroadcast(exampleBroadcast.getClass(), studentService);
        messageBus.sendBroadcast(exampleBroadcast);
        Message message = null;
        try{
            message = messageBus.awaitMessage(studentService);
        }
        catch (Exception e){
            fail("test failed because it was interrupted, try rerun without interrupting");
        }
        assertEquals("Did not get the expected broadcast", exampleBroadcast, message);
    }

    @Test
    public void awaitMessageEventType() {
        messageBus.register(studentService);
        messageBus.subscribeEvent(exampleEvent.getClass(), studentService);
        messageBus.sendEvent(exampleEvent);
        Message message = null;
        try{
            message = messageBus.awaitMessage(studentService);
        }
        catch (Exception e){
            fail("test failed because it was interrupted, try rerun without interrupting");
        }
        assertEquals("Did not get the expected event", exampleEvent, message);
    }
}