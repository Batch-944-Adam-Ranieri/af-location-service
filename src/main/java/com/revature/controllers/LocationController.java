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


@Component
@RestController
public class LocationController {

    @Autowired
    LocationService locationService;

    @Verify
    @PostMapping("/locations")
    public ResponseEntity<Location> createLocation(UserDto userDto, @RequestBody LocationDto locationDTO, @RequestHeader(name = "Authorization", required = false) String auth) {
        if (userDto.getRole().equals("admin")) {
            Location location = locationTransfer(locationDTO);
            location.setLocationId(0);
            locationService.createLocation(location);
            return ResponseEntity.status(HttpStatus.CREATED).body(location);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Verify
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAllLocations(UserDto userDto, @RequestHeader(name = "Authorization", required = false) String auth) {

        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.status(HttpStatus.OK).body(locations);
    }

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

    @Verify
    @PutMapping("/locations/{locationId}")
    public ResponseEntity<Location> updateLocation(UserDto userDto,
                                                   @RequestBody LocationDto locationDTO,
                                                   @PathVariable int locationId,
                                                   @RequestHeader(name = "Authorization", required = false) String auth) throws LocationNotFoundException {

        if (userDto.getRole().equals("admin")) {
            try {
                Location location = locationTransfer(locationDTO);
                location = locationService.updateLocation(location);
                return ResponseEntity.status(HttpStatus.OK).body(location);
            } catch (LocationNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Verify
    @DeleteMapping("/locations/{locationId}")
    public ResponseEntity<String> deleteLocation(UserDto userDto, @PathVariable int locationId, @RequestHeader(name = "Authorization", required = false) String auth) {

        if (userDto.getRole().equals("admin")) {
            try {
                Location location = locationService.getLocationById(locationId);
                locationService.deleteLocation(locationId);
                return ResponseEntity.status(HttpStatus.OK).body("Deleted location");
            } catch (LocationNotFoundException e) {
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
    }

    public Location locationTransfer(LocationDto locationDTO) {
        Location location = new Location();
        location.setCity(locationDTO.getCity());
        location.setState(locationDTO.getState());
        location.setZipcode(locationDTO.getZipcode());
        location.setBuildings(locationDTO.getBuildings());
        return location;
    }
}
