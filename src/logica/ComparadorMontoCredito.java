package logica;

import modelo.SolicitudCredito;
import java.util.Comparator;

/**
 * Comparador compuesto para mantener la cartera de créditos ordenada por monto en un TreeSet.
 * 
 * GUION PEDAGÓGICO / SUSTENTACIÓN (NEUTRALIZACIÓN DE CASCARITA TREESET):
 * - ¿Qué es?: Es un Comparator que ordena las solicitudes de mayor a menor monto financiero.
 * - ¿Por qué se diseñó compuesto?: En un TreeSet, si dos solicitudes tienen el mismo monto
 *   (ej. dos créditos de $30.000.000) y el comparador retorna 0, el árbol rojo-negro interpreta
 *   que son duplicados y descarta silenciosamente el segundo crédito.
 * - Solución de ingeniería:
 *   1. Criterio primario: Monto solicitado descendente (s2.compareTo(s1)).
 *   2. Desempate obligatorio: Por 'numeroSolicitud' ascendente.
 *   Esto garantiza que dos solicitudes distintas nunca retornen 0, manteniendo el 100%
 *   de las solicitudes en la estructura arbórea balanceada O(log n).
 */
public class ComparadorMontoCredito implements Comparator<SolicitudCredito> {

    @Override
    public int compare(SolicitudCredito s1, SolicitudCredito s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return 1;
        if (s2 == null) return -1;

        // 1. Criterio primario: Monto solicitado en orden descendente (mayor a menor)
        int comparacionMonto = s2.getMontoSolicitado().compareTo(s1.getMontoSolicitado());
        if (comparacionMonto != 0) {
            return comparacionMonto;
        }

        // 2. Desempate estricto: Por código alfanumérico de solicitud para evitar colisión en TreeSet
        return s1.getNumeroSolicitud().compareToIgnoreCase(s2.getNumeroSolicitud());
    }
}
