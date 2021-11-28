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

}
