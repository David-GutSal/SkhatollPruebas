<div align="center">

# 🎓 TFG – Skhatoll

Aplicación web desarrollada como **Trabajo de Fin de Grado**, basada en una arquitectura
cliente-servidor con frontend SPA y backend desacoplado mediante API REST y WebSocket.

</div>

---

## 🧠 Descripción del proyecto

Skhatoll es una aplicación web desarrollada como proyecto académico, cuyo objetivo es
aplicar de forma práctica los conocimientos adquiridos durante el ciclo formativo de
**Desarrollo de Aplicaciones Web (DAW)**.

El proyecto sigue una **arquitectura MVC desacoplada**, separando claramente el frontend
y el backend, facilitando la escalabilidad, el mantenimiento y el trabajo en equipo.

---

## 🛠️ Tecnologías

<p align="center">
  <img src="https://skillicons.dev/icons?i=html,css,js,vue,bootstrap,java,spring,hibernate,mysql,docker" />  
</p>

### Backend
- Java
- Spring Boot
- Hibernate / JPA
- Spring WebSocket
- API REST
- MySQL

### Frontend
- HTML, CSS, JavaScript
- Vue.js
- Vue Router
- Vuex
- Bootstrap

### DevOps / Herramientas
- Docker & Docker Compose
- Git & GitHub

---

## 📁 Estructura del repositorio

```text
Skhatoll/
├── backend/        # API REST y WebSocket (Spring Boot)
├── frontend/       # Aplicación SPA (Vue.js)
├── database/       # Scripts y configuración de base de datos
├── docker/         # Configuración Docker
├── docs/           # Documentación técnica
├── docker-compose.yml
└── README.md
```
## 🔐 Reglas del repositorio

### 🌿 Reglas de uso de ramas

#### Rama `main`
- Contiene únicamente versiones **estables y funcionales** del proyecto.
- No se permite realizar desarrollo directo sobre esta rama.
- Solo se actualiza mediante Pull Requests desde `develop`.

#### Rama `develop`
- Rama principal de **desarrollo continuo**.
- Recibe los cambios desde ramas `feature/*`.
- Todo cambio debe integrarse mediante Pull Requests.

#### Ramas `feature/*`
- Utilizadas para el desarrollo de nuevas funcionalidades.
- Cada funcionalidad debe desarrollarse en su propia rama.
- Una vez completada, se integran en `develop` mediante Pull Request.

### 🚫 Restricciones de push

- No se permite hacer `push` directo a la rama `main`.
- No se permite desarrollar directamente sobre la rama `develop`.
- Todo desarrollo debe realizarse en ramas `feature/*`.

### 📝 Convención de commits

Los mensajes de commit deben seguir el siguiente formato:
```
<tipo>: <descripción breve>
```
Tipos permitidos:
- feat: nueva funcionalidad
- fix: corrección de errores
- refactor: cambios internos sin modificar funcionalidad
- docs: cambios en documentación

Ejemplos:
- feat: añade sistema de autenticación
- fix: corrige validación de usuario
- refactor: reorganiza servicios de negocio

### 🔍 Revisión de código

Todos los cambios deben integrarse mediante Pull Requests, los cuales son revisados
antes de ser aceptados para garantizar la calidad y estabilidad del proyecto.
