package com.proyecto.analisis.dto;

import lombok.Data;
import java.util.List;

@Data
public class MenuResponse {
    private int id;
    private String nombre;
    private int orden;
    private List<MenuItem> menus;

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