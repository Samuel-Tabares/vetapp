package com.veterinaria.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import jakarta.annotation.PostConstruct;
import java.util.Properties;

/**
 * Configuración explícita de JavaMailSender
 * Usa puerto 465 con SSL para Railway (587 está bloqueado)
 */
@Configuration
@Slf4j
public class MailConfig {

    @Value("${spring.mail.host:smtp.gmail.com}")
    private String host;

    @Value("${spring.mail.port:465}")
    private int port;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.password:}")
    private String password;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @PostConstruct
    public void logMailConfig() {
        log.info("═══════════════════════════════════════════════════════════");
        log.info("           CONFIGURACIÓN DE EMAIL CARGADA                  ");
        log.info("═══════════════════════════════════════════════════════════");
        log.info("Host: {}", host);
        log.info("Port: {} (SSL)", port);
        log.info("Username: {}", username);
        log.info("Password: {}", password != null && !password.isEmpty() ? "****CONFIGURADO****" : "⚠️ NO CONFIGURADO");
        log.info("Mail Enabled: {}", mailEnabled);
        log.info("═══════════════════════════════════════════════════════════");
    }

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        log.info("Creando JavaMailSender con SSL (puerto 465)...");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(465);  // Puerto SSL
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.connectiontimeout", "15000");
        props.put("mail.smtp.timeout", "15000");
        props.put("mail.smtp.writetimeout", "15000");
        props.put("mail.debug", "true");

        log.info("JavaMailSender configurado con SSL en puerto 465");
        return mailSender;
    }
}