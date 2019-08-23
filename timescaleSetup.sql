
CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;

CREATE TABLE motor(
    timestamp bigint PRIMARY KEY NOT NULL,
    name integer,
    bat_voltage DOUBLE PRECISION NOT NULL,
    temperature DOUBLE PRECISION NOT NULL
);

CREATE TABLE proximity(
    timestamp bigint PRIMARY KEY NOT NULL,
    name integer,
    distance DOUBLE PRECISION NOT NULL
);

CREATE TABLE gyroscope(
    timestamp bigint PRIMARY KEY NOT NULL,
    name integer,
    x DOUBLE PRECISION ,
    y DOUBLE PRECISION ,
    z DOUBLE PRECISION
);

CREATE TABLE accelerometer(
    timestamp bigint PRIMARY KEY NOT NULL,
    name integer,
    x DOUBLE PRECISION ,
    y DOUBLE PRECISION ,
    z DOUBLE PRECISION
);
-- Set chunk time interval to 1 hour
SELECT create_hypertable('accelerometer', 'timestamp', chunk_time_interval => 3600000);
SELECT create_hypertable('gyroscope', 'timestamp', chunk_time_interval => 3600000);
SELECT create_hypertable('motor', 'timestamp', chunk_time_interval => 3600000);
SELECT create_hypertable('proximity', 'timestamp', chunk_time_interval => 3600000);



