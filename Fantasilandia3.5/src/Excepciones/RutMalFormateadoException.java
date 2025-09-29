package Excepciones;

//aparece cuando el rut de cliente en las diversas instancias(ya sea en la lista de clientes o bloques) se encuentran mal formateados.
@SuppressWarnings("serial")
public class RutMalFormateadoException extends Exception {
    public RutMalFormateadoException(String mensaje) {
        super(mensaje);
    }
}
