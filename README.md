# Sistema de Gestión de Solicitudes de Crédito

**Institución de Educación Superior:** Universidad de Cartagena (Centro Promotor / PESAD)  
**Facultad:** Facultad de Ingeniería  
**Programa Académico:** Ingeniería de Software / Ciencias de la Computación  
**Asignatura:** Estructuras de Datos  
**Unidad Académica:** Unidad III – Protocolo Colaborativo (30%)  
**Caso Asignado:** Caso 6 – Sistema de solicitudes de crédito  
**Estudiante:** Rosary Eilyn Carmona Navarro  
**Correo Institucional:** rcarmonan@unicartagena.edu.co  
**Versión de Java:** OpenJDK 17 LTS  

---

## 1. Descripción del Problema y Modelo del Dominio

El sistema modela una solución bancaria integral para la radicación, control de duplicidad, consulta indexada, ordenamiento dinámico y procesamiento prioritario de solicitudes de crédito y sus clientes titulares.

### Entidades Principales y Atributos:
* **`Cliente`:** Representa al titular solicitante. Contiene `numeroDocumento` (clave única de identidad), `nombreCompleto`, `correoElectronico`, `telefono` e `ingresosMensuales` (`BigDecimal`). Implementa contratos `equals()` y `hashCode()` basados en el documento y orden natural `Comparable<Cliente>`.
* **`SolicitudCredito`:** Entidad principal del crédito. Contiene `numeroSolicitud` (código alfanumérico único), referencia al `cliente` titular, `montoSolicitado` (`BigDecimal`), `plazoMeses`, `tipoCredito` (`TipoCredito`), `prioridad` (`NivelPrioridad`), `estado` (`EstadoSolicitud`), `fechaRadicacion` (`LocalDateTime`) y `observaciones` de auditoría. Implementa `equals()`, `hashCode()` y `Comparable<SolicitudCredito>`.
* **Líneas de Crédito (`TipoCredito`):** `HIPOTECARIO` (tasa ref. 11.5%, máx 240 m), `VEHICULO` (14.0%, máx 72 m), `LIBRE_INVERSION` (18.5%, máx 60 m), `COMERCIAL` (13.0%, máx 120 m), `EDUCATIVO` (9.5%, máx 48 m).
* **Nivel de Prioridad (`NivelPrioridad`):** `URGENTE` (peso 4), `ALTA` (peso 3), `MEDIA` (peso 2), `BAJA` (peso 1).
* **Ciclo de Vida (`EstadoSolicitud`):** `PENDIENTE`, `EN_EVALUACION`, `APROBADA`, `RECHAZADA`, `CANCELADA`.

---

## 2. Matriz de Selección y Responsabilidad de Colecciones

En estricto cumplimiento de la restricción de la actividad (Sección 4 de la rúbrica), **no se implementaron estructuras manuales caseras** ni se sustituyeron operaciones mediante Stream API (Sección 18). Cada colección del SDK de Java cumple una función específica dentro del programa:

| Necesidad Funcional del Sistema | Interfaz SDK | Implementación Concreta | Justificación Técnica de Ingeniería |
|---|---|---|---|
| **Registro Maestro Histórico** | `List<SolicitudCredito>` | `ArrayList<SolicitudCredito>` | Almacena la totalidad de solicitudes radicadas preservando el orden cronológico estricto de radicación. Ofrece acceso indexado en $\mathcal{O}(1)$ y bajo consumo de memoria comparado con listas enlazadas. |
| **Control de Unicidad de Clientes** | `Set<String>` y `Set<Cliente>` | `HashSet<String>` y `HashSet<Cliente>` | Garantiza que no existan clientes repetidos con el mismo documento. Mediante dispersión hash (`hashCode` y `equals`), `set.add()` valida e inserta en tiempo $\mathcal{O}(1)$ promedio, rechazando colisiones de identidad. |
| **Búsqueda Instantánea por Identificador** | `Map<String, SolicitudCredito>` | `HashMap<String, SolicitudCredito>` | Asocia el código único de solicitud con el objeto de negocio. Permite recuperar, consultar existencia (`containsKey`), actualizar y desvincular registros en $\mathcal{O}(1)$ promedio, superando la búsqueda lineal $\mathcal{O}(n)$ de una lista. |
| **Cartera Ordenada por Monto Financiero** | `Set<SolicitudCredito>` | `TreeSet<SolicitudCredito>` | Mantiene la cartera clasificada de mayor a menor monto en una estructura arbórea auto-balanceada (Red-Black Tree) con costo $\mathcal{O}(\log n)$. Aplica un `Comparator` compuesto que desempata por código para evitar que empates de monto eliminen registros. |
| **Cartera Ordenada por Código (Orden Natural)** | `Set<SolicitudCredito>` | `TreeSet<SolicitudCredito>` | Utiliza la interfaz `Comparable<SolicitudCredito>` implementada en la entidad para desplegar la vista alfanumérica canónica. |
| **Evaluación Crediticia por Prioridad** | `Queue<SolicitudCredito>` | `PriorityQueue<SolicitudCredito>` | Administra el comité de evaluación mediante un montículo binario (heap). Las solicitudes con mayor peso jerárquico (`URGENTE` > `ALTA` > `MEDIA` > `BAJA`) son despachadas primero (`poll()`), desempatando por orden FIFO de radicación. |

---

## 3. Estructura y Organización del Código

El proyecto adopta una arquitectura modular de tres capas bajo principios de alta cohesión y bajo acoplamiento:

```text
src/
├── modelo/
│   ├── Cliente.java                      # Modelo del cliente (equals, hashCode y Comparable)
│   ├── SolicitudCredito.java             # Entidad del crédito con validaciones y contratos
│   ├── TipoCredito.java                  # Enum con tasas referenciales y plazos máximos
│   ├── NivelPrioridad.java               # Enum con jerarquía numérica para PriorityQueue
│   └── EstadoSolicitud.java              # Ciclo de vida transaccional del crédito
├── logica/
│   ├── GestorCredito.java                # Servicio orquestador de colecciones y sincronización
│   ├── ComparadorPrioridadEvaluacion.java # Comparator jerárquico + FIFO para PriorityQueue
│   ├── ComparadorMontoCredito.java       # Comparator compuesto por monto + desempate para TreeSet
│   ├── CargadorDatosIniciales.java       # Precarga obligatoria de 15 registros deliberados
│   ├── ClienteDuplicadoException.java    # Excepción ante duplicidad en Set
│   ├── SolicitudDuplicadaException.java  # Excepción ante clave duplicada en Map
│   └── SolicitudNoEncontradaException.java # Excepción ante clave inexistente en Map
└── vista/
    └── CreditoApp.java                   # Interfaz interactiva de consola con menú asistido
```

---

## 4. Guía de Instalación, Compilación y Ejecución (Paso a Paso)

### Requisitos Previos:
* Java Development Kit (JDK) 17 o superior instalado.
* Terminal de comandos (PowerShell en Windows, Bash en Linux/macOS o CMD).

### Paso 1: Clonar el repositorio
```bash
git clone https://github.com/rcarmonan-commits/sistema-solicitudes-credito.git
cd sistema-solicitudes-credito
```

### Paso 2: Compilar el código fuente
Compila todas las clases de los tres paquetes hacia la carpeta `bin/`:
```bash
javac -d bin src/modelo/*.java src/logica/*.java src/vista/*.java
```

### Paso 3: Ejecutar la aplicación
Inicia la consola interactiva con la clase principal:
```bash
java -cp bin vista.CreditoApp
```

---

## 5. Manual de Usuario: Guía Paso a Paso de las Opciones del Menú

Al iniciar la aplicación, se despliega la carátula institucional y se cargan automáticamente **15 registros iniciales de prueba**. El menú principal cuenta con 13 operaciones funcionales:

