-- Create a keyspace
CREATE KEYSPACE IF NOT EXISTS lulz_ltd_keyspace
        WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

-- Use the keyspace
USE lulz_ltd_keyspace;

-- Create a table
CREATE TABLE IF NOT EXISTS devices
(
    id      UUID PRIMARY KEY,
    device  UUID,
    data    TEXT,
    version INT
);

create index index_device
    on devices (device);
