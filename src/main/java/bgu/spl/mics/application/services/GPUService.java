package bgu.spl.mics.application.services;
import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.messages.DataPreProcessEvent;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private final GPU gpu;
    private MessageBusImpl messageBus;
    private Model model;
    private Cluster cluster = Cluster.getInstance();
    /*
    flow:
    GPUService calls to messageBus.complete(Event<T> e, T result)
    inside meesageBus.complete we need to invoke studentService.complete(Event<T> e, T result)
    inside studentService.complete we need to invoke future.resolve(T result)
    inside future.resolve we need to change the result that future holds.
     */

    public GPUService(String name, GPU gpu) {
        super(name);
        this.gpu = gpu;
        this.messageBus = MessageBusImpl.getInstance();
    }

    private void createAndSendBatches(Data data) {
        Cluster cluster = Cluster.getInstance();
        for (int i = 0; i < data.getSize(); i += 1000) {
            DataBatch batch = new DataBatch(data, i, gpu);
            cluster.sendUnprocessedBatch(batch);
        }

    }

    @Override
    protected void initialize() {
        messageBus.register(this);
        subscribeBroadcast(TickBroadcast.class, c -> {
            gpu.advanceTick();
            if(c.getCurrentTick()==0){
                Thread.currentThread().interrupt();
            }
        });
        subscribeEvent(TrainModelEvent.class, c -> {
            Model model = c.getModel();
            createAndSendBatches(model.getData());
            synchronized (gpu) {
                if (model.getStatus() != Model.Status.Trained) {
//                    System.out.println("finished training model");
                    try {
                        gpu.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("finished Training the model");
                    complete(c, model);
                }
            }
        });
        subscribeEvent(TestModelEvent.class, c -> {
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
            messageBus.complete(c, model);
        });
    }

}
