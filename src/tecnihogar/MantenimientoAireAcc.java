package tecnihogar;

public class MantenimientoAireAcc extends OrdenDeServicio {
    private int btu; // entre 9,000 y 60,000

    public MantenimientoAireAcc(String codigo, String nombreCliente, String direccion, double horasEstimadas, Zona zona, int btu) throws InvalidOperationException {
        super(codigo, nombreCliente, direccion, horasEstimadas, zona);
        if (zona != Zona.METROPOLITANA) {
            throw new InvalidOperationException("Mantenimiento de aire acondicionado solo se acepta en zona METROPOLITANA.");
        }
        if (btu < 9000 || btu > 60000) {
            throw new InvalidOperationException("BTU debe estar entre 9,000 y 60,000.");
        }
        if (horasEstimadas <= 0) {
            throw new InvalidOperationException("Horas estimadas deben ser mayores a 0.");
        }
        this.btu = btu;
    }

    public int getBtu() { return btu; }

    @Override
    public double calcularCostoBase() {
        // Tarifa base metropolitana (asumimos 0) + Q110 * horasEstimadas + Q80 refrigerante
        return 110.0 * horasEstimadas + 80.0;
    }

    @Override
    public int calcularTiempoAtencion() {
        return 2; // días
    }

    @Override
    public int getLimiteVisitas() {
        return 1;
    }

    @Override
    public boolean requiereRefrigeracion() {
        return true;
    }
}

