package bgu.spl.mics.application.objects;

import org.junit.Before;
import org.junit.Test;

import static bgu.spl.mics.application.objects.GPU.Type.RTX3090;
import static org.junit.Assert.*;

public class GPUTest {
    private GPU gpu;
    private Data data;
    private Model deepLearningModel;
    private DataBatch batch;

    @Before
    public void setUp() throws Exception {
        data = new Data(Data.Type.Tabular, 0, 100_000);
        deepLearningModel=new Model("deepLearningModel", data, new Student());
        gpu = new GPU(RTX3090);
    }

    /*@Test
    public void getUnprocessedBatches() {
        DataBatch getbatch=gpu.getUnprocessedBatches();
    }*/

   /* @Test
    public void advanceTick() {
        int ticksBefore = gpu.getTicks();
        gpu.advanceTick();
        int ticksAfter = gpu.getTicks();
        assertEquals(ticksBefore + 1, ticksAfter);
    }*/

    /*@Test
    public void addProcessedBatch() {

    }*/

    @Test
    public void getNumberOfProcessedBatches() {
        assertEquals(data.getSize()/1000,gpu.getNumberOfProcessedBatches());
    }

    /*
    @Test
    public void finalizeModelTraining() {
    }*/

    @Test
    public void getData() {
        assertEquals(data,gpu.getData());
    }

    @Test
    public void getModel() {
        assertEquals(gpu.getModel(),deepLearningModel);
    }

    @Test
    public void getType() {
        assertEquals(gpu.getType(),RTX3090);
    }
}