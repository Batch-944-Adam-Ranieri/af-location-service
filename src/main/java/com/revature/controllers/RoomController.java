package com.revature.controllers;

import com.revature.aspects.Verify;
import com.revature.dtos.RoomDto;
import com.revature.dtos.UserDto;
import com.revature.entities.Room;
import com.revature.entities.RoomType;
import com.revature.exceptions.RoomNotFoundException;
import com.revature.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling Room requests
 * <p>
 *      This controller is designed to handle requests for performing CRUD operations on rooms
 *      in the AssignForce database. These requests are intended to be used by Revature's AssignForce web page
 *      to allow the user to track room reservations by building and location. The Room objects contain the
 *      information about the rooms that are tracked for reservations used by AssignForce.
 *
 * @author Stefan Maurer, Hogan Brown, Michael Bennett, Nathan J, Samuel Araga
 * @version 1.0
 */
@Component
@RestController
@CrossOrigin
public class RoomController {

    /**
     * Auth string constants for the user's role
     */
    private static final String ADMIN = "admin";
    private static final String TRAINER = "trainer";

    /**
     * Room Service
     */
    @Autowired
    RoomService roomService;

    /**
     * Create a new room
     *<p>
     *     Handler method for a post to the rooms endpoint. Used to create
     *     room entries in the database.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param roomDto       Room data transfer object provided by the request body.
     *                      Used by the getRoom method to construct a Room object.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<RoomDto> that has a body of the Room data transfer
     * object that was created
     */
    @Verify
    @PostMapping("/locations/{locationId}/buildings/{buildingId}/rooms")
    public ResponseEntity<RoomDto> createRoom(UserDto userDto,
                                              @RequestBody RoomDto roomDto,
                                              @RequestHeader(name = "Authorization", required = false) String auth) {

        if (userDto.getRole().equals(ADMIN)) {
            Room room = new Room(roomDto);
            room = this.roomService.createRoom(room);
            return ResponseEntity.status(HttpStatus.CREATED).body(new RoomDto(room));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Get room by ID
     *<p>
     *     Handler method for get room by id endpoint. Used to retrieve a
     *     room record at a given location from the AssignForce database
     *     by a room ID.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param roomId        Unique room ID
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<Building> that has a body of the building object
     * retrieved from the database.
     */
    @Verify
    @GetMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(UserDto userDto,
                                               @PathVariable int roomId,
                                               @RequestHeader(name = "Authorization", required = false) String auth) {

        try {
            if (userDto.getRole().equals(ADMIN) || userDto.getRole().equals(TRAINER)) {
                Room room = this.roomService.getRoomById(roomId);
                return ResponseEntity.status(HttpStatus.OK).body(new RoomDto(room));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (RoomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get all rooms
     *<p>
     *     Handler method for the get all buildings endpoint. Used to retrieve all
     *     building records at a given location from the Building table in the
     *     AssignForce database.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param buildingId    Building Id provided in the URI path. Assigned to the new
     *                      Room object returned by the getRoom method.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<List<RoomDto>> that has a body of the list of room
     * objects that were retrieved from the database.
     */
    @Verify
    @GetMapping("/locations/{locationId}/buildings/{buildingId}/rooms")
    public ResponseEntity<List<RoomDto>> getAllRooms(UserDto userDto,
                                                     @PathVariable int buildingId,
                                                     @RequestHeader(name = "Authorization", required = false) String auth,
                                                     @RequestParam(value = "type", required = false) RoomType type) {
        List<Room> rooms;

        if (userDto.getRole().equals(ADMIN) || userDto.getRole().equals(TRAINER)) {
            if (type == null) {
                rooms = this.roomService.getRoomsByBuildingId(buildingId);
            } else {
                rooms = this.roomService.getRoomsByType(buildingId, type);
            }
            List<RoomDto> roomDtos = new ArrayList<>();
            for (Room r : rooms) {
                roomDtos.add(new RoomDto(r));
            }
            return ResponseEntity.status(HttpStatus.OK).body(roomDtos);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Update room by ID
     *<p>
     *     Handler method for update room endpoint. Used to update a
     *     room record from the AssignForce database by a room ID
     *     and building ID.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param buildingId    Unique building ID.
     * @param roomDto       Room data transfer object provided by the request body.
     *                      Used to create the updated Room object to be persisted.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<RoomDto> that has a body of a room object retrieved
     * from the AssignForce database.
     */
    @Verify
    @PutMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<RoomDto> updateRoom(UserDto userDto,
                                              @PathVariable int buildingId,
                                              @PathVariable int roomId,
                                              @RequestBody RoomDto roomDto,
                                              @RequestHeader(name = "Authorization", required = false) String auth) {
        try {
            if (userDto.getRole().equals(ADMIN)) {
                Room room = new Room(roomDto);
                room.setRoomId(roomId);
                room.setBuildingId(buildingId);
                room = this.roomService.updateRoom(room);
                return ResponseEntity.status(HttpStatus.OK).body(new RoomDto(room));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (RoomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Delete room by ID
     *<p>
     *     Handler method for update room endpoint. Used to update a
     *     room record from the AssignForce database by a room ID.
     *
     * @param userDto       User data transfer object populated by the SecurityAspect.
     *                      Used to determine a users roll and CRUD permissions.
     * @param roomId        Unique room ID.
     * @param auth          Authentication JWT provided by the request header.
     *
     * @return A ResponseEntity<Boolean> that returns true if the record was deleted
     * and false if the record still exists.
     */
    @Verify
    @DeleteMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<Boolean> deleteRoom(UserDto userDto,
                                              @PathVariable int roomId,
                                              @RequestHeader(name = "Authorization", required = false) String auth) {
        if (userDto.getRole().equals(ADMIN)) {
            boolean result = this.roomService.deleteRoom(roomId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

}
