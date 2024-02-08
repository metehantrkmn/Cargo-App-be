package com.mete.branch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mete.cargo.Cargo;
import com.mete.delivery.Delivery;
import com.mete.personel.Personel;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int branchId;
    String branchAddress;
    String branchName;
    @OneToOne
    @JoinColumn(name = "managerId")
    @JsonManagedReference
    Personel branchManager;
    @JsonManagedReference
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    List<Personel> personelList;

    //@Data annotation creates some methods like toString ...
    //In initialization process toString method creates a reference to branch
    //but branch object also have references to cargo
    //so this cauese circular dependency problem
    //use @ToString.Exclude to prevent this problem or Handle the dependencies
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    List<Cargo> cargos;


}
