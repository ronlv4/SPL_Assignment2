package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

import java.util.List;

public class PublishConferenceBroadcast implements Broadcast {
    private List<Model> goodModels;

    public PublishConferenceBroadcast(List<Model> toPublish){
        this.goodModels =toPublish;
    }

    public List<Model> getGoodModels(){
        return goodModels;
    }
}
