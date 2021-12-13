package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;

public class PublishResultsEvent implements Event {
    private Model model;

    public PublishResultsEvent(Model models){
        this.model = models;
    }

    public Model get(){
        return model;
    }

}
