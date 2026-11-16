package logica;

import modelo.SolicitudCredito;
import java.util.Comparator;

/**
 * Comparador personalizado para gobernar el orden de extracción en la PriorityQueue.
 * 
 * GUION PEDAGÓGICO / SUSTENTACIÓN:
 * - ¿Qué es?: Es un Comparator que define qué solicitud de crédito debe evaluarse primero.
 * - ¿Por qué se diseñó así?: La PriorityQueue requiere una regla explícita de ordenamiento.
 *   En lugar del orden natural alfanumérico, aquí se aplica un criterio de prelación bancaria.
 * - Criterio Compuesto:
 *   1. Mayor nivel de prioridad primero (URGENTE [4] > ALTA [3] > MEDIA [2] > BAJA [1]).
 *   2. En caso de empate en prioridad: desempate FIFO por fecha de radicación más antigua
 *      (la solicitud que llegó primero tiene el turno preferente).
 *   3. En caso de idéntico timestamp: desempate estricto por el identificador 'numeroSolicitud'.
 */
public class ComparadorPrioridadEvaluacion implements Comparator<SolicitudCredito> {

    @Override
    public int compare(SolicitudCredito s1, SolicitudCredito s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return 1;
        if (s2 == null) return -1;

        // 1. Criterio primario: Prioridad descendente (mayor peso numérico sale primero)
        int comparacionPrioridad = Integer.compare(
                s2.getPrioridad().getPesoJerarquico(),
                s1.getPrioridad().getPesoJerarquico()
        );

        if (comparacionPrioridad != 0) {
            return comparacionPrioridad;
        }

        // 2. Criterio secundario (Desempate FIFO): Fecha de radicación más antigua primero
        int comparacionFecha = s1.getFechaRadicacion().compareTo(s2.getFechaRadicacion());
        if (comparacionFecha != 0) {
            return comparacionFecha;
        }

        // 3. Criterio terciario: Desempate por número de solicitud para estabilidad total
        return s1.getNumeroSolicitud().compareToIgnoreCase(s2.getNumeroSolicitud());
    }
}
