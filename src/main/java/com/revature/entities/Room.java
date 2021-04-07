package com.revature.entities;

import com.revature.dtos.RoomDto;

import javax.persistence.*;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int roomId;

    @Column(name = "room_name")
    private String name;

    @Column(name = "room_type")
    @Enumerated(value = EnumType.STRING)
    private RoomType type;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "bld_id")
    private int buildingId;

    public Room() {
    }

    public Room(int roomId, String name, RoomType type, int capacity, int buildingId) {
        this.roomId = roomId;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.buildingId = buildingId;
    }

    public Room(RoomDto roomDto) {
        this.roomId = roomDto.getRoomId();
        this.name = roomDto.getName();
        this.type = RoomType.valueOf(roomDto.getType());
        this.capacity = roomDto.getCapacity();
        this.buildingId = roomDto.getBuildingId();
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

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
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



    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", capacity=" + capacity +
                ", buildingId=" + buildingId +
                '}';
    }
}
