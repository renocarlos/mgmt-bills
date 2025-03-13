package com.renuox.mgmt.bills.service.impl;

import com.renuox.mgmt.bills.enums.PeriodName;
import com.renuox.mgmt.bills.enums.PeriodType;
import com.renuox.mgmt.bills.exception.ResourceNotFoundException;
import com.renuox.mgmt.bills.exception.ResourceNotSavedException;
import com.renuox.mgmt.bills.model.BothPeriods;
import com.renuox.mgmt.bills.model.Period;
import com.renuox.mgmt.bills.repository.PeriodRepository;
import com.renuox.mgmt.bills.util.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public BothPeriods findById(Long id) {
        Period activePeriod = periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Period not found with id: " + id));
        if (activePeriod.getType().equals(PeriodType.ACTIVE)) {
            Period passivePeriod = this.findByTypeAndNameAndYear(PeriodType.PASSIVE, activePeriod.getName(), activePeriod.getYear());
            return new BothPeriods(activePeriod, passivePeriod);
        } else {
            Period passivePeriod = this.findByTypeAndNameAndYear(PeriodType.ACTIVE, activePeriod.getName(), activePeriod.getYear());
            return new BothPeriods(passivePeriod, activePeriod);
        }
    }

    public void saveByYear(int year) {
        int currentYear = Year.now().getValue();
        int subtractionYears = year - currentYear;

        if (subtractionYears > -1 && subtractionYears < 6) {
            for (PeriodType type : PeriodType.values()) {
                for (PeriodName name : PeriodName.values()) {
                    if (this.findByTypeAndNameAndYear(type, name, year) == null) {
                        Period period = new Period(type, name, year);
                        DateUtils.setDatesToPeriod(period);
                        periodRepository.save(period);
                        if (type == PeriodType.PASSIVE) {
                            billService.saveBudgetByPeriod(period);
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

    public BothPeriods getCurrentPeriod() {
        LocalDate currentDate = LocalDate.now();
        String month = this.getMothCurrentPeriod(currentDate);
        Period activePeriod = this.findByTypeAndNameAndYear(PeriodType.ACTIVE, PeriodName.valueOf(month), currentDate.getYear());
        if (activePeriod == null) {
            this.saveByYear(currentDate.getYear());
        }
        return new BothPeriods(activePeriod,
                this.findByTypeAndNameAndYear(PeriodType.PASSIVE, PeriodName.valueOf(month), currentDate.getYear()));

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

    public List<BothPeriods> findNextPeriods(int nextPeriods) {
        this.saveByYear(LocalDate.now().getYear());
        nextPeriods *= 2;
        List<BothPeriods> bothPeriodsList = new ArrayList<>(nextPeriods);

        Period currentActivePeriod = this.getCurrentPeriod().getActivePeriod();
        int indexCurrentPeriod = currentActivePeriod.getName().ordinal();

        int length = PeriodName.values().length;
        boolean nextYear = false;

        for (int i = 0; i < nextPeriods; i++) {
            int index = (indexCurrentPeriod + i);
            PeriodName nextPeriod = PeriodName.values()[index];
            bothPeriodsList.add(new BothPeriods(this.findByTypeAndNameAndYear(PeriodType.ACTIVE, nextPeriod, currentActivePeriod.getYear()),
                    this.findByTypeAndNameAndYear(PeriodType.PASSIVE, nextPeriod, currentActivePeriod.getYear())));
            if ((index + 1) == length) {
                nextYear = true;
                break;
            }
        }

        if (nextYear) {
            this.saveByYear(currentActivePeriod.getYear() + 1);
            for (int i = 0; i < indexCurrentPeriod; i++) {
                PeriodName nextPeriod = PeriodName.values()[i];
                bothPeriodsList.add(new BothPeriods(this.findByTypeAndNameAndYear(PeriodType.ACTIVE, nextPeriod, currentActivePeriod.getYear() + 1),
                        this.findByTypeAndNameAndYear(PeriodType.PASSIVE, nextPeriod, currentActivePeriod.getYear() + 1)));
            }
        }
        return bothPeriodsList;
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