package com.proyecto.analisis.service;

import com.proyecto.analisis.dto.MenuResponse;
import com.proyecto.analisis.entity.Usuario;
import com.proyecto.analisis.entity.Menu;
import com.proyecto.analisis.entity.Modulo;
import com.proyecto.analisis.entity.Opcion;
import com.proyecto.analisis.service.AuthService;
import com.proyecto.analisis.entity.RoleOpcion;
import com.proyecto.analisis.repository.MenuRepository;
import com.proyecto.analisis.repository.ModuloRepositori;
import com.proyecto.analisis.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;
    private final MenuRepository repository;
    private final ModuloRepositori moduloRepository;

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

        // Agrupar por módulo y luego por menú
        // Cambio: Modificado para agrupar primero por módulo y luego por menú para incluir la estructura de módulos
        Map<Modulo, Map<Menu, List<RoleOpcion>>> moduloGrouped = roleOpciones.stream()
                .collect(Collectors.groupingBy(
                        ro -> ro.getOpcion().getMenu().getModulo(),
                        Collectors.groupingBy(ro -> ro.getOpcion().getMenu())
                ));

        // Convertir al DTO
        List<MenuResponse> response = new ArrayList<>();

        for (Map.Entry<Modulo, Map<Menu, List<RoleOpcion>>> moduloEntry : moduloGrouped.entrySet()) {
            Modulo modulo = moduloEntry.getKey();
            Map<Menu, List<RoleOpcion>> menuGrouped = moduloEntry.getValue();

            // Crear un MenuResponse por módulo
            MenuResponse menuResponse = new MenuResponse();
            menuResponse.setId(modulo.getIdModulo());
            menuResponse.setNombre(modulo.getNombre());
            menuResponse.setOrden(modulo.getOrdenMenu());

            List<MenuResponse.MenuItem> menuItems = new ArrayList<>();

            for (Map.Entry<Menu, List<RoleOpcion>> menuEntry : menuGrouped.entrySet()) {
                Menu menu = menuEntry.getKey();
                List<RoleOpcion> opcionesRol = menuEntry.getValue();

                // Crear MenuItem para cada menú
                MenuResponse.MenuItem menuItem = new MenuResponse.MenuItem();
                menuItem.setId(menu.getIdMenu());
                menuItem.setNombre(menu.getNombre());
                menuItem.setOrden(menu.getOrdenMenu());

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

                // Ordenar opciones por ordenMenu
                opcionItems.sort(Comparator.comparingInt(MenuResponse.OpcionItem::getOrden));
                menuItem.setOpciones(opcionItems);
                menuItems.add(menuItem);
            }

            // Ordenar menús por orden
            menuItems.sort(Comparator.comparingInt(MenuResponse.MenuItem::getOrden));
            menuResponse.setMenuItems(menuItems);
            response.add(menuResponse);
        }

        // Ordenar módulos por orden
        response.sort(Comparator.comparingInt(MenuResponse::getOrden));

        return response;
    }

    public Menu guardar(Menu menu, String token) {
        menu.setFechaCreacion(LocalDateTime.now());
        menu.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(menu);
    }

    public List<Menu> listarTodos() {
        return repository.findAll();
    }

    public Optional<Menu> buscarPorId(Integer id) {
        return repository.findById(id);
    }

     public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public Optional<Menu> actualizar(Integer id, Menu menuActualizado, String token) {
        return repository.findById(id).map(menuExistente -> {
            menuExistente.setNombre(menuActualizado.getNombre());
            menuExistente.setOrdenMenu(menuActualizado.getOrdenMenu());
            menuExistente.setModulo(menuActualizado.getModulo());
            menuExistente.setFechaModificacion(LocalDateTime.now());
            menuExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(menuExistente);
        });
    }

    public List<Modulo> listarModulos() {
        return moduloRepository.findAll();
    }
}
