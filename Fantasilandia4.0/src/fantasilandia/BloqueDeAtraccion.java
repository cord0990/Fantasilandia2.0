package fantasilandia;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;

/**
 * Bloque de atracción: fecha, código, atracción referenciada, horario y lista de clientes.
 *
 * Se añaden validaciones y lanzamiento de excepciones. El bloque tiene una capacidad opcional.
 */
public class BloqueDeAtraccion {
    private String fecha; // yyyy-MM-dd
    private String codigoBloque;
    private Atraccion atraccion;
    private HorariosAtraccion horario;
    private List<Cliente> clientesParticipantes = new ArrayList<>();
    private int capacidad = 20; // por defecto; se puede personalizar con los getters

    //Constructor; SIA 2.9. Excepciones de formato
    public BloqueDeAtraccion(String fecha, String codigoBloque, Atraccion atraccion, HorariosAtraccion horario)
            throws FechaMalFormateadaException, BloqueMalFormateadoException, HorarioMalFormateadoException {
        setFecha(fecha);
        setCodigoBloque(codigoBloque);
        setAtraccion(atraccion);
        setHorario(horario);
    }

    //SIA 1.3
    // Getters
    public String getFecha() { return fecha; }
    public String getCodigoBloque() { return codigoBloque; }
    public Atraccion getAtraccion() { return atraccion; }
    public HorariosAtraccion getHorario() { return horario; }
    public List<Cliente> getClientesParticipantes() { return clientesParticipantes; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = Math.max(1, capacidad); }

    // Setters con validaciones
    public void setFecha(String fecha) throws FechaMalFormateadaException {
        if (fecha == null) throw new FechaMalFormateadaException("Fecha vacía");
        validarFecha(fecha.trim());
        this.fecha = fecha.trim();
    }

    //Metodos
    public void setCodigoBloque(String codigoBloque) throws BloqueMalFormateadoException {
        if (codigoBloque == null) throw new BloqueMalFormateadoException("Código de bloque vacío");
        codigoBloque = codigoBloque.trim();
        if (codigoBloque.isEmpty()) throw new BloqueMalFormateadoException("Código de bloque vacío");
        this.codigoBloque = codigoBloque;
    }

    public void setAtraccion(Atraccion atraccion) throws BloqueMalFormateadoException {
        if (atraccion == null) throw new BloqueMalFormateadoException("Atracción nula para el bloque");
        this.atraccion = atraccion;
    }

    public void setHorario(HorariosAtraccion horario) throws HorarioMalFormateadoException {
        if (horario == null) throw new HorarioMalFormateadoException("Horario nulo para el bloque");
        // HorariosAtraccion ya valida formato/orden en su constructor/setters
        this.horario = horario;
    }

    // Agregar cliente con control de duplicados y capacidad
    public void addCliente(Cliente c) throws BloqueMalFormateadoException {
        if (c != null) {
            if (!this.clientesParticipantes.contains(c)) {
                if (this.clientesParticipantes.size() >= this.capacidad) {
                    throw new BloqueMalFormateadoException("Bloque " + this.codigoBloque + " ha alcanzado su capacidad (" + this.capacidad + ")");
                } else {
                    this.clientesParticipantes.add(c);
                }
            }
        }
    }

    // Eliminar cliente por RUT (útil para menús)
    public boolean removeClientePorRut(String rut) {
        return this.clientesParticipantes.removeIf((cc) -> cc.getRut().equals(rut));
    }

    private void validarFecha(String fecha) throws FechaMalFormateadaException {
        try {
            LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            throw new FechaMalFormateadaException("La fecha es inválida: " + fecha);
        }
    }

    //Se utiliza para comparar si dos bloques son iguales o no
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BloqueDeAtraccion)) return false;
        BloqueDeAtraccion other = (BloqueDeAtraccion) o;
        // Comparar por código de bloque (case-insensitive) y por fecha (ambos deben coincidir)
        if (this.codigoBloque == null || other.codigoBloque == null) return false;
        boolean codigoIgual = this.codigoBloque.equalsIgnoreCase(other.codigoBloque);
        boolean fechaIgual = (this.fecha == null ? other.fecha == null : this.fecha.equals(other.fecha));
        return codigoIgual && fechaIgual;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (codigoBloque == null ? 0 : codigoBloque.toLowerCase().hashCode());
        result = 31 * result + (fecha == null ? 0 : fecha.hashCode());
        return result;
    }


    @Override
    public String toString() {
        return String.format("Bloque[%s] %s %s (%d clientes)", codigoBloque, fecha, horario == null ? "NO-HORARIO" : horario.toString(), clientesParticipantes.size());
    }


}
