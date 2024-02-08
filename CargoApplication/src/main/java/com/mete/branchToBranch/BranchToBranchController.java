package com.mete.branchToBranch;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequiredArgsConstructor
public class BranchToBranchController {

    private final BranchToBranchService btobService;

    @GetMapping("/api/btob/get-all")
    public ResponseEntity getBtoB(){
        return btobService.getAllDeliveries();
    }

    @GetMapping("/api/btob/get-by-pnrNo")
    public ResponseEntity getByCargo(@RequestParam(name = "pnrNo") String pnrNo){
        return btobService.getDeliveryRouteByCargo(pnrNo);
    }

    public record BtobRecord(
            String pnrNo,
            String personelId,
            Integer branchId,
            Date recordDate,
            STATUS status
    ){}
    @PostMapping("/api/btob/add-btob")
    public ResponseEntity addBtoB(@RequestBody BtobRecord body){
        return btobService.addBTOB(body);
    }


}
