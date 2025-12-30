# 🏛️ Sistema de Gestión de Expedientes - Consejo Departamental

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![EclipseLink](https://img.shields.io/badge/EclipseLink-JPA-blue?style=for-the-badge)

## 📖 Descripción del Proyecto

Este sistema de escritorio fue desarrollado como proyecto final para la asignatura **Programación Orientada a Objetos 1**. El objetivo principal es digitalizar y optimizar el flujo de trabajo del Consejo Departamental de Informática.

La aplicación permite al Director y a los administrativos gestionar el ciclo de vida completo de los expedientes, desde su ingreso hasta su tratamiento en reuniones, generando actas (minutas) y registrando las acciones tomadas.

## 🚀 Funcionalidades Principales

El sistema resuelve la problemática de gestión mediante los siguientes módulos:

* **🗂️ Gestión de Expedientes:** Registro de notas, iniciantes, fechas de ingreso y seguimiento de involucrados.
* **📅 Organización de Reuniones:** Creación de convocatorias y generación automática del "Orden del Día" basado en expedientes abiertos.
* **📝 Minutas y Actas:** Generación de minutas detalladas por cada tema tratado en la reunión.
* **✅ Control de Asistencia:** Registro de los miembros del consejo presentes en cada sesión.
* **🔄 Seguimiento de Acciones:** Historial de decisiones y acciones realizadas sobre cada expediente con sus respectivas fechas.
* **👥 ABM Completo:** Gestión de Altas, Bajas y Modificaciones para Personal, Reuniones y Expedientes.

## 🛠️ Stack Tecnológico

El proyecto fue construido siguiendo una arquitectura **MVC (Modelo-Vista-Controlador)** para garantizar la escalabilidad y mantenibilidad del código.

* **Lenguaje:** Java (JDK 17+ recomendado).
* **Interfaz Gráfica (GUI):** Java Swing (Diseñado con WindowBuilder/NetBeans GUI Editor).
* **Base de Datos:** PostgreSQL.
* **Persistencia (ORM):** JPA (Java Persistence API) utilizando **EclipseLink** como proveedor.
* **Driver:** JDBC para la conectividad base.

## ⚙️ Estructura de la Base de Datos

El sistema persiste la información utilizando un modelo relacional robusto en PostgreSQL, manejando relaciones complejas como:
* *Muchos a Muchos* (Expedientes <-> Involucrados).
* *Uno a Muchos* (Reunión -> Temas/Minutas).

## 📦 Instalación y Ejecución

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/Luketi02/Proyecto-POO1-ASC-PUC-2025.git](https://github.com/Luketi02/Proyecto-POO1-ASC-PUC-2025.git)
    ```
2.  **Base de Datos:**
    * Asegúrate de tener PostgreSQL instalado y corriendo.
    * Crea una base de datos llamada `gestion_consejo` (o ajusta el nombre en el archivo `persistence.xml`).
3.  **Configuración:**
    * Verifica las credenciales de base de datos en `src/META-INF/persistence.xml`.
4.  **Ejecutar:**
    * Importa el proyecto en tu IDE favorito (Eclipse, IntelliJ, NetBeans).
    * Ejecuta la clase principal `Main.java` (o la clase que inicie la GUI).

## 👤 Autores

**Juan Lucas Miño**
* Profesor Universitario en Computación
* Estudiante de Licenciatura en Sistemas
* [LinkedIn](/lucas-juan-mi)

**Nazadyk Fernando Emanuel**
* Estudiante de Analiste en Sistemas de Computación
* Estudiante de Licenciatura en Sistemas de Información
* [LinkedIn](/fernando-nasadyk)

---
*Este proyecto fue realizado con fines académicos demostrando el uso de patrones de diseño y persistencia de datos en Java.*
