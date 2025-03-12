package com.renuox.mgmt.bills.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String concept;

    @Column(nullable = false)
    private String person;

    @Column(nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal amountPaid = BigDecimal.ZERO;

    @Column(nullable = false)
    private int paymentNumber = 1;

    @Column(nullable = false)
    private int totalPayments = 1;

    @Column(nullable = false)
    private String note = "";

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    @JsonBackReference
    private Period period;

    public Bill(BigDecimal amount, String concept, String person, Period period) {
        this.amount = amount;
        this.concept = concept;
        this.person = person;
        this.period = period;
    }
}
