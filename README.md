# af-location-service
Microservice responsible for the Location vertical. Part of the AssignForce suite

## Routes, Requests, and Responses
The Location service is subdivided into 3 objects; Location, Building, and Room.  Valid requests will return a Location, Building, or Room JSON object.

### Location
Locations are objects containing the city, state, and zipcode of the geographical location. Zipcodes are passed as **strings**.

**Location JSON**
```
{
  "locationId" : INT,
  "city" : "STRING",
  "state" : "STRING",
  "zipcode" : "STRING"
}
```

Routes | Details | Response
------ | ------- | --------
`POST /locations`| Creates a new location. Requires a body with the Location JSON object form.|Location JSON (201); Forbidden (403); Unauthorized (401)
`GET /locations` | Retrieves all persisted locations.|Location List (201); Forbidden (403); Unauthorized (401)
`GET /locations/{locationId}` | Retrieves a location by locationId.|Location JSON (200); Not Found (404); Unauthorized (401)
`PUT /locations/{locationId}` | Updates a location by locationId with a Location JSON object in the body.|Location JSON (200); Not Found (404); Unauthorized (401)
`DELETE /locations/{locationId}` | Removes a location from the database by locationId.|"OK" (200); Forbidden (403); Unauthorized (401) 

### Building
Buildings are objects that contain the address of the building where a room is located. The locationId  references a Location object that is the geographical location of the building. **All building requests require a locationId.** 

**Building JSON:**
```
{
  "buildingId" : INT,
  "address" : "STRING",
  "locationId" : INT (References a Location object) 
}
```
Routes | Details | Response
------ | ------- | --------
`POST /locations/{locationId}/buildings`|Creates a new building with a given locationId. Body requires Building JSON object.|Building JSON (201); Forbidden (403); Unauthorized (401)
`GET /locations/{locationId}/buildings`| Retrieves all persisted buildings at a given locationId.|Building List (201); Forbidden (403); Unauthorized (401)
`GET /locations/{locationId}/buildings/{buildingId}`| Retrieves a building by buildingId at a certain location. |Building JSON (200); Not Found (404); Unauthorized (401)
`PUT /locations/{locationId}/buildings/{buildingId}`| Updates a building by buildingId at a certain location with a Building JSON object in the body.|Building JSON (200); Not Found (404); Unauthorized (401)
`DELETE /locations/{locationId}/buildings/{buildingId}`| Removes a building from the database by buildingId at a certain location.|"OK" (200); Not Found (404); Unauthorized (401)


### Room
Rooms are objects that contain the room name, capacity, and room type. Room type is an enum that can be "VIRTUAL", "REMOTE", "MEETING" or NULL. The buiLdingId references the building at which the room is located. **All room requests require a locationId and buildingId.**

**Room JSON:**
```
{
  "roomId" : INT,
  "name" : "STRING",
  "type" : "STRING (VIRTUAL | REMOTE | MEETING)",
  "capacity" : INT,
  "buildingId" : INT (References a Building object)
}
```
Routes | Details | Response
------ | ------- | --------
`POST /locations/{locationId}/buildings/{buildingId}/rooms`|Creates a new room with given buildingId and locationId. Body requires Room JSON object.|Room JSON (201); Forbidden (403); Unauthorized (401)
`GET /locations/{locationId}/buildings/{buildingId}/rooms`|Retrieves all persisted rooms at a given location and building.|Room List (201); Forbidden (403); Unauthorized (401)
`GET /locations/{locationId}/buildings/{buildingId}/rooms/{roomId}`|Retrieves a room by roomId at a certain building and location.|Room JSON (200); Not Found (404); Unauthorized (401)
`PUT /locations/{locationId}/buildings/{buildingId}/rooms/{roomId}`|Updates a room by roomId at a certain location and building with a Room JSON object in the body.|Room JSON (200); Not Found (404); Unauthorized (401)
`DELETE /locations/{locationId}/buildings/{buildingId}/rooms/{roomId}`|Removes a room from the database by roomId at a certain location and building.|"OK" (200); Not Found (404); Unauthorized (401)

## Authorization
Requests are required to have an Authorization header with a valid JWT. 
