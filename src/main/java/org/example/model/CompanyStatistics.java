package org.example.model;

public class CompanyStatistics {
    private int employeeCount;
    private double averageSalary;
    private String topEarner;

    public CompanyStatistics(int employeeCount, double averageSalary, String topEarner) {
        this.employeeCount = employeeCount;
        this.averageSalary = averageSalary;
        this.topEarner = topEarner;
    }

    @Override
    public String toString() {
        return "Ilosc pracownikow: " + employeeCount + ", Srednie wynagrodzenie: " + averageSalary + ", Najlepiej zarabiajacy: " + topEarner;
    }
}
