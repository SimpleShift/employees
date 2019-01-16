package com.simpleshift.app.employees;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Database {
    private static List<Employee> employees = new ArrayList<Employee>();

    static List<Employee> getEmployees() {
        return employees;
    }

    static List<Employee> getEmployeesFrom(String locationId) {
        List<Employee> ret = new ArrayList<Employee>();
        for (Employee e : employees) {
            if (e.getLocationId().equals(locationId))
                ret.add(e);
        }
        return ret;
    }

    static Employee getEmployee(String id) {
        for (Employee e : employees) {
            if (e.getId().equals(id))
                return e;
        }

        return null;
    }



    static void addEmployee(Employee e) {
        employees.add(e);
    }

    static void deleteEmployee(String id) {
        for (Employee e : employees) {
            if (e.getId().equals(id)) {
                employees.remove(e);
                break;
            }
        }
    }

    static void logTime(String id, ArrayList<Date> hoursWorked){
        for (Employee e : employees) {
            if (e.getId().equals(id)) {

                e.addHoursWorked(hoursWorked);

                break;
            }
        }
    }
}