package com.revature.dtos;

import com.revature.entities.Room;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

public class BuildingDto {

    private int buildingId;
    private String address;
    private int locationId;
    List<Room> rooms;

    public BuildingDto() {
    }

    public int getBuildingId() {
        return buildingId;
    }

    public String getAddress() {
        return address;
    }

    public int getLocationId() {
        return locationId;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
