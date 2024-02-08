package com.mete.authentication.token;

import com.mete.userInfo.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private boolean revoked;
    private String token;
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserInfo user;
}
