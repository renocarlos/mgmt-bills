package com.renuox.mgmt.bills.controller;

import com.renuox.mgmt.bills.enums.PeriodType;
import com.renuox.mgmt.bills.model.Period;
import com.renuox.mgmt.bills.service.impl.PeriodService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${cors.allowedOrigins}")
@RestController
@RequestMapping("/periods")
public class PeriodController {

    @Autowired
    PeriodService periodService;

    @GetMapping("/list")
    public List<Period> listPeriod() {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return periodService.findAll();
    }

    @GetMapping("/year")
    public List<Period> findByYear(@RequestParam int year) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return periodService.findByYear(year);
    }

    @GetMapping
    public Period findByTypeAndYearAndMonthNumberAndDay(@RequestParam PeriodType type, @RequestParam int year,
                                                        @RequestParam int monthNumber, @RequestParam int day) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return periodService.findByTypeAndYearAndMonthNumberAndDay(type, year, monthNumber, day);
    }

    @PostMapping("/year")
    public ResponseEntity<Map<String, Boolean>> savePeriodByYear(@RequestBody @NotNull Period period) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        periodService.saveByYear(period.getYear());

        Map<String, Boolean> response = new HashMap<>();
        response.put("saved", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Period> getPeriodById(@PathVariable Long id) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return ResponseEntity.ok(periodService.findById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Period> updatePeriodById(@PathVariable Long id, @RequestBody Period periodRequest) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return ResponseEntity.ok(periodService.update(id, periodRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deletePeriodById(@PathVariable Long id) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        periodService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
