package logica;

/**
 * Excepción lanzada cuando una búsqueda por identificador no encuentra registros asociados.
 */
public class SolicitudNoEncontradaException extends RuntimeException {
    public SolicitudNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
