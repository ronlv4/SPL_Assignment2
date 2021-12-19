package bgu.spl.mics.application.objects;

import bgu.spl.mics.Event;
import com.google.gson.annotations.Expose;

import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConferenceInformation {

    @Expose private String name;
    @Expose private int date;
    @Expose public List<Model>  publications = null;

    public ConferenceInformation(String name, int date){
        this.name=name;
        this.date=date;
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


}
