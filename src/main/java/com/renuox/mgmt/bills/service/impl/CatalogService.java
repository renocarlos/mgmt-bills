package com.renuox.mgmt.bills.service.impl;

import com.renuox.mgmt.bills.exception.ResourceNotFoundException;
import com.renuox.mgmt.bills.model.Catalog;
import com.renuox.mgmt.bills.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {

    @Autowired
    CatalogRepository catalogRepository;

    public List<Catalog> findAll() {
        return catalogRepository.findAll();
    }

    public Catalog findById(Long id) {
        return catalogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catalog not found with id: " + id));
    }

    public Catalog save(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    public Catalog update(Long id, @org.jetbrains.annotations.NotNull Catalog catalogRequest) {
        catalogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catalog not found with id: " + id));
        catalogRequest.setId(id);
        return catalogRepository.save(catalogRequest);
    }

    public void delete(Long id) {
        catalogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catalog not found with id: " + id));
        catalogRepository.deleteById(id);
    }

    public int saveCatalogs(List<Catalog> catalogs) {
        int i = 0;
        for (Catalog catalog : catalogs) {
            if (catalogRepository.findByName(catalog.getName()) == null) {
                catalogRepository.save(catalog);
                i++;
            }
        }
        return i;
    }

    public int updateCatalogs(List<Catalog> catalogs) {
        int i = 0;
        for (Catalog catalogRequested : catalogs) {
            if (catalogRepository.findById(catalogRequested.getId()).isPresent()) {
                catalogRepository.save(catalogRequested);
                i++;
            }
        }
        return i;
    }

    public void initializeCatalogs() {
        if (catalogRepository.findByName("first-period-day") == null) {
            catalogRepository.save(new Catalog("7", "first-period-day", "Day from first period"));
        }
        if (catalogRepository.findByName("second-period-day") == null) {
            catalogRepository.save(new Catalog("21", "second-period-day", "Day from second period"));
        }
        if (catalogRepository.findByName("daily-amount") == null) {
            catalogRepository.save(new Catalog("0", "daily-amount", "daily amount"));
        }
        if (catalogRepository.findByName("weekend-amount") == null) {
            catalogRepository.save(new Catalog("0", "weekend-amount", "weekend amount"));
        }
    }

    public Catalog findByName(String name) {
        return catalogRepository.findByName(name);
    }


}
