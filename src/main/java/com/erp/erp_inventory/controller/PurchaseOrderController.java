package com.erp.erp_inventory.controller;

import com.erp.erp_inventory.entity.PurchaseOrder;
import com.erp.erp_inventory.service.PurchaseOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/po")
@CrossOrigin(origins = "*")
public class PurchaseOrderController {

    private final PurchaseOrderService poService;

    public PurchaseOrderController(PurchaseOrderService poService) {
        this.poService = poService;
    }

    // Create PO
    @PostMapping
    public PurchaseOrder createPO(@RequestBody PurchaseOrder po) {
        return poService.createPO(po);
    }

    // Approve PO
    @PutMapping("/{id}/approve")
    public PurchaseOrder approvePO(@PathVariable Long id) {
        return poService.approvePO(id);
    }

    // Receive Goods
    @PostMapping("/{id}/receive")
    public String receiveGoods(@PathVariable Long id) {
        poService.receiveGoods(id);
        return "Goods received successfully";
    }

    // All POs
    @GetMapping
    public List<PurchaseOrder> getAllPOs() {
        return poService.getAllPOs();
    }

    // Pending POs
    @GetMapping("/pending")
    public List<PurchaseOrder> getPendingPOs() {
        return poService.getPendingPOs();
    }

    // Approved POs
    @GetMapping("/approved")
    public List<PurchaseOrder> getApprovedPOs() {
        return poService.getApprovedPOs();
    }
}
