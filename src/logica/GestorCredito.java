package logica;

import modelo.Cliente;
import modelo.SolicitudCredito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
 */
public class GestorCredito {

    // Requisito 1: Colección principal para conservar el historial maestro en orden de llegada
    private final List<SolicitudCredito> listaGeneralSolicitudes;

    // Requisito 2: Control estricto de documentos únicos de clientes
    private final Set<String> documentosClientesUnicos;

    // Repositorio de entidades de clientes registrados sin duplicados
    private final Set<Cliente> clientesRegistrados;

    public GestorCredito() {
        this.listaGeneralSolicitudes = new ArrayList<>();
        this.documentosClientesUnicos = new HashSet<>();
        this.clientesRegistrados = new HashSet<>();
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
     * Radica una nueva solicitud de crédito en la lista general maestra.
     * 
     * @param solicitud Solicitud a radicar.
     * @return true si la solicitud fue registrada.
     */
    public boolean radicarSolicitud(SolicitudCredito solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula.");
        }

        // Si el cliente no está en el Set, se registra automáticamente asegurando integridad
        Cliente cliente = solicitud.getCliente();
        if (!this.documentosClientesUnicos.contains(cliente.getNumeroDocumento())) {
            this.documentosClientesUnicos.add(cliente.getNumeroDocumento());
            this.clientesRegistrados.add(cliente);
        }

        // Se almacena en la lista principal conservando el orden cronológico de ingreso
        return this.listaGeneralSolicitudes.add(solicitud);
    }

    /**
     * Retorna una vista de solo lectura de todas las solicitudes radicadas.
     * Protege el encapsulamiento para que componentes externos no alteren la colección interna.
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
