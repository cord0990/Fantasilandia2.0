package fantasilandia;
import java.time.LocalDate;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;

public class BloqueReservado extends BloqueDeAtraccion {
    private Cliente cliente;
    private LocalDate fechaReserva;
    private boolean pagado;

    public BloqueReservado(String id, String horario, Atraccion atraccion,
                           Cliente cliente, LocalDate fechaReserva, boolean pagado) throws FechaMalFormateadaException, BloqueMalFormateadoException, HorarioMalFormateadoException {
        super(id, horario, atraccion, null);
        this.cliente = cliente;
        this.fechaReserva = fechaReserva;
        this.pagado = pagado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    @Override
    public String toString() {
        return super.toString() + " | Reserva de: " + cliente.getNombre() +
               " | Fecha: " + fechaReserva +
               " | Pagado: " + (pagado ? "Sí" : "No");
    }
}
