package Excepciones;

//Aparece cuando una atraccion (en su lista anidada de horarios), bloque u horarioAtraccion se encuentran mal formateados (hh:mm)
@SuppressWarnings("serial")
public class HorarioMalFormateadoException extends Exception {
    public HorarioMalFormateadoException(String mensaje) {
        super(mensaje);
    }
}