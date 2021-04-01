package com.revature.services;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Building;
import com.revature.entities.Location;
import com.revature.entities.Room;
import com.revature.entities.RoomType;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.exceptions.RoomNotFoundException;
import com.revature.repos.BuildingRepo;
import com.revature.repos.LocationRepo;
import com.revature.repos.RoomRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import static com.revature.services.BuildingServiceTests.illegalBuilding;
//import static com.revature.services.BuildingServiceTests.testBuildingList;

@SpringBootTest(classes = AfLocationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoomServiceTests {

    @InjectMocks
    RoomServiceImpl roomService;

    @MockBean
    RoomRepo roomRepo;

    @MockBean
    BuildingRepo buldingRepo;

    Room testRoom;
    Room illegalRoom;
    Building testBuilding;
    Building illegalBuilding;
    Location testLocation;

    @BeforeAll
    void setup(){
        testLocation = new Location(1, "Test City", "Test State","testzip");
        testBuilding = new Building(1,"Test Building",1);

        List<Room> testRoomList = new ArrayList<Room>();
        for(int i = 1; i < 5; i++){
            Room room = new Room(i,"Room "+i,RoomType.CLASSROOM,50,1);
            testRoomList.add(room);
        }
        testRoom = testRoomList.get(1);
        illegalRoom = new Room(100,"Illegal Name",RoomType.CLASSROOM,10000,100);
        illegalBuilding = new Building(100,"Illegal Address",100);

        Mockito.when(roomRepo.save(testRoom)).thenReturn(testRoom);
        Mockito.when(roomRepo.findAll()).thenReturn(testRoomList);
        Mockito.when(roomRepo.findById(2)).thenReturn(Optional.of(testRoomList.get(1)));
        Mockito.when(roomRepo.findById(100)).thenReturn(Optional.empty());
        Mockito.when(roomRepo.findById(100)).thenReturn(Optional.empty());
        Mockito.when(roomRepo.findRoomByBuildingId(illegalBuilding.getBuildingId())).thenReturn(new ArrayList<Room>());

    }

    @Test
    void testMocking() {
        Optional<Room> o = roomRepo.findById(2);
        Room l = o.orElse(null);
        Assertions.assertNotNull(l);
    }

    @Test
    @Order(1)
    void create_room_test(){
        Room room = new Room(0,"New Room", RoomType.CLASSROOM,30,1);
        room = this.roomService.createRoom(room);
        Assertions.assertNotNull(room);
        Assertions.assertNotEquals(0,room.getRoomId());
    }

    @Test
    @Order(2)
    void get_all_rooms_test(){
        List<Room> rooms = this.roomService.getAllRooms();
        Assertions.assertNotNull(rooms);
        Assertions.assertNotEquals(1,rooms.size());
    }

    @Test
    @Order(3)
    void get_room_by_id(){
        try{
            Room room = this.roomService.getRoomById(testRoom.getRoomId());
            Assertions.assertNotNull(room);
            Assertions.assertEquals(testRoom.getRoomId(), room.getRoomId());
        }catch(RoomNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    @Order(4)
    void get_rooms_by_building_id_test(){
        int id = testBuilding.getBuildingId();
        List<Room> rooms = this.roomService.getRoomsByBuildingId(id);
        Assertions.assertNotNull(rooms);
        for(Room r : rooms){
            Assertions.assertEquals(id,r.getBuildingId());
        }
    }

    @Test
    @Order(5)
    void update_room_test(){
        try{
            String updatedRoomName = "Updated Room Name";
            Room room = new Room(testRoom.getRoomId(), updatedRoomName, testRoom.getType(), testRoom.getCapacity(), testRoom.getBuildingId());
            room = this.roomService.updateRoom(room);
            Assertions.assertNotNull(room);
            Assertions.assertEquals(updatedRoomName, room.getName());
        }catch(RoomNotFoundException e){
            Assertions.fail();
        }
    }

    @Test
    @Order(6)
    void get_room_by_id_exception_test(){
        try{
            this.roomService.getRoomById(100);
            Assertions.fail();
        }catch(RoomNotFoundException e){

        }
    }

    @Test
    @Order(7)
    void update_room_exception_test(){
        try{
            this.roomService.updateRoom(illegalRoom);
            Assertions.fail();
        }catch(RoomNotFoundException e){

        }
    }

}
