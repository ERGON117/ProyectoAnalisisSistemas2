// src/app/models/menu.model.ts
import { Modulo } from './modulo.model';

export interface Menu {
  idMenu: number;
  modulo: Modulo;
  nombre: string;
  ordenMenu: number;
  fechaCreacion?: string;
  usuarioCreacion?: string;
  fechaModificacion?: string;
  usuarioModificacion?: string;
}
