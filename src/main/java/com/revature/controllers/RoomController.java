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
@CrossOrigin
public class RoomController {

    @Autowired
    RoomService roomService;

    @PostMapping("/locations/{locationId}/buildings/{buildingId}/rooms")
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto){
        Room room = new Room(roomDto);
        room = this.roomService.createRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RoomDto(room));
    }

    @GetMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<Object> getRoomById(@PathVariable int locationId, @PathVariable int buildingId, @PathVariable int roomId){
        try{
            Room room = this.roomService.getRoomById(roomId);
            return ResponseEntity.status(HttpStatus.OK).body(new RoomDto(room));
        }catch(RoomNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error message: Room not found");
        }
    }

    @GetMapping("/locations/{locationId}/buildings/{buildingId}/rooms")
    public ResponseEntity<List<RoomDto>> getAllRooms(@PathVariable int locationId, @PathVariable int buildingId){
        List<Room> rooms = this.roomService.getAllRooms();
        List<RoomDto> roomDtos = new ArrayList<>();
        for(Room r : rooms){
            roomDtos.add(new RoomDto(r));
        }
        return ResponseEntity.status(HttpStatus.OK).body(roomDtos);
    }

    @PutMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable int locationId, @PathVariable int buildingId,@PathVariable int roomId, @RequestBody RoomDto roomDto){
        try{
            Room room = new Room(roomDto);
            room = this.roomService.updateRoom(room);
            return ResponseEntity.status(HttpStatus.OK).body(new RoomDto(room));
        }catch(RoomNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/locations/{locationId}/buildings/{buildingId}/rooms/{roomId}")
    public ResponseEntity<Boolean> deleteRoom(@PathVariable int locationId, @PathVariable int buildingId, @PathVariable int roomId){
        boolean result = this.roomService.deleteRoom(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
