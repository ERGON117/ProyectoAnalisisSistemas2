package com.proyecto.analisis.service;

import com.proyecto.analisis.entity.BitacoraAcceso;
import com.proyecto.analisis.repository.BitacoraAccesoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BitacoraAccesoService {

    private final BitacoraAccesoRepository repository;

    public BitacoraAccesoService(BitacoraAccesoRepository repository) {
        this.repository = repository;
    }

    public List<BitacoraAcceso> getUltimosAccesos(String idUsuario) {
        return repository.findTop10ByIdUsuarioOrderByFechaAccesoDesc(idUsuario);
    }

    public long contarIntentosRecientes(String idUsuario, LocalDateTime desde) {
        return repository.countByIdUsuarioAndFechaAccesoAfter(idUsuario, desde);
    }

    public List<BitacoraAcceso> getAccesosPorRango(String idUsuario, LocalDateTime desde, LocalDateTime hasta) {
        return repository.findByIdUsuarioAndFechaAccesoBetween(idUsuario, desde, hasta);
    }

    public List<BitacoraAcceso> filtrarPorTipoAcceso(String nombreTipo, LocalDateTime desde, LocalDateTime hasta) {
        return repository.findByTipoAcceso_NombreAndFechaAccesoBetween(nombreTipo, desde, hasta);
    }

    public BitacoraAcceso guardar(BitacoraAcceso bitacora) {
        return repository.save(bitacora);
    }
}
