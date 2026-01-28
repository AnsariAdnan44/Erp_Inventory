package com.erp.erp_inventory.service;
import com.erp.erp_inventory.entity.Vendor;
import com.erp.erp_inventory.repository.VendorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VendorService {
    private final VendorRepository repo;
    public VendorService(VendorRepository repo) { this.repo = repo; }

    public Vendor addVendor(Vendor v) { return repo.save(v); }
    public List<Vendor> getAll() { return repo.findAll(); }
    public Vendor updateVendor(Vendor v) { return repo.save(v); }
    public void deleteVendor(Long id) { repo.deleteById(id); }
}
