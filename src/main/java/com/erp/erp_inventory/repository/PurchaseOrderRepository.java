package com.erp.erp_inventory.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.erp.erp_inventory.entity.PurchaseOrder;
import com.erp.erp_inventory.entity.POStatus;
import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByStatus(POStatus status);
}
