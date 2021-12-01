package bgu.spl.mics;
import static org.junit.Assert.*;

import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.services.StudentService;
import org.junit.Before;
import org.junit.Test;

public class MessageBusTest {

    private MessageBusImpl messageBus;
    private StudentService studentService;

    @Before
    public void setUp(){
        messageBus = new MessageBusImpl();
        studentService = new StudentService("Taz");


    }

    @Test
    public void testSusbriceEvent(){
        messageBus.

        //<TrainModel<Model>
//    <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m);

    }

    @Test
    public void testMessageFlow(){

    }

}
