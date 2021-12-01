package bgu.spl.mics;
import static org.junit.Assert.*;

import bgu.spl.mics.application.messages.TrainModel;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.services.StudentService;
import org.junit.Before;
import org.junit.Test;

public class MessageBusTest {

    private MessageBusImpl messageBus;
    private StudentService studentService;
    private TrainModel trainModel;

    @Before
    public void setUp(){
        messageBus = new MessageBusImpl();
        studentService = new StudentService("Taz");
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
//        messageBus.register(studentService);
//        messageBus.subscribeBroadcast();
//        messageBus.sendEvent(trainModel);


    }

//        messageBus.subscribeEvent(trainModel.getClass(), studentService);

        //<TrainModel<Model>
//    <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m);
    @Test
    public void testMessageFlow(){

    }

}
