# Learnsync

## Descripción

Learnsync es un foro para estudiantes de medicina, diseñado para que los chicos recién ingresados a la universidad puedan comunicarse con los chicos próximos a egresar. Este proyecto proporciona una plataforma para compartir experiencias, hacer preguntas y obtener consejos de compañeros más experimentados.

## API del Proyecto

Este repositorio contiene el código del API para el proyecto Learnsync. Proporciona los endpoints necesarios para la gestión de usuarios, publicaciones y comentarios dentro del foro.

## Tecnologías Utilizadas

- **Java**: Un lenguaje de programación robusto y de alto rendimiento utilizado para el desarrollo del backend del proyecto.
- **Spring Boot**: Un framework de Java que facilita la creación de aplicaciones robustas y escalables, gestionando la configuración y las dependencias de manera eficiente.
- **Spring Security**: Un framework de seguridad que proporciona autenticación y autorización integradas en las aplicaciones Spring.
- **Thymeleaf**: Un motor de plantillas de Java para representar vistas web de manera dinámica y efectiva.
- **Docker**: Una plataforma que permite la creación, despliegue y ejecución de aplicaciones en contenedores, asegurando que el entorno de desarrollo y producción sean consistentes.
- **Gradle**: Una herramienta de automatización de compilaciones que gestiona las dependencias y facilita la construcción del proyecto de manera eficiente.
- **JWT (JSON Web Tokens)**: Un estándar abierto que permite la transmisión segura de información entre partes como un objeto JSON, utilizado para la autenticación y autorización en el proyecto.
- **PostgreSQL**: Un sistema de gestión de bases de datos relacional, conocido por su estabilidad y rendimiento, utilizado para almacenar y gestionar los datos del foro.
- **JPA (Java Persistence API)**: Una API de Java que facilita el mapeo y la gestión de datos relacionales en Java, permitiendo operaciones CRUD y transacciones de manera eficiente.
- **JUnit**: Un framework para realizar pruebas unitarias en Java, asegurando que el código funcione como se espera.
- **Mockito**: Un framework para crear objetos simulados y realizar pruebas unitarias más flexibles y completas.
- **MockMvc**: Una herramienta de Spring que permite realizar pruebas unitarias de controladores MVC de manera sencilla y efectiva.


## Instalación

Sigue estos pasos para configurar e instalar el proyecto:

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/learnsync.git
   cd learnsync
   ```
2. Configura Docker: Asegúrate de tener Docker instalado y en funcionamiento en tu máquina. Construye y ejecuta los contenedores de Docker:
   ```bash
   docker-compose up --build
   ```
3. Construye el proyecto con Gradle:
   ```bash
   ./gradlew build
   ```
3. Ejecuta la aplicación:
   ```bash
   ./gradlew bootRun
   ```

> [!WARNING]
> Tener en cuenta que si tienes alguna versión de Java menor a la recomendada, la aplicación dará fallos

## Ejecución de los Tests

Para ejecutar los test unitarios y de integración debes abrir tu terminal y ejecutar le siguiente comando:
   ```bash
   ./gradlew test
   ```
> [!IMPORTANT]
> Debes de tener en cuenta que es posible que algunos tests fallen al usar este comando debido a que se ejecutan en orden aleatorio. 

## Sugerencias

Si tienes sugerencias, no dudes en dejar una issue en el repositorio. Nos encantaría recibir tu feedback para mejorar el proyecto.
