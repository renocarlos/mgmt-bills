package com.renuox.mgmt.bills.service.impl;

import com.renuox.mgmt.bills.enums.PeriodType;
import com.renuox.mgmt.bills.exception.ResourceNotFoundException;
import com.renuox.mgmt.bills.model.BothPeriods;
import com.renuox.mgmt.bills.model.Period;
import com.renuox.mgmt.bills.repository.PeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BothPeriodsService {

    @Autowired
    PeriodRepository periodRepository;

    @Autowired
    CatalogService catalogService;

    @Autowired
    PeriodService periodService;

    public BothPeriods getBothPeriodsByPeriodId(Long id) {

        Period requestedPeriod = periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Period not found with id: " + id));

        int year = requestedPeriod.getYear();
        int month = requestedPeriod.getMonthNumber();
        int day = requestedPeriod.getDay();


        if (requestedPeriod.getType().equals(PeriodType.ACTIVE)) {

            //TODO Delete Comments
//            List<Bill> activeBills = billRepository.findByTypeAndPeriodDayAndPeriodType("undefined", day, PeriodType.ACTIVE);
//
//            for (Bill b : activeBills) {
//                requestedPeriod.getBills().add(b);
//            }

            Period passivePeriod = periodService.findByTypeAndYearAndMonthNumberAndDay(PeriodType.PASSIVE, year,
                    month, day);

                    //TODO Delete Comments

            //List<Bill> passiveBills = billRepository.findByTypeAndPeriodDayAndPeriodType("undefined", day, PeriodType.PASSIVE);

//            for (Bill b : passiveBills) {
//                passivePeriod.getBills().add(b);
//            }
            periodService.updateBudgetByPeriod(passivePeriod);
            periodService.updateTotalAmount(requestedPeriod, requestedPeriod.getBills());
            periodService.updateTotalAmount(passivePeriod, passivePeriod.getBills());

            return new BothPeriods(requestedPeriod, passivePeriod);
        } else {

            Period activePeriod = periodService.findByTypeAndYearAndMonthNumberAndDay(PeriodType.ACTIVE, year,
                    month, day);


                    //TODO Delete Comments

//            List<Bill> activeBills = billRepository.findByTypeAndPeriodDayAndPeriodType("undefined", day, PeriodType.ACTIVE);
//
//            for (Bill b : activeBills) {
//                requestedPeriod.getBills().add(b);
//            }

//            List<Bill> passiveBills = billRepository.findByTypeAndPeriodDayAndPeriodType("undefined", day, PeriodType.PASSIVE);
//
//            for (Bill b : passiveBills) {
//                activePeriod.getBills().add(b);
//            }
            periodService.updateBudgetByPeriod(requestedPeriod);
            periodService.updateTotalAmount(activePeriod, activePeriod.getBills());
            periodService.updateTotalAmount(requestedPeriod, requestedPeriod.getBills());

            return new BothPeriods(activePeriod, requestedPeriod);
        }
    }

    public BothPeriods getCurrentPeriod() {
        LocalDate currentDate = LocalDate.now();
        catalogService.initializeCatalogs();
        int firstPeriodDay = Integer.parseInt(catalogService.findByName("first-period-day").getCode());
        int secondPeriodDay = Integer.parseInt(catalogService.findByName("second-period-day").getCode());

        int day = secondPeriodDay;

        LocalDate firDate = currentDate.withDayOfMonth(firstPeriodDay);
        LocalDate secondDate = currentDate.withDayOfMonth(secondPeriodDay);

        if (currentDate.isBefore(firDate)) {
            currentDate = currentDate.minusMonths(1);
        } else if (currentDate.isBefore(secondDate)) {
            day = firstPeriodDay;
        }

        return new BothPeriods(
                periodService.findByTypeAndYearAndMonthNumberAndDay(
                        PeriodType.ACTIVE, currentDate.getYear(), currentDate.getMonthValue(), day),
                periodService.findByTypeAndYearAndMonthNumberAndDay(
                        PeriodType.PASSIVE, currentDate.getYear(), currentDate.getMonthValue(), day));
    }

    public List<BothPeriods> findNextPeriods(int nextPeriods) {
        LocalDate currentDate = LocalDate.now();
        List<BothPeriods> bothPeriodsList = new ArrayList<>();

        int firstPeriodDay = Integer.parseInt(catalogService.findByName("first-period-day").getCode());
        int secondPeriodDay = Integer.parseInt(catalogService.findByName("second-period-day").getCode());

        for (int i = 0; i < nextPeriods / 2; i++) {

            LocalDate nextDate = currentDate.plusMonths(i);

            int year = nextDate.getYear();
            int month = nextDate.getMonthValue();

            Period firstActivePeriod = periodService.findByTypeAndYearAndMonthNumberAndDay(PeriodType.ACTIVE, year, month, firstPeriodDay);
            if (firstActivePeriod == null) {
                break;
            }
            Period firstPassivePeriod = periodService.findByTypeAndYearAndMonthNumberAndDay(PeriodType.PASSIVE, year, month, firstPeriodDay);
            bothPeriodsList.add(new BothPeriods(firstActivePeriod, firstPassivePeriod));

            Period secondActivePeriod = periodService.findByTypeAndYearAndMonthNumberAndDay(PeriodType.ACTIVE, year, month, secondPeriodDay);
            Period secondPassivePeriod = periodService.findByTypeAndYearAndMonthNumberAndDay(PeriodType.PASSIVE, year, month, secondPeriodDay);
            bothPeriodsList.add(new BothPeriods(secondActivePeriod, secondPassivePeriod));
        }
        return bothPeriodsList;
    }
}