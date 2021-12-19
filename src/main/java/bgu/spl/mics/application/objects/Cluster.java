package bgu.spl.mics.application.objects;


import bgu.spl.mics.application.InputParsing.InputFile;
import com.google.gson.annotations.Expose;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

    private static Cluster instance = null;
    private static boolean isDone = false;
    private static GPU[] GPUS;
    private static CPU[] CPUS;
    private InputFile arrays;
    private Object[] statistics = new Object[6];
    private int cpuPointer;
    private int gpuPointer;
    private Queue<DataBatch>[] processedDataBatches;
    @Expose private int batchesProcessed=0;
    @Expose private int gpuTimeUsed=0;
    @Expose private int cpuTimeUsed=0;

    private Cluster() {

    }
    /**
     * Retrieves the single instance of this class.
     */

    public static Cluster getInstance() {
        if (!isDone) {
            synchronized (Cluster.class) {
                if (!isDone) {
                    instance = new Cluster();
                    isDone = true;
                }
            }
        }
        return instance;
    }

    public void setCPUS(CPU[] inputCPUS) {
        CPUS = inputCPUS;
        cpuPointer = 0;
    }

    public void setGPUS(GPU[] inputGPUS) {
        GPUS = inputGPUS;
        gpuPointer = 0;
        processedDataBatches = new Queue[GPUS.length];
        for (int i = 0; i< GPUS.length; i++){
            processedDataBatches[i] = new ConcurrentLinkedQueue<>();
        }
    }

    /**
     * used by GPU to send unprocessed batches to the cpu.
     * those batches will be transferred to some CPU by the Cluster
     * @param batch - the unprocessed batch to transfer
     */
    public synchronized void sendUnprocessedBatch(DataBatch batch){
//        System.out.println(batch.getStartIndex() + " of model " +batch.getGpu().getModel().getName() + " to cpu index " + cpuPointer);
        CPUS[cpuPointer].addDataBatch(batch);
        cpuPointer++;
        cpuPointer %= CPUS.length;
    }

    public void setArrays(InputFile statistics){
        this.arrays = statistics;
    }

    /**
     * used by CPU to send processed batches back to the GPU
     * those batches will be transferred to some GPU by the Cluster
     * @param batch - the processed batch to transfer
     */
    public void sendProcessedBatch(DataBatch batch){
//        System.out.println("from cluster sending a processed batch");
        processedDataBatches[batch.getGPUIndex()].add(batch);
    }

    public DataBatch requestProcessedBatch(int GPUIndex){
        if (processedDataBatches[GPUIndex].isEmpty()){
            return null;
        }
        return processedDataBatches[GPUIndex].poll();
    }

    public Object[] getStatistics() {
        for(int i=0; i< GPUS.length; i++){
            gpuTimeUsed+=GPUS[i].getTotalTime();
        }
        for(int i=0; i< CPUS.length; i++){
            batchesProcessed+=CPUS[i].getNumOfProcessed();
            cpuTimeUsed+=CPUS[i].getTotalTime();
        }
        statistics[0] = arrays;
        statistics[1] = GPUS;
        statistics[2] = CPUS;
        statistics[3] = gpuTimeUsed;
        statistics[4] = cpuTimeUsed;
        statistics[5] = batchesProcessed;

        return statistics;
    }

}
