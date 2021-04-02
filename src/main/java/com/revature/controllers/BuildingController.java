package com.revature.controllers;


import com.revature.dtos.BuildingDto;
import com.revature.entities.Building;
import com.revature.services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Component
@RestController
public class BuildingController {

    @Autowired
    BuildingService buildingService;

    @PostMapping("/locations/{locationId}/buildings")
    public ResponseEntity<Building> createBuilding(@PathVariable int locationId, @RequestBody BuildingDto buildingDTO){
        System.out.println("createBuilding controller called");
        Building building = getBuilding(buildingDTO, locationId);

        try{
            System.out.println(building);
            this.buildingService.createBuilding(building);
            System.out.println(building);
            return ResponseEntity.status(HttpStatus.CREATED).body(building);

        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/locations/{locationId}/buildings")
    public ResponseEntity<List<Building>> getAllBuildings(@PathVariable int locationId){

        try{
            List<Building> buildings = this.buildingService.getBuildingByLocation(locationId);
            return ResponseEntity.status(HttpStatus.OK).body(buildings);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/locations/{locationId}/buildings/{buildingId}")
    public ResponseEntity<Building> getBuildingById(@PathVariable int locationId, @PathVariable int buildingId){

        try{
            Building building = this.buildingService.getBuildingById(buildingId);
            return ResponseEntity.status(HttpStatus.OK).body(building);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            //TODO return error message for BuildingNotFoundException
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    @PutMapping("/locations/{locationId}/buildings/{buildingId}")
    public ResponseEntity<Building> updateBuilding(@PathVariable int locationId, @PathVariable int buildingId, @RequestBody BuildingDto buildingDTO){

        try{
            Building building = this.getBuilding(buildingDTO, locationId);
            building = this.buildingService.updateBuilding(building);
            return ResponseEntity.status(HttpStatus.OK).body(building);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/locations/{locationId}/buildings/{buildingId}")
    public ResponseEntity<Building> deleteBuilding(@PathVariable int locationId, @PathVariable int buildingId){

        try{
            this.buildingService.deleteBuildingById(buildingId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    private Building getBuilding(BuildingDto dto, int locationId){
        Building building = new Building();
        building.setBuildingId(dto.getBuildingId());
        building.setAddress(dto.getAddress());
        building.setLocationId(locationId);
        //building.setRooms(dto.getRooms());
        return building;
    }

}
