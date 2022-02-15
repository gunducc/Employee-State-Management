package com.workmotion.statemachine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "employees", uniqueConstraints = {@UniqueConstraint(columnNames={"email","passport_number"})}
)

public class Employee {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(name = "first_name", nullable = false)
    @Size(min=2, max = 100, message = "Firstname length should be between 2 and 100 chars")
    private String firstName;
    @Column(name = "last_name", nullable = false)
    @Size(min=2, max = 100, message = "Lastname length should be between 2 and 100 chars")
    private String lastName;
    @Column(name = "email", nullable = false)
    @Email(message = "Email address format is not correct")
    private String email;
    @Column(name = "address")
    private String address;
    @Column(name = "age")
    @Min(value = 18, message = "Employees under 18 are not allowed to work")
    private Integer age;
    @Column(name = "passport_number", nullable = false)
    @Min(value = 9, message = "Passport number length should be greater than 8 digits")
    private String passportNumber;
    @Column(name = "creation_date")
    private OffsetDateTime creationDate;
    @Column(name = "state", nullable = false)
    private String state;


}