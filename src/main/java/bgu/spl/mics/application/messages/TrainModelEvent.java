package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event<Model> {

    public TrainModelEvent(Model model) {
        this.model = model;
    }

    private Model model;

    public Model getModel() {
        return model;
    }
}
