// src/app/models/opcion.model.ts
import { Menu } from './menu.model';

export interface Opcion {
  idOpcion: number;
  menu: Menu;
  nombre: string;
  ordenMenu: number;
  pagina: string;
  fechaCreacion?: string;
  usuarioCreacion?: string;
  fechaModificacion?: string;
  usuarioModificacion?: string;
}
