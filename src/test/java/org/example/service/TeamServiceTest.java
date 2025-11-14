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


    @ParameterizedTest(name = "Tworzenie zespołu z maxSize={0} → oczekiwany sukces: {1}")
    @CsvSource({
            "1,false",
            "3,true",
            "5,true"
    })
    void shouldCreateTeamDependingOnMaxSize(int maxSize, boolean expectedSuccess) {
        if (expectedSuccess) {
            ProjectTeam team = service.createTeam("Team" + maxSize, maxSize);

            assertThat(team.getMaxSize()).isEqualTo(maxSize);
        } else {
            assertThatThrownBy(() -> service.createTeam("Team" + maxSize, maxSize))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("at least 2");
        }
    }

    @Test
    void shouldAddEmployeeToTeam() {
        ProjectTeam team = service.createTeam("Beta", 2);
        Employee emp = new Employee("Jan", "Kowalski", "jan@example.com", "Comp", Position.PROGRAMISTA);

        service.addEmployeeToTeam(emp, team);

        assertThat(team.getEmployees())
                .hasSize(1)
                .anySatisfy(e -> {
                    assertThat(e.getPosition()).isEqualTo(Position.PROGRAMISTA);
                    assertThat(e.getEmail()).isEqualTo("jan@example.com");
                });
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
        Employee manager = new Employee("M", "M", "m@example.com", "Comp", Position.MANAGER);
        Employee programmer = new Employee("P", "P", "p@example.com", "Comp", Position.PROGRAMISTA);

        service.addEmployeeToTeam(manager, team);
        service.addEmployeeToTeam(programmer, team);

        assertThat(service.validateTeamComposition(team)).isTrue();

        assertThat(team.getEmployees())
                .anySatisfy(e -> {
                    assertThat(e.getPosition()).isEqualTo(Position.MANAGER);
                    assertThat(e.getFullName()).isEqualTo("M M");
                })
                .anySatisfy(e -> {
                    assertThat(e.getPosition()).isEqualTo(Position.PROGRAMISTA);
                    assertThat(e.getFullName()).isEqualTo("P P");
                });

        assertThat(team.getEmployees()).hasSize(2);
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
        assertThat(to.getEmployees())
                .hasSize(1)
                .anySatisfy(e -> {
                    assertThat(e.getFullName()).isEqualTo("X X");
                    assertThat(e.getPosition()).isEqualTo(Position.PROGRAMISTA);
                });
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
