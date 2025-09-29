package Excepciones;

//Manda error si es que el bloque a crear o modificar se encuentra mal formateado o ya exista en datos o lista
@SuppressWarnings("serial")
public class BloqueMalFormateadoException extends Exception {


	public BloqueMalFormateadoException(String mensaje) {
        super(mensaje);
    }
}
