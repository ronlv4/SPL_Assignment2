package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

    private final static int SERVICES_DEFAULT_INITIAL_CAPACITY = 21;

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
    private Map<MicroService, PriorityBlockingQueue<Message>> microServicesMessages;
    private Map<Class<? extends Event<?>>, Deque<MicroService>> eventSubscribersByType;
    private Map<Class<? extends Broadcast>, Deque<MicroService>> broadcastSubscribersByType;
    private Map<MicroService, Deque<Class<? extends Event<?>>>> eventSubscribersByMicroService;
    private Map<MicroService, Deque<Class<? extends Broadcast>>> broadcastSubscribersByMicroService;
    private HashMap<Event<?>, Future<?>> eventToFuture;

    private MessageBusImpl() {
        microServicesMessages = new HashMap<>();
        broadcastSubscribersByType = new HashMap<>();
        eventSubscribersByType = new HashMap<>();
        broadcastSubscribersByMicroService = new HashMap<>();
        eventSubscribersByMicroService = new HashMap<>();
        eventToFuture = new HashMap<>();
        instance = this;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        // adding the microservice to the subscriberByType queue
        Deque<MicroService> subscribedMicroServiceDeque;
        synchronized (this) {
            subscribedMicroServiceDeque = eventSubscribersByType.get(type);
            if (subscribedMicroServiceDeque == null) {
                subscribedMicroServiceDeque = new ConcurrentLinkedDeque<>();
                eventSubscribersByType.put(type, subscribedMicroServiceDeque);
            }
        }
        subscribedMicroServiceDeque.addFirst(m);

        // adding the microservice to the subscriberByMicroService queue
        Deque<Class<? extends Event<?>>> typesSubscriptions = eventSubscribersByMicroService.get(m);
        synchronized (this) {
            if (typesSubscriptions == null) {
                typesSubscriptions = new ConcurrentLinkedDeque<>();
                eventSubscribersByMicroService.put(m, typesSubscriptions);
            }
        }
        typesSubscriptions.addFirst(type);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        Deque<MicroService> subscribedMicroServiceList;
        synchronized (this) {
            subscribedMicroServiceList = broadcastSubscribersByType.get(type);
            if (subscribedMicroServiceList == null) {
                subscribedMicroServiceList = new ConcurrentLinkedDeque<>();
                broadcastSubscribersByType.put(type, subscribedMicroServiceList);
            }
        }
        subscribedMicroServiceList.addFirst(m);

        Deque<Class<? extends Broadcast>> typesSubscriptions = broadcastSubscribersByMicroService.get(m);
        synchronized (this) {
            if (typesSubscriptions == null) {
                typesSubscriptions = new ConcurrentLinkedDeque<>();
                broadcastSubscribersByMicroService.put(m, typesSubscriptions);
            }
        }
        typesSubscriptions.add(type);
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        if (result != null) {
            Future<T> future = (Future<T>) eventToFuture.get(e);
            future.resolve(result);
        }
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        Queue<MicroService> subscribedMicroServiceQueue = broadcastSubscribersByType.get(b.getClass());
        for (MicroService subscriber : subscribedMicroServiceQueue) {
            microServicesMessages.get(subscriber).add(b);
        }
//        notifyAll();
    }

    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        Queue<MicroService> subscribedMicroServiceQueue = eventSubscribersByType.get(e.getClass());
        if (subscribedMicroServiceQueue == null || subscribedMicroServiceQueue.isEmpty()) //TODO make sure that if an event has no subscribers it's being deleted from the map
            return null; //TODO what should we do if the type of event has no subscribers?
        synchronized (this) {
            MicroService microServiceInLine = subscribedMicroServiceQueue.remove();
            microServicesMessages.get(microServiceInLine).add(e);
//            notifyAll();
            subscribedMicroServiceQueue.add(microServiceInLine);
        }
        Future<T> newFuture = new Future<>();
        eventToFuture.put(e, newFuture);
        return newFuture;
    }

    @Override
    public void register(MicroService m) {
        microServicesMessages.put(m, new PriorityBlockingQueue<>(SERVICES_DEFAULT_INITIAL_CAPACITY, (m1,m2)->{
            // returns negative if m1 < m2
            // Broadcast > event
            if (Broadcast.class.isAssignableFrom(m1.getClass()) && Event.class.isAssignableFrom(m2.getClass()))
                return 1;
            else if (Broadcast.class.isAssignableFrom(m2.getClass()) && Event.class.isAssignableFrom(m1.getClass()))
                return -1;
            else
                return 0;
        }));
    }

    @Override
    public void unregister(MicroService m) {
        // TODO rewrite this function
//        for (Class<Message> type : eventSubscribersByMicroService.get(m)) {
//            broadcastSubscribersByType.get(type).remove(m);
//        }
        eventSubscribersByMicroService.remove(m);
        microServicesMessages.remove(m); // does not throw exception if key does not exist in map
    }

    @Override
    public synchronized Message awaitMessage(MicroService m) throws InterruptedException {
        if (!isRegistered(m))
            throw new IllegalStateException("Microservice " + m.getName() + " Was never registered");
        PriorityBlockingQueue<Message> awaitingMessagesQueue = microServicesMessages.get(m);
        return awaitingMessagesQueue.take();
    }

    public static MessageBusImpl getInstance() {
        if (!isDone) {
            synchronized (MessageBusImpl.class) {
                if (!isDone) {
                    instance = new MessageBusImpl();
                    isDone = true;
                }
            }
        }
        return instance;
    }

    private boolean isRegistered(MicroService m){
        return microServicesMessages.containsKey(m);
    }

}
