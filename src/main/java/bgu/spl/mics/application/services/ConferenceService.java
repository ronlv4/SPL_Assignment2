package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.PublishResultsEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Model;

import java.util.LinkedList;
import java.util.List;


/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConfrenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {

    private ConfrenceInformation conference;
    private MessageBusImpl messageBus;
    private List<Model> toPublish;

    public ConferenceService(String name, ConfrenceInformation conference) {
        super(name);
        this.conference = conference;
        this.messageBus=MessageBusImpl.getInstance();
        toPublish = new LinkedList<Model>();
    }

    @Override
    protected void initialize() {
        messageBus.register(this);
        subscribeEvent(PublishResultsEvent.class,c->{toPublish.add(c.get());});
        //subscribeBroadcast(TickBroadcast.class,c->);
        PublishConferenceBroadcast broadcast = new PublishConferenceBroadcast(toPublish);
        messageBus.sendBroadcast(broadcast);
    }
}
