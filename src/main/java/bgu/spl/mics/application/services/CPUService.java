package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.messages.DataPreProcessEvent;

/**
 * CPU service is responsible for handling the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {

    private CPU cpu;
    private MessageBusImpl MessageBus;

    public CPUService(String name, CPU cpu) {
        super(name);
        this.cpu = cpu;
        this.MessageBus = MessageBusImpl.getInstance();
    }

    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);
        subscribeBroadcast(TickBroadcast.class, c->{
            cpu.advanceTick();
            if(c.getCurrentTick()==0){
                Thread.currentThread().interrupt();
            }
        });
    }
}
