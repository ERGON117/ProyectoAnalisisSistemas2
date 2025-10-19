package com.proyecto.analisis.dto;

public class UserAgentParser {

    public static String getSistemaOperativo(String userAgent) {
        if (userAgent == null) return "Desconocido";
        if (userAgent.toLowerCase().contains("windows")) return "Windows";
        if (userAgent.toLowerCase().contains("mac")) return "MacOS";
        if (userAgent.toLowerCase().contains("x11")) return "Unix";
        if (userAgent.toLowerCase().contains("android")) return "Android";
        if (userAgent.toLowerCase().contains("iphone")) return "iOS";
        return "Desconocido";
    }

    public static String getNavegador(String userAgent) {
        if (userAgent == null) return "Desconocido";
        if (userAgent.contains("Edg")) return "Microsoft Edge";
        if (userAgent.contains("Chrome")) return "Google Chrome";
        if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) return "Safari";
        if (userAgent.contains("Firefox")) return "Mozilla Firefox";
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) return "Internet Explorer";
        return "Desconocido";
    }

    public static String getDispositivo(String userAgent) {
        if (userAgent == null) return "Desconocido";
        if (userAgent.toLowerCase().contains("mobile")) return "Móvil";
        if (userAgent.toLowerCase().contains("tablet")) return "Tablet";
        return "PC";
    }
}
