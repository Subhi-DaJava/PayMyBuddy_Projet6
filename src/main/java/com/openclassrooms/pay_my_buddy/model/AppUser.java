package com.openclassrooms.pay_my_buddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity(name = "AppUser")
@DynamicUpdate
@Transactional
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int appUserid;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true, length = 100)
    @NotBlank(message = "Email may not be empty and null")
    private String email;

    @NotBlank(message = "Password may not be empty and null")
    @JsonIgnore
    private String password;

    private double balance;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "contact",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "target_id")
    )
    private Set<AppUser> contacts = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "source")
    private List<Transaction> transactionsSources = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "target")
    private List<Transaction> transactionsTarget = new ArrayList<>();

    @OneToOne(mappedBy = "appUser")
    private UserBankAccount userBankAccount;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    public AppUser() {

    }

    public AppUser(int appUserid, String firstName, String lastName, String email, String password, double balance, Set<AppUser> contacts, List<Transaction> transactionsSources, List<Transaction> transactionsTarget, UserBankAccount userBankAccount, List<Role> roles) {
        this.appUserid = appUserid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.contacts = contacts;
        this.transactionsSources = transactionsSources;
        this.transactionsTarget = transactionsTarget;
        this.userBankAccount = userBankAccount;
        this.roles = roles;
    }

    public AppUser(String firstName, String lastName, String email, String password, double balance, Set<AppUser> contacts, List<Transaction> transactionsSources, List<Transaction> transactionsTarget, UserBankAccount userBankAccount, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.contacts = contacts;
        this.transactionsSources = transactionsSources;
        this.transactionsTarget = transactionsTarget;
        this.userBankAccount = userBankAccount;
        this.roles = roles;
    }

    public AppUser(String firstName, String lastName, String email, String password, double balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public int getAppUserid() {
        return appUserid;
    }

    public void setAppUserid(int appUserid) {
        this.appUserid = appUserid;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<AppUser> getContacts() {
        return contacts;
    }

    public void setContacts(Set<AppUser> contacts) {
        this.contacts = contacts;
    }

    public List<Transaction> getTransactionsSources() {
        return transactionsSources;
    }

    public void setTransactionsSources(List<Transaction> transactionsSources) {
        this.transactionsSources = transactionsSources;
    }

    public List<Transaction> getTransactionsTarget() {
        return transactionsTarget;
    }

    public void setTransactionsTarget(List<Transaction> transactionsTarget) {
        this.transactionsTarget = transactionsTarget;
    }

    public UserBankAccount getUserBankAccount() {
        return userBankAccount;
    }

    public void setUserBankAccount(UserBankAccount userBankAccount) {
        this.userBankAccount = userBankAccount;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "appUserid=" + appUserid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", contacts=" + contacts +
                ", transactionsSources=" + transactionsSources +
                ", transactionsTarget=" + transactionsTarget +
                ", userBankAccount=" + userBankAccount +
                ", roles=" + roles +
                '}';
    }
}
