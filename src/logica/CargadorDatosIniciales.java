package logica;

import modelo.Cliente;
import modelo.EstadoSolicitud;
import modelo.NivelPrioridad;
import modelo.SolicitudCredito;
import modelo.TipoCredito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Módulo de aprovisionamiento de datos iniciales para la sustentación y pruebas del sistema.
 * 
 * GUION PEDAGÓGICO / SUSTENTACIÓN (CUMPLIMIENTO SECCIÓN 17 Y 23):
 * - ¿Por qué se creó?: La rúbrica prohíbe realizar la sustentación con 2 o 3 registros improvisados.
 *   Exige como mínimo 15 objetos de prueba con situaciones preparadas deliberadamente.
 * - Casos de prueba preparados:
 *   1. Cola de Prioridad (PriorityQueue):
 *      Las solicitudes se radican en orden cronológico (SOL-1001 de baja prioridad ingresa primero,
 *      mientras que SOL-1004, SOL-1008, SOL-1010 y SOL-1015 ingresan después pero con prioridad URGENTE).
 *      Al ejecutar poll(), el sistema demuestra que atiende primero las URGENTES, superando el orden FIFO.
 *   2. Ordenamiento en Arbol (TreeSet):
 *      Existen empates deliberados de monto financiero:
 *      - SOL-1003 y SOL-1008 empatan en $45.000.000.
 *      - SOL-1001 y SOL-1011 empatan en $15.000.000.
 *      Esto demuestra en vivo que el ComparadorMontoCredito compuesto evita la pérdida de datos en TreeSet.
 *   3. Control de Duplicados (HashSet):
 *      Clientes con documentos 'CC-1045234001' a 'CC-1045234015' listos para probar el rechazo en vivo.
 *   4. Búsqueda Directa (HashMap):
 *      Claves como 'SOL-1005' para búsqueda O(1) exitosa y 'SOL-9999' para búsqueda inexistente.
 */
public class CargadorDatosIniciales {

    public static void cargarDatosDemostracion(GestorCredito gestor) {
        if (gestor == null) {
            throw new IllegalArgumentException("El gestor no puede ser nulo.");
        }

        LocalDateTime baseTime = LocalDateTime.of(2026, 8, 16, 8, 0);

        // Registro 1: Prioridad BAJA radicada primero en el tiempo
        Cliente c1 = new Cliente("CC-1045234001", "Juan Carlos Pérez Gómez", "jperez@bancocredito.co", "3001112233", new BigDecimal("4500000.00"));
        SolicitudCredito s1 = new SolicitudCredito("SOL-1001", c1, new BigDecimal("15000000.00"), 36, TipoCredito.LIBRE_INVERSION,
                NivelPrioridad.BAJA, EstadoSolicitud.PENDIENTE, baseTime.plusHours(1), "Compra de electrodomésticos");
        gestor.radicarSolicitud(s1);

        // Registro 2: Prioridad MEDIA
        Cliente c2 = new Cliente("CC-1045234002", "María Fernanda Castillo Luna", "mcastillo@bancocredito.co", "3102223344", new BigDecimal("8200000.00"));
        SolicitudCredito s2 = new SolicitudCredito("SOL-1002", c2, new BigDecimal("180000000.00"), 180, TipoCredito.HIPOTECARIO,
                NivelPrioridad.MEDIA, EstadoSolicitud.PENDIENTE, baseTime.plusHours(2), "Adquisición vivienda nueva en Manga");
        gestor.radicarSolicitud(s2);

        // Registro 3: Prioridad ALTA con monto de $45M (preparado para empate en TreeSet con SOL-1008)
        Cliente c3 = new Cliente("CC-1045234003", "Andrés Felipe Morales Ríos", "amorales@bancocredito.co", "3153334455", new BigDecimal("6000000.00"));
        SolicitudCredito s3 = new SolicitudCredito("SOL-1003", c3, new BigDecimal("45000000.00"), 48, TipoCredito.VEHICULO,
                NivelPrioridad.ALTA, EstadoSolicitud.PENDIENTE, baseTime.plusHours(3), "Vehículo familiar sedán");
        gestor.radicarSolicitud(s3);

        // Registro 4: Prioridad URGENTE radicada de cuarta (Debe salir antes que SOL-1001 a SOL-1003 en PriorityQueue)
        Cliente c4 = new Cliente("CC-1045234004", "Laura Sofía Mendoza Ortiz", "lmendoza@bancocredito.co", "3014445566", new BigDecimal("3500000.00"));
        SolicitudCredito s4 = new SolicitudCredito("SOL-1004", c4, new BigDecimal("8000000.00"), 24, TipoCredito.EDUCATIVO,
                NivelPrioridad.URGENTE, EstadoSolicitud.PENDIENTE, baseTime.plusHours(4), "Matrícula posgrado ingeniería de software");
        gestor.radicarSolicitud(s4);

        // Registro 5: Prioridad ALTA con el monto más elevado de la cartera ($250M)
        Cliente c5 = new Cliente("CC-1045234005", "Carlos Eduardo Herrera Vargas", "cherrera@bancocredito.co", "3125556677", new BigDecimal("18000000.00"));
        SolicitudCredito s5 = new SolicitudCredito("SOL-1005", c5, new BigDecimal("250000000.00"), 84, TipoCredito.COMERCIAL,
                NivelPrioridad.ALTA, EstadoSolicitud.PENDIENTE, baseTime.plusHours(5), "Expansión de planta de producción industrial");
        gestor.radicarSolicitud(s5);

        // Registro 6: Prioridad MEDIA
        Cliente c6 = new Cliente("CC-1045234006", "Valentina Díaz Romero", "vdiaz@bancocredito.co", "3186667788", new BigDecimal("7500000.00"));
        SolicitudCredito s6 = new SolicitudCredito("SOL-1006", c6, new BigDecimal("150000000.00"), 120, TipoCredito.HIPOTECARIO,
                NivelPrioridad.MEDIA, EstadoSolicitud.PENDIENTE, baseTime.plusHours(6), "Remodelación integral de inmueble");
        gestor.radicarSolicitud(s6);

        // Registro 7: Prioridad BAJA
        Cliente c7 = new Cliente("CC-1045234007", "Gabriel Antonio Torres Silva", "gtorres@bancocredito.co", "3047778899", new BigDecimal("5000000.00"));
        SolicitudCredito s7 = new SolicitudCredito("SOL-1007", c7, new BigDecimal("20000000.00"), 36, TipoCredito.LIBRE_INVERSION,
                NivelPrioridad.BAJA, EstadoSolicitud.PENDIENTE, baseTime.plusHours(7), "Consolidación de pasivos comerciales");
        gestor.radicarSolicitud(s7);

        // Registro 8: Prioridad URGENTE y monto de $45M (Empate exacto con SOL-1003 para TreeSet)
        Cliente c8 = new Cliente("CC-1045234008", "Daniela Marcela Ruiz Navarro", "druiz@bancocredito.co", "3168889900", new BigDecimal("9200000.00"));
        SolicitudCredito s8 = new SolicitudCredito("SOL-1008", c8, new BigDecimal("45000000.00"), 60, TipoCredito.VEHICULO,
                NivelPrioridad.URGENTE, EstadoSolicitud.PENDIENTE, baseTime.plusHours(8), "Adquisición camioneta de trabajo");
        gestor.radicarSolicitud(s8);

        // Registro 9: Prioridad ALTA
        Cliente c9 = new Cliente("CC-1045234009", "Sebastián Camilo Paternina Polo", "spaternina@bancocredito.co", "3149990011", new BigDecimal("12000000.00"));
        SolicitudCredito s9 = new SolicitudCredito("SOL-1009", c9, new BigDecimal("95000000.00"), 60, TipoCredito.COMERCIAL,
                NivelPrioridad.ALTA, EstadoSolicitud.PENDIENTE, baseTime.plusHours(9), "Capital de trabajo para temporada comercial");
        gestor.radicarSolicitud(s9);

        // Registro 10: Prioridad URGENTE
        Cliente c10 = new Cliente("CC-1045234010", "Paula Andrea Vergara Meza", "pvergara@bancocredito.co", "3201113355", new BigDecimal("4100000.00"));
        SolicitudCredito s10 = new SolicitudCredito("SOL-1010", c10, new BigDecimal("12000000.00"), 36, TipoCredito.EDUCATIVO,
                NivelPrioridad.URGENTE, EstadoSolicitud.PENDIENTE, baseTime.plusHours(10), "Especialización en ciberseguridad");
        gestor.radicarSolicitud(s10);

        // Registro 11: Prioridad MEDIA con monto de $15M (Empate exacto con SOL-1001 para TreeSet)
        Cliente c11 = new Cliente("CC-1045234011", "Ricardo José Salgado Benítez", "rsalgado@bancocredito.co", "3052224466", new BigDecimal("5500000.00"));
        SolicitudCredito s11 = new SolicitudCredito("SOL-1011", c11, new BigDecimal("15000000.00"), 24, TipoCredito.LIBRE_INVERSION,
                NivelPrioridad.MEDIA, EstadoSolicitud.PENDIENTE, baseTime.plusHours(11), "Adecuaciones locativas de vivienda");
        gestor.radicarSolicitud(s11);

        // Registro 12: Prioridad ALTA
        Cliente c12 = new Cliente("CC-1045234012", "Natalia Carolina Mercado Bello", "nmercado@bancocredito.co", "3173335577", new BigDecimal("14500000.00"));
        SolicitudCredito s12 = new SolicitudCredito("SOL-1012", c12, new BigDecimal("200000000.00"), 180, TipoCredito.HIPOTECARIO,
                NivelPrioridad.ALTA, EstadoSolicitud.PENDIENTE, baseTime.plusHours(12), "Compra apartamento sobre planos");
        gestor.radicarSolicitud(s12);

        // Registro 13: Prioridad BAJA
        Cliente c13 = new Cliente("CC-1045234013", "Jorge Luis Arrieta Puello", "jarrieta@bancocredito.co", "3024446688", new BigDecimal("4800000.00"));
        SolicitudCredito s13 = new SolicitudCredito("SOL-1013", c13, new BigDecimal("35000000.00"), 48, TipoCredito.VEHICULO,
                NivelPrioridad.BAJA, EstadoSolicitud.PENDIENTE, baseTime.plusHours(13), "Motocicleta de alta gama y equipamiento");
        gestor.radicarSolicitud(s13);

        // Registro 14: Prioridad MEDIA
        Cliente c14 = new Cliente("CC-1045234014", "Diana Patricia Cuadrado Buelvas", "dcuadrado@bancocredito.co", "3195557799", new BigDecimal("9800000.00"));
        SolicitudCredito s14 = new SolicitudCredito("SOL-1014", c14, new BigDecimal("80000000.00"), 72, TipoCredito.COMERCIAL,
                NivelPrioridad.MEDIA, EstadoSolicitud.PENDIENTE, baseTime.plusHours(14), "Maquinaria para consultorio odontológico");
        gestor.radicarSolicitud(s14);

        // Registro 15: Prioridad URGENTE
        Cliente c15 = new Cliente("CC-1045234015", "Mateo Alejandro Gómez Cardona", "mgomez@bancocredito.co", "3136668800", new BigDecimal("3900000.00"));
        SolicitudCredito s15 = new SolicitudCredito("SOL-1015", c15, new BigDecimal("10000000.00"), 36, TipoCredito.EDUCATIVO,
                NivelPrioridad.URGENTE, EstadoSolicitud.PENDIENTE, baseTime.plusHours(15), "Intercambio académico internacional");
        gestor.radicarSolicitud(s15);
    }
}
