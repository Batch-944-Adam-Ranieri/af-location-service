package com.revature.services;

import com.revature.entities.Building;
import com.revature.entities.Location;
import com.revature.exceptions.BuildingNotFoundException;
import com.revature.exceptions.LocationNotFoundException;
import com.revature.repos.BuildingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class BuildingServiceImpl implements BuildingService{

    @Autowired
    BuildingRepo buildingRepo;

//    public BuildingServiceImpl() {
//    }
//
//    public BuildingRepo getBuildingRepo() {
//        return buildingRepo;
//    }
//
//    public void setBuildingRepo(BuildingRepo buildingRepo) {
//        this.buildingRepo = buildingRepo;
//    }

    @Override
    public Building createBuilding(Building building) {
        building.setBuildingId(0);
        this.buildingRepo.save(building);
        return building;
    }

    @Override
    public List<Building> getAllBuildings() {
        return (List<Building>) this.buildingRepo.findAll();
    }

    @Override
    public Building getBuildingById(int id) throws BuildingNotFoundException {
        Building building;
        Optional<Building> op = buildingRepo.findById(id);

        if(op.isPresent()) {
            building = op.get();
            System.out.println(building);
        }else{
            throw new BuildingNotFoundException();
        }
        return building;
    }

    @Override
    public List<Building> getBuildingByLocation(int locationId){

        List<Building> buildings = buildingRepo.findBuildingsByLocationId(locationId);

        return buildings;
//        if(op.isPresent()) {
//            location = op.get();
//            System.out.println(location);
//        }else{
//            throw new LocationNotFoundException();
//        }
//        return location;
    }

    @Override
    public Building updateBuilding(Building building) throws BuildingNotFoundException {

        Optional<Building> op = buildingRepo.findById(building.getBuildingId());
        if (!op.isPresent())
            throw new BuildingNotFoundException();
        Building oldBuilding = op.get();
        if (building.getAddress() != null)
            oldBuilding.setAddress(building.getAddress());
        Building updatedBuilding = buildingRepo.save(oldBuilding);
        return updatedBuilding;
    }

    @Override
    public boolean deleteBuildingById(int id) {
//        try{
//            this.buildingRepo.delete(this.buildingRepo.findById(id).orElse(null));
//            return true;
//        }catch(Exception e){
//            e.printStackTrace();
//            return false;
//        }
        try {
            this.buildingRepo.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
