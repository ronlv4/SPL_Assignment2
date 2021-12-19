package bgu.spl.mics.application.messages;

import java.util.List;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;


public class PublishConferenceBroadcast implements Broadcast {
    private List<Model> goodModels;

    public PublishConferenceBroadcast(List<Model> toPublish) {
        this.goodModels = toPublish;
    }

    public List<Model> getGoodModels() {
        return goodModels;
    }
}
