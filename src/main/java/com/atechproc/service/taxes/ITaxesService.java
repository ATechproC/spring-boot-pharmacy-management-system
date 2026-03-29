package com.atechproc.service.taxes;

import com.atechproc.dto.TaxDto;
import com.atechproc.model.Tax;
import com.atechproc.request.taxes.AddNewTaxRequest;
import com.atechproc.request.taxes.UpdateTaxRequest;

import java.util.List;

public interface ITaxesService {
    Tax getTaxById(Long id);
    TaxDto addNewTax(AddNewTaxRequest required, String jwt) throws Exception;
    TaxDto updateTax(Long id,UpdateTaxRequest request, String jwt) throws Exception;
    List<TaxDto> getTaxes(String jwt);
    void deleteTax(Long id, String jwt) throws Exception;
}
