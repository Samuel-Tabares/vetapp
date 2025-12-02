package com.veterinaria.modules.historia.entity;

import com.veterinaria.modules.mascota.entity.Mascota;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad HistoriaClinica - Registro médico de mascotas
 */
@Entity
@Table(name = "historias_clinicas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_consulta", nullable = false)
    private LocalDateTime fechaConsulta;

    @Column(nullable = false, length = 200)
    private String diagnostico;

    @Column(length = 1000)
    private String sintomas;

    @Column(length = 1000)
    private String tratamiento;

    @Column(length = 500)
    private String observaciones;

    @Column  // Sin precision ni scale
    private Double pesoRegistrado;

    @Column  // Sin precision ni scale
    private Double temperatura;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    // Relación con Mascota
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
}