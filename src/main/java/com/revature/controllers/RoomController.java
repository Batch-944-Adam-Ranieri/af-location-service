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

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@Component
@RestController
public class RoomController {

    private String adminRoll = "admin";
    private String trainerRoll = "trainer";

    @Autowired
    RoomService roomService;
    @Verify
    @PostMapping("/locations/{locationId}/buildings/{buildingId}/rooms")
    public ResponseEntity<RoomDto> createRoom(UserDto userDto, @RequestBody RoomDto roomDto, @RequestHeader(name = "Authorization", required = false) String auth){

        if (userDto.getRole().equals(adminRoll)) {
            Room room = new Room(roomDto);
            room = this.roomService.createRoom(room);
            return ResponseEntity.status(HttpStatus.CREATED).body(new RoomDto(room));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @Verify
    @GetMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(UserDto userDto, @PathVariable int locationId, @PathVariable int buildingId, @PathVariable int roomId, @RequestHeader(name = "Authorization", required = false) String auth){

        try{
            if (userDto.getRole().equals(adminRoll) || userDto.getRole().equals(trainerRoll)) {
                Room room = this.roomService.getRoomById(roomId);
                return ResponseEntity.status(HttpStatus.OK).body(new RoomDto(room));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }catch(RoomNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @Verify
    @GetMapping("/locations/{locationId}/buildings/{buildingId}/rooms")
    public ResponseEntity<List<RoomDto>> getAllRooms(UserDto userDto,
                                                     @PathVariable int locationId,
                                                     @PathVariable int buildingId,
                                                     @RequestHeader(name = "Authorization", required = false) String auth,
                                                     @RequestParam(value="type", required = false)RoomType type){
        List<Room> rooms;

        if (userDto.getRole().equals(adminRoll) || userDto.getRole().equals(trainerRoll)) {
            if(type == null) {
                rooms = this.roomService.getRoomsByBuildingId(buildingId);
            }
            else {
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
    @Verify
    @PutMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<RoomDto> updateRoom(UserDto userDto, @PathVariable int locationId, @PathVariable int buildingId,@PathVariable int roomId, @RequestBody RoomDto roomDto, @RequestHeader(name = "Authorization", required = false) String auth){
        try {
            if (userDto.getRole().equals(adminRoll)) {
                Room room = new Room(roomDto);
                room = this.roomService.updateRoom(room);
                return ResponseEntity.status(HttpStatus.OK).body(new RoomDto(room));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }catch(RoomNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @Verify
    @DeleteMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<Boolean> deleteRoom(UserDto userDto, @PathVariable int locationId, @PathVariable int buildingId, @PathVariable int roomId, @RequestHeader(name = "Authorization", required = false) String auth){
        if (userDto.getRole().equals(adminRoll)) {
            boolean result = this.roomService.deleteRoom(roomId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

}
