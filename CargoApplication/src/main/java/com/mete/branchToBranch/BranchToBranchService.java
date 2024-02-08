package com.mete.branchToBranch;

import com.mete.RestService;
import com.mete.branch.BranchRepository;
import com.mete.cargo.Cargo;
import com.mete.cargo.CargoRepository;
import com.mete.cargo.NoSuchBranchException;
import com.mete.cargo.NoSuchCargoException;
import com.mete.personel.NoSuchPersonelException;
import com.mete.personel.PersonelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BranchToBranchService {

    private final BranchToBranchRepository branchToBranchRepository;
    private final CargoRepository cargoRepository;
    private final BranchRepository branchRepository;
    private final PersonelRepository personelRepository;
    private final RestService restService;

    public ResponseEntity getAllDeliveries(){
        List<BranchToBranch> list = branchToBranchRepository.findAll();
        return ResponseEntity.ok(list);
    }

    public ResponseEntity getDeliveryRouteByCargo(String pnrno){

        if(!cargoRepository.existsById(pnrno))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no such cargo with that pnrno");

        List<BranchToBranch> list = branchToBranchRepository.findAllByCargo_PnrNo(pnrno);
        return ResponseEntity.ok(list);
    }

    public ResponseEntity addBTOB(BranchToBranchController.BtobRecord body){

        //additional checks like if personel is the personel of the branch which makes the shipment
        //or cargo is at the branch as record say

        Cargo cargo = cargoRepository.findCargoByPnrNo(body.pnrNo()).orElseThrow(
                () -> new NoSuchCargoException("no such cargo exists")
        );

        BranchToBranch btob = branchToBranchRepository.save(
                BranchToBranch.builder()
                        .toBranch(branchRepository.findBranchByBranchId(body.branchId()).orElseThrow(
                                () -> new NoSuchBranchException("no such branch")
                        ))
                        .status(body.status())
                        .recordDate(Date.valueOf(LocalDate.now()))
                        .personel(personelRepository.findPersonelByPersonelId(body.personelId()).orElseThrow(
                                () -> new NoSuchPersonelException("no such personel")
                        ))
                        .cargo(cargo)
                        .build()
        );

        RestService.FirebaseRecord firebaseRecord = new RestService.FirebaseRecord(
                "routingNotification",
                "Branch To Branch Transfer",
                "Your cargo has been transfered to the other branch",
                Map.of("pnrNo",cargo.getPnrNo(),
                        "brancAddress",btob.getToBranch().getBranchAddress(),
                        "status",btob.getStatus().name())
        );
        restService.sendRequestToFirebaseService(firebaseRecord);

        return ResponseEntity.ok("succesfully added new btob record");
    }


}
