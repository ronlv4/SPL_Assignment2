package bgu.spl.mics.application.objects;

import org.junit.Before;
import org.junit.Test;

import static bgu.spl.mics.application.objects.GPU.Type.RTX3090;
import static org.junit.Assert.*;

public class GPUTest {
    private GPU gpu;
    private Data data;
    private DataBatch batch;
    private Model model;

    @Before
    public void setUp() {
        data = new Data(Data.Type.Tabular, 0, 100_000);
        batch = new DataBatch(data, 0);
        gpu = new GPU(RTX3090);
    }

    @Test
    public void advanceTick() {
        int ticksBefore = gpu.getTicks();
        gpu.advanceTick();
        int ticksAfter = gpu.getTicks();
        assertEquals(ticksBefore + 1, ticksAfter);
    }

    @Test
    public void addProcessedBatch() {
        int numOfBatchesBefore = gpu.getNumberOfProcessedBatches();
        gpu.addProcessedBatch(batch);
        int numOfBatchesAfter = gpu.getNumberOfProcessedBatches();
        assertEquals(numOfBatchesBefore + 1, numOfBatchesAfter);
    }

    @Test
    public void getNumberOfProcessedBatches() {
        assertEquals(data.getSize() / 1000, gpu.getNumberOfProcessedBatches());
    }

    @Test
    public void getData() {
        assertEquals(data, gpu.getData());
    }

    @Test
    public void getModel() {
        model = new Model("deepLearning", data, new Student());
        gpu.setModel(model);
        assertEquals(gpu.getModel(), model);
    }

    @Test
    public void getType() {
        assertEquals(gpu.getType(), RTX3090);
    }
}