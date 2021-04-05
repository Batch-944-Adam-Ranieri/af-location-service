package com.revature.controllers;


import com.revature.aspects.Verify;
import com.revature.dtos.BuildingDto;
import com.revature.dtos.UserDto;
import com.revature.entities.Building;
import com.revature.entities.Location;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Component
@RestController
public class BuildingController {

    @Autowired
    BuildingService buildingService;

    UserDto userDto;

    @Verify
    @PostMapping("/locations/{locationId}/buildings")
    public ResponseEntity<Building> createBuilding(UserDto userDto, @PathVariable int locationId,
                                                   @RequestBody BuildingDto buildingDTO,
                                                   @RequestHeader(name = "Authorization", required = false) String auth) {
        if (userDto.getRole().equals("admin")) {
            Building building = getBuilding(buildingDTO, locationId);
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
            if (userDto.getRole().equals("admin")) {
                Building building = getBuilding(buildingDTO, locationId);
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
            if (userDto.getRole().equals("admin")) {
                boolean result = buildingService.deleteBuildingById(buildingId);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

            }
        }

        private Building getBuilding (BuildingDto dto,int locationId){
            Building building = new Building();
            building.setBuildingId(dto.getBuildingId());
            building.setAddress(dto.getAddress());
            building.setLocationId(locationId);
            return building;
        }

    }
