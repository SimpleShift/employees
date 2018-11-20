package com.simpleshift.app.employees;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static List<Employee> employees = new ArrayList<Employee>();

    public static List<Employee> getEmployees() {
        return employees;
    }

    public static Employee getEmployee(String id) {
        for (Employee e : employees) {
            if (e.getId().equals(id))
                return e;
        }

        return null;
    }

    public static void addEmployee(Employee e) {
        employees.add(e);
    }

    public static void deleteEmployee(String id) {
        for (Employee e : employees) {
            if (e.getId().equals(id)) {
                employees.remove(e);
                break;
            }
        }
    }
}