package com.revature.controllers;


import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.services.RoomService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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
        ResultActions ra = mvc.perform(post("/locations/1/buildings/1/rooms").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.CREATED.value()));
    }
    @Test
    void getRoomByIdTest()throws Exception{
        ResultActions ra = mvc.perform(get("/locations/1/buildings/1/rooms/1").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }
    @Test
    void getAllRoomsTest()throws Exception{
        ResultActions ra = mvc.perform(get("/locations/1/buildings/1/rooms").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }
    @Test
    void updateRoomTest()throws Exception{
        ResultActions ra = mvc.perform(put("/locations/1/buildings/1/rooms/1").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }
    @Test
    void deleteRoomTest()throws Exception{
        ResultActions ra = mvc.perform(delete("/locations/1/buildings/1/rooms/1").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void createRoomUnauthorizedTest()throws Exception{
        ResultActions ra = mvc.perform(post("/locations/1/buildings/1/rooms").header("Authorization", "unauthorized"));
        ra.andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }
    @Test
    void updateRoomUnauthorized()throws Exception{
        ResultActions ra = mvc.perform(put("/locations/1/buildings/1/rooms/1").header("Authorization", "unauthorized"));
        ra.andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }
    @Test
    void deleteRoomUnauthorized()throws Exception{
        ResultActions ra = mvc.perform(delete("/locations/1/buildings/1/rooms/1").header("Authorization", "unauthorized"));
        ra.andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }
    @Test
    void createRoomNoAuthorizationTest()throws Exception{
        ResultActions ra = mvc.perform(post("/locations/1/buildings/1/rooms"));
        ra.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }
    @Test
    void updateRoomNoAuthorizationTest()throws Exception{
        ResultActions ra = mvc.perform(put("/locations/1/buildings/1/rooms/1"));
        ra.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }
    @Test
    void deleteRoomNoAuthorizationTest()throws Exception{
        ResultActions ra = mvc.perform(delete("/locations/1/buildings/1/rooms/1"));
        ra.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }
    @Test
    void getRoomNotExistTest()throws Exception{
        ResultActions ra = mvc.perform(get("/locations/1/buildings/1/rooms/1000").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
    @Test
    void getBuildingNotExistTest()throws Exception{
        ResultActions ra = mvc.perform(get("/locations/1/buildings/1000/rooms/1").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
    @Test
    void getRoomLocationNotExistTest()throws Exception{
        ResultActions ra = mvc.perform(get("/locations/1000/buildings/1/rooms/1").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
    @Test
    void getAllRoomsBuildingNotExist ()throws Exception{
        ResultActions ra = mvc.perform(get("/locations/1/buildings/1000/rooms").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void getAllRoomsLocationNotExist ()throws Exception{
        ResultActions ra = mvc.perform(get("/locations/1000/buildings/1/rooms").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }


}
