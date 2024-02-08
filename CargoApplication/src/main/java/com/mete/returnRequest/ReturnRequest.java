package com.mete.returnRequest;

import com.mete.cargo.Cargo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReturnRequest {

    @Id
    @GeneratedValue
    private Integer returnId;
    @OneToOne
    @JoinColumn(name = "PNRNO")
    private Cargo cargo;
    private Date returnDate;

}
