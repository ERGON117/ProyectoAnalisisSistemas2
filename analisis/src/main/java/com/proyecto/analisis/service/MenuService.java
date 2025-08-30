package com.proyecto.analisis.service;

import com.proyecto.analisis.dto.MenuResponse;
import com.proyecto.analisis.entity.Usuario;
import com.proyecto.analisis.entity.Menu;
import com.proyecto.analisis.entity.Opcion;
import com.proyecto.analisis.service.AuthService;
import com.proyecto.analisis.entity.RoleOpcion;
import com.proyecto.analisis.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;

    public List<MenuResponse> getMenuStructure(String token) {
        // Obtener usuario desde el token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(authService.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String idUsuario = claims.getSubject();
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Recuperar las opciones permitidas por el rol del usuario
    List<RoleOpcion> roleOpciones = usuario.getRole().getRoleOpciones();

        // Agrupar por menú
        Map<Menu, List<RoleOpcion>> menuGrouped = roleOpciones.stream()
                .collect(Collectors.groupingBy(ro -> ro.getOpcion().getMenu()));

        // Convertir al DTO
        List<MenuResponse> response = new ArrayList<>();

        for (Map.Entry<Menu, List<RoleOpcion>> entry : menuGrouped.entrySet()) {
            Menu menu = entry.getKey();
            List<RoleOpcion> opcionesRol = entry.getValue();

            MenuResponse menuResponse = new MenuResponse();
            menuResponse.setId(menu.getIdMenu());
            menuResponse.setNombre(menu.getNombre());
            menuResponse.setOrden(menu.getOrdenMenu());

            List<MenuResponse.OpcionItem> opcionItems = new ArrayList<>();
            for (RoleOpcion ro : opcionesRol) {
                Opcion opcion = ro.getOpcion();

                MenuResponse.OpcionItem opcionItem = new MenuResponse.OpcionItem();
                opcionItem.setId(opcion.getIdOpcion());
                opcionItem.setNombre(opcion.getNombre());
                opcionItem.setOrden(opcion.getOrdenMenu());
                opcionItem.setPagina(opcion.getPagina());
                opcionItem.setAlta(Boolean.TRUE.equals(ro.getAlta()));
                opcionItem.setBaja(Boolean.TRUE.equals(ro.getBaja()));
                opcionItem.setCambio(Boolean.TRUE.equals(ro.getCambio()));
                opcionItem.setImprimir(Boolean.TRUE.equals(ro.getImprimir()));
                opcionItem.setExportar(Boolean.TRUE.equals(ro.getExportar()));

                opcionItems.add(opcionItem);
            }

            // ordenar opciones por ordenMenu
            opcionItems.sort(Comparator.comparingInt(MenuResponse.OpcionItem::getOrden));

            // Setear dentro de un único menú
            MenuResponse.MenuItem menuItem = new MenuResponse.MenuItem();
            menuItem.setId(menu.getIdMenu());
            menuItem.setNombre(menu.getNombre());
            menuItem.setOrden(menu.getOrdenMenu());
            menuItem.setOpciones(opcionItems);

            menuResponse.setMenus(List.of(menuItem));
            response.add(menuResponse);
        }

        // ordenar menús por orden
        response.sort(Comparator.comparingInt(MenuResponse::getOrden));

        return response;
    }
}
