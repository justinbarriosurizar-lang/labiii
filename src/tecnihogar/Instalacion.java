package tecnihogar;

public class Instalacion extends OrdenDeServicio {
    private int puntos; // entero positivo

    public Instalacion(String codigo, String nombreCliente, String direccion, double horasEstimadas, Zona zona, int puntos) throws InvalidOperationException {
        super(codigo, nombreCliente, direccion, horasEstimadas, zona);
        if (puntos <= 0) {
            throw new InvalidOperationException("Puntos debe ser un entero positivo.");
        }
        if (horasEstimadas <= 0) {
            throw new InvalidOperationException("Horas estimadas deben ser mayores a 0.");
        }
        this.puntos = puntos;
    }

    public int getPuntos() { return puntos; }

    @Override
    public double calcularCostoBase() {
        double base = (zona == Zona.METROPOLITANA) ? 100.0 : 175.0;
        double horasFacturables = Math.max(horasEstimadas, puntos * 0.75);
        // cobrar Q90 por cada hora facturable o fracción
        int horasCobro = (int) Math.ceil(horasFacturables);
        return base + horasCobro * 90.0;
    }

    @Override
    public int calcularTiempoAtencion() {
        return (zona == Zona.METROPOLITANA) ? 3 : 5; // días
    }

    @Override
    public int getLimiteVisitas() {
        return 3;
    }

    @Override
    public boolean requiereRefrigeracion() {
        return false;
    }
}

