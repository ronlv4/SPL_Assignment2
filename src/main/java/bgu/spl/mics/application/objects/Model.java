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


    enum Status {PreTrained,Training,Trained,Tested;};
    enum Results {None, Good, Bad};

    public Model(String name, Data data, Student student){
        this.name = name;
        this.data = data;
        this.student = student;
        this.status =Status.PreTrained;
        this.result = Results.None;
    }

    public Data getData(){
        return data;
    }
}
