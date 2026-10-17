package tecnihogar;

public class Cuadrilla {
    private String codigo;
    private double capacidadHorasMax;
    private boolean certificadaRefrigeracion;

    public Cuadrilla(String codigo, double capacidadHorasMax, boolean certificadaRefrigeracion) throws InvalidOperationException {
        if (codigo == null || codigo.isBlank()) {
            throw new InvalidOperationException("Código de cuadrilla no puede estar vacío.");
        }
        if (capacidadHorasMax <= 0) {
            throw new InvalidOperationException("Capacidad de horas debe ser mayor que 0.");
        }
        this.codigo = codigo;
        this.capacidadHorasMax = capacidadHorasMax;
        this.certificadaRefrigeracion = certificadaRefrigeracion;
    }

    public String getCodigo() { return codigo; }
    public double getCapacidadHorasMax() { return capacidadHorasMax; }
    public boolean isCertificadaRefrigeracion() { return certificadaRefrigeracion; }

    @Override
    public String toString() {
        return String.format("Cuadrilla %s - Capacidad: %.2f hrs - Certificada: %s",
                codigo, capacidadHorasMax, certificadaRefrigeracion ? "sí" : "no");
    }
}

