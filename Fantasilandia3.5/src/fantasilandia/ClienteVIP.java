package fantasilandia;

import Excepciones.FechaMalFormateadaException;
import Excepciones.RutMalFormateadoException;

public class ClienteVIP extends Cliente {
    private String beneficio;

    public ClienteVIP(String nombre, String rut, String idCliente, String fechaNacimiento, String beneficio) throws RutMalFormateadoException, FechaMalFormateadaException {
        super(nombre, rut, idCliente, fechaNacimiento);
        this.beneficio = "20% de descuento por persona";
    }

    public String getBeneficio() { return beneficio; }

    // Sobrescritura: redefinimos el método toString()
    @Override
    public String toString() {
        return "Cliente VIP " + getNombre() + " (RUT: " + getRut() + ", Beneficio: " + beneficio + ")";
    }
}
