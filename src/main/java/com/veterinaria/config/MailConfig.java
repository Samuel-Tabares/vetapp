package com.veterinaria.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configuración de Email usando Resend
 * Solo para logging de configuración
 */
@Configuration
@Slf4j
public class MailConfig {

    @Value("${app.resend.api-key:}")
    private String resendApiKey;

    @Value("${app.resend.from:VetApp <onboarding@resend.dev>}")
    private String resendFrom;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @PostConstruct
    public void logMailConfig() {
        log.info("═══════════════════════════════════════════════════════════");
        log.info("           CONFIGURACIÓN DE EMAIL (RESEND)                 ");
        log.info("═══════════════════════════════════════════════════════════");
        log.info("Provider: Resend (HTTP API)");
        log.info("API Key: {}", resendApiKey != null && !resendApiKey.isEmpty() ? "****CONFIGURADO****" : "⚠️ NO CONFIGURADO");
        log.info("From: {}", resendFrom);
        log.info("Mail Enabled: {}", mailEnabled);
        log.info("═══════════════════════════════════════════════════════════");

        if (resendApiKey == null || resendApiKey.isEmpty()) {
            log.warn("⚠️ RESEND_API_KEY no está configurado - Los emails no se enviarán");
        }
    }
}