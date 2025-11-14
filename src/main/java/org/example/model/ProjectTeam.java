package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class ProjectTeam {
    private final String name;
    private final int maxSize;
    private final List<Employee> employees = new ArrayList<>();

    public ProjectTeam(String name, int maxSize) {
        if (maxSize < 2) {
            throw new IllegalArgumentException("Team size must be at least 2 to meet diversity requirements");
        }
        this.name = name;
        this.maxSize = maxSize;
    }

    public String getName() {
        return name;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
