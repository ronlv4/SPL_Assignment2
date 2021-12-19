package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;

public class TestModelEvent implements Event<Model> {

    private Model modelToTest;

    public TestModelEvent(Model modelToTest) {
        this.modelToTest = modelToTest;
    }

    public Model getModelToTest() {
        return modelToTest;
    }
}
