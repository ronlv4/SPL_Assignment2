package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.CRMSRunner;
import bgu.spl.mics.application.InputParsing.InputFile;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Student;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {

    //	private static TimeService instance = null;
    private MessageBusImpl MessageBus;
    private int tickTime;
    private int Duration;
    private int currentTick;
    private Timer timer;

    public TimeService(int tickTime, int Duration) {
        super("Universal_Time_Service");
        this.tickTime = tickTime;
        this.Duration = Duration;
        this.MessageBus = MessageBusImpl.getInstance();
        this.currentTick = 1;
        this.timer = new Timer();
    }

    @Override
    protected void initialize() {
        MessageBus.register(this);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Sending tick broadcast: tick #" + currentTick);
                MessageBus.sendBroadcast(new TickBroadcast(currentTick));
                currentTick ++;
                if (currentTick > Duration) {
                    System.out.println("Sending termination tick");
                    MessageBus.sendBroadcast(new TickBroadcast(0));
                    timer.cancel();
                    CRMSRunner.buildOutputFile((InputFile) Cluster.getInstance().getStatistics()[0], (GPU[]) Cluster.getInstance().getStatistics()[1], (CPU[])Cluster.getInstance().getStatistics()[2]);
                    Thread.currentThread().interrupt();
                }
            }
        }, 3000, tickTime);
    }
}
