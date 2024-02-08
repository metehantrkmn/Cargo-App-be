package com.mete.returnRequest;

import com.mete.RestService;
import com.mete.branch.BranchRepository;
import com.mete.cargo.Cargo;
import com.mete.cargo.CargoRepository;
import com.mete.cargo.NoSuchBranchException;
import com.mete.cargo.NoSuchCargoException;
import com.mete.userInfo.UserInfo;
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
public class ReturnService {

    private final ReturnRequestRepository returnRequestRepository;
    private final CargoRepository cargoRepository;
    private final RestService restService;
    private final BranchRepository branchRepository;

    public ResponseEntity addReturn(String pnrNo, Integer branchId){

        //todo => send email to both sender and reciever about return request

        if(returnRequestRepository.existsByCargo_PnrNo(pnrNo))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("this cargo already requested to return");

        Cargo cargo = cargoRepository.findCargoByPnrNo(pnrNo).orElseThrow(
                () -> new NoSuchCargoException("no such cargo with that pnrno")
        );

        RestService.IdentityRecord resender = restService
                .getUserByNameSurname(cargo.getReceiverName(), cargo.getReceiverSurname());

        ReturnRequest returnRequest = ReturnRequest.builder()
                .returnDate(Date.valueOf(LocalDate.now()))
                .cargo(cargo)
                .build();

        returnRequestRepository.save(returnRequest);

        /*
        burada return için sender adress ve reciever adress yer değişterecek
        ek olarak yeni oluşturalacak kargo kaydında branch kargonun o an bulunduğu subeye set edilecek
         */

        RestService.IdentityRecord newReciever = restService.getUserInfoFromIdentityService(cargo.getSenderIdentityNo());

        String newPnrNo = Cargo.generateRandomString(6);
        Cargo returnedCargo = Cargo.builder()
                .pnrNo(newPnrNo)
                .phoneNumber(cargo.getPhoneNumber())
                .senderIdentityNo(resender.identity())
                .receiverAddress(cargo.getSenderAddress())
                .senderAddress(cargo.getReceiverAddress())
                .branch(
                        branchRepository.findBranchByBranchId(branchId).orElseThrow(
                                () -> new NoSuchBranchException("NO SUCH BRANCH!!!")
                        )
                )
                .receiverName(newReciever.name())
                .receiverSurname(newReciever.surname())
                .deliveryDate(Date.valueOf(LocalDate.now()))
                .build();
        cargoRepository.save(returnedCargo);

        restService.sendRequestToMailingService(
                new RestService.MailingRecord(
                        "routingMailing",
                        resender.email(),
                        resender.name(),
                        returnedCargo.getPnrNo(),
                        6
                )
        );

        restService.sendRequestToMailingService(
                new RestService.MailingRecord(
                        "routingMailing",
                        newReciever.email(),
                        newReciever.name(),
                        cargo.getPnrNo(),
                        7
                )
        );

        RestService.FirebaseRecord firebaseRecord = new RestService.FirebaseRecord(
                "routingNotification",
                "Cargo Return",
                "Your cargo return has been saved",
                Map.of("pnrNo",newPnrNo,
                        "Returned-brancAddress",returnedCargo.getBranch().getBranchAddress())
        );
        restService.sendRequestToFirebaseService(firebaseRecord);

        return ResponseEntity.ok("succesfuly created a return request");
    }

    public ResponseEntity getAllReturns(){
        List<ReturnRequest> list = returnRequestRepository.findAll();
        return ResponseEntity.ok(list);
    }

}
