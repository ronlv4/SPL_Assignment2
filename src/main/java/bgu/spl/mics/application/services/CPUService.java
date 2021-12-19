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
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {

    private final CPU cpu;
    private final MessageBusImpl messageBus = MessageBusImpl.getInstance();

    public CPUService(String name, CPU cpu) {
        super(name);
        this.cpu = cpu;
    }

    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);
        subscribeBroadcast(TickBroadcast.class, c -> {
//            System.out.println("from CPUService - calling to cpu.advancetick");
            cpu.advanceTick();
            if (c.getCurrentTick() == 0) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
