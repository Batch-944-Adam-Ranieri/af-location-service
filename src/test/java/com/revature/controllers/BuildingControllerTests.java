package com.revature.controllers;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.services.BuildingService;
import com.revature.services.LocationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = AfLocationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BuildingControllerTests {

    @MockBean
    BuildingService buildingService;

    @Autowired
    MockMvc mvc;

    /*
        - `POST, GET /locations/{locationId}/buildings`<-- Post is only available for admins
        - `DELETE, PUT, GET /locations/{locationId}/buildings/{buildingId}`<-- delete and put is only available to admins
     */
    @BeforeAll
    void setup() {
        try {

        }
        catch (Exception e){
            System.err.println("setup failed");
        }
    }

    /*
    with valid authorization

     */
}
