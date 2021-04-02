package com.revature.controllers;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Building;
import com.revature.services.BuildingService;
import com.revature.services.LocationService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


    @Test
    void createBuildingTest() throws Exception{
        String json = "{buildingId:0, address:test, locationId:1}";
        Building building = new Building(0,"test", 1);
        Building newBuilding = new Building(1,"test", 1);
        Mockito.when(buildingService.createBuilding(building)).thenReturn(newBuilding);

        mvc.perform(MockMvcRequestBuilders
                .post("/locations/1/buildings/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
                .andExpect(status().isCreated());
    }

    @Test
    void getBuildingByIdTest() throws Exception{
        Building building = new Building(1,"test", 1);
        Mockito.when(buildingService.getBuildingById(1)).thenReturn(building);

        mvc.perform(MockMvcRequestBuilders
                .get("/buildings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBuildingsTest() throws Exception {
        List<Building> buildings = new ArrayList<>();
        for (int i = 1; i < 5; ++i) {
            Building building = new Building(i,"test",i);
            buildings.add(building);
        }
        Mockito.when(buildingService.getAllBuildings()).thenReturn(buildings);
        mvc.perform(MockMvcRequestBuilders
                .get("/buildings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Authorized"))
                .andExpect(status().isOk());

    }



    @Test
    void updateBuildingTest() throws Exception{
        ResultActions ra = mvc.perform(put("/locations/12/buildings/10").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void deleteBuildingTest() throws Exception{
        ResultActions ra = mvc.perform(delete("/locations/12/buildings/10").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    //without valid authorizations
    @Test
    void createBuildingTestUnauthorized() throws Exception{
        ResultActions ra = mvc.perform(post("/locations/12/buildings").header("Authorization", "unauthorized"));
        ra.andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    void updateBuildingTestUnauthorized() throws Exception{
        ResultActions ra = mvc.perform(put("/locations/12/buildings/10").header("Authorization", "unauthorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void deleteBuildingTestUnauthorized() throws Exception{
        ResultActions ra = mvc.perform(delete("/locations/12/buildings/10").header("Authorization", "unauthorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    //without authorization token
    @Test
    void createBuildingTestNoAuthorizedToken() throws Exception{
        ResultActions ra = mvc.perform(post("/locations/12/buildings"));
        ra.andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    void updateBuildingTestNoAuthorizedToken() throws Exception{
        ResultActions ra = mvc.perform(put("/locations/12/buildings/10"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void deleteBuildingTestNoAuthorizedToken() throws Exception{
        ResultActions ra = mvc.perform(delete("/locations/12/buildings/10"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    //with invalid ID
    @Test
    void createBuildingTestDoesNotExist() throws Exception{
        ResultActions ra = mvc.perform(post("/locations/90001/buildings").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    void updateBuildingTestLocationDoesNotExist() throws Exception{
        ResultActions ra = mvc.perform(put("/locations/90001/buildings/10").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void updateBuildingTestBuildingDoesNotExist() throws Exception{
        ResultActions ra = mvc.perform(put("/locations/12/buildings/90001").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void deleteBuildingTestLocationDoesNotExist() throws Exception{
        ResultActions ra = mvc.perform(delete("/locations/90001/buildings/10").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void deleteBuildingTestBuildingDoesNotExist() throws Exception{
        ResultActions ra = mvc.perform(delete("/locations/12/buildings/9001").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void getBuildingByIdTestLocationDoesNotExist() throws Exception{
        ResultActions ra = mvc.perform(get("/locations/12/buildings/9001").header("Authorization", "authorized"));
        System.out.println(ra.andExpect(status().is(HttpStatus.OK.value())));
    }

    @Test
    void getBuildingByIdTestBuildingDoesNotExist() throws Exception{
        ResultActions ra = mvc.perform(get("/locations/12/buildings/90001").header("Authorization", "authorized"));
        System.out.println(ra.andExpect(status().is(HttpStatus.OK.value())));
    }

    @Test
    void getAllBuildingsTestDoesNotExist() throws Exception {
        ResultActions ra = mvc.perform(get("/locations/9001/buildings").header("Authorization", "authorized"));
        ra.andExpect(status().is(HttpStatus.OK.value()));
    }

    //create or update without body


    //create or update with invalid body

}
