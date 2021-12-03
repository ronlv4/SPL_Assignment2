package bgu.spl.mics.application.objects;

import bgu.spl.mics.Message;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private int cores;
    private boolean isProcessing; //decide between the 2
    private int ticks; // decide between the 2
//    private Collection<DataBatch> dataBatchContainer;
    private Cluster cluster;


    public CPU(int cores, Cluster cluster){

    }

    public void advanceTick(){

    }

    public int getNumOfCores(){
        return cores;
    }

    public void addDataBatch(DataBatch batch){

    }
}
