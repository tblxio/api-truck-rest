package io.techhublisbon.lego.truck.rest.component.entity.events;

import io.techhublisbon.lego.truck.rest.component.entity.Component;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Event interface used to outline all the functions to be
 * implemented by the different kinds of events
 */
@NoRepositoryBean
@MappedSuperclass
public class Event {
    @Id
    Long timestamp;
    Component name;

    public Event(long timestamp) {
        this.timestamp = timestamp;
    }

    public Event() {
    }

    public long getTimestamp() {
        return timestamp;
    }
}
