package modelo;

/**
 * Enumeración que define los niveles de prelación para la atención de solicitudes en la PriorityQueue.
 * 
 * GUION PEDAGÓGICO / SUSTENTACIÓN:
 * - ¿Qué es?: Modela la prioridad de evaluación asignada a cada solicitud de crédito.
 * - ¿Por qué se usó un Enum con peso?: La PriorityQueue requiere un criterio estricto para
 *   determinar qué elemento debe extraerse primero (poll). Al asociar un peso jerárquico numérico
 *   (URGENTE = 4, ALTA = 3, MEDIA = 2, BAJA = 1), se evita depender de ordenamientos alfabéticos
 *   accidentales y se facilita la lógica del Comparator.
 * - ¿Cómo funciona?: Una solicitud con peso 4 saldrá antes que una con peso 1, independientemente
 *   de cuál de las dos llegó primero a la cola.
 */
public enum NivelPrioridad {
    URGENTE(4, "Urgente (Trámite express / Emergencia)"),
    ALTA(3, "Alta (Cliente preferencial / Monto crítico)"),
    MEDIA(2, "Media (Trámite ordinario prioritario)"),
    BAJA(1, "Baja (Trámite estándar)");

    private final int pesoJerarquico;
    private final String descripcion;

    NivelPrioridad(int pesoJerarquico, String descripcion) {
        this.pesoJerarquico = pesoJerarquico;
        this.descripcion = descripcion;
    }

    public int getPesoJerarquico() {
        return pesoJerarquico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return name() + " (Nivel " + pesoJerarquico + " - " + descripcion + ")";
    }
}
