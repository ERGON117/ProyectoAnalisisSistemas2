export interface MenuResponse {
  id: number;
  nombre: string;
  orden: number;
  // Cambio: Renombrado 'menus' a 'menuItems' para mantener consistencia con el backend
  menuItems: MenuItem[];
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
