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

    @Before
    public void setUp() throws Exception {
        CPU cpu32Cors = new CPU(32,new Cluster());
        CPU cpu16Cors = new CPU(16,new Cluster());
        DataBatch batch = new DataBatch(new Data(Images));
    }

    @Test
    public void advanceTick() {

    }

    @Test
    public void getNumOfCores() {
    }

    @Test
    public void addDataBatch() {
    }
}