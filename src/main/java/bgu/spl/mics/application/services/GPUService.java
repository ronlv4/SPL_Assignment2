package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.messages.DataPreProcessEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private final GPU gpu;
    private Model model;
    private Event<Model> currentEvent;

    private final Cluster cluster = Cluster.getInstance();
    private final MessageBusImpl messageBus = MessageBusImpl.getInstance();

    public GPUService(String name, GPU gpu) {
        super(name);
        this.gpu = gpu;
    }

    private void createAndSendBatches(Data data) {
        for (int i = 0; i < data.getSize(); i += 1000) {
            DataBatch batch = new DataBatch(data, i, gpu.getGPUIndex());
            cluster.sendUnprocessedBatch(batch);
        }

    }

    @Override
    protected void initialize() {
        messageBus.register(this);
        subscribeBroadcast(TickBroadcast.class, c -> {
            gpu.advanceTick();
            if (model != null && model.getStatus() == Model.Status.Trained) {
//                System.out.println("completed training");
                complete(currentEvent, model);
            }
            if (c.getCurrentTick() == 0) {
                Thread.currentThread().interrupt();
            }
        });
        subscribeEvent(TrainModelEvent.class, c -> {
//            System.out.println(Thread.currentThread().getName() + " got a model");
            currentEvent = c;
            Model model = c.getModel();
            gpu.setModel(model);
            this.model = model;
            createAndSendBatches(model.getData());
        });
        subscribeEvent(TestModelEvent.class, c -> {
            System.out.println("Testing a model");
            Model model = c.getModelToTest();
            if (model.getStudent().isMsc()) {
                if (Math.random() < 0.6) {
                    model.setGoodResult();
                } else {
                    model.setBadResult();
                }
            } else {
                if (Math.random() < 0.8) {
                    model.setGoodResult();
                } else {
                    model.setBadResult();
                }
            }
            model.setStatus(Model.Status.Tested);
            messageBus.complete(c, model);
        });
    }

}
