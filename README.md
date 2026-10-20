<div align="center">

# UNIVERSIDAD DE CARTAGENA

### Programa de Ingeniería de Software
### Asignatura: Estructuras de Datos — Unidad III

<br>

## ACTIVIDAD 3 — PROTOCOLO COLABORATIVO

### Diseño de un sistema de gestión utilizando Java Collections Framework

<br>

# EJERCICIO 6
## SISTEMA DE SOLICITUDES DE CRÉDITO

<br>

**Estudiante:** Rosary Carmona<br>
**Tutor:** John Carlos Arrieta Arrieta<br>
**Modalidad:** Individual<br>
**Fecha:** 19 de octubre de 2026

<br>

**Cartagena de Indias, Colombia**

</div>

---

## Presentación

Hola, mi nombre es **Rosary Carmona**. En este repositorio presento el desarrollo del **Ejercicio 6: Sistema de solicitudes de crédito**, correspondiente a la Actividad 3 de la Unidad III de la asignatura Estructuras de Datos. El proyecto aplica el Java Collections Framework a un caso de gestión de clientes y solicitudes, de acuerdo con los requisitos de la actividad.

## Introducción

La elección de una estructura de datos afecta directamente la forma en que un programa registra, organiza, consulta y procesa información. El Java Collections Framework ofrece interfaces e implementaciones con comportamientos diferentes: una lista conserva una secuencia, un conjunto permite controlar la unicidad, un mapa asocia claves con valores y una cola de prioridad selecciona elementos según un criterio. Comprender estas diferencias permite elegir una estructura por su función y no simplemente por conveniencia.

El caso elegido para este trabajo es el **Ejercicio 6: Sistema de solicitudes de crédito**. El sistema administra clientes y solicitudes asociadas a ellos. Cada solicitud contiene un número único, un monto, un tipo de crédito, un nivel de prioridad y un estado. La aplicación puede registrar clientes y solicitudes, impedir documentos y números duplicados, consultar solicitudes por su número y organizar las solicitudes pendientes para su evaluación según la prioridad.

La solución se implementó como una aplicación de consola en Java 17. Además del uso de colecciones, se aplican clases de dominio relacionadas, validaciones, encapsulamiento y separación de responsabilidades. La aplicación es una demostración académica: la evaluación y decisión de aprobación o rechazo son manuales, y los datos se mantienen en memoria mientras el programa está abierto.

## Objetivos

### Objetivo general

Desarrollar una aplicación de consola para administrar clientes y solicitudes de crédito, seleccionando interfaces e implementaciones del Java Collections Framework de acuerdo con las necesidades de unicidad, búsqueda, registro e interpretación de prioridades.

### Objetivos específicos

- Representar los conceptos de cliente y solicitud de crédito mediante clases de dominio relacionadas.
- Controlar documentos de cliente y números de solicitud duplicados.
- Consultar clientes por documento y solicitudes por número.
- Conservar un registro general y un historial limitado de solicitudes recientes.
- Procesar solicitudes pendientes según su nivel de prioridad.
- Explicar la diferencia entre las interfaces de colección y sus implementaciones concretas.
- Utilizar Git para conservar el historial real de cambios del proyecto.

## Descripción del problema

Una entidad necesita registrar clientes y recibir solicitudes de crédito. Cada cliente se identifica por su documento y cada solicitud por un número único. Una solicitud solo puede registrarse para un cliente que ya esté en el sistema. Para organizar la evaluación, las solicitudes pendientes deben atenderse por prioridad y no únicamente por el orden en que fueron recibidas.

El sistema representa este flujo mediante un menú de consola. La persona usuaria registra la información y, al procesar una solicitud, toma manualmente la decisión de aprobarla o rechazarla.

## Alcance funcional

La aplicación permite:

1. Registrar clientes y rechazar documentos que ya existen.
2. Registrar solicitudes para clientes previamente registrados.
3. Rechazar números de solicitud repetidos y montos no positivos.
4. Asignar prioridades enteras de 1 a 5.
5. Consultar una solicitud por su número único.
6. Mostrar todas las solicitudes en orden de registro.
7. Seleccionar para evaluación la solicitud pendiente de mayor prioridad.
8. Cambiar el estado de la solicitud seleccionada a `EN_REVISION`.
9. Registrar la decisión manual de aprobación o rechazo.
10. Consultar las diez solicitudes registradas más recientemente.

### Regla para ordenar la evaluación

La prioridad **5** es la más alta y la prioridad **1** es la más baja. Si varias solicitudes tienen la misma prioridad, el sistema desempata por el número de solicitud en orden alfanumérico ascendente. Por ejemplo, una solicitud con prioridad 5 se selecciona antes que otra con prioridad 2, aunque haya sido registrada después.

### Estados

| Estado | Significado |
| --- | --- |
| `PENDIENTE` | Registrada, a la espera de evaluación. |
| `EN_REVISION` | Seleccionada para que la persona usuaria la evalúe. |
| `APROBADA` | Decisión manual de aprobación. |
| `RECHAZADA` | Decisión manual de rechazo. |

## Marco conceptual: Java Collections Framework

Una **interfaz** describe el comportamiento esperado de una colección; una **implementación** proporciona una forma concreta de realizarlo. Por ejemplo, `List` define operaciones de secuencia y `ArrayList` es una implementación que puede utilizarse para esa interfaz. El programa declara variables con las interfaces (`List`, `Set`, `Map`, `Queue`, `Deque`) y crea objetos de implementaciones provistas por Java.

El sistema utiliza:

- **List:** colección secuencial que conserva el orden y admite elementos repetidos.
- **Set:** colección de elementos únicos, adecuada para controlar duplicados.
- **Map:** asociación de una clave con un valor, conveniente para búsquedas mediante identificadores.
- **Queue / PriorityQueue:** estructura para procesar el elemento prioritario antes que los demás.
- **Deque / ArrayDeque:** estructura de doble extremo utilizada aquí para conservar los registros recientes.

## Diseño del sistema

### Clases del dominio

- [`Cliente.java`](src/main/java/sistemacreditos/Cliente.java): representa a la persona solicitante mediante documento, nombre y teléfono.
- [`SolicitudCredito.java`](src/main/java/sistemacreditos/SolicitudCredito.java): contiene el número, monto, tipo, prioridad, estado y referencia al cliente.
- [`EstadoSolicitud.java`](src/main/java/sistemacreditos/EstadoSolicitud.java): enumera los estados permitidos.

### Clases de aplicación

- [`SistemaCreditos.java`](src/main/java/sistemacreditos/SistemaCreditos.java): administra las colecciones y aplica las reglas de registro, consulta y evaluación.
- [`Main.java`](src/main/java/sistemacreditos/Main.java): presenta el menú, solicita datos y comunica resultados.

## Selección y justificación de colecciones

| Interfaz | Implementación | Responsabilidad dentro del sistema | Justificación |
| --- | --- | --- | --- |
| `List<SolicitudCredito>` | `ArrayList` | Registro general de solicitudes | Mantiene el orden de registro y permite recorrer el catálogo para listarlo. |
| `Set<String>` | `HashSet` | Documentos de clientes registrados | Evita documentos duplicados y permite comprobar si ya se registró uno. |
| `Map<String, Cliente>` | `HashMap` | Índice de clientes por documento | Asocia el documento con el cliente que se debe localizar para crear una solicitud. |
| `Map<String, SolicitudCredito>` | `HashMap` | Índice de solicitudes por número | Permite consultar directamente una solicitud usando su identificador. |
| `Queue<SolicitudCredito>` | `PriorityQueue` | Solicitudes pendientes de evaluación | Selecciona primero la solicitud de mayor prioridad, en vez de limitarse al orden de llegada. |
| `Deque<SolicitudCredito>` | `ArrayDeque` | Diez solicitudes registradas más recientemente | Agrega cada nueva solicitud al inicio y retira la más antigua al exceder el límite. |

Las colecciones se declaran usando interfaces y se instancian con implementaciones estándar del SDK. No se crean estructuras manuales como listas enlazadas, nodos, colas, pilas, conjuntos o mapas propios.

## Desarrollo de la solución

### Paso 1. Organización del proyecto

El código Java está organizado bajo `src/main/java/sistemacreditos`, separando las entidades del dominio, la lógica de gestión y la interacción por consola. Maven administra la compilación y las pruebas; sus archivos generados se guardan en `target`, que está excluida del repositorio mediante `.gitignore`.

### Paso 2. Modelado de clientes y solicitudes

Se crearon `Cliente` y `SolicitudCredito` como clases distintas, relacionadas por una referencia desde la solicitud al cliente. Los datos identificadores y descriptivos se encapsulan en las clases. El estado de solicitud se representa con el enum `EstadoSolicitud`, en lugar de aceptar cualquier cadena arbitraria.

### Paso 3. Registro y validaciones

`SistemaCreditos` rechaza documentos de cliente ya registrados y números de solicitud repetidos. También valida los campos obligatorios, que el monto sea mayor que cero, que la prioridad esté entre 1 y 5 y que el cliente asociado haya sido registrado en el sistema.

### Paso 4. Búsqueda y colecciones

El registro general de solicitudes se conserva en un `ArrayList`. Los `HashMap` indexan clientes y solicitudes por sus claves, mientras que el `HashSet` mantiene los documentos únicos. La `PriorityQueue` organiza el trabajo pendiente y el `ArrayDeque` conserva un historial limitado de las solicitudes más recientes.

### Paso 5. Procesamiento y decisión

Al elegir la opción de procesamiento, la cola selecciona la solicitud pendiente con prioridad más alta y cambia su estado a `EN_REVISION`. Después, la persona usuaria ingresa la decisión de aprobación o rechazo. El sistema solo permite resolver solicitudes que están en revisión.

### Paso 6. Menú de consola

La interacción se implementa en `Main.java`. El menú ofrece el registro y consulta de datos, el listado, el procesamiento prioritario, la consulta del historial y la salida del programa. Las entradas inválidas se comunican al usuario y no se registran como operaciones correctas.

## Ejecución y prueba manual

Se requiere JDK 17 o superior y Maven 3.9 o superior. Desde la raíz del proyecto, ejecuta las pruebas automatizadas y compila con:

```powershell
mvn clean test
mvn package
```

Las pruebas automatizadas usan JUnit 5 y cubren validaciones del modelo, duplicados, asociación y búsqueda de solicitudes, límite del historial, orden de prioridad y transición de estados.

Para iniciar el menú interactivo después de compilar:

```powershell
java -cp target\classes sistemacreditos.Main
```

El menú presenta estas opciones:

```text
=== SISTEMA DE SOLICITUDES DE CREDITO ===
1. Registrar cliente
2. Registrar solicitud de credito
3. Consultar solicitud por numero
4. Listar solicitudes
5. Procesar siguiente solicitud por prioridad
6. Ver las solicitudes mas recientes
0. Salir
```

### Escenario de verificación

Se registró un cliente y se intentó registrar otra vez el mismo documento. El sistema rechazó el duplicado. Luego se registraron dos solicitudes del mismo cliente: `SC-001` con prioridad 2 y `SC-002` con prioridad 5. La consulta por número encontró `SC-001`, y el procesamiento seleccionó primero `SC-002`.

Salida observada durante la prueba:

```text
No se pudo registrar el cliente: Ya existe un cliente con el documento 111.
Solicitud SC-001 | Cliente: Ana | Monto: $1000 | Tipo: Personal | Prioridad: 2 | Estado: PENDIENTE
Solicitud seleccionada: Solicitud SC-002 | Cliente: Ana | Monto: $1200 | Tipo: Auto | Prioridad: 5 | Estado: EN_REVISION
Solicitud aprobada.
Solicitud seleccionada: Solicitud SC-001 | Cliente: Ana | Monto: $1000 | Tipo: Personal | Prioridad: 2 | Estado: EN_REVISION
Solicitud rechazada.
```

Este escenario confirma el control de duplicados, la búsqueda por número, el orden por prioridad y el registro de una decisión manual.

## Estructura de archivos

```text
SistemaCreditos/
├── .gitignore
├── pom.xml
├── README.md
└── src/
    ├── main/
        └── java/
            └── sistemacreditos/
                ├── Cliente.java
                ├── EstadoSolicitud.java
                ├── Main.java
                ├── SistemaCreditos.java
                └── SolicitudCredito.java
    └── test/
        └── java/
            └── sistemacreditos/
                ├── ClienteTest.java
                ├── ProcesamientoPrioridadTest.java
                ├── SistemaCreditosRegistroTest.java
                └── SolicitudCreditoTest.java
```

## Control de versiones con Git

El repositorio remoto del proyecto es [sistema-solicitudes-credito](https://github.com/rcarmonan-commits/sistema-solicitudes-credito). Para desarrollar la actividad se utiliza la rama `desarrollo-ejercicio`; sus avances se publican en remoto y luego se integran en `main`. Los mensajes describen cada cambio (configuración, validaciones, registro, prioridad y documentación). Las fechas del historial corresponden a los momentos reales en que se realizaron los cambios; no se modifican para aparentar trabajo en días anteriores.

Para clonar y ejecutar:

```powershell
git clone https://github.com/rcarmonan-commits/sistema-solicitudes-credito.git
cd sistema-solicitudes-credito
mvn clean test
mvn package
java -cp target\classes sistemacreditos.Main
```

Para continuar el desarrollo colaborativo, cambia a la rama publicada:

```powershell
git switch --track origin/desarrollo-ejercicio
```

Registra y publica cada avance real:

```powershell
git status
git add .
git commit -m "test: describir el avance realizado"
git push
```

Al finalizar, integra el trabajo en la rama principal:

```powershell
git switch main
git pull
git merge desarrollo-ejercicio
git push origin main
```

Para continuar trabajando después de la integración:

```powershell
git status
git add .
git commit -m "Describe el cambio realizado"
git push
```

## Limitaciones y mejoras futuras

Esta entrega es una aplicación académica de consola. Los datos solo permanecen durante la ejecución; no se utilizan archivos ni base de datos para persistencia. El programa tampoco calcula capacidad de pago ni decide automáticamente la elegibilidad financiera. Como mejoras futuras se podrían incorporar persistencia, pruebas automatizadas, una capa de interfaz gráfica y criterios de evaluación configurables.
