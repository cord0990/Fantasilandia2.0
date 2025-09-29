package fantasilandia;
import java.time.LocalDate;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;

//SIA 2.7. Clase con Herencia, SobreEscritura mas abajo.
public class BloqueReservado extends BloqueDeAtraccion {
    private Cliente cliente;
    private LocalDate fechaReserva;
    private boolean pagado;

    //Constructoor
    public BloqueReservado(String id, String horario, Atraccion atraccion,
                           Cliente cliente, LocalDate fechaReserva, boolean pagado) throws FechaMalFormateadaException, BloqueMalFormateadoException, HorarioMalFormateadoException {
        super(id, horario, atraccion, null);
        this.cliente = cliente;
        this.fechaReserva = fechaReserva;
        this.pagado = pagado;
    }

    //SIA 1.3 Setters y Getters.
    public Cliente getCliente() {return cliente;}
    public LocalDate getFechaReserva() {return fechaReserva;}

    public boolean isPagado() {return pagado;}
    public void setPagado(boolean pagado) {this.pagado = pagado; }


}
