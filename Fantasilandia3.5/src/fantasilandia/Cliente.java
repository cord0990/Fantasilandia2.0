package fantasilandia;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import Excepciones.FechaMalFormateadaException;
import Excepciones.RutMalFormateadoException;

public class Cliente {
    private String nombre;
    private String rut;
    private String idCliente;
    private String fechaNacimiento; // yyyy-MM-dd

    // Constructor. SIA 2.9. Excepciones sobre Rut mal formateado y fecha mal formateada.
    public Cliente(String nombre, String rut, String idCliente, String fechaNacimiento)
            throws RutMalFormateadoException, FechaMalFormateadaException {
        setNombre(nombre);
        setRut(rut);
        setIdCliente(idCliente);
        setFechaNacimiento(fechaNacimiento);
    }

    // Getters / Setters. SIA 1.3
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = (nombre == null ? "" : nombre.trim()); }

    public String getRut() { return rut; }
    public void setRut(String rut) throws RutMalFormateadoException {
        if (rut == null) throw new RutMalFormateadoException("RUT vacío");
        rut = rut.trim();
        // formato "12345678-9" o "1234567-K"
        if (!rut.matches("\\d{7,8}-[\\dkK]")) {
            throw new RutMalFormateadoException("El RUT ingresado es inválido: " + rut);
        }
        this.rut = rut;
    }

    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) { this.idCliente = (idCliente == null ? "" : idCliente.trim()); }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) throws FechaMalFormateadaException {
        if (fechaNacimiento == null) throw new FechaMalFormateadaException("Fecha vacía");
        fechaNacimiento = fechaNacimiento.trim();
        validarFecha(fechaNacimiento); // lanza FechaMalFormateadaException si falla
        this.fechaNacimiento = fechaNacimiento;
    }

    //SIA 2.9 Validacion de fecha.
    private void validarFecha(String fecha) throws FechaMalFormateadaException {
        try {
            // espera ISO yyyy-MM-dd
            LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            throw new FechaMalFormateadaException("La fecha es inválida (debe ser yyyy-MM-dd): " + fecha);
        }
    }

    @Override
    public String toString() {
        return String.format("ID:%s RUT:%s Nombre:%s FechaNacimiento:%s", idCliente, rut, nombre, fechaNacimiento);
    }
}
