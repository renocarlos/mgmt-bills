package com.renuox.mgmt.bills.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "cardBill")
public class CardBill implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String store;

    @Column(nullable = false)
    private String concept;

    @Column(nullable = false)
    private int currentPayment;

    @Column(nullable = false)
    private int totalPayments;

    @Column(nullable = false)
    private int monthsLeft;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private BigDecimal monthlyAmount;

    @Column(nullable = false)
    private BigDecimal amountOwed;

    @ManyToOne
    @JoinColumn(name = "CARD_ID")
    private Card card;
}
