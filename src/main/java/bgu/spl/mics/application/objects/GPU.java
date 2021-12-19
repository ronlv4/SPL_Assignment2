package bgu.spl.mics.application.objects;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {
        RTX3090,
        RTX2080,
        GTX1080
    }

    private Type type;
    private Model model;
    private Cluster cluster;
    private int totalTime = 0;
    private AtomicInteger currentTick = new AtomicInteger(1);

    private Queue<DataBatch> VRAM;

    public GPU(Type type) {
        this.cluster = Cluster.getInstance();
        this.type = type;
        if (type == Type.RTX3090) {
            VRAM = new LinkedBlockingQueue<>(32);
        } else if (type == Type.RTX2080) {
            VRAM = new LinkedBlockingQueue<>(16);
        } else {
            VRAM = new LinkedBlockingQueue<>(8);
        }
    }

    public Collection<DataBatch> getUnprocessedBatches() {
        return null;
    }

    public void advanceTick() {
        if (!VRAM.isEmpty()) {
            DataBatch batch = VRAM.poll();
            totalTime++;
            currentTick.incrementAndGet();
            System.out.println(Thread.currentThread().getName() + " training batch index " + batch.getStartIndex());
            model.setStatus(Model.Status.Training);
            if (type == Type.RTX3090) {
                Train3090(batch);
            } else if (batch.getDataType() == Data.Type.Text) {
                Train2080(batch);
            } else {
                Train1080(batch);
            }
        }
    }

    private void Train3090(DataBatch batch) {
        train(batch, 1);
    }

    private void Train2080(DataBatch batch) {
        train(batch, 2);
    }

    private void Train1080(DataBatch batch) {
        train(batch, 4);
    }

    private void train(DataBatch batch, int trainTimeRequired) {
//        System.out.println("Current tick: " + currentTick.intValue());
//        System.out.println("starting train tick: " + batch.getStartingTrainTick());
//        System.out.println("traing time required: " + trainTimeRequired);
        if (currentTick.intValue() - batch.getStartingTrainTick() >= trainTimeRequired) {
            System.out.println("finished training a batch");
            if (batch.getStartIndex() == getData().getSize() - 1000) {
                finalizeModelTraining();
                System.out.println("finished training a model");
            }
        }
        else {
            addProcessedBatch(batch);
        }
    }

    public void tryAddProcessedBatch(DataBatch batch) {
        batch.setStartingTrainTick(currentTick.intValue());
        addProcessedBatch(batch);
    }
    private void addProcessedBatch(DataBatch batch){
        boolean wasAdded;
        do {
//            System.out.println("trying to add");
            wasAdded = VRAM.offer(batch);
        } while (!wasAdded);
    }

    public int getNumberOfProcessedBatches() {
        return VRAM.size();
    }

    public void finalizeModelTraining() {
        model.setStatus(Model.Status.Trained);
        System.out.println("finished Training");
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Data getData() {
        return model.getData();
    }


    public Model getModel() {
        return model;
    }

    public Type getType() {
        return type;
    }

    public int getCurrentTick() {
        return currentTick.get();
    }

    public int getTotalTime() {
        return totalTime;
    }
}
