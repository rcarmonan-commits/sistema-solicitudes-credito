package logica;

/**
 * Excepción lanzada cuando se intenta registrar un cliente con un documento ya existente en el Set.
 */
public class ClienteDuplicadoException extends RuntimeException {
    public ClienteDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
