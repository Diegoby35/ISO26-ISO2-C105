# DECISIONS.md

Registro de decisiones y supuestos del proyecto de Ingeniería del Software II. Este documento acompaña a la planificación y a los casos de uso; no sustituye la memoria teórica ni las decisiones que debe aprobar el equipo humano.

## Contexto y objetivo

El sistema gestionará pruebas radiológicas e imagen médica del Servicio de Salud de Castilla-La Mancha (SESCAM): solicitudes, citación, realización e informes, además de agendas, recursos y seguimiento. Debe poder evolucionar hacia clientes web, de escritorio y móviles, e integrarse progresivamente con servicios hospitalarios.

El proceso se guiará por el Proceso Unificado de Desarrollo (PUD): dirigido por casos de uso, iterativo e incremental, y centrado en una arquitectura explícita. El equipo humano conserva la responsabilidad de analizar requisitos, priorizar casos de uso, planificar, decidir la arquitectura y diseñar contratos y pruebas. La IA puede apoyar la implementación y ejecución de pruebas bajo revisión humana.

## Decisiones y supuestos iniciales

### D-01 — Trazabilidad de requisitos y casos de uso

- **Decisión inicial:** utilizar como referencia de planificación el mapeo 1:1 entre cada requisito funcional y un caso de uso, y entre cada caso de uso y una iteración.
- **Motivo:** es la simplificación recomendada por el enunciado para estimar inicialmente el proyecto.
- **Consecuencia:** el catálogo de casos de uso debe conservar enlaces a los requisitos de origen y a la iteración/release que los realiza.
- **Revisión:** el equipo debe comprobar que esta simplificación no agrupe artificialmente funcionalidades que deban tratarse juntas ni fragmente flujos coherentes.

### D-02 — Fases e iteraciones de planificación

- **Decisión inicial:** contemplar una Iteración 0 de inicio, iteraciones de realización asociadas a casos de uso y una Iteración N de transición, integración, pruebas de integración, despliegue y cierre.
- **Supuestos del enunciado:** para estimaciones iniciales, Iteración 0 cuesta 1.000 € y la Iteración N cuesta 2.000 € en recursos humanos. Una semana de consultoría humana equivale a 40 horas.
- **Pendiente del equipo:** estimar el coste y duración de las iteraciones de realización, explicitar calendario de releases, capacidad del equipo, revisión humana de entregables de IA y costes de licencias/modelos. Las estimaciones deben reflejar incertidumbre y el posible sesgo de optimismo.

### D-03 — Arquitectura por componentes y contratos

- **Decisión inicial:** organizar el sistema en componentes cohesivos con bajo acoplamiento, que se comuniquen mediante interfaces explícitas. El diseño debe separar responsabilidades de presentación, aplicación/servicios, dominio e infraestructura.
- **Motivo:** permite desarrollar e integrar incrementos de manera controlada, manteniendo contratos comprensibles entre módulos.
- **Dirección tecnológica:** Java y Maven son la opción de referencia recomendada por el enunciado. Cada componente planificado debe tener responsabilidades, versión Semantic Versioning, interfaces y criterios de aceptación documentados.
- **Estado actual:** el código inicial de este repositorio es un único módulo Maven; todavía no demuestra una arquitectura multimódulo ni contratos entre componentes. La descomposición deberá decidirse y aplicarse en las iteraciones correspondientes, evitando crear módulos sin un caso de uso y una responsabilidad justificados.

### D-04 — Integraciones hospitalarias

- **Decisión inicial:** mantener las integraciones con SSO, Historia Clínica Electrónica, admisión y RIS/PACS como límites de integración explícitos, con adaptadores detrás de contratos propios cuando se planifiquen esos casos de uso.
- **Estándares a considerar:** DICOM para imágenes y los estándares HL7/FHIR para intercambio clínico, según las necesidades acordadas.
- **Alcance inicial:** no se presupone acceso a sistemas reales, credenciales, endpoints ni datos clínicos de producción. No se afirma que exista una integración funcional hasta que se diseñe, implemente y verifique.

### D-05 — Roles y permisos

- **Actores identificados:** pacientes, médicos, personal administrativo/admisión y administradores del sistema.
- **Decisión inicial:** vincular permisos a responsabilidades y limitar el acceso a información clínica al mínimo necesario para cada tarea.
- **Pendiente del equipo:** especificar una matriz de permisos y los flujos de auditoría antes de implementar autorización. La autenticación corporativa/SSO se considera una integración, no una capacidad ya resuelta por el modelo de dominio.

### D-06 — Catálogo de pruebas y prioridades

- **Decisión inicial:** el catálogo de tipos de prueba debe poder representar modalidad, equipo/sala, duración estimada, preparación, contraste, consentimiento informado, seguridad, prioridad y posibilidad de seguimiento periódico.
- **Prioridad clínica:** conservar los niveles urgente, preferente y ordinario como valores explícitos; las reglas de ordenación y asignación de huecos deben definirse con el personal responsable.
- **Alcance inicial:** los tipos de prueba presentes en el código son datos de ejemplo, no un catálogo clínico completo ni protocolos aprobados.

### D-07 — Seguimiento y citas

- **Requisito:** una prescripción de seguimiento puede especificar periodicidad y dar lugar a nuevas necesidades de cita.
- **Decisión inicial:** distinguir la prescripción/plan de seguimiento de las citas concretas que gestione admisión. Generar citas automáticamente solo después de definir reglas, responsables, límites y aceptación del flujo.
- **Estado actual:** el código inicial almacena una indicación de seguimiento en una solicitud, pero aún no programa automáticamente solicitudes ni citas futuras.

### D-08 — Calidad y pruebas

- **Prioridades iniciales:** corrección funcional, seguridad y confidencialidad, mantenibilidad, interoperabilidad, usabilidad y testabilidad.
- **Decisión inicial:** derivar criterios de aceptación y pruebas de los casos de uso. Cada componente debe contar con pruebas adecuadas a sus contratos; la integración y el sistema completo se verificarán en las iteraciones previstas para ello.
- **Pendiente del equipo:** definir métricas y umbrales verificables de calidad, estrategia de pruebas y tratamiento de datos de prueba. No utilizar datos personales o clínicos reales en pruebas sin autorización y controles apropiados.

### D-09 — Gestión del trabajo y configuración

- **Decisión inicial:** mantener trazabilidad entre planificación, reuniones, decisiones, issues, entregas y cambios del repositorio. Utilizar ramas y pull requests para revisar e integrar trabajo; evitar commits directos a `main`, conforme al enunciado.
- **Versionado:** usar Semantic Versioning para componentes y construcciones, con ramas y releases coherentes con el plan de configuración que acuerde el equipo.
- **Documentación:** enlazar desde la wiki del repositorio la planificación, casos de uso, actas, decisiones y demás entregables requeridos.

## Límites de la línea base

Las decisiones anteriores son una propuesta inicial para revisión del equipo. No constituyen aprobación de requisitos clínicos ni sustituyen las decisiones humanas que se deben justificar en la memoria. Las funcionalidades existentes en el código deben evaluarse frente a los casos de uso y sus contratos antes de considerarse completas.
