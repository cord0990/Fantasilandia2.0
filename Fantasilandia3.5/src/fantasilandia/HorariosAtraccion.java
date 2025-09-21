package fantasilandia;

import java.time.format.DateTimeParseException;
import java.time.LocalTime;

import Excepciones.HorarioMalFormateadoException;

/**
 * Representa un rango horario de una atracción: horaInicio - horaFin (HH:mm).
 */
public class HorariosAtraccion {
    private String horaInicio; // "HH:mm"
    private String horaFin;    // "HH:mm"

    public HorariosAtraccion(String horaInicio, String horaFin) throws HorarioMalFormateadoException {
        setHoraInicio(horaInicio);
        setHoraFin(horaFin);
        // valida orden lógico: inicio < fin
        validarOrden();
    }

    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }

    public void setHoraInicio(String horaInicio) throws HorarioMalFormateadoException {
        if (horaInicio == null) throw new HorarioMalFormateadoException("Hora inicio vacía");
        horaInicio = horaInicio.trim();
        validarFormatoHora(horaInicio);
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(String horaFin) throws HorarioMalFormateadoException {
        if (horaFin == null) throw new HorarioMalFormateadoException("Hora fin vacía");
        horaFin = horaFin.trim();
        validarFormatoHora(horaFin);
        this.horaFin = horaFin;
    }

    private void validarFormatoHora(String horario) throws HorarioMalFormateadoException {
        // formato HH:mm 00:00 - 23:59
        if (!horario.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            throw new HorarioMalFormateadoException("El horario no cumple el formato HH:mm: " + horario);
        }
    }

    private void validarOrden() throws HorarioMalFormateadoException {
        try {
            LocalTime inicio = LocalTime.parse(horaInicio);
            LocalTime fin = LocalTime.parse(horaFin);
            if (!inicio.isBefore(fin)) {
                throw new HorarioMalFormateadoException("La hora de inicio debe ser anterior a la hora de fin");
            }
        } catch (DateTimeParseException e) {
            throw new HorarioMalFormateadoException("Error parseando horas: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return horaInicio + " - " + horaFin;
    }
}
