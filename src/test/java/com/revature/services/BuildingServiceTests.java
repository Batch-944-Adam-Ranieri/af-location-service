package com.revature.services;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Building;
import com.revature.entities.Location;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.repos.BuildingRepo;
import com.revature.repos.LocationRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = AfLocationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuildingServiceTests {

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
    void setup() {

        testBuildingList = new ArrayList<Building>();
        for (int i = 1; i < 5; i++) {
            Building building = new Building(i, "test " + i, 1);
            if (i == 1) {
                testBuilding = building;
            }
            testBuildingList.add(building);
        }

        illegalBuilding = new Building(100, "Illegal Building Address", 100);
        testLocation = new Location(1, "Dallas", "TX", "75019");
        illegalLocation = new Location(100, "Illegal City", "Illegal State", "illegalzip");

    }

    @Test
    @Order(0)
    void testMocking() {
        Mockito.when(buildingrepo.findById(1)).thenReturn(java.util.Optional.of(testBuilding));

        Optional<Building> o = buildingrepo.findById(1);
        Building l = o.orElse(null);
        Assertions.assertNotNull(l);

        Assertions.assertNotNull(buildingrepo.findById(1).orElse(null));
    }

    @Test
    @Order(1)
    void create_building_test() {
        Mockito.when(buildingrepo.save(testBuilding)).thenReturn(testBuilding);

        Building building = new Building(0, "123 Created Address", 1);
        building = this.buildingService.createBuilding(building);
        Assertions.assertNotNull(building);
        Assertions.assertEquals(0, building.getBuildingId());
    }

    @Test
    @Order(2)
    void get_all_buildings_test() {
        Mockito.when(buildingrepo.findAll()).thenReturn(testBuildingList);

        List<Building> buildingList = this.buildingService.getAllBuildings();
        Assertions.assertNotNull(buildingList);
        Assertions.assertNotEquals(0, buildingList.size());
    }

    @Test
    @Order(3)
    void get_building_by_id_test() {
        Mockito.when(buildingrepo.findById(1)).thenReturn(Optional.of(testBuilding));

        try {
            int id = testBuilding.getBuildingId();
            Building building = this.buildingService.getBuildingById(id);
            Assertions.assertNotNull(building);
            Assertions.assertEquals(1, building.getBuildingId());

        } catch (BuildingNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    @Order(4)
    void get_buildings_by_location_test() {
        int id = testLocation.getLocationId();
        Mockito.when(buildingrepo.findByLocationIdEquals(id)).thenReturn(testBuildingList);

        List<Building> buildingsByLoc = this.buildingService.getBuildingByLocation(id);
        Assertions.assertNotNull(buildingsByLoc);
        for (Building b : buildingsByLoc) {
            Assertions.assertEquals(id, b.getLocationId());
        }
    }

    @Test
    @Order(5)
    void update_building() {
        Mockito.when(buildingrepo.findById(testBuilding.getBuildingId())).thenReturn(java.util.Optional.of(testBuilding));
        Mockito.when(buildingrepo.save(testBuilding)).thenReturn(testBuilding);
        try {
            String updatedAddress = "Updated Address";
            System.out.println(testBuilding);
            Building building = new Building(testBuilding.getBuildingId(), updatedAddress, testLocation.getLocationId());
            building = this.buildingService.updateBuilding(building);
            Assertions.assertNotNull(building);
            Assertions.assertEquals(updatedAddress, building.getAddress());
        } catch (BuildingNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    @Order(6)
    void get_building_by_id_exception_test() {
        Mockito.when(buildingrepo.findById(100)).thenReturn(Optional.empty());

        try {
            this.buildingService.getBuildingById(100);
            Assertions.fail();
        } catch (BuildingNotFoundException e) {

        }
    }

    @Test
    @Order(7)
    void update_building_exception_test() {
        Mockito.when(buildingrepo.findById(100)).thenReturn(Optional.empty());
        Mockito.when(buildingrepo.save(any())).thenReturn(illegalBuilding);

        try {
            this.buildingService.updateBuilding(illegalBuilding);
            Assertions.fail();
        } catch (BuildingNotFoundException e) {

        }
    }

    @Test
    @Order(8)
    void delete_building() {
        Assertions.assertTrue(buildingService.deleteBuildingById(testBuilding.getBuildingId()));
    }


}
