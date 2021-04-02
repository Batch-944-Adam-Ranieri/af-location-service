package com.revature.controllers;


import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Building;
import com.revature.entities.Room;
import com.revature.entities.RoomType;
import com.revature.exceptions.RoomNotFoundException;
import com.revature.services.RoomService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AfLocationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoomControllerTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    RoomService roomService;

    @BeforeAll
    void setUp(){

    }

    @Test
    void createRoomTest() throws Exception{
        String json = "{roomId:0, name:testName, type:ONLINE, capacity:10, buildingId:1}";
        Room room = new Room(0, "testName", RoomType.ONLINE, 10, 1);
        Room newRoom = new Room(1, "testName", RoomType.ONLINE, 10, 1);
        Mockito.when(roomService.createRoom(room)).thenReturn(newRoom);

        mvc.perform(MockMvcRequestBuilders
                .post("/locations/1/buildings/1/rooms")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
                .andExpect(status().isCreated());
    }
    @Test
    void getRoomByIdTest()throws Exception{
        Room room = new Room(1, "testName", RoomType.ONLINE, 10, 1);
        Mockito.when(roomService.getRoomById(1)).thenReturn(room);

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
                .andExpect(status().isOk());
    }
    @Test
    void getAllRoomsTest()throws Exception{
        List<Room> rooms = new ArrayList<Room>();
        for(int i = 0; i < 5; i++){
            Room room = new Room(1, "testName", RoomType.ONLINE, 10, 1);
            rooms.add(room);
        }
        Mockito.when(roomService.getAllRooms()).thenReturn(rooms);

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
                .andExpect(status().isOk());
    }
    @Test
    void updateRoomTest()throws Exception{
        String json = "{roomId:1, name:testName, type:ONLINE, capacity:10, buildingId:1}";
        Room newRoom = new Room(1, "testName", RoomType.ONLINE, 10, 1);
        Mockito.when(roomService.createRoom(newRoom)).thenReturn(newRoom);

        mvc.perform(MockMvcRequestBuilders
                .put("/locations/1/buildings/1/rooms/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
                .andExpect(status().isOk());
    }
    @Test
    void deleteRoomTest()throws Exception{
        Mockito.when(roomService.deleteRoom(1)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1/buildings/1/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
                .andExpect(status().isOk());
    }

    @Test
    void createRoomForbiddenTest()throws Exception{
        String json = "{roomId:0, name:testName, type:ONLINE, capacity:10, buildingId:1}";

        mvc.perform(MockMvcRequestBuilders
                .post("/locations/1/buildings/1/rooms")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Unauthorized"))
                .andExpect(status().isForbidden());
    }
    @Test
    void updateRoomForbiddenTest()throws Exception{
        String json = "{roomId:1, name:testName, type:ONLINE, capacity:10, buildingId:1}";

        mvc.perform(MockMvcRequestBuilders
                .put("/locations/1/buildings/1/rooms/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Unauthorized"))
                .andExpect(status().isForbidden());
    }
    @Test
    void deleteRoomForbiddenTest()throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1/buildings/1/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Unauthorized"))
                .andExpect(status().isForbidden());
    }
    @Test
    void createRoomUnauthorizedTest()throws Exception{
        String json = "{roomId:0, name:testName, type:ONLINE, capacity:10, buildingId:1}";

        mvc.perform(MockMvcRequestBuilders
                .post("/locations/1/buildings/1/rooms")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void updateRoomUnauthorizedTest()throws Exception{
        String json = "{roomId:1, name:testName, type:ONLINE, capacity:10, buildingId:1}";

        mvc.perform(MockMvcRequestBuilders
                .put("/locations/1/buildings/1/rooms/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void deleteRoomUnauthorizedTest()throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1/buildings/1/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    @Test
    void getRoomNotExistTest()throws Exception{
        Room room = new Room(1000, "testName", RoomType.ONLINE, 10, 1);
        Mockito.when(roomService.getRoomById(1000)).thenThrow(new RoomNotFoundException());

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1/rooms/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
                .andExpect(status().reason("Room not found"));
    }
//    @Test
//    void getBuildingNotExistTest()throws Exception{
//        ResultActions ra = mvc.perform(get("/locations/1/buildings/1000/rooms/1").header("Authorization", "authorized"));
//        ra.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
//    }
//    @Test
//    void getRoomLocationNotExistTest()throws Exception{
//        ResultActions ra = mvc.perform(get("/locations/1000/buildings/1/rooms/1").header("Authorization", "authorized"));
//        ra.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
//    }
    @Test
    void getAllRoomsBuildingNotExist ()throws Exception{
        List<Room> rooms = new ArrayList<Room>();
        Mockito.when(roomService.getRoomsByBuildingId(1000)).thenReturn(rooms);

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1000/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
                .andExpect(status().isOk());
    }


}
