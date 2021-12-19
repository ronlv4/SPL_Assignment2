package bgu.spl.mics.application.objects;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CPUTest {

    private CPU cpu;

    @Before
    public void setUp() throws Exception {
        this.cpu = new CPU(32);
    }

    @Test
    public void getNumOfBatches() {
        int numOfBatchesBefore = cpu.getNumOfBatches();
        Data data = new Data(Data.Type.Images, 200_000);
        cpu.addDataBatch(new DataBatch(data, 0, 5));
        int numOfBatchesAfter = cpu.getNumOfBatches();
        assertEquals(numOfBatchesBefore + 1, numOfBatchesAfter);
    }

    @Test
    public void advanceTick() {
        int tickBefore = cpu.getTicks();
        cpu.advanceTick();
        int tickAfter = cpu.getTicks();
        assertEquals(tickBefore + 1, tickAfter);
    }
}