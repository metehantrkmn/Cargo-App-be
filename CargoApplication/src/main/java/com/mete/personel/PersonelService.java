package com.mete.personel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonelService {

    private final PersonelRepository personelRepository;

    public ResponseEntity addPersonel(String personelId){

        //todo => send email about regstering the system

        personelRepository.save(
                Personel.builder()
                        .personelId(personelId)
                        .build()
        );

        return ResponseEntity.ok("succesfuly added new personel!!");
    }

    public ResponseEntity deletePersonel(String personleId){

        if(!personelRepository.existsById(personleId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("personel not found");

        personelRepository.deleteById(personleId);

        return ResponseEntity.ok("succesfully delted personel");
    }

}
