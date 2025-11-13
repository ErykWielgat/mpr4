package org.example.model;

public enum Position {
    PREZES(25000, 1, 30000),
    WICEPREZES(18000, 2, 22000),
    MANAGER(12000, 3, 15000),
    PROGRAMISTA(8000, 4, 12000),
    STAZYSTA(3000, 5, 5000);

    private final double baseSalary;
    private final int hierarchyLevel;
    private final double maxSalary;

    Position(double baseSalary, int hierarchyLevel, double maxSalary) {
        this.baseSalary = baseSalary;
        this.hierarchyLevel = hierarchyLevel;
        this.maxSalary = maxSalary;
    }

    public double getBaseSalary() { return baseSalary; }
    public int getHierarchyLevel() { return hierarchyLevel; }
    public double getMaxSalary() { return maxSalary; }
}
