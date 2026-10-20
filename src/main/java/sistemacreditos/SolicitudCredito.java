package sistemacreditos;

import java.math.BigDecimal;

public final class SolicitudCredito {
    private final String numero;
    private final BigDecimal montoSolicitado;
    private final String tipoCredito;
    private final int prioridad;
    private final Cliente cliente;
    private EstadoSolicitud estado;

    public SolicitudCredito(
            String numero,
            BigDecimal montoSolicitado,
            String tipoCredito,
            int prioridad,
            Cliente cliente) {
        this.numero = textoObligatorio(numero, "El numero de solicitud");
        if (montoSolicitado == null || montoSolicitado.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        this.montoSolicitado = montoSolicitado;
        this.tipoCredito = textoObligatorio(tipoCredito, "El tipo de credito");
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5.");
        }
        this.prioridad = prioridad;
        if (cliente == null) {
            throw new IllegalArgumentException("La solicitud debe tener un cliente.");
        }
        this.cliente = cliente;
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    private static String textoObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        return valor.trim();
    }

    public String getNumero() {
        return numero;
    }

    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }

    public String getTipoCredito() {
        return tipoCredito;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Solicitud " + numero
                + " | Cliente: " + cliente.getNombre()
                + " | Monto: $" + montoSolicitado.toPlainString()
                + " | Tipo: " + tipoCredito
                + " | Prioridad: " + prioridad
                + " | Estado: " + estado;
    }
}
