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
    List<Room> testRoomList;
    Building testBuilding;
    Building illegalBuilding;
    Location testLocation;

    @BeforeAll
    void setup(){
        testLocation = new Location(1, "Test City", "Test State","testzip");
        testBuilding = new Building(1,"Test Building",1);

        testRoomList = new ArrayList<Room>();
        for(int i = 1; i < 5; i++){
            Room room = new Room(i,"Room "+i,RoomType.CLASSROOM,50,1);
            if(i==1){
                testRoom=room;
            }
            testRoomList.add(room);
        }
        illegalBuilding = new Building(100,"Illegal Address",100);

//        Mockito.when(roomRepo.save(testRoom)).thenReturn(testRoom);
//        Mockito.when(roomRepo.findAll()).thenReturn(testRoomList);
//        Mockito.when(roomRepo.findById(2)).thenReturn(Optional.of(testRoomList.get(1)));
//        Mockito.when(roomRepo.findById(100)).thenReturn(Optional.empty());
//        Mockito.when(roomRepo.findRoomByBuildingId(illegalBuilding.getBuildingId())).thenReturn(new ArrayList<Room>());

    }

    @Test
    void testMocking() {
        Mockito.when(roomRepo.findById(1)).thenReturn(Optional.of(testRoom));

        Optional<Room> o = roomRepo.findById(1);
        Room room = o.orElse(null);
        Assertions.assertNotNull(room);
    }

    @Test
    @Order(1)
    void create_room_test(){
        Mockito.when(roomRepo.save(testRoom)).thenReturn(testRoom);

        Room room = new Room(5,"New Room", RoomType.CLASSROOM,30,1);
        room = this.roomService.createRoom(room);
        Assertions.assertNotNull(room);
        Assertions.assertEquals(0,room.getRoomId());
    }

    @Test
    @Order(2)
    void get_all_rooms_test(){
        Mockito.when(roomRepo.findAll()).thenReturn(testRoomList);

        List<Room> rooms = this.roomService.getAllRooms();
        Assertions.assertNotNull(rooms);
        Assertions.assertNotEquals(1,rooms.size());
    }

    @Test
    @Order(3)
    void get_room_by_id(){
        Mockito.when(roomRepo.findById(testRoom.getRoomId())).thenReturn(Optional.of(testRoom));

        try{
            int id = testRoom.getRoomId();
            Room room = this.roomService.getRoomById(id);
            Assertions.assertNotNull(room);
            Assertions.assertEquals(id, room.getRoomId());
        }catch(RoomNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    @Order(4)
    void get_rooms_by_building_id_test(){
        Mockito.when(roomRepo.findRoomByBuildingId(testBuilding.getBuildingId())).thenReturn(testRoomList);

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
        Mockito.when(roomRepo.findById(testRoom.getRoomId())).thenReturn(Optional.of(testRoom));
        Mockito.when(roomRepo.save(testRoom)).thenReturn(testRoom);

        try{
            int roomId = testRoom.getRoomId();
            String updatedRoomName = "Updated Room Name";
            Room room = new Room(roomId, updatedRoomName, testRoom.getType(), testRoom.getCapacity(), testRoom.getBuildingId());
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
        Mockito.when(roomRepo.findById(100)).thenReturn(Optional.empty());

        try{
            this.roomService.getRoomById(100);
            Assertions.fail();
        }catch(RoomNotFoundException e){

        }
    }

    @Test
    @Order(7)
    void update_room_exception_test(){
        Mockito.when(roomRepo.findById(100)).thenReturn(Optional.empty());
        Mockito.when(roomRepo.save(illegalRoom)).thenReturn(illegalRoom);
        illegalRoom = new Room(100,"Illegal Name",RoomType.CLASSROOM,10000,100);

        try{
            this.roomService.updateRoom(illegalRoom);
            Assertions.fail();
        }catch(RoomNotFoundException e){

        }
    }

    @Test
    @Order(8)
    void delete_room(){
        Mockito.when(roomRepo.findById(testRoom.getRoomId())).thenReturn(Optional.of(testRoom));
        int id = testRoom.getRoomId();

//        Assertions.assertNotNull(id);
        Assertions.assertTrue(roomService.deleteRoom(testRoom.getRoomId()));
    }

}
