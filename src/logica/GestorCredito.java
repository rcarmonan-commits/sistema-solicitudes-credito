package logica;

import modelo.Cliente;
import modelo.EstadoSolicitud;
import modelo.SolicitudCredito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Servicio central de lógica de negocio para la administración del sistema crediticio.
 * 
 * GUION PEDAGÓGICO / SUSTENTACIÓN:
 * - ¿Qué es?: Es la clase administradora que orquesta las colecciones del SDK de Java, separando
 *   la persistencia en memoria y las reglas de negocio de la interfaz de consola (Sección 15).
 * - Requisito 1 (Colección Principal - ArrayList):
 *   Almacena el historial general de solicitudes radicadas en el sistema.
 * - Requisito 2 (Control de Duplicados - HashSet):
 *   Controla la unicidad de los clientes según su número de documento en O(1) promedio.
 * - Requisito 3 (Búsqueda Directa por Clave - HashMap):
 *   Asocia cada código único 'numeroSolicitud' con su objeto SolicitudCredito en O(1).
 * - Requisito 4 (Estructura con Orden Mantenido - TreeSet):
 *   Mantiene colecciones ordenadas mediante árboles rojo-negro (Red-Black Trees) garantizando
 *   costo temporal logarítmico O(log n) para inserciones, eliminaciones y búsquedas.
 *   - Demostración dual:
 *     1. Orden natural (Comparable): Ordena por 'numeroSolicitud'.
 *     2. Orden con Comparator compuesto (ComparadorMontoCredito): Ordena por monto financiero
 *        descendente con desempate obligatorio por código para evitar pérdida de registros.
 * - Requisito 5 (Procesamiento por Prioridad - PriorityQueue):
 *   Gestiona la cola de evaluación bancaria mediante un montículo binario (heap) gobernado
 *   por ComparadorPrioridadEvaluacion.
 */
public class GestorCredito {

    // Requisito 1: Colección principal para conservar el historial maestro en orden de llegada
    private final List<SolicitudCredito> listaGeneralSolicitudes;

    // Requisito 2: Control estricto de documentos únicos de clientes
    private final Set<String> documentosClientesUnicos;

    // Repositorio de entidades de clientes registrados sin duplicados
    private final Set<Cliente> clientesRegistrados;

    // Requisito 3: Indice asociativo para recuperacion instantanea O(1) por clave unica
    private final Map<String, SolicitudCredito> solicitudesPorNumero;

    // Requisito 5: Cola con prioridad gobernada por heap binario y Comparator
    private final PriorityQueue<SolicitudCredito> colaPrioridadEvaluacion;

    public GestorCredito() {
        this.listaGeneralSolicitudes = new ArrayList<>();
        this.documentosClientesUnicos = new HashSet<>();
        this.clientesRegistrados = new HashSet<>();
        this.solicitudesPorNumero = new HashMap<>();
        this.colaPrioridadEvaluacion = new PriorityQueue<>(new ComparadorPrioridadEvaluacion());
    }

    /**
     * Registra un nuevo cliente en el sistema verificando que su documento no exista previamente.
     * 
     * @param nuevoCliente Cliente a registrar.
     * @return true si el cliente fue registrado con éxito.
     * @throws ClienteDuplicadoException si el documento ya se encuentra registrado.
     */
    public boolean registrarCliente(Cliente nuevoCliente) {
        if (nuevoCliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }

        String documento = nuevoCliente.getNumeroDocumento();
        boolean agregado = this.documentosClientesUnicos.add(documento);

        if (!agregado) {
            throw new ClienteDuplicadoException(
                    "El cliente con documento [" + documento + "] ya se encuentra registrado en el sistema.");
        }

        this.clientesRegistrados.add(nuevoCliente);
        return true;
    }

    /**
     * Radica una nueva solicitud de crédito sincronizándola en la lista general, en el Map
     * y, si su estado inicial es PENDIENTE o EN_EVALUACION, en la PriorityQueue de evaluación.
     * 
     * @param solicitud Solicitud a radicar.
     * @return true si la solicitud fue registrada.
     * @throws SolicitudDuplicadaException si el código ya existe en el HashMap.
     */
    public boolean radicarSolicitud(SolicitudCredito solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula.");
        }

        String codigo = solicitud.getNumeroSolicitud();

        if (this.solicitudesPorNumero.containsKey(codigo)) {
            throw new SolicitudDuplicadaException(
                    "Ya existe una solicitud radicada con el número [" + codigo + "].");
        }

        Cliente cliente = solicitud.getCliente();
        if (!this.documentosClientesUnicos.contains(cliente.getNumeroDocumento())) {
            this.documentosClientesUnicos.add(cliente.getNumeroDocumento());
            this.clientesRegistrados.add(cliente);
        }

        this.listaGeneralSolicitudes.add(solicitud);
        this.solicitudesPorNumero.put(codigo, solicitud);

        if (solicitud.getEstado() == EstadoSolicitud.PENDIENTE || solicitud.getEstado() == EstadoSolicitud.EN_EVALUACION) {
            this.colaPrioridadEvaluacion.add(solicitud);
        }

        return true;
    }

    /**
     * Búsqueda directa por identificador en tiempo O(1) promedio utilizando el HashMap.
     * 
     * @param numeroSolicitud Código único a buscar.
     * @return Objeto SolicitudCredito encontrado.
     * @throws SolicitudNoEncontradaException si la clave no existe en el Map.
     */
    public SolicitudCredito buscarSolicitudPorNumero(String numeroSolicitud) {
        if (numeroSolicitud == null || numeroSolicitud.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de solicitud no puede ser vacío.");
        }

        String claveLimpia = numeroSolicitud.trim().toUpperCase();
        SolicitudCredito encontrada = this.solicitudesPorNumero.get(claveLimpia);

        if (encontrada == null) {
            throw new SolicitudNoEncontradaException(
                    "No se encontró ninguna solicitud de crédito asociada al código [" + claveLimpia + "].");
        }

        return encontrada;
    }

    /**
     * Requisito 4: Obtiene la cartera de solicitudes ordenada de forma permanente por monto financiero descendente.
     * 
     * Demostración para sustentación:
     * Utiliza un TreeSet respaldado por un árbol rojo-negro (Red-Black Tree) con ComparadorMontoCredito.
     * Garantiza inserción y ordenamiento en O(log n) resolviendo empates de monto mediante el código
     * para que ningún registro sea descartado.
     * 
     * @return Conjunto ordenado inmutable por monto descendente.
     */
    public Set<SolicitudCredito> obtenerCarteraOrdenadaPorMonto() {
        Set<SolicitudCredito> carteraOrdenada = new TreeSet<>(new ComparadorMontoCredito());
        carteraOrdenada.addAll(this.listaGeneralSolicitudes);
        return Collections.unmodifiableSet(carteraOrdenada);
    }

    /**
     * Requisito 4 (Variante Orden Natural): Obtiene las solicitudes ordenadas según su orden natural Comparable.
     * 
     * Demostración para sustentación:
     * En este caso no se pasa Comparator al constructor de TreeSet; la estructura utiliza el método
     * compareTo() implementado en SolicitudCredito (alfabético por numeroSolicitud).
     * 
     * @return Conjunto ordenado inmutable por código alfanumérico.
     */
    public Set<SolicitudCredito> obtenerCarteraOrdenadaPorCodigo() {
        Set<SolicitudCredito> carteraPorCodigo = new TreeSet<>(this.listaGeneralSolicitudes);
        return Collections.unmodifiableSet(carteraPorCodigo);
    }

    /**
     * Requisito 4 aplicado a Clientes: Retorna los clientes ordenados por documento mediante TreeSet.
     */
    public Set<Cliente> obtenerClientesOrdenadosPorDocumento() {
        Set<Cliente> clientesOrdenados = new TreeSet<>(this.clientesRegistrados);
        return Collections.unmodifiableSet(clientesOrdenados);
    }

    /**
     * Encola una solicitud existente en la PriorityQueue de evaluación crediticia.
     */
    public boolean encolarParaEvaluacion(String numeroSolicitud) {
        SolicitudCredito solicitud = buscarSolicitudPorNumero(numeroSolicitud);

        if (this.colaPrioridadEvaluacion.contains(solicitud)) {
            throw new IllegalStateException("La solicitud [" + numeroSolicitud + "] ya se encuentra en la cola de evaluación.");
        }

        solicitud.setEstado(EstadoSolicitud.EN_EVALUACION);
        return this.colaPrioridadEvaluacion.add(solicitud);
    }

    /**
     * Consulta la siguiente solicitud que encabeza la PriorityQueue sin retirarla (peek).
     */
    public SolicitudCredito consultarProximaPrioritaria() {
        return this.colaPrioridadEvaluacion.peek();
    }

    /**
     * Despacha y atiende la siguiente solicitud prioritaria retirándola del heap con poll().
     */
    public SolicitudCredito atenderProximaSolicitudPrioritaria(EstadoSolicitud nuevoEstado, String dictamenComite) {
        if (this.colaPrioridadEvaluacion.isEmpty()) {
            throw new IllegalStateException("No hay solicitudes pendientes en la cola de evaluación prioritaria.");
        }

        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo.");
        }

        SolicitudCredito atendida = this.colaPrioridadEvaluacion.poll();

        atendida.setEstado(nuevoEstado);
        if (dictamenComite != null && !dictamenComite.trim().isEmpty()) {
            atendida.setObservaciones(dictamenComite.trim());
        }

        return atendida;
    }

    /**
     * Comprueba en O(1) si existe una solicitud radicada con la clave dada.
     */
    public boolean existeSolicitudPorNumero(String numeroSolicitud) {
        if (numeroSolicitud == null) return false;
        return this.solicitudesPorNumero.containsKey(numeroSolicitud.trim().toUpperCase());
    }

    /**
     * Modifica atributos financieros de una solicitud existente manteniendo consistencia.
     */
    public void modificarCondicionesCredito(String numeroSolicitud, BigDecimal nuevoMonto, int nuevoPlazo) {
        SolicitudCredito solicitud = buscarSolicitudPorNumero(numeroSolicitud);
        solicitud.setMontoSolicitado(nuevoMonto);
        solicitud.setPlazoMeses(nuevoPlazo);
    }

    /**
     * Reasignación de código único controlando la clave en el Map y sincronizando la cola.
     */
    public void reasignarCodigoSolicitud(String codigoAnterior, String nuevoCodigo) {
        SolicitudCredito solicitud = buscarSolicitudPorNumero(codigoAnterior);
        String claveNueva = nuevoCodigo.trim().toUpperCase();

        if (this.solicitudesPorNumero.containsKey(claveNueva)) {
            throw new SolicitudDuplicadaException(
                    "El nuevo código [" + claveNueva + "] ya está ocupado por otra solicitud.");
        }

        boolean estabaEnCola = this.colaPrioridadEvaluacion.remove(solicitud);

        this.solicitudesPorNumero.remove(solicitud.getNumeroSolicitud());

        SolicitudCredito solicitudActualizada = new SolicitudCredito(
                claveNueva,
                solicitud.getCliente(),
                solicitud.getMontoSolicitado(),
                solicitud.getPlazoMeses(),
                solicitud.getTipoCredito(),
                solicitud.getPrioridad(),
                solicitud.getEstado(),
                solicitud.getFechaRadicacion(),
                solicitud.getObservaciones() + " (Código actualizado desde " + codigoAnterior + ")"
        );

        int indiceLista = this.listaGeneralSolicitudes.indexOf(solicitud);
        if (indiceLista >= 0) {
            this.listaGeneralSolicitudes.set(indiceLista, solicitudActualizada);
        }

        this.solicitudesPorNumero.put(claveNueva, solicitudActualizada);

        if (estabaEnCola) {
            this.colaPrioridadEvaluacion.add(solicitudActualizada);
        }
    }

    /**
     * Elimina una solicitud manteniendo sincronización estricta entre todas las colecciones (Sección 13).
     */
    public boolean eliminarSolicitud(String numeroSolicitud) {
        SolicitudCredito solicitud = buscarSolicitudPorNumero(numeroSolicitud);

        this.colaPrioridadEvaluacion.remove(solicitud);
        this.solicitudesPorNumero.remove(solicitud.getNumeroSolicitud());
        this.listaGeneralSolicitudes.remove(solicitud);

        return true;
    }

    /**
     * Retorna una lista con los elementos actuales de la PriorityQueue ordenados según su turno de atención.
     */
    public List<SolicitudCredito> listarColaPrioridadOrdenada() {
        PriorityQueue<SolicitudCredito> copiaTemporal = new PriorityQueue<>(this.colaPrioridadEvaluacion);
        List<SolicitudCredito> resultadoOrdenado = new ArrayList<>();
        while (!copiaTemporal.isEmpty()) {
            resultadoOrdenado.add(copiaTemporal.poll());
        }
        return resultadoOrdenado;
    }

    public List<SolicitudCredito> listarTodasLasSolicitudes() {
        return Collections.unmodifiableList(this.listaGeneralSolicitudes);
    }

    public Set<Cliente> listarClientesRegistrados() {
        return Collections.unmodifiableSet(this.clientesRegistrados);
    }

    public Map<String, SolicitudCredito> obtenerMapaSolicitudes() {
        return Collections.unmodifiableMap(this.solicitudesPorNumero);
    }

    public boolean existeClientePorDocumento(String documento) {
        if (documento == null) return false;
        return this.documentosClientesUnicos.contains(documento.trim().toUpperCase());
    }

    public int totalSolicitudesRegistradas() {
        return this.listaGeneralSolicitudes.size();
    }

    public int totalClientesRegistrados() {
        return this.clientesRegistrados.size();
    }

    public int totalSolicitudesEnColaPrioridad() {
        return this.colaPrioridadEvaluacion.size();
    }
}
