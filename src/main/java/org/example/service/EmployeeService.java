package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.model.CompanyStatistics;

import java.util.*;

public class EmployeeService {
    private final List<Employee> employees = new ArrayList<>();
    private final Map<Employee, List<Integer>> ratings = new HashMap<>();

    public void addEmployee(Employee employee) {
        for (Employee e : employees) {
            if (e.getEmail().equalsIgnoreCase(employee.getEmail())) {
                System.out.println("Pracownik z takim emailem juz istnieje: " + employee.getEmail());
                return;
            }
        }
        employees.add(employee);
    }

    public void displayAll() {
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    public List<Employee> findByCompany(String company) {
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getCompany().equalsIgnoreCase(company)) {
                result.add(e);
            }
        }
        return result;
    }

    public List<Employee> sortByLastName() {
        List<Employee> copy = new ArrayList<>(employees);
        copy.sort(Comparator.comparing(Employee::getLastName));
        return copy;
    }

    public Map<Position, List<Employee>> groupByPosition() {
        Map<Position, List<Employee>> map = new HashMap<>();
        for (Employee e : employees) {
            Position p = e.getPosition();
            map.computeIfAbsent(p, k -> new ArrayList<>()).add(e);
        }
        return map;
    }

    public Map<Position, Integer> countByPosition() {
        Map<Position, Integer> map = new HashMap<>();
        for (Employee e : employees) {
            Position p = e.getPosition();
            map.put(p, map.getOrDefault(p, 0) + 1);
        }
        return map;
    }

    public double averageSalary() {
        if (employees.isEmpty()) return 0;
        double sum = 0;
        for (Employee e : employees) {
            sum += e.getSalary();
        }
        return sum / employees.size();
    }

    public Employee topEarner() {
        if (employees.isEmpty()) return null;
        Employee best = employees.get(0);
        for (Employee e : employees) {
            if (e.getSalary() > best.getSalary()) {
                best = e;
            }
        }
        return best;
    }

    public List<Employee> validateSalaryConsistency() {
        List<Employee> invalid = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getSalary() < e.getPosition().getBaseSalary() || e.getSalary() > e.getPosition().getMaxSalary()) {
                invalid.add(e);
            }
        }
        return invalid;
    }

    public Map<String, CompanyStatistics> getCompanyStatistics() {
        Map<String, List<Employee>> grouped = new HashMap<>();
        for (Employee e : employees) {
            grouped.computeIfAbsent(e.getCompany(), k -> new ArrayList<>()).add(e);
        }

        Map<String, CompanyStatistics> stats = new HashMap<>();
        for (String company : grouped.keySet()) {
            List<Employee> list = grouped.get(company);
            int count = list.size();
            double sum = 0;
            Employee top = null;

            for (Employee e : list) {
                sum += e.getSalary();
                if (top == null || e.getSalary() > top.getSalary()) {
                    top = e;
                }
            }

            double avg = count > 0 ? sum / count : 0;
            stats.put(company, new CompanyStatistics(count, avg, top.getFullName()));
        }

        return stats;
    }


    public void promote(Employee employee, Position newPosition) {
        if (newPosition.getHierarchyLevel() >= employee.getPosition().getHierarchyLevel()) {
            throw new IllegalArgumentException(
                    "Awans możliwy tylko na wyższe stanowisko niż obecne: " + employee.getPosition());
        }
        employee.setSalary(newPosition.getBaseSalary());
        employee.setPosition(newPosition);
    }

    public void giveRaise(Employee employee, double percentage) {
        if (percentage <= 0) {
            throw new IllegalArgumentException("Procent podwyżki musi być dodatni");
        }
        double newSalary = employee.getSalary() * (1 + percentage / 100);
        if (newSalary > employee.getPosition().getMaxSalary()) {
            throw new IllegalArgumentException(
                    "Przekroczono maksymalną pensję dla stanowiska " + employee.getPosition());
        }
        employee.setSalary(newSalary);
    }

    public void addRating(Employee employee, int year, int rating) {
        if (!employees.contains(employee)) {
            throw new IllegalArgumentException("Pracownik nie istnieje w bazie: " + employee.getFullName());
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Ocena musi być w skali 1-5");
        }
        employee.getYearlyRatings().put(year, rating);
    }

    public double calculateAverageRating(Employee employee) {
        Map<Integer, Integer> ratings = employee.getYearlyRatings();
        if (ratings.isEmpty()) {
            return 0.0;
        }

        int sum = 0;
        for (Integer rating : ratings.values()) {
            sum += rating;
        }

        return (double) sum / ratings.size();
    }


    public Employee getTopRatingEmployee() {
        Employee top = null;
        double maxAvg = 0.0;

        for (Employee e : employees) {
            double avg = calculateAverageRating(e);
            if (top == null || avg > maxAvg) {
                top = e;
                maxAvg = avg;
            }
        }

        return top;
    }
}

