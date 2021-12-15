package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {

    private Data data;
    private int startIndex;
    private int startingProcessTick;

    public DataBatch(Data data, int startIndex) {
        this.data = data;
        this.startIndex = startIndex;
    }

    public void setStartingProcessTick(int startingProcessTick) {
        this.startingProcessTick = startingProcessTick;
    }

    public int getStartingProcessTick() {
        return startingProcessTick;
    }

    public Data.Type getDataType(){
        return data.getType();
    }
}
