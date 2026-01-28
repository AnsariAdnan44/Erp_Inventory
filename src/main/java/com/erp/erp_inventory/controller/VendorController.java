package com.erp.erp_inventory.controller;


import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.erp.erp_inventory.entity.Vendor;
import com.erp.erp_inventory.repository.VendorRepository;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "*")
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    // ADD vendor
    @PostMapping
    public Vendor addVendor(@RequestBody Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    // VIEW all vendors
    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // UPDATE vendor
    @PutMapping("/{id}")
    public Vendor updateVendor(@PathVariable Long id, @RequestBody Vendor vendor) {
        Vendor existing = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        existing.setName(vendor.getName());
        existing.setPhone(vendor.getPhone());
        existing.setEmail(vendor.getEmail());

        return vendorRepository.save(existing);
    }

    // DELETE vendor
    @DeleteMapping("/{id}")
    public String deleteVendor(@PathVariable Long id) {
        vendorRepository.deleteById(id);
        return "Vendor deleted successfully";
    }
}

