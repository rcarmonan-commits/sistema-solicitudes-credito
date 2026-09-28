package logica;

import modelo.Cliente;
import modelo.SolicitudCredito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Servicio central de lógica de negocio para la administración del sistema crediticio.
 * 
 * GUION PEDAGÓGICO / SUSTENTACIÓN:
 * - ¿Qué es?: Es la clase administradora que orquesta las colecciones del SDK de Java, separando
 *   la persistencia en memoria y las reglas de negocio de la interfaz de consola (Sección 15).
 * - Requisito 1 (Colección Principal - ArrayList):
 *   Almacena el historial general de solicitudes radicadas en el sistema.
 *   - ¿Por qué conserva orden?: Mantiene el orden de radicación temporal (secuencia de llegada).
 *   - ¿Por qué ArrayList?: Ofrece acceso indexado en tiempo constante O(1), bajo consumo de
 *     memoria por elemento frente a nodos enlazados, e iteraciones de alto rendimiento.
 * - Requisito 2 (Control de Duplicados - HashSet):
 *   Controla la unicidad de los clientes según su número de documento.
 *   - ¿Por qué HashSet?: Gracias a su dispersión por código hash (hashCode y equals), comprueba e
 *     inserta elementos únicos en O(1) promedio. Si se intenta ingresar un documento repetido,
 *     el método set.add() retorna false de forma inmediata, permitiendo rechazar la duplicidad.
 * - Requisito 3 (Búsqueda Directa por Clave - HashMap):
 *   Asocia cada código único 'numeroSolicitud' con su objeto SolicitudCredito.
 *   - ¿Por qué HashMap?: Permite localizar instantáneamente cualquier solicitud en tiempo O(1)
 *     promedio mediante su función hash interna, superando radicalmente la búsqueda lineal O(n)
 *     que implicaría recorrer elemento por elemento la lista general.
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

    public GestorCredito() {
        this.listaGeneralSolicitudes = new ArrayList<>();
        this.documentosClientesUnicos = new HashSet<>();
        this.clientesRegistrados = new HashSet<>();
        this.solicitudesPorNumero = new HashMap<>();
    }

    /**
     * Registra un nuevo cliente en el sistema verificando que su documento no exista previamente.
     * 
     * Demostración para sustentación (Requisito 2):
     * El método 'documentosClientesUnicos.add(documento)' verifica e inserta en un solo paso.
     * Si retorna false, significa que la tabla hash ya contenía ese documento, impidiendo duplicados.
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
     * Radica una nueva solicitud de crédito sincronizándola en la lista general y en el Map asociativo.
     * 
     * Demostración para sustentación (Requisitos 1, 2 y 3):
     * 1. Verifica en O(1) en el Map si el número de solicitud ya existe para evitar colisiones de clave.
     * 2. Si el cliente no existía, lo almacena en el Set de clientes únicos.
     * 3. Registra en el ArrayList maestro conservando el momento cronológico de llegada.
     * 4. Asocia la clave única al objeto en el HashMap (solicitudesPorNumero.put).
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

        // Verificación O(1) en el HashMap para evitar colisión de claves únicas
        if (this.solicitudesPorNumero.containsKey(codigo)) {
            throw new SolicitudDuplicadaException(
                    "Ya existe una solicitud radicada con el número [" + codigo + "].");
        }

        // Si el cliente no estaba en el Set, se registra asegurando integridad referencial
        Cliente cliente = solicitud.getCliente();
        if (!this.documentosClientesUnicos.contains(cliente.getNumeroDocumento())) {
            this.documentosClientesUnicos.add(cliente.getNumeroDocumento());
            this.clientesRegistrados.add(cliente);
        }

        // Sincronización en la lista general (Requisito 1)
        this.listaGeneralSolicitudes.add(solicitud);

        // Sincronización en el índice de búsqueda Map (Requisito 3)
        this.solicitudesPorNumero.put(codigo, solicitud);

        return true;
    }

    /**
     * Búsqueda directa por identificador en tiempo O(1) promedio utilizando el HashMap.
     * 
     * Pregunta de sustentación (Sección 9 y 25):
     * ¿Por qué es conceptualmente diferente a recorrer una lista?
     * En una lista secuencial, encontrar un elemento en la posición N requiere N comparaciones (O(n)).
     * En el HashMap, la clave pasa por una función de dispersión (hash) que calcula de forma
     * matemática directa el índice de la cubeta (bucket), accediendo al registro en tiempo O(1).
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
     * Comprueba en O(1) si existe una solicitud radicada con la clave dada.
     */
    public boolean existeSolicitudPorNumero(String numeroSolicitud) {
        if (numeroSolicitud == null) return false;
        return this.solicitudesPorNumero.containsKey(numeroSolicitud.trim().toUpperCase());
    }

    /**
     * Modifica atributos financieros de una solicitud existente manteniendo consistencia en el Map.
     * 
     * Demostración para sustentación (Sección 12):
     * Al modificar atributos del objeto, como el Map almacena referencias, el objeto consultado
     * por clave refleja de inmediato el cambio.
     */
    public void modificarCondicionesCredito(String numeroSolicitud, BigDecimal nuevoMonto, int nuevoPlazo) {
        SolicitudCredito solicitud = buscarSolicitudPorNumero(numeroSolicitud);
        solicitud.setMontoSolicitado(nuevoMonto);
        solicitud.setPlazoMeses(nuevoPlazo);
    }

    /**
     * Control de modificación de clave en Map (Requisito de rúbrica Sección 12):
     * Si por auditoría se actualiza el código de radicación, debe retirarse la asociación anterior
     * y registrarse la nueva para evitar inconsistencias o duplicidades huérfanas en el HashMap.
     */
    public void reasignarCodigoSolicitud(String codigoAnterior, String nuevoCodigo) {
        SolicitudCredito solicitud = buscarSolicitudPorNumero(codigoAnterior);
        String claveNueva = nuevoCodigo.trim().toUpperCase();

        if (this.solicitudesPorNumero.containsKey(claveNueva)) {
            throw new SolicitudDuplicadaException(
                    "El nuevo código [" + claveNueva + "] ya está ocupado por otra solicitud.");
        }

        // Retiro de la clave previa y registro con la nueva clave en el Map
        this.solicitudesPorNumero.remove(solicitud.getNumeroSolicitud());
        
        // Creamos la versión actualizada reemplazando la referencia en la lista
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
    }

    /**
     * Elimina una solicitud manteniendo la sincronización entre colecciones (Sección 12 y 13).
     * Se retira del HashMap y del ArrayList general.
     */
    public boolean eliminarSolicitud(String numeroSolicitud) {
        SolicitudCredito solicitud = buscarSolicitudPorNumero(numeroSolicitud);
        
        // Desvinculación del Map
        this.solicitudesPorNumero.remove(solicitud.getNumeroSolicitud());

        // Desvinculación de la lista general
        this.listaGeneralSolicitudes.remove(solicitud);

        return true;
    }

    /**
     * Retorna una vista de solo lectura de todas las solicitudes radicadas.
     */
    public List<SolicitudCredito> listarTodasLasSolicitudes() {
        return Collections.unmodifiableList(this.listaGeneralSolicitudes);
    }

    /**
     * Retorna el conjunto inmutable de clientes únicos registrados.
     */
    public Set<Cliente> listarClientesRegistrados() {
        return Collections.unmodifiableSet(this.clientesRegistrados);
    }

    /**
     * Retorna una vista inmutable del mapa de solicitudes por código.
     */
    public Map<String, SolicitudCredito> obtenerMapaSolicitudes() {
        return Collections.unmodifiableMap(this.solicitudesPorNumero);
    }

    /**
     * Verifica si un documento de cliente ya se encuentra en el HashSet.
     */
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
}
