package com.mete.branch;

import com.mete.personel.Personel;
import com.mete.userInfo.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BranchController {

    private final BranchRepository branchRepository;
    private final BranchService branchService;

    @GetMapping("/api/branch/deneme")
    public void deneme(){
        branchRepository.save(
                Branch.builder()
                        .branchId(1)
                        .branchAddress("Kukurtlu mahallesi 4. kelebek sokak")
                        .branchName("cekirge subesi")
                        .build()
        );
        Optional<Branch> mockUser = branchRepository.findBranchByBranchId(1);

        if(mockUser.isPresent())
            System.out.println(mockUser.get());

        System.out.println(mockUser.get());

    }


    public record BranchRecord(
            String branchAddress,
            String branchName
    ){}
    @PostMapping("/api/branch/add-branch")
    public ResponseEntity addBranch(@RequestBody BranchRecord body){
        return branchService.addBranch(body);
    }


    @DeleteMapping("/api/branch/delete-branch")
    public ResponseEntity deleteBranch(@RequestParam(name = "branchId") Integer branchId){
        return branchService.deleteBranch(branchId);
    }

    public record PersonelRecord(
        String personelId,
        Integer branchId
    ){}
    @PostMapping("/api/branch/add-personel")
    public ResponseEntity addPersonel(@RequestBody PersonelRecord personelRecord){
        return branchService.addPersonel(personelRecord.personelId,personelRecord.branchId);
    }

    @GetMapping("/api/branch/cargos")
    public ResponseEntity getCargosByBranch(@RequestParam Integer branchId){
        return branchService.getCargos(branchId);
    }

    @GetMapping("/api/branch/all-personel")
    public ResponseEntity allPersonel(@RequestParam Integer branchId){
        return branchService.allPersonel(branchId);
    }

    //add another controller
    //branch Id ver
    //

    @GetMapping("/api/branch/cargos-in-branch")
    public ResponseEntity getCargostAtThisBranch(@RequestParam(name = "branchId") Integer branchId){
        return branchService.getCargosAtThisBranch(branchId);
    }


}
