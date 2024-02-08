package com.mete.branch;

import com.mete.cargo.Cargo;
import com.mete.cargo.CargoRepository;
import com.mete.cargo.NoSuchBranchException;
import com.mete.config.AuthenticationFilter;
import com.mete.personel.NoSuchPersonelException;
import com.mete.personel.Personel;
import com.mete.personel.PersonelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final PersonelRepository personelRepository;
    private final CargoRepository cargoRepository;

    public ResponseEntity addBranch(BranchController.BranchRecord body){

        AuthenticationFilter.UserRecord userRecord = (AuthenticationFilter.UserRecord) SecurityContextHolder.getContext()
                                                                                .getAuthentication().getDetails();

        Personel manager = personelRepository.findPersonelByPersonelId(userRecord.userId()).orElseThrow(
                () -> new NoSuchPersonelException("no such personel exist in database")
        );

        Branch branch = Branch.builder()
                .branchManager(
                        manager
                )
                .branchAddress(body.branchAddress())
                .branchName(body.branchName())
                .build();

        branchRepository.save(
                branch
        );

        //branch field of manager is empty
        //we must also set that field
        manager.setBranch(branch);

        personelRepository.save(manager);

        return ResponseEntity.ok("succesfully added branch");
    }

    public ResponseEntity deleteBranch(Integer branchId){

        if(!branchRepository.existsById(branchId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("branch not found");

        branchRepository.deleteById(branchId);

        return ResponseEntity.ok("branch succesfully deleted");
    }

    public ResponseEntity addPersonel(String personelId, Integer branchId){

        if(!personelRepository.existsById(personelId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no such perosnel with that id");

        Personel personel = personelRepository.findPersonelByPersonelId(personelId).get();
        personel.setBranch(
                branchRepository.findBranchByBranchId(branchId).orElseThrow(
                        () -> new NoSuchBranchException("no such branch with that id")
                )
        );

        personelRepository.save(personel);

        return ResponseEntity.ok("succesfully added new personel to the branch");
    }

    public ResponseEntity getCargos(Integer branchId){

        Branch  branch = branchRepository.findBranchByBranchId(branchId).orElseThrow(
                () -> new NoSuchBranchException("no such branch wit taht branchId")
        );
        return ResponseEntity.ok(branch.cargos);
    }

    public ResponseEntity allPersonel(Integer branchId){
        Branch  branch = branchRepository.findBranchByBranchId(branchId).orElseThrow(
                () -> new NoSuchBranchException("no such branch wit that branchId")
        );
        return ResponseEntity.ok(branch.personelList);
    }

    public ResponseEntity getCargosAtThisBranch(Integer branchId){

        List<Cargo> list = cargoRepository.findCargosAtBranch(branchId);
        return ResponseEntity.ok(list);
    }


}
