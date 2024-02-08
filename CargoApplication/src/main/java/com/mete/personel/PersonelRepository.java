package com.mete.personel;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonelRepository extends CrudRepository<Personel,String> {
    Optional<Personel> findPersonelByPersonelId(String personelId);
}
