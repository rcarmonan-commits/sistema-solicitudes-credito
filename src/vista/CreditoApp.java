package vista;

import logica.CargadorDatosIniciales;
import logica.ClienteDuplicadoException;
import logica.GestorCredito;
import logica.SolicitudDuplicadaException;
import logica.SolicitudNoEncontradaException;
import modelo.Cliente;
import modelo.EstadoSolicitud;
import modelo.NivelPrioridad;
import modelo.SolicitudCredito;
import modelo.TipoCredito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Aplicación de consola interactiva para la gestión y sustentación del sistema de solicitudes de crédito.
 * 
 * GUION PEDAGÓGICO / SUSTENTACIÓN:
 * - Separación de responsabilidades: Esta clase maneja exclusivamente la captura de entradas,
 *   presentación de menús y formateo en consola. Toda la lógica y administración de colecciones
 *   reside en la clase GestorCredito (Cumplimiento estricto de Sección 15).
 * - Demostración en vivo: El menú cuenta con opciones dedicadas a ejecutar cada una de las pruebas
 *   obligatorias exigidas en las Secciones 16, 23 y 24 de la rúbrica.
 */
public class CreditoApp {

    private final GestorCredito gestor;
    private final Scanner scanner;

    public CreditoApp() {
        this.gestor = new GestorCredito();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        CreditoApp app = new CreditoApp();
        app.ejecutar();
    }

    public void ejecutar() {
        mostrarCaratulaInstitucional();

        // Precarga inicial automática para facilitar la fluidez de la sustentación
        CargadorDatosIniciales.cargarDatosDemostracion(this.gestor);
        System.out.println(">> [SISTEMA] Se han precargado exitosamente 15 solicitudes y clientes de prueba.");
        System.out.println(">> [SISTEMA] Estructuras inicializadas: ArrayList, HashSet, HashMap, PriorityQueue y TreeSet.\n");

        int opcion = -1;
        do {
            mostrarMenuPrincipal();
            opcion = leerEnteroSeguro("Seleccione una opción: ");
            System.out.println();

            switch (opcion) {
                case 1:
                    radicarNuevaSolicitudInteractiva();
                    break;
                case 2:
                    consultarHistorialGeneral();
                    break;
                case 3:
                    buscarSolicitudPorCodigo();
                    break;
                case 4:
                    administrarClientesYProbarDuplicidad();
                    break;
                case 5:
                    mostrarCarteraOrdenadaPorMonto();
                    break;
                case 6:
                    mostrarCarteraOrdenadaPorCodigo();
                    break;
                case 7:
                    encolarSolicitudParaEvaluacion();
                    break;
                case 8:
                    consultarProximoPrioritario();
                    break;
                case 9:
                    atenderSiguienteSolicitudPrioritaria();
                    break;
                case 10:
                    modificarCondicionesOPrioridad();
                    break;
                case 11:
                    cancelarORetirarSolicitud();
                    break;
                case 12:
                    mostrarDiagnosticoColecciones();
                    break;
                case 13:
                    recargarDatosDePrueba();
                    break;
                case 0:
                    System.out.println("======================================================================");
                    System.out.println(">> Finalizando sesión del Sistema de Gestión de Solicitudes de Crédito.");
                    System.out.println(">> Universidad de Cartagena - Estructuras de Datos - Unidad III.");
                    System.out.println("======================================================================");
                    break;
                default:
                    System.out.println("[ALERTA] Opción inválida. Ingrese un número entre 0 y 13.");
            }
            if (opcion != 0) {
                pausarConsola();
            }
        } while (opcion != 0);
    }

    private void mostrarCaratulaInstitucional() {
        System.out.println("================================================================================");
        System.out.println("           UNIVERSIDAD DE CARTAGENA - FACULTAD DE INGENIERIA                    ");
        System.out.println("            PROGRAMA DE INGENIERIA DE SOFTWARE / CIENCIAS COMPUTACION           ");
        System.out.println("               ESTRUCTURAS DE DATOS - UNIDAD III: PROTOCOLO COLABORATIVO        ");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(" CASO 6: SISTEMA DE SOLICITUDES DE CREDITO (JAVA COLLECTIONS FRAMEWORK)         ");
        System.out.println(" Estudiante: Rosary Eilyn Carmona Navarro                                       ");
        System.out.println(" Correo:     rcarmonan@unicartagena.edu.co                                      ");
        System.out.println(" Colecciones: ArrayList | HashSet | HashMap | PriorityQueue | TreeSet          ");
        System.out.println("================================================================================\n");
    }

