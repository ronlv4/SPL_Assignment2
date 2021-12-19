package bgu.spl.mics.application.objects;

import com.google.gson.annotations.Expose;

import java.util.LinkedList;

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

    @Expose private String name;
    @Expose private String department;
    @Expose private Degree status;
    @Expose private int publications;
    @Expose private int papersRead;
    private Model[] models;
    @Expose public static LinkedList<Model> trainedModels = new LinkedList<>();

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
