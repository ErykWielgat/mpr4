package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.stream.Stream;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class EmployeeServiceRatingTest {

    private final EmployeeService service = new EmployeeService();

    @ParameterizedTest(name = "Średnia dla jednej oceny {1} w roku {0}")
    @CsvSource({
            "2023, 5",
            "2022, 3",
            "2021, 1"
    })
    void shouldCalculateAverageForSingleRating(int year, int rating) {
        Employee emp = new Employee("Jan", "Kowalski", "jan@example.com", "CompanyA", Position.PREZES);
        service.addEmployee(emp);
        service.addRating(emp, year, rating);

        double avg = service.calculateAverageRating(emp);
        assertThat(avg).isEqualTo(rating);
    }

    @ParameterizedTest(name = "Średnia dla ocen {1},{2},{3}")
    @CsvSource({
            "2021,5,2022,5,5",
            "2020,3,2021,3,3",
            "2019,1,2020,1,1"
    })
    void shouldCalculateAverageForSameRatings(int year1, int rating1, int year2, int rating2, double expected) {
        Employee emp = new Employee("Anna", "Nowak", "anna@example.com", "CompanyB", Position.MANAGER);
        service.addEmployee(emp);
        service.addRating(emp, year1, rating1);
        service.addRating(emp, year2, rating2);

        double avg = service.calculateAverageRating(emp);
        assertThat(avg).isEqualTo(expected);
    }

    @ParameterizedTest(name = "Średnia dla ocen {1},{2},{3},{4} powinna wynosić {5}")
    @CsvSource({
            "2021,5,2022,3,4.0",
            "2020,2,2021,4,3.0",
            "2019,1,2020,5,3.0"
    })
    void shouldCalculateAverageForDifferentRatings(int year1, int rating1, int year2, int rating2, double expected) {
        Employee emp = new Employee("Piotr", "Zieliński", "piotr@example.com", "CompanyC", Position.MANAGER);
        service.addEmployee(emp);
        service.addRating(emp, year1, rating1);
        service.addRating(emp, year2, rating2);

        double avg = service.calculateAverageRating(emp);
        assertThat(avg).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0} średnia z ocen {1}")
    @MethodSource("provideEmployeesWithRatings")
    void shouldCalculateAverageUsingMethodSource(Employee emp, List<Integer> ratings, double expectedAvg) {
        service.addEmployee(emp);
        int year = 2020;
        for (int rating : ratings) {
            service.addRating(emp, year++, rating);
        }

        double avg = service.calculateAverageRating(emp);
        assertThat(avg).isEqualTo(expectedAvg);
    }

    static Stream<Arguments> provideEmployeesWithRatings() {
        return Stream.of(
                Arguments.of(
                        new Employee("Marek", "Kowal", "marek@example.com", "CompanyE", Position.PREZES),
                        List.of(5, 4, 3),
                        4.0
                ),
                Arguments.of(
                        new Employee("Ela", "Nowak", "ela@example.com", "CompanyF", Position.MANAGER),
                        List.of(1, 2, 3, 4, 5),
                        3.0
                )
        );
    }

    @ParameterizedTest(name = "Top rating employee wśród {0} i {1}")
    @CsvSource({
            "Jan Kowalski, Anna Nowak",
            "Piotr Zieliński, Ela Nowak"
    })
    void shouldReturnTopRatingEmployee(String empName1, String empName2) {
        Employee emp1 = new Employee(empName1.split(" ")[0], empName1.split(" ")[1], empName1 + "@example.com", "CompanyX", Position.PREZES);
        Employee emp2 = new Employee(empName2.split(" ")[0], empName2.split(" ")[1], empName2 + "@example.com", "CompanyY", Position.PREZES);
        service.addEmployee(emp1);
        service.addEmployee(emp2);

        service.addRating(emp1, 2023, 4);
        service.addRating(emp2, 2023, 5);

        Employee top = service.getTopRatingEmployee();
        assertThat(top.getFullName()).isEqualTo(empName2);
    }
}
