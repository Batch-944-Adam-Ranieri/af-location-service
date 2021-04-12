package com.revature.controllers;


import com.revature.AfLocationServiceApplication;
import com.revature.entities.Room;
import com.revature.entities.RoomType;
import com.revature.exceptions.RoomNotFoundException;
import com.revature.services.RoomService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AfLocationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {"spring.cloud.discovery.enabled=false", "spring.cloud.consul.enabled=false", "spring.cloud.config.enabled=false, spring.cloud.loadbalancer.ribbon.enabled=false"})
@SetEnvironmentVariable(key = "AUTH_SERVER", value = "http://34.66.212.65:8080/verify")
@ActiveProfiles("test")
class RoomControllerTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    RoomService roomService;

    static String trainerJwt;
    static String adminJwt;


    @BeforeAll
    static void setUp() throws Exception {
        trainerJwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoidHJhaW5lciIsImlkIjo5LCJlbWFpbCI6InBvc3RtYW4udGVzdEBlbWFpbC5jb20ifQ.ho14xAMZkwH-RUMWcrEPwyFXOHVMbIY992o5B14EpQA";
        adminJwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJpZCI6MjMsImVtYWlsIjoibGNhcnJpY284MjdAZ21haWwuY29tIn0.lrI1-a3CfLb-nVeHZ9BJBHJ1MN2RHezl8DyP8J4GM8A";

    }

    @Test
    void createRoomTest() throws Exception{
        String json = "{\"roomId\":\"1\", \"name\":\"testName\", \"type\":\"VIRTUAL\", \"buildingId\":1, \"capacity\":\"5\"}";

        Room newRoom = new Room(1, "testName", RoomType.VIRTUAL, 10, 1);
        Mockito.when(roomService.createRoom(any())).thenReturn(newRoom);

        mvc.perform(MockMvcRequestBuilders
                .post("/locations/1/buildings/1/rooms")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isCreated());
    }
    @Test
    void getRoomByIdTest()throws Exception{
        Room room = new Room(1, "testName", RoomType.VIRTUAL, 10, 1);
        Mockito.when(roomService.getRoomById(1)).thenReturn(room);

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isOk());
    }
    @Test
    void getAllRoomsTest()throws Exception{
        List<Room> rooms = new ArrayList<Room>();
        for(int i = 0; i < 5; i++){
            Room room = new Room(1, "testName", RoomType.VIRTUAL, 10, 1);
            rooms.add(room);
        }
        Mockito.when(roomService.getAllRooms()).thenReturn(rooms);

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRoomsByTypeTest()throws Exception{
        List<Room> rooms = new ArrayList<Room>();
        for(int i = 0; i < 5; i++){
            Room room = new Room(1, "testName", RoomType.MEETING, 10, 1);
            rooms.add(room);
        }
        Mockito.when(roomService.getAllRooms()).thenReturn(rooms);

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1/rooms?type=MEETING")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRoomsByTypeExceptionTest()throws Exception{

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1/rooms?type=STUFF")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateRoomTest()throws Exception{
        String json = "{\"roomId\":\"1\", \"name\":\"testName\", \"type\":\"VIRTUAL\", \"buildingId\":1, \"capacity\":\"10\"}";
        Room newRoom = new Room(1, "testName", RoomType.VIRTUAL, 10, 1);
        Mockito.when(roomService.updateRoom(any())).thenReturn(newRoom);

        mvc.perform(MockMvcRequestBuilders
                .put("/locations/1/buildings/1/rooms/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }
    @Test
    void deleteRoomTest()throws Exception{
        Mockito.when(roomService.deleteRoom(anyInt())).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1/buildings/1/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }

    @Test
    void createRoomForbiddenTest()throws Exception{
        String json = "{\"roomId\":\"1\", \"name\":\"testName\", \"type\":\"VIRTUAL\", \"buildingId\":1, \"capacity\":\"5\"}";

        mvc.perform(MockMvcRequestBuilders
                .post("/locations/1/buildings/1/rooms")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isForbidden());
    }
    @Test
    void updateRoomForbiddenTest()throws Exception{
        String json = "{\"roomId\":\"1\", \"name\":\"testName\", \"type\":\"VIRTUAL\", \"buildingId\":1, \"capacity\":\"5\"}";

        mvc.perform(MockMvcRequestBuilders
                .put("/locations/1/buildings/1/rooms/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isForbidden());
    }
    @Test
    void deleteRoomForbiddenTest()throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1/buildings/1/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isForbidden());
    }
    @Test
    void createRoomUnauthorizedTest()throws Exception{
        String json = "{\"roomId\":\"1\", \"name\":\"testName\", \"type\":\"VIRTUAL\", \"buildingId\":1, \"capacity\":\"5\"}";

        mvc.perform(MockMvcRequestBuilders
                .post("/locations/1/buildings/1/rooms")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void updateRoomUnauthorizedTest()throws Exception{
        String json = "{\"roomId\":\"1\", \"name\":\"testName\", \"type\":\"VIRTUAL\", \"buildingId\":1, \"capacity\":\"5\"}";

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
                .andExpect(status().isUnauthorized());
    }
    @Test
    void getRoomNotExistTest()throws Exception{
        Room room = new Room(1000, "testName", RoomType.VIRTUAL, 10, 1);
        Mockito.when(roomService.getRoomById(1000)).thenThrow(new RoomNotFoundException());

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1/rooms/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllRoomsBuildingNotExist ()throws Exception{
        List<Room> rooms = new ArrayList<Room>();
        Mockito.when(roomService.getRoomsByBuildingId(1000)).thenReturn(rooms);

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1000/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isOk());
    }


}
