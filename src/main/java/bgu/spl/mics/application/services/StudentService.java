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

    public StudentService(String name, Student student) {
        super(name);
        this.student = student;
    }


    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);
        subscribeBroadcast(TickBroadcast.class, c -> {
            if(c.getCurrentTick()==0)
                System.out.println("interrupting service " + getName());
                Thread.currentThread().interrupt();
            });
        subscribeBroadcast(PublishConferenceBroadcast.class, c -> {
            for (Model goodModel : c.getGoodModels()) {
                if (goodModel.getStudent().equals(student)){
                    student.increasePublications();
                }
                else{
                    student.increasePapersRead();
                }
            }
        });

        for (Model model : student.getModels()) {
            Future<Model> future;
            do {
                future  = sendEvent(new TrainModelEvent(model));
            }while (future == null);
            System.out.println("future is not null");
            future.get();
            System.out.println("got a future");
            future = sendEvent(new TestModelEvent(model));
            Model finishedModel = future.get();
            if (finishedModel.getResult() == Model.Results.Good){
                sendEvent(new PublishResultsEvent(model));
            }
        }
    }
}
