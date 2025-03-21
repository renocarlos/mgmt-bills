package com.renuox.mgmt.bills.service.impl;

import com.renuox.mgmt.bills.exception.ResourceNotFoundException;
import com.renuox.mgmt.bills.model.Bill;
import com.renuox.mgmt.bills.model.Period;
import com.renuox.mgmt.bills.repository.BillRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BillService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    PeriodService periodService;

    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    public Bill findById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
    }

    public Bill save(Bill bill) {
        this.saveAndReplicateInvoice(bill);
        periodService.updateTotalAmount(bill.getPeriod(), billRepository.findByPeriodId(bill.getPeriod().getId()));
        return bill;
    }

    public Bill update(Long id, @NotNull Bill bill) {

        billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        bill.setId(id);
        Bill billSaved = billRepository.save(bill);
        periodService.updateTotalAmount(bill.getPeriod(), billRepository.findByPeriodId(bill.getPeriod().getId()));
        return billSaved;
    }

    public List<Bill> findByPeriodId(Long periodId) {
        return billRepository.findByPeriodId(periodId);
    }
    public void delete(Long id) {
        billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        billRepository.deleteById(id);
    }

    private void saveAndReplicateInvoice(@NotNull Bill bill) {
        Period period = periodService.findPeriod(bill.getPeriod());
        bill.setPeriod(period);

        LocalDate firstDate = LocalDate.of(period.getYear(), period.getMonthNumber(), period.getDay());

        if(bill.getType().equals("undefined")){
            bill.setPaymentNumber(1);
            bill.setTotalPayments(1);
        }

        for (int i = 0; i < bill.getTotalPayments(); i++) {
            LocalDate nextMonthDate = firstDate.plusMonths(i);
            Bill newBill = new Bill();
            newBill.setConcept(bill.getConcept());
            newBill.setPerson(bill.getPerson());
            newBill.setAmount(bill.getAmount());
            newBill.setPaymentNumber(i + 1);
            newBill.setNote(bill.getNote());
            newBill.setType(bill.getType());
            newBill.setPeriod(periodService.findByTypeAndYearAndMonthNumberAndDay(
                    period.getType(), nextMonthDate.getYear(), nextMonthDate.getMonthValue(), period.getDay()));
            billRepository.save(newBill);
        }
    }
}