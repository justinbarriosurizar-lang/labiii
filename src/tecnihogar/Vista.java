package tecnihogar;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Vista {
    private Scanner scanner;
    private Controller controller;

    public Vista(Controller controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opción");
            try {
                switch (opcion) {
                    case 1:
                        manejarRegistroOrden();
                        break;
                    case 2:
                        manejarAsignacionOrden();
                        break;
                    case 3:
                        manejarIniciarJornada();
                        break;
                    case 4:
                        manejarRegistroVisita();
                        break;
                    case 5:
                        manejarCerrarJornada();
                        break;
                    case 6:
                        System.out.println(controller.reporteIngresos());
                        break;
                    case 7:
                        listarOrdenes();
                        break;
                    case 8:
                        continuar = false;
                        System.out.println("Hasta luego.");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (InvalidOperationException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, intente de nuevo.");
                scanner.nextLine(); // limpiar buffer
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n=== Menú TecniHogar ===");
        System.out.println("1. Registrar nueva orden");
        System.out.println("2. Asignar orden a jornada");
        System.out.println("3. Iniciar jornada");
        System.out.println("4. Registrar visita a una orden");
        System.out.println("5. Cerrar jornada");
        System.out.println("6. Reporte de ingresos");
        System.out.println("7. Listar ordenes");
        System.out.println("8. Salir");
    }

    // ---------- Utilidades de lectura ----------
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje + ": ");
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje + ": ");
            try {
                double val = Double.parseDouble(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    private String leerString(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.nextLine().trim();
    }

    private boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (s/n): ");
            String resp = scanner.nextLine().trim().toLowerCase();
            if (resp.equals("s") || resp.equals("si")) return true;
            if (resp.equals("n") || resp.equals("no")) return false;
            System.out.println("Respuesta no válida, indique s o n.");
        }
    }

    // ---------- Manejo de opciones ----------
    private void manejarRegistroOrden() throws InvalidOperationException {
        System.out.println("Tipo de orden: 1-Diagnostico, 2-Instalacion, 3-InstalacionElectricaCertificada, 4-MantenimientoAireAcc");
        int tipo = leerEntero("Seleccione tipo");
        String codigo = leerString("Código");
        String cliente = leerString("Nombre del cliente");
        String direccion = leerString("Dirección");
        double horas = leerDouble("Horas estimadas");
        Zona zona = leerZona();
        OrdenDeServicio orden;
        switch (tipo) {
            case 1:
                orden = new Diagnostico(codigo, cliente, direccion, horas, zona);
                break;
            case 2:
                int puntos = leerEntero("Puntos (entero positivo)");
                orden = new Instalacion(codigo, cliente, direccion, horas, zona, puntos);
                break;
            case 3:
                int pts = leerEntero("Puntos (entero positivo)");
                double valorMat = leerDouble("Valor de materiales");
                orden = new InstalacionElectricaCertificada(codigo, cliente, direccion, horas, zona, pts, valorMat);
                break;
            case 4:
                int btu = leerEntero("BTU (9,000 - 60,000)");
                orden = new MantenimientoAireAcc(codigo, cliente, direccion, horas, zona, btu);
                break;
            default:
                System.out.println("Tipo inválido.");
                return;
        }
        controller.registrarOrden(orden);
        System.out.println("Orden registrada exitosamente.");
    }

    private Zona leerZona() {
        while (true) {
            String zonaStr = leerString("Zona (METROPOLITANA / PERIFERICA)").toUpperCase();
            try {
                return Zona.valueOf(zonaStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Zona no reconocida.");
            }
        }
    }

    private void manejarAsignacionOrden() throws InvalidOperationException {
        String codigoOrden = leerString("Código de la orden a asignar");
        OrdenDeServicio orden = controller.buscarOrden(codigoOrden);
        if (orden == null) {
            System.out.println("Orden no encontrada.");
            return;
        }
        String codigoJornada = leerString("Código de la jornada");
        Jornada jornada = controller.buscarJornada(codigoJornada);
        if (jornada == null) {
            System.out.println("Jornada no encontrada.");
            return;
        }
        jornada.agregarOrden(orden);
        System.out.println("Orden asignada a la jornada.");
    }

    private void manejarIniciarJornada() throws InvalidOperationException {
        String codigo = leerString("Código de jornada a iniciar");
        controller.iniciarJornada(codigo);
        System.out.println("Jornada iniciada.");
    }

    private void manejarRegistroVisita() throws InvalidOperationException {
        String codigoOrden = leerString("Código de orden para registrar visita");
        boolean exitosa = leerBoolean("Visita exitosa?");
        controller.registrarVisita(codigoOrden, exitosa);
        System.out.println("Visita registrada.");
    }

    private void manejarCerrarJornada() throws InvalidOperationException {
        String codigo = leerString("Código de jornada a cerrar");
        controller.cerrarJornada(codigo);
        System.out.println("Jornada cerrada.");
    }

    private void listarOrdenes() {
        System.out.println("=== Lista de Órdenes ===");
        for (OrdenDeServicio o : controller.getOrdenes()) {
            System.out.println(o);
        }
    }
}

