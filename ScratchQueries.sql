WITH aggregation as
            (select *,
            time_bucket(1000000,timestamp) as interval,
            where timestamp between 0 and 100000000 group by interval
            order by interval desc)            SELECT interval as timestamp, name, x,y,z from aggregation;

-- Mode x,y,z
WITH aggregation as (
select time_bucket(:interval ,timestamp) as interval,
max(name) as name,
percentile_cont(0.5) within group(order by x) as x,
percentile_cont(0.5) within group(order by y) as y,
percentile_cont(0.5) within group(order by z) as z
from #{#entityName}
where timestamp between :begin and :end
group by interval order by interval desc)
SELECT interval as timestamp, name, x,y,z from aggregation;


-- Mode x,y,z
WITH aggregation as (
select time_bucket(:interval ,timestamp) as interval,
max(name) as name,
mode() within group(order by x) as x,
mode() within group(order by y) as y,
mode() within group(order by z) as z
from #{#entityName}
where timestamp between :begin and :end
group by interval order by interval desc)
SELECT interval as timestamp, name, x,y,z from aggregation;