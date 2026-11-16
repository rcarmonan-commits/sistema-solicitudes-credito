package modelo;

/**
 * Enumeración que tipifica las distintas líneas de crédito ofrecidas por la entidad.
 * 
 * GUION PEDAGÓGICO / SUSTENTACIÓN:
 * - ¿Qué es?: Modela las categorías de financiamiento disponibles en el sistema bancario.
 * - ¿Por qué se usó un Enum?: Garantiza seguridad de tipos (type safety) en tiempo de compilación,
 *   evitando errores por cadenas arbitrarias y centralizando tasas de interés referenciales.
 * - ¿Cómo funciona?: Cada constante contiene atributos inmutables (nombre formal, tasa de interés
 *   anual referencial y plazo máximo permitido en meses).
 */
public enum TipoCredito {
    HIPOTECARIO("Crédito Hipotecario / Vivienda", 11.5, 240),
    VEHICULO("Crédito de Vehículo", 14.0, 72),
    LIBRE_INVERSION("Crédito de Libre Inversión", 18.5, 60),
    COMERCIAL("Crédito Empresarial / Comercial", 13.0, 120),
    EDUCATIVO("Crédito Educativo", 9.5, 48);

    private final String nombreLegible;
    private final double tasaAnualReferencial;
    private final int plazoMaximoMeses;

    TipoCredito(String nombreLegible, double tasaAnualReferencial, int plazoMaximoMeses) {
        this.nombreLegible = nombreLegible;
        this.tasaAnualReferencial = tasaAnualReferencial;
        this.plazoMaximoMeses = plazoMaximoMeses;
    }

    public String getNombreLegible() {
        return nombreLegible;
    }

    public double getTasaAnualReferencial() {
        return tasaAnualReferencial;
    }

    public int getPlazoMaximoMeses() {
        return plazoMaximoMeses;
    }

    @Override
    public String toString() {
        return nombreLegible + " (Tasa ref: " + tasaAnualReferencial + "% E.A., Plazo máx: " + plazoMaximoMeses + " meses)";
    }
}
