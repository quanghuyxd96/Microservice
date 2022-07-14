package com.example.demo.service;

import com.example.demo.entity.Supplier;
import com.example.demo.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;


    public Supplier saveSupplier(Supplier supplier) {
        Supplier supplierRepo = supplierRepository.findByName(supplier.getName());
        if (supplierRepo == null) {
            return supplierRepository.save(supplier);
        }
        return null;
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        if (suppliers == null) {
            return null;
        }
        return suppliers;
    }

    public Supplier getSupplierById(long id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
            return supplier.get();
        }
        return null;
    }

    public Supplier updateSupplierById(Supplier supplier, long id) {
        Optional<Supplier> supplierRepositoryById = supplierRepository.findById(id);
        if(!supplierRepositoryById.isPresent()){
            return null;
        }
        supplierRepositoryById.get().setName(supplier.getName());
        supplierRepositoryById.get().setAddress(supplier.getAddress());
        supplierRepositoryById.get().setPhoneNumber(supplier.getPhoneNumber());
        return supplierRepository.save(supplierRepositoryById.get());
    }

    public boolean deleteSupplierById(long id) {
        Optional<Supplier> orderRepositoryById = supplierRepository.findById(id);
        if (orderRepositoryById.isPresent()) {
            supplierRepository.deleteById(id);
            return true;
        }
        return false;
    }



}
