package org.example;

import org.example.model.*;
import org.example.service.*;
import org.example.exception.*;

import java.util.List;
import java.util.Map;

import static org.example.model.Position.*;

public class Main {
    public static void main(String[] args) {
        EmployeeService system = new EmployeeService();
        ImportService importService = new ImportService(system);
        ApiService apiService = new ApiService();

        system.addEmployee(new Employee("Jan", "Kowalski", "jan.k@techcorp.pl", "TechCorp", MANAGER));
        system.addEmployee(new Employee("Anna", "Nowak", "anna.n@techcorp.pl", "TechCorp", PROGRAMISTA));
        system.addEmployee(new Employee("Michał", "Wiśniewski", "michal.w@devhouse.pl", "DevHouse", STAZYSTA));
        system.addEmployee(new Employee("Eryk", "Wielgat", "eryk.w@techcorp.pl", "TechCorp", PREZES));
        system.addEmployee(new Employee("Ewa", "Zielińska", "ewa.z@techcorp.pl", "TechCorp", WICEPREZES));


        System.out.println("\n********* Wszyscy pracownicy *********");
        system.displayAll();

        System.out.println("\n********* Pracownicy firmy TechCorp *********");
        for (Employee p : system.findByCompany("TechCorp")) {
            System.out.println(p);
        }

        System.out.println("\n********* Posortowani po nazwisku *********");
        for (Employee p : system.sortByLastName()) {
            System.out.println(p);
        }

        System.out.println("\n********* Grupowanie po stanowisku *********");
        Map<Position, List<Employee>> grupy = system.groupByPosition();
        for (Position s : grupy.keySet()) {
            System.out.println(s + ": " + grupy.get(s).size() + " pracownik(i/ów)");
        }

        System.out.println("\n********* Zliczanie pracowników *********");
        Map<Position, Integer> liczby = system.countByPosition();
        for (Position s : liczby.keySet()) {
            System.out.println(s + ": " + liczby.get(s));
        }

        System.out.println("\n********* Średnie wynagrodzenie *********");
        System.out.printf("Srednie zarobki wszystkich pracownikow z bazy: %.2f zł%n", system.averageSalary());

        System.out.println("\n********* Najlepiej zarabiający *********");
        Employee top = system.topEarner();
        if (top != null) {
            System.out.println(top);
        }

        System.out.println("\n********* Import z pliku CSV *********");
        try {
            ImportSummary summary = importService.importFromCsv("??????? nie wiem");
            System.out.println("Zaimportowano: " + summary.getImportedCount());
            if (!summary.getErrors().isEmpty()) {
                System.out.println("Błędy podczas importu:");
                for (String error : summary.getErrors()) {
                    System.out.println(" - " + error);
                }
            }
        } catch (Exception e) {
            System.out.println("Bład importu CSV: " + e.getMessage());
        }

        System.out.println("\n********* Pobieranie danych z API *********");
        try {
            List<Employee> apiEmployees = apiService.fetchEmployeesFromApi();
            for (Employee e : apiEmployees) {
                system.addEmployee(e);
            }
            System.out.println("Zaimportowano z API: " + apiEmployees.size() + " pracownikow");
        } catch (ApiException e) {
            System.out.println("Bład API: " + e.getMessage());
        }

        System.out.println("\n********* Sprawdzenie spójności wynagrodzen *********");
        List<Employee> invalidSalaries = system.validateSalaryConsistency();
        if (invalidSalaries.isEmpty()) {
            System.out.println("Wszyscy pracownicy mają poprawne wynagrodzenie.");
        } else {
            for (Employee e : invalidSalaries) {
                System.out.println("Blad: " + e.getFullName() + " zarabia mniej niż minimalna stawka dla " + e.getPosition());
            }
        }

        System.out.println("\n********* Statystyki firm *********");
        Map<String, CompanyStatistics> stats = system.getCompanyStatistics();
        for (Map.Entry<String, CompanyStatistics> entry : stats.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
