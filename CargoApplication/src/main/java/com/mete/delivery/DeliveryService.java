package com.mete.delivery;

import com.mete.RestService;
import com.mete.branch.BranchRepository;
import com.mete.cargo.Cargo;
import com.mete.cargo.CargoRepository;
import com.mete.cargo.NoSuchBranchException;
import com.mete.cargo.NoSuchCargoException;
import com.mete.personel.NoSuchPersonelException;
import com.mete.personel.PersonelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final CargoRepository cargoRepository;
    private final PersonelRepository personelRepository;
    private final BranchRepository branchRepository;
    private final RestService restService;

    public ResponseEntity addDelivery(DeliveryController.DeliveryRecord deliveryRecord){

        //todo => send email to sender and reciever about delivery

        Cargo cargo = cargoRepository.findCargoByPnrNo(deliveryRecord.pnrno()).orElseThrow(
                () -> new NoSuchCargoException("no such cargo with that pnrno")
        );

        Delivery delivery = Delivery.builder()
                .cargo(cargo)
                .deliveryDate(Date.valueOf(LocalDate.now()))
                .status(deliveryRecord.status())
                .personel(
                        personelRepository.findPersonelByPersonelId(deliveryRecord.personelId()).orElseThrow(
                                () -> new NoSuchPersonelException("no such personel with that personelId")
                        )
                )
                .recieverId(deliveryRecord.recieverId())
                .build();

        deliveryRepository.save(delivery);

        RestService.IdentityRecord identityRecord = restService.getUserInfoFromIdentityService(
                cargo.getSenderIdentityNo());

        Integer templateNumber = null;
        if(delivery.getStatus() == STATUS.ON_DELIVERY){
            templateNumber = 4;
        }else if(delivery.getStatus() == STATUS.DELIVERED){
            templateNumber = 2;
        }else if(delivery.getStatus() == STATUS.AT_BRANCH){
            templateNumber = 5;
        }

        restService.sendRequestToMailingService(
                new RestService.MailingRecord(
                        "routingMailing",
                        identityRecord.email(),
                        identityRecord.name(),
                        cargo.getPnrNo(),
                        templateNumber
                )
        );

        RestService.FirebaseRecord firebaseRecord = new RestService.FirebaseRecord(
                "routingNotification",
                "Delivery",
                "Your cargo delivery status has been updated",
                Map.of("pnrNo",delivery.getCargo().getPnrNo(),
                        "delivery-status",delivery.getStatus().name())
        );
        restService.sendRequestToFirebaseService(firebaseRecord);

        return ResponseEntity.ok("succesfully saved delivery record");
    }

    public ResponseEntity getDeliveriesByBranch(Integer branchId){
        branchRepository.findBranchByBranchId(branchId).orElseThrow(
                () -> new NoSuchBranchException("no such branch with that branchId")
        );

        List<Delivery> deliveries = deliveryRepository.findAllByCargo_Branch_BranchId(branchId);
        return ResponseEntity.ok(deliveries);
    }

    public ResponseEntity getSucessfulDeliveries(Integer branchId){
        branchRepository.findBranchByBranchId(branchId).orElseThrow(
                () -> new NoSuchBranchException("no such branch with that branchId")
        );
        List<Delivery> deliveries = deliveryRepository.findAllByCargo_Branch_BranchIdAndStatus(branchId,STATUS.DELIVERED);
        return ResponseEntity.ok(deliveries);
    }

    public ResponseEntity getDeliveriesOnDelivery(Integer branchId){
        branchRepository.findBranchByBranchId(branchId).orElseThrow(
                () -> new NoSuchBranchException("no such branch with that branchId")
        );

        List<Delivery> deliveries = deliveryRepository.findAllByCargo_Branch_BranchIdAndStatus(branchId,STATUS.ON_DELIVERY);
        return  ResponseEntity.ok(deliveries);
    }

    public ResponseEntity getDeliveriesAtBranch(Integer branchId){
        branchRepository.findBranchByBranchId(branchId).orElseThrow(
                () -> new NoSuchBranchException("no such branch with that branchId")
        );
        List<Delivery> deliveries = deliveryRepository.findAllByCargo_Branch_BranchIdAndStatus(branchId,STATUS.AT_BRANCH);
        return ResponseEntity.ok(deliveries);
    }


}
