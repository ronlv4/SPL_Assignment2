package bgu.spl.mics.application.objects;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Queue;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {


    /* GPU receives a model which holds Data.
    GPU split Data into Data batches and then sends them to CPUS
    .
    .
    .
    GPU receives processed data batches and then uses them to train the model
    when the GPU is done training a model, it sends it back to the messageBus
     */


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
    int ticks;

    public GPU(Model model, Cluster cluster, Type type) {
//        this.model = model;
//        this.cluster = cluster;
//        if (type.equals("RTX3090")){
//            this.type=Type.RTX3090;
//        }
//        else if (type.equals("RTX2080")){
//            this.type = Type.RTX2080;
//        }
//        else if (type.equals("GTX1080")){
//            this.type = Type.GTX1080;
//        }
//        else{

//        }
    }

    public Collection<DataBatch> getUnprocessedBatches() {
        return null;
    }

    public void advanceTick() {

    }

    public void addProcessedBatch(DataBatch batch) {

    }

    public int getNumberOfProcessedBatches() {
        return 0;
    }

    public void finalizeModelTraining() {

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
}