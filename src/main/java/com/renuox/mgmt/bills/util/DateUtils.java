package com.renuox.mgmt.bills.util;

import com.renuox.mgmt.bills.model.Period;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {

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
        String[] periodNameSplit = (period.getName().name().split("_"));

        int startDay;
        int endDay;

        Month month = Month.valueOf(periodNameSplit[0]);
        String monthNumber = String.format("%02d", month.getValue());

        String yearMonth = period.getYear() + "-" + monthNumber;
        String stringStartDate;

        LocalDate endDate;

        if (periodNameSplit[1].equals("1")) {
            endDay = 21;
            String stringEndDate = yearMonth + "-" + endDay;
            endDate = LocalDate.parse(stringEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            startDay = 7;
        } else {
            endDay = 7;
            String stringEndDate = yearMonth + "-01";
            endDate = LocalDate.parse(stringEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .plusMonths(1).withDayOfMonth(endDay);
            startDay = 21;
        }

        stringStartDate = yearMonth + "-01";
        LocalDate startDate = LocalDate.parse(stringStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .withDayOfMonth(startDay);


        period.setStartDate(startDate);
        period.setEndDate(endDate);

    }
}