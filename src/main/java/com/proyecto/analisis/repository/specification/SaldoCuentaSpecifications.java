package com.proyecto.analisis.repository.specification;

import com.proyecto.analisis.entity.SaldoCuenta;
import org.springframework.data.jpa.domain.Specification;

public class SaldoCuentaSpecifications {

    public static Specification<SaldoCuenta> isActivo() {
        return (root, query, criteriaBuilder) -> {
            // Asumiendo que StatusCuenta tiene un campo 'activo'
            return criteriaBuilder.isTrue(root.get("statusCuenta").get("activo"));
        };
    }
}