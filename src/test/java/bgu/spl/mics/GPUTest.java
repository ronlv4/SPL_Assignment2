package bgu.spl.mics;

import bgu.spl.mics.application.objects.*;
import org.junit.Before;
import org.junit.Test;

import static bgu.spl.mics.application.objects.GPU.Type.RTX3090;
import static org.junit.Assert.*;

public class GPUTest {
    private GPU gpu;
    private DataBatch batch;

    @Before
    public void setUp() throws Exception {
        batch = new DataBatch(new Data(Data.Type.Tabular, 0,100_000), 0);
        gpu = new GPU(RTX3090);
    }

    @Test
    public void getUnprocessedBatches() {
    }

    @Test
    public void advanceTick() {
    }

    @Test
    public void addProcessedBatch() {
    }

    @Test
    public void getNumberOfProcessedBatches() {
    }

    @Test
    public void finalizeModelTraining() {
    }

    @Test
    public void getData() {
    }

    @Test
    public void getModel() {
    }

    @Test
    public void getType() {
    }
}