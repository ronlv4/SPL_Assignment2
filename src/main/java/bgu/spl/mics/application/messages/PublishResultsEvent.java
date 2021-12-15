package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;

import java.util.List;

public class PublishResultsEvent implements Event {
    private List<Model> goodModels;

    public PublishResultsEvent(List<Model> models){
        this.goodModels = models;
    }

    public List<Model> getGoodModels(){
        return goodModels;
    }

}