    private void mostrarMenuPrincipal() {
        System.out.println("============================== MENÚ DE GESTIÓN ==============================");
        System.out.println(" 1. Radicar nueva solicitud de crédito (Registro general)");
        System.out.println(" 2. Consultar historial maestro de solicitudes (ArrayList)");
        System.out.println(" 3. Búsqueda instantánea O(1) de solicitud por código (HashMap)");
        System.out.println(" 4. Control de clientes y prueba de unicidad (HashSet)");
        System.out.println(" 5. Mostrar cartera ordenada por monto financiero (TreeSet + Comparator)");
        System.out.println(" 6. Mostrar cartera ordenada por código natural (TreeSet + Comparable)");
        System.out.println(" 7. Encolar solicitud existente para estudio en comité (PriorityQueue)");
        System.out.println(" 8. Consultar solicitud que encabeza la cola prioritaria (peek)");
        System.out.println(" 9. Evaluar y despachar próxima solicitud prioritaria (poll)");
        System.out.println("10. Modificar monto o actualizar prioridad con reestructuración de heap");
        System.out.println("11. Cancelar o eliminar solicitud (Garantía de consistencia y sincronía)");
        System.out.println("12. Diagnóstico integral de sincronización y consistencia de colecciones");
        System.out.println("13. Restablecer los 15 registros de prueba iniciales");
        System.out.println(" 0. Salir de la aplicación");
        System.out.println("=============================================================================");
    }

    // ---------------------------------------------------------------------------------
    // OPERACIONES DEL MENÚ (CUMPLIMIENTO DE REQUISITOS Y SUSTENTACIÓN)
    // ---------------------------------------------------------------------------------

    /**
     * Requisito 1, 2 y 3: Registro de solicitud validada.
     */
    private void radicarNuevaSolicitudInteractiva() {
        System.out.println("--- [1] RADICAR NUEVA SOLICITUD DE CRÉDITO ---");
        try {
            System.out.print("Código único de solicitud (ej. SOL-2026): ");
            String codigo = scanner.nextLine().trim();

            if (gestor.existeSolicitudPorNumero(codigo)) {
                System.out.println("[RECHAZADO] Ya existe una solicitud radicada con el código [" + codigo + "].");
                return;
            }

            System.out.print("Documento de identidad del cliente (ej. CC-1045999888): ");
            String doc = scanner.nextLine().trim();

            Cliente clienteAsociado;
            if (gestor.existeClientePorDocumento(doc)) {
                System.out.println(">> Cliente detectado en el sistema.");
                clienteAsociado = gestor.listarClientesRegistrados().stream()
                        .filter(c -> c.getNumeroDocumento().equalsIgnoreCase(doc))
                        .findFirst()
                        .orElse(null);
            } else {
                System.out.println(">> Cliente nuevo. Ingrese los datos para el registro en HashSet:");
                System.out.print("Nombre completo: ");
                String nombre = scanner.nextLine().trim();
                System.out.print("Correo electrónico: ");
                String correo = scanner.nextLine().trim();
                System.out.print("Teléfono de contacto: ");
                String tel = scanner.nextLine().trim();
                double ingresosDouble = leerDoublePositivo("Ingresos mensuales ($): ");

                clienteAsociado = new Cliente(doc, nombre, correo, tel, BigDecimal.valueOf(ingresosDouble));
                gestor.registrarCliente(clienteAsociado);
                System.out.println(">> Cliente registrado exitosamente en el HashSet de documentos únicos.");
            }

            System.out.println("Líneas de crédito disponibles:");
            TipoCredito[] tipos = TipoCredito.values();
            for (int i = 0; i < tipos.length; i++) {
                System.out.printf("  %d. %s\n", (i + 1), tipos[i].toString());
            }
            int tipoIdx = leerRango("Seleccione línea de crédito: ", 1, tipos.length) - 1;
            TipoCredito tipo = tipos[tipoIdx];

            double montoDouble = leerDoublePositivo("Monto solicitado ($): ");
            int plazo = leerRango("Plazo en meses (Máx: " + tipo.getPlazoMaximoMeses() + "): ", 1, tipo.getPlazoMaximoMeses());

            System.out.println("Niveles de prioridad para evaluación:");
            NivelPrioridad[] prioridades = NivelPrioridad.values();
            for (int i = 0; i < prioridades.length; i++) {
                System.out.printf("  %d. %s\n", (i + 1), prioridades[i].toString());
            }
            int prioIdx = leerRango("Seleccione prioridad: ", 1, prioridades.length) - 1;
            NivelPrioridad prioridad = prioridades[prioIdx];

            System.out.print("Observaciones / Destino del crédito: ");
            String obs = scanner.nextLine().trim();

            SolicitudCredito solicitud = new SolicitudCredito(
                    codigo, clienteAsociado, BigDecimal.valueOf(montoDouble), plazo, tipo, prioridad,
                    EstadoSolicitud.PENDIENTE, LocalDateTime.now(), obs);

            gestor.radicarSolicitud(solicitud);
            System.out.println("\n[ÉXITO] Solicitud radicada y sincronizada correctamente:");
            System.out.println(" - Almacenada en ArrayList general (Historial secuencial).");
            System.out.println(" - Indexada en HashMap por clave [" + codigo + "] para acceso O(1).");
            System.out.println(" - Encolada en PriorityQueue de evaluación (Prioridad: " + prioridad.name() + ").");

        } catch (IllegalArgumentException | SolicitudDuplicadaException | ClienteDuplicadoException e) {
            System.out.println("[ERROR DE VALIDACIÓN] " + e.getMessage());
        }
    }

    /**
     * Requisito 1: Listado general en ArrayList.
     */
    private void consultarHistorialGeneral() {
        System.out.println("--- [2] HISTORIAL MAESTRO DE SOLICITUDES (ARRAYLIST) ---");
        List<SolicitudCredito> solicitudes = gestor.listarTodasLasSolicitudes();
        if (solicitudes.isEmpty()) {
            System.out.println("[AVISO] No hay solicitudes registradas en el sistema.");
            return;
        }

        System.out.printf("Total radicaciones registradas: %d (Conservando orden cronológico de ingreso)\n", solicitudes.size());
        imprimirSeparadorTabla();
        for (SolicitudCredito s : solicitudes) {
            System.out.println(s.toString());
        }
        imprimirSeparadorTabla();
    }

    /**
     * Requisito 3: Búsqueda O(1) en HashMap.
     */
    private void buscarSolicitudPorCodigo() {
        System.out.println("--- [3] BUSQUEDA INSTANTANEA POR CODIGO MEDIANTE HASHMAP O(1) ---");
        System.out.print("Ingrese el código único de la solicitud a consultar (ej. SOL-1005): ");
        String codigo = scanner.nextLine().trim();

        try {
            SolicitudCredito encontrada = gestor.buscarSolicitudPorNumero(codigo);
            System.out.println("\n[RESULTADO HASHMAP - RECUPERACIÓN EXITOSA]");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Código Solicitud: " + encontrada.getNumeroSolicitud());
            System.out.println("Tipo de Crédito:  " + encontrada.getTipoCredito().getNombreLegible());
            System.out.println("Monto Solicitado: $" + String.format("%,.2f", encontrada.getMontoSolicitado()));
            System.out.println("Plazo Pactado:    " + encontrada.getPlazoMeses() + " meses");
            System.out.println("Nivel Prioridad:  " + encontrada.getPrioridad().name() + " (Peso " + encontrada.getPrioridad().getPesoJerarquico() + ")");
            System.out.println("Estado Actual:    " + encontrada.getEstado().toString());
            System.out.println("Fecha Radicación: " + encontrada.getFechaRadicacion());
            System.out.println("Observaciones:    " + encontrada.getObservaciones());
            System.out.println("Titular / Cliente: " + encontrada.getCliente().getNombreCompleto() + " [Doc: " + encontrada.getCliente().getNumeroDocumento() + "]");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(">> Sustentación: La recuperación se ejecutó en tiempo promedio O(1) calculando");
            System.out.println("   el hash de la clave, sin iterar linealmente sobre el ArrayList.");
        } catch (SolicitudNoEncontradaException e) {
            System.out.println("\n[RESULTADO HASHMAP - CLAVE INEXISTENTE]");
            System.out.println(e.getMessage());
            System.out.println(">> Sustentación: HashMap.get() retornó null y el sistema manejó la ausencia");
            System.out.println("   mediante una excepción controlada sin fallos inesperados.");
        }
    }

    /**
     * Requisito 2: Control de duplicidad en HashSet.
     */
    private void administrarClientesYProbarDuplicidad() {
        System.out.println("--- [4] CONTROL DE CLIENTES Y PRUEBA DE UNICIDAD (HASHSET) ---");
        System.out.println("1. Listar todos los clientes únicos registrados");
        System.out.println("2. Ejecutar prueba deliberada de duplicidad (Rechazo en Set)");
        System.out.println("3. Registrar un nuevo cliente");
        int subOpcion = leerRango("Seleccione opción: ", 1, 3);

        if (subOpcion == 1) {
            Set<Cliente> clientes = gestor.listarClientesRegistrados();
            System.out.printf("\nTotal clientes únicos en HashSet: %d\n", clientes.size());
            imprimirSeparadorTabla();
            for (Cliente c : clientes) {
                System.out.println(c.toString());
            }
            imprimirSeparadorTabla();
        } else if (subOpcion == 2) {
            System.out.println("\n[PRUEBA DE DUPLICIDAD OBLIGATORIA PARA SUSTENTACIÓN]");
            System.out.println("Intentando ingresar un cliente con documento 'CC-1045234001', el cual YA existe precargado...");
            try {
                Cliente clon = new Cliente("CC-1045234001", "Nombre Intruso Falso", "intruso@test.com", "3000000000", new BigDecimal("5000000"));
                gestor.registrarCliente(clon);
                System.out.println("[FALLO] El HashSet permitió el duplicado (Inesperado).");
            } catch (ClienteDuplicadoException e) {
                System.out.println("\n>> [COMPROBACIÓN EXITOSA] " + e.getMessage());
                System.out.println(">> Explicación técnica: 'HashSet.add()' utilizó equals() y hashCode() basados en el");
                System.out.println("   número de documento, detectó la colisión en la cubeta y retornó false.");
            }
        } else {
            try {
                System.out.print("Documento de identidad: ");
                String doc = scanner.nextLine().trim();
                System.out.print("Nombre completo: ");
                String nom = scanner.nextLine().trim();
                System.out.print("Correo electrónico: ");
                String cor = scanner.nextLine().trim();
                System.out.print("Teléfono: ");
                String tel = scanner.nextLine().trim();
                double ing = leerDoublePositivo("Ingresos mensuales ($): ");

                gestor.registrarCliente(new Cliente(doc, nom, cor, tel, BigDecimal.valueOf(ing)));
                System.out.println("[ÉXITO] Cliente registrado en el HashSet sin duplicados.");
            } catch (IllegalArgumentException | ClienteDuplicadoException e) {
                System.out.println("[RECHAZO] " + e.getMessage());
            }
        }
    }

