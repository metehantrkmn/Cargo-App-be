package com.mete.delivery;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends CrudRepository<Delivery,Integer> {

    Optional<Delivery> findDeliveryByDeliveryId(Integer deliveryId);
    List<Delivery> findAllByCargo_Branch_BranchId(Integer branchId);

    List<Delivery> findAllByCargo_Branch_BranchIdAndStatus(Integer branchId, STATUS status);

    Optional<Delivery> findDeliveryByStatusAndCargo_PnrNo(STATUS status, String pnrNo);

    boolean existsByStatusAndCargo_PnrNo(STATUS status, String pnrNo);

    Optional<Delivery> findDeliveryByCargo_pnrNo(String pnrNo);

    Optional<Delivery> findTopByCargo_PnrNoOrderByDeliveryDateDesc(String pnrNo);

}
