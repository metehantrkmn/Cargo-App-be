package com.mete.returnRequest;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ReturnRequestRepository extends CrudRepository<ReturnRequest, Integer> {

    Optional<ReturnRequest> findReturnRequestByReturnId(Integer returnId);

    boolean existsByCargo_PnrNo(String pnrNo);

    List<ReturnRequest> findAll();

}
