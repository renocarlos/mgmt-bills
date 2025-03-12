package com.renuox.mgmt.bills.repository;

import com.renuox.mgmt.bills.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByNameAndBankName(String name, String banString);
}
