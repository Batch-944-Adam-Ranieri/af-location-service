package com.revature.repos;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Location;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes= AfLocationServiceApplication.class)
class LocationRepoTests {

    @Autowired
    LocationRepo locationRepo;

    @Test
    @Order(1)
    void create_location_test(){
        Location loc1 = new Location(0,"new test city","new test state", "new test zip");

        loc1 = this.locationRepo.save(loc1);

        Assertions.assertNotEquals(0,loc1.getLocationId());
    }

    @Test
    @Order(2)
    void get_all_locations_test(){
        List<Location> locations = (ArrayList<Location>)this.locationRepo.findAll();

        Assertions.assertTrue(locations.size()>1);
    }

    @Test
    @Order(3)
    void get_location_by_id(){
        Location location = this.locationRepo.findById(2).orElse(null);

        Assertions.assertTrue(location!=null);
    }

    @Test
    @Order(4)
    void update_location_by_id(){
        Location newLocation = new Location(1, "Updated city","updated state","updated zip");
        Location newLoc = this.locationRepo.save(newLocation);

        Assertions.assertEquals("Updated city", newLoc.getCity());
    }

    @Test
    @Order(5)
    void delete_location_by_id(){
        int id = 2;
        this.locationRepo.deleteById(id);
        Location result = this.locationRepo.findById(id).orElse(null);
        Assertions.assertNull(result);
    }


}
