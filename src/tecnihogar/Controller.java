package tecnihogar;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<OrdenDeServicio> ordenes;
    private List<Cuadrilla> cuadrillas;
    private List<Jornada> jornadas;
    private double ingresosPorRegistro;
    private double ingresosVisitasExtras;

    public Controller() {
        this.ordenes = new ArrayList<>();
        this.cuadrillas = new ArrayList<>();
        this.jornadas = new ArrayList<>();
        this.ingresosPorRegistro = 0.0;
        this.ingresosVisitasExtras = 0.0;
    }

    // ---------- Ordenes ----------
    public void registrarOrden(OrdenDeServicio orden) throws InvalidOperationException {
        // verificar código duplicado
        for (OrdenDeServicio o : ordenes) {
            if (o.getCodigo().equalsIgnoreCase(orden.getCodigo())) {
                throw new InvalidOperationException("Código de orden ya existe: " + orden.getCodigo());
            }
        }
        ordenes.add(orden);
        // ingresos al registrar: costo base (sin visitas extra)
        ingresosPorRegistro += orden.calcularCostoBase();
    }

    public OrdenDeServicio buscarOrden(String codigo) {
        return ordenes.stream()
                .filter(o -> o.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    public List<OrdenDeServicio> getOrdenes() {
        return ordenes;
    }

    // ---------- Cuadrillas ----------
    public void registrarCuadrilla(Cuadrilla cuadrilla) throws InvalidOperationException {
        for (Cuadrilla c : cuadrillas) {
            if (c.getCodigo().equalsIgnoreCase(cuadrilla.getCodigo())) {
                throw new InvalidOperationException("Código de cuadrilla ya existe: " + cuadrilla.getCodigo());
            }
        }
        cuadrillas.add(cuadrilla);
    }

    public Cuadrilla buscarCuadrilla(String codigo) {
        return cuadrillas.stream()
                .filter(c -> c.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    public List<Cuadrilla> getCuadrillas() {
        return cuadrillas;
    }

    // ---------- Jornadas ----------
    public void registrarJornada(Jornada jornada) throws InvalidOperationException {
        for (Jornada j : jornadas) {
            if (j.getCodigo().equalsIgnoreCase(jornada.getCodigo())) {
                throw new InvalidOperationException("Código de jornada ya existe: " + jornada.getCodigo());
            }
        }
        jornadas.add(jornada);
    }

    public Jornada buscarJornada(String codigo) {
        return jornadas.stream()
                .filter(j -> j.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    public List<Jornada> getJornadas() {
        return jornadas;
    }

    // ---------- Operaciones de Jornada ----------
    public void iniciarJornada(String codigoJornada) throws InvalidOperationException {
        Jornada j = buscarJornada(codigoJornada);
        if (j == null) {
            throw new InvalidOperationException("Jornada no encontrada: " + codigoJornada);
        }
        j.iniciarJornada();
    }

    public void cerrarJornada(String codigoJornada) throws InvalidOperationException {
        Jornada j = buscarJornada(codigoJornada);
        if (j == null) {
            throw new InvalidOperationException("Jornada no encontrada: " + codigoJornada);
        }
        j.cerrarJornada();
    }

    // ---------- Registro de Visitas ----------
    public void registrarVisita(String codigoOrden, boolean exitosa) throws InvalidOperationException {
        OrdenDeServicio o = buscarOrden(codigoOrden);
        if (o == null) {
            throw new InvalidOperationException("Orden no encontrada: " + codigoOrden);
        }
        int visitasPrevias = o.getCantVisitas();
        o.registrarVisita(exitosa);
        // si la visita fue exitosa y es una visita extra (más de la primera), contar ingreso extra
        if (exitosa && o.getCantVisitas() > 1) {
            ingresosVisitasExtras += 40.0;
        }
    }

    // ---------- Reportes ----------
    public String reporteIngresos() {
        double total = ingresosPorRegistro + ingresosVisitasExtras;
        return String.format("Ingresos por registro: Q%.2f\nIngresos por visitas extras: Q%.2f\nTOTAL: Q%.2f",
                ingresosPorRegistro, ingresosVisitasExtras, total);
    }
}

