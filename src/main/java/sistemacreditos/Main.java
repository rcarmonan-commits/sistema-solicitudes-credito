package sistemacreditos;

import java.math.BigDecimal;
import java.util.Scanner;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        SistemaCreditos sistema = new SistemaCreditos();
        try (Scanner entrada = new Scanner(System.in)) {
            boolean continuar = true;
            while (continuar) {
                mostrarMenu();
                switch (entrada.nextLine().trim()) {
                    case "1" -> registrarCliente(entrada, sistema);
                    case "2" -> registrarSolicitud(entrada, sistema);
                    case "3" -> buscarSolicitud(entrada, sistema);
                    case "4" -> listarSolicitudes(sistema);
                    case "5" -> procesarSolicitud(entrada, sistema);
                    case "6" -> mostrarHistorial(sistema);
                    case "0" -> continuar = false;
                    default -> System.out.println("Opcion no valida.");
                }
            }
        }
        System.out.println("Sistema cerrado.");
    }

    private static void mostrarMenu() {
        System.out.println("""

                === SISTEMA DE SOLICITUDES DE CREDITO ===
                1. Registrar cliente
                2. Registrar solicitud de credito
                3. Consultar solicitud por numero
                4. Listar solicitudes
                5. Procesar siguiente solicitud por prioridad
                6. Ver las solicitudes mas recientes
                0. Salir
                Seleccione una opcion:""");
    }

    private static void registrarCliente(Scanner entrada, SistemaCreditos sistema) {
        try {
            System.out.print("Documento: ");
            String documento = entrada.nextLine();
            System.out.print("Nombre: ");
            String nombre = entrada.nextLine();
            System.out.print("Telefono: ");
            String telefono = entrada.nextLine();
            sistema.registrarCliente(new Cliente(documento, nombre, telefono));
            System.out.println("Cliente registrado.");
        } catch (IllegalArgumentException ex) {
            System.out.println("No se pudo registrar el cliente: " + ex.getMessage());
        }
    }

    private static void registrarSolicitud(Scanner entrada, SistemaCreditos sistema) {
        try {
            System.out.print("Documento del cliente: ");
            String documento = entrada.nextLine().trim();
            Cliente cliente = sistema.buscarCliente(documento).orElse(null);
            if (cliente == null) {
                System.out.println("No existe un cliente registrado con ese documento.");
                return;
            }

            System.out.print("Numero unico de solicitud: ");
            String numero = entrada.nextLine();
            System.out.print("Monto solicitado: ");
            BigDecimal monto = new BigDecimal(entrada.nextLine().trim());
            System.out.print("Tipo de credito: ");
            String tipo = entrada.nextLine();
            int prioridad = leerEntero(entrada, "Prioridad (1 a 5): ");

            SolicitudCredito solicitud = new SolicitudCredito(
                    numero, monto, tipo, prioridad, cliente);
            sistema.registrarSolicitud(solicitud);
            System.out.println("Solicitud registrada.");
        } catch (NumberFormatException ex) {
            System.out.println("El monto debe ser un numero valido.");
        } catch (IllegalArgumentException ex) {
            System.out.println("No se pudo registrar la solicitud: " + ex.getMessage());
        }
    }

    private static void buscarSolicitud(Scanner entrada, SistemaCreditos sistema) {
        System.out.print("Numero de solicitud: ");
        sistema.buscarSolicitudPorNumero(entrada.nextLine().trim())
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No se encontro la solicitud."));
    }

    private static void listarSolicitudes(SistemaCreditos sistema) {
        if (sistema.obtenerSolicitudes().isEmpty()) {
            System.out.println("No hay solicitudes registradas.");
            return;
        }
        sistema.obtenerSolicitudes().forEach(System.out::println);
    }

    private static void procesarSolicitud(Scanner entrada, SistemaCreditos sistema) {
        var solicitud = sistema.procesarSiguienteSolicitud();
        if (solicitud.isEmpty()) {
            System.out.println("No hay solicitudes pendientes de evaluacion.");
            return;
        }

        System.out.println("Solicitud seleccionada: " + solicitud.get());
        while (true) {
            System.out.print("Decision (A = aprobar, R = rechazar): ");
            String decision = entrada.nextLine().trim();
            if (decision.equalsIgnoreCase("A") || decision.equalsIgnoreCase("R")) {
                EstadoSolicitud resultado = decision.equalsIgnoreCase("A")
                        ? EstadoSolicitud.APROBADA
                        : EstadoSolicitud.RECHAZADA;
                sistema.resolverSolicitud(solicitud.get().getNumero(), resultado);
                System.out.println("Solicitud " + resultado.name().toLowerCase() + ".");
                return;
            }
            System.out.println("Ingrese A para aprobar o R para rechazar.");
        }
    }

    private static void mostrarHistorial(SistemaCreditos sistema) {
        if (sistema.obtenerHistorialReciente().isEmpty()) {
            System.out.println("El historial esta vacio.");
            return;
        }
        sistema.obtenerHistorialReciente().forEach(System.out::println);
    }

    private static int leerEntero(Scanner entrada, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(entrada.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }
}
