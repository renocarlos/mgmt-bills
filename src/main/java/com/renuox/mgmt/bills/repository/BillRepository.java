package com.renuox.mgmt.bills.repository;

import com.renuox.mgmt.bills.enums.PeriodName;
import com.renuox.mgmt.bills.enums.PeriodType;
import com.renuox.mgmt.bills.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Iterable<Bill> findByPeriodId(Long periodId);

    Iterable<Bill> findByPeriodTypeAndPeriodNameAndPeriodYear(PeriodType periodType, PeriodName name, int year);
}
