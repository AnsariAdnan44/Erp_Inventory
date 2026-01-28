package com.erp.erp_inventory.service;

import com.erp.erp_inventory.entity.*;
import com.erp.erp_inventory.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository poRepo;
    private final InventoryRepository invRepo;
    private final GoodsReceiptNoteRepository grnRepo;
    private final VendorRepository vendorRepo;

    public PurchaseOrderService(PurchaseOrderRepository poRepo,
                                InventoryRepository invRepo,
                                GoodsReceiptNoteRepository grnRepo,
                                VendorRepository vendorRepo) {
        this.poRepo = poRepo;
        this.invRepo = invRepo;
        this.grnRepo = grnRepo;
        this.vendorRepo = vendorRepo;
    }

    // Create a new PO
    public PurchaseOrder createPO(PurchaseOrder po) {
        po.setStatus(POStatus.CREATED);
        return poRepo.save(po);
    }

    // Approve an existing PO
    public PurchaseOrder approvePO(Long poId) {
        PurchaseOrder po = poRepo.findById(poId)
                .orElseThrow(() -> new RuntimeException("PO not found"));
        if (po.getStatus() != POStatus.CREATED)
            throw new RuntimeException("PO already approved");
        po.setStatus(POStatus.APPROVED);
        return poRepo.save(po);
    }

    // Receive goods for a PO → updates inventory and creates GRN
    public void receiveGoods(Long poId) {
        PurchaseOrder po = poRepo.findById(poId)
                .orElseThrow(() -> new RuntimeException("PO not found"));

        if (po.getStatus() != POStatus.APPROVED)
            throw new RuntimeException("PO must be approved before receiving goods");

        // Update Inventory
        for (PurchaseOrderItem item : po.getItems()) {
            Inventory inv = invRepo.findByProductName(item.getProductName());
            if (inv == null) {
                inv = new Inventory();
                inv.setProductName(item.getProductName());
                inv.setQuantity(item.getQuantity());
            } else {
                inv.setQuantity(inv.getQuantity() + item.getQuantity());
            }
            invRepo.save(inv);

            // Optional: mark item as received by setting quantity to 0
            item.setQuantity(0);
        }

        // Save updated PO items
        poRepo.save(po);

        // Create Goods Receipt Note
        GoodsReceiptNote grn = new GoodsReceiptNote();
        grn.setPurchaseOrder(po);
        grn.setReceivedDate(LocalDate.now());
        grnRepo.save(grn);
    }

    // List all POs
    public List<PurchaseOrder> getAllPOs() {
        return poRepo.findAll();
    }

    // List pending POs (CREATED)
    public List<PurchaseOrder> getPendingPOs() {
        return poRepo.findByStatus(POStatus.CREATED);
    }

    // List approved POs
    public List<PurchaseOrder> getApprovedPOs() {
        return poRepo.findByStatus(POStatus.APPROVED);
    }
}
