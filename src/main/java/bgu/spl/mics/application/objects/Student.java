package bgu.spl.mics.application.objects;

import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    public enum Degree {
        MSc, PhD
    }

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;
    private Model[] models;
    private List<Model> trainedModels = new LinkedList<>();

    public void addTrainedModel(Model trainedModel){
        trainedModels.add(trainedModel);

    }
    public Model[] getModels() {
        return models;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public Degree getStatus() {
        return status;
    }

    public int getPublications() {
        return publications;
    }

    public int getPapersRead() {
        return papersRead;
    }

    public boolean isMsc(){
        return status == Degree.MSc;
    }

    public boolean isPhD(){
        return status == Degree.PhD;
    }

    public void increasePublications(){
        publications++;
    }

    public void increasePapersRead(){
        papersRead++;
    }
}
