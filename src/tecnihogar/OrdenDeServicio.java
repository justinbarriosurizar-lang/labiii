package tecnihogar;

import java.text.DecimalFormat;

public abstract class OrdenDeServicio {
    protected String codigo;
    protected String nombreCliente;
    protected String direccion;
    protected double horasEstimadas;
    protected Zona zona;
    protected EstadoOrden estado;
    protected int cantVisitas;

    public OrdenDeServicio(String codigo, String nombreCliente, String direccion, double horasEstimadas, Zona zona) {
        this.codigo = codigo;
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.horasEstimadas = horasEstimadas;
        this.zona = zona;
        this.estado = EstadoOrden.PENDIENTE;
        this.cantVisitas = 0;
    }

    // Getters y Setters (solo los necesarios)
    public String getCodigo() { return codigo; }
    public String getNombreCliente() { return nombreCliente; }
    public String getDireccion() { return direccion; }
    public double getHorasEstimadas() { return horasEstimadas; }
    public Zona getZona() { return zona; }
    public EstadoOrden getEstado() { return estado; }
    public int getCantVisitas() { return cantVisitas; }

    // Métodos abstractos que deben implementar las sub‑clases
    public abstract double calcularCostoBase();
    public abstract int calcularTiempoAtencion(); // días
    public abstract int getLimiteVisitas();
    public abstract boolean requiereRefrigeracion();

    // Cálculo del costo total (costo base + Q40 por visita extra)
    public double calcularCostoTotal() {
        double base = calcularCostoBase();
        int visitasExtras = Math.max(0, cantVisitas - 1);
        return base + visitasExtras * 40.0;
    }

    // Registro de una visita
    public void registrarVisita(boolean exitosa) {
        if (exitosa) {
            cantVisitas++;
            if (cantVisitas >= getLimiteVisitas()) {
                estado = EstadoOrden.COMPLETADA;
            } else {
                estado = EstadoOrden.EN_ATENCION;
            }
        } else {
            // visita fallida, se cancela la orden
            estado = EstadoOrden.CANCELADA;
        }
    }

    // Formateo de montos a 2 decimales
    protected String formatMonto(double monto) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(monto);
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s) - Estado: %s - Visitas: %d - Costo Total: Q%s",
                codigo, nombreCliente, zona, estado, cantVisitas, formatMonto(calcularCostoTotal()));
    }
}

