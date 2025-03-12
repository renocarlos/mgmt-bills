package com.renuox.mgmt.bills.repository;

import com.renuox.mgmt.bills.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
