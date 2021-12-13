package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	/*
	subscribers: need to hold which microservice is subscribed to some type of event
	subscribeEvent: mark that some microservice is subscribed to an event.
	sendEvent: need to add an event to a microservice queue who is subscribed in a round robbin manner.
	this queue is reachable through the map microservices
	map from an event type to a queue of the services that are subscribed to it.
	the first ms in the queue is the next to be assigned the event.
	 */

    private static MessageBusImpl instance = null;
    private static boolean isDone = false;
    private Map<MicroService, Queue<Message>> microServices;
    private Map<Class<? extends Event<?>>, Deque<MicroService>> subscribersByType;
    private Map<MicroService, Deque<Class<? extends Event<?>>>> subscribersByMicroService;

    private MessageBusImpl() {
        microServices = new HashMap<>();
        subscribersByType = new HashMap<>();
        subscribersByMicroService = new HashMap<>();
        instance = this;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        Deque<MicroService> subscribedMicroServiceDeque = subscribersByType.get(type);
        // adding the microsevice to the subscriberByType queue
        if (subscribedMicroServiceDeque == null){
            subscribedMicroServiceDeque = new ConcurrentLinkedDeque<>();
            subscribersByType.put(type, subscribedMicroServiceDeque);
        }
        subscribedMicroServiceDeque.addFirst(m);

        // adding the microservice to the subscriberByMicroService queue
        Deque<Class<? extends Event<?>>> typesSubscriptions = subscribersByMicroService.get(m);
        if (typesSubscriptions == null){
            typesSubscriptions = new ConcurrentLinkedDeque<>();
            subscribersByMicroService.put(m, typesSubscriptions);
        }
        typesSubscriptions.add(type);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendBroadcast(Broadcast b) {
        // TODO Auto-generated method stub

    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        Queue<MicroService> subscribedMicroServiceQueue = subscribersByType.get(e.getClass());
        if (subscribedMicroServiceQueue == null) //TODO make sure that if an event has no subscribers it's being deleted from the map
            return null; //TODO what should we do if the type of event has no subscribers?
        MicroService microServiceInLine = subscribedMicroServiceQueue.remove();
        microServices.get(microServiceInLine).add(e);
        subscribedMicroServiceQueue.add(microServiceInLine);
        return new Future<>();
    }

    @Override
    public void register(MicroService m) {
        microServices.put(m, new ConcurrentLinkedQueue<>());
    }

    @Override
    public void unregister(MicroService m) {
        for (Class<? extends Event<?>> type: subscribersByMicroService.get(m)){
            subscribersByType.get(type).remove(m);
        }
        subscribersByMicroService.remove(m);
        microServices.remove(m); // does not throw exception if key does not exist in map
    }

    @Override
    public synchronized Message awaitMessage(MicroService m) throws InterruptedException {
        Queue<? extends Message> microServiceQueue = microServices.get(m);
        if (microServiceQueue.isEmpty()) {
            wait(); // locked on "this", waiting for notify when the Queue won't be empty
        }
        return microServiceQueue.remove();
    }

    public static MessageBusImpl getInstance() {
        if(isDone == false) {
            synchronized(MessageBusImpl.class)
            {
                if(isDone == false) {
                    instance = new MessageBusImpl();
                    isDone = true;
                }
            }
        }
        return instance;
    }

    private <T> boolean isSubscribed(MicroService m, Event<T> e) {
        Queue<MicroService> eventSubscribers = subscribersByType.get(e.getClass());
        for (MicroService service : eventSubscribers) {
            if (service.equals(m))
                return true;
        }
        return false;
    }
}
