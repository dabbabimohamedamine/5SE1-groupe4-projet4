package tn.esprit.devops_project.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {
    private Long idSupplier;
    private String code;
    private String label;
    private SupplierCategory supplierCategory;
}