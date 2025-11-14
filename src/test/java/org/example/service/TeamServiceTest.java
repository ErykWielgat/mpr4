package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.model.ProjectTeam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class TeamServiceTest {

    private final TeamService service = new TeamService();

    @Test
    void shouldCreateTeamWithGivenNameAndMaxSize() {
        ProjectTeam team = service.createTeam("Alpha", 3);

        assertThat(team.getName()).isEqualTo("Alpha");
        assertThat(team.getMaxSize()).isEqualTo(3);
        assertThat(team.getEmployees()).isEmpty();
    }

    @ParameterizedTest(name = "Dodanie pracownika {0} do zespołu powinno działać poprawnie")
    @CsvSource({
            "MANAGER",
            "PROGRAMISTA"
    })
    void shouldAddEmployeeToTeam(Position pos) {
        ProjectTeam team = service.createTeam("Beta", 2);
        Employee emp = new Employee("Jan", "Kowalski", "jan@example.com", "Comp", pos);

        service.addEmployeeToTeam(emp, team);

        assertThat(team.getEmployees())
                .hasSize(1)
                .extracting(Employee::getPosition)
                .containsExactly(pos);
    }

    @Test
    void shouldNotAddEmployeeWhenTeamIsFull() {
        ProjectTeam team = service.createTeam("Gamma", 2);
        Employee e1 = new Employee("A", "A", "a@example.com", "Comp", Position.MANAGER);
        Employee e2 = new Employee("B", "B", "b@example.com", "Comp", Position.PROGRAMISTA);
        Employee e3 = new Employee("C", "C", "c@example.com", "Wino", Position.PROGRAMISTA);

        service.addEmployeeToTeam(e1, team);
        service.addEmployeeToTeam(e2, team);

        assertThatThrownBy(() -> service.addEmployeeToTeam(e3, team))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Team is full");
    }

    @Test
    void shouldValidateTeamCompositionWithManagerAndProgrammer() {
        ProjectTeam team = service.createTeam("Delta", 5);
        service.addEmployeeToTeam(new Employee("M", "M", "m@example.com", "Comp", Position.MANAGER), team);
        service.addEmployeeToTeam(new Employee("P", "P", "p@example.com", "Comp", Position.PROGRAMISTA), team);

        assertThat(service.validateTeamComposition(team)).isTrue();
    }

    @Test
    void shouldFailValidationWhenNoManagerOrNoProgrammer() {
        ProjectTeam team = service.createTeam("Epsilon", 5);
        service.addEmployeeToTeam(new Employee("OnlyDev", "Dev", "dev@example.com", "Comp", Position.PROGRAMISTA), team);

        assertThat(service.validateTeamComposition(team)).isFalse();
    }

    @Test
    void shouldMoveEmployeeBetweenTeams() {
        ProjectTeam from = service.createTeam("From", 5);
        ProjectTeam to = service.createTeam("To", 5);
        Employee emp = new Employee("X", "X", "x@example.com", "Comp", Position.PROGRAMISTA);

        service.addEmployeeToTeam(emp, from);
        service.moveEmployee(emp, from, to);

        assertThat(from.getEmployees()).isEmpty();
        assertThat(to.getEmployees()).contains(emp);
    }

    @Test
    void shouldNotMoveEmployeeIfAlreadyInTargetTeam() {
        ProjectTeam from = service.createTeam("From", 5);
        ProjectTeam to = service.createTeam("To", 5);
        Employee emp = new Employee("Y", "Y", "y@example.com", "Comp", Position.MANAGER);

        service.addEmployeeToTeam(emp, from);
        service.addEmployeeToTeam(emp, to);

        assertThatThrownBy(() -> service.moveEmployee(emp, from, to))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already in team");
    }

    @Test
    void shouldNotMoveEmployeeIfTargetTeamIsFull() {
        ProjectTeam from = service.createTeam("From", 5);
        ProjectTeam to = service.createTeam("To", 2);

        Employee emp = new Employee("Z", "Z", "z@example.com", "Comp", Position.PROGRAMISTA);

        service.addEmployeeToTeam(emp, from);
        service.addEmployeeToTeam(new Employee("Other", "Other", "o@example.com", "Comp", Position.MANAGER), to);
        service.addEmployeeToTeam(new Employee("Dev", "Dev", "dev@example.com", "Comp", Position.PROGRAMISTA), to);

        assertThatThrownBy(() -> service.moveEmployee(emp, from, to))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Team is full");
    }

}
