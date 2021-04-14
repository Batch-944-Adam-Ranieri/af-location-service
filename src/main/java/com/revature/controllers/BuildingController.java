package com.revature.controllers;


import com.revature.aspects.Verify;
import com.revature.dtos.BuildingDto;
import com.revature.dtos.UserDto;
import com.revature.entities.Building;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling Building requests
 * <p>
 *      This controller is designed to handle requests for performing CRUD operations on buildings
 *      in the AssignForce database. These requests are intended to be used by Revature's AssignForce web page
 *      to allow the user to track room reservations by building location. The Building objects contain the
 *      address of a building that contains rooms used by Revature.
 *
 * @author Stefan Maurer, Hogan Brown, Michael Bennett, Nathan J, Samuel Araga
 * @version 1.0
 */
@Component
@RestController
@CrossOrigin
public class BuildingController {

    /**
     * Building Service Injection
     */
    @Autowired
    BuildingService buildingService;
    static final String ADMIN = "admin";

    /**
     * Create a new building
     *<p>
     *     Handler method for a post to the buildings endpoint. Used to create
     *     building entries in the database.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param locationId    Location Id provided in the URI path. Assigned to the new
     *                      Building object returned by the getBuilding method.
     * @param buildingDTO   Building data transfer object provided by the request body.
     *                      Used by the getBuilding method to construct a Building object.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<Building> that has a body of the Building object that was created
     */
    @Verify
    @PostMapping("/locations/{locationId}/buildings")
    public ResponseEntity<Building> createBuilding(UserDto userDto, @PathVariable int locationId,
                                                   @RequestBody BuildingDto buildingDTO,
                                                   @RequestHeader(name = "Authorization", required = false) String auth) {
        if (userDto.getRole().equals(ADMIN)) {
            Building building = new Building(buildingDTO);
            building.setBuildingId(0);
            this.buildingService.createBuilding(building);
            return ResponseEntity.status(HttpStatus.CREATED).body(building);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

    }


    /**
     * Get all buildings
     *<p>
     *     Handler method for the get all buildings endpoint. Used to retrieve all
     *     building records at a given location from the Building table in the
     *     AssignForce database.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param locationId    Location Id provided in the URI path. Assigned to the new
     *                      Building object returned by the getBuilding method.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<List<Location>> that has a body of the list of Location
     * objects that were retrieved from the database.
     */
    @Verify
    @GetMapping("/locations/{locationId}/buildings")
    public ResponseEntity<List<Building>> getAllBuildings(UserDto userDto,
                                                          @PathVariable int locationId,
                                                          @RequestHeader(name = "Authorization", required = false) String auth) {

        List<Building> buildings = buildingService.getBuildingByLocation(locationId);
        return ResponseEntity.status(HttpStatus.OK).body(buildings);
    }

    /**
     * Get building by ID
     *<p>
     *     Handler method for get building by id endpoint. Used to retrieve a
     *     building record at a given location from the AssignForce database
     *     by a building ID and location ID.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param buildingId    Unique building ID
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<Building> that has a body of the building object
     * retrieved from the database.
     */
    @Verify
    @GetMapping("/locations/{locationId}/buildings/{buildingId}")
    public ResponseEntity<Building> getBuildingById(UserDto userDto,
                                                    @PathVariable int buildingId,
                                                    @RequestHeader(name = "Authorization", required = false) String auth) {
        Building building = null;
        try {
            building = buildingService.getBuildingById(buildingId);
        } catch (BuildingNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(building);
    }

    /**
     * Update building by ID
     *<p>
     *     Handler method for update building endpoint. Used to update a
     *     building record from the AssignForce database by a building ID
     *     and location ID.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param locationId    Unique location ID.
     * @param buildingDTO   Building data transfer object provided by the request body.
     *                      Used by the getBuilding method to construct a Building object.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<Building> that has a body of a building object retrieved
     * from the AssignForce database.
     */
    @Verify
    @PutMapping("/locations/{locationId}/buildings/{buildingId}")
    public ResponseEntity<Building> updateBuilding(UserDto userDto,
                                                   @PathVariable int locationId, @PathVariable int buildingId,
                                                   @RequestBody BuildingDto buildingDTO,
                                                   @RequestHeader(name = "Authorization", required = false) String auth) {
        try {
            if (userDto.getRole().equals(ADMIN)) {
                Building building = new Building(buildingDTO);
                building.setBuildingId(buildingId);
                building.setLocationId(locationId);
                this.buildingService.updateBuilding(building);
                return ResponseEntity.status(HttpStatus.OK).body(building);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (BuildingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Delete building by ID
     *<p>
     *     Handler method for update location endpoint. Used to update a
     *     location record from the AssignForce database by a building ID
     *     and location ID.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param locationId    Unique location ID.
     * @param buildingId    Unique building ID.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<Boolean> that returns true if the record was deleted
     * and false if the record still exists.
     */
    @Verify
    @DeleteMapping("/locations/{locationId}/buildings/{buildingId}")
    public ResponseEntity<Boolean> deleteBuilding (UserDto userDto,
                                                    @PathVariable int locationId, @PathVariable int buildingId,
                                                    @RequestHeader(name = "Authorization", required = false) String auth){
        if (userDto.getRole().equals(ADMIN)) {
            boolean result = buildingService.deleteBuildingById(buildingId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
    }
}
