package sistemacreditos;

public final class Cliente {
    private final String documento;
    private final String nombre;
    private final String telefono;

    public Cliente(String documento, String nombre, String telefono) {
        this.documento = textoObligatorio(documento, "El documento");
        this.nombre = textoObligatorio(nombre, "El nombre");
        this.telefono = textoObligatorio(telefono, "El telefono");
    }

    private static String textoObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        return valor.trim();
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    @Override
    public String toString() {
        return nombre + " (documento: " + documento + ", telefono: " + telefono + ")";
    }
}
