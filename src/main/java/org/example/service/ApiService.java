package org.example.service;

import com.google.gson.*;
import org.example.model.*;
import org.example.exception.ApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
import java.util.List;

public class ApiService {

    public List<Employee> fetchEmployeesFromApi() throws ApiException {
        List<Employee> employees = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://jsonplaceholder.typicode.com/users")).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ApiException("błąd HTTP: " + response.statusCode());
            }

            Gson gson = new Gson();
            JsonArray users = gson.fromJson(response.body(), JsonArray.class);

            for (JsonElement element : users) {
                JsonObject user = element.getAsJsonObject();

                String fullName = user.get("name").getAsString();
                String[] parts = fullName.split(" ", 2);
                String firstName = parts[0];
                String lastName = parts[1];
                String email = user.get("email").getAsString();
                String company = user.getAsJsonObject("company").get("name").getAsString();
                employees.add(new Employee(firstName, lastName, email, company, Position.PROGRAMISTA));

            }

        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            throw new ApiException("Błąd pobierania danych z API: " + e.getMessage());
        }

        return employees;
    }
}

