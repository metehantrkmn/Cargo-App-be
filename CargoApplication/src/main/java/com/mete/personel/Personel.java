package com.mete.personel;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mete.branch.Branch;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Personel {

    @Id
    private String personelId;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "branchId")
    private Branch branch;

}
