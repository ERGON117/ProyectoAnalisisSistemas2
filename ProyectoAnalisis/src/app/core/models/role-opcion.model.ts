// src/app/models/role-opcion.model.ts
import { Role } from './role.model';
import { Opcion } from './opcion.model';

export interface RoleOpcion {
  role: Role;
  opcion: Opcion;
  alta: boolean;
  baja: boolean;
  cambio: boolean;
  imprimir: boolean;
  exportar: boolean;
  fechaCreacion?: string;
  usuarioCreacion?: string;
  fechaModificacion?: string;
  usuarioModificacion?: string;
}
