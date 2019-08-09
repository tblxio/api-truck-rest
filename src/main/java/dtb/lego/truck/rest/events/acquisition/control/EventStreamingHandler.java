package dtb.lego.truck.rest.events.acquisition.control;

import dtb.lego.truck.rest.component.entity.events.Event;
import dtb.lego.truck.rest.events.acquisition.entity.LegoTruckException;
import dtb.lego.truck.rest.errors.Errors;
import dtb.lego.truck.rest.motor.control.control.MotorDriveHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This class performs all the operations regarding the streaming of events to the websocket requested by the client.
 */
@Component
public class EventStreamingHandler {

    private final MessageSendingOperations<String> messagingTemplate;
    private MotorDriveHandler motorDriveHandler;
    /**
     * Used to save the information on the subscribed clients in order to stop streaming when they disconnect from the
     * requested stream, since every requested stream is given a unique destination and session ID. It is concurrent
     * since it needs to be checked by the EventListeners and the streamers which run in separate threads.
     */
    private ConcurrentMap<String, String> subscriptions;
    private EventDataHandler eventDataHandler;

    @Autowired
    public EventStreamingHandler(MessageSendingOperations<String> messagingTemplate, MotorDriveHandler motorDriveHandler, EventDataHandler eventDataHandler) {
        this.messagingTemplate = messagingTemplate;
        this.motorDriveHandler = motorDriveHandler;
        this.subscriptions = new ConcurrentHashMap<>();
        this.eventDataHandler = eventDataHandler;
    }



    /**
     * Event listener that runs whenever a new client subscribes to a topic. It extracts the information regarding the stream
     * in order to call {@link #streamer(String, String, int, String, String) streamer} function in the requested way, as
     * well as add the session ID and destination to the current subscriptions map
     *
     * @param event Contains the information regarding the subscription
     */
    @Async
    @EventListener
    //TODO check the exception handling here
    public void onSubscribeApplicationEvent(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String destination = headers.getDestination();
        String[] subtopics = destination.split("/");
        Integer interval = Integer.parseInt(subtopics[2]);
        String sessionID = headers.getSessionId();
        String fields = subtopics[3];
        String transformation = subtopics[4];
        subscriptions.putIfAbsent(sessionID, destination);
        streamer(headers.getSessionId(), destination, interval, fields, transformation);
    }

    /**
     * Handles the streaming of data to the client. It loops until the client unsubscribe and its sessionId is removed from
     * the streaming list.
     *
     * @param sessionId      Client sessionID
     * @param destination    The topic to be published
     * @param interval       The interval in which the data is sent
     * @param fields         The components from which the data is requested, separated by -
     * @param transformation The transformation to be applied to downsample the data.
     */
    @Async("AsyncWebsocketExecutor")
    public void streamer(String sessionId, String destination, int interval, String fields, String transformation) {
        // Get different components from request
        String[] components = fields.split("-");
        while (true) {
            // If the client disconnects his sessionId is removed, so the loop should stop
            if (!this.subscriptions.containsKey(sessionId)) break;
            // Send the last data from each component
            try {
                for (String component : components) {
                    Event ne = eventDataHandler.getTransformedEvent(interval, component, transformation);
                    if (ne == null) ne = new Event(System.currentTimeMillis());
                    messagingTemplate.convertAndSend(destination, ne);
                }
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                throw new LegoTruckException(Errors.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Event listener that runs whenever a client disconnects, therefore disconnecting from all the subscribed topics.
     * It removes the client subscriptions from the list so the streamer can stop looping.
     *
     * @param event Contains the information regarding the subscription
     */
    @Async
    @EventListener
    public void onDisconnectApplicationEvent(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        this.subscriptions.remove(headers.getSessionId());
        System.out.println(headers.getSessionId() + " has disconnected");
    }

    /**
     * Event listener that runs whenever a client unsubscribe from a topic.
     * It removes the client subscriptions from the list so the streamer can stop looping.
     *
     * @param event Contains the information regarding the subscription
     */
    @Async
    @EventListener
    public void onUnsubscribeApplicationEvent(SessionUnsubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        this.subscriptions.remove(headers.getSessionId());
        System.out.println(headers.getSessionId() + " has unsubscribed");
    }

    /**
     * Generates an unique websocket streaming topic with the requested interval, components and transformation to be applied,
     * for the client to connect to.
     *
     * @param interval       The between messages from the streamer
     * @param fields         The requested components separated by '-'
     * @param transformation The transformation to be applied to downsample the data to match the requested interval
     */
    public String getStream(int interval, String fields, String transformation) {
        return "/events/" + interval + "/" + fields + "/" + transformation + "/" + new Random().nextInt(15000);
    }


}