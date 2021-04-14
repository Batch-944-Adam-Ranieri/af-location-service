package com.revature.services;

import com.revature.entities.Building;
import com.revature.exceptions.BuildingNotFoundException;

import java.util.List;

/**
 * Service interface for Crud operations on Building objects
 */
public interface BuildingService {

    Building createBuilding(Building building);

    List<Building> getAllBuildings();
    Building getBuildingById(int id) throws BuildingNotFoundException;
    List<Building> getBuildingByLocation(int locationId);

    Building updateBuilding(Building building) throws BuildingNotFoundException;

    boolean deleteBuildingById(int id);

}
