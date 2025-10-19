// src/app/components/asignar-opciones/asignar-opciones.component.ts
import { Component, OnInit } from '@angular/core';
import { RoleService } from '../../core/services/role.service';
import { OpcionService } from '../../core/services/opcion.service';
import { RoleOpcionService } from '../../core/services/role-opcion.service';
import { Role } from '../../core/models/role.model';
import { Opcion } from '../../core/models/opcion.model';
import { RoleOpcionDTO } from '../../core/models/role-opcion.dto';

@Component({
  selector: 'app-role-opcion',
  templateUrl: './role-opcion.component.html',
  styleUrls: ['./role-opcion.component.css']
})
export class RoleOpcionComponent implements OnInit {
 roles: Role[] = [];
  opciones: Opcion[] = [];
  selectedRole: number | null = null;
  asignaciones: RoleOpcionDTO[] = [];

  constructor(
    private roleService: RoleService,
    private opcionService: OpcionService,
    private roleOpcionService: RoleOpcionService
  ) {}

  ngOnInit(): void {
    this.opcionService.getOpciones().subscribe(opciones => {
      this.opciones = opciones;
      this.cargarRoles();
    });
  }

  cargarRoles(): void {
    this.roleService.getRoles().subscribe(roles => this.roles = roles);
  }

  onRoleChange(): void {
    if (this.selectedRole) {
      this.roleOpcionService.obtenerOpcionesPorRol(this.selectedRole).subscribe(asignacionesExistentes => {
        // Inicializa asignaciones con las existentes o valores por defecto
        this.asignaciones = this.opciones.map(opcion => {
          const asignacionExistente = asignacionesExistentes.find(a => a.opcion.idOpcion === opcion.idOpcion);
          return {
            idRole: this.selectedRole!,
            idOpcion: opcion.idOpcion,
            alta: asignacionExistente?.alta || false,
            baja: asignacionExistente?.baja || false,
            cambio: asignacionExistente?.cambio || false,
            imprimir: asignacionExistente?.imprimir || false,
            exportar: asignacionExistente?.exportar || false
          };
        });
      });
    } else {
      this.asignaciones = [];
    }
  }

  getOpcionNombre(idOpcion: number): string {
    const opcion = this.opciones.find(op => op.idOpcion === idOpcion);
    return opcion ? opcion.nombre : 'N/A';
  }

  guardarAsignaciones(): void {
    if (this.selectedRole && this.asignaciones.length > 0) {
      // Filtra solo las asignaciones con al menos un permiso seleccionado
      const opcionesSeleccionadas = this.asignaciones.filter(asignacion =>
        asignacion.alta || asignacion.baja || asignacion.cambio || asignacion.imprimir || asignacion.exportar
      );

      if (opcionesSeleccionadas.length > 0) {
        this.roleOpcionService.asignarOpciones(this.selectedRole, opcionesSeleccionadas)
          .subscribe({
            next: (response) => alert(response.message || 'Opciones asignadas correctamente'),
            error: (err) => {
              console.error('Error completo:', err);
              alert('Error: ' + (err.error?.message || err.message || 'Error desconocido'));
            }
          });
      } else {
        alert('No hay opciones seleccionadas para guardar.');
      }
    }
  }
}
