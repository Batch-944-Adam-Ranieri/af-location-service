package com.revature.services;


import com.revature.entities.Location;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.repos.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the interface LocationService
 */
@Component
@Service
public class LocationServiceImpl implements LocationService{

    /**
     * Location Repo injection
     */
    @Autowired
    LocationRepo locationRepo;

    /**
     * Uses the location repo to create a new record in the Location table
     * of the AssignForce database by setting the ID of the new Location
     * record to zero.
     *
     * @param location      Transient Location object
     * @return              Detached Location object that was persisted
     */
    @Override
    public Location createLocation(Location location) {
        location.setLocationId(0);
        this.locationRepo.save(location);
        return location;
    }

    /**
     * Uses the location repo to find a record in the Location table of
     * the AssignForce database by location ID.
     *
     * @param id            ID of the Building to retrieve from the database
     * @return              Building object
     * @throws LocationNotFoundException thrown if there are no location
     * records that with a matching locationId field
     */
    @Override
    public Location getLocationById(int id) throws LocationNotFoundException {

        Location location;
        Optional<Location> op = locationRepo.findById(id);

        if(op.isPresent()) {
            location = op.get();
        }else{
            throw new LocationNotFoundException();
        }
        return location;
    }

    /**
     * Uses the location repo to get all locations persisted in the
     * Location table of the AssignForce database.
     *
     * @return              List of Location objects
     */
    @Override
    public List<Location> getAllLocations() {

        return (List<Location>) this.locationRepo.findAll();
    }

    /**
     * Alters a record in the Location table of the AssignForce database
     * with the same locationId as the Location object passed to it.
     *
     * @param location      Transient Location object
     * @return              Updated (detached) Location object
     * @throws LocationNotFoundException thrown when there are no location
     * records that have a locationId matching the Location object parameter
     */
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

        if (location.getZipcode() != null)
            oldLocation.setZipcode(location.getZipcode());

        if (location.getName() != null)
            oldLocation.setName(location.getName());

        return locationRepo.save(oldLocation);
    }

    /**
     * Deletes a record from the Location table of the AssignForce database
     * with a location ID that matches the id parameter.
     *
     * @param id            ID of the Location record to delete
     * @return              True if the location was deleted, false if an
     *                      exception was thrown
     */
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