```text
============================== MENÚ DE GESTIÓN ==============================
 1. Radicar nueva solicitud de crédito (Registro general)
 2. Consultar historial maestro de solicitudes (ArrayList)
 3. Búsqueda instantánea O(1) de solicitud por código (HashMap)
 4. Control de clientes y prueba de unicidad (HashSet)
 5. Mostrar cartera ordenada por monto financiero (TreeSet + Comparator)
 6. Mostrar cartera ordenada por código natural (TreeSet + Comparable)
 7. Encolar solicitud existente para estudio en comité (PriorityQueue)
 8. Consultar solicitud que encabeza la cola prioritaria (peek)
 9. Evaluar y despachar próxima solicitud prioritaria (poll)
10. Modificar monto o actualizar prioridad con reestructuración de heap
11. Cancelar o eliminar solicitud (Garantía de consistencia y sincronía)
12. Diagnóstico integral de sincronización y consistencia de colecciones
13. Restablecer los 15 registros de prueba iniciales
 0. Salir de la aplicación
=============================================================================
```

### Guía de Operaciones Principales:

* **Opción 1: Radicar Nueva Solicitud (Registro y Sincronización):**
  Permite ingresar código de solicitud, documento de cliente, línea de crédito, monto, plazo y prioridad. El sistema registra simultáneamente en `ArrayList` (historial), `HashMap` (búsqueda) y `PriorityQueue` (si entra como PENDIENTE).
* **Opción 2: Consultar Historial Maestro (`ArrayList`):**
  Despliega todas las solicitudes en el orden cronológico estricto en que fueron radicadas, mostrando el índice de radicación.
* **Opción 3: Búsqueda Instantánea en $\mathcal{O}(1)$ (`HashMap`):**
  Solicita el código único (ej. `SOL-1005`). La búsqueda se efectúa por dispersión hash directa sin recorrer la lista. Si se busca un código inexistente (ej. `SOL-9999`), el sistema captura la excepción `SolicitudNoEncontradaException` informando al usuario.
* **Opción 4: Control de Clientes y Prueba de Unicidad (`HashSet`):**
  Permite listar los clientes registrados o ejecutar la prueba deliberada de duplicidad con el documento `CC-1045234001`, comprobando que `HashSet.add()` rechaza la inserción arrojando `ClienteDuplicadoException`.
* **Opción 5: Cartera Ordenada por Monto (`TreeSet` + `Comparator`):**
  Muestra los créditos clasificados de mayor a menor monto. Permite observar que solicitudes con idéntico monto (`SOL-1003` y `SOL-1008` por $45M, y `SOL-1001` y `SOL-1011` por $15M) coexisten gracias al desempate por código.
* **Opción 6: Cartera Ordenada por Código (`TreeSet` + `Comparable`):**
  Muestra las solicitudes ordenadas según el orden alfanumérico natural `compareTo()` de la clase `SolicitudCredito`.
* **Opción 8 y 9: Inspección y Despacho en `PriorityQueue` (`peek()` y `poll()`):**
  La opción 8 muestra la solicitud en la cima del montículo sin extraerla (`peek()`). La opción 9 evalúa y retira la solicitud (`poll()`), demostrando que las solicitudes `URGENTE` salen primero aunque hayan llegado después de las de baja prioridad.
* **Opción 10: Modificación y Reestructuración del Montículo:**
  Permite actualizar el monto o la prioridad de un crédito. Al cambiar la prioridad, el sistema extrae temporalmente la solicitud de la `PriorityQueue` y la reinserta para forzar la flotación (sift-up / sift-down) en el heap binario.
* **Opción 11: Cancelación y Sincronización:**
  Al cancelar una solicitud, el sistema actualiza su estado a `CANCELADA` y la retira de la `PriorityQueue` de evaluación, manteniendo consistencia en todas las colecciones.
* **Opción 12: Diagnóstico de Sincronía y Balance:**
  Emite un informe cuantitativo comparando los tamaños de `ArrayList`, `HashMap`, `HashSet` y `PriorityQueue`, certificando que no existen referencias huérfanas o desincronizadas.

---

## 6. Catálogo de Datos de Prueba Precargados (15 Casos Deliberados)

El sistema incluye 15 solicitudes y 15 clientes iniciales precargados en `CargadorDatosIniciales` para sustentar cada caso de prueba sin digitar a mano:

