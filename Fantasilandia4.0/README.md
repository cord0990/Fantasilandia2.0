<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0,1a2744,2e4a7a&height=200&section=header&text=Fantasilandia%202.0&fontSize=52&fontColor=ffffff&fontAlignY=38&desc=Sistema%20de%20gestion%20de%20parque%20de%20atracciones&descAlignY=58&descSize=16" />

<br/>

![Java](https://img.shields.io/badge/Java-11-1a2744?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-6b7280?style=for-the-badge&logo=java&logoColor=white)
![Status](https://img.shields.io/badge/Estado-Completado-2e4a7a?style=flat-square&labelColor=1a1a1a&color=2e4a7a)
![Universidad](https://img.shields.io/badge/PUCV-INF2236-3d3d3d?style=flat-square)

</div>

<br/>

<img src="https://capsule-render.vercel.app/api?type=rect&color=2e4a7a&height=2&section=header" />

### ▎DESCRIPCIÓN

**Fantasilandia 2.0** es un sistema de información de escritorio basico para gestionar un parque de atracciones. Desarrollado en Java (JDK 11) con interfaz gráfica **Java Swing**, aplicando principios de **Programación Orientada a Objetos**.

Permite administrar clientes, atracciones y bloques de horario, con persistencia de datos y filtrado avanzado.

<br/>

<img src="https://capsule-render.vercel.app/api?type=rect&color=2e4a7a&height=2&section=header" />

### ▎FUNCIONALIDADES DEL PROYECTO

- **Gestión de clientes y atracciones** — registro, edición y eliminación con validaciones.
- **Administración de bloques de horario** — inscripción de clientes a atracciones por tramos horarios.
- **Filtrado y búsqueda** — búsqueda de bloques por criterios específicos a través de una o más colecciones.
- **Persistencia de datos** — carga automática al iniciar y guardado al cerrar mediante archivos CSV/TXT.
- **Reportes** — generación de archivos de texto con el estado actual de las colecciones.
- **Interfaz gráfica completa** — todas las operaciones disponibles a través de ventanas Swing.

<br/>

<img src="https://capsule-render.vercel.app/api?type=rect&color=2e4a7a&height=2&section=header" />

### ▎TECNOLOGÍAS USADAS

| Tecnología | Uso |
|---|---|
| ![](https://img.shields.io/badge/Java%2011-1a2744?style=flat-square&logo=openjdk&logoColor=white) | Lenguaje principal |
| ![](https://img.shields.io/badge/Java%20Swing-6b7280?style=flat-square) | Interfaz gráfica de escritorio |
| ![](https://img.shields.io/badge/Collections%20Framework-2e4a7a?style=flat-square) | Listas, Maps y colecciones anidadas |
| ![](https://img.shields.io/badge/Herencia%20%26%20Polimorfismo-1a2744?style=flat-square) | Jerarquía de clases del dominio |
| ![](https://img.shields.io/badge/Excepciones%20personalizadas-6b7280?style=flat-square) | Manejo de errores del negocio |
| ![](https://img.shields.io/badge/Persistencia%20CSV%2FTXT-2e4a7a?style=flat-square) | Carga y guardado batch de datos |

<br/>

<img src="https://capsule-render.vercel.app/api?type=rect&color=2e4a7a&height=2&section=header" />

### ▎CAPTURAS DEL PROYECTO

| Menú principal | Gestión de atracciones |
|---|---|
| ![Menu](src/screenshots/menu_principal.png) | ![Atracciones](src/screenshots/Gestion_de_atracciones.png) |

| Bloques de horario | Gestión de clientes |
|---|---|
| ![Bloques](src/screenshots/Bloques_de_horario.png) | ![Clientes](src/screenshots/Gestion_Clientes.png) |

| Reportes y filtros | |
|---|---|
| ![Reportes](src/screenshots/Reportes_Filtros.png) | |

<br/>

<img src="https://capsule-render.vercel.app/api?type=rect&color=2e4a7a&height=2&section=header" />

### ▎ESTRUCTURA

```
Fantasilandia4.0/
├── src/
│   ├── consola/         # Interacción por consola
│   ├── Excepciones/     # Clases de excepciones personalizadas
│   ├── fantasilandia/   # Clases del dominio (Cliente, Atraccion, BloqueDeAtraccion...)
│   ├── GUI/             # Ventanas Swing
│   ├── persistencia/    # Lógica de carga y guardado de datos
│   ├── resources/       # Recursos del proyecto
│   ├── screenshots/     # Capturas de pantalla
│   └── Main.java        # Punto de entrada
├── data/                # Archivos CSV/TXT de persistencia
├── bin/                 # Archivos compilados
└── README.md
```

<br/>

<img src="https://capsule-render.vercel.app/api?type=rect&color=2e4a7a&height=2&section=header" />

### ▎REQUISITOS

- **JDK 11** — [Descargar](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
- **IDE compatible**: IntelliJ IDEA, Eclipse o NetBeans

<br/>

<img src="https://capsule-render.vercel.app/api?type=rect&color=2e4a7a&height=2&section=header" />

### ▎EJECUCIÓN

**IntelliJ IDEA**
```
Build > Build Project  →  Run > Run 'Main'
```
**Eclipse**
```
Import > Existing Projects  →  Clic derecho Main.java > Run As > Java Application
```
**NetBeans**
```
Build Project (F11)  →  Run Project (F6)
```

<br/>

<img src="https://capsule-render.vercel.app/api?type=rect&color=2e4a7a&height=2&section=header" />

### ▎CONTEXTO ACADÉMICO 2025

Proyecto desarrollado para **INF2236 — Programación Avanzada** durante 2025 en la [Pontificia Universidad Católica de Valparaíso (PUCV)](https://www.pucv.cl), 4° semestre de Ingeniería en Informática.

Requisitos cubiertos: colecciones anidadas (JCF), sobrecarga y sobreescritura, excepciones personalizadas, persistencia batch, interfaz Swing, diagrama UML.

<br/>

<img src="https://capsule-render.vercel.app/api?type=rect&color=2e4a7a&height=2&section=header" />

### ▎AUTORES

<div align="center">

[![cord0990](https://img.shields.io/badge/@cord0990-6e1423?style=for-the-badge&logo=github&logoColor=white)](https://github.com/cord0990)
[![PatataSubnormal](https://img.shields.io/badge/@PatataSubnormal-e91e8c?style=for-the-badge&logo=github&logoColor=white)](https://github.com/PatataSubnormal)
[![cortadew](https://img.shields.io/badge/@cortadew-2e4a7a?style=for-the-badge&logo=github&logoColor=white)](https://github.com/cortadew)

</div>

<br/>

<img src="https://capsule-render.vercel.app/api?type=waving&color=0,1a2744,2e4a7a&height=100&section=footer" />