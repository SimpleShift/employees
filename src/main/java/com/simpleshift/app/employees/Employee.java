package com.simpleshift.app.employees;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee {


    private String id;
    private String firstName;
    private String lastName;
    private String locationId;
    private ArrayList<ArrayList<Date>> hoursWorked = new ArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }


    public ArrayList<ArrayList<Date>> getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(ArrayList<ArrayList<Date>> hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public void addHoursWorked(ArrayList<Date> hours){
        hoursWorked.add(hours);
    }
}