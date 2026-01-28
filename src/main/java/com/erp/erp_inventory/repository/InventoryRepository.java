package com.erp.erp_inventory.repository;
import com.erp.erp_inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByProductName(String productName);
}
