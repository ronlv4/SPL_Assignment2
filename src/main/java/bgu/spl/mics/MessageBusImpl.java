package bgu.spl.mics;

import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
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
    private Map<MicroService, Queue<Event<?>>> microServices;
    private Map<Class<? extends Event<?>>, Deque<MicroService>> subscribersByType;
    private Map<MicroService, Deque<Class<? extends Event<?>>>> subscribersByMicroService;
    private Map<MicroService, Queue<Message>> microServicesBroadcasts;
    private Map<Class<? extends Broadcast>, Deque<MicroService>> subscribersByTypeBroadcasts;
    private Map<MicroService, Deque<Class<? extends Broadcast>>> subscribersByMicroServiceBroadcasts;
    private HashMap<Event<?> , Future<?>> eventToFuture;

    private MessageBusImpl() {
        microServices = new HashMap<>();
        subscribersByType = new HashMap<>();
        subscribersByMicroService = new HashMap<>();
        microServicesBroadcasts = new HashMap<>();
        subscribersByTypeBroadcasts = new HashMap<>();
        subscribersByMicroServiceBroadcasts = new HashMap<>();
        eventToFuture = new HashMap<>();
        instance = this;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        // adding the microservice to the subscriberByType queue
        Deque<MicroService> subscribedMicroServiceDeque;
        synchronized (this){
            subscribedMicroServiceDeque = subscribersByType.get(type);
            if (subscribedMicroServiceDeque == null){
                subscribedMicroServiceDeque = new ConcurrentLinkedDeque<>();
                subscribersByType.put(type, subscribedMicroServiceDeque);
            }
        }
        subscribedMicroServiceDeque.addFirst(m);
//        AtomicBoolean lock = new AtomicBoolean(true);
//        while (!lock.compareAndSet(false,true)){
//            lock.set(false);
//        }

        // adding the microservice to the subscriberByMicroService queue
        Deque<Class<? extends Event<?>>> typesSubscriptions = subscribersByMicroService.get(m);
        synchronized (this){
            if (typesSubscriptions == null){
                typesSubscriptions = new ConcurrentLinkedDeque<>();
                subscribersByMicroService.put(m, typesSubscriptions);
            }
        }
        typesSubscriptions.addFirst(type);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        Deque<MicroService> subscribedMicroServiceDeque = subscribersByTypeBroadcasts.get(type);
        // adding the microsevice to the subscriberByType queue
        if (subscribedMicroServiceDeque == null){ // TODO: Needs to bo done in a do_while thread safe design
            subscribedMicroServiceDeque = new ConcurrentLinkedDeque<>();
            subscribersByTypeBroadcasts.put(type, subscribedMicroServiceDeque);
        }
        subscribedMicroServiceDeque.addFirst(m);

        // adding the microservice to the subscriberByMicroService queue
        Deque<Class<? extends Broadcast>> typesSubscriptions = subscribersByMicroServiceBroadcasts.get(m);
        if (typesSubscriptions == null){
            typesSubscriptions = new ConcurrentLinkedDeque<>();
            subscribersByMicroServiceBroadcasts.put(m, typesSubscriptions);
        }
        typesSubscriptions.add(type);
        // TODO Auto-generated method stub
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        // TODO Auto-generated method stub
        if (result != null) {
            Future<T> future = (Future<T>) eventToFuture.get(e);
            future.resolve(result);
        }
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        Queue<MicroService> subscribedMicroServiceQueue = subscribersByType.get(b.getClass());
        for(int i=0; i<subscribedMicroServiceQueue.size(); i++) {
            MicroService microServiceInLine = subscribedMicroServiceQueue.remove();
            microServicesBroadcasts.get(microServiceInLine).add(b);
            subscribedMicroServiceQueue.add(microServiceInLine);
        }
        // TODO Auto-generated method stub
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        Queue<MicroService> subscribedMicroServiceQueue = subscribersByType.get(e.getClass());
        if (subscribedMicroServiceQueue == null || subscribedMicroServiceQueue.isEmpty()) //TODO make sure that if an event has no subscribers it's being deleted from the map
            return null; //TODO what should we do if the type of event has no subscribers?
        synchronized (this){
            MicroService microServiceInLine = subscribedMicroServiceQueue.remove();
            microServices.get(microServiceInLine).add(e);
            notifyAll();
            subscribedMicroServiceQueue.add(microServiceInLine);
        }
        Future<T> newFuture = new Future<>();
        eventToFuture.put(e ,newFuture);
        return newFuture;
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
        while (microServiceQueue.isEmpty())
            wait();
        return microServiceQueue.poll();
    }

    public static MessageBusImpl getInstance() {
        if(!isDone) {
            synchronized(MessageBusImpl.class)
            {
                if(!isDone) {
                    instance = new MessageBusImpl();
                    isDone = true;
                }
            }
        }
        return instance;
    }

}
