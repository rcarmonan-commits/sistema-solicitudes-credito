package modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Entidad principal que modela una solicitud de crédito radicada en el sistema.
 * 
 * GUION PEDAGÓGICO / SUSTENTACIÓN:
 * - ¿Qué es?: Modela la solicitud financiera con su identificador único, cliente asociado,
 *   monto solicitado, tipo de crédito, nivel de prioridad y estado en el ciclo de vida.
 * - ¿Por qué se diseñó con dos entidades relacionadas?: Cumple con la exigencia de modelado
 *   del dominio (Cliente y SolicitudCredito interconectados), evitando objetos planos o genéricos.
 * - Papel en las colecciones:
 *   1. ArrayList: Guarda el registro secuencial maestro de todas las solicitudes radicadas.
 *   2. HashMap: Su clave primaria es 'numeroSolicitud', habilitando búsquedas directas en O(1).
 *   3. PriorityQueue: La prioridad determina el turno de desembolso o comité evaluador.
 *   4. TreeSet: Permite vistas ordenadas por monto de mayor a menor con desempate por código.
 * - Contrato equals() y hashCode(): Basado estrictamente en 'numeroSolicitud'. Dos solicitudes
 *   son idénticas si y solo si comparten el mismo código único de radicación.
 */
public class SolicitudCredito implements Comparable<SolicitudCredito> {

    private final String numeroSolicitud;
    private final Cliente cliente;
    private BigDecimal montoSolicitado;
    private int plazoMeses;
    private final TipoCredito tipoCredito;
    private NivelPrioridad prioridad;
    private EstadoSolicitud estado;
    private final LocalDateTime fechaRadicacion;
    private String observaciones;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public SolicitudCredito(String numeroSolicitud, Cliente cliente, BigDecimal montoSolicitado,
                            int plazoMeses, TipoCredito tipoCredito, NivelPrioridad prioridad,
                            EstadoSolicitud estado, LocalDateTime fechaRadicacion, String observaciones) {
        validarCadenaNoVacia(numeroSolicitud, "El número de solicitud no puede ser nulo ni vacío.");
        if (cliente == null) {
            throw new IllegalArgumentException("La solicitud debe estar asociada a un cliente válido.");
        }
        validarMonto(montoSolicitado);
        if (tipoCredito == null) {
            throw new IllegalArgumentException("Debe especificarse un tipo de crédito válido.");
        }
        validarPlazo(plazoMeses, tipoCredito);
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad no puede ser nula.");
        }
        if (estado == null) {
            throw new IllegalArgumentException("El estado de la solicitud no puede ser nulo.");
        }

        this.numeroSolicitud = numeroSolicitud.trim().toUpperCase();
        this.cliente = cliente;
        this.montoSolicitado = montoSolicitado;
        this.tipoCredito = tipoCredito;
        this.plazoMeses = plazoMeses;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaRadicacion = (fechaRadicacion != null) ? fechaRadicacion : LocalDateTime.now();
        this.observaciones = (observaciones != null) ? observaciones.trim() : "Sin observaciones iniciales.";
    }

    private static void validarCadenaNoVacia(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    private static void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto solicitado debe ser estrictamente mayor a cero.");
        }
    }

    private static void validarPlazo(int plazo, TipoCredito tipo) {
        if (plazo <= 0 || plazo > tipo.getPlazoMaximoMeses()) {
            throw new IllegalArgumentException(String.format(
                    "El plazo (%d meses) excede el máximo permitido para %s (%d meses).",
                    plazo, tipo.getNombreLegible(), tipo.getPlazoMaximoMeses()));
        }
    }

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(BigDecimal montoSolicitado) {
        validarMonto(montoSolicitado);
        this.montoSolicitado = montoSolicitado;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        validarPlazo(plazoMeses, this.tipoCredito);
        this.plazoMeses = plazoMeses;
    }

    public TipoCredito getTipoCredito() {
        return tipoCredito;
    }

    public NivelPrioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(NivelPrioridad prioridad) {
        if (prioridad == null) {
            throw new IllegalArgumentException("La nueva prioridad no puede ser nula.");
        }
        this.prioridad = prioridad;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        this.estado = estado;
    }

    public LocalDateTime getFechaRadicacion() {
        return fechaRadicacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = (observaciones != null) ? observaciones.trim() : "";
    }

    /**
     * Dos solicitudes son consideradas iguales en el sistema si comparten el mismo 'numeroSolicitud'.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolicitudCredito that = (SolicitudCredito) o;
        return Objects.equals(numeroSolicitud, that.numeroSolicitud);
    }

    /**
     * Mantiene consistencia contractual con equals().
     */
    @Override
    public int hashCode() {
        return Objects.hash(numeroSolicitud);
    }

    /**
     * Orden natural por código alfanumérico de solicitud, garantizando consistencia con equals().
     * Para ordenamiento por montos o prioridades, se utilizan Comparators específicos en las colecciones.
     */
    @Override
    public int compareTo(SolicitudCredito otra) {
        if (otra == null) return 1;
        return this.numeroSolicitud.compareToIgnoreCase(otra.numeroSolicitud);
    }

    @Override
    public String toString() {
        return String.format("[%s] %-15s | Monto: $%,14.2f | Plazo: %2d m | Prio: %-7s | Estado: %-13s | Cliente: %s (%s)",
                numeroSolicitud,
                tipoCredito.name(),
                montoSolicitado,
                plazoMeses,
                prioridad.name(),
                estado.name(),
                cliente.getNombreCompleto(),
                cliente.getNumeroDocumento());
    }
}
