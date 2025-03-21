package com.renuox.mgmt.bills.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.renuox.mgmt.bills.enums.PeriodType;
import com.renuox.mgmt.bills.util.DateUtils;
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
@Table(name = "periods")
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
    private int year;

    @Column(nullable = false)
    private int monthNumber;

    @Column(nullable = false)
    private String monthName;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private BigDecimal totalAmount = new BigDecimal(0);

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    public Period(PeriodType type, String monthName, int year, int monthNumber, int day) {
        this.type = type;
        this.monthName = monthName;
        this.year = year;
        this.monthNumber = monthNumber;
        this.day = day;
        DateUtils.setDatesToPeriod(this);
    }
}
