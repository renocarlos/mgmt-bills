package com.renuox.mgmt.bills.controller;

import com.renuox.mgmt.bills.model.Purchase;
import com.renuox.mgmt.bills.service.impl.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping
    public List<Purchase> listPurchase() {
        return purchaseService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Purchase savePurchase(@RequestBody Purchase purchase) {
        return purchaseService.save(purchase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> listPurchaseById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Purchase> updatePurchaseById(@PathVariable Long id, @RequestBody Purchase purchaseRequest) {
        return ResponseEntity.ok(purchaseService.update(id, purchaseRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deletePurchaseById(@PathVariable Long id) {
        purchaseService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}
