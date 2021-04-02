package com.revature.controllers;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Location;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.services.LocationService;
import com.sun.xml.bind.v2.TODO;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = AfLocationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LocationControllerTest {

    @MockBean
    LocationService locationService;

    @Autowired
    MockMvc mvc;

    /*
        - `POST, GET /locations` <-- Post is only available for admins

        - `DELETE, PUT, GET /locations/{locationId}`<-- delete and put is only available to admins

     */

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
                .header("Authorization","Authorized"))
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
                .header("Authorization","Authorized"))
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
                .header("Authorization","Authorized"))
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
                .header("Authorization","Authorized"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLocationTest() throws Exception{
        Mockito.when(locationService.deleteLocation(12)).thenReturn(true);
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/12")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
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
                .header("Authorization","Unauthorized"))
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
                .header("Authorization","Unauthorized"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteLocationTestForbidden() throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/12")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Unauthorized"))
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
                .header("Authorization","Authorized"))
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
                .header("Authorization","Authorized"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLocationTestDoesNotExist() throws Exception{
        Mockito.when(locationService.deleteLocation(1000)).thenReturn(true);
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
                .andExpect(status().isOk());
    }

}
