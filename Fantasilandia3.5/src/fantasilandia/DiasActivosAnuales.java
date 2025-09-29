package fantasilandia;

import java.util.ArrayList;
import java.util.List;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;

//Representa los bloques de horarios de una atraccion en un dia. (SIA 1.2)
public class DiasActivosAnuales {
    private String fecha; //Fecha dia activo
    private List<BloqueDeAtraccion> bloques; // Lista de bloques de horario de la atracción para ese día (SIA 1.4)

    //--Constructor--
    //inicializa la fecha y crea la lista vacía de bloques de horarios
    public DiasActivosAnuales(String fecha) {
        this.fecha = fecha;
        this.bloques = new ArrayList<>();
    }

    // ====== Getters ======(SIA 1.3)
    public String getFecha() { return fecha; } // Devuelve la fecha
    public List<BloqueDeAtraccion> getBloques() { return bloques; } // Devuelve la lista de bloques de horario
    
    
    // ========= Setters ========(SIA 1.3)
    	// ====== Métodos para agregar bloques ======
    
 // SIA1.6: sobrecarga para agregar bloque ya existente y SIA 2.9 ante excepciones de mal formateado de bloques
    public void addBloque(BloqueDeAtraccion bloque) throws BloqueMalFormateadoException {
        if (bloque == null) throw new BloqueMalFormateadoException("Bloque nulo");
        // verificar duplicado por código dentro del mismo día
        for (BloqueDeAtraccion b : this.bloques) {
            if (b.getCodigoBloque() != null && b.getCodigoBloque().equalsIgnoreCase(bloque.getCodigoBloque())) {
                throw new BloqueMalFormateadoException("Ya existe un bloque con código " + bloque.getCodigoBloque() + " para la fecha " + this.fecha);
            }
        }
        this.bloques.add(bloque);
    }

    // Crear y agregar un bloque en una sola linea (sobrecarga)
    public BloqueDeAtraccion addBloque(String codigoBloque, Atraccion atr, HorariosAtraccion h)
            throws BloqueMalFormateadoException, HorarioMalFormateadoException, FechaMalFormateadaException {
        // Validar duplicado antes de crear
        for (BloqueDeAtraccion b : this.bloques) {
            if (b.getCodigoBloque() != null && b.getCodigoBloque().equalsIgnoreCase(codigoBloque)) {
            	//SIA 2.9. Si el codigo ya existe, manda error
                throw new BloqueMalFormateadoException("Ya existe un bloque con código " + codigoBloque + " para la fecha " + this.fecha);
            }
        }
        BloqueDeAtraccion b = new BloqueDeAtraccion(this.fecha, codigoBloque, atr, h);
        this.bloques.add(b);
        return b; // Devuelve el bloque creado
    }



    // ====== Búsqueda de bloques por código ======
    public BloqueDeAtraccion buscarBloque(String codigoBloque) {
        //Aqui recorremos la lista.
        for (BloqueDeAtraccion b : bloques) {
            if (b.getCodigoBloque().equalsIgnoreCase(codigoBloque))
                return b;// Devuelve el bloque de horario si lo encuentra
        }
        return null; //Caso que no encuentre el bloque de horario
    }
    
    	//Setter de fecha, en caso de que se deba modificar
    public void setFecha(String newFecha) {
    	this.fecha = newFecha;
    }

    @Override
    // Sobrescribimos toString para mostrar la información del cliente
    public String toString() {
        return "Día activo: " + fecha +
                " | Bloques: " + bloques.size();
    }
}
