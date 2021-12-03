package bgu.spl.mics;
import static org.junit.Assert.*;

import bgu.spl.mics.application.messages.TrainModel;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.Before;
import org.junit.Test;

public class MessageBusTest {

    private MessageBusImpl messageBus;
    private StudentService studentService;
    private TrainModel trainModel;
    private ExampleEvent exampleEvent;
    private ExampleBroadcast exampleBroadcast;

    @Before
    public void setUp(){
        messageBus = new MessageBusImpl();
        studentService = new StudentService("Taz");
        exampleEvent = new ExampleEvent("Timon");
        exampleBroadcast = new ExampleBroadcast("123");
        trainModel = new TrainModel();


    }

    @Test
    public void testSendEventNoSubscribers() {
        messageBus.register(studentService);
        assertNull("Future should be null because no subscribers available for this type of event", messageBus.sendEvent(trainModel));
    }

    @Test
    public void testSendEventWithSubscribers() {
        messageBus.register(studentService);
        messageBus.subscribeEvent(trainModel.getClass(), studentService);
        assertNotNull("Future should not be null because there is a subscriber for this type of event", messageBus.sendEvent(trainModel));
    }

    @Test
    public void testSendEvent(){
        messageBus.register(studentService);
        messageBus.subscribeEvent(trainModel.getClass(), studentService);
        messageBus.sendEvent(trainModel);
        try{
            assertNotNull("A message should be accepted", messageBus.awaitMessage(studentService));
        }
        catch (InterruptedException e){
            System.out.println("Interrupted");

        }
    }


    @Test
    public void testSendBroadcast(){
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
    public void testComplete(){
        Future<String> future = messageBus.sendEvent(exampleEvent);
        messageBus.complete(exampleEvent, "test");
        assertEquals("test", future.get());
    }

    @Test
    public void testUnregister(){
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
}
