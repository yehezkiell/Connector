package com.example.yehezkiel.eclassapp;

/**
 * Created by Yehezkiel on 7/20/2018.
 */

public abstract class ListItem {
    public static final int TYPE_NAME = 0;
    public static final int TYPE_TASK = 1;

    abstract public int getType();

}
