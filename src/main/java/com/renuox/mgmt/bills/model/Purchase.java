package com.renuox.mgmt.bills.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "purchases")
@Data
public class Purchase {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String concept;

    @Column(nullable = false)
    private int frequency;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private boolean daily;

    @Transient
    private BigDecimal biweekly;

    @Transient
    private BigDecimal monthly;

}
