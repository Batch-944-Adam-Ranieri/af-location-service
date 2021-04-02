package com.revature.exceptions;

public class BuildingNotFoundException extends Exception{

    public BuildingNotFoundException(String message) {
        super(message);
    }

    public BuildingNotFoundException() {
        super();
    }
}
