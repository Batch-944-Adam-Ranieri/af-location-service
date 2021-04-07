package com.revature.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "building")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="building_id")
    private int buildingId;

    @Column(name = "address")
    private String address;

    @Column(name = "loc_id")
    private int locationId;

    @JsonIgnore
    @OneToMany(mappedBy = "buildingId")
    List<Room> rooms;

    public Building() {
    }

    public Building(int buildingId, String address, int locationId) {
        this.buildingId = buildingId;
        this.address = address;
        this.locationId = locationId;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "Building{" +
                "buildingId=" + buildingId +
                ", address='" + address + '\'' +
                ", locationId=" + locationId +
                ", rooms=" + rooms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return getBuildingId() == building.getBuildingId() && getLocationId() == building.getLocationId() && Objects.equals(getAddress(), building.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBuildingId(), getAddress(), getLocationId());
    }
}
