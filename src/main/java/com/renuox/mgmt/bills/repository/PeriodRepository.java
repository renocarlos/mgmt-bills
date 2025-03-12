package com.renuox.mgmt.bills.repository;

import com.renuox.mgmt.bills.enums.PeriodName;
import com.renuox.mgmt.bills.enums.PeriodType;
import com.renuox.mgmt.bills.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {
    Period findByTypeAndNameAndYear(PeriodType type, PeriodName name, int year);

    Optional<Iterable<Period>> findByYear(int year);
}
