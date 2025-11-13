package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeServiceTest {

    private final EmployeeService service = new EmployeeService();

    @ParameterizedTest(name = "{0} awansuje na {1} i otrzymuje pensję {2}")
    @CsvSource({
            "STAZYSTA, PROGRAMISTA, 8000",
            "PROGRAMISTA, MANAGER, 12000"
    })
    void promoteRedTest(String currentPosition, String newPosition, double expectedSalary) {
        Employee employee = new Employee("Jan", "Kowalski", "test@example.com", "CompanyY", Position.valueOf(currentPosition));

        service.promote(employee, Position.valueOf(newPosition));

        assertThat(employee.getPosition()).isEqualTo(Position.valueOf(newPosition));
        assertThat(employee.getSalary()).isEqualTo(expectedSalary);
    }

    @ParameterizedTest(name = "{0} z pensją {1} otrzymuje podwyżkę {2}% i ma {3}")
    @CsvSource({
            "STAZYSTA, 3000, 50, 4500",
            "PROGRAMISTA, 8000, 10, 8800"
    })
    void giveRaiseRedTest(String position, double currentSalary, double percentage, double expectedSalary) {
        Employee employee = new Employee("Anna", "Nowak", "test2@example.com", "CompanyX", Position.valueOf(position));
        employee.setSalary(currentSalary);

        service.giveRaise(employee, percentage);

        assertThat(employee.getSalary()).isEqualTo(expectedSalary);
    }
}
