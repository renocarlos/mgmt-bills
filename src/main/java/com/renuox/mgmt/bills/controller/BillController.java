package com.renuox.mgmt.bills.controller;

import com.renuox.mgmt.bills.model.Bill;
import com.renuox.mgmt.bills.service.impl.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${cors.allowedOrigins}")
@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    BillService billService;

    @GetMapping("/list")
    public List<Bill> listBill() {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return billService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bill saveBill(@RequestBody Bill bill) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return billService.save(bill);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return ResponseEntity.ok(billService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBillById(@PathVariable Long id, @RequestBody Bill billRequest) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return ResponseEntity.ok(billService.update(id, billRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteBillByID(@PathVariable Long id) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        billService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/period/{id}")
    public List<Bill> findByPeriodId(@PathVariable Long id) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return billService.findByPeriodId(id);
    }
}
