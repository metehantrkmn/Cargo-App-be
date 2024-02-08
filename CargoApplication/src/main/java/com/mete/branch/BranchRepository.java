package com.mete.branch;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BranchRepository extends CrudRepository<Branch,Integer> {
    Optional<Branch> findBranchByBranchId(Integer branchId);

}
