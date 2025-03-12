package com.renuox.mgmt.bills.service.impl;

import com.renuox.mgmt.bills.exception.ResourceNotFoundException;
import com.renuox.mgmt.bills.model.Card;
import com.renuox.mgmt.bills.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    @Autowired
    CardRepository cardRepository;

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public Card findById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public Card update(Long id, @org.jetbrains.annotations.NotNull Card cardRequest) {
        cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
        cardRequest.setId(id);
        return cardRepository.save(cardRequest);
    }

    public void delete(Long id) {
        cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
        cardRepository.deleteById(id);
    }

}
