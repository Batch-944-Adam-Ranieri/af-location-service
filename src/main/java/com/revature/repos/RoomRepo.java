package com.revature.repos;

import com.revature.entities.Room;
import com.revature.entities.RoomType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repo for the entity Room. Executes queries specified and the implicit ones
 */
@Component
@Repository
public interface RoomRepo extends CrudRepository<Room, Integer> {
    /**
     * Gets all the Room objects from a building based on building id
     * @param buildingId Unique id of a building.
     * @return List of Room objects within the building based on building id.
     */
    List<Room> findRoomByBuildingId(int buildingId);

    /**
     * @param type a type of a room
     * @param buildingId unique id of a building
     * @return List of a particular type of Room objects based on room type from a building with a particular building id
     */
    List<Room> findRoomByTypeAndBuildingId(RoomType type, int buildingId);

    /**
     * @return List of all the virtual rooms regardless of the location or buildings
     */
    @Query("FROM Room r WHERE r.type = 'VIRTUAL'")
    List<Room> findVirtualRooms();
}
