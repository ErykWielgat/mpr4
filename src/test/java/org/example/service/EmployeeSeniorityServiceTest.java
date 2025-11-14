package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class EmployeeServiceSeniorityTest {

    private final EmployeeService service = new EmployeeService();

    @ParameterizedTest(name = "Staż dla daty {0} powinien wynosić {1} lat")
    @CsvSource({
            "2010-01-01, 15",
            "2020-05-10, 5",
            "2024-02-29, 1"
    })
    void shouldCalculateSeniorityCorrectly(LocalDate hireDate, int expectedYears) {
        Employee emp = new Employee("Jan", "Kowalski", "jan@example.com", "CompanyX", Position.PROGRAMISTA);
        emp.setHireDate(hireDate);
        service.addEmployee(emp);

        int seniority = service.getEmploymentYears(emp);

        assertThat(seniority, is(expectedYears));
    }

    @ParameterizedTest(name = "{1} powinien mieć staż {2} lat")
    @MethodSource("provideEmployeesWithDates")
    void shouldCalculateSeniorityUsingMethodSource(Employee emp, String name, int expectedYears) {
        service.addEmployee(emp);

        int seniority = service.getEmploymentYears(emp);

        assertThat(seniority, equalTo(expectedYears));
    }

    static Stream<Arguments> provideEmployeesWithDates() {
        return Stream.of(
                Arguments.of(
                        employeeWithDate("Anna", "Nowak", LocalDate.now().minusYears(3)),
                        "Anna Nowak",
                        3
                ),
                Arguments.of(
                        employeeWithDate("Piotr", "Zieliński", LocalDate.of(2000, 2, 29)), // przestępny
                        "Piotr Zieliński",
                        LocalDate.now().getYear() - 2000
                )
        );
    }

    private static Employee employeeWithDate(String fn, String ln, LocalDate date) {
        Employee e = new Employee(fn, ln, fn.toLowerCase() + "@example.com", "Comp", Position.PROGRAMISTA);
        e.setHireDate(date);
        return e;
    }

    @ParameterizedTest(name = "Zakres {0}-{1} lat → liczba pracowników: {2}")
    @CsvSource({
            "0,5,2",
            "5,10,1",
            "10,20,1"
    })
    void shouldFilterEmployeesBySeniorityRange(int minYears, int maxYears, int expectedCount) {
        Employee e1 = employeeWithDate("A", "A", LocalDate.now().minusYears(2));
        Employee e2 = employeeWithDate("B", "B", LocalDate.now().minusYears(5));
        Employee e3 = employeeWithDate("C", "C", LocalDate.now().minusYears(12));
        Employee e4 = employeeWithDate("D", "D", LocalDate.now().minusYears(7));

        service.addEmployee(e1);
        service.addEmployee(e2);
        service.addEmployee(e3);
        service.addEmployee(e4);

        List<Employee> filtered = service.filterBySeniorityRange(minYears, maxYears);

        assertThat(filtered, hasSize(expectedCount));
        assertThat(filtered, everyItem(notNullValue()));
    }

    @ParameterizedTest(name = "Jubileusz dla stażu {0} lat powinien = {1}")
    @CsvSource({
            "10, true",
            "20, true",
            "7, false",
            "1, false"
    })
    void shouldDetectJubileeEmployees(int seniorityYears, boolean isJubilee) {
        Employee emp = employeeWithDate("E", "E", LocalDate.now().minusYears(seniorityYears));
        service.addEmployee(emp);

        List<Employee> jubilees = service.getJubileeEmployees();

        if (isJubilee) {
            assertThat(jubilees, hasItem(hasProperty("firstName", equalTo("E"))));
        } else {
            assertThat(jubilees, not(hasItem(hasProperty("firstName", equalTo("E")))));
        }
    }

    @ParameterizedTest(name = "Raport powinien pokazać {1} pracowników w przedziale {0}")
    @CsvSource({
            "0-3, 1",
            "3-7, 2",
            "10-20, 1"
    })
    void shouldGenerateSeniorityDemographicReport(String range, int expectedCount) {
        service.addEmployee(employeeWithDate("X", "X", LocalDate.now().minusYears(1)));
        service.addEmployee(employeeWithDate("Y", "Y", LocalDate.now().minusYears(4)));
        service.addEmployee(employeeWithDate("Z", "Z", LocalDate.now().minusYears(6)));
        service.addEmployee(employeeWithDate("W", "W", LocalDate.now().minusYears(15)));

        Map<String, Integer> raport = service.generateSeniorityReport();

        assertThat(raport.keySet(), hasItem(range));
        assertThat(raport.get(range), is(expectedCount));
    }
}
