package com.renuox.mgmt.bills.service.impl;

import com.renuox.mgmt.bills.exception.ResourceNotFoundException;
import com.renuox.mgmt.bills.model.Card;
import com.renuox.mgmt.bills.model.CardBill;
import com.renuox.mgmt.bills.repository.CardBillRepository;
import com.renuox.mgmt.bills.repository.CardRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CardBillService {

    @Autowired
    CardBillRepository cardBillRepository;

    @Autowired
    CardRepository cardRepository;

    public List<CardBill> findAll() {
        return cardBillRepository.findAll();
    }

    public CardBill findById(Long id) {
        return cardBillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card bill not found with id: " + id));
    }

    public CardBill save(@NotNull CardBill cardBill) {
        cardBill.setMonthsLeft(cardBill.getTotalPayments() - cardBill.getCurrentPayment());

        if (cardBill.getCurrentPayment() <= cardBill.getTotalPayments()) {
            cardBill.setMonthlyAmount(cardBill.getTotalAmount()
                    .divide(BigDecimal.valueOf(cardBill.getTotalPayments()), 2, RoundingMode.HALF_UP));
        } else {
            cardBill.setMonthlyAmount(BigDecimal.ZERO);
        }

        cardBill.setAmountOwed(cardBill.getMonthlyAmount().multiply(BigDecimal.valueOf(cardBill.getMonthsLeft() + 1)));

        CardBill newCardBill = cardBillRepository.save(cardBill);
        this.updateTotalDebt(newCardBill.getCard());
        return newCardBill;
    }

    public CardBill update(Long id, @NotNull CardBill cardBill) {
        cardBillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card bill not found with id: " + id));

        cardBill.setId(id);
        CardBill newCardBill = cardBillRepository.save(cardBill);
        this.updateTotalDebt(newCardBill.getCard());
        return newCardBill;

    }

    public void delete(Long id) {
        CardBill cardBill = cardBillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card bill not found with id: " + id));

        cardBillRepository.deleteById(id);
        this.updateTotalDebt(cardBill.getCard());
    }

    private void updateTotalDebt(@NotNull Card card) {
        Long cardId = card.getId();

        if (cardId != null) {
            card = this.findCardById(cardId);
        } else {
            card = this.findCardByNameAndBankName(card.getName(), card.getBankName());
        }

        List<CardBill> cardBills = (List<CardBill>) cardBillRepository.findByCardId(cardId);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CardBill cardBill : cardBills) {
            totalAmount = totalAmount.add(cardBill.getAmountOwed());
        }
        card.setTotalDebt(totalAmount);
        cardRepository.save(card);
    }

    public Card findCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
    }

    public Card findCardByNameAndBankName(String name, String bankName) {
        return cardRepository.findByNameAndBankName(name, bankName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Card %s from bank %s not found.", name, bankName)));
    }
}