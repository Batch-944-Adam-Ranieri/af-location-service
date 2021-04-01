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
        - `POST, GET /locations/{locationId}/buildings`<-- Post is only available for admins
        - `POST, GET /locations/{locationId}/buildings/{buildingId}/rooms`<-- Post is only available for admins
        - `DELETE, PUT, GET /locations/{locationId}`<-- delete and put is only available to admins
        - `DELETE, PUT, GET /locations/{locationId}/buildings/{buildingId}`<-- delete and put is only available to admins
        - `DELETE, PUT, GET /locations/{locationId}/buildings/{buildingId}/rooms/{roomId}` <-- delete and put is only available to admins
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
        //ra.andExpect(status() == 200);
        System.out.println(ra.andExpect(status() HttpStatus.OK)));
    }
}
