package com.erp.erp_inventory.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.erp.erp_inventory.entity.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
