package com.veterinaria.modules.facturacion.facade;

import com.veterinaria.modules.facturacion.dto.DetalleFacturaDTO;
import com.veterinaria.modules.facturacion.dto.FacturaDTO;
import com.veterinaria.modules.facturacion.service.FacturaService;
import com.veterinaria.modules.notificacion.dto.NotificacionDTO;
import com.veterinaria.modules.notificacion.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Facade para simplificar operaciones complejas de Factura
 * Patrón Facade: coordina FacturaService + NotificacionService
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FacturaFacade {

    private final FacturaService facturaService;
    private final NotificacionService notificacionService;

    /**
     * Crea una factura y envía notificación al propietario automáticamente
     */
    public FacturaDTO crearFacturaConNotificacion(FacturaDTO facturaDTO) {
        // 1. Crear la factura
        FacturaDTO facturaCreada = facturaService.crear(facturaDTO);
        log.info("Factura creada: {}", facturaCreada.getNumeroFactura());

        // 2. Preparar y enviar notificación
        NotificacionDTO notificacion = crearNotificacionFactura(facturaCreada);
        notificacionService.enviarNotificacion(notificacion);
        log.info("Notificación de factura enviada a: {}", facturaCreada.getPropietarioEmail());

        return facturaCreada;
    }

    /**
     * Crea el contenido del email con formato de factura
     */
    private NotificacionDTO crearNotificacionFactura(FacturaDTO factura) {
        StringBuilder detallesHtml = new StringBuilder();

        // Construir tabla de detalles
        for (DetalleFacturaDTO detalle : factura.getDetalles()) {
            detallesHtml.append(String.format(
                    "  • %s (x%d) - $%,.2f c/u = $%,.2f%n",
                    detalle.getDescripcion(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario(),
                    detalle.getSubtotal()
            ));
        }

        String mensaje = String.format(
                "══════════════════════════════════════════════════════════%n" +
                        "                    FACTURA VETERINARIA                    %n" +
                        "══════════════════════════════════════════════════════════%n%n" +
                        "Número de Factura: %s%n" +
                        "Fecha de Emisión: %s%n%n" +
                        "──────────────────────────────────────────────────────────%n" +
                        "DATOS DEL CLIENTE%n" +
                        "──────────────────────────────────────────────────────────%n" +
                        "Propietario: %s%n" +
                        "Mascota: %s%n%n" +
                        "──────────────────────────────────────────────────────────%n" +
                        "DETALLE DE SERVICIOS%n" +
                        "──────────────────────────────────────────────────────────%n" +
                        "%s%n" +
                        "──────────────────────────────────────────────────────────%n" +
                        "                              Subtotal:    $%,.2f%n" +
                        "                              IVA (%s%%):   $%,.2f%n" +
                        "                              ─────────────────────%n" +
                        "                              TOTAL:       $%,.2f%n" +
                        "══════════════════════════════════════════════════════════%n%n" +
                        "Estado: %s%n%n" +
                        "%s%n%n" +
                        "Gracias por confiar en nosotros para el cuidado de %s.%n%n" +
                        "Atentamente,%n" +
                        "VetApp - Sistema de Gestión Veterinaria%n" +
                        "══════════════════════════════════════════════════════════%n",
                factura.getNumeroFactura(),
                factura.getFechaEmision(),
                factura.getPropietarioNombre(),
                factura.getMascotaNombre(),
                detallesHtml.toString(),
                factura.getSubtotal(),
                factura.getImpuesto(),
                factura.getSubtotal().multiply(factura.getImpuesto()).divide(new java.math.BigDecimal("100")),
                factura.getTotal(),
                factura.getEstado(),
                factura.getObservaciones() != null ? "Observaciones: " + factura.getObservaciones() : "",
                factura.getMascotaNombre()
        );

        return NotificacionDTO.builder()
                .destinatario(factura.getPropietarioEmail())
                .asunto("Factura " + factura.getNumeroFactura() + " - VetApp Veterinaria")
                .mensaje(mensaje)
                .tipoNotificacion("EMAIL")
                .build();
    }
}