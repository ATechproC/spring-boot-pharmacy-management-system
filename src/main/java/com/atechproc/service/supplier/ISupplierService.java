package com.atechproc.service.supplier;

import com.atechproc.dto.SupplierDto;
import com.atechproc.model.Supplier;
import com.atechproc.request.supplier.CreateSupplierRequest;
import com.atechproc.request.supplier.UpdateSupplierRequest;

import java.util.List;

public interface ISupplierService {
    Supplier getSupplierById(Long id);
    SupplierDto createSupplier(CreateSupplierRequest request, String jwt) throws Exception;
    SupplierDto updateSupplier(UpdateSupplierRequest request, Long id, String jwt) throws Exception;
    List<SupplierDto> getAllSuppliers(String jwt) throws Exception;
    void deleteSupplier(Long id, String jwt) throws Exception;
    List<SupplierDto> searchForSupplier(String keyword, String jwt) throws Exception;
}
