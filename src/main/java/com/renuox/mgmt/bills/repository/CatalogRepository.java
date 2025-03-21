package com.renuox.mgmt.bills.repository;

import com.renuox.mgmt.bills.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    Catalog findByName(String name);
}
