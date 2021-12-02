package bgu.spl.mics;

import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.application.services.TimeService;
import org.junit.Before;
import org.junit.Test;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FutureTest<T> {

    private Future<Integer> future;
    private Model deepLearningModel;

    @Before
    public void setUp() {
        future = new Future<>();
        deepLearningModel = new Model("Deep Learning Model", new Data(), new Student());
    }

    @Test
    public void testResolve() {
        future.resolve(10);
        assertTrue("Future should be resolved", future.isDone());
    }

    @Test
    public void testIsDoneUnResolved() {
        assertFalse("Future should not be resolved", future.isDone());
    }

    @Test
    public void testIsDoneResolved(){
        future.resolve(10);
        assertTrue("Future should be resolved", future.isDone());
    }

    @Test
    public void testGetResolved() {
        future.resolve(10);
        assertEquals(Integer.valueOf(10), future.get());
    }

    @Test
    public void testGetBlocking() {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                future.resolve(1);
            } catch (Exception e) {
                fail("testGetBlocking was interrupted so the test has failed, try rerun without interrupting");
            }
        });
        t1.start();
        long time = System.nanoTime();
        future.get();
        time = time - System.nanoTime();
        assertTrue("future was resolved after " + time / 1_000_000 + " ms but expected to take at least 1000 ms", time > 900 * 1_000_000);
    }

    @Test
    public void testGetAfterTimeOut() {
        long time = System.nanoTime();
        future.get(1000, TimeUnit.MILLISECONDS);
        time = time - System.nanoTime();
        assertTrue("Future.get ended after " + time / 1_000_000 + "ms but expected to wait at least 1000ms", time > 900 * 1_000_000);
    }
}
