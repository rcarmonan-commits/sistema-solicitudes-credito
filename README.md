# Sistema de solicitudes de credito

Aplicacion de consola en Java 17 para registrar clientes y solicitudes de credito, consultar solicitudes por numero y procesarlas segun su prioridad.

## Requisitos

- JDK 17 o superior
- Git

## Compilar y ejecutar

Desde la raiz del proyecto, en PowerShell:

```powershell
javac -encoding UTF-8 -d out src\main\java\sistemacreditos\*.java
java -cp out sistemacreditos.Main
```

## Funcionalidades

- Registrar clientes y rechazar documentos repetidos.
- Crear solicitudes asociadas a clientes registrados y rechazar numeros de solicitud repetidos.
- Consultar una solicitud por su numero.
- Listar solicitudes y ver las solicitudes registradas mas recientemente.
- Procesar primero las solicitudes pendientes con mayor prioridad y registrar su decision.

Las prioridades van de 1 (mas baja) a 5 (mas alta). Si varias solicitudes tienen igual prioridad, se atienden por numero de solicitud, en orden ascendente.

## Colecciones del Java Collections Framework

| Interfaz e implementacion | Responsabilidad | Motivo |
| --- | --- | --- |
| `List<SolicitudCredito>` / `ArrayList` | Registro completo de solicitudes | Conserva el orden de registro y permite recorrer la coleccion para listados. |
| `Set<String>` / `HashSet` | Control de documentos registrados | Impide documentos duplicados y permite comprobar su existencia. |
| `Map<String, Cliente>` / `HashMap` | Clientes indexados por documento | Facilita localizar al cliente asociado a una solicitud. |
| `Map<String, SolicitudCredito>` / `HashMap` | Solicitudes indexadas por numero | Resuelve consultas directas por el identificador unico. |
| `Queue<SolicitudCredito>` / `PriorityQueue` | Solicitudes pendientes de evaluacion | Ordena el procesamiento por nivel de prioridad, no por llegada. |
| `Deque<SolicitudCredito>` / `ArrayDeque` | Historial de las diez solicitudes mas recientes | Permite agregar las nuevas al inicio y recorrer las recientes primero. |

Las colecciones se usan desde sus interfaces (`List`, `Set`, `Map`, `Queue` y `Deque`) y se instancian con implementaciones concretas del SDK. No se implementan listas, colas, conjuntos ni mapas manualmente.

## Modelo del dominio

- `Cliente`: documento unico, nombre y telefono.
- `SolicitudCredito`: numero unico, monto, tipo de credito, prioridad, estado y cliente asociado.
- `EstadoSolicitud`: estados de una solicitud: pendiente, en revision, aprobada o rechazada.

## Flujo de Git sugerido

El historial de este repositorio debe reflejar trabajo real. No se deben cambiar fechas ni crear commits para aparentar actividad en dias distintos. Para continuar desarrollando:

```powershell
git status
git add .
git commit -m "Describe el cambio realizado"
git push
```
