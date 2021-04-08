package com.revature.controllers;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Location;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.services.LocationService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = AfLocationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {"spring.cloud.discovery.enabled=false", "spring.cloud.consul.enabled=false", "spring.cloud.config.enabled=false"})
@SetEnvironmentVariable(key = "AUTH_SERVER", value = "http://35.232.107.40:8080/verify")
@ActiveProfiles("test")
class LocationControllerTest {

    @MockBean
    LocationService locationService;

    @Autowired
    MockMvc mvc;

    static String trainerJwt;
    static String adminJwt;

    /*
        - `POST, GET /locations` <-- Post is only available for admins

        - `DELETE, PUT, GET /locations/{locationId}`<-- delete and put is only available to admins

     */

    @BeforeAll
    static void setUp() throws Exception {
        trainerJwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoidHJhaW5lciIsImlkIjo5LCJlbWFpbCI6InBvc3RtYW4udGVzdEBlbWFpbC5jb20ifQ.ho14xAMZkwH-RUMWcrEPwyFXOHVMbIY992o5B14EpQA";
        adminJwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJpZCI6MjMsImVtYWlsIjoibGNhcnJpY284MjdAZ21haWwuY29tIn0.lrI1-a3CfLb-nVeHZ9BJBHJ1MN2RHezl8DyP8J4GM8A";
    }

    @Test
    void createLocationTest() throws Exception{
        String json = "{\"locationId\":\"0\", \"city\":\"test\", \"state\":\"test\", \"zipcode\":\"test\"}";
        Location location = new Location(0,"test","test","test");
        Location newLocation = new Location(1,"test","test","test");
        Mockito.when(locationService.createLocation(any())).thenReturn(newLocation);
        mvc.perform(MockMvcRequestBuilders
                .post("/locations")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isCreated());
    }

    @Test
    void getLocationByIdTest() throws Exception{
        Location location = new Location(12, "test","test","test");
        Mockito.when(locationService.getLocationById(12)).thenReturn(location);
        mvc.perform(MockMvcRequestBuilders
                .get("/locations/12")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }

    @Test
    void getAllLocationsTest() throws Exception {
        List<Location> locations = new ArrayList<>();
        for(int i = 1; i <5; ++i) {
            Location location = new Location(i,"test","test","test");
            locations.add(location);
        }
        Mockito.when(locationService.getAllLocations()).thenReturn(locations);

        mvc.perform(MockMvcRequestBuilders
                .get("/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }

    @Test
    void updateLocationTest() throws Exception{
        Location location = new Location(12, "newtest", "test", "test");
        Mockito.when(locationService.updateLocation(any(Location.class))).thenReturn(location);
        String json = "{\"locationId\":\"12\", \"city\":\"newtest\", \"state\":\"test\", \"zipcode\":\"test\"}";
        mvc.perform(MockMvcRequestBuilders
                .put("/locations/12")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLocationTest() throws Exception{
        Mockito.when(locationService.deleteLocation(12)).thenReturn(true);
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/12")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }

    @Test
    void createLocationTestForbidden() throws Exception{
        String json = "{\"locationId\":\"0\", \"city\":\"test\", \"state\":\"test\", \"zipcode\":\"test\"}";
        mvc.perform(MockMvcRequestBuilders
                .post("/locations")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateLocationTestForbidden() throws Exception{
        String json = "{\"locationId\":\"12\", \"city\":\"newtest\", \"state\":\"test\", \"zipcode\":\"test\"}";
        mvc.perform(MockMvcRequestBuilders
                .put("/locations/12")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteLocationTestForbidden() throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/12")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isForbidden());
    }

    @Test
    void createLocationTestUnauthorized() throws Exception{
        String json = "{\"locationId\":\"0\", \"city\":\"test\", \"state\":\"test\", \"zipcode\":\"test\"}";
        mvc.perform(MockMvcRequestBuilders
                .post("/locations")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateLocationTestUnauthorized() throws Exception{
        String json = "{\"locationId\":\"0\", \"city\":\"test\", \"state\":\"test\", \"zipcode\":\"test\"}";
        mvc.perform(MockMvcRequestBuilders
                .put("/locations/12")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteLocationTestNoUnauthorized() throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/12")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getLocationByIdTestDoesNotExist() throws Exception{
        Mockito.when(locationService.getLocationById(1000)).thenThrow(new LocationNotFoundException());
        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isNotFound());
    }

    @Test

    void updateLocationTestDoesNotExist() throws Exception{

        Location location = new Location(1000, "newtest", "test", "test");
        String json = "{\"locationId\":\"12\", \"city\":\"newtest\", \"state\":\"test\", \"zipcode\":\"test\"}";
        Mockito.when(locationService.updateLocation(any(Location.class))).thenThrow(new LocationNotFoundException());
        mvc.perform(MockMvcRequestBuilders
                .put("/locations/1000")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLocationTestDoesNotExist() throws Exception{
        Mockito.when(locationService.deleteLocation(1000)).thenReturn(true);
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }

}
