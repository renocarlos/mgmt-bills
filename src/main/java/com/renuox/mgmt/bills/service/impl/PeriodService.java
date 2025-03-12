package com.renuox.mgmt.bills.service.impl;

import com.renuox.mgmt.bills.enums.PeriodName;
import com.renuox.mgmt.bills.enums.PeriodType;
import com.renuox.mgmt.bills.exception.ResourceNotFoundException;
import com.renuox.mgmt.bills.exception.ResourceNotSavedException;
import com.renuox.mgmt.bills.model.Period;
import com.renuox.mgmt.bills.repository.PeriodRepository;
import com.renuox.mgmt.bills.util.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class PeriodService {

    @Autowired
    PeriodRepository periodRepository;

    @Autowired
    BillService billService;

    public List<Period> findAll() {
        return periodRepository.findAll();
    }

    public Period findById(Long id) {
        return periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Period not found with id: " + id));
    }

    public void saveByYear(int year) {
        int currentYear = Year.now().getValue();
        int subtractionYears = year - currentYear;
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        Month month = Month.valueOf(getMothCurrentPeriod(currentDate).split("_")[0]);
        System.out.println("currentMonth " + currentMonth);
        System.out.println("Month " + month);

        if (month.compareTo(currentMonth) < 0) {
            currentDate = currentDate.minusMonths(1).withDayOfMonth(7);
        }

        if (subtractionYears > -1 && subtractionYears < 6) {
            for (PeriodType type : PeriodType.values()) {
                for (PeriodName name : PeriodName.values()) {
                    if (this.findByTypeAndNameAndYear(type, name, year) == null) {
                        Period period = new Period(type, name, year);
                        DateUtils.setDatesToPeriod(period);
                        if (currentDate.isBefore(period.getStartDate())) {
                            periodRepository.save(period);
                            if (type == PeriodType.PASSIVE) {
                                billService.saveBudgetByPeriod(period);
                            }
                        }
                    }
                }
            }
        } else {
            throw new ResourceNotSavedException("Values allowed between "
                    + currentYear + " and " + (currentYear + 5));
        }
    }

    public Period findByTypeAndNameAndYear(PeriodType type, PeriodName name, int year) {
        return periodRepository.findByTypeAndNameAndYear(type, name, year);
    }

    public List<Period> getCurrentPeriod() {
        List<Period> periods = new ArrayList<>(2);
        LocalDate currentDate = LocalDate.now();

        String month = this.getMothCurrentPeriod(currentDate);

        periods.add(this.findByTypeAndNameAndYear(PeriodType.ACTIVE, PeriodName.valueOf(month), currentDate.getYear()));
        periods.add(this.findByTypeAndNameAndYear(PeriodType.PASSIVE, PeriodName.valueOf(month), currentDate.getYear()));

        return periods;
    }

    private String getMothCurrentPeriod(LocalDate currentDate) {

        LocalDate firstDate = currentDate.withDayOfMonth(7);
        LocalDate endDate = currentDate.withDayOfMonth(21);

        String month = currentDate.getMonth().toString();
        if (currentDate.isBefore(firstDate)) {
            month = currentDate.minusMonths(1).getMonth().toString();
            month += "_2";
        } else if (currentDate.isBefore(endDate)) {
            month += "_1";
        } else if (currentDate.isAfter(endDate)) {
            month += "_2";
        }
        return month;
    }

    public List<Period> findByYear(int year) {
        return (List<Period>) periodRepository.findByYear(year)
                .orElseThrow(() -> new ResourceNotFoundException("Period not found with year: " + year));
    }

    public List<Period> findNextPeriods(int nextPeriods) {
        List<Period> periods = new ArrayList<>(nextPeriods * 2);
        Period currentPeriod = this.getCurrentPeriod().get(0);

        LocalDate currentDate = currentPeriod.getStartDate();

        int indexCurrentPeriod = currentPeriod.getName().ordinal();

        for (int i = 0; i < nextPeriods; i++) {
            Month month = currentDate.getMonth().plus(i);
            PeriodName nextPeriod = PeriodName.values()[(indexCurrentPeriod + i)];
            periods.add(this.findByTypeAndNameAndYear(PeriodType.ACTIVE, nextPeriod, currentPeriod.getYear()));
            periods.add(this.findByTypeAndNameAndYear(PeriodType.PASSIVE, nextPeriod, currentPeriod.getYear()));
        }

//
//        PeriodName start = currentPeriod.getName();
//        boolean startFound = false;
//
//        for (PeriodName period : PeriodName.values()) {
//            if (period == start) {
//                startFound = true;
//            }
//            if (startFound) {
//                periods.add(this.findByTypeAndNameAndYear(PeriodType.ACTIVE, period, currentPeriod.getYear()));
//                periods.add(this.findByTypeAndNameAndYear(PeriodType.PASSIVE, period, currentPeriod.getYear()));
//            }
//        }
//
//        for (PeriodName period : PeriodName.values()) {
//            if (period == start) {
//                break;
//            }
//            periods.add(this.findByTypeAndNameAndYear(PeriodType.ACTIVE, period, currentPeriod.getYear() + 1));
//            periods.add(this.findByTypeAndNameAndYear(PeriodType.PASSIVE, period, currentPeriod.getYear() + 1));
//        }
        return periods;
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
}