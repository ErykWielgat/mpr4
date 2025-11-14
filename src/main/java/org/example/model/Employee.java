package org.example.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Employee {
    private String firstName;
    private String lastName;
    private String email;
    private String company;
    private Position position;
    private double salary;
    private LocalDate hireDate;

    private Map<Integer, Integer> yearlyRatings = new HashMap<>();

    public Employee(String firstName, String lastName, String email, String company, Position position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.company = company;
        this.position = position;
        this.salary = this.position.getBaseSalary();
    }

    public Employee(String firstName, String lastName, String email, String company, Position position,LocalDate hireDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.company = company;
        this.position = position;
        this.salary = this.position.getBaseSalary();
        this.hireDate=hireDate;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getCompany() {
        return company;
    }
    public Position getPosition() {
        return position;
    }
    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        if (salary <= 0) {
            throw new IllegalArgumentException("Wynagrodzenie musi być dodatnie");
        }
        this.salary = salary;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(email, employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s) - %s - %s - %.2f",
                firstName, lastName, email, company, position, salary);
    }


    public void setPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Stanowisko nie może być nullem");
        }
        this.position = position;
    }
    public Map<Integer, Integer> getYearlyRatings() {
        return yearlyRatings;
    }
    public void setYearlyRatings(Map<Integer, Integer> yearlyRatings) {
        this.yearlyRatings = yearlyRatings;
    }
    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
}