package com.mete.cargo;

import com.mete.RestService;
import com.mete.branch.Branch;
import com.mete.branch.BranchRepository;
import com.mete.branchToBranch.BranchToBranch;
import com.mete.branchToBranch.BranchToBranchRepository;
import com.mete.branchToBranch.STATUS;
import com.mete.config.AuthenticationFilter;
import com.mete.delivery.Delivery;
import com.mete.delivery.DeliveryRepository;
import com.mete.personel.NoSuchPersonelException;
import com.mete.personel.PersonelRepository;
import com.mete.userInfo.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;
    private final BranchRepository branchRepository;
    private final UserInfoRepository userInfoRepository;
    private final BranchToBranchRepository branchToBranchRepository;
    private final PersonelRepository personelRepository;
    private final DeliveryRepository deliveryRepository;
    private final RestService restService;

    public ResponseEntity addCargo(CargoController.CargoRecord cargo){

        //todo => check if identity no valid throw exception othervise
        //todo => send email and firebase messages to the rabbitmq server

        //if user not exists exception othervise get user info and use it later
        RestService.IdentityRecord identity = restService.getUserInfoFromIdentityService(
                cargo.senderIdentityNo()
        );

        System.out.println("getUserInfo Sonrasi alinan identity objesi");
        System.out.println(identity);

        String newPnrno = Cargo.generateRandomString(6);
        Cargo newCargo = Cargo.builder()
                .receiverSurname(cargo.receiverSurname())
                .receiverName(cargo.receiverName())
                .phoneNumber(cargo.phoneNumber())
                .deliveryDate(Date.valueOf(LocalDate.now()))
                .branch(branchRepository.findBranchByBranchId(cargo.branchId()).orElseThrow(
                        () -> new NoSuchBranchException("no such branch!!!")
                ))
                .receiverAddress(cargo.receiverAddress())
                .senderAddress(cargo.senderAddress())
                .senderIdentityNo(cargo.senderIdentityNo())
                .pnrNo(newPnrno)
                .build();

        cargoRepository.save(
            newCargo
        );


        branchToBranchRepository.save(
                BranchToBranch.builder()
                        .recordDate(Date.valueOf(LocalDate.now()))
                        .cargo(newCargo)
                        .personel(
                                personelRepository.findPersonelByPersonelId(cargo.personelId()).orElseThrow(
                                        () -> new NoSuchPersonelException("no such personel")
                                )
                        )
                        .toBranch(
                                branchRepository.findBranchByBranchId(cargo.branchId()).orElseThrow(
                                        () -> new NoSuchBranchException("NO SUCH BRANCH")
                                )
                        )
                        .status(STATUS.AT_BRANCH)
                        .build()
        );


        RestService.MailingRecord newRecord = new RestService.MailingRecord(
                "routingMailing",
                identity.email(),
                identity.name(),
                newCargo.getPnrNo(),
                3
        );
        restService.sendRequestToMailingService(newRecord);

        RestService.FirebaseRecord firebaseRecord = new RestService.FirebaseRecord(
                "routingNotification",
                "New Cargo",
                "Your new cargo has been delivered to the branch",
                Map.of("pnrNo",newCargo.getPnrNo(),
                        "brancAddress",newCargo.getBranch().getBranchAddress())
        );
        restService.sendRequestToFirebaseService(firebaseRecord);


        System.out.println("mqapplication sonrasi");

        return ResponseEntity.ok("succesfuly added cargo record");
    }

    public ResponseEntity getCargoByPnrNo(String pnrno){

        Cargo cargo = cargoRepository.findCargoByPnrNo(pnrno).orElseThrow(
                () -> new NoSuchCargoException("ther is no cargo with that pnrno")
        );

        return ResponseEntity.ok(cargo);

    }

    public ResponseEntity getAllCargo(String userId){

        if(!userInfoRepository.existsById(userId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no such user with that identity no");

        List<Cargo> list = cargoRepository.findAllBySenderIdentityNo(userId);
        return ResponseEntity.ok(list);
    }


    public record CargoLocation(
            Branch recentBranch,
            Delivery delivery
    ){}
    public ResponseEntity getCargoLocation(String pnrNo){
        Delivery delivery = null;
        if(!cargoRepository.existsById(pnrNo))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no such cargo with that pnrNo");

        //it doesnt matter if it is null
        //we will send it in all cases
        if(deliveryRepository.findTopByCargo_PnrNoOrderByDeliveryDateDesc(pnrNo).isPresent())
            delivery = deliveryRepository.findTopByCargo_PnrNoOrderByDeliveryDateDesc(pnrNo).get();


        //if cargo with pnrNo exists
        //it is certain that at least one record saved to branchtobranch table
        Branch branch = cargoRepository.findCargoLocation(pnrNo);

        CargoLocation response = new CargoLocation(branch, delivery);
        return ResponseEntity.ok(response);
    }

    public record CargoRoute(
        List<Branch> route,
        Delivery delivery,
        Cargo cargo
    ){}
    public ResponseEntity getCargoRoute(String pnrNo){
        Delivery delivery = null;
        Cargo cargo = cargoRepository.findCargoByPnrNo(pnrNo).orElseThrow(
                () -> new NoSuchCargoException("no such cargo")
        );
        List<Branch> route = cargoRepository.findCargoRoute(pnrNo);
        if(deliveryRepository.findTopByCargo_PnrNoOrderByDeliveryDateDesc(pnrNo).isPresent())
            delivery = deliveryRepository.findTopByCargo_PnrNoOrderByDeliveryDateDesc(pnrNo).get();

        CargoRoute response = new CargoRoute(route,delivery,cargo);
        return ResponseEntity.ok(response);
    }


}
