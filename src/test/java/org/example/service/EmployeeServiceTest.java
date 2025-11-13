package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private final EmployeeService service = new EmployeeService();

    @ParameterizedTest(name = "Podwyżka {0}% powinna zwiększyć pensję poprawnie")
    @ValueSource(doubles = {5, 10, 15, 20})
    void shouldGiveRaiseWithValueSource(double percentage) {
        Employee employee = new Employee("Jan", "Kowalski", "jan@example.com", "CompanyA", Position.PROGRAMISTA);
        double initialSalary = employee.getSalary();
        service.giveRaise(employee, percentage);
        assertEquals(initialSalary * (1 + percentage / 100), employee.getSalary(), 0.01);
    }

    @ParameterizedTest(name = "Awans {0} -> {1} powinien ustawić pensję na {2}")
    @CsvSource({
            "STAZYSTA, PROGRAMISTA, 8000",
            "PROGRAMISTA, MANAGER, 12000",
            "MANAGER, WICEPREZES, 18000",
            "WICEPREZES, PREZES, 25000"
    })
    void shouldPromoteWithCsvSource(String current, String next, double expectedSalary) {
        Employee employee = new Employee("Anna", "Nowak", "anna@example.com", "CompanyB", Position.valueOf(current));
        service.promote(employee, Position.valueOf(next));
        assertEquals(Position.valueOf(next), employee.getPosition());
        assertEquals(expectedSalary, employee.getSalary(), 0.01);
    }

    @ParameterizedTest(name = "Nieprawidłowy awans {0} ")
    @EnumSource(value = Position.class, names = {"STAZYSTA", "PROGRAMISTA", "MANAGER", "WICEPREZES"})
    void shouldThrowWhenPromoteToLowerOrEqual(Position currentPosition) {
        Employee employee = new Employee("Piotr", "Zieliński", "piotr@example.com", "CompanyC", currentPosition);
        assertThatThrownBy(() -> service.promote(employee, currentPosition))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Awans możliwy tylko na wyższe stanowisko");
    }

    @ParameterizedTest(name = "Podwyżka {2}% na pensję {1} dla {0} powinna zakończyć się wyjątkiem jeśli przekroczy maxSalary")
    @CsvSource({
            "STAZYSTA, 4500, 50",
            "PROGRAMISTA, 12000, 50",
            "MANAGER, 15000, 100"
    })
    void shouldThrowWhenRaiseExceedsMaxSalary(String position, double currentSalary, double percentage) {
        Employee employee = new Employee("Kasia", "Wiśniewska", "kasia@example.com", "CompanyD", Position.valueOf(position));
        employee.setSalary(currentSalary);
        assertThatThrownBy(() -> service.giveRaise(employee, percentage))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Przekroczono maksymalną pensję");
    }

    @ParameterizedTest(name = "{0} awansuje z {1} do {2}")
    @MethodSource("provideEmployeesForPromotion")
    void shouldPromoteWithMethodSource(Employee employee, Position newPosition, double expectedSalary) {
        service.promote(employee, newPosition);
        assertEquals(newPosition, employee.getPosition());
        assertEquals(expectedSalary, employee.getSalary(), 0.01);
    }

    static Stream<Arguments> provideEmployeesForPromotion() {
        return Stream.of(
                Arguments.of(new Employee("Marek", "Kowal", "marek@example.com", "CompanyE", Position.STAZYSTA),
                        Position.PROGRAMISTA, 8000),
                Arguments.of(new Employee("Ela", "Nowak", "ela@example.com", "CompanyF", Position.PROGRAMISTA),
                        Position.MANAGER, 12000),
                Arguments.of(new Employee("Robert", "Lewandowski", "robert@example.com", "CompanyG", Position.MANAGER),
                        Position.WICEPREZES, 18000)
        );
    }
}