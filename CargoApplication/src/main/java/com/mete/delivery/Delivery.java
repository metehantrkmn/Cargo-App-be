package com.mete.delivery;

import com.mete.cargo.Cargo;
import com.mete.personel.Personel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer deliveryId;
    @ManyToOne
    @JoinColumn(name = "pnrNo")
    private Cargo cargo;
    private Date deliveryDate;
    @Enumerated(EnumType.STRING)
    private STATUS status;
    private String recieverId;
    @OneToOne
    @JoinColumn(name = "personelId")
    private Personel personel;

}
