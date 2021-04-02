package com.revature.dtos;

import com.revature.entities.Room;
import com.revature.entities.RoomType;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class RoomDto {

    private int roomId;
    private String name;
    private String type;
    private int capacity;
    private int buildingId;

    public RoomDto() {
    }

//    public RoomDto(int roomId, String name, String type, int capacity, int buildingId) {
//        this.roomId = roomId;
//        this.name = name;
//        this.type = type;
//        this.capacity = capacity;
//        this.buildingId = buildingId;
//    }

    public RoomDto(Room room) {
        this.roomId = room.getRoomId();
        this.name = room.getName();
        this.type = room.getType().toString();
        this.capacity = room.getCapacity();
        this.buildingId = room.getBuildingId();
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }
}
