package com.revature.controllers;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Location;
import com.revature.services.LocationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = AfLocationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class LocationControllerTest {

    @MockBean
    static LocationService locationService;

    @Autowired
    MockMvc mvc;

    /*
        - `POST, GET /locations` <-- Post is only available for admins

        - `DELETE, PUT, GET /locations/{locationId}`<-- delete and put is only available to admins

     */

    @BeforeAll
    static void setup(){
        try {
            Mockito.when(locationService.getLocationById(12)).thenReturn(new Location(12, "testCity", "testState", "testZip"));
        }
        catch (Exception e){
            System.err.println("setup failed");
        }
    }

    @Test
    void getLocationByIdTest() throws Exception{
        ResultActions ra = mvc.perform(get("/locations/12"));
        System.out.println(ra.andExpect(status().is(200)));
    }

    @Test
    void getAllLocationsTest() throws Exception {
        ResultActions ra = mvc.perform(get("/locations"));
        ra.andExpect(status().is(200));
    }

    @Test
    void createLocationTest() throws Exception{
        ResultActions ra = mvc.perform(post("/locations"));
        ra.andExpect(status().is(401));//is unauthorized
    }

    @Test
    void updateLocationTest() throws Exception{
        ResultActions ra = mvc.perform(post("/locations/12"));
        ra.andExpect(status().is(401));//is unauthorized
    }

    @Test
    void deleteLocationTest() throws Exception{
        ResultActions ra = mvc.perform(post("/locations/12"));
        ra.andExpect(status().is(401));//is unauthorized
    }
}
