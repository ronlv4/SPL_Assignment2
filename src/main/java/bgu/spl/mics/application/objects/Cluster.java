package bgu.spl.mics.application.objects;


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
    private Object[] statistics = new Object[3];
    private int cpuPointer;
    private int gpuPointer;

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
    }

    /**
     * used by GPU to send unprocessed batches to the cpu.
     * those batches will be transferred to some CPU by the Cluster
     * @param batch - the unprocessed batch to transfer
     */
    public synchronized void sendUnprocessedBatch(DataBatch batch){
        CPUS[cpuPointer].addDataBatch(batch);
        cpuPointer++;
        cpuPointer %= CPUS.length;
    }

    public void setStatistics(Object[] statistics){
        this.statistics = statistics;
    }

    /**
     * used by CPU to send processed batches back to the GPU
     * those batches will be transferred to some GPU by the Cluster
     * @param batch - the processed batch to transfer
     */
    public void sendProcessedBatch(DataBatch batch){
//        System.out.println("from cluster sending a processed batch");
        batch.getGpu().tryAddProcessedBatch(batch);
    }

    public Object[] getStatistics() {
        return statistics;
    }
}
