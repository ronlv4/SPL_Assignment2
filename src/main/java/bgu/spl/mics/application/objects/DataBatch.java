package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {

    private Data data;
    private int startIndex;
    private int startingProcessTick;
    private int startingTrainTick;

    public int getStartIndex() {
        return startIndex;
    }

    public int getStartingTrainTick() {
        return startingTrainTick;
    }

    public void setStartingTrainTick(int startingTrainTick) {
        this.startingTrainTick = startingTrainTick;
    }

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
