package tecnihogar;

public class Diagnostico extends OrdenDeServicio {
    public Diagnostico(String codigo, String nombreCliente, String direccion, double horasEstimadas, Zona zona) throws InvalidOperationException {
        super(codigo, nombreCliente, direccion, horasEstimadas, zona);
        if (horasEstimadas <= 0) {
            throw new InvalidOperationException("Horas estimadas deben ser mayores a 0.");
        }
        if (horasEstimadas > 1.5) {
            throw new InvalidOperationException("Diagnóstico no puede tener más de 1.5 horas estimadas.");
        }
    }

    @Override
    public double calcularCostoBase() {
        return (zona == Zona.METROPOLITANA) ? 150.0 : 225.0;
    }

    @Override
    public int calcularTiempoAtencion() {
        return (zona == Zona.METROPOLITANA) ? 1 : 2; // días
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

