package com.proyecto.analisis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleOpcionId implements Serializable {

    private Integer role; // Corresponde a IdRole
    private Integer opcion; // Corresponde a IdOpcion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleOpcionId that = (RoleOpcionId) o;
        return role.equals(that.role) && opcion.equals(that.opcion);
    }

    @Override
    public int hashCode() {
        return 31 * role.hashCode() + opcion.hashCode();
    }
}