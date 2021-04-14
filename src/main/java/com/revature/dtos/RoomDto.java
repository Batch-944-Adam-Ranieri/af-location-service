package com.revature.dtos;

import com.revature.entities.Room;

/**
 * Data transfer object that is not persisted and is only meant to take the input from the request body. Has all the
 * same fields and methods as the entity version
 */
public class RoomDto {

    private int roomId;
    private String name;
    private String type;
    private int capacity;
    private int buildingId;

    public RoomDto() {
    }

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
