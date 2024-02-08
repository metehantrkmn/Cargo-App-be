package com.mete.cargo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mete.branch.Branch;
import jakarta.persistence.*;
import lombok.*;

import java.security.SecureRandom;
import java.sql.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cargo {

    @Id
    private String pnrNo;
    @NonNull
    private Date deliveryDate;

    //since user dont have to use the application
    //this field will not mapped to the table userInfo
    //customers which are not using the application itsel also can deliver their cargo
    //and they can follow their delivery using pnr no
    //But they will not be able to follow their all delivery using their id no
    @NonNull
    private String senderIdentityNo;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "branchId")
    private Branch branch;
    @NonNull
    private String receiverAddress;
    private String senderAddress;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String receiverName;
    @NonNull
    private String receiverSurname;


    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomString = new StringBuilder();

        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

}
