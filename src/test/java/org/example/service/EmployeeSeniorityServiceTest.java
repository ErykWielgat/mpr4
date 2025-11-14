package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class EmployeeSeniorityServiceTest {

    private final EmployeeService service = new EmployeeService();

    private Employee createEmployee(String fn, String ln, LocalDate date) {
        return new Employee(fn, ln, fn.toLowerCase() + "@example.com", "Comp", Position.PROGRAMISTA, date);
    }

    @ParameterizedTest(name = "Staż dla daty {0} powinien wynosić {1} lat")
    @CsvSource({
            "2010-01-01, 15",
            "2020-05-10, 5",
            "2024-02-29, 1"
    })
    void shouldCalculateSeniorityCorrectly(LocalDate hireDate, int expectedYears) {
        Employee emp = createEmployee("Jan", "Kowalski", hireDate);
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
                        new Employee("Anna", "Nowak", "anna@example.com", "Comp", Position.PROGRAMISTA, LocalDate.now().minusYears(3)),
                        "Anna Nowak",
                        3
                ),
                Arguments.of(
                        new Employee("Piotr", "Zieliński", "piotr@example.com", "Comp", Position.PROGRAMISTA, LocalDate.of(2000, 2, 29)),
                        "Piotr Zieliński",
                        LocalDate.now().getYear() - 2000
                )
        );
    }

    @ParameterizedTest(name = "Zakres {0}-{1} lat → liczba pracowników: {2}")
    @CsvSource({
            "0,4,1",
            "5,9,2",
            "10,20,1"
    })
    void shouldFilterEmployeesBySeniorityRange(int minYears, int maxYears, int expectedCount) {
        service.addEmployee(createEmployee("A", "A", LocalDate.now().minusYears(2)));
        service.addEmployee(createEmployee("B", "B", LocalDate.now().minusYears(5)));
        service.addEmployee(createEmployee("C", "C", LocalDate.now().minusYears(12)));
        service.addEmployee(createEmployee("D", "D", LocalDate.now().minusYears(7)));

        List<Employee> filtered = service.filterBySeniorityRange(minYears, maxYears);

        assertThat(filtered, hasSize(expectedCount));
        assertThat(filtered, everyItem(allOf(
                hasProperty("hireDate", lessThanOrEqualTo(LocalDate.now())),
                notNullValue()
        )));
    }

    @ParameterizedTest(name = "Jubileusz dla stażu {0} lat powinien = {1}")
    @CsvSource({
            "10, true",
            "20, true",
            "7, false",
            "1, false"
    })
    void shouldDetectJubileeEmployees(int seniorityYears, boolean isJubilee) {
        Employee emp = createEmployee("E", "E", LocalDate.now().minusYears(seniorityYears));
        service.addEmployee(emp);

        List<Employee> jubilees = service.getJubileeEmployees();

        if (isJubilee) {
            assertThat(jubilees, hasItem(hasProperty("firstName", equalTo("E"))));
        } else {
            assertThat(jubilees, not(hasItem(hasProperty("firstName", equalTo("E")))));
        }
    }

    @ParameterizedTest(name = "Raport powinien pokazać {1} pracowników ze stażem {0} lat")
    @CsvSource({
            "1, 1",
            "4, 2",
            "6, 1",
            "15, 1"
    })
    void shouldGenerateSeniorityDemographicReport(int specificYear, int expectedCount) {
        service.addEmployee(createEmployee("X", "X", LocalDate.now().minusYears(1)));
        service.addEmployee(createEmployee("Y", "Y", LocalDate.now().minusYears(4)));
        service.addEmployee(createEmployee("U", "U", LocalDate.now().minusYears(4)));
        service.addEmployee(createEmployee("Z", "Z", LocalDate.now().minusYears(6)));
        service.addEmployee(createEmployee("W", "W", LocalDate.now().minusYears(15)));

        Map<Integer, Integer> raport = service.generateSeniorityReport();

        assertThat(raport.keySet(), hasItem(specificYear));
        assertThat(raport.get(specificYear), is(expectedCount));
    }
}