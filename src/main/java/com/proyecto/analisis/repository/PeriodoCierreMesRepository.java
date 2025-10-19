package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.PeriodoCierreMes;
import com.proyecto.analisis.entity.PeriodoCierreMesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodoCierreMesRepository extends JpaRepository<PeriodoCierreMes, PeriodoCierreMesId>, 
                                                     JpaSpecificationExecutor<PeriodoCierreMes> {
    
    Optional<PeriodoCierreMes> findByAnioAndMes(Integer anio, Integer mes);
    
    List<PeriodoCierreMes> findByFechaCierreIsNullOrderByAnioAscMesAsc();
    
    List<PeriodoCierreMes> findByFechaCierreIsNull();
}