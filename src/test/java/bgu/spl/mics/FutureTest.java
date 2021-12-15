package bgu.spl.mics;

import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static bgu.spl.mics.application.objects.Data.Type.Images;
import static org.junit.Assert.*;

public class FutureTest {

    private Future<Integer> future;
    private Model deepLearningModel;

    @Before
    public void setUp() throws Exception {
        future = new Future<>();
//        deepLearningModel = new Model("Deep Learning Model", new Data(Images, 0,100_000), new Student("Simba", "Computer Science", Student.Degree.MSc, 0, 0));
    }

    @Test
    public void resolve() {
        future.resolve(10);
        assertTrue("Future should be resolved", future.isDone());
    }

    @Test
    public void isDoneUnresolved() {
        assertFalse("Future should not be resolved", future.isDone());
    }

    @Test
    public void isDoneResolved() {
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
                fail("test failed because it was interrupted, try rerun without interrupting");
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