package sistemacreditos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public final class SistemaCreditos {
    private static final int MAX_HISTORIAL = 10;

    private final List<SolicitudCredito> solicitudes = new ArrayList<>();
    private final Set<String> documentosRegistrados = new HashSet<>();
    private final Map<String, Cliente> clientesPorDocumento = new HashMap<>();
    private final Map<String, SolicitudCredito> solicitudesPorNumero = new HashMap<>();
    private final Queue<SolicitudCredito> colaEvaluacion = new PriorityQueue<>(
            Comparator.comparingInt(SolicitudCredito::getPrioridad)
                    .reversed()
                    .thenComparing(SolicitudCredito::getNumero));
    private final Deque<SolicitudCredito> historialReciente = new ArrayDeque<>();

    public void registrarCliente(Cliente cliente) {
        if (!documentosRegistrados.add(cliente.getDocumento())) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente con el documento " + cliente.getDocumento() + ".");
        }
        clientesPorDocumento.put(cliente.getDocumento(), cliente);
    }

    public void registrarSolicitud(SolicitudCredito solicitud) {
        if (solicitudesPorNumero.containsKey(solicitud.getNumero())) {
            throw new IllegalArgumentException(
                    "Ya existe una solicitud con el numero " + solicitud.getNumero() + ".");
        }
        Cliente clienteRegistrado = clientesPorDocumento.get(solicitud.getCliente().getDocumento());
        if (clienteRegistrado != solicitud.getCliente()) {
            throw new IllegalArgumentException(
                    "La solicitud debe estar asociada al objeto de un cliente registrado.");
        }

        solicitudes.add(solicitud);
        solicitudesPorNumero.put(solicitud.getNumero(), solicitud);
        colaEvaluacion.offer(solicitud);
        historialReciente.addFirst(solicitud);
        if (historialReciente.size() > MAX_HISTORIAL) {
            historialReciente.removeLast();
        }
    }

    public Optional<Cliente> buscarCliente(String documento) {
        return Optional.ofNullable(clientesPorDocumento.get(documento));
    }

    public Optional<SolicitudCredito> buscarSolicitudPorNumero(String numero) {
        return Optional.ofNullable(solicitudesPorNumero.get(numero));
    }

    public List<SolicitudCredito> obtenerSolicitudes() {
        return List.copyOf(solicitudes);
    }

    public List<SolicitudCredito> obtenerHistorialReciente() {
        return List.copyOf(historialReciente);
    }

    public Optional<SolicitudCredito> procesarSiguienteSolicitud() {
        SolicitudCredito siguiente = colaEvaluacion.poll();
        if (siguiente == null) {
            return Optional.empty();
        }
        siguiente.setEstado(EstadoSolicitud.EN_REVISION);
        return Optional.of(siguiente);
    }

    public void resolverSolicitud(String numero, EstadoSolicitud resultado) {
        if (resultado != EstadoSolicitud.APROBADA && resultado != EstadoSolicitud.RECHAZADA) {
            throw new IllegalArgumentException("La decision debe ser aprobada o rechazada.");
        }
        SolicitudCredito solicitud = solicitudesPorNumero.get(numero);
        if (solicitud == null) {
            throw new IllegalArgumentException("No se encontro la solicitud " + numero + ".");
        }
        if (solicitud.getEstado() != EstadoSolicitud.EN_REVISION) {
            throw new IllegalArgumentException(
                    "Solo se pueden resolver solicitudes que estan en revision.");
        }
        solicitud.setEstado(resultado);
    }
}
