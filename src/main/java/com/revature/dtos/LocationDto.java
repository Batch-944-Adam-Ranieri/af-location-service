package com.revature.dtos;

import com.revature.entities.Building;

import java.util.List;

public class LocationDto {

    private int locationId;
    private String city;
    private String state;
    private String zipcode;
    private List<Building> buildings;

    public LocationDto() {
    }

    public LocationDto(int locationId, String city, String state, String zipcode, List<Building> buildings) {
        this.locationId = locationId;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.buildings = buildings;
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

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    @Override
    public String toString() {
        return "LocationDto{" +
                "locationId=" + locationId +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", buildings=" + buildings +
                '}';
    }
}
