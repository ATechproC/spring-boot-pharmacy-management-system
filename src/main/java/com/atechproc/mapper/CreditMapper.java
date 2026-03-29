package com.atechproc.mapper;

import com.atechproc.dto.CreditDto;
import com.atechproc.model.Credit;

import java.util.List;

public class CreditMapper {
    public static CreditDto toDto(Credit credit) {
        CreditDto dto = new CreditDto();
        dto.setId(credit.getId());
        dto.setClientId(credit.getClientId());
        dto.setName(credit.getName());
        dto.setTotalAmount(credit.getTotalAmount());
        dto.setPaidAmount(credit.getPaidAmount());
        dto.setRemainingAmount(credit.getRemainingAmount());
        dto.setStatus(credit.getStatus());
        return dto;
    }

    public static List<CreditDto> toDTOs(List<Credit> credits) {
        return credits.stream()
                .map(CreditMapper::toDto).toList();
    }
}
