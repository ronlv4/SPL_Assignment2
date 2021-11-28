package bgu.spl.mics;

import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.application.services.TimeService;
import org.junit.Before;
import org.junit.Test;

import java.sql.Time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FutureTest<T> {

    private Future<Model> future;
    private Model deepLearningModel;

    @Before
    public void setUp(){
        future = new Future<>();
        deepLearningModel = new Model("Deep Learning Model", new Data(), new Student());
        future.resolve(deepLearningModel);
    }

    @Test
    public void testResolve(){
        assertTrue("Future should be resolved", future.isDone());
    }

    @Test
    public void testIsDone(){
        assertTrue("Future should be resolved", future.isDone());
    }

    @Test
    public void testGet(){
        assertEquals(deepLearningModel, future.get()); // TODO check if assertEqual is deep equal.
    }

    @Test
    public void testGetTimeOut(){

    }
}
