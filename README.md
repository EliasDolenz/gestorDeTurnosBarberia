# 💈 Sistema de Gestión de Barbería - Fullstack

Este proyecto es una aplicación integral para la gestión de turnos de una barbería, desarrollada con una arquitectura desacoplada: un **Backend Robusto** en Java y un **Frontend Dinámico** en React.

## 🏗️ Estructura del Proyecto

El repositorio se divide en dos módulos principales:

* **/gestionDeTurnosBarberia (Backend):** API REST desarrollada con Spring Boot, JPA y MySQL. Maneja la lógica de negocio, validaciones de turnos y persistencia.
* **/gestion-turnos-frontend (Frontend):** Interfaz de usuario moderna construida con React y Vite, que consume la API de manera asincrónica.

## 🛠️ Tecnologías Principales

**Backend:** Java 21, Spring Boot, MySQL, Hibernate, Swagger/OpenAPI.
**Frontend:** React, Vite, CSS moderno, Fetch API.

## 🚀 Cómo ejecutar el proyecto

### 1. Requisitos previos
* JDK 21 instalado.
* Node.js y npm instalados.
* MySQL Server corriendo.

### 2. Configuración del Backend
1. Entrar a la carpeta `gestionDeTurnosBarberia`.
2. Configurar el archivo `application.properties` con tus credenciales de MySQL.
3. Ejecutar `./mvnw spring-boot:run`. La API estará disponible en `http://localhost:8080`.

### 3. Configuración del Frontend
1. Entrar a la carpeta `gestion-turnos-frontend`.
2. Ejecutar `npm install` para descargar las dependencias.
3. Ejecutar `npm run dev`. La web estará disponible en `http://localhost:5173`.

---
Desarrollado por Elias - [Tu LinkedIn si querés]