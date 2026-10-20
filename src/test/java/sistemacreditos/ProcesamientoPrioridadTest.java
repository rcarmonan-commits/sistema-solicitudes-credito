package sistemacreditos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProcesamientoPrioridadTest {
    private SistemaCreditos sistema;
    private Cliente cliente;

    @BeforeEach
    void prepararSistema() {
        sistema = new SistemaCreditos();
        cliente = new Cliente("DOC-01", "Ana Pérez", "3001234567");
        sistema.registrarCliente(cliente);
    }

    @Test
    void procesaPrimeroLaPrioridadMasAlta() {
        sistema.registrarSolicitud(crearSolicitud("SC-001", 2));
        sistema.registrarSolicitud(crearSolicitud("SC-002", 5));

        SolicitudCredito procesada = sistema.procesarSiguienteSolicitud().orElseThrow();

        assertEquals("SC-002", procesada.getNumero());
        assertEquals(EstadoSolicitud.EN_REVISION, procesada.getEstado());
    }

    @Test
    void desempataPorNumeroEnOrdenAscendente() {
        sistema.registrarSolicitud(crearSolicitud("SC-010", 4));
        sistema.registrarSolicitud(crearSolicitud("SC-002", 4));

        assertEquals("SC-002",
                sistema.procesarSiguienteSolicitud().orElseThrow().getNumero());
        assertEquals("SC-010",
                sistema.procesarSiguienteSolicitud().orElseThrow().getNumero());
    }

    @Test
    void procesaCadaSolicitudPendienteUnaSolaVez() {
        sistema.registrarSolicitud(crearSolicitud("SC-001", 1));

        assertTrue(sistema.procesarSiguienteSolicitud().isPresent());
        assertFalse(sistema.procesarSiguienteSolicitud().isPresent());
    }

    @Test
    void requiereRevisionAntesDeAprobarORechazar() {
        sistema.registrarSolicitud(crearSolicitud("SC-001", 3));

        assertThrows(IllegalArgumentException.class,
                () -> sistema.resolverSolicitud("SC-001", EstadoSolicitud.APROBADA));
        assertThrows(IllegalArgumentException.class,
                () -> sistema.resolverSolicitud("SC-001", EstadoSolicitud.PENDIENTE));
        assertThrows(IllegalArgumentException.class,
                () -> sistema.resolverSolicitud("NO-EXISTE", EstadoSolicitud.RECHAZADA));

        sistema.procesarSiguienteSolicitud();
        sistema.resolverSolicitud("SC-001", EstadoSolicitud.APROBADA);

        assertEquals(EstadoSolicitud.APROBADA,
                sistema.buscarSolicitudPorNumero("SC-001").orElseThrow().getEstado());
        assertThrows(IllegalArgumentException.class,
                () -> sistema.resolverSolicitud("SC-001", EstadoSolicitud.RECHAZADA));
    }

    private SolicitudCredito crearSolicitud(String numero, int prioridad) {
        return new SolicitudCredito(
                numero, new BigDecimal("1000"), "Personal", prioridad, cliente);
    }
}
