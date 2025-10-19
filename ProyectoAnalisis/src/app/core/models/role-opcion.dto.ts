// src/app/models/role-opcion.dto.ts
export interface RoleOpcionDTO {
  idRole: number;
  idOpcion: number;
  alta: boolean;
  baja: boolean;
  cambio: boolean;
  imprimir: boolean;
  exportar: boolean;
}
