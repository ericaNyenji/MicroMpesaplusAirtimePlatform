package com.erica.fintech.MicroMpesaplusAirtimePlatform.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false)
    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    @NotBlank(message = "Surname is required")
    private String surname;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "National ID is required")
    private String nationalId;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;

    private String password;
    private String role;

    public User() {}

    public User(String firstName, String middleName, String surname, String phone, String nationalId, String password, String role) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.phone = phone;
        this.nationalId = nationalId;
        this.password = password;
        this.role = role;
    }


    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
