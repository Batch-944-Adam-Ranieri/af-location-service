package com.revature.controllers;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Location;
import com.revature.services.LocationService;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.*;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = AfLocationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LocationControllerTest {

    @MockBean
    LocationService locationService;

    @Autowired
    MockMvc mvc;

    /*
        - `POST, GET /locations` <-- Post is only available for admins

        - `DELETE, PUT, GET /locations/{locationId}`<-- delete and put is only available to admins

     */

    @BeforeAll
    void setup(){
        try {
            ArrayList<Location> locations = new ArrayList<Location>();
            locations.add(new Location(12, "testCity", "testState", "testZip"));

            Mockito.when(locationService.getLocationById(12)).thenReturn(locations.get(0));
            Mockito.when(locationService.getAllLocations()).thenReturn(locations);

            Mockito.when(locationService.createLocation(Mockito.any(Location.class))).then(i-> i.getArguments()[0]);

            Mockito.when(locationService.updateLocation(Mockito.any(Location.class))).then(i-> i.getArguments()[0]);

            Mockito.when(locationService.deleteLocation(12)).thenReturn(true);

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
    void createLocationTestUnauthorized() throws Exception{
        ResultActions ra = mvc.perform(post("/locations").header("Authorization", "unauthorized"));
        ra.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));//is unauthorized
    }

    @Test
    void updateLocationTestUnauthorized() throws Exception{
        ResultActions ra = mvc.perform(put("/locations").header("Authorization", "unauthorized"));
        ra.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));//is unauthorized
    }

    @Test
    void deleteLocationTestUnauthorized() throws Exception{
        ResultActions ra = mvc.perform(delete("/locations").header("Authorization", "unauthorized"));
        ra.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));//is unauthorized
    }

    @Test
    void getLocationByIdTestDoesNotExist() throws Exception{
        ResultActions ra = mvc.perform(get("/locations/9001"));
        ra.andExpect(MockMvcResultMatchers.status().reason(StringContains.containsString("Cannot find location")));
    }

    @Test
    void updateLocationTestDoesNotExist() throws Exception{
        ResultActions ra = mvc.perform(put("/locations/9001"));
        //System.out.printf("ra status reason: %s\n", ra.andReturn());
        ra.andExpect(MockMvcResultMatchers.status().reason(StringContains.containsString("Cannot find location")));
    }

    @Test
    void deleteLocationTestDoesNotExist() throws Exception{
        ResultActions ra = mvc.perform(delete("/locations/9001"));
        ra.andExpect(MockMvcResultMatchers.status().reason(StringContains.containsString("Cannot find location")));
    }

    @Test
    void createLocationTest() throws Exception{
        ResultActions ra = mvc.perform(post("/locations").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    void updateLocationTest() throws Exception{
        ResultActions ra = mvc.perform(put("/locations/12").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void deleteLocationTest() throws Exception{
        ResultActions ra = mvc.perform(delete("/locations/12").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void createLocationTestNoAuthorizedToken() throws Exception{
        ResultActions ra = mvc.perform(post("/locations"));
        ra.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));//is unauthorized
    }

    @Test
    void updateLocationTestNoAuthorizedToken() throws Exception{
        ResultActions ra = mvc.perform(put("/locations/12"));
        ra.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));//is unauthorized
    }

    @Test
    void deleteLocationTestNoAuthorizedToken() throws Exception{
        ResultActions ra = mvc.perform(delete("/locations/12"));
        ra.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));//is unauthorized
    }
}
