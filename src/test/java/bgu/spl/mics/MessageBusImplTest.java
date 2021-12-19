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
    private ExampleEvent exampleEvent;
    private ExampleBroadcast exampleBroadcast;

    @Before
    public void setUp() throws Exception {
        messageBus = MessageBusImpl.getInstance();
        exampleEvent = new ExampleEvent("Timon");
        exampleBroadcast = new ExampleBroadcast("123");
    }

}