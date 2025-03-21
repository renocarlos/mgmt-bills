package com.renuox.mgmt.bills.controller;

import com.renuox.mgmt.bills.model.CardBill;
import com.renuox.mgmt.bills.service.impl.CardBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${cors.allowedOrigins}")
@RestController
@RequestMapping("/card-bills")
public class CardBillController {

    @Autowired
    CardBillService cardBillService;

    @GetMapping("/list")
    public List<CardBill> listCardBill() {
        return cardBillService.findAll();
    }

    @PostMapping
    public CardBill saveCardBill(@RequestBody CardBill cardBill) {
        return cardBillService.save(cardBill);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardBill> listCardBillById(@PathVariable Long id) {
        return ResponseEntity.ok(cardBillService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardBill> updateCardBillById(@PathVariable Long id, @RequestBody CardBill cardBillRequest) {
        return ResponseEntity.ok(cardBillService.update(id, cardBillRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteCardBillById(@PathVariable Long id) {
        cardBillService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
