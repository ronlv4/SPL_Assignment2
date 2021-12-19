package bgu.spl.mics.application.objects;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GPUTest {

    private GPU gpu;

    @Before
    public void setUp() throws Exception {
        this.gpu = new GPU(GPU.Type.RTX3090, 4);
    }


    @Test
    public void getModel() {
        Model model = new Model("deepLearningModel", "Images", 20_000);
        gpu.setModel(model);
        assertEquals(model, gpu.getModel());
    }
}