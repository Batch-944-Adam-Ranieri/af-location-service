package com.revature.repos;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Room;
import com.revature.entities.RoomType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
@SpringBootTest(classes= AfLocationServiceApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomRepoTests {

    @Autowired
    RoomRepo roomRepo;

    @Test
    @Order(1)
    void create_room_test(){
        Room room = new Room(0, "Room Name", RoomType.MEETING, 50, 1);
        room = this.roomRepo.save(room);
        Assertions.assertNotEquals(0,room.getRoomId());
    }

    @Test
    @Order(2)
    void get_all_rooms(){
        List<Room> rooms = (ArrayList<Room>)this.roomRepo.findAll();
        Assertions.assertTrue(rooms.size()>1);
    }

    @Test
    @Order(3)
    void get_room_by_id(){
        int id = 2;
        Room room = this.roomRepo.findById(id).orElse(null);
        Assertions.assertEquals(id,room.getRoomId());
    }

    @Test
    void get_remote_rooms() {
        int buildingId = 2;
        List<Room> rooms = this.roomRepo.findRoomByTypeAndBuildingId(RoomType.REMOTE, buildingId);

        for(Room room: rooms){
            Assertions.assertEquals(RoomType.REMOTE, room.getType());
            Assertions.assertEquals(buildingId, room.getBuildingId());
        }
    }

    @Test
    void get_meeting_rooms() {
        int buildingId = 3;
        List<Room> rooms = this.roomRepo.findRoomByTypeAndBuildingId(RoomType.MEETING, buildingId);

        for(Room room: rooms){
            Assertions.assertEquals(RoomType.MEETING, room.getType());
            Assertions.assertEquals(buildingId, room.getBuildingId());
        }
    }

    @Test
    void get_virtual_rooms() {
        List<Room> rooms = this.roomRepo.findVirtualRooms();
        for(Room room: rooms){
            Assertions.assertEquals(RoomType.VIRTUAL, room.getType());
        }
        Assertions.assertTrue(rooms.size() > 1);
    }

    @Test
    void get_rooms_by_building_id(){
        int id = 3;
        List<Room> rooms = roomRepo.findRoomByBuildingId(id);
        for(Room r: rooms) {
            Assertions.assertEquals(3, r.getBuildingId());
        }
    }
    @Test
    @Order(4)
    void update_room_by_id(){
        String updatedRoomName = "Updated Room Name";
        Room newRoom = new Room(1, updatedRoomName, RoomType.MEETING, 50, 1);
        Room newRm = this.roomRepo.save(newRoom);

        Assertions.assertEquals(updatedRoomName, newRm.getName());
    }

    @Test
    @Order(5)
    void delete_room_by_id(){
        int id = 2;
        this.roomRepo.deleteById(id);
        Room result = this.roomRepo.findById(id).orElse(null);
        Assertions.assertNull(result);
    }

}
