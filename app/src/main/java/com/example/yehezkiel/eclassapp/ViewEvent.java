package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 10/30/2018.
 */

public class ViewEvent {

    private String message;

    public ViewEvent( String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
