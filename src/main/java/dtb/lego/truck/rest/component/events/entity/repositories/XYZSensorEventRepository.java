package dtb.lego.truck.rest.component.events.entity.repositories;

import dtb.lego.truck.rest.component.events.entity.events.xyz.sensor.XYZSensorEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

@NoRepositoryBean
public interface XYZSensorEventRepository<T extends XYZSensorEvent> extends EventRepository<T> {

    @Query(value = "SELECT MAX(timestamp) AS timestamp , MAX(name) as name,  AVG(x) AS x, AVG(y) AS y, AVG(z) AS z " +
            "FROM #{#entityName} where timestamp between extract(epoch from now())*1e6 - :interval and extract(epoch from now())*1e6;",
            nativeQuery = true)
    T findEventMeaninLastTimeUnits(@Param("interval") long interval);

    @Query(value = "SELECT MAX(timestamp) AS timestamp , MAX(name) as name,  MAX(x) AS x, MAX(y) AS y, MAX(z) AS z " +
            "FROM #{#entityName} where timestamp between extract(epoch from now())*1e6 - :interval and extract(epoch from now())*1e6;",
            nativeQuery = true)
    T findEventMaxinLastTimeUnits(@Param("interval") long interval);

    @Query(value = "SELECT MAX(timestamp) AS timestamp , MAX(name) as name,  MIN(x) AS x, MIN(y) AS y, MIN(z) AS z " +
            "FROM #{#entityName} where timestamp between extract(epoch from now())*1e6 - :interval and extract(epoch from now())*1e6;",
            nativeQuery = true)
    T findEventMininLastTimeUnits(@Param("interval") long interval);

    /**
     * Creates an auxiliary series to represent the requested interval between events, use a left join then group all the events
     * contained in between the new interval and perform the requested operation to aggregate the events, in this case the mean.
     */
    @Query(value = "with RequestedTimeSeries as (select generate_series(:begin ,:end , :interval ) Serie) " +
            "select RequestedTimeSeries.Serie as timestamp, " +
            "MAX(name) as name,  AVG(x) AS x, AVG(y) AS y, AVG(z) AS z from RequestedTimeSeries " +
            "left join #{#entityName} on #{#entityName}.timestamp >= RequestedTimeSeries.Serie " +
            "and #{#entityName}.timestamp< RequestedTimeSeries.Serie + :interval " +
            "group by RequestedTimeSeries.Serie " +
            "order by RequestedTimeSeries.Serie;",
            nativeQuery = true)
    Collection<T> findAvgEventsInInterval(@Param("begin") long begin,
                                          @Param("end") long end,
                                          @Param("interval") long interval);

    /**
     * Creates an auxiliary series to represent the requested interval between events, use a left join then group all the events
     * contained in between the new interval and perform the requested operation to aggregate the events, in this case it finds
     * the max.
     */
    @Query(value = "with RequestedTimeSeries as (select generate_series(:begin ,:end , :interval ) Serie) " +
            "select RequestedTimeSeries.Serie as timestamp, " +
            "MAX(name) as name,  MAX(x) AS x, max (y) AS y, MAX(z) AS z from RequestedTimeSeries " +
            "left join #{#entityName} on #{#entityName}.timestamp >= RequestedTimeSeries.Serie " +
            "and #{#entityName}.timestamp< RequestedTimeSeries.Serie + :interval " +
            "group by RequestedTimeSeries.Serie " +
            "order by RequestedTimeSeries.Serie;",
            nativeQuery = true)
    Collection<T> findMaxEventsInInterval(@Param("begin") long begin,
                                          @Param("end") long end,
                                          @Param("interval") long interval);

    /**
     * Creates an auxiliary series to represent the requested interval between events, use a left join then group all the events
     * contained in between the new interval and perform the requested operation to aggregate the events, in this case it finds
     * the min.
     */
    @Query(value = "with RequestedTimeSeries as (select generate_series(:begin ,:end , :interval ) Serie) " +
            "select RequestedTimeSeries.Serie as timestamp, " +
            "MAX(name) as name,  MIN(x) AS x, MIN(y) AS y, MIN(z) AS z from RequestedTimeSeries " +
            "left join #{#entityName} on #{#entityName}.timestamp >= RequestedTimeSeries.Serie " +
            "and #{#entityName}.timestamp< RequestedTimeSeries.Serie + :interval " +
            "group by RequestedTimeSeries.Serie " +
            "order by RequestedTimeSeries.Serie;",
            nativeQuery = true)
    Collection<T> findMinEventsInInterval(@Param("begin") long begin,
                                          @Param("end") long end,
                                          @Param("interval") long interval);
    /*
    To use this function the following SQL function has to be defined in the DB
    CREATE FUNCTION _final_median(anyarray) RETURNS float8 AS $$
  WITH q AS
  (
     SELECT val
     FROM unnest($1) val
     WHERE VAL IS NOT NULL
     ORDER BY 1
  ),
  cnt AS
  (
    SELECT COUNT(*) AS c FROM q
  )
  SELECT AVG(val)::float8
  FROM
  (
    SELECT val FROM q
    LIMIT  2 - MOD((SELECT c FROM cnt), 2)
    OFFSET GREATEST(CEIL((SELECT c FROM cnt) / 2.0) - 1,0)
  ) q2;
$$ LANGUAGE SQL IMMUTABLE;

CREATE AGGREGATE median(anyelement) (
  SFUNC=array_append,
  STYPE=anyarray,
  FINALFUNC=_final_median,
  INITCOND='{}'
);
     */
//    @Query(value = "SELECT MAX(timestamp) AS timestamp , MAX(name) as name,  median(x) AS x, median(y) AS y, median(z) AS z " +
//            "FROM #{#entityName} where timestamp between extract(epoch from now())*1e6 - :interval and extract(epoch from now())*1e6;",
//            nativeQuery = true)
//    T findEventMedianinLastInterval(@Param("interval") long interval);
}
