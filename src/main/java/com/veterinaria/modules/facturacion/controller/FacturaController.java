package com.veterinaria.modules.facturacion.controller;

import com.veterinaria.modules.facturacion.dto.FacturaDTO;
import com.veterinaria.modules.facturacion.facade.FacturaFacade;
import com.veterinaria.modules.facturacion.service.FacturaService;
import com.veterinaria.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Facturas
 * Endpoints: /api/facturas
 * Usa FacturaFacade para operaciones con notificación
 */
@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FacturaController {

    private final FacturaFacade facturaFacade;
    private final FacturaService service;

    @PostMapping
    public ResponseEntity<ApiResponse<FacturaDTO>> crear(@Valid @RequestBody FacturaDTO dto) {
        // Usa el Facade para crear factura + enviar notificación
        FacturaDTO creada = facturaFacade.crearFacturaConNotificacion(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(creada, "Factura creada y enviada por email exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FacturaDTO>>> listar() {
        List<FacturaDTO> facturas = service.listarTodas();
        return ResponseEntity.ok(ApiResponse.success(facturas, "Facturas obtenidas"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacturaDTO>> obtener(@PathVariable Long id) {
        FacturaDTO factura = service.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(factura, "Factura encontrada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Factura eliminada"));
    }
}