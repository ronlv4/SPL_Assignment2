package bgu.spl.mics.application.objects;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private int totalTime;
    private AtomicInteger currentTick = new AtomicInteger(1);

    private Queue<DataBatch> VRAM;

    public GPU(Type type) {
        this.cluster = Cluster.getInstance();
        this.type = type;
        this.totalTime = 0;
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

    public synchronized void advanceTick() {
        if (!VRAM.isEmpty()) {
            totalTime++;
            currentTick.incrementAndGet();
            DataBatch batch = VRAM.poll();
            System.out.println(Thread.currentThread().getName() + " training batch index " + batch.getStartIndex());
            batch.setStartingTrainTick(currentTick.intValue());
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
        if (currentTick.intValue() - batch.getStartingTrainTick() == trainTimeRequired) {
            if (batch.getStartIndex() == getData().getSize() - 1000) {
                finalizeModelTraining();
            }
        }
    }

    public void addProcessedBatch(DataBatch batch) {
        boolean wasAdded;
        do {
            wasAdded = VRAM.offer(batch);
        } while (!wasAdded);

    }

    public int getNumberOfProcessedBatches() {
        return VRAM.size();
    }

    public synchronized void finalizeModelTraining() {
        model.setStatus(Model.Status.Trained);
        this.notifyAll();
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
