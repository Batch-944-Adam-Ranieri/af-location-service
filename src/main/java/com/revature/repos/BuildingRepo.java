package com.revature.repos;

import com.revature.entities.Building;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface BuildingRepo extends CrudRepository<Building, Integer> {
    @Query("from Building b where b.locationId = :id")
    List<Building> findByLocationIdEquals(int id);
}
