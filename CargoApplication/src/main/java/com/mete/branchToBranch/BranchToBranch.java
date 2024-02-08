package com.mete.branchToBranch;

import com.mete.branch.Branch;
import com.mete.cargo.Cargo;
import com.mete.personel.Personel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchToBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "pnrNo")
    private Cargo cargo;
    @ManyToOne
    @JoinColumn(name = "personelId")
    private Personel personel;
    @ManyToOne
    @JoinColumn(name = "toBranch")
    private Branch toBranch;
    private Date recordDate;
    @Enumerated(EnumType.STRING)
    private STATUS status;

}