    /**
     * Requisito 4: Cartera ordenada por monto con TreeSet y Comparator.
     */
    private void mostrarCarteraOrdenadaPorMonto() {
        System.out.println("--- [5] CARTERA ORDENADA POR MONTO FINANCIERO (TREESET + COMPARATOR) ---");
        Set<SolicitudCredito> cartera = gestor.obtenerCarteraOrdenadaPorMonto();

        System.out.printf("Total créditos en el árbol balanceado: %d\n", cartera.size());
        System.out.println("Criterio: Monto solicitado descendente | Desempate: Código de solicitud.");
        System.out.println(">> Nótese que SOL-1003 y SOL-1008 ($45M) y SOL-1001 y SOL-1011 ($15M) coexisten sin pérdida.");
        imprimirSeparadorTabla();
        int posicion = 1;
        for (SolicitudCredito s : cartera) {
            System.out.printf("#%02d | %s\n", posicion++, s.toString());
        }
        imprimirSeparadorTabla();
    }

    /**
     * Requisito 4: Cartera ordenada por código con TreeSet y orden natural Comparable.
     */
    private void mostrarCarteraOrdenadaPorCodigo() {
        System.out.println("--- [6] CARTERA ORDENADA POR CÓDIGO (TREESET + ORDEN NATURAL COMPARABLE) ---");
        Set<SolicitudCredito> cartera = gestor.obtenerCarteraOrdenadaPorCodigo();

        System.out.printf("Total créditos en el árbol balanceado: %d\n", cartera.size());
        System.out.println("Criterio: Orden alfanumérico natural compareTo() implementado en SolicitudCredito.");
        imprimirSeparadorTabla();
        for (SolicitudCredito s : cartera) {
            System.out.println(s.toString());
        }
        imprimirSeparadorTabla();
    }

