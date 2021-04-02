package com.revature.controllers;


import com.revature.dtos.BuildingDto;
import com.revature.entities.Building;
import com.revature.exceptions.BuildingNotFoundException;
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

    @PostMapping("/locations/{locationId}/buildings")
    public ResponseEntity<Building> createBuilding(@PathVariable int locationId, @RequestBody BuildingDto buildingDTO, @RequestHeader(name = "Authorization", required = false) String auth){

        if (auth == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else if(auth.equals("Authorized")){
            Building building = getBuilding(buildingDTO, locationId);
            this.buildingService.createBuilding(building);
            return ResponseEntity.status(HttpStatus.CREATED).body(building);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/locations/{locationId}/buildings")
    public ResponseEntity<List<Building>> getAllBuildings(@PathVariable int locationId, @RequestHeader(name = "Authorization", required = false) String auth){
        if (auth == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else if(auth.equals("Authorized")){
            List<Building> buildings = this.buildingService.getBuildingByLocation(locationId);
            return ResponseEntity.status(HttpStatus.OK).body(buildings);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/locations/{locationId}/buildings/{buildingId}")
    public ResponseEntity<Building> getBuildingById(@PathVariable int locationId, @PathVariable int buildingId, @RequestHeader(name = "Authorization", required = false) String auth){
        if (auth == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else if(auth.equals("Authorized")){
            try {
                Building building = this.buildingService.getBuildingById(buildingId);
                if (building == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(building);
                }
                return ResponseEntity.status(HttpStatus.OK).body(building);
            }
            catch (BuildingNotFoundException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PutMapping("/locations/{locationId}/buildings/{buildingId}")
    public ResponseEntity<Building> updateBuilding(@PathVariable int locationId, @PathVariable int buildingId, @RequestBody BuildingDto buildingDTO, @RequestHeader(name = "Authorization", required = false) String auth){
        if (auth == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else if(auth.equals("Authorized")){
            try {
                Building building = this.getBuilding(buildingDTO, locationId);
                building = this.buildingService.updateBuilding(building);
                return ResponseEntity.status(HttpStatus.OK).body(building);
            }
            catch (BuildingNotFoundException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

    }

    @DeleteMapping("/locations/{locationId}/buildings/{buildingId}")
    public ResponseEntity<Building> deleteBuilding(@PathVariable int locationId, @PathVariable int buildingId, @RequestHeader(name = "Authorization", required = false) String auth){
        if (auth == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else if(auth.equals("Authorized")){
            this.buildingService.deleteBuildingById(buildingId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    private Building getBuilding(BuildingDto dto, int locationId){
        Building building = new Building();
        building.setBuildingId(dto.getBuildingId());
        building.setAddress(dto.getAddress());
        building.setLocationId(locationId);
        return building;
    }

}
