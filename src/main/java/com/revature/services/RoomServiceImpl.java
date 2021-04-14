package com.revature.services;


import com.revature.entities.Room;
import com.revature.entities.RoomType;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.exceptions.RoomNotFoundException;
import com.revature.repos.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the interface RoomService
 */
@Component
@Service
public class RoomServiceImpl implements RoomService {

    /**
     * Room Repo injection
     */
    @Autowired
    RoomRepo roomRepo;

    /**
     * Uses the room repo to create a new record in the Room table
     * of the AssignForce database by setting the ID of the new Room
     * record to zero.
     *
     * @param room          Transient Room object
     * @return              Detached Room object that was persisted
     */
    @Override
    public Room createRoom(Room room) {
        room.setRoomId(0);
        this.roomRepo.save(room);
        return room;
    }

    /**
     * Uses the room repo to get all rooms persisted in the
     * Room table of the AssignForce database.
     *
     * @return              List of Room objects
     */
    @Override
    public List<Room> getAllRooms() {

        return (List<Room>) this.roomRepo.findAll();
    }

    /**
     * Uses the location repo to find a record in the Location table of
     * the AssignForce database by location ID.
     *
     * @param roomId        ID of the Room to retrieve from the database
     * @return              Room object
     * @throws RoomNotFoundException thrown if there are no room
     * records that with a matching roomId field
     */
    @Override
    public Room getRoomById(int roomId) throws RoomNotFoundException {
        Room room;
        Optional<Room> op = roomRepo.findById(roomId);

        if(op.isPresent()) {
            room = op.get();
        }else{
            throw new RoomNotFoundException();
        }
        return room;
    }

    /**
     * Uses the room repo to retrieve all records in the Room table
     * of the AssignForce database that have a matching building ID.
     *
     * @param buildingId    ID of the Building to get rooms from
     * @return              List of Room Objects
     */
    @Override
    public List<Room> getRoomsByBuildingId(int buildingId) {
         return this.roomRepo.findRoomByBuildingId(buildingId);
    }

    @Override
    public List<Room> getRoomsByType(int buildingId, RoomType roomType) {
        List<Room> rooms;
        if(roomType.equals(RoomType.VIRTUAL)){
            rooms = this.roomRepo.findVirtualRooms();
        }else{
            rooms = this.roomRepo.findRoomByTypeAndBuildingId(roomType,buildingId);
        }
        return rooms;
    }

    /**
     * Alters a record in the Room table of the AssignForce database
     * with the same roomId as the room object passed to it.
     *
     * @param room          Room object to replace with in the database
     * @return              Updated Room object
     * @throws RoomNotFoundException thrown when there are no room
     * records that have a roomId matching the Room object parameter
     */
    @Override
    public Room updateRoom(Room room) throws RoomNotFoundException {
        Optional<Room> op = roomRepo.findById(room.getRoomId());
        if (!op.isPresent())
            throw new RoomNotFoundException();
        Room oldRoom = op.get();
        if (room.getName() != null && room.getType() != null && room.getBuildingId() != 0) {
            oldRoom.setCapacity(room.getCapacity());
            oldRoom.setType(room.getType());
            oldRoom.setName(room.getName());
            oldRoom.setBuildingId(room.getBuildingId());
        }
        return  roomRepo.save(oldRoom);
    }

    /**
     * Deletes a record from the Room table of the AssignForce database
     * with a room ID that matches the id parameter.
     *
     * @param roomId        ID of the Room record to delete
     * @return              True if the room was deleted, false if an
     *                      exception was thrown
     */
    @Override
    public boolean deleteRoom(int roomId) {
        try {
            this.getRoomById(roomId);
            this.roomRepo.deleteById(roomId);
            return true;
        }catch (Exception e){
            return false;
        }    }
}
