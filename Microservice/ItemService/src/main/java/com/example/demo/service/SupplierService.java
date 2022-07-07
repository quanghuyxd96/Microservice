package com.example.demo.service;

import com.example.demo.entity.Item;
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



    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }


    public Supplier saveSupplier(Supplier supplier) {
        List<Supplier> suppliers = supplierRepository.findAll();
        long idMax =0;
        for(Supplier supplier1 : suppliers){
            idMax = Math.max(idMax,supplier1.getId());
        }
        supplier.setId(idMax+1);
        return supplierRepository.save(supplier);
    }


    public Supplier getSupplierById(long id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
            return supplier.get();
        }
        return null;
    }


    public boolean deleteSupplierById(long id) {
        Optional<Supplier> orderRepositoryById = supplierRepository.findById(id);
        if(orderRepositoryById.isPresent()){
            supplierRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public Supplier updateSupplierById(Supplier supplier, long id) {
        Optional<Supplier> supplierRepositoryById = supplierRepository.findById(id);
        supplierRepositoryById.get().setName(supplier.getName());
        supplierRepositoryById.get().setAddress(supplier.getAddress());
        supplierRepositoryById.get().setPhoneNumber(supplier.getPhoneNumber());
        return supplierRepository.save(supplierRepositoryById.get());
    }
}
