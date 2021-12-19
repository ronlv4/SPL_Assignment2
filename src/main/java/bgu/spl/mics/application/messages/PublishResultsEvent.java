package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;

public class PublishResultsEvent implements Event<Model> {
    private Model goodModel;

    public PublishResultsEvent(Model model) {
        this.goodModel = model;
    }

    public Model getGoodModel() {
        return goodModel;
    }

}
