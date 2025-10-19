package com.proyecto.analisis.repository.specification;

import com.proyecto.analisis.entity.PeriodoCierreMes;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class PeriodoCierreMesSpecifications {

    public static Specification<PeriodoCierreMes> hasAnioAndMesAndFechaCierreNull(Integer anio, Integer mes) {
        return (root, query, criteriaBuilder) -> {
            Predicate anioPredicate = criteriaBuilder.equal(root.get("anio"), anio);
            Predicate mesPredicate = criteriaBuilder.equal(root.get("mes"), mes);
            Predicate fechaCierreNullPredicate = criteriaBuilder.isNull(root.get("fechaCierre"));
            
            return criteriaBuilder.and(anioPredicate, mesPredicate, fechaCierreNullPredicate);
        };
    }
}