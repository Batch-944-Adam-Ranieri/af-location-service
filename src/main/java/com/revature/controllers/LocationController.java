package com.revature.controllers;


import com.revature.aspects.Verify;
import com.revature.dtos.LocationDto;
import com.revature.dtos.UserDto;
import com.revature.entities.Location;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling Location requests
 * <p>
 *      This controller is designed to handle requests for performing CRUD operations on locations
 *      in the AssignForce database. These requests are intended to be used by Revature's AssignForce web page
 *      to allow the user to track room reservations by building location. The Location objects are a
 *      geographical location of a building that contains rooms used by Revature.
 *
 * @author Stefan Maurer, Hogan Brown, Michael Bennett, Nathan J, Samuel Araga
 * @version 1.0
 */
@Component
@RestController
@CrossOrigin
public class LocationController {

    /**
     * Location Service
     */
    @Autowired
    LocationService locationService;

    private static final String ADMIN = "admin";

    /**
     * Create a new location
     *<p>
     *     Handler method for a post to the locations endpoint. Used to create records
     *     in the Location table of the AssignForce database.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param locationDTO   Location data transfer object provided by the request body.
     *                      Used by the getLocation method to construct a Location object.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<Location> that has a body of the Location object that was created
     */
    @Verify
    @PostMapping("/locations")
    public ResponseEntity<Location> createLocation(UserDto userDto, @RequestBody LocationDto locationDTO, @RequestHeader(name = "Authorization", required = false) String auth) {
        if (userDto.getRole().equals(ADMIN)) {
            Location location = new Location(locationDTO);
            location.setLocationId(0);
            locationService.createLocation(location);
            return ResponseEntity.status(HttpStatus.CREATED).body(location);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Get all locations
     *<p>
     *     Handler method for the get all locations endpoint. Used to retrieve all
     *     location records from the Location table in the AssignForce database.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<List<Location>> that has a body of the list of Location
     * objects that were retrieved from the database.
     */
    @Verify
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAllLocations(UserDto userDto, @RequestHeader(name = "Authorization", required = false) String auth) {

        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.status(HttpStatus.OK).body(locations);
    }

    /**
     * Get location by ID
     *<p>
     *     Handler method for get location by id endpoint. Used to retrieve a
     *     location record from the AssignForce database by a location ID.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param locationId    Unique location ID
     *
     * @return A ResponseEntity<Location> that has a body of the list of Location
     * objects that were retrieved from the database.
     *
     * @throws LocationNotFoundException thrown when the location ID does not exist in
     * the Location table of the AssignForce database
     */
    @Verify
    @GetMapping("/locations/{locationId}")
    public ResponseEntity<Location> getLocationById(UserDto userDto, @PathVariable int locationId) throws LocationNotFoundException {
        try {
            Location location = locationService.getLocationById(locationId);
            return ResponseEntity.status(HttpStatus.OK).body(location);
        } catch (LocationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Update location by ID
     *<p>
     *     Handler method for update location endpoint. Used to update a
     *     location record from the AssignForce database by a location ID.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param locationId    Unique location ID.
     * @param locationDTO   Location data transfer object provided by the request body.
     *                      Used by the getLocation method to construct a Location object.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<Location> that has a body of the list of Location
     * objects that were retrieved from the database.
     *
     * @throws LocationNotFoundException thrown when the location ID does not exist in
     * the Location table of the AssignForce database
     */
    @Verify
    @PutMapping("/locations/{locationId}")
    public ResponseEntity<Location> updateLocation(UserDto userDto,
                                                   @RequestBody LocationDto locationDTO,
                                                   @PathVariable int locationId,
                                                   @RequestHeader(name = "Authorization", required = false) String auth) throws LocationNotFoundException {

        if (userDto.getRole().equals(ADMIN)) {
            try {
                Location location = new Location(locationDTO);
                location.setLocationId(locationId);
                location = locationService.updateLocation(location);
                return ResponseEntity.status(HttpStatus.OK).body(location);
            } catch (LocationNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Delete location by ID
     *<p>
     *     Handler method for update location endpoint. Used to update a
     *     location record from the AssignForce database by a location ID.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param locationId    Unique location ID.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<Boolean> that returns true if the record was deleted
     * and false if the record still exists.
     */
    @Verify
    @DeleteMapping("/locations/{locationId}")
    public ResponseEntity<Boolean> deleteLocation(UserDto userDto, @PathVariable int locationId, @RequestHeader(name = "Authorization", required = false) String auth) {

        if (userDto.getRole().equals(ADMIN)) {
                Boolean result = locationService.deleteLocation(locationId);
                return ResponseEntity.status(HttpStatus.OK).body(result);

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
    }

    /**
     * Health check
     *
     * @return true if service is running correctly, false otherwise.
     */
    @GetMapping("/health")
    public Boolean healthCheck(){
        return true;
    }
}
