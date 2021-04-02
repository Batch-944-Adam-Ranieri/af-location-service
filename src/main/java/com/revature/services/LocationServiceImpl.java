package com.revature.services;


import com.revature.entities.Location;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.repos.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@Service
public class LocationServiceImpl implements LocationService{

    @Autowired
    LocationRepo locationRepo;

//    public LocationServiceImpl() {
//    }
//
//    public LocationRepo getLocationRepo() {
//
//        return locationRepo;
//    }
//
//    public void setLocationRepo(LocationRepo locationRepo) {
//        this.locationRepo = locationRepo;
//    }

    @Override
    public Location createLocation(Location location) {
        location.setLocationId(0);
        this.locationRepo.save(location);
        return location;
    }

    @Override
    public Location getLocationById(int id) throws LocationNotFoundException {

        Location location;
        Optional<Location> op = locationRepo.findById(id);

        if(op.isPresent()) {
            location = op.get();
            System.out.println(location);
        }else{
            throw new LocationNotFoundException();
        }
        return location;

//        return location;
    }


    @Override
    public List<Location> getAllLocations() {

        return (List<Location>) this.locationRepo.findAll();
    }

    @Override
    public Location updateLocation(Location location) throws LocationNotFoundException {

        Optional<Location> op = locationRepo.findById(location.getLocationId());
        if (!op.isPresent())
            throw new LocationNotFoundException();
        Location oldLocation = op.get();
        if (location.getCity() != null)
            oldLocation.setCity(location.getCity());
        if (location.getState() != null)
            oldLocation.setState(location.getState());
        Location updatedLocation = locationRepo.save(oldLocation);
        return updatedLocation;
    }

    @Override
    public boolean deleteLocation(int id) {

        try{
            this.locationRepo.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
