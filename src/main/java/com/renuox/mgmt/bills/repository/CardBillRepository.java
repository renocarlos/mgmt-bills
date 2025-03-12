package com.renuox.mgmt.bills.repository;

import com.renuox.mgmt.bills.model.CardBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardBillRepository extends JpaRepository<CardBill, Long> {
    Iterable<CardBill> findByCardId(Long cardId);
}
