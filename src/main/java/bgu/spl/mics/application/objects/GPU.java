package bgu.spl.mics.application.objects;

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
        RTX3090, RTX2080, GTX1080
    }

    private final Type type;
    private Model model;
    private final Cluster cluster = Cluster.getInstance();
    private int totalTime = 0;
    private final int GPUIndex;
    private int currentTick = 1;

    private LinkedBlockingQueue<DataBatch> VRAM;

    public GPU(Type type, int index) {
        this.type = type;
        if (type == Type.RTX3090) {
            VRAM = new LinkedBlockingQueue<>(32);
        } else if (type == Type.RTX2080) {
            VRAM = new LinkedBlockingQueue<>(16);
        } else {
            VRAM = new LinkedBlockingQueue<>(8);
        }
        this.GPUIndex = index;
    }


    public void advanceTick() {
        if (VRAM.remainingCapacity() > 0) {
            fillVRAM();
        }
        if (!VRAM.isEmpty()) {
            DataBatch batch = VRAM.poll();
            totalTime++;
            currentTick++;
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
        if (currentTick - batch.getStartingTrainTick() >= trainTimeRequired) {
            Data currentData = batch.getData();
            currentData.incTrainedBatches();

            if (currentData.getTrainedBatches() == currentData.getSize() / 1000) {
                finalizeModelTraining();
            }
        } else {
            VRAM.offer(batch);
        }
    }

    public int getNumberOfProcessedBatches() {
        return VRAM.size();
    }

    public void finalizeModelTraining() {
        model.setStatus(Model.Status.Trained);
        model.getStudent().addTrainedModel(model);
    }

    public void fillVRAM() {
        DataBatch nextBatch = cluster.requestProcessedBatch(GPUIndex);
        if (nextBatch != null) {
            nextBatch.setStartingTrainTick(currentTick);
            VRAM.offer(nextBatch);
        }
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

    public int getTotalTime() {
        return totalTime;
    }

    public int getGPUIndex() {
        return GPUIndex;
    }
}
