package com.atechproc.dto;

import lombok.Data;

@Data
public class InventoryDetails {
    private Long availableMedicines;
    private Long expiredMedicines;
    private Long suppliers;
    private Long groups;
    private Long pharmacists;
    private Long medicinesOutOfStock;
    private Long totalNumberOfMedicines;
    private Long totalOfCredits;
}
