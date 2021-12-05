package bgu.spl.mics;

import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.DataBatch;
import org.junit.Before;
import org.junit.Test;

import static bgu.spl.mics.application.objects.Data.Type.Images;
import static org.junit.Assert.*;

public class CPUTest {

    private CPU cpu32Cores;
    private CPU cpu16Cores;
    private DataBatch batch;

    @Before
    public void setUp() throws Exception {
        Cluster cluster = Cluster.getInstance();
        cpu32Cores = new CPU(32);
        cpu16Cores = new CPU(16);
        batch = new DataBatch(new Data(Images, 0, 500_000),0);
    }

    @Test
    public void advanceTick() {
        int ticksBefore = cpu16Cores.getTicks();
        cpu16Cores.advanceTick();
        int ticksAfter = cpu16Cores.getTicks();
        assertEquals(ticksBefore + 1, ticksAfter);

    }

    @Test
    public void getNumOfCores() {
        assertEquals(16, cpu16Cores.getNumOfCores());
        assertEquals(32, cpu32Cores.getNumOfCores());
    }

    @Test
    public void addDataBatch() {
        int numOfBatchesBefore = cpu16Cores.getNumOfBatches();
        cpu16Cores.addDataBatch(batch);
        int numOfBatchesAfter = cpu16Cores.getNumOfBatches();
        assertEquals(numOfBatchesBefore + 1, numOfBatchesAfter);
    }
}