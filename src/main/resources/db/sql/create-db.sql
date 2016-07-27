DROP TABLE IF EXISTS country_city;

CREATE TABLE country_city (
    country VARCHAR(200),
    city VARCHAR(200),
    PRIMARY KEY (country, city)
);

CREATE INDEX country_idx ON country_city (country);