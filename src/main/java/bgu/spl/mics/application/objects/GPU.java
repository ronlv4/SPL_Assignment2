package bgu.spl.mics.application.objects;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
    private int currentTick;

    private Queue<DataBatch> VRAM;

    public GPU(Type type) {
        this.cluster = Cluster.getInstance();
        this.type = type;
        this.totalTime = 0;
        this.currentTick = 1;
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

    public synchronized Model advanceTick() {
        if (!VRAM.isEmpty()) {
            totalTime++;
            DataBatch batch = VRAM.poll();
            batch.setStartingTrainTick(currentTick);
            model.setStatus(Model.Status.Training);
            if (type == Type.RTX3090) {
                return Train3090(batch);
            } else if (batch.getDataType() == Data.Type.Text) {
                return Train2080(batch);
            } else {
                return Train1080(batch);
            }
        }
        return null;
    }

    private Model Train3090(DataBatch batch) {
        return train(batch, 1);
    }

    private Model Train2080(DataBatch batch) {
        return train(batch, 2);
    }

    private Model Train1080(DataBatch batch) {
        return train(batch, 4);
    }

    private Model train(DataBatch batch, int trainTimeRequired) {
        if (currentTick - batch.getStartingTrainTick() == trainTimeRequired) {
            if (batch.getStartIndex() == getData().getSize()-1000){
                finalizeModelTraining();
                return model;
            }
        }
        return null;
    }

    public void addProcessedBatch(DataBatch batch) {
        boolean wasAdded;
        do {
            wasAdded = VRAM.offer(batch);
        }while(!wasAdded);
    }

    public int getNumberOfProcessedBatches() {
        return VRAM.size();
    }

    public void finalizeModelTraining() {
        model.setStatus(Model.Status.Trained);
//        return
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
        return currentTick;
    }

    public int getTotalTime(){return totalTime;}
}
