package tecnihogar;

import java.util.ArrayList;
import java.util.List;

public class Jornada {
    private String codigo;
    private Cuadrilla cuadrilla;
    private List<OrdenDeServicio> ordenesAsignadas;
    private EstadoJornada estado;

    public Jornada(String codigo, Cuadrilla cuadrilla) throws InvalidOperationException {
        if (codigo == null || codigo.isBlank()) {
            throw new InvalidOperationException("Código de jornada no puede estar vacío.");
        }
        if (cuadrilla == null) {
            throw new InvalidOperationException("Jornada debe tener una cuadrilla asignada.");
        }
        this.codigo = codigo;
        this.cuadrilla = cuadrilla;
        this.ordenesAsignadas = new ArrayList<>();
        this.estado = EstadoJornada.EN_PREPARACION;
    }

    public String getCodigo() { return codigo; }
    public Cuadrilla getCuadrilla() { return cuadrilla; }
    public EstadoJornada getEstado() { return estado; }
    public List<OrdenDeServicio> getOrdenesAsignadas() { return ordenesAsignadas; }

    // Agrega una orden a la jornada (validación de capacidad y certificación)
    public void agregarOrden(OrdenDeServicio orden) throws InvalidOperationException {
        if (estado != EstadoJornada.EN_PREPARACION) {
            throw new InvalidOperationException("Solo se pueden agregar órdenes mientras la jornada está en preparación.");
        }
        // Verificar capacidad horaria
        double totalHoras = ordenesAsignadas.stream()
                .mapToDouble(o -> o.getHorasEstimadas())
                .sum();
        totalHoras += orden.getHorasEstimadas();
        if (totalHoras > cuadrilla.getCapacidadHorasMax()) {
            throw new InvalidOperationException("La suma de horas estimadas supera la capacidad de la cuadrilla.");
        }
        // Verificar requerimiento de refrigeración
        if (orden.requiereRefrigeracion() && !cuadrilla.isCertificadaRefrigeracion()) {
            throw new InvalidOperationException("La cuadrilla no está certificada para refrigeración requerida por la orden.");
        }
        ordenesAsignadas.add(orden);
    }

    public void retirarOrden(OrdenDeServicio orden) throws InvalidOperationException {
        if (estado != EstadoJornada.EN_PREPARACION) {
            throw new InvalidOperationException("Solo se pueden retirar órdenes mientras la jornada está en preparación.");
        }
        if (!ordenesAsignadas.remove(orden)) {
            throw new InvalidOperationException("Orden no encontrada en la jornada.");
        }
    }

    // Iniciar la jornada: cambia estado de la jornada y de cada orden a EN_ATENCION, contabiliza visitas extra al iniciar (costo extra para visitas >1 se contabiliza al registrar visitas, no aquí)
    public void iniciarJornada() throws InvalidOperationException {
        if (estado != EstadoJornada.EN_PREPARACION) {
            throw new InvalidOperationException("La jornada ya está iniciada o cerrada.");
        }
        if (ordenesAsignadas.isEmpty()) {
            throw new InvalidOperationException("No hay órdenes asignadas para iniciar la jornada.");
        }
        estado = EstadoJornada.EN_PROCESO;
        for (OrdenDeServicio o : ordenesAsignadas) {
            o.estado = EstadoOrden.EN_ATENCION;
        }
    }

    // Cerrar la jornada: verifica que todas las órdenes estén COMPLETADA o CANCELADA
    public void cerrarJornada() throws InvalidOperationException {
        if (estado != EstadoJornada.EN_PROCESO) {
            throw new InvalidOperationException("Solo se puede cerrar una jornada en proceso.");
        }
        for (OrdenDeServicio o : ordenesAsignadas) {
            if (o.getEstado() != EstadoOrden.COMPLETADA && o.getEstado() != EstadoOrden.CANCELADA) {
                throw new InvalidOperationException("Todas las órdenes deben estar completadas o canceladas antes de cerrar la jornada.");
            }
        }
        estado = EstadoJornada.CERRADA;
    }

    @Override
    public String toString() {
        return String.format("Jornada %s - Cuadrilla %s - Estado %s - Órdenes %d",
                codigo, cuadrilla.getCodigo(), estado, ordenesAsignadas.size());
    }
}

