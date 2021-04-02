package com.revature.controllers;


import com.revature.dtos.LocationDto;
import com.revature.entities.Location;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Component
@RestController
public class LocationController {

    @Autowired
    LocationService locationService;


    @PostMapping("/locations")
    public ResponseEntity<Location> createLocation(@RequestBody LocationDto locationDTO,@RequestHeader(name = "Authorization",required = false) String auth) {

        if(auth == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if(auth.equals("Authorized")) {
            Location location = locationTransfer(locationDTO);
            location.setLocationId(0);

            locationService.createLocation(location);

            return ResponseEntity.status(201).body(location);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAllLocations(){

        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.status(200).body(locations);
    }

    @GetMapping("/locations/{locationId}")
    public ResponseEntity<Location> getLocationById(@PathVariable int locationId) throws LocationNotFoundException {
        try {
            Location location = locationService.getLocationById(locationId);
            return ResponseEntity.status(200).body(location);
        }catch(LocationNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/locations/{locationId}")
    public ResponseEntity<Location> updateLocation(@RequestBody LocationDto locationDTO,
                                                   @PathVariable int locationId,
                                                   @RequestHeader(name = "Authorization",required = false) String auth) throws LocationNotFoundException {
        if(auth==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }else if(auth.equals("Authorized")) {
            try {
                Location location = locationTransfer(locationDTO);
                location = locationService.updateLocation(location);
                return ResponseEntity.status(200).body(location);
            }catch(LocationNotFoundException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            }
        }else{

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
    }

    @DeleteMapping("/locations/{locationId}")
    public ResponseEntity<String> deleteLocation(@PathVariable int locationId,@RequestHeader(name="Authorization",required = false) String auth) throws LocationNotFoundException {

        if(auth ==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }else if(auth.equals("Authorized")) {

            Location location = locationService.getLocationById(locationId);
            locationService.deleteLocation(locationId);
            return ResponseEntity.status(200).body("Deleted location");
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
    }

    public Location locationTransfer(LocationDto locationDTO){
        Location location = new Location();
        location.setCity(locationDTO.getCity());
        location.setState(locationDTO.getState());
        location.setZipcode(locationDTO.getZipcode());
        location.setBuildings(locationDTO.getBuildings());
        return location;
    }
}
