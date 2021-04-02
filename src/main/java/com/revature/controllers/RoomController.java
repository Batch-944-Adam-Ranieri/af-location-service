package com.revature.controllers;

import com.revature.dtos.RoomDto;
import com.revature.entities.Room;
import com.revature.exceptions.RoomNotFoundException;
import com.revature.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RestController
public class RoomController {

    @Autowired
    RoomService roomService;

    @PostMapping("/locations/{locationId}/buildings/{buildingId}/rooms")
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto, @RequestHeader(name = "Authorization", required = false) String auth){

        if(auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (auth.equals("Authorized")) {
            Room room = new Room(roomDto);
            room = this.roomService.createRoom(room);
            return ResponseEntity.status(HttpStatus.CREATED).body(new RoomDto(room));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable int locationId, @PathVariable int buildingId, @PathVariable int roomId, @RequestHeader(name = "Authorization", required = false) String auth){

        try{
            if (auth.equals("Authorized")) {
                Room room = this.roomService.getRoomById(roomId);
                return ResponseEntity.status(HttpStatus.OK).body(new RoomDto(room));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }catch(RoomNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/locations/{locationId}/buildings/{buildingId}/rooms")
    public ResponseEntity<List<RoomDto>> getAllRooms(@PathVariable int locationId, @PathVariable int buildingId,@RequestHeader(name = "Authorization", required = false) String auth){
        if (auth.equals("Authorized")) {
            List<Room> rooms = this.roomService.getAllRooms();
            List<RoomDto> roomDtos = new ArrayList<>();
            for (Room r : rooms) {
                roomDtos.add(new RoomDto(r));
            }
            return ResponseEntity.status(HttpStatus.OK).body(roomDtos);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable int locationId, @PathVariable int buildingId,@PathVariable int roomId, @RequestBody RoomDto roomDto, @RequestHeader(name = "Authorization", required = false) String auth){
        try {
            if (auth == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }else if ( auth.equals("Authorized")) {
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

    @DeleteMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<Boolean> deleteRoom(@PathVariable int locationId, @PathVariable int buildingId, @PathVariable int roomId, @RequestHeader(name = "Authorization", required = false) String auth){
        if(auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (auth.equals("Authorized")) {
            boolean result = this.roomService.deleteRoom(roomId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

}
