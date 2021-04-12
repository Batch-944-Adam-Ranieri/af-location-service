package com.revature.controllers;


import com.revature.aspects.Verify;
import com.revature.dtos.BuildingDto;
import com.revature.dtos.UserDto;
import com.revature.entities.Building;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * Controller for endpoints concerned with buildings in the database
 * </p>
 * @author Stefan Maurer, Hogan Brown, Michael Bennett, Nathan J, Samuel Araga
 * @version 1.0
 */
@Component
@RestController
@CrossOrigin
public class BuildingController {

    @Autowired
    BuildingService buildingService;
    static final String ADMIN = "admin";

    /**
     *<p>
     *     Handler method for a post to the buildings endpoint. Used to create
     *     building entries in the database.
     *</p>
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param locationId    Location Id provided in the URI path. Assigned to the new
     *                      Building object returned by the getBuilding method.
     * @param buildingDTO   Building data transfer object provided by the request body.
     *                      Used by the getBuilding method to construct a Building object.
     * @param auth          Authentication JWT provided by the request header.
     * @return A ResponseEntity<Building> that has a body of the Building object that was created
     * @see com.revature.aspects.SecurityAspect
     * @see com.revature.entities.Building
     * @see com.revature.dtos.BuildingDto
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

    @Verify
    @GetMapping("/locations/{locationId}/buildings")
    public ResponseEntity<List<Building>> getAllBuildings(UserDto userDto,
                                                          @PathVariable int locationId,
                                                          @RequestHeader(name = "Authorization", required = false) String auth) {

        List<Building> buildings = buildingService.getBuildingByLocation(locationId);
        return ResponseEntity.status(HttpStatus.OK).body(buildings);
    }

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
