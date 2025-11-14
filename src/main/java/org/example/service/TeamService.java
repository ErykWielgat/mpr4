package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.model.ProjectTeam;

import java.util.List;

public class TeamService {

    public ProjectTeam createTeam(String name, int maxSize) {
        if (maxSize < 2) {
            throw new IllegalArgumentException("Team size must be at least 2 to meet diversity requirements");
        }
        return new ProjectTeam(name, maxSize);
    }

    public void addEmployeeToTeam(Employee emp, ProjectTeam team) {
        List<Employee> employees = team.getEmployees();

        if (employees.size() >= team.getMaxSize()) {
            throw new IllegalStateException("Team is full");
        }
        if (employees.contains(emp)) {
            throw new IllegalStateException("Employee already in team");
        }
        employees.add(emp);
    }

    public void moveEmployee(Employee emp, ProjectTeam from, ProjectTeam to) {
        List<Employee> fromEmployees = from.getEmployees();
        List<Employee> toEmployees = to.getEmployees();

        if (!fromEmployees.contains(emp)) {
            throw new IllegalStateException("Employee not in source team");
        }
        if (toEmployees.contains(emp)) {
            throw new IllegalStateException("Employee already in team");
        }
        if (toEmployees.size() >= to.getMaxSize()) {
            throw new IllegalStateException("Team is full");
        }

        fromEmployees.remove(emp);
        toEmployees.add(emp);
    }

    public boolean validateTeamComposition(ProjectTeam team) {
        boolean hasManager = false;
        boolean hasProgrammer = false;

        for (Employee employee : team.getEmployees()) {
            if (employee.getPosition() == Position.MANAGER) {
                hasManager = true;
            }
            if (employee.getPosition() == Position.PROGRAMISTA) {
                hasProgrammer = true;
            }

            if (hasManager && hasProgrammer) {
                return true;
            }
        }

        return false;
    }

    public java.util.List<Employee> getEmployees(ProjectTeam team) {
        return team.getEmployees();
    }
}
