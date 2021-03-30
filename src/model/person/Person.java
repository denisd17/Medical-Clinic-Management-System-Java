package model.person;

import java.util.Objects;

public class Person {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String cnp;

    public Person() {

    }

    public Person(String firstName, String lastName, String phoneNumber, String email, String cnp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.cnp = cnp;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + "\n" +
                "Phone number: " + phoneNumber + "\n" +
                "Email: " + email + "\n" +
                "CNP: " + cnp + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;

        return firstName.equals(person.firstName)
                && lastName.equals(person.lastName)
                && phoneNumber.equals(person.phoneNumber)
                && email.equals(person.email)
                && cnp.equals(person.cnp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phoneNumber, email, cnp);
    }
}
