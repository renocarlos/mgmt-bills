


package com.renuox.mgmt.bills;

import com.renuox.mgmt.bills.service.impl.CatalogService;
import com.renuox.mgmt.bills.service.impl.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StartupListener {

    @Autowired
    CatalogService catalogService;

    @Autowired
    PeriodService periodService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        catalogService.initializeCatalogs();
        periodService.saveByYear(LocalDate.now().getYear());
    }
}