| Código | Documento Cliente | Nombre del Titular | Tipo de Crédito | Monto ($) | Plazo | Prioridad | Estado | Escenario de Prueba |
|:---:|:---:|:---|:---:|:---:|:---:|:---:|:---:|:---|
| **SOL-1001** | CC-1045234001 | Juan Carlos Pérez Gómez | LIBRE_INVERSION | $15.000.000 | 36 m | BAJA | PENDIENTE | Radicada primero; sale de última en PriorityQueue por baja prioridad. |
| **SOL-1002** | CC-1045234002 | María Fernanda Castillo Luna | HIPOTECARIO | $180.000.000 | 180 m | MEDIA | PENDIENTE | Caso estándar crédito de vivienda. |
| **SOL-1003** | CC-1045234003 | Andrés Felipe Morales Ríos | VEHICULO | $45.000.000 | 48 m | ALTA | PENDIENTE | **Empate en TreeSet** con SOL-1008 ($45M). |
| **SOL-1004** | CC-1045234004 | Laura Sofía Mendoza Ortiz | EDUCATIVO | $8.000.000 | 24 m | URGENTE | PENDIENTE | **Salto en PriorityQueue:** Radicada de 4ta pero atendida de 1ra por ser URGENTE. |
| **SOL-1005** | CC-1045234005 | Carlos Eduardo Herrera Vargas | COMERCIAL | $250.000.000 | 84 m | ALTA | PENDIENTE | Monto más alto de la cartera (#1 en TreeSet). Búsqueda O(1) en HashMap. |
| **SOL-1006** | CC-1045234006 | Valentina Díaz Romero | HIPOTECARIO | $150.000.000 | 120 m | MEDIA | PENDIENTE | Caso hipotecario intermedio. |
| **SOL-1007** | CC-1045234007 | Gabriel Antonio Torres Silva | LIBRE_INVERSION | $20.000.000 | 36 m | BAJA | PENDIENTE | Caso ordinario consumo. |
| **SOL-1008** | CC-1045234008 | Daniela Marcela Ruiz Navarro | VEHICULO | $45.000.000 | 60 m | URGENTE | PENDIENTE | **Empate en TreeSet** ($45M) y atención preferente en PriorityQueue. |
| **SOL-1009** | CC-1045234009 | Sebastián Camilo Paternina Polo | COMERCIAL | $95.000.000 | 60 m | ALTA | PENDIENTE | Caso comercial de mediana empresa. |
| **SOL-1010** | CC-1045234010 | Paula Andrea Vergara Meza | EDUCATIVO | $12.000.000 | 36 m | URGENTE | PENDIENTE | Trámite preferente en PriorityQueue. |
| **SOL-1011** | CC-1045234011 | Ricardo José Salgado Benítez | LIBRE_INVERSION | $15.000.000 | 24 m | MEDIA | PENDIENTE | **Empate en TreeSet** con SOL-1001 ($15M). |
| **SOL-1012** | CC-1045234012 | Natalia Carolina Mercado Bello | HIPOTECARIO | $200.000.000 | 180 m | ALTA | PENDIENTE | Segundo monto más alto (#2 en TreeSet). |
| **SOL-1013** | CC-1045234013 | Jorge Luis Arrieta Puello | VEHICULO | $35.000.000 | 48 m | BAJA | PENDIENTE | Caso vehicular estándar. |
| **SOL-1014** | CC-1045234014 | Diana Patricia Cuadrado Buelvas | COMERCIAL | $80.000.000 | 72 m | MEDIA | PENDIENTE | Caso equipamiento comercial. |
| **SOL-1015** | CC-1045234015 | Mateo Alejandro Gómez Cardona | EDUCATIVO | $10.000.000 | 36 m | URGENTE | PENDIENTE | Cuarta solicitud URGENTE en cola de atención. |

---

## 7. Análisis de Complejidad Algorítmica y Decisiones de Diseño

| Colección | Operación Principal | Complejidad Temporal | Comportamiento Interno |
|---|---|:---:|---|
| **`ArrayList`** | `add(e)` (al final) | $\mathcal{O}(1)$ amortizado | Inserción directa en arreglo dinámico continuo. |
| **`ArrayList`** | `get(index)` | $\mathcal{O}(1)$ | Acceso indexado inmediato mediante cálculo de dirección de memoria. |
| **`HashSet`** | `add(e)` / `contains(e)` | $\mathcal{O}(1)$ promedio | Evaluación de `hashCode()` y resolución de cubeta (bucket). |
| **`HashMap`** | `put(k, v)` / `get(k)` | $\mathcal{O}(1)$ promedio | Búsqueda por clave mediante dispersión hash en tiempo constante. |
| **`PriorityQueue`** | `offer(e)` / `poll()` | $\mathcal{O}(\log n)$ | Reordenamiento en montículo binario (heap) mediante sift-up / sift-down. |
| **`PriorityQueue`** | `peek()` | $\mathcal{O}(1)$ | Consulta directa del elemento en la raíz del árbol binario. |
| **`TreeSet`** | `add(e)` / `contains(e)` | $\mathcal{O}(\log n)$ | Inserción balanceada en árbol rojo-negro (Red-Black Tree). |

### Reglas de Integridad y Consistencia:
1. **Preservación de Registros en `TreeSet`:** Cuando dos solicitudes tienen el mismo monto solicitado, el comparador no retorna `0`; en su lugar, desempata alfabéticamente por `numeroSolicitud`. Esto evita que el árbol balanceado interprete empates como duplicidades.
2. **Reestructuración Dinámica en `PriorityQueue`:** Al mutar el nivel de prioridad de una solicitud encolada, el gestor retira el objeto (`remove`) y lo reinserta (`add`), forzando al algoritmo del montículo a recalcular su posición exacta.
3. **Contrato `equals()` y `hashCode()`:** Implementados sobre las claves naturales del problema (`numeroSolicitud` en `SolicitudCredito` y `numeroDocumento` en `Cliente`), garantizando coherencia para todas las colecciones basadas en dispersión.
4. **Manejo Monetario:** Se utilizó `BigDecimal` para evitar imprecisiones de redondeo en tipos flotantes primitivos IEEE 754.

---

## 8. Historial de Desarrollo y Control de Versiones en Git

El proyecto fue desarrollado aplicando la metodología progresiva de Git exigida en la Sección 19 y 20 de la rúbrica, utilizando ramas de trabajo independientes y commits semánticos:

* **Rama `main`:** Rama de producción que contiene el commit raíz inicial y el merge final con la solución completa.
* **Rama `desarrollo-ejercicio`:** Rama de trabajo donde se construyeron modularmente las entidades, colecciones, comparadores y pruebas.

### Cronología de Commits Progresivos:
1. `2026-08-15 15:45` – `chore: inicializar estructura del proyecto y configuracion de gitignore`
2. `2026-08-28 10:20` – `feat: crear modelos del dominio Cliente, SolicitudCredito y enumeraciones con validacion defensiva`
3. `2026-09-12 16:30` – `feat: implementar registro general con ArrayList y control de duplicados con HashSet`
4. `2026-09-28 11:15` – `feat: agregar indice de busqueda directa por identificador mediante HashMap`
5. `2026-10-10 18:40` – `feat: implementar cola de atencion segun evaluacion con PriorityQueue`
6. `2026-10-22 14:05` – `feat: implementar vista ordenada de solicitudes mediante TreeSet y Comparator compuesto`
7. `2026-10-31 09:50` – `fix: garantizar sincronizacion y consistencia entre colecciones ante cambios y cancelaciones`
8. `2026-11-08 17:15` – `test: incorporar suite de 15 datos precargados para demostracion de sustentacion`
9. `2026-11-12 20:30` – `feat: implementar interfaz de consola guiada con menu interactivo en consola`
10. `2026-11-14 15:10` – `docs: estructurar documentacion tecnica y guia de ejecucion en README.md`
11. `2026-11-15 23:50` – `merge: integrar rama desarrollo-ejercicio en main`

---

## 9. Enlaces Oficiales de Entrega

* **Repositorio Público en GitHub:**  
  [https://github.com/rcarmonan-commits/sistema-solicitudes-credito](https://github.com/rcarmonan-commits/sistema-solicitudes-credito)
* **Video de Sustentación:**  
  *(Enlace de visualización en YouTube / Google Drive / SIMA-PESAD pendiente de grabación por la estudiante)*
* **Plataforma Académica Oficial:**  
  SIMA-PESAD – Universidad de Cartagena
