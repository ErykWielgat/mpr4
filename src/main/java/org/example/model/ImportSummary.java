package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class ImportSummary {
    private int importedCount;
    private List<String> errors;

    public ImportSummary() {
        this.errors = new ArrayList<>();
    }

    public void addError(String error) {
        errors.add(error);
    }

    public void incrementImported() {
        importedCount++;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "Zaimportowano: " + importedCount + ", Bledow: " + errors.size() + "\n" + String.join("\n", errors);
    }
}

