# 🐾 VetApp - Sistema de Gestión Veterinaria

Sistema completo de gestión veterinaria con **arquitectura modular** y **patrones de diseño**.

## 🏗️ Arquitectura y Patrones Implementados

### Patrones de Diseño
- **Repository Pattern**: Acceso a datos desacoplado
- **Service Layer Pattern**: Lógica de negocio centralizada
- **Facade Pattern**: Simplifica operaciones complejas (módulo Citas)
- **Factory Pattern**: Creación de notificaciones (módulo Notificación)
- **Strategy Pattern**: Diferentes tipos de cálculo de facturación
- **DTO Pattern**: Transferencia de datos segura

### Arquitectura Modular
```
vetapp/  
├── src/main/java/com/veterinaria/
│    ├── modules/
│    │   ├── propietario/      # Gestión de propietarios
│    │   ├── mascota/          # Gestión de mascotas
│    │   ├── cita/             # Agendamiento con notificaciones
│    │   ├── historia/         # Historias clínicas
│    │   ├── prescripcion/     # Prescripciones médicas
│    │   ├── facturacion/      # Facturación con estrategias
│    │   └── notificacion/     # Sistema de notificaciones
│    ├── shared/               # Componentes compartidos
│    └── config/               # Configuraciones
├──.gitignore
├──application.yml
├──docker-compose.yml
├──dockerfile
├──pom.xml
├──README.md
```

## 🚀 Stack Tecnológico

- **Backend**: Java 17 + Spring Boot 3.2.0
- **Base de Datos**: MySQL 8.0
- **Frontend**: HTML5 + CSS3 + JavaScript vanilla
- **Containerización**: Docker + Docker Compose
- **Email**: JavaMail con SMTP Gmail
- **Despliegue**: Railway

## 📋 Funcionalidades

1. ✅ **Registrar propietarios**
2. ✅ **Asignar mascotas a propietarios**
3. ✅ **Crear citas con notificación automática por email**
4. ✅ **Gestionar historias clínicas**
5. ✅ **Modificar historias clínicas**
6. ✅ **Prescribir medicamentos**
7. ✅ **Generar facturas completas**
8. ✅ **Notificaciones automáticas por Gmail**

## 🛠️ Instalación y Ejecución

### Prerrequisitos
- Docker y Docker Compose instalados
- Git

### Paso 1: Clonar el repositorio
```bash
git clone https://github.com/Samuel-Tabares/vetapp
cd vetapp
```

### Paso 2: Configurar variables de entorno
```bash
cp .env
```

### Paso 3: Levantar con Docker
```bash
docker-compose up --build
```

Esto levantará:
- **MySQL** en puerto 3306
- **Backend API** en puerto 8080
- Creará automáticamente la base de datos

### Paso 4: Acceder al sistema
- **API**: http://localhost:8080/api
- **Frontend**: Abrir `vetapp/frontend/index.html` en el navegador

## 📡 Endpoints de la API

### Propietarios
- `POST /api/propietarios` - Crear propietario
- `GET /api/propietarios` - Listar todos
- `GET /api/propietarios/{id}` - Obtener por ID
- `PUT /api/propietarios/{id}` - Actualizar
- `DELETE /api/propietarios/{id}` - Eliminar

### Mascotas
- `POST /api/mascotas` - Registrar mascota
- `GET /api/mascotas` - Listar todas
- `GET /api/mascotas/{id}` - Obtener por ID
- `GET /api/mascotas/propietario/{propietarioId}` - Por propietario
- `PUT /api/mascotas/{id}` - Actualizar
- `DELETE /api/mascotas/{id}` - Eliminar

### Citas (con Facade Pattern)
- `POST /api/citas` - Crear cita + notificación automática
- `GET /api/citas` - Listar todas
- `GET /api/citas/{id}` - Obtener por ID
- `GET /api/citas/mascota/{mascotaId}` - Por mascota
- `PUT /api/citas/{id}` - Actualizar + notificar cambios
- `DELETE /api/citas/{id}` - Eliminar

### Historias Clínicas
- `POST /api/historias` - Crear historia
- `GET /api/historias` - Listar todas
- `GET /api/historias/{id}` - Obtener por ID
- `GET /api/historias/mascota/{mascotaId}` - Por mascota
- `PUT /api/historias/{id}` - Actualizar
- `DELETE /api/historias/{id}` - Eliminar

### Prescripciones
- `POST /api/prescripciones` - Crear prescripción
- `GET /api/prescripciones` - Listar todas
- `GET /api/prescripciones/{id}` - Obtener por ID
- `GET /api/prescripciones/mascota/{mascotaId}` - Por mascota
- `PUT /api/prescripciones/{id}` - Actualizar
- `DELETE /api/prescripciones/{id}` - Eliminar

### Facturas (con Strategy Pattern)
- `POST /api/facturas` - Crear factura
- `GET /api/facturas` - Listar todas
- `GET /api/facturas/{id}` - Obtener por ID
- `DELETE /api/facturas/{id}` - Eliminar

## 🐳 Comandos Docker Útiles

```bash
# Levantar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Detener servicios
docker-compose down

# Reconstruir después de cambios
docker-compose up --build

# Acceder a MySQL
docker exec -it vetapp-mysql mysql -uroot -proot vetapp
```

## 🚢 Despliegue en Railway

1. Crear cuenta en [Railway.app](https://railway.app)
2. Conectar repositorio de GitHub
3. Railway detectará automáticamente Docker
4. Agregar servicio MySQL desde marketplace
5. Configurar variables de entorno en Railway
6. Deploy automático

## 📁 Estructura de Archivos

```
vetapp/
├── src/
│   └── main/
│       ├── java/com/veterinaria/
│       │   ├── modules/
│       │   ├── shared/
│       │   ├── config/
│       │   └── VetAppApplication.java
│       └── resources/
│           └── application.yml
├── frontend/
│   ├── index.html
│   ├── styles.css
│   └── app.js
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── .env
└── README.md
```

## 🎯 Características Técnicas

- ✅ Arquitectura limpia y modular
- ✅ Separación de responsabilidades
- ✅ Patrones de diseño aplicados
- ✅ Validación de datos con Bean Validation
- ✅ Manejo global de excepciones
- ✅ DTOs para seguridad
- ✅ Transacciones con @Transactional
- ✅ Logging con SLF4J
- ✅ CORS habilitado para desarrollo
- ✅ Dockerización completa
- ✅ Notificaciones asíncronas

## 📝 Notas Importantes

1. **Gmail**: Requiere "Contraseña de aplicación", no tu contraseña normal
2. **MySQL**: Los datos persisten en volumen Docker
3. **Puertos**: 8080 (API) y 3306 (MySQL) deben estar libres
4. **Frontend**: Es básico, enfocado en probar funcionalidad

**Desarrollado con arquitectura modular y patrones de diseño** 🚀