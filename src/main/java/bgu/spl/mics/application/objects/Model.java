package bgu.spl.mics.application.objects;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {

    private String name;
    private Data data;
    private Student student;
    private Results result;
    private Status status;


    enum Status {
        PreTrained,
        Training,
        Trained,
        Tested
    }

    public enum Results {
        None,
        Good,
        Bad
    }

    public Model(String name, String type, int size){
        this.name = name;
        Data data;
        if (type.equals("Images")) {
            data = new Data(Data.Type.Images, 0, size);
        } else if (type.equals("Text")){
            data = new Data(Data.Type.Text, 0, size);
        }
        else {
            data = new Data(Data.Type.Tabular, 0, size);
        }
        this.data = data;
        this.student = student;
        this.status = Status.PreTrained;
        this.result = Results.None;
    }

    public Data getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private void setResult(Results result) {
        this.result = result;
    }

    public void setGoodResult(){
        setResult(Results.Good);
    }
    public void setBadResult(){
        setResult(Results.Bad);
    }
    public Student getStudent() {
        return student;
    }

    public Results getResult() {
        return result;
    }

    public Status getStatus() {
        return status;
    }
}
