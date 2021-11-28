package bgu.spl.mics;
import static org.junit.Assert.*;

import bgu.spl.mics.application.objects.CPU;
import org.junit.Before;
import org.junit.Test;

public class MessageBusTest {

    private MessageBusImpl messageBus;

    @Before
    public void setUp(){
        messageBus = new MessageBusImpl();
    }

    @Test
    public void testMultiply(){
        int result = 25;
        assertEquals("result should be equal to 20", result, messageBus.multiply(5, 4));
    }
}
