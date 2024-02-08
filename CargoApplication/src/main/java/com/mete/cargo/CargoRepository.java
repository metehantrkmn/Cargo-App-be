package com.mete.cargo;

import com.mete.branch.Branch;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CargoRepository extends CrudRepository<Cargo,String> {

    Optional<Cargo> findCargoByPnrNo(String pnrNo);
    List<Cargo> findAllBySenderIdentityNo(String senderIdentityNo);

    @Query(value = """
            SELECT c
            FROM Cargo c, BranchToBranch b
            WHERE c.pnrNo IN (
                              SELECT c_in.pnrNo
                              FROM BranchToBranch b_in, Cargo c_in, (SELECT branchto.cargo.pnrNo AS in_pnrNo, MAX(branchto.recordDate) AS l_date
                                                                        FROM BranchToBranch branchto
                                                                        GROUP BY branchto.cargo.pnrNo
                                                                        ) AS latestRecords
                              where c_in.pnrNo=b_in.cargo.pnrNo AND c_in.pnrNo=latestRecords.in_pnrNo 
                                    AND b_in.recordDate=latestRecords.l_date AND b_in.status='AT_BRANCH'
                                    AND b_in.toBranch.branchId = :branchId
                              )
                             AND
                  c.pnrNo NOT IN (
                                   SELECT d.cargo.pnrNo
                                   FROM Delivery d
                                   WHERE d.status = 'ON_DELIVERY' OR d.status = 'DELIVERED'
                                  )
            AND b.cargo.pnrNo=c.pnrNo
            """)
    List<Cargo> findCargosAtBranch(@Param("branchId") Integer branchId);

    @Query(value = """
            SELECT c
            FROM Cargo c, BranchToBranch b
            WHERE c.pnrNo IN (
                              SELECT c_in.pnrNo
                              FROM BranchToBranch b_in, Cargo c_in, (SELECT branchto.cargo.pnrNo AS in_pnrNo, MAX(branchto.recordDate) AS l_date
                                                                        FROM BranchToBranch branchto
                                                                        GROUP BY branchto.cargo.pnrNo
                                                                        ) AS latestRecords
                              where c_in.pnrNo=b_in.cargo.pnrNo AND b_in.cargo.pnrNo=latestRecords.in_pnrNo 
                                    AND b_in.recordDate=latestRecords.l_date AND b_in.status='AT_BRANCH'
                              )
                              AND
                              c.pnrNo=b.cargo.pnrNo
            """)
    List<Cargo> deneme();

    @Query(value = """
            SELECT btob.toBranch
            FROM Cargo c, BranchToBranch btob , (SELECT branchto.cargo.pnrNo AS in_pnrNo, MAX(branchto.recordDate) AS l_date
                                                                        FROM BranchToBranch branchto
                                                                        GROUP BY branchto.cargo.pnrNo
                                                                        ) AS latestRecords
            WHERE c.pnrNo=btob.cargo.pnrNo AND c.pnrNo=latestRecords.in_pnrNo AND latestRecords.l_date=btob.recordDate
            AND c.pnrNo= :pnrNo
            """)
    Branch findCargoLocation(@Param("pnrNo") String pnrNo);

    @Query(value = """
            SELECT btob.toBranch
            FROM BranchToBranch btob, Cargo c
            WHERE c.pnrNo=btob.cargo.pnrNo AND c.pnrNo= :pnrNo
            ORDER BY btob.recordDate DESC
            """)
    List<Branch> findCargoRoute(@Param("pnrNo") String pnrNo);

}
