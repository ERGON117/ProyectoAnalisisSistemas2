package com.proyecto.analisis.repository;

import com.proyecto.analisis.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
// List<Menu> findByModuloIdModuloOrderByOrdenMenu(Integer idModulo);
}