package sistemacreditos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ClienteTest {
    @Test
    void normalizaEspaciosEnLosDatosDelCliente() {
        Cliente cliente = new Cliente("  DOC-01 ", "  Ana Pérez  ", "  3001234567 ");

        assertEquals("DOC-01", cliente.getDocumento());
        assertEquals("Ana Pérez", cliente.getNombre());
        assertEquals("3001234567", cliente.getTelefono());
    }

    @Test
    void rechazaCamposObligatoriosVacios() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cliente(" ", "Ana", "3001234567"));
        assertThrows(IllegalArgumentException.class,
                () -> new Cliente("DOC-01", "", "3001234567"));
        assertThrows(IllegalArgumentException.class,
                () -> new Cliente("DOC-01", "Ana", null));
    }
}
