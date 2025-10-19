package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.SaldoCuentaHist;
import com.proyecto.analisis.entity.SaldoCuentaHistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaldoCuentaHistRepository extends JpaRepository<SaldoCuentaHist, SaldoCuentaHistId>, 
                                                   JpaSpecificationExecutor<SaldoCuentaHist> {
    
    boolean existsByAnioAndMesAndIdSaldoCuenta(Integer anio, Integer mes, Integer idSaldoCuenta);
    
    List<SaldoCuentaHist> findByAnioAndMes(Integer anio, Integer mes);
}