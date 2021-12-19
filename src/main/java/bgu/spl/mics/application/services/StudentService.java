package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {

    private Student student;
    private int modelIndex;

    public StudentService(String name, Student student) {
        super(name);
        this.student = student;
    }

    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);
        modelIndex = 0;
        subscribeBroadcast(TickBroadcast.class, c -> {
            Model currentModel = student.getModels()[modelIndex];
            Future<Model> future;
            if (currentModel.getStatus() == Model.Status.PreTrained) {
                System.out.println("Sent a new Train Model event to model " + currentModel.getName());
                currentModel.setStatus(Model.Status.Training);
                sendEvent(new TrainModelEvent(currentModel));
            } else if (currentModel.getStatus() == Model.Status.Trained) {

                System.out.println("finished training model " + currentModel.getName());
                sendEvent(new TestModelEvent(currentModel));
            } else if (currentModel.getStatus() == Model.Status.Tested) {
                if (currentModel.getResult() == Model.Results.Good) {
                    System.out.println("it has good results");
                    sendEvent(new PublishResultsEvent(currentModel));
                }
                if (modelIndex+1 < student.getModels().length)
                    modelIndex++;
                else
                    Thread.currentThread().interrupt();
            }
            if (c.getCurrentTick() == 0) {
                Thread.currentThread().interrupt();
            }
        });
        subscribeBroadcast(PublishConferenceBroadcast.class, c -> {
            for (Model goodModel : c.getGoodModels()) {
                if (goodModel.getStudent().equals(student)) {
                    student.increasePublications();
                } else {
                    student.increasePapersRead();
                }
            }
        });
    }
}