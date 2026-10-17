package tecnihogar;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        try {
            // Cuadrillas
            Cuadrilla c1 = new Cuadrilla("C001", 40.0, true);   // certificada refrigeración
            Cuadrilla c2 = new Cuadrilla("C002", 35.0, false);  // no certificada
            Cuadrilla c3 = new Cuadrilla("C003", 30.0, false);
            controller.registrarCuadrilla(c1);
            controller.registrarCuadrilla(c2);
            controller.registrarCuadrilla(c3);

            // Órdenes pendientes (2 por categoría)
            // Diagnóstico
            OrdenDeServicio d1 = new Diagnostico("D001", "Cliente A", "Dirección X", 1.0, Zona.METROPOLITANA);
            OrdenDeServicio d2 = new Diagnostico("D002", "Cliente B", "Dirección Y", 1.5, Zona.PERIFERICA);
            controller.registrarOrden(d1);
            controller.registrarOrden(d2);
            // Instalación
            OrdenDeServicio i1 = new Instalacion("I001", "Cliente C", "Dirección Z", 2.0, Zona.METROPOLITANA, 4);
            OrdenDeServicio i2 = new Instalacion("I002", "Cliente D", "Dirección W", 3.5, Zona.PERIFERICA, 5);
            controller.registrarOrden(i1);
            controller.registrarOrden(i2);
            // Instalación certificada
            OrdenDeServicio ic1 = new InstalacionElectricaCertificada("IC001", "Cliente E", "Dirección V", 2.5, Zona.METROPOLITANA, 3, 200.0);
            OrdenDeServicio ic2 = new InstalacionElectricaCertificada("IC002", "Cliente F", "Dirección U", 3.0, Zona.PERIFERICA, 4, 350.0);
            controller.registrarOrden(ic1);
            controller.registrarOrden(ic2);
            // Mantenimiento A/C
            OrdenDeServicio m1 = new MantenimientoAireAcc("M001", "Cliente G", "Dirección T", 2.0, Zona.METROPOLITANA, 12000);
            OrdenDeServicio m2 = new MantenimientoAireAcc("M002", "Cliente H", "Dirección S", 1.5, Zona.METROPOLITANA, 15000);
            controller.registrarOrden(m1);
            controller.registrarOrden(m2);

            // Jornadas (vinculamos algunas órdenes)
            Jornada j1 = new Jornada("J001", c1);
            j1.agregarOrden(d1);
            j1.agregarOrden(i1);
            controller.registrarJornada(j1);

            Jornada j2 = new Jornada("J002", c2);
            j2.agregarOrden(d2);
            j2.agregarOrden(i2);
            controller.registrarJornada(j2);

            // Vista interactiva
            Vista vista = new Vista(controller);
            vista.iniciar();
        } catch (InvalidOperationException e) {
            System.out.println("Error de inicialización: " + e.getMessage());
        }
    }
}

