package com.veterinaria.modules.notificacion.strategy;

import com.veterinaria.modules.notificacion.dto.NotificacionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementación de envío de email usando Resend API
 * Resend usa HTTP, no SMTP, así que funciona en Railway
 */
@Component
@Slf4j
public class ResendEmailSender implements NotificacionSender {

    @Value("${app.resend.api-key:}")
    private String apiKey;

    @Value("${app.resend.from:VetApp <onboarding@resend.dev>}")
    private String fromEmail;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String RESEND_API_URL = "https://api.resend.com/emails";

    @Override
    public void enviar(NotificacionDTO notificacion) {
        log.info("=== INICIO ENVÍO EMAIL CON RESEND ===");
        log.info("Mail enabled: {}", mailEnabled);
        log.info("Destinatario: {}", notificacion.getDestinatario());
        log.info("Asunto: {}", notificacion.getAsunto());

        if (!mailEnabled) {
            log.warn("⚠️ Email deshabilitado - No se envió: {}", notificacion.getAsunto());
            return;
        }

        if (apiKey == null || apiKey.isEmpty()) {
            log.error("❌ RESEND_API_KEY no está configurado!");
            return;
        }

        try {
            // Crear headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // Crear body
            Map<String, Object> body = new HashMap<>();
            body.put("from", fromEmail);
            body.put("to", new String[]{notificacion.getDestinatario()});
            body.put("subject", notificacion.getAsunto());
            body.put("text", notificacion.getMensaje());

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            log.info("📧 Enviando email via Resend API...");
            ResponseEntity<String> response = restTemplate.postForEntity(RESEND_API_URL, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("✅ Email enviado exitosamente via Resend a: {}", notificacion.getDestinatario());
                log.info("Respuesta: {}", response.getBody());
            } else {
                log.error("❌ Error de Resend: {} - {}", response.getStatusCode(), response.getBody());
            }

        } catch (Exception e) {
            log.error("❌ Error al enviar email con Resend: {}", e.getMessage());
            log.error("Stack trace:", e);
        }

        log.info("=== FIN ENVÍO EMAIL CON RESEND ===");
    }

    @Override
    public String getTipo() {
        return "EMAIL";
    }
}