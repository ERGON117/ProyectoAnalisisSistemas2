package com.proyecto.analisis.dto;

import com.proyecto.analisis.entity.StatusCuenta;
import com.proyecto.analisis.entity.TipoSaldoCuenta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatologosCuentasDTO {
    private List<StatusCuenta> statusCuentas;
    private List<TipoSaldoCuenta> tiposSaldoCuentas;
}