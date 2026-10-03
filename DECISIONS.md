# DECISIONS.md

## Contexto

Este proyecto se ha diseñado como una línea base para el sistema integral de gestión de radiodiagnóstico e imagen médica del Servicio de Salud de Castilla-La Mancha (SESCAM). La intención es cumplir la orientación del enunciado de la práctica: desarrollo dirigido por casos de uso, diseño centrado en la arquitectura y gestión incremental del producto.

## Decisiones principales

### 1. Estructura de dominio

Se ha elegido un modelo de dominio sencillo y cohesivo basado en los actores del sistema y los conceptos clave del servicio:

- Paciente
- Médico
- Personal administrativo
- Administrador del sistema
- Tipo de prueba
- Solicitud de prueba
- Cita
- Informe médico

Esto permite mantener el modelo comprensible, compatible con pruebas unitarias y fácilmente ampliable a una arquitectura multicapa.

### 2. Arquitectura sugerida

Se propone una arquitectura mínima basada en capas:

- Capa de dominio: entidades y tipos de valor
- Capa de servicio: lógica de negocio y coordinación
- Capa de infraestructura: persistencia, integración externa y adaptadores (futuro)
- Capa de presentación: UI web o cliente (futuro)

### 3. Gestión de casos de uso

Dado que el enunciado recomienda un mapeo 1:1 entre requisitos funcionales y casos de uso, la solución se organiza en torno a casos de uso concretos:

- Registro y gestión de pacientes
- Solicitud de prueba diagnóstica
- Programación de citas
- Confirmación/cancelación de citas
- Atención del paciente y seguimiento
- Emisión de informes médicos
- Generación de estadísticas y reportes

### 4. Manejo de prioridades

Se ha incorporado un orden explícito de prioridad para que el sistema pueda gestionar citas urgentes, preferentes y ordinarias de forma determinista.

### 5. Seguimiento periódico

Los tipos de prueba con seguimiento periódico generan solicitudes de seguimiento de forma automática, en línea con el requisito de control y citación periódica para pruebas de cribado y revisiones.

### 6. Integración futura

Se mantienen interfaces que pueden adaptarse a SSO, HCE, sistemas de admisión y estándares RIS/PACS (DICOM, HL7 y FHIR) sin comprometer el modelo de dominio base.

## Suposiciones razonadas

- La solución inicial no implementa la integración real con sistemas externos; se modela una capa de adaptación que puede ampliarse más adelante.
- La autenticación y autorización se asumen como requisitos de infraestructura y no forman parte de la lógica de dominio base.
- La configuración del catálogo de pruebas se gestiona de modo programático para facilitar pruebas y cambios de negocio.
- Las entidades se diseñan con identificación única para permitir trazabilidad y validación.

## Métricas de calidad

Se priorizan propiedades como:

- Correción funcional
- Facilidad de mantenimiento
- Claridad del diseño
- Testabilidad
- Bajo acoplamiento entre componentes

## Conclusión

La solución actual es una base sólida para un desarrollo iterativo e incremental, siguiendo la recomendación del enunciado: definir primero el núcleo del dominio, después la lógica de servicio y, finalmente, ampliar con capas de infraestructura y presentación.
