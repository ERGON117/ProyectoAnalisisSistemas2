package com.proyecto.analisis.dto;

import lombok.Data;
import java.util.List;

@Data
public class MenuResponse {
    private int id;
    private String nombre;
    private int orden;
    // Cambio: Renombrado 'menus' a 'menuItems' para reflejar que ahora contiene menús agrupados bajo un módulo
    private List<MenuItem> menuItems;

    @Data
    public static class MenuItem {
        private int id;
        private String nombre;
        private int orden;
        private List<OpcionItem> opciones;
    }

    @Data
    public static class OpcionItem {
        private int id;
        private String nombre;
        private int orden;
        private String pagina;
        private boolean alta;
        private boolean baja;
        private boolean cambio;
        private boolean imprimir;
        private boolean exportar;
    }
}