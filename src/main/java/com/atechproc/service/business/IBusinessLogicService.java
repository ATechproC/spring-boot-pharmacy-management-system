package com.atechproc.service.business;

import com.atechproc.dto.MedicineDto;
import com.atechproc.dto.SupplierDto;
import com.atechproc.model.Supplier;

import java.util.List;

public interface IBusinessLogicService {
    Long countNumberOfActiveMedicinesInSupplier(Long pharmacyId, Long supplierId);
    Long countNumberOfActiveMedicinesInGroup(Long pharmacyId, Long groupId);
}
