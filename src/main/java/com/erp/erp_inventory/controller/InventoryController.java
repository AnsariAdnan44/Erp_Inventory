package com.erp.erp_inventory.controller;

import com.erp.erp_inventory.entity.Inventory;
import com.erp.erp_inventory.repository.InventoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*") // allow frontend calls
public class InventoryController {

    private final InventoryRepository invRepo;

    public InventoryController(InventoryRepository invRepo) {
        this.invRepo = invRepo;
    }

    // GET /api/inventory → list all products
    @GetMapping
    public List<Inventory> getAllInventory() {
        return invRepo.findAll();
    }
}
