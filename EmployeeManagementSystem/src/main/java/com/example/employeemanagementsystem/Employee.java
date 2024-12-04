package com.example.employeemanagementsystem;

public abstract class Employee {
    private String name;
    private String employeeType;

    public Employee(String name, String employeeType) {
        this.name = name;
        this.employeeType = employeeType;
    }

    public String getName() {
        return name;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    // Abstract method to calculate salary
    public abstract double calculateSalary();

    @Override
    public String toString() {
        return name + " (" + employeeType + ")";
    }
}
