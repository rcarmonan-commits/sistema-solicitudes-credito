package sistemacreditos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SolicitudCreditoTest {
    private final Cliente cliente = new Cliente("DOC-01", "Ana Pérez", "3001234567");

    @Test
    void creaSolicitudPendienteConPrioridadValida() {
        SolicitudCredito solicitud = new SolicitudCredito(
                "SC-001", new BigDecimal("1500000"), "Libre inversión", 5, cliente);

        assertEquals("SC-001", solicitud.getNumero());
        assertEquals(new BigDecimal("1500000"), solicitud.getMontoSolicitado());
        assertEquals(5, solicitud.getPrioridad());
        assertEquals(EstadoSolicitud.PENDIENTE, solicitud.getEstado());
        assertEquals(cliente, solicitud.getCliente());
    }

    @Test
    void rechazaMontoNoPositivo() {
        assertThrows(IllegalArgumentException.class,
                () -> new SolicitudCredito(
                        "SC-001", BigDecimal.ZERO, "Libre inversión", 3, cliente));
        assertThrows(IllegalArgumentException.class,
                () -> new SolicitudCredito(
                        "SC-001", new BigDecimal("-1"), "Libre inversión", 3, cliente));
    }

    @Test
    void rechazaPrioridadFueraDelRangoPermitido() {
        assertThrows(IllegalArgumentException.class,
                () -> new SolicitudCredito(
                        "SC-001", BigDecimal.ONE, "Libre inversión", 0, cliente));
        assertThrows(IllegalArgumentException.class,
                () -> new SolicitudCredito(
                        "SC-001", BigDecimal.ONE, "Libre inversión", 6, cliente));
    }

    @Test
    void rechazaDatosObligatoriosAusentes() {
        assertThrows(IllegalArgumentException.class,
                () -> new SolicitudCredito(
                        " ", BigDecimal.ONE, "Libre inversión", 3, cliente));
        assertThrows(IllegalArgumentException.class,
                () -> new SolicitudCredito(
                        "SC-001", BigDecimal.ONE, " ", 3, cliente));
        assertThrows(IllegalArgumentException.class,
                () -> new SolicitudCredito(
                        "SC-001", BigDecimal.ONE, "Libre inversión", 3, null));
    }
}
