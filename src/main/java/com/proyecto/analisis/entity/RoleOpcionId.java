package com.proyecto.analisis.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleOpcionId implements Serializable {

    private Integer role;
    private Integer opcion;

  

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleOpcionId that = (RoleOpcionId) o;
        return Objects.equals(role, that.role) && Objects.equals(opcion, that.opcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, opcion);
    }
}