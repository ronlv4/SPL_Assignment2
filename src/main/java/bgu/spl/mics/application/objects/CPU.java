package bgu.spl.mics.application.objects;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private final int cores;
    private int currentTick = 1;
    private int numOfProcessed;
    private BlockingQueue<DataBatch> unprocessedDataBatches;
    private final Cluster cluster = Cluster.getInstance();
    private int totalTime = 0;

    public CPU(int cores) {
        this.cores = cores;
        this.numOfProcessed = 0;
        this.unprocessedDataBatches = new LinkedBlockingQueue<>();
    }

    public int getTicks() {
        return currentTick;
    }

    public int getNumOfBatches() {
        return unprocessedDataBatches.size();
    }

    /**
     * @pre getTicks() + 1 == @post getTicks()
     */
    public void advanceTick() {
        currentTick++;
        if (!unprocessedDataBatches.isEmpty()) {
            DataBatch batch = unprocessedDataBatches.poll();
            totalTime++;
            if (batch.getDataType() == Data.Type.Tabular) {
                processTabular(batch);
            } else if (batch.getDataType() == Data.Type.Text) {
                processText(batch);
            } else {
                processImage(batch);
            }
        }
    }

    private void processTabular(DataBatch batch) {
        process(batch, 32 / cores);
    }

    private void processText(DataBatch batch) {
        process(batch, 32 / cores * 2);
    }

    private void processImage(DataBatch batch) {
        process(batch, 32 / cores * 4);
    }

    private void process(DataBatch batch, int processTimeRequired) {
        if (currentTick - batch.getStartingProcessTick() >= processTimeRequired) {
            cluster.sendProcessedBatch(batch);
            numOfProcessed++;
        } else {
            unprocessedDataBatches.add(batch);
        }
    }

    /**
     * @return number of cores of this CPU
     */
    public int getNumOfCores() {
        return cores;
    }

    /**
     * @param batch - the batch of data to be added to the DataBatch collection later to be processed
     * @pre this.getNumOfBatches.size() + 1 = @post this.getNumOfBatches()
     */
    public void addDataBatch(DataBatch batch) {
        batch.setStartingProcessTick(currentTick);
        unprocessedDataBatches.add(batch);
    }

    public int getNumOfProcessed() {
        return numOfProcessed;
    }

    public int getTotalTime() {
        return totalTime;
    }
}
