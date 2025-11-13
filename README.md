1. Na początku stworzyliśmy testy parametryczne w EmployeeServiceTest
-Testy podwyżek giveRaise sprawdzające różne procenty podwyżki.
-Testy awansów promote sprawdzające zmianę stanowiska i pensji.

Testy początkowo nie przechodziły bo metody nie były zaimplementowane. To jest faza Red która pokazuje jak system ma sie zachowywać.

2. Green (implementacja minimalna)
Zaimplementowano metody w EmployeeService minimalnie, aby testy przeszły:

//////////////////

public void promote(Employee employee, Position newPosition) {
    if (newPosition.getHierarchyLevel() >= employee.getPosition().getHierarchyLevel()) {
        throw new IllegalArgumentException(
            "Awans możliwy tylko na wyższe stanowisko niż obecne: " + employee.getPosition());
    }
    employee.setSalary(newPosition.getBaseSalary());
    employee.setPosition(newPosition);
}

public void giveRaise(Employee employee, double percentage) {
    if (percentage <= 0) {
        throw new IllegalArgumentException("Procent podwyżki musi być dodatni");
    }
    double newSalary = employee.getSalary() * (1 + percentage / 100);
    if (newSalary > employee.getPosition().getMaxSalary()) {
        throw new IllegalArgumentException(
            "Przekroczono maksymalną pensję dla stanowiska " + employee.getPosition());
    }
    employee.setSalary(newSalary);
}

///////////

Po tej implementacji wszystkie testy przeszły.

3. Refactor (ulepszanie testów i kodu)
Testy zostały uporządkowane i zrefaktoryzowane użyłem @MethodSource dla złożonych scenariuszy i @EnumSource dla wartości enum. Testy stały się bardziej ogólne i czytelne.
Kod produkcyjny nie zawiera duplikacji i jest zgodny z zasadami czystego kodu.