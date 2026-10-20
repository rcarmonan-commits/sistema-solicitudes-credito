package sistemacreditos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SistemaCreditosRegistroTest {
    private SistemaCreditos sistema;
    private Cliente cliente;

    @BeforeEach
    void prepararSistema() {
        sistema = new SistemaCreditos();
        cliente = new Cliente("DOC-01", "Ana Pérez", "3001234567");
        sistema.registrarCliente(cliente);
    }

    @Test
    void evitaDocumentosDuplicadosYPermiteBuscarCliente() {
        assertTrue(sistema.buscarCliente("DOC-01").isPresent());

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> sistema.registrarCliente(
                        new Cliente("DOC-01", "Otra persona", "3000000000")));

        assertTrue(error.getMessage().contains("DOC-01"));
        assertEquals(cliente, sistema.buscarCliente("DOC-01").orElseThrow());
    }

    @Test
    void registraYBuscaSolicitudPorNumeroUnico() {
        SolicitudCredito solicitud = crearSolicitud("SC-001", 3);
        sistema.registrarSolicitud(solicitud);

        assertEquals(solicitud, sistema.buscarSolicitudPorNumero("SC-001").orElseThrow());
        assertEquals(1, sistema.obtenerSolicitudes().size());
        assertFalse(sistema.buscarSolicitudPorNumero("NO-EXISTE").isPresent());
    }

    @Test
    void rechazaNumeroDeSolicitudDuplicado() {
        sistema.registrarSolicitud(crearSolicitud("SC-001", 3));

        assertThrows(IllegalArgumentException.class,
                () -> sistema.registrarSolicitud(crearSolicitud("SC-001", 5)));
        assertEquals(1, sistema.obtenerSolicitudes().size());
    }

    @Test
    void rechazaSolicitudAsociadaAClienteNoRegistrado() {
        Cliente clienteNoRegistrado =
                new Cliente("DOC-01", "Ana Pérez", "3001234567");
        SolicitudCredito solicitud = new SolicitudCredito(
                "SC-001", BigDecimal.ONE, "Personal", 3, clienteNoRegistrado);

        assertThrows(IllegalArgumentException.class,
                () -> sistema.registrarSolicitud(solicitud));
        assertTrue(sistema.obtenerSolicitudes().isEmpty());
    }

    @Test
    void conservaSoloLasDiezSolicitudesMasRecientesEnElHistorial() {
        for (int numero = 1; numero <= 12; numero++) {
            sistema.registrarSolicitud(crearSolicitud("SC-" + numero, 1));
        }

        assertEquals(10, sistema.obtenerHistorialReciente().size());
        assertEquals("SC-12",
                sistema.obtenerHistorialReciente().get(0).getNumero());
        assertEquals("SC-3",
                sistema.obtenerHistorialReciente().get(9).getNumero());
        assertEquals(12, sistema.obtenerSolicitudes().size());
    }

    private SolicitudCredito crearSolicitud(String numero, int prioridad) {
        return new SolicitudCredito(
                numero, new BigDecimal("1000"), "Personal", prioridad, cliente);
    }
}
