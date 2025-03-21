package com.renuox.mgmt.bills.repository;

import com.renuox.mgmt.bills.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPeriodId(Long periodId);

    Bill findByConceptAndPeriodId(String concept, Long periodId);
}
