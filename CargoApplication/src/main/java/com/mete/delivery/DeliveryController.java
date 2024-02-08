package com.mete.delivery;

import com.mete.cargo.CargoRepository;
import com.mete.personel.Personel;
import com.mete.personel.PersonelRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;
    private final CargoRepository cargoRepository;
    private final PersonelRepository personelRepository;
    private final DeliveryService deliveryService;

    @GetMapping("/api/delivery/deneme")
    public void deneme(){
        deliveryRepository.save(
                Delivery.builder()
                        .deliveryId(1)
                        .cargo(
                                cargoRepository.findCargoByPnrNo("denemepnr").get()
                        )
                        .deliveryDate(Date.valueOf(LocalDate.now()))
                        .status(STATUS.ON_DELIVERY)
                        .personel(
                                personelRepository.findPersonelByPersonelId("denemeId").get()
                        )
                        .build()
        );
        Optional<Delivery> mockUser = deliveryRepository.findDeliveryByDeliveryId(1);

        if(mockUser.isPresent())
            System.out.println(mockUser.get());

        System.out.println(mockUser.get());

    }


    public record DeliveryRecord(
        String pnrno,
        STATUS status,
        String personelId,
        String recieverId
    ){}
    @PostMapping("/api/delivery/add-delivery")
    public ResponseEntity addDelivery(@RequestBody DeliveryRecord delivRecord){
        return deliveryService.addDelivery(delivRecord);
    }

    @GetMapping("/api/delivery/deliveries-by-branch")
    public ResponseEntity getDeliveriesByBranch(@RequestParam Integer branchId){
        return deliveryService.getDeliveriesByBranch(branchId);
    }

    @GetMapping("/api/delivery/succesful-deliveries")
    public ResponseEntity getSucessfulDeliveries(@RequestParam Integer branchId){
        return deliveryService.getSucessfulDeliveries(branchId);
    }

    @GetMapping("/api/delivery/at-branch-deliveries")
    public ResponseEntity getAtBranchDeliveries(@RequestParam Integer branchId){
        return deliveryService.getDeliveriesAtBranch(branchId);
    }

    @GetMapping("/api/delivery/on-delivery")
    public ResponseEntity getDeliveriesOnDelivery(@RequestParam Integer branchId){
        return deliveryService.getDeliveriesOnDelivery(branchId);
    }

}
