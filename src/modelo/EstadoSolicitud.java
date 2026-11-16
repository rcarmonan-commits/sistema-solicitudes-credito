package modelo;

/**
 * Enumeración que representa las etapas del ciclo de vida de una solicitud de crédito.
 * 
 * GUION PEDAGÓGICO / SUSTENTACIÓN:
 * - ¿Qué es?: Controla el estado operativo de cada crédito radicado en el banco.
 * - ¿Por qué se usó?: Permite implementar consistencia transaccional entre colecciones. Por ejemplo,
 *   si una solicitud pasa a APROBADA, RECHAZADA o CANCELADA, el sistema sabe que debe retirarse
 *   o no volver a ser procesada en la PriorityQueue de créditos pendientes de evaluación.
 * - ¿Cómo funciona?: Define constantes con descripciones claras para presentación en reportes.
 */
public enum EstadoSolicitud {
    PENDIENTE("Pendiente de evaluación"),
    EN_EVALUACION("En estudio por comité de crédito"),
    APROBADA("Aprobada y lista para desembolso"),
    RECHAZADA("Rechazada por riesgo financiero"),
    CANCELADA("Cancelada por el solicitante");

    private final String descripcion;

    EstadoSolicitud(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return name() + " [" + descripcion + "]";
    }
}
