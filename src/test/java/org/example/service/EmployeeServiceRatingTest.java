package org.example.service;
import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeServiceRatingTest {

    private final EmployeeService service = new EmployeeService();

    @Test
    void shouldAddRatingToEmployee() {
        Employee employee = new Employee("Jan", "Kowalski", "jan@example.com", "CompanyA", Position.PROGRAMISTA);
        service.addRating(employee, 5);
        assertThat(service.getRatingsHistory(employee)).containsExactly(5);
    }

    @Test
    void shouldCalculateAverageRating() {
        Employee employee = new Employee("Anna", "Nowak", "anna@example.com", "CompanyB", Position.MANAGER);
        double avg = service.calculateAverageRating(employee);
        assertThat(avg).isEqualTo(4.0);
    }

    @Test
    void shouldReturnTopRatingEmployee() {

        Employee top = service.getTopRatingEmployee();
        assertThat(top).isNotNull();
    }
}
