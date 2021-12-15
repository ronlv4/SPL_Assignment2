package bgu.spl.mics.application.objects;


import bgu.spl.mics.MessageBusImpl;

import java.nio.file.LinkPermission;
import java.util.Collection;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

    private static Cluster instance = null;
    private static GPU[] GPUS;
    private static CPU[] CPUS;

    private Cluster() {
    }
    /**
     * Retrieves the single instance of this class.
     */
    public static Cluster getInstance() {
        return instance != null? instance : new Cluster();
    }

    public static void setCPUS(CPU[] inputCPUS) {
        CPUS = inputCPUS;
    }

    public static void setGPUS(GPU[] inputGPUS) {
        GPUS = inputGPUS;
    }

    /**
     * used by GPU to send unprocessed batches to the cpu.
     * those batches will be transferred to some CPU by the Cluster
     * @param batch
     */
    public void sendUnprocessedBatch(DataBatch batch){

    }

    public void sendProcessedBatch(DataBatch batch){

    }

}
