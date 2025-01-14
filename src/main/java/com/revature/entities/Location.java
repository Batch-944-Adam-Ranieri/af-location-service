package com.revature.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revature.dtos.LocationDto;

import javax.persistence.*;
import java.util.List;

/**
 * Persisted bean for the location. Bean is managed by Spring. Mapped to table location_ in database
 */
@Entity
@Table(name = "location_")
public class Location {

    /**
     * Unique location id. Autogenerated and auto-incremented in the database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;

    /**
     * Name of the city
     */
    @Column(name = "city", nullable = false)
    private String city;

    /**
     * Name of the state
     */
    @Column(name = "state", nullable = false)
    private String state;

    /**
     * Zip code of the location
     */
    @Column(name = "zipcode", nullable = false)
    private String zipcode;

    /**
     * Name of the location
     */
    @Column(name = "\"name\"")
    private String name;

    /**
     * List of all the buildings in a location
     */
    @JsonIgnore
    @OneToMany(mappedBy = "locationId")
    private List<Building> buildings;

    public Location(int locationId, String city, String state, String zipcode) {
        this.locationId = locationId;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public Location() {
    }

    public Location(LocationDto locationDto) {
        this.locationId = locationDto.getLocationId();
        this.city = locationDto.getCity();
        this.state = locationDto.getState();
        this.zipcode = locationDto.getZipcode();
        this.buildings = locationDto.getBuildings();
        this.name = locationDto.getName();
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

}
