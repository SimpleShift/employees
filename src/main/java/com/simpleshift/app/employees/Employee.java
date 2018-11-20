package com.simpleshift.app.employees;

class Employee {


    private String id;
    private String firstName;
    private String lastName;
    private String locationId;

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getFirstName() {
        return firstName;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    String getLastName() {
        return lastName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    String getLocationId() {
        return locationId;
    }

    void setLocationId(String locationId) {
        this.locationId = locationId;
    }

}