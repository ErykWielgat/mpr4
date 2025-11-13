package org.example.service;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.model.*;
import org.example.exception.InvalidDataException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ImportService {

    private final EmployeeService employeeService;

    public ImportService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public ImportSummary importFromCsv(String filePath) {
        ImportSummary summary = new ImportSummary();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> allRows = reader.readAll();

            for (int i = 1; i < allRows.size(); i++) {
                String[] row = allRows.get(i);

                if (row.length != 5 || row[0].trim().isEmpty()) {
                    summary.addError("Linia " + (i + 1) + ": nieprawidłowa liczba kolumn lub pusta linia");
                    continue;
                }

                try {
                    String firstName = row[0].trim();
                    String lastName = row[1].trim();
                    String email = row[2].trim();
                    String company = row[3].trim();
                    String positionText = row[4].trim().toUpperCase();

                    Position position;
                    try {
                        position = Position.valueOf(positionText);
                    } catch (IllegalArgumentException e) {
                        throw new InvalidDataException("Nieprawidłowe stanowisko: " + positionText);
                    }

                    Employee employee = new Employee(firstName, lastName, email, company, position);
                    employee.setSalary(employee.getSalary());
                    employeeService.addEmployee(employee);
                    summary.incrementImported();

                } catch (InvalidDataException e) {
                    summary.addError("Linia " + (i + 1) + ": " + e.getMessage());
                } catch (Exception e) {
                    summary.addError("Linia " + (i + 1) + ": nieznany błąd - " + e.getMessage());
                }
            }

        } catch (IOException | CsvException e) {
            summary.addError("Błąd odczytu pliku: " + e.getMessage());
        }

        return summary;
    }
}
