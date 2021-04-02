package com.revature.services;


import com.revature.entities.Building;
import com.revature.entities.Room;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.exceptions.RoomNotFoundException;
import com.revature.repos.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepo roomRepo;

    @Override
    public Room createRoom(Room room) {
        room.setRoomId(0);
        this.roomRepo.save(room);
        return room;
    }

    @Override
    public List<Room> getAllRooms() {

        return (List<Room>) this.roomRepo.findAll();
    }

    @Override
    public Room getRoomById(int roomId) throws RoomNotFoundException {
        Room room;
        Optional<Room> op = roomRepo.findById(roomId);

        if(op.isPresent()) {
            room = op.get();
            System.out.println(room);
        }else{
            throw new RoomNotFoundException();
        }
        return room;
    }

    @Override
    public List<Room> getRoomsByBuildingId(int buildingId) {
        List<Room> rooms = this.roomRepo.findRoomByBuildingId(buildingId);
        return rooms;
    }

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
        Room updatedRoom = roomRepo.save(oldRoom);
        return updatedRoom;
    }

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
