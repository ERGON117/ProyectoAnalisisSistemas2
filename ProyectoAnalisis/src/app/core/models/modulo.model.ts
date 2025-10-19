// src/app/models/modulo.model.ts
export interface Modulo {
  idModulo: number;
  nombre: string; // Asumido, ajusta si hay más campos
  ordenMenu: number;
  fechaCreacion?: string;
  usuarioCreacion?: string;
  fechaModificacion?: string;
  usuarioModificacion?: string;
}
