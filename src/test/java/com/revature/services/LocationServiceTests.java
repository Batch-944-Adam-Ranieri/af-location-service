package com.revature.services;

import com.revature.AFLocationService.AfLocationServiceApplication;
import com.revature.entities.Location;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.repos.LocationRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = AfLocationServiceApplication.class)
@ExtendWith(MockitoExtension.class)
public class LocationServiceTests {

    @InjectMocks
    static LocationServiceImpl locationService;


    static Location testLocation;
    static List<Location> testLocationList;
    static Location illegalLocation;

    @Mock
    static LocationRepo locationRepo = Mockito.mock(LocationRepo.class);

    @BeforeAll
    static void setUp() {

        locationService = new LocationServiceImpl();


        testLocationList = new ArrayList<Location>();


        for (int i = 1; i < 5; i++) {
            Location location = new Location(i, "testcity" + i, "teststate" + i, "testzip" + i);
            if (i == 1) {
                testLocation = location;
            }
            testLocationList.add(location);
            System.out.println(location);
            System.out.println("adjfkjahf");

        }

        illegalLocation = new Location(50,"madison","wi","53704");

        Mockito.when(locationRepo.findAll()).thenReturn(testLocationList);
        Mockito.when(locationRepo.findById(1)).thenReturn(Optional.of(testLocation));
//        Mockito.when(locationRepo.findById(1).get()).thenReturn(testLocation);
//        Mockito.when(locationRepo.findById(1)).thenReturn(Optional.empty());


        Mockito.when(locationRepo.save(any())).thenReturn(testLocation);
        Mockito.when(locationRepo.findById(100)).thenReturn(Optional.empty());
        Mockito.when(locationRepo.findById(50)).thenReturn(Optional.empty());



    }

    @Order(9)
    @Test
    void create_location() {
        Location location = new Location(5,"Test City","Test State","20033");
        location = locationService.createLocation(location);
        System.out.println(location);
        Assertions.assertNotNull(location);
        Assertions.assertEquals(0,location.getLocationId());
    }

    @Order(2)
    @Test
    void get_location_by_id_test() {

        try {
            System.out.println(testLocation);
            int id = testLocation.getLocationId();
            Location location = locationService.getLocationById(id);

            System.out.println(locationService.getLocationById(id));
           // System.out.println(location);
//            Assertions.assertNotNull(location);

            Assertions.assertEquals(1, location.getLocationId());
        } catch (LocationNotFoundException e) {
            Assertions.fail();
        }


    }

    @Order(3)
    @Test
    void get_all_locations_test() {
        List<Location> locations = locationService.getAllLocations();
        System.out.println(locations);
        Assertions.assertNotNull(locations);
        Assertions.assertTrue(locations.size() > 1);

    }

    @Order(4)
    @Test
    void update_location_test() {

        Location location = testLocation;
        location.setCity("new test city");
        try {
            location = locationService.updateLocation(location);
            System.out.println(location);
            Assertions.assertNotNull(location);
            Assertions.assertEquals("new test city", location.getCity());

        } catch (LocationNotFoundException e) {
            Assertions.fail();
        }
    }


    @Order(10)
    @Test
    void delete_location() {
        Assertions.assertTrue(locationService.deleteLocation(1));
    }

    @Order(6)
    @Test
    void get_location_by_id_exception_test() {
        try {
            Location location = locationService.getLocationById(100);
            System.out.println(location);
            Assertions.fail();
        } catch (LocationNotFoundException e) {
        }

    }

    @Order(7)
    @Test
    void update_location_exception_test() {
        try {
            Location location = locationService.updateLocation(illegalLocation);
            System.out.println(location);
            Assertions.fail();
        } catch (LocationNotFoundException e) {
        }
    }


}
