package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

import java.util.List;

public class PublishConferenceBroadcast implements Broadcast {
    private List<Model> toPublish;

    public PublishConferenceBroadcast(List<Model> toPublish){
        this.toPublish=toPublish;
    }

    public List<Model> get(){
        return toPublish;
    }
}
