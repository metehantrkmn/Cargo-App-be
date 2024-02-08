package com.mete;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class UserInfo {
    @Id
    private String identity;
    private String name;
    private String surname;
    @Enumerated(EnumType.STRING)
    private SEX sex;
    private Date birthDate;
    private String email;
}
