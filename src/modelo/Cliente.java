package modelo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidad de dominio que representa a un cliente titular o solicitante de crédito.
 * 
 * GUION PEDAGÓGICO / SUSTENTACIÓN:
 * - ¿Qué es?: Representa a la persona natural o jurídica que tramita solicitudes crediticias.
 * - ¿Por qué se diseñó así?: Se implementa encapsulamiento defensivo estricto. Cada atributo
 *   se valida en el constructor para impedir estados inválidos o nulos.
 * - Papel en las colecciones: El atributo 'numeroDocumento' actúa como clave de unicidad para el Set
 *   (evita registrar clientes duplicados) y forma parte del contrato de identidad.
 * - equals() y hashCode(): Basados exclusivamente en 'numeroDocumento', asegurando que dos instancias
 *   con el mismo documento sean tratadas como el mismo cliente en cualquier HashSet o HashMap.
 */
public class Cliente implements Comparable<Cliente> {

    private final String numeroDocumento;
    private String nombreCompleto;
    private String correoElectronico;
    private String telefono;
    private BigDecimal ingresosMensuales;

    public Cliente(String numeroDocumento, String nombreCompleto, String correoElectronico,
                   String telefono, BigDecimal ingresosMensuales) {
        validarCadenaNoVacia(numeroDocumento, "El número de documento no puede ser nulo ni vacío.");
        validarCadenaNoVacia(nombreCompleto, "El nombre completo no puede ser nulo ni vacío.");
        validarCadenaNoVacia(correoElectronico, "El correo electrónico no puede ser nulo ni vacío.");
        validarCadenaNoVacia(telefono, "El teléfono de contacto no puede ser nulo ni vacío.");
        validarIngresos(ingresosMensuales);

        this.numeroDocumento = numeroDocumento.trim().toUpperCase();
        this.nombreCompleto = nombreCompleto.trim();
        this.correoElectronico = correoElectronico.trim().toLowerCase();
        this.telefono = telefono.trim();
        this.ingresosMensuales = ingresosMensuales;
    }

    private static void validarCadenaNoVacia(String valor, String mensajeError) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private static void validarIngresos(BigDecimal ingresos) {
        if (ingresos == null || ingresos.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Los ingresos mensuales deben ser mayores a cero.");
        }
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        validarCadenaNoVacia(nombreCompleto, "El nombre completo no puede ser vacío.");
        this.nombreCompleto = nombreCompleto.trim();
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        validarCadenaNoVacia(correoElectronico, "El correo electrónico no puede ser vacío.");
        this.correoElectronico = correoElectronico.trim().toLowerCase();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        validarCadenaNoVacia(telefono, "El teléfono no puede ser vacío.");
        this.telefono = telefono.trim();
    }

    public BigDecimal getIngresosMensuales() {
        return ingresosMensuales;
    }

    public void setIngresosMensuales(BigDecimal ingresosMensuales) {
        validarIngresos(ingresosMensuales);
        this.ingresosMensuales = ingresosMensuales;
    }

    /**
     * Define igualdad de negocio basada en el documento de identidad.
     * En Java, dos objetos son iguales si sus números de documento coinciden.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(numeroDocumento, cliente.numeroDocumento);
    }

    /**
     * Coherencia contractual con equals(): si equals() compara numeroDocumento,
     * hashCode() debe calcularse sobre numeroDocumento para que las colecciones hash
     * ubiquen el objeto exactamente en la misma cubeta (bucket).
     */
    @Override
    public int hashCode() {
        return Objects.hash(numeroDocumento);
    }

    /**
     * Orden natural alfabético por documento para estructuras ordenadas como TreeSet.
     */
    @Override
    public int compareTo(Cliente otro) {
        if (otro == null) return 1;
        return this.numeroDocumento.compareToIgnoreCase(otro.numeroDocumento);
    }

    @Override
    public String toString() {
        return String.format("Doc: %-12s | Nombre: %-25s | Tel: %-10s | Ingresos: $%,.2f",
                numeroDocumento, nombreCompleto, telefono, ingresosMensuales);
    }
}
