package com.erp.erp_inventory.service;

import com.erp.erp_inventory.entity.*;
import com.erp.erp_inventory.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository poRepo;
    private final InventoryRepository invRepo;
    private final GoodsReceiptNoteRepository grnRepo;

    public PurchaseOrderService(PurchaseOrderRepository poRepo,
                                InventoryRepository invRepo,
                                GoodsReceiptNoteRepository grnRepo) {
        this.poRepo = poRepo;
        this.invRepo = invRepo;
        this.grnRepo = grnRepo;
    }

    // Create PO
    public PurchaseOrder createPO(PurchaseOrder po) {
    po.setStatus(POStatus.CREATED);

    // 🔥 CRITICAL FIX: link items to PO
    if (po.getItems() != null) {
        for (PurchaseOrderItem item : po.getItems()) {
            item.setPurchaseOrder(po);
        }
    }

    return poRepo.save(po);
}


    // Approve PO
    public PurchaseOrder approvePO(Long poId) {
        PurchaseOrder po = poRepo.findById(poId)
                .orElseThrow(() -> new RuntimeException("PO not found"));

        if (po.getStatus() != POStatus.CREATED)
            throw new RuntimeException("PO already approved");

        po.setStatus(POStatus.APPROVED);
        return poRepo.save(po);
    }

    // Receive Goods (ERP CRITICAL LOGIC)
    @Transactional
    public void receiveGoods(Long poId) {

        PurchaseOrder po = poRepo.findById(poId)
                .orElseThrow(() -> new RuntimeException("PO not found"));

        if (po.getStatus() != POStatus.APPROVED)
            throw new RuntimeException("PO must be approved before receiving goods");

        // Update inventory
        for (PurchaseOrderItem item : po.getItems()) {

            Inventory inventory = invRepo.findByProductName(item.getProductName());

            if (inventory == null) {
                inventory = new Inventory();
                inventory.setProductName(item.getProductName());
                inventory.setQuantity(item.getQuantity());
            } else {
                inventory.setQuantity(
                        inventory.getQuantity() + item.getQuantity()
                );
            }

            invRepo.save(inventory);
        }

        // Mark PO as RECEIVED
        po.setStatus(POStatus.APPROVED);
        poRepo.save(po);

        // Create GRN
        GoodsReceiptNote grn = new GoodsReceiptNote();
        grn.setPurchaseOrder(po);
        grn.setReceivedDate(LocalDate.now());
        grnRepo.save(grn);
    }

    // Reports
    public List<PurchaseOrder> getAllPOs() {
        return poRepo.findAll();
    }

    public List<PurchaseOrder> getPendingPOs() {
        return poRepo.findByStatus(POStatus.CREATED);
    }

    public List<PurchaseOrder> getApprovedPOs() {
        return poRepo.findByStatus(POStatus.APPROVED);
    }
}
