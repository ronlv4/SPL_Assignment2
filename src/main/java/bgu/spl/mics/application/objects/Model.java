package bgu.spl.mics.application.objects;

import com.google.gson.annotations.Expose;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {

    @Expose
    private String name;
    @Expose
    private Data data;
    @Expose
    private Status status;
    @Expose
    private Results result;
    private Student student;


    public enum Status {
        PreTrained, Training, Trained, Tested
    }

    public enum Results {
        None, Good, Bad
    }

    public Model(String name, String type, int size) {
        this.name = name;
        Data data;
        if (type.equals("Images")) {
            data = new Data(Data.Type.Images, size);
        } else if (type.equals("Text")) {
            data = new Data(Data.Type.Text, size);
        } else {
            data = new Data(Data.Type.Tabular, size);
        }
        this.data = data;
        this.status = Status.PreTrained;
        this.result = Results.None;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public void setGoodResult() {
        setResult(Results.Good);
    }

    public void setBadResult() {
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
