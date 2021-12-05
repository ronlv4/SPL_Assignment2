package bgu.spl.mics.application.objects;


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
    private static Collection<GPU> GPUS;
    private static Collection<CPU> CPUS;

    private Cluster() {
    }
    /**
     * Retrieves the single instance of this class.
     */
    public static Cluster getInstance() {
        if (instance == null){
            instance = new Cluster();
        }
        return instance;
    }

    public static void setCPUS(Collection<CPU> inputCPUS) {
        CPUS = inputCPUS;
    }

    public static void setGPUS(Collection<GPU> inputGPUS) {
        GPUS = inputGPUS;
    }
}
