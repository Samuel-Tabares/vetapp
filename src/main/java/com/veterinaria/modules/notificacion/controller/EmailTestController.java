package com.veterinaria.modules.notificacion.controller;

import com.veterinaria.modules.notificacion.dto.NotificacionDTO;
import com.veterinaria.modules.notificacion.service.NotificacionService;
import com.veterinaria.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para probar el envío de emails con Resend
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class EmailTestController {

    private final NotificacionService notificacionService;

    @Value("${app.resend.api-key:}")
    private String resendApiKey;

    @Value("${app.resend.from:VetApp <onboarding@resend.dev>}")
    private String resendFrom;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    /**
     * Verifica la configuración de email
     */
    @GetMapping("/email-config")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEmailConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("provider", "Resend");
        config.put("apiKeyConfigured", resendApiKey != null && !resendApiKey.isEmpty() && resendApiKey.startsWith("re_"));
        config.put("from", resendFrom);
        config.put("enabled", mailEnabled);

        log.info("Email config: {}", config);

        return ResponseEntity.ok(ApiResponse.success(config, "Configuración de email"));
    }

    /**
     * Envía un email de prueba usando Resend directamente
     * USO: POST /api/test/send-email?to=tu@email.com
     */
    @PostMapping("/send-email")
    public ResponseEntity<ApiResponse<String>> sendTestEmail(@RequestParam String to) {
        log.info("═══════════════════════════════════════════════════════════");
        log.info("           PRUEBA DE ENVÍO DE EMAIL CON RESEND             ");
        log.info("═══════════════════════════════════════════════════════════");
        log.info("Destinatario: {}", to);
        log.info("From: {}", resendFrom);
        log.info("API Key configurada: {}", resendApiKey != null && !resendApiKey.isEmpty());

        if (!mailEnabled) {
            return ResponseEntity.ok(ApiResponse.error("Email está deshabilitado (MAIL_ENABLED=false)"));
        }

        if (resendApiKey == null || resendApiKey.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error("RESEND_API_KEY no está configurado en Railway"));
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + resendApiKey);

            // Body
            Map<String, Object> body = new HashMap<>();
            body.put("from", resendFrom);
            body.put("to", new String[]{to});
            body.put("subject", "🧪 Test Email - VetApp");
            body.put("text", "¡Hola!\n\nEste es un email de prueba desde VetApp usando Resend.\n\nSi recibes este mensaje, la configuración está funcionando correctamente.\n\nSaludos,\nVetApp");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            log.info("Enviando email via Resend API...");
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.resend.com/emails",
                    request,
                    String.class
            );

            log.info("Respuesta Resend: {} - {}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(ApiResponse.success(
                        "Email enviado a " + to,
                        "✅ Email enviado exitosamente via Resend"
                ));
            } else {
                return ResponseEntity.ok(ApiResponse.error(
                        "Error de Resend: " + response.getBody()
                ));
            }

        } catch (Exception e) {
            log.error("❌ Error enviando email: {}", e.getMessage());
            log.error("Stack trace:", e);

            return ResponseEntity.ok(ApiResponse.error(
                    "Error: " + e.getMessage()
            ));
        }
    }

    /**
     * Prueba el servicio de notificación completo
     */
    @PostMapping("/send-notification")
    public ResponseEntity<ApiResponse<String>> sendTestNotification(@RequestParam String to) {
        log.info("Probando servicio de notificación...");

        try {
            NotificacionDTO notificacion = NotificacionDTO.builder()
                    .destinatario(to)
                    .asunto("🧪 Test Notificación - VetApp")
                    .mensaje("Este es un test del sistema de notificaciones.\n\nSi recibes este mensaje, todo está funcionando.\n\nSaludos,\nVetApp")
                    .tipoNotificacion("EMAIL")
                    .build();

            notificacionService.enviarNotificacion(notificacion);

            return ResponseEntity.ok(ApiResponse.success(
                    "Notificación enviada a " + to,
                    "Revisa tu bandeja de entrada"
            ));

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("Error: " + e.getMessage()));
        }
    }
}