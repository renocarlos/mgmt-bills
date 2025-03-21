package com.renuox.mgmt.bills.controller;

import com.renuox.mgmt.bills.model.BothPeriods;
import com.renuox.mgmt.bills.service.impl.BothPeriodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "${cors.allowedOrigins}")
@RestController
@RequestMapping("/both-periods")
public class BothPeriodsController {

    @Autowired
    BothPeriodsService bothPeriodsService;

    @GetMapping("/next-periods")
    public List<BothPeriods> findNextPeriods(@RequestParam int nextPeriods) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return bothPeriodsService.findNextPeriods(nextPeriods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BothPeriods> getBothPeriodsByPeriodId(@PathVariable Long id) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return ResponseEntity.ok(bothPeriodsService.getBothPeriodsByPeriodId(id));
    }

    @GetMapping("/current")
    public BothPeriods getCurrentPeriod() {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return bothPeriodsService.getCurrentPeriod();
    }

}