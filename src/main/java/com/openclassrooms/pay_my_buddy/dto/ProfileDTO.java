package com.openclassrooms.pay_my_buddy.dto;
import java.util.Set;

public class ProfileDTO {

    private String firstName;
    private String lastName;
    private String email;
    private Set<String> connections;
    private double balance;
    private Set<String> roles;

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public ProfileDTO() {
    }

    public ProfileDTO(String firstName,
                      String lastName, String email, Set<String> connections,
                      double balance, Set<String> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.connections = connections;
        this.balance = balance;
        this.roles = roles;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getConnections() {
        return connections;
    }

    public void setConnections(Set<String> connections) {
        this.connections = connections;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
