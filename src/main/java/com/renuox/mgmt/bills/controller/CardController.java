package com.renuox.mgmt.bills.controller;

import com.renuox.mgmt.bills.model.Card;
import com.renuox.mgmt.bills.service.impl.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${cors.allowedOrigins}")
@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    CardService cardService;

    @GetMapping("/list")
    public List<Card> listCard() {
        return cardService.findAll();
    }

    @PostMapping
    public Card saveCard(@RequestBody Card card) {
        return cardService.save(card);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> listCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCardById(@PathVariable Long id, @RequestBody Card cardRequest) {
        return ResponseEntity.ok(cardService.update(id, cardRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteCardById(@PathVariable Long id) {
        cardService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
