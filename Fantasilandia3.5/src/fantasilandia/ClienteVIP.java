package fantasilandia;

import Excepciones.FechaMalFormateadaException;
import Excepciones.RutMalFormateadoException;

public class ClienteVIP extends Cliente {
    private String beneficio;
    private boolean activadorVip;

    /** Valor por defecto del beneficio si no se especifica. */
    private static final String BENEFICIO_DEFAULT = "20% de descuento por persona";

    // SIA 2.7: Clase con sobreescritura de métodos
    public ClienteVIP(String nombre, String rut, String idCliente, String fechaNacimiento, String beneficio)
            throws RutMalFormateadoException, FechaMalFormateadaException {
        super(nombre, rut, idCliente, fechaNacimiento);
        // Usa el parámetro si viene, si no deja el default:
        this.beneficio = (beneficio == null || beneficio.isBlank()) ? BENEFICIO_DEFAULT : beneficio.trim();
        this.activadorVip = true; // por defecto; configurable con setter
    }

    /** Constructor de conveniencia que usa el beneficio por defecto. */
    public ClienteVIP(String nombre, String rut, String idCliente, String fechaNacimiento)
            throws RutMalFormateadoException, FechaMalFormateadaException {
        this(nombre, rut, idCliente, fechaNacimiento, BENEFICIO_DEFAULT);
    }

    // Getters/Setters
    public String getBeneficio() { return beneficio; }
    public void setBeneficio(String newBeneficio) {
        this.beneficio = (newBeneficio == null || newBeneficio.isBlank())
                ? BENEFICIO_DEFAULT
                : newBeneficio.trim();
    }

    public boolean isVip() { return activadorVip; }
    public void setVip(boolean vip) { this.activadorVip = vip; }

    /** SIA 2.7: sobrescritura para reflejar condición VIP en listados/reportes. */
    @Override
    public String toString() {
        return super.toString()
                + " | VIP: " + (activadorVip ? "Sí" : "No")
                + " | Beneficio: " + beneficio;
    }

    // --- Utilidad opcional (no rompe compatibilidad con tu modelo) ---

    /**
     * Calcula el monto con descuento si el VIP está activo.
     * @param montoOriginal monto base
     * @return monto descontado (si VIP activo) o el original
     */
    public double aplicarDescuento(double montoOriginal) {
        if (!activadorVip) return montoOriginal;

        // Si el beneficio mantiene el 20%, aplica 0.20. Si después quieres
        // parametrizar el porcentaje desde el texto, cambia esta lógica.
        double porcentaje = 0.20;
        return Math.max(0.0, montoOriginal * (1.0 - porcentaje));
    }
}
