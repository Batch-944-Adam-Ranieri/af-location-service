package com.revature.repos;

import com.revature.entities.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Repo for the entity Location. Executes implicit queries.
 */
@Component
@Repository
public interface LocationRepo extends CrudRepository<Location, Integer> {
}
