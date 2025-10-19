package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.Menu;
import com.proyecto.analisis.entity.Opcion;
import com.proyecto.analisis.repository.MenuRepository;
import com.proyecto.analisis.repository.OpcionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OpcionService {

    private final OpcionRepository repository;
    private final MenuRepository menuRepository;
    private final AuthService authService;

    public OpcionService(OpcionRepository repository, MenuRepository menuRepository, AuthService authService) {
        this.repository = repository;
        this.menuRepository = menuRepository;
        this.authService = authService;
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }


    public Opcion guardar(Opcion opcion, String token) {
        opcion.setFechaCreacion(LocalDateTime.now());
        opcion.setUsuarioCreacion(authService.getCurrentUserId(token));
        return repository.save(opcion);
    }

    public List<Opcion> listarTodos() {
        return repository.findAll();
    }

    public Optional<Opcion> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    public Optional<Opcion> actualizar(Integer id, Opcion opcionActualizada, String token) {
        return repository.findById(id).map(opcionExistente -> {
            opcionExistente.setNombre(opcionActualizada.getNombre());
            opcionExistente.setMenu(opcionActualizada.getMenu());
            opcionExistente.setOrdenMenu(opcionActualizada.getOrdenMenu());
            opcionExistente.setPagina(opcionActualizada.getPagina());
            opcionExistente.setFechaModificacion(LocalDateTime.now());
            opcionExistente.setUsuarioModificacion(authService.getCurrentUserId(token));
            return repository.save(opcionExistente);
        });
    }
}