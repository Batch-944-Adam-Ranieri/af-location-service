package com.revature.services;

import com.revature.entities.Building;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.repos.BuildingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the interface BuildingService.
 */
@Component
@Service
public class BuildingServiceImpl implements BuildingService {

    /**
     * Building Repo injection
     */
    @Autowired
    BuildingRepo buildingRepo;

    /**
     * Uses the building repo to create a new record in the Building table
     * of the AssignForce database by setting the ID of the new Building
     * record to zero.
     *
     * @param building      Transient building
     * @return              Detached Building object that was persisted
     */
    @Override
    public Building createBuilding(Building building) {
        building.setBuildingId(0);
        this.buildingRepo.save(building);
        return building;
    }

    /**
     * Uses the building repo to get all buildings persisted in the
     * Building table of the AssignForce database.
     *
     * @return              List of Building objects
     */
    @Override
    public List<Building> getAllBuildings() {
        return (List<Building>) this.buildingRepo.findAll();
    }

    /**
     * Uses the building repo to find a record in the Building table of
     * the AssignForce database by building ID.
     *
     * @param id            ID of the Building to retrieve from the database
     * @return              Building object
     * @throws BuildingNotFoundException thrown if there are no building
     * records that with a matching buildingId field
     */
    @Override
    public Building getBuildingById(int id) throws BuildingNotFoundException {
        Building building;
        Optional<Building> op = buildingRepo.findById(id);

        if (op.isPresent())
            building = op.get();
        else
            throw new BuildingNotFoundException();

        return building;
    }

    /**
     * Uses the building repo to retrieve all records in the building table
     * of the AssignForce database that have a matching location ID.
     *
     * @param locationId    ID of the Location to get buildings from
     * @return              List of Building Objects
     */
    @Override
    public List<Building> getBuildingByLocation(int locationId) {
        return buildingRepo.findByLocationIdEquals(locationId);
    }

    /**
     * Alters a record in the Building table of the AssignForce database
     * with the same buildingId as the building object passed to it.
     *
     * @param building      Building object to replace with in the database
     * @return              Updated Building object
     * @throws BuildingNotFoundException thrown when there are no building
     * records that have a buildingId matching the Building object parameter
     */
    @Override
    public Building updateBuilding(Building building) throws BuildingNotFoundException {

        Optional<Building> op = buildingRepo.findById(building.getBuildingId());

        if (!op.isPresent())
            throw new BuildingNotFoundException();

        Building oldBuilding = op.get();

        if (building.getAddress() != null)
            oldBuilding.setAddress(building.getAddress());

        return buildingRepo.save(oldBuilding);
    }

    /**
     * Deletes a record from the Building table of the AssignForce database
     * with a building ID that matches the id parameter.
     *
     * @param id            ID of the Building record to delete
     * @return              True if the building was deleted, false if an
     *                      exception was thrown
     */
    @Override
    public boolean deleteBuildingById(int id) {
        try {
            this.buildingRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
