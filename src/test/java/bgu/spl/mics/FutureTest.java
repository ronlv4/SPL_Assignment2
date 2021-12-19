package bgu.spl.mics;

import bgu.spl.mics.application.objects.Model;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FutureTest {

    private Future<Integer> future;

    @Before
    public void setUp() throws Exception {
        future = new Future<>();
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
}