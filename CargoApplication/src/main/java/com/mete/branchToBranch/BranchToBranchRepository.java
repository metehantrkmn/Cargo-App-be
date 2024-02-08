package com.mete.branchToBranch;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchToBranchRepository extends CrudRepository<BranchToBranch,Integer> {
    @Override
    Optional<BranchToBranch> findById(Integer integer);

    List<BranchToBranch> findAll();
    List<BranchToBranch> findAllByCargo_PnrNo(String pnrNo);

}