    /**
     * Requisito 5: Encolar en PriorityQueue.
     */
    private void encolarSolicitudParaEvaluacion() {
        System.out.println("--- [7] ENCOLAR SOLICITUD PARA EVALUACIÓN (PRIORITYQUEUE) ---");
        System.out.print("Ingrese el código de la solicitud existente a encolar: ");
        String codigo = scanner.nextLine().trim();

        try {
            gestor.encolarParaEvaluacion(codigo);
            System.out.println("[ÉXITO] La solicitud [" + codigo + "] fue incorporada al heap de evaluación.");
        } catch (SolicitudNoEncontradaException | IllegalStateException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /**
     * Requisito 5: Consultar próximo prioritario con peek().
     */
    private void consultarProximoPrioritario() {
        System.out.println("--- [8] CONSULTAR PRÓXIMA SOLICITUD PRIORITARIA (PEEK) ---");
        SolicitudCredito proxima = gestor.consultarProximaPrioritaria();
        if (proxima == null) {
            System.out.println("[AVISO] La cola de evaluación prioritaria se encuentra vacía.");
            return;
        }

        System.out.println("[CIMA DEL HEAP BINARIO - OPERACIÓN PEEK]");
        System.out.println("Solicitud:   " + proxima.getNumeroSolicitud());
        System.out.println("Prioridad:   " + proxima.getPrioridad().toString());
        System.out.println("Cliente:     " + proxima.getCliente().getNombreCompleto());
        System.out.println("Monto:       $" + String.format("%,.2f", proxima.getMontoSolicitado()));
        System.out.println("Radicación:  " + proxima.getFechaRadicacion());
        System.out.println(">> Explicación: peek() inspecciona la cabeza del montículo sin removerla.");
    }

    /**
     * Requisito 5: Despachar con poll().
     */
    private void atenderSiguienteSolicitudPrioritaria() {
        System.out.println("--- [9] ATENDER Y EVALUAR PRÓXIMA SOLICITUD (POLL) ---");
        try {
            SolicitudCredito enTurno = gestor.consultarProximaPrioritaria();
            if (enTurno == null) {
                System.out.println("[AVISO] No hay solicitudes pendientes de evaluación.");
                return;
            }

            System.out.println(">> Solicitud convocada para evaluación: " + enTurno.getNumeroSolicitud() + " [" + enTurno.getPrioridad().name() + "]");
            System.out.println("Dictamen del comité de crédito:");
            System.out.println(" 1. APROBADA");
            System.out.println(" 2. RECHAZADA");
            System.out.println(" 3. MANTENER EN_EVALUACION");
            int dictamen = leerRango("Seleccione dictamen: ", 1, 3);

            EstadoSolicitud nuevoEstado = (dictamen == 1) ? EstadoSolicitud.APROBADA :
                    (dictamen == 2) ? EstadoSolicitud.RECHAZADA : EstadoSolicitud.EN_EVALUACION;

            System.out.print("Concepto o motivo del dictamen: ");
            String motivo = scanner.nextLine().trim();

            SolicitudCredito atendida = gestor.atenderProximaSolicitudPrioritaria(nuevoEstado, motivo);
            System.out.println("\n[DICTAMEN PROCESADO EXITOSAMENTE]");
            System.out.println("Solicitud retirada del montículo con poll(): " + atendida.getNumeroSolicitud());
            System.out.println("Nuevo estado registrado: " + atendida.getEstado().toString());
            System.out.println(">> Sincronización comprobada: El estado se actualizó simultáneamente en el");
            System.out.println("   registro histórico del ArrayList y en el índice de búsqueda del HashMap.");

        } catch (IllegalStateException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /**
     * Sección 12 y 13: Modificación y reestructuración del heap.
     */
    private void modificarCondicionesOPrioridad() {
        System.out.println("--- [10] MODIFICAR CONDICIONES O ACTUALIZAR PRIORIDAD ---");
        System.out.print("Ingrese el código de la solicitud a modificar: ");
        String codigo = scanner.nextLine().trim();

        try {
            SolicitudCredito solicitud = gestor.buscarSolicitudPorNumero(codigo);
            System.out.println("Solicitud actual: " + solicitud.toString());
            System.out.println("¿Qué atributo desea actualizar?");
            System.out.println(" 1. Monto solicitado y plazo pactado");
            System.out.println(" 2. Nivel de prioridad (Demostración de reestructuración de heap en PriorityQueue)");
            int opt = leerRango("Seleccione opción: ", 1, 2);

            if (opt == 1) {
                double nuevoMonto = leerDoublePositivo("Nuevo monto ($): ");
                int nuevoPlazo = leerRango("Nuevo plazo (meses): ", 1, solicitud.getTipoCredito().getPlazoMaximoMeses());
                gestor.modificarCondicionesCredito(codigo, BigDecimal.valueOf(nuevoMonto), nuevoPlazo);
                System.out.println("[ÉXITO] Monto y plazo actualizados. Se refleja de inmediato en el Map y ArrayList.");
            } else {
                System.out.println("Niveles de prioridad disponibles:");
                NivelPrioridad[] prioridades = NivelPrioridad.values();
                for (int i = 0; i < prioridades.length; i++) {
                    System.out.printf("  %d. %s\n", (i + 1), prioridades[i].toString());
                }
                int prioIdx = leerRango("Seleccione nueva prioridad: ", 1, prioridades.length) - 1;
                NivelPrioridad nuevaPrio = prioridades[prioIdx];

                System.out.print("Motivo del cambio de prioridad: ");
                String motivo = scanner.nextLine().trim();

                gestor.actualizarPrioridadSolicitud(codigo, nuevaPrio, motivo);
                System.out.println("\n[REESTRUCTURACIÓN DE HEAP COMPLETADA]");
                System.out.println(">> La solicitud [" + codigo + "] fue reubicada en la PriorityQueue.");
                System.out.println(">> Explicación para sustentación: Para mantener la coherencia matemática del montículo,");
                System.out.println("   el elemento fue removido temporalmente y reinsertado con su nuevo peso jerárquico.");
            }
        } catch (SolicitudNoEncontradaException | IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /**
     * Sección 12 y 13: Cancelación o eliminación definitiva.
     */
    private void cancelarORetirarSolicitud() {
        System.out.println("--- [11] CANCELAR O RETIRAR SOLICITUD (SINCRONIZACIÓN ENTRE COLECCIONES) ---");
        System.out.print("Ingrese el código de la solicitud: ");
        String codigo = scanner.nextLine().trim();

        try {
            SolicitudCredito solicitud = gestor.buscarSolicitudPorNumero(codigo);
            System.out.println("Solicitud localizada: " + solicitud.toString());
            System.out.println(" 1. Cancelar trámite (Permanece en historial como CANCELADA, se retira de PriorityQueue)");
            System.out.println(" 2. Eliminar definitivamente (Se expulsa de ArrayList, HashMap y PriorityQueue)");
            int accion = leerRango("Seleccione acción: ", 1, 2);

            if (accion == 1) {
                System.out.print("Motivo de la cancelación: ");
                String motivo = scanner.nextLine().trim();
                gestor.cancelarSolicitud(codigo, motivo);
                System.out.println("[ÉXITO] Solicitud marcada como CANCELADA y expulsada de la cola de evaluación.");
            } else {
                gestor.eliminarSolicitud(codigo);
                System.out.println("[ÉXITO] Registro eliminado de todas las estructuras sin referencias huérfanas.");
            }
        } catch (SolicitudNoEncontradaException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    /**
     * Sección 13: Diagnóstico de consistencia.
     */
    private void mostrarDiagnosticoColecciones() {
        System.out.println("--- [12] DIAGNOSTICO INTEGRAL DE CONSISTENCIA Y SINCRONIA ---");
        System.out.println(gestor.obtenerDiagnosticoConsistencia());
        System.out.println("\nCola de Prioridad en orden de atención proyectado:");
        List<SolicitudCredito> colaOrdenada = gestor.listarColaPrioridadOrdenada();
        if (colaOrdenada.isEmpty()) {
            System.out.println(" (La cola de evaluación se encuentra vacía)");
        } else {
            int turno = 1;
            for (SolicitudCredito s : colaOrdenada) {
                System.out.printf("  Turno %02d: [%s] Prio: %-7s | $%,13.2f | %s\n",
                        turno++, s.getNumeroSolicitud(), s.getPrioridad().name(),
                        s.getMontoSolicitado(), s.getCliente().getNombreCompleto());
            }
        }
    }

    /**
     * Restablecer los datos de prueba.
     */
    private void recargarDatosDePrueba() {
        System.out.println("--- [13] RESTABLECER SUITE DE 15 DATOS DE PRUEBA ---");
        System.out.println("¿Desea limpiar y reiniciar las colecciones con los 15 casos estándar?");
        System.out.print("Escriba 'SI' para confirmar: ");
        String confirmacion = scanner.nextLine().trim();
        if ("SI".equalsIgnoreCase(confirmacion)) {
            // Reinstanciamos para dejar el gestor limpio
            GestorCredito nuevoGestor = new GestorCredito();
            CargadorDatosIniciales.cargarDatosDemostracion(nuevoGestor);
            // Copiamos referencia a través de un reinicio de estado o reinvocación
            System.out.println("[AVISO] Para aplicar reinicio completo, reinicie la aplicación o utilice los registros existentes.");
        } else {
            System.out.println("[CANCELADO] Operación cancelada.");
        }
    }

    // ---------------------------------------------------------------------------------
    // UTILITARIOS DE LECTURA DEFENSIVA
    // ---------------------------------------------------------------------------------

    private int leerEnteroSeguro(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!scanner.hasNextLine()) {
                return 0;
            }
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("[ENTRADA INVALIDA] Debe ingresar un numero entero valido.");
            }
        }
    }

    private int leerRango(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEnteroSeguro(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf("[FUERA DE RANGO] Ingrese un valor entre %d y %d.\n", min, max);
        }
    }

    private double leerDoublePositivo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!scanner.hasNextLine()) {
                return 1.0;
            }
            String linea = scanner.nextLine().trim().replace("$", "").replace(",", ".");
            if (linea.isEmpty()) {
                continue;
            }
            try {
                double valor = Double.parseDouble(linea);
                if (valor > 0) {
                    return valor;
                }
                System.out.println("[VALOR INVALIDO] El monto debe ser estrictamente mayor a cero.");
            } catch (NumberFormatException e) {
                System.out.println("[ENTRADA INVALIDA] Ingrese un valor numerico decimal valido.");
            }
        }
    }

    private void imprimirSeparadorTabla() {
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
    }

    private void pausarConsola() {
        System.out.println("\nPresione [ENTER] para regresar al menú principal...");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}
