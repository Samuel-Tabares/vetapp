package com.veterinaria.modules.mascota.entity;

import com.veterinaria.modules.propietario.entity.Propietario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Mascota - Paciente veterinario
 */
@Entity
@Table(name = "mascotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String especie; // Perro, Gato, Ave, etc.

    @Column(length = 50)
    private String raza;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(length = 10)
    private String sexo; // Macho, Hembra

    @Column(length = 20)
    private String color;

    @Column  // Sin precision ni scale
    private Double peso;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    // Relación muchos a uno con Propietario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Propietario propietario;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}