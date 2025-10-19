package com.proyecto.analisis.service;

import com.proyecto.analisis.dto.RoleOpcionDTO;
import com.proyecto.analisis.entity.Opcion;
import com.proyecto.analisis.entity.Role;
import com.proyecto.analisis.entity.RoleOpcion;
import com.proyecto.analisis.repository.OpcionRepository;
import com.proyecto.analisis.repository.RoleOpcionRepository;
import com.proyecto.analisis.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleOpcionService {

    @Autowired
    private RoleOpcionRepository roleOpcionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OpcionRepository opcionRepository;

    @Transactional
    public void asignarOpcionesARol(Integer idRole, List<RoleOpcionDTO> opciones) {
        // Eliminar asignaciones existentes para el rol
        roleOpcionRepository.deleteByRole_IdRole(idRole);

        // Obtener el rol
        Role role = roleRepository.findById(idRole)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Asignar nuevas opciones
        List<RoleOpcion> roleOpciones = opciones.stream().map(dto -> {
            Opcion opcion = opcionRepository.findById(dto.getIdOpcion())
                    .orElseThrow(() -> new RuntimeException("Opción no encontrada"));

            return RoleOpcion.builder()
                    .role(role)
                    .opcion(opcion)
                    .alta(dto.getAlta() != null ? dto.getAlta() : false)
                    .baja(dto.getBaja() != null ? dto.getBaja() : false)
                    .cambio(dto.getCambio() != null ? dto.getCambio() : false)
                    .imprimir(dto.getImprimir() != null ? dto.getImprimir() : false)
                    .exportar(dto.getExportar() != null ? dto.getExportar() : false)
                    .fechaCreacion(LocalDateTime.now())
                    .usuarioCreacion("Administrador") // Valor predeterminado
                    .fechaModificacion(LocalDateTime.now())
                    .usuarioModificacion("Administrador") // Valor predeterminado
                    .build();
        }).collect(Collectors.toList());

        // Guardar las nuevas asignaciones
        roleOpcionRepository.saveAll(roleOpciones);
    }

    public List<RoleOpcion> obtenerOpcionesPorRol(Integer idRole) {
        return roleOpcionRepository.findByRole_IdRole(idRole);
    }
}