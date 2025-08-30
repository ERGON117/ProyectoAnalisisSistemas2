// src/app/core/models/menu-response.model.ts
export interface MenuResponse {
  id: number;
  nombre: string;
  orden: number;
  menus: MenuItem[];
}

export interface MenuItem {
  id: number;
  nombre: string;
  orden: number;
  opciones: OpcionItem[];
}

export interface OpcionItem {
  id: number;
  nombre: string;
  orden: number;
  pagina: string;
  alta: boolean;
  baja: boolean;
  cambio: boolean;
  imprimir: boolean;
  exportar: boolean;
}
