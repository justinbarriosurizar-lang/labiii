package tecnihogar;

public class InstalacionElectricaCertificada extends Instalacion {
    private double valorMateriales;

    public InstalacionElectricaCertificada(String codigo, String nombreCliente, String direccion, double horasEstimadas, Zona zona, int puntos, double valorMateriales) throws InvalidOperationException {
        super(codigo, nombreCliente, direccion, horasEstimadas, zona, puntos);
        if (valorMateriales < 0) {
            throw new InvalidOperationException("Valor de materiales no puede ser negativo.");
        }
        this.valorMateriales = valorMateriales;
    }

    @Override
    public double calcularCostoBase() {
        double baseInst = super.calcularCostoBase();
        double certificado = 120.0;
        double materialFee = valorMateriales * 0.05;
        return baseInst + certificado + materialFee;
    }

    @Override
    public int calcularTiempoAtencion() {
        // Tiempo de instalación normal + 2 días
        return super.calcularTiempoAtencion() + 2;
    }

    @Override
    public int getLimiteVisitas() {
        return 2; // límite específico para certificada
    }

    @Override
    public boolean requiereRefrigeracion() {
        return false;
    }
}

