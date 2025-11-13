package org.example.service;
import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.assertj.core.api.Assertions.assertThat;

class EmployeeServiceRatingTest {

    private EmployeeService service;
    private Employee e1;
    private Employee e2;

    @BeforeEach
    void setUp() {
        service = new EmployeeService();

        e1 = new Employee("Jan", "Kowalski", "jan@example.com", "CompanyA", Position.PROGRAMISTA);
        e2 = new Employee("Anna", "Nowak", "anna@example.com", "CompanyB", Position.MANAGER);
        service.addEmployee(e1);
        service.addEmployee(e2);

        service.addRating(e1, 2022, 4);
        service.addRating(e1, 2023, 5);
        service.addRating(e1, 2024, 4);
        service.addRating(e2, 2022, 3);
        service.addRating(e2, 2023, 3);
        service.addRating(e2, 2024, 4);
    }

    @Test
    void shouldCalculateAverageRating() {
        double avgE1 = service.calculateAverageRating(e1, 2022, 2024);
        double avgE2 = service.calculateAverageRating(e2, 2022, 2024);

        assertEquals(4.33, avgE1, 0.01);
        assertEquals(3.33, avgE2, 0.01);
    }

    @Test
    void shouldReturnTopRatingEmployee() {
        Employee top = service.getTopRatingEmployee(2022, 2024);
        assertEquals(e1, top);
    }
}
