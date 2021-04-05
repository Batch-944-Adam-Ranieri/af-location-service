DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS building;
DROP TABLE IF EXISTS location;

CREATE TABLE location(
    location_id INTEGER NOT NULL AUTO_INCREMENT,
    city VARCHAR(200) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zipcode VARCHAR(20) NOT NULL,
    PRIMARY KEY (location_id)
);
CREATE TABLE building (
    building_id INTEGER NOT NULL AUTO_INCREMENT,
    address VARCHAR(200) NOT NULL,
    loc_id INTEGER NOT NULL,
    PRIMARY KEY (building_id),
    CONSTRAINT loc_bld FOREIGN KEY (loc_id) REFERENCES location(location_id)
    ON DELETE CASCADE
);
CREATE TABLE room (
    room_id INTEGER AUTO_INCREMENT,
    capacity INTEGER NOT NULL,
    room_name VARCHAR(200) NOT NULL,
    type VARCHAR(20) NOT NULL,
    bld_id INTEGER NOT NULL,
    PRIMARY KEY (room_id),
    CONSTRAINT bld_rm FOREIGN KEY (bld_id) REFERENCES building(building_id)
    ON DELETE CASCADE
);
