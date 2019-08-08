package dtb.lego.truck.rest.component.events.entity.repositories;

import dtb.lego.truck.rest.component.events.entity.events.xyz.sensor.XYZSensorEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

@NoRepositoryBean
public interface XYZSensorEventRepository<T extends XYZSensorEvent> extends EventRepository<T> {

    @Query(value = "SELECT MAX(timestamp) AS timestamp , MAX(name) as name,  AVG(x) AS x, AVG(y) AS y, AVG(z) AS z " +
            "FROM #{#entityName} where timestamp between extract(epoch from now())*1e3 - :interval and extract(epoch from now())*1e3;",
            nativeQuery = true)
    T findEventMeaninLastTimeUnits(@Param("interval") long interval);

    @Query(value = "SELECT MAX(timestamp) AS timestamp , MAX(name) as name,  MAX(x) AS x, MAX(y) AS y, MAX(z) AS z " +
            "FROM #{#entityName} where timestamp between extract(epoch from now())*1e3 - :interval and extract(epoch from now())*1e3;",
            nativeQuery = true)
    T findEventMaxinLastTimeUnits(@Param("interval") long interval);

    @Query(value = "SELECT MAX(timestamp) AS timestamp , MAX(name) as name,  MIN(x) AS x, MIN(y) AS y, MIN(z) AS z " +
            "FROM #{#entityName} where timestamp between extract(epoch from now())*1e3 - :interval and extract(epoch from now())*1e3;",
            nativeQuery = true)
    T findEventMininLastTimeUnits(@Param("interval") long interval);

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as" +
            "(select time_bucket(:interval ,timestamp) as interval, MAX(name) AS name," +
            "AVG(x) AS x, AVG(y) AS y, AVG(z) AS z " +
            "from #{#entityName} " +
            "where timestamp between :begin and :end group by interval " +
            "order by interval desc)" +
            "SELECT interval as timestamp, name, x,y,z from aggregation;",
            nativeQuery = true)
    Collection<T> findAvgEventsInInterval(@Param("begin") long begin,
                                          @Param("end") long end,
                                          @Param("interval") long interval);

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as" +
            "(select time_bucket(:interval ,timestamp) as interval, MAX(name) AS name,max(x) AS x, max(y) AS y, max(z) AS z " +
            "from #{#entityName} " +
            "where timestamp between :begin and :end group by interval " +
            "order by interval desc)" +
            "SELECT interval as timestamp, name, x,y,z from aggregation;",
            nativeQuery = true)
    Collection<T> findMaxEventsInInterval(@Param("begin") long begin,
                                          @Param("end") long end,
                                          @Param("interval") long interval);

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as" +
            "(select time_bucket(:interval ,timestamp) as interval, MAX(name) AS name,MIN(x) AS x, MIN(y) AS y, MIN(z) AS z " +
            "from #{#entityName} " +
            "where timestamp between :begin and :end group by interval " +
            "order by interval desc)" +
            "SELECT interval as timestamp, name, x,y,z from aggregation;",
            nativeQuery = true)
    Collection<T> findMinEventsInInterval(@Param("begin") long begin,
                                          @Param("end") long end,
                                          @Param("interval") long interval);

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as" +
            "(select time_bucket(:interval ,timestamp) as interval, MAX(name) AS name,last(x,timestamp) AS x,last(y,timestamp) AS y" +
            ", last(z,timestamp) AS z " +
            "from #{#entityName} " +
            "where timestamp between :begin and :end group by interval " +
            "order by interval desc)" +
            "SELECT interval as timestamp, name, x,y,z from aggregation;",
            nativeQuery = true)
    Collection<T> findLastEventsInInterval(@Param("begin") long begin,
                                           @Param("end") long end,
                                           @Param("interval") long interval);

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object. It also fills the gaps with null values when there are no measurements.
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as" +
            "(select time_bucket_gapfill(:interval ,timestamp, :begin, :end) as interval, MAX(name) AS name," +
            "AVG(x) AS x, AVG(y) AS y, AVG(z) AS z " +
            "from #{#entityName} " +
            "where timestamp between :begin and :end group by interval " +
            "order by interval desc)" +
            "SELECT interval as timestamp, name, x,y,z from aggregation;",
            nativeQuery = true)
    Collection<T> findAvgEventsInIntervalFillingGaps(@Param("begin") long begin,
                                                     @Param("end") long end,
                                                     @Param("interval") long interval);

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object. It also fills the gaps with null values when there are no measurements.
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as" +
            "(select time_bucket_gapfill(:interval ,timestamp, :begin, :end) as interval, MAX(name) AS name,max(x) AS x, max(y) AS y, max(z) AS z " +
            "from #{#entityName} " +
            "where timestamp between :begin and :end group by interval " +
            "order by interval desc)" +
            "SELECT interval as timestamp, name, x,y,z from aggregation;",
            nativeQuery = true)
    Collection<T> findMaxEventsInIntervalFillingGaps(@Param("begin") long begin,
                                                     @Param("end") long end,
                                                     @Param("interval") long interval);

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object. It also fills the gaps with null values when there are no measurements.
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as" +
            "(select time_bucket_gapfill(:interval ,timestamp, :begin, :end) as interval, MAX(name) AS name,MIN(x) AS x, MIN(y) AS y, MIN(z) AS z " +
            "from #{#entityName} " +
            "where timestamp between :begin and :end group by interval " +
            "order by interval desc)" +
            "SELECT interval as timestamp, name, x,y,z from aggregation;",
            nativeQuery = true)
    Collection<T> findMinEventsInIntervalFillingGaps(@Param("begin") long begin,
                                                     @Param("end") long end,
                                                     @Param("interval") long interval);

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object. It also fills the gaps with null values when there are no measurements.
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as" +
            "(select time_bucket_gapfill(:interval ,timestamp, :begin, :end) as interval, MAX(name) AS name,last(x,timestamp) AS x,last(y,timestamp) AS y" +
            ", last(z,timestamp) AS z " +
            "from #{#entityName} " +
            "where timestamp between :begin and :end group by interval " +
            "order by interval desc)" +
            "SELECT interval as timestamp, name, x,y,z from aggregation;",
            nativeQuery = true)
    Collection<T> findLastEventsInIntervalFillingGaps(@Param("begin") long begin,
                                                      @Param("end") long end,
                                                      @Param("interval") long interval);

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object.
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as (" +
            "select time_bucket(:interval ,timestamp) as interval," +
            "max(name) as name," +
            "percentile_cont(0.5) within group(order by x) as x," +
            "percentile_cont(0.5) within group(order by y) as y," +
            "percentile_cont(0.5) within group(order by z) as z " +
            "from #{#entityName} " +
            "where timestamp between :begin and :end " +
            "group by interval order by interval desc)" +
            "SELECT interval as timestamp, name, x,y,z from aggregation;",
            nativeQuery = true)
    Collection<T> findMedianEventsInInterval(@Param("begin") long begin,
                                             @Param("end") long end,
                                             @Param("interval") long interval);

    /**
     * Uses the timescaleDB time_bucket function to create the intervals and applies the the transforms over them.
     * The use of the WITH clause is just because the final name of the timestamp column needing to be Timestamp
     * for the JPA to properly package the object.
     * <p>
     * For more information on the time_bucket: https://docs.timescale.com/latest/api#time_bucket
     */
    @Query(value = "WITH aggregation as (" +
            "select time_bucket_gapfill(:interval ,timestamp, :begin, :end) as interval," +
            "max(name) as name," +
            "percentile_cont(0.5) within group(order by x) as x," +
            "percentile_cont(0.5) within group(order by y) as y," +
            "percentile_cont(0.5) within group(order by z) as z " +
            "from #{#entityName} " +
            "where timestamp between :begin and :end " +
            "group by interval order by interval desc)" +
            "SELECT interval as timestamp, name, x,y,z from aggregation;",
            nativeQuery = true)
    Collection<T> findMedianEventsInIntervalFillingGaps(@Param("begin") long begin,
                                                        @Param("end") long end,
                                                        @Param("interval") long interval);


}
