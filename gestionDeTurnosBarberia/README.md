# 💈 Gestor de Turnos - Barbería API

Esta es una API REST profesional desarrollada para la gestión eficiente de turnos en una barbería. El sistema permite administrar clientes, barberos y sus agendas diarias, asegurando que no existan conflictos de horarios y facilitando la consulta de turnos por profesional.

## 🚀 Tecnologías Utilizadas

* **Java 21**
* **Spring Boot 3.3.2**
* **Spring Data JPA** (Persistencia de datos)
* **MySQL** (Base de Datos)
* **Maven** (Gestión de dependencias)
* **Lombok** (Productividad)
* **Swagger/OpenAPI 3** (Documentación interactiva)



## 🛠️ Desafíos Técnicos Resueltos

Durante el desarrollo de este proyecto, se implementaron soluciones a problemas comunes en el desarrollo backend:

* **Lógica de Disponibilidad:** Implementación de validaciones para evitar que un barbero tenga dos turnos en el mismo horario o en el pasado.
* **Recursión Infinita (JSON):** Resolución de ciclos circulares en relaciones bidireccionales (`@ManyToOne` / `@OneToMany`) mediante el uso estratégico de `@JsonManagedReference` y `@JsonBackReference`.
* **Consultas de Agenda Dinámica:** Creación de filtros avanzados en el repositorio para obtener la agenda de un barbero específico dentro de un rango de tiempo (inicio y fin del día).
* **Transferencia de Datos (DTOs):** Uso de objetos de transferencia para desacoplar las entidades de la base de datos de los datos recibidos por la API.

## 📖 Documentación de la API

La API se encuentra totalmente documentada con **Swagger**. Una vez que el proyecto esté en ejecución, puedes acceder a la interfaz interactiva para probar los endpoints en:

`http://localhost:8080/swagger-ui/index.html`



## 📋 Funcionalidades Principales

1. **Gestión de Barberos y Clientes:** CRUD completo para la administración de personal y usuarios.
2. **Reserva de Turnos:** Creación de citas validando la existencia de los IDs y la disponibilidad horaria.
3. **Agenda por Barbero:** Endpoint especializado para listar los turnos de un profesional en una fecha determinada, ordenados cronológicamente.
4. **Validaciones Robustas:** Prevención de errores mediante el manejo de excepciones personalizadas.

---
Desarrollado por Elias Dolenz - En busca de mi primer desafío como Junior Backend Developer.