package io.techhublisbon.lego.truck.rest.components.control;

import io.techhublisbon.lego.truck.rest.events.acquisition.entity.MotorControllerEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;

/**
 * CrudRepository extension for motor controller Events.
 * Used in order to store the motor controller events on the database
 */
@Transactional
public interface MotorControllerEventRepository extends EventRepository<MotorControllerEvent> {

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as" + "(select time_bucket(:interval ,timestamp) as interval, MAX(name) AS name,last(bat_voltage,timestamp) AS bat_voltage,last(temperature,timestamp) AS temperature" + " from #{#entityName} " + "where timestamp between :begin and :end group by interval " + "order by interval desc)" + "SELECT interval as timestamp, name, x,y,z from aggregation;", nativeQuery = true)
    Collection<MotorControllerEvent> findLastEventsInInterval(@Param("begin") long begin, @Param("end") long end, @Param("interval") long interval);

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object. It also fills the gaps with null values when there are no measurements.
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as" + "(select time_bucket_gapfill(:interval ,timestamp, :begin, :end) as interval, MAX(name) AS name," + "last(bat_voltage,timestamp) AS bat_voltage,last(temperature,timestamp) AS temperature " + "from #{#entityName} " + "where timestamp between :begin and :end group by interval " + "order by interval desc)" + "SELECT interval as timestamp, name, x,y,z from aggregation;", nativeQuery = true)
    Collection<MotorControllerEvent> findLastEventsInIntervalFillingGaps(@Param("begin") long begin, @Param("end") long end, @Param("interval") long interval);
}
