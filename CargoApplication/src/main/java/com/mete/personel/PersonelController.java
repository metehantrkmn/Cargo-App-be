package com.mete.personel;

import com.mete.branch.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PersonelController {

    private final PersonelRepository personelRepository;
    private final PersonelService personelService;

    @GetMapping("/api/personel/deneme")
    public ResponseEntity deneme(){
        personelRepository.save(
                Personel.builder()
                        .personelId("denemeId")
                        .build()
        );
        Optional<Personel> mockUser = personelRepository.findPersonelByPersonelId("denemeId");

        if(mockUser.isPresent())
            System.out.println(mockUser.get());

        System.out.println(mockUser.get());
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getDetails());
    }

    //add record fro gettin personel properly!!!!!
    public record PersonelRecord(
            String personelId
    ){}
    @PostMapping("/api/personel/add-personel")
    public ResponseEntity addPersonel(@RequestBody PersonelRecord personelRecord){
        return personelService.addPersonel(personelRecord.personelId);
    }

    @DeleteMapping("/api/personel/delete-personel")
    public ResponseEntity deletePersonel(@RequestParam(name = "personelId") String personelId){
        return personelService.deletePersonel(personelId);
    }

}
