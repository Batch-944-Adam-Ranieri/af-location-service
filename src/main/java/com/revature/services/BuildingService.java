package com.revature.services;

import com.revature.entities.Building;
import com.revature.entities.Location;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.exceptions.LocationNotFoundException;

import java.util.List;

public interface BuildingService {

    Building createBuilding(Building building);

    List<Building> getAllBuildings();
    Building getBuildingById(int id) throws BuildingNotFoundException;
    List<Building> getBuildingByLocation(int locationId);

    Building updateBuilding(Building building) throws BuildingNotFoundException;

    boolean deleteBuildingById(int id);

}
