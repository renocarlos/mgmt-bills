package com.renuox.mgmt.bills.controller;

import com.renuox.mgmt.bills.model.Catalog;
import com.renuox.mgmt.bills.service.impl.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${cors.allowedOrigins}")
@RestController
@RequestMapping("/catalogs")
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @GetMapping("/list")
    public List<Catalog> listCatalog() {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return catalogService.findAll();
    }

    @PostMapping
    public Catalog saveCatalog(@RequestBody Catalog catalog) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return catalogService.save(catalog);
    }

    @PostMapping("/list")
    public ResponseEntity<Map<String, Integer>>  saveCatalogs(@RequestBody List<Catalog> catalogs) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        int saved = catalogService.saveCatalogs(catalogs);
        Map<String, Integer> response = new HashMap<>();
        response.put("saved", saved);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Catalog> listCatalogById(@PathVariable Long id) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return ResponseEntity.ok(catalogService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Catalog> updateCatalogById(@PathVariable Long id, @RequestBody Catalog catalogRequest) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return ResponseEntity.ok(catalogService.update(id, catalogRequest));
    }

    @PutMapping("/list")
    public ResponseEntity<Map<String, Integer>>  updateCatalogs(@RequestBody List<Catalog> catalogs) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        int saved = catalogService.updateCatalogs(catalogs);
        Map<String, Integer> response = new HashMap<>();
        response.put("updated", saved);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteCatalogById(@PathVariable Long id) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        catalogService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
