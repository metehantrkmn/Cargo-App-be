package com.mete.returnRequest;

import com.mete.cargo.CargoRepository;
import com.mete.personel.Personel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ReturnRequestController {

    private final ReturnRequestRepository returnRequestRepository;
    private final CargoRepository cargoRepository;
    private final ReturnService returnService;

    @GetMapping("/api/return-request/deneme")
    public void deneme(){
        returnRequestRepository.save(
                ReturnRequest.builder()
                        .returnId(1)
                        .returnDate(Date.valueOf(LocalDate.now()))
                        .cargo(
                               cargoRepository.findCargoByPnrNo("denemepnr").get()
                        )
                        .build()
        );
        Optional<ReturnRequest> mockUser = returnRequestRepository.findReturnRequestByReturnId(1);

        if(mockUser.isPresent())
            System.out.println(mockUser.get());

        System.out.println(mockUser.get());

    }

    public record ReturnRecord(
        String pnrno,
        Integer branchId
    ){}
    @PostMapping("/api/return/add-return")
    public ResponseEntity addReturn(@RequestBody ReturnRecord returnRecord){
        return returnService.addReturn(returnRecord.pnrno(), returnRecord.branchId);
    }

    @GetMapping("/api/return/all-returns")
    public ResponseEntity getAllReturns(){
        return returnService.getAllReturns();
    }

}
