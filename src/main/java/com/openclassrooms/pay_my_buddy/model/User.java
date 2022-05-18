package com.openclassrooms.pay_my_buddy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity(name = "User")
@DynamicUpdate
@Transactional
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name" )
    private String lastName;

    @Column(name = "user_name", unique = true, length = 100)
    @NotBlank(message = "UserName may not be empty and null")
    private String userName;

    @Column(name = "email", unique = true, length = 100)
    @NotBlank(message = "Email may not be empty and null")
    private String email;

    @NotBlank(message = "Password may not be empty and null")
    private String password;

    private double balance;

    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinTable(name = "contact",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<User> contacts = new HashSet<>();

    @OneToMany(targetEntity = Transaction.class, mappedBy="userPay")
    private List<Transaction> transactions = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserBankAccount userBankAccount;

    public User() {
    }

    public User(String firstName, String lastName,
                String userName, String email, String password,
                double balance, Set<User> contacts, List<Transaction> transactions,
                UserBankAccount userBankAccount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.contacts = contacts;
        this.transactions = transactions;
        this.userBankAccount = userBankAccount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Set<User> getContacts() {
        return contacts;
    }

    public void setContacts(Set<User> contacts) {
        this.contacts = contacts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public UserBankAccount getUserBankAccount() {
        return userBankAccount;
    }

    public void setUserBankAccount(UserBankAccount userBankAccount) {
        this.userBankAccount = userBankAccount;
    }


}
