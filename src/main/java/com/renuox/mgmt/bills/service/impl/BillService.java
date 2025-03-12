package com.renuox.mgmt.bills.service.impl;

import com.renuox.mgmt.bills.enums.PeriodName;
import com.renuox.mgmt.bills.enums.PeriodType;
import com.renuox.mgmt.bills.exception.ResourceNotFoundException;
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
import java.time.Month;
import java.util.List;

@Service
public class BillService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    PeriodRepository periodRepository;

    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    public Bill findById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
    }

    public Bill save(Bill bill) {
        this.saveAndReplicateInvoice(bill);
        this.updateTotalAmount(bill.getPeriod());
        return bill;
    }

    public Bill saveBudgetByPeriod(Period period) {

        BigDecimal dailyAmount = BigDecimal.valueOf(285.00);
        BigDecimal weekendAmount = BigDecimal.valueOf(1000.00);

        LocalDate startDate = period.getStartDate();
        LocalDate endDate = period.getEndDate();

        long weekends = DateUtils.countWeekends(startDate, endDate);
        LocalDate currentDate = LocalDate.now();
        LocalTime currentHour = LocalTime.now();
        int hour = currentHour.getHour();
        long days = -(weekends * 2);

        if (currentDate.isAfter(startDate)) {
            days += DateUtils.getDifferenceDays(currentDate, endDate);
            if (hour > 14) {
                days -= 1;
            }
        } else {
            days += DateUtils.getDifferenceDays(startDate, endDate);
        }

        BigDecimal amount = weekendAmount.multiply(BigDecimal.valueOf(weekends))
                .add(dailyAmount.multiply(BigDecimal.valueOf(days)));
        return this.save(new Bill(amount, "BUDGET", "SELF", period));
    }

    public Bill update(Long id, @NotNull Bill bill) {

        billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));

        bill.setId(id);
        Bill billSaved = billRepository.save(bill);
        this.updateTotalAmount(bill.getPeriod());
        return billSaved;
    }

    public List<Bill> findByPeriodId(Long periodId) {
        return (List<Bill>) billRepository.findByPeriodId(periodId);
    }

    public List<Bill> findByPeriodTypeAndPeriodNameAndPeriodYear(PeriodType periodType, PeriodName name, int year) {
        return (List<Bill>) billRepository.findByPeriodTypeAndPeriodNameAndPeriodYear(periodType, name, year);
    }

    public void delete(Long id) {
        billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        billRepository.deleteById(id);
    }

    private void saveAndReplicateInvoice(@NotNull Bill bill) {
        Period period = this.findPeriod(bill.getPeriod());
        bill.setPeriod(period);

        String[] periodNameSplit = (period.getName().name()).split("_");

        int year = period.getYear();
        int month = (Month.valueOf(periodNameSplit[0])).getValue();
        int day = Integer.parseInt(periodNameSplit[1]);

        LocalDate firstDate = LocalDate.of(year, month, day);


        for (int i = 0; i < bill.getTotalPayments(); i++) {
            LocalDate nextMonthDate = firstDate.plusMonths(i);
            Month nextMonth = nextMonthDate.getMonth();
            PeriodName name = PeriodName.valueOf(nextMonth + "_" + day);

            Bill newBill = new Bill();

            newBill.setConcept(bill.getConcept());
            newBill.setPerson(bill.getPerson());
            newBill.setAmount(bill.getAmount());
            newBill.setPaymentNumber(i + 1);
            newBill.setNote(bill.getNote());
            newBill.setPeriod(this.findPeriodByTypeAndNameAndYear(period.getType(), name, nextMonthDate.getYear()));
            billRepository.save(newBill);

            if ((nextMonthDate.getMonth().toString()).equals("FEBRUARY") && day == 28) {
                day = 30;
            } else if ((nextMonthDate.getMonth().toString()).equals("JANUARY") && day == 30) {
                day = 28;
            }

        }
    }

    private void updateTotalAmount(@NotNull Period period) {
        period = findPeriod(period);

        List<Bill> bills = (List<Bill>) billRepository.findByPeriodId(period.getId());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Bill bill : bills) {
            totalAmount = totalAmount.add(bill.getAmount());
        }
        period.setTotalAmount(totalAmount);
        periodRepository.save(period);
    }

    private Period findPeriod(Period period) {
        if (period.getId() != null) {
            return this.findPeriodById(period.getId());
        } else {
            Period p = this.findPeriodByTypeAndNameAndYear(period.getType(), period.getName(), period.getYear());
            if (p == null)
                throw new ResourceNotFoundException("Period not found with type: " + period.getType() + " and name: " + period.getName() + " and year: " + period.getYear());
            return p;
        }
    }

    public Period findPeriodById(Long id) {
        return periodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Period not found with id: " + id));
    }

    public Period findPeriodByTypeAndNameAndYear(PeriodType type, PeriodName name, int year) {
        return periodRepository.findByTypeAndNameAndYear(type, name, year);
    }
}