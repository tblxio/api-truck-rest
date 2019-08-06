
delete from accelerometer where timestamp>0;
delete from gyroscope where timestamp>0;
delete from motor where timestamp>0;

-- Set chunck time interval to 1 hour
SELECT create_hypertable('accelerometer', 'timestamp', chunk_time_interval => 3600000);
SELECT create_hypertable('gyroscope', 'timestamp', chunk_time_interval => 3600000);
SELECT create_hypertable('motor', 'timestamp', chunk_time_interval => 3600000);

select set_chunk_time_interval('accelerometer', 3600000);
select set_chunk_time_interval('gyroscope', 3600000);
select set_chunk_time_interval('motor', 3600000);

elect time_bucket(300,timestamp) as five_seconds, avg(x) from gyroscope
where timestamp between 1565105247177 and 1565105247478 group by five_seconds
order by five_seconds desc;

explain analyze with RequestedTimeSeries as (select generate_series(1565005247478 ,1565105247478, 300) Serie)
            select RequestedTimeSeries.Serie as timestamp,
            MAX(name) as name,  AVG(x) AS x, AVG(y) AS y, AVG(z) AS z from RequestedTimeSeries
            left join gyroscope on gyroscope.timestamp >= RequestedTimeSeries.Serie
            and gyroscope.timestamp< RequestedTimeSeries.Serie + 300 group by RequestedTimeSeries.Serie order by RequestedTimeSeries.Serie;


explain analyze select time_bucket(300,timestamp) as timestamp, AVG(x) AS x, AVG(y) AS y, AVG(z) AS z from gyroscope
where timestamp between 0 and 1565105247478 group by timestamp
order by timestamp desc;


with aux as(select time_bucket(300,timestamp) as interval, AVG(x) AS x, AVG(y) AS y, AVG(z) AS z
 from gyroscope g
where timestamp between 1561005247478 and 1565105347478 group by interval
order by interval desc)
select interval as timestamp, x,y,z from aux;


select * from gyroscope where timestamp between 1561005247478 and 1565105347478 order by timestamp desc limit 5;


"WITH aggregation as" +
            "(select time_bucket(:interval ,timestamp) as interval, AVG(x) AS x, AVG(y) AS y, AVG(z) AS z from #{#entityName} " +
            "where timestamp between :begin and :end group by interval " +
            "order by timestamp desc)" +
            "SELECT interval as timestamp, x,y,z from aggregation",