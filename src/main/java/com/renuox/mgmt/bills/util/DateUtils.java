package com.renuox.mgmt.bills.util;

import com.renuox.mgmt.bills.model.Period;
import com.renuox.mgmt.bills.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


@Component
public class DateUtils {

    private static CatalogRepository catalogRepository;

    @Autowired
    public void setCatalogRepository(CatalogRepository catalogRepository) {
        DateUtils.catalogRepository = catalogRepository;
    }


    public static long getDifferenceDays(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static long countWeekends(LocalDate startDate, LocalDate endDate) {
        long count = 0;
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            DayOfWeek day = date.getDayOfWeek();
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                count++;
            }
            date = date.plusDays(1);
        }
        return count / 2;
    }

    public static void setDatesToPeriod(Period period) {

        int startDay;
        int endDay;

        String monthNumber = String.valueOf(period.getMonthNumber());

        monthNumber = monthNumber.length() == 1 ? "0" + monthNumber : monthNumber;

        String yearMonth = period.getYear() + "-" + monthNumber;
        String stringStartDate;

        LocalDate endDate;

        int firstPeriodDay = Integer.parseInt(catalogRepository.findByName("first-period-day").getCode());
        if (period.getDay() == firstPeriodDay) {
            endDay = Integer.parseInt(catalogRepository.findByName("second-period-day").getCode());
            String stringEndDate = yearMonth + "-" + endDay;
            endDate = LocalDate.parse(stringEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            startDay = firstPeriodDay;
        } else {
            endDay = firstPeriodDay;
            String stringEndDate = yearMonth + "-01";
            endDate = LocalDate.parse(stringEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusMonths(1).withDayOfMonth(endDay);
            startDay = Integer.parseInt(catalogRepository.findByName("second-period-day").getCode());
        }

        stringStartDate = yearMonth + "-01";
        LocalDate startDate = LocalDate.parse(stringStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).withDayOfMonth(startDay);

        period.setStartDate(startDate);
        period.setEndDate(endDate);

    }
}