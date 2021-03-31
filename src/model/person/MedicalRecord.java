package model.person;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MedicalRecord {
    boolean smoker;
    Set<String> alergies = new HashSet<>();
    Set<String> chronicDiseases = new HashSet<>();


    public MedicalRecord() {
    }

    public MedicalRecord(boolean smoker, Set<String> alergies, Set<String> chronicDiseases) {
        this.smoker = smoker;
        this.alergies = alergies;
        this.chronicDiseases = chronicDiseases;
    }

    public boolean getSmoker() {
        return smoker;
    }

    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }

    public Set<String> getAlergies() {
        return alergies;
    }

    public void setAlergies(Set<String> alergies) {
        this.alergies = alergies;
    }

    public Set<String> getChronicDiseases() {
        return chronicDiseases;
    }

    public void setChronicDiseases(Set<String> chronicDiseases) {
        this.chronicDiseases = chronicDiseases;
    }

    public void addAlergy(String alergy) {
        alergies.add(alergy);
    }

    public void addDisease(String disease) {
        chronicDiseases.add(disease);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("The patient is ");

        if (smoker) {
            result.append("a smoker.\n");
        }
        else {
            result.append("not a smoker.\n");
        }

        if (alergies.isEmpty()) {
            result.append("There are no recorded alergies for this patient.\n");
        }
        else {
            result.append("The patient suffers from the following alergies:\n");

            for (String alergy : alergies) {
                result.append("- ").append(alergy).append("\n");
            }
        }

        if (chronicDiseases.isEmpty()) {
            result.append("There are no recorded chronic diseases for this patient.\n");
        }
        else {
            result.append("The patient suffers from the following chronic diseases:\n");

            for (String disease : chronicDiseases) {
                result.append("- ").append(disease).append("\n");
            }
        }

        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MedicalRecord medicalRecord = (MedicalRecord) o;
        return smoker == medicalRecord.smoker
                && alergies.equals(medicalRecord.alergies)
                && chronicDiseases.equals(medicalRecord.chronicDiseases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smoker, alergies, chronicDiseases);
    }
}
