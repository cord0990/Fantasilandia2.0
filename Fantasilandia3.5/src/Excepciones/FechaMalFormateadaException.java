package Excepciones;

//aparece cuando en clientes y bloques tienen fecha (yyyy-mm-dd) mal formateado
@SuppressWarnings("serial")
public class FechaMalFormateadaException extends Exception {
    public FechaMalFormateadaException(String mensaje) {
        super(mensaje);
    }
}