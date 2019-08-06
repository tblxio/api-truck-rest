
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
