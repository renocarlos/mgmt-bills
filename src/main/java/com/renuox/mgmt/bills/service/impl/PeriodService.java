package com.renuox.mgmt.bills.service.impl;

import com.renuox.mgmt.bills.enums.PeriodType;
import com.renuox.mgmt.bills.exception.ResourceNotFoundException;
import com.renuox.mgmt.bills.exception.ResourceNotSavedException;
import com.renuox.mgmt.bills.model.Bill;
import com.renuox.mgmt.bills.model.Period;
import com.renuox.mgmt.bills.repository.BillRepository;
import com.renuox.mgmt.bills.repository.PeriodRepository;
import com.renuox.mgmt.bills.util.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class PeriodService {

    @Autowired
    PeriodRepository periodRepository;


    @Autowired
    CatalogService catalogService;

    @Autowired
    BillRepository billRepository;

    public List<Period> findAll() {
        return periodRepository.findAll();
    }

    public void saveByYear(int year) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int subtractionYears = year - currentYear;
        if (subtractionYears > -1 && subtractionYears < 6) {

            int firstPeriodDay = Integer.parseInt(catalogService.findByName("first-period-day").getCode());
            int secondPeriodDay = Integer.parseInt(catalogService.findByName("second-period-day").getCode());

            if (currentDate.getYear() < year) {
                currentDate = LocalDate.of(year, 1, firstPeriodDay);
            }
            int i = 0;
            while (true) {
                LocalDate nextDate = currentDate.plusMonths(i);
                if (nextDate.getYear() > year) break;

                int month = nextDate.getMonthValue();

                if (this.findByTypeAndYearAndMonthNumberAndDay(PeriodType.ACTIVE, year, month, firstPeriodDay) == null) {
                    periodRepository.save(new Period(PeriodType.ACTIVE, nextDate.getMonth().toString(), year, month, firstPeriodDay));
                }

                if (this.findByTypeAndYearAndMonthNumberAndDay(PeriodType.PASSIVE, year, month, firstPeriodDay) == null) {
                    this.saveBudgetByPeriod(
                            periodRepository.save(
                                    new Period(PeriodType.PASSIVE, nextDate.getMonth().toString(), year, month, firstPeriodDay)));
                }

                if (this.findByTypeAndYearAndMonthNumberAndDay(PeriodType.ACTIVE, nextDate.getYear(), nextDate.getMonthValue(), secondPeriodDay) == null) {
                    periodRepository.save(new Period(PeriodType.ACTIVE, nextDate.getMonth().toString(), nextDate.getYear(), nextDate.getMonthValue(), secondPeriodDay));
                }

                if (this.findByTypeAndYearAndMonthNumberAndDay(PeriodType.PASSIVE, nextDate.getYear(), nextDate.getMonthValue(), secondPeriodDay) == null) {
                    this.saveBudgetByPeriod(
                            periodRepository.save(
                                    new Period(PeriodType.PASSIVE, nextDate.getMonth().toString(), nextDate.getYear(), nextDate.getMonthValue(), secondPeriodDay)));
                }
                i++;
            }
        } else {
            throw new ResourceNotSavedException("Values allowed between "
                    + currentYear + " and " + (currentYear + 5));
        }
    }

    public Period findPeriod(Period period) {
        if (period.getId() != null) {
            return this.findPeriodById(period.getId());
        } else {
            Period p = this.findByTypeAndYearAndMonthNumberAndDay(
                    period.getType(), period.getYear(), period.getMonthNumber(), period.getDay());
            if (p == null)
                throw new ResourceNotFoundException(
                        "Period not found with type: " + period.getType() +
                        " and year: " + period.getYear() +
                        " and MonthNumber: " + period.getMonthNumber() +
                        " and Day: " + period.getDay());
            return p;
        }
    }

    public void updateTotalAmount(@NotNull Period period, List<Bill> bills) {
        period = this.findPeriod(period);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Bill bill : bills) {
            totalAmount = totalAmount.add(bill.getAmount());
        }
        period.setTotalAmount(totalAmount);
        periodRepository.save(period);
    }

    public void saveBudgetByPeriod(@NotNull Period period) {
        billRepository.save(
                new Bill("BUDGET", "RENO", getAmountBudget(period), "defined", period));
    }

    private BigDecimal getAmountBudget(Period period) {

        BigDecimal dailyAmount = new BigDecimal(catalogService.findByName("daily-amount").getCode());
        BigDecimal weekendAmount = new BigDecimal(catalogService.findByName("weekend-amount").getCode());

        LocalDate startDate = period.getStartDate();
        LocalDate endDate = period.getEndDate();
        LocalDate currentDate = LocalDate.now();

        long weekends;
        long days;
        if (currentDate.isAfter(startDate)) {
            weekends = DateUtils.countWeekends(currentDate, endDate);
            days = DateUtils.getDifferenceDays(currentDate, endDate) - (weekends * 2);
            if (LocalTime.now().getHour() > 14) {
                days -= 1;
            }
        } else {
            weekends = DateUtils.countWeekends(startDate, endDate);
            days = DateUtils.getDifferenceDays(startDate, endDate) - (weekends * 2);
        }
        return weekendAmount.multiply(BigDecimal.valueOf(weekends))
                .add(dailyAmount.multiply(BigDecimal.valueOf(days)));

    }

    public void updateBudgetByPeriod(@NotNull Period period) {
        Bill bill = billRepository.findByConceptAndPeriodId("BUDGET", period.getId());
        if (bill != null) {
            bill.setAmount(this.getAmountBudget(period));
            billRepository.save(bill);
        }
    }

    public Period findPeriodById(Long id) {
        return periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Period not found with id: " + id));
    }

    public Period findByTypeAndYearAndMonthNumberAndDay(PeriodType type, int year, int monthNumber, int day) {
        return periodRepository.findByTypeAndYearAndMonthNumberAndDay(type, year, monthNumber, day);
    }

    public List<Period> findByYear(int year) {
        return (List<Period>) periodRepository.findByYear(year)
                .orElseThrow(() -> new ResourceNotFoundException("Period not found with year: " + year));
    }

    public Period update(Long id, @NotNull Period periodRequest) {
        periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Period not found with id: " + id));
        periodRequest.setId(id);
        return periodRepository.save(periodRequest);
    }

    public void delete(Long id) {
        periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Period not found with id: " + id));
        periodRepository.deleteById(id);
    }

    public Period findById(Long id) {
        return periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
    }
}