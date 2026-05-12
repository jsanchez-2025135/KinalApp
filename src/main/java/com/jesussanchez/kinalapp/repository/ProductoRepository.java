package com.jesussanchez.kinalapp.repository;

import com.jesussanchez.kinalapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository <Producto, Long> {
    List<Producto> findByEstado(int estado);
}
