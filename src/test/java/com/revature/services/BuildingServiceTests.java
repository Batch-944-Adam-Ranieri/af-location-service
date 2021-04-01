package com.revature.services;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Building;
import com.revature.entities.Location;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.repos.BuildingRepo;
import com.revature.repos.LocationRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

@SpringBootTest(classes = AfLocationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BuildingServiceTests {

    @InjectMocks
    BuildingServiceImpl buildingService;

    @MockBean
    LocationRepo locationRepo;

    @MockBean
    BuildingRepo buildingrepo;

    Location testLocation;
    Location illegalLocation;
    List<Building> testBuildingList;
    Building testBuilding;
    Building illegalBuilding;

    @BeforeAll
    void setup(){

        testBuildingList=new ArrayList<Building>();
        for(int i = 1; i < 5; i++){
            Building building = new Building(i, "test "+i, 1);
            testBuildingList.add(building);
        }

        testBuilding = testBuildingList.get(1);
        illegalBuilding = new Building(1000,"Illegal Building Address",1000);
        testLocation = new Location(1, "Dallas","TX","75019");
        illegalLocation = new Location(1000,"Illegal City","Illegal State","illegalzip");

        Mockito.when(locationRepo.findById(1)).thenReturn(java.util.Optional.of(testLocation));
        Mockito.when(buildingrepo.save(testBuilding)).thenReturn(testBuilding);
        Mockito.when(buildingrepo.findAll()).thenReturn(testBuildingList);
        Mockito.when(buildingrepo.findById(2)).thenReturn(Optional.of(testBuildingList.get(2)));
        Mockito.when(buildingrepo.findById(100)).thenReturn(Optional.empty());
        Mockito.when(buildingrepo.findBuildingsByLocationId(illegalLocation.getLocationId())).thenReturn(new ArrayList<Building>());
    }

    @Test
    void testMocking() {
        Optional<Location> o = locationRepo.findById(1);
        Location l = o.orElse(null);
        Assertions.assertNotNull(l);

        Assertions.assertNotNull(buildingrepo.findById(2).orElse(null));
    }

    @Test
    @Order(1)
    void create_building_test() {
        Building building = new Building(0, "123 Created Address", 1);
        building = this.buildingService.createBuilding(building);
        Assertions.assertNotNull(building);
        Assertions.assertNotEquals(0,building.getBuildingId());
    }

    @Test
    @Order(2)
    void get_all_buildings_test(){
        List<Building> buildingList = this.buildingService.getAllBuildings();
        Assertions.assertNotNull(buildingList);
        Assertions.assertNotEquals(0,buildingList.size());
    }

    @Test
    @Order(3)
    void get_building_by_id_test(){
        try{
            int id = 1;
            Building building = this.buildingService.getBuildingById(id);
            Assertions.assertNotNull(building);
            Assertions.assertEquals(id, building.getBuildingId());

        }catch(BuildingNotFoundException e){
            Assertions.fail();
        }
    }

    @Test
    @Order(4)
    void get_buildings_by_location_test(){
        List<Building> buildingsByLoc = this.buildingService.getBuildingByLocation(testLocation);
        Assertions.assertNotNull(buildingsByLoc);
        for (Building b: buildingsByLoc){
//            Assertions.assertNotNull(b);
            Assertions.assertEquals(testLocation.getLocationId(),b.getLocationId());
        }

//        Assertions.assertNotNull(testLocation);
//        Assertions.assertTrue(buildingsByLoc.size()>1);
    }

    @Test
    @Order(5)
    void update_building(){
        try{
            String updatedAddress = "123 Updated Address";
            Building building = new Building(2, updatedAddress, testLocation.getLocationId());
            building = this.buildingService.updateBuilding(building);
            Assertions.assertNotNull(building);
            Assertions.assertEquals(updatedAddress, building.getAddress());
        }catch(BuildingNotFoundException e){
            Assertions.fail();
        }
    }

    @Test
    @Order(6)
    void get_building_by_id_exception_test(){
        try{
            this.buildingService.getBuildingById(100);
            Assertions.fail();
        }catch(BuildingNotFoundException e){

        }
    }

    @Test
    @Order(7)
    void update_building_exception_test(){
        try{
            this.buildingService.updateBuilding(illegalBuilding);
            Assertions.fail();
        }catch(BuildingNotFoundException e){

        }
    }

}
