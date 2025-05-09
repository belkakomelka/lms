CREATE SCHEMA IF NOT EXISTS lms;
ALTER ROLE postgres SET search_path TO lms, public;
GRANT ALL PRIVILEGES ON SCHEMA lms TO postgres;