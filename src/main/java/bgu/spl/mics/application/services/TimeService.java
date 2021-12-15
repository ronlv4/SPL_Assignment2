package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

//	private static TimeService instance = null;
	private MessageBusImpl MessageBus;
	private int TickTime;//speed?
	private int Duration;
	private int time;
	private Timer timer;



	public TimeService(int TickTime, int Duration) {
		super("Universal_Time_Service");
		this.TickTime = TickTime;//speed?
		this.Duration = Duration;
		this.MessageBus = MessageBusImpl.getInstance();
		this.time = 1;
		this.timer = new Timer();
	}

	@Override
	protected void initialize() {
		MessageBus.register(this);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				time++;
				MessageBus.sendBroadcast(new TickBroadcast(time));
			}
		}, TickTime, Duration);
		terminate();//?
	}
	//public static TimeService getInstance(){
	//	return instance != null? instance : new TimeService();
	//}

}
