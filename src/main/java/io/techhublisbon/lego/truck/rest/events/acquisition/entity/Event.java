package io.techhublisbon.lego.truck.rest.events.acquisition.entity;

import io.techhublisbon.lego.truck.rest.components.entity.Component;
import lombok.Getter;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Event interface used to outline all the functions to be
 * implemented by the different kinds of events
 */
@NoRepositoryBean
@MappedSuperclass
@Getter
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
