package com.mete.userInfo;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo implements UserDetails {

    @Id
    private String userId;
    @Enumerated(EnumType.STRING)
    private ROLE role;
    private String email;
    private String phoneNumber;
    private String name;
    private String surname;
    @Enumerated(EnumType.STRING)
    private SEX sex;
    private String password;
    private Date registrationDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static String generateRandomNumberString(int length) {
        // Define the characters allowed in the random string (0-9)
        String allowedChars = "0123456789";

        // Create a StringBuilder to build the random number string
        StringBuilder randomStringBuilder = new StringBuilder(length);

        // Create a Random object
        Random random = new Random();

        // Generate the random number string
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            randomStringBuilder.append(randomChar);
        }

        // Convert StringBuilder to String
        return randomStringBuilder.toString();
    }

}
