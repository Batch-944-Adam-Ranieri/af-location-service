package com.revature.dtos;

import com.revature.entities.Building;
import com.revature.entities.Location;

import java.util.List;

/**
 * Data transfer object that is not persisted and is only meant to take the input from the request body. Has all the
 * same fields and methods as the entity version
 */
public class LocationDto {

    private int locationId;
    private String city;
    private String state;
    private String zipcode;
    private String name;
    private List<Building> buildings;

    public LocationDto() {
    }

    public LocationDto(Location location){
        this.locationId = location.getLocationId();
        this.city = location.getCity();
        this.state = location.getState();
        this.zipcode = location.getZipcode();
        this.buildings = location.getBuildings();
        this.name = location.getName();
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
