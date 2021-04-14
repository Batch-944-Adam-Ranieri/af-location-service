package com.revature.repos;

import com.revature.entities.Building;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repo for the entity Building. Executes queries specified and the implicit ones.
 */
@Component
@Repository
public interface BuildingRepo extends CrudRepository<Building, Integer> {
    /**
     * Gets all the Building objects for a location based on the location id
     * @param id id of a locaion
     * @return List of Building object for a location based on location id
     */
    @Query("from Building b where b.locationId = :id")
    List<Building> findByLocationIdEquals(int id);
}
