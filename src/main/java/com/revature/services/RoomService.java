package com.revature.services;

import com.revature.entities.Room;
import com.revature.entities.RoomType;
import com.revature.exceptions.RoomNotFoundException;

import java.util.List;

/**
 * Service interface for Crud operations on Room objects
 */
public interface RoomService {

    Room createRoom(Room room);

    List<Room> getAllRooms();
    Room getRoomById(int roomId) throws RoomNotFoundException;
    List<Room> getRoomsByBuildingId(int buildingId);
    List<Room> getRoomsByType(int buildingId, RoomType roomType);

    Room updateRoom(Room room) throws RoomNotFoundException;

    boolean deleteRoom(int roomId);

}
