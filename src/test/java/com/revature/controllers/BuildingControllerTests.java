package com.revature.controllers;

import com.revature.AfLocationServiceApplication;
import com.revature.entities.Building;
import com.revature.entities.Location;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.services.BuildingService;
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
@SetEnvironmentVariable(key = "AUTH_SERVER", value = "http://34.66.212.65:8080/verify")
@ActiveProfiles("test")
class BuildingControllerTests {

    @MockBean
    BuildingService buildingService;

    @Autowired
    MockMvc mvc;

    static String trainerJwt;
    static String adminJwt;


    /*
        - `POST, GET /locations/{locationId}/buildings`<-- Post is only available for admins
        - `DELETE, PUT, GET /locations/{locationId}/buildings/{buildingId}`<-- delete and put is only available to admins
     */

    @BeforeAll
    static void setUp() throws Exception {
        trainerJwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoidHJhaW5lciIsImlkIjo5LCJlbWFpbCI6InBvc3RtYW4udGVzdEBlbWFpbC5jb20iLCJzdGF0dXMiOiJjcmVhdGVkIn0.Y4OpecIzq9LSSMwlKAAXLt1_hwhx5_4BmwWZ9f2lhjE";
        adminJwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiYWRtaW4iLCJpZCI6MjMsImVtYWlsIjoibGNhcnJpY284MjdAZ21haWwuY29tIn0.lrI1-a3CfLb-nVeHZ9BJBHJ1MN2RHezl8DyP8J4GM8A";

    }

    @Test
    void createBuildingTest() throws Exception{
        String json = "{\"buildingId\":0, \"address\":\"test\", \"locationId\":1}";
        Building building = new Building(0,"test", 1);
        Building newBuilding = new Building(1,"test", 1);
        Mockito.when(buildingService.createBuilding(any(Building.class))).thenReturn(new Building());

        mvc.perform(MockMvcRequestBuilders
                .post("/locations/1/buildings")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isCreated());
    }

    @Test
    void getBuildingByIdTest() throws Exception{
        Building building = new Building(1,"test", 1);
        Mockito.when(buildingService.getBuildingById(1)).thenReturn(building);

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBuildingsOfLocationTest() throws Exception {
        List<Building> buildings = new ArrayList<>();
        for (int i = 1; i < 5; ++i) {
            Building building = new Building(i,"test",1);
            buildings.add(building);
        }
        Mockito.when(buildingService.getAllBuildings()).thenReturn(buildings);
        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isOk());

    }



    @Test
    void updateBuildingTest() throws Exception{
        String json = "{\"buildingId\":1, \"address\":\"newtest\", \"locationId\":1}";
        Building building = new Building(1,"newtest", 1);
        Mockito.when(buildingService.updateBuilding(building)).thenReturn(building);

        mvc.perform(MockMvcRequestBuilders
                .put("/locations/1/buildings/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBuildingTest() throws Exception{
        Mockito.when(buildingService.deleteBuildingById(1)).thenReturn(true);
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1/buildings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }

    //without valid authorizations
    @Test
    void createBuildingTestForbidden() throws Exception{
        String json = "{\"buildingId\":0, \"address\":\"test\", \"locationId\":1}";
        mvc.perform(MockMvcRequestBuilders
                .post("/locations/1/buildings")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateBuildingTestForbidden() throws Exception{
        String json = "{\"buildingId\":1, \"address\":\"newtest\", \"locationId\":1}";
        mvc.perform(MockMvcRequestBuilders
                .put("/locations/1/buildings/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBuildingTestForbidden() throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1/buildings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isForbidden());
    }

    //without authorization token
    @Test
    void createBuildingTestUnauthorized() throws Exception{
        String json = "{\"buildingId\":0, \"address\":\"test\", \"locationId\":1}";
        mvc.perform(MockMvcRequestBuilders
                .post("/locations/1/buildings")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateBuildingTestUnauthorized() throws Exception{
        String json = "{\"buildingId\":1, \"address\":\"newtest\", \"locationId\":1}";
        mvc.perform(MockMvcRequestBuilders
                .put("/locations/1/buildings/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBuildingTestUnauthorized() throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1/buildings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void updateBuildingBuildingDoesNotExistTest() throws Exception{
        String json = "{\"buildingId\":1000, \"address\":\"newtest\", \"locationId\":1}";
        Building building = new Building(1000,"newtest", 1);
        Mockito.when(buildingService.updateBuilding(building)).thenThrow(new BuildingNotFoundException());

        mvc.perform(MockMvcRequestBuilders
                .put("/locations/1/buildings/1000")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBuildingLocationDoesNotExistTest() throws Exception{
        Mockito.when(buildingService.deleteBuildingById(1)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1000/buildings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBuildingBuildingDoesNotExistTest() throws Exception{
        Mockito.when(buildingService.deleteBuildingById(1000)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                .delete("/locations/1/buildings/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",adminJwt))
                .andExpect(status().isOk());
    }

    @Test
    void getBuildingByIdBuildingDoesNotExistTest() throws Exception{

        Mockito.when(buildingService.getBuildingById(1000)).thenThrow(new BuildingNotFoundException());

        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1/buildings/1000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllBuildingsDoesNotExist() throws Exception {
        List<Building> buildings = new ArrayList<Building>();
        Location location = new Location(1000, "test","test","test");
        Mockito.when(buildingService.getBuildingByLocation(location.getLocationId())).thenReturn(buildings);
        mvc.perform(MockMvcRequestBuilders
                .get("/locations/1000/buildings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization",trainerJwt))
                .andExpect(status().isOk());
    }


}
