package logica;

/**
 * Excepción lanzada cuando se intenta registrar una solicitud con un código ya existente.
 */
public class SolicitudDuplicadaException extends RuntimeException {
    public SolicitudDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
