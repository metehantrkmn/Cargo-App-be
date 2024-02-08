package com.mete.cargo;

import com.mete.branch.Branch;
import com.mete.branch.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CargoController {

    private final CargoRepository cargoRepository;

    //deneme icin
    private final BranchRepository branchRepository;
    private final CargoService cargoService;

    public record CargoRecord(
            String senderIdentityNo,
            Integer branchId,
            String receiverAddress,
            String senderAddress,
            String phoneNumber,
            String receiverName,
            String receiverSurname,
            String personelId
    ){}
    @PostMapping("/api/cargo/add-cargo")
    public ResponseEntity addCargo(@RequestBody CargoRecord cargo){
        return cargoService.addCargo(cargo);
    }

    //this endpoint will be open to all
    @GetMapping("/api/cargo/get-cargo-record")
    public ResponseEntity getCargoByPnrNo(@RequestParam String pnrno){
        return cargoService.getCargoByPnrNo(pnrno);
    }

    @GetMapping("/api/cargo/get-all-cargo")
    public ResponseEntity getAllCargo(@RequestParam String userId){
        return cargoService.getAllCargo(userId);
    }

    @GetMapping("/api/cargo/get-cargo_location")
    public ResponseEntity getCargoLocation(@RequestParam(name = "pnrNo") String pnrNo){
        return cargoService.getCargoLocation(pnrNo);
    }

    @GetMapping("/api/cargo/get-cargo-route")
    public ResponseEntity getCargoRoute(@RequestParam(name = "pnrNo") String pnrNo){
        return cargoService.getCargoRoute(pnrNo);
    }


}
