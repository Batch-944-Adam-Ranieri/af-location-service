package com.revature.controllers;


import com.revature.entities.Location;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@Component
@RestController
public class LocationController {

    @Autowired
    LocationService locationService;


    @PostMapping
    public Location getLocationById(@PathVariable int id) throws IOException {
//        try {
//            Location location = this.locationService.getLocationById(id);
//
//        }
//        catch (LocationNotFoundException e){
//
//        }
        return null;
    }
}
