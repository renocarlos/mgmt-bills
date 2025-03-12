package com.renuox.mgmt.bills.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.renuox.mgmt.bills.enums.PeriodName;
import com.renuox.mgmt.bills.enums.PeriodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "period")
public class Period {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "period", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Bill> bills;

    @Column(nullable = false)
    private PeriodType type;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PeriodName name;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private BigDecimal totalAmount = new BigDecimal(0);

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    public Period(PeriodType type, PeriodName name, int year) {
        this.type = type;
        this.name = name;
        this.year = year;
    }
}
