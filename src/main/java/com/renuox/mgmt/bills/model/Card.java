package com.renuox.mgmt.bills.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private Set<CardBill> cardBills;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private int cutOffDate;

    @Column(nullable = false)
    private BigDecimal totalDebt = BigDecimal.ZERO;

}
