package bgu.spl.mics.application.objects;

import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConferenceInformation {

    private String name;
    private int date;
    private List publications;

    public ConferenceInformation(String name, int date){
        this.name=name;
        this.date=date;
        this.publications = null;
    }

    public String getName(){
        return name;
    }

    public int getDate(){
        return date;
    }

    public List getpublications(){
        return publications;
    }

    public void setResults(List goodResultsModels) {
        this.publications = goodResultsModels;
    }
}
