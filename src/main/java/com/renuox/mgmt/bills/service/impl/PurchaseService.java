package com.renuox.mgmt.bills.service.impl;

import com.renuox.mgmt.bills.exception.ResourceNotFoundException;
import com.renuox.mgmt.bills.model.Purchase;
import com.renuox.mgmt.bills.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    PurchaseRepository purchaseRepository;

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }

    public Purchase findById(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + id));
    }

    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public Purchase update(Long id, Purchase purchaseRequest) {
        purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + id));
        purchaseRequest.setId(id);
        return purchaseRepository.save(purchaseRequest);
    }

    public void delete(Long id) {
        purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + id));
        purchaseRepository.deleteById(id);
    }
}
