import java.util.ArrayList;
import java.util.List;

//Representa los bloques de horarios de una atraccion en un dia.
public class DiasActivosAnuales {
    private String fecha; //Fecha dia activo
    private List<BloqueDeAtraccion> bloques; // Lista de bloques de horario de la atracción para ese día

    //--Constructor--
    //inicializa la fecha y crea la lista vacía de bloques de horarios
    public DiasActivosAnuales(String fecha) {
        this.fecha = fecha;
        this.bloques = new ArrayList<>();
    }

    // ====== Getters ======
    public String getFecha() { return fecha; } // Devuelve la fecha
    public List<BloqueDeAtraccion> getBloques() { return bloques; } // Devuelve la lista de bloques de horario
    
    
    // ========= Setters ========
    	// ====== Métodos para agregar bloques ======
    
    // SIA1.6: sobrecarga para agregar bloque ya existente
    public void addBloque(BloqueDeAtraccion bloque) { this.bloques.add(bloque); }

    //Crear y agregar un bloque en una sola linea Utilizando Sobrecarga de métodos
    public BloqueDeAtraccion addBloque(String codigoBloque, Atraccion atr, HorariosAtraccion h) {
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
