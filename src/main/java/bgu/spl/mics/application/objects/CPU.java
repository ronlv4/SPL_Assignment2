package bgu.spl.mics.application.objects;

import bgu.spl.mics.Message;

import java.util.Collection;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private int cores;
    private boolean isProcessing; //decide between the 2
    private int ticks; // decide between the 2
    private Collection<DataBatch> dataBatchCollection;
    private Cluster cluster;


    public CPU(int cores){
        this.cores = cores;
        this.cluster = Cluster.getInstance();
        this.ticks = 0;
    }

    public int getTicks() {
        return ticks;
    }

    public Collection<DataBatch> getDataBatchCollection() {
        return dataBatchCollection;
    }

    public int getNumOfBatches(){
        return dataBatchCollection.size();
    }

    public Cluster getCluster() {
        return cluster;
    }

    /**
     * @pre getTicks() + 1 == @post getTicks()
     *
     */
    public void advanceTick(){

    }

    /**
     * @return number of cores of this CPU
     */
    public int getNumOfCores(){
        return cores;
    }

    /**
     * @pre this.getNumOfBatches.size() + 1 = @post this.getNumOfBatches()
     * @param batch - the batch of data to be added to the DataBatch collection later to be processed
     */
    public void addDataBatch(DataBatch batch){

    }
}
