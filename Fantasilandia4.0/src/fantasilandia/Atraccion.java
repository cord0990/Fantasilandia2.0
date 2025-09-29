package fantasilandia;

import java.util.*;
//vemos que valores tendra la variable atraccion (SIA 1.2)

public class Atraccion {

    // Atributos privados: solo accesibles dentro de la clase
    private String nombre; 
    private String clasificacion; //clasificacion del juego mecanico
    private String codigoAtraccion;
    private boolean activa;
    private String descripcion;
    private List<HorariosAtraccion> horariosEstandares = new ArrayList<HorariosAtraccion>();


    // Constructor: sirve para inicializar los atributos de un objeto al crearlo
    public Atraccion(String nombre, String clasificacion,String codigoAtraccion, boolean activa,
                     String descripcion) {
        this.nombre = nombre;
        this.clasificacion = clasificacion;
        this.codigoAtraccion = codigoAtraccion;
        this.activa = activa;
        this.descripcion = descripcion;
    }

    //getter y setter del nombre juego mecanico (SIA 1.3)
    public String getNombre() {return this.nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    //getter y setter de la clasificacion juego mecanico (SIA 1.3)
    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }

    //getter y setter del codigo juego mecanico (SIA 1.3)
    public String getCodigoAtraccion() {
        return this.codigoAtraccion;
    }
    public void setCodigoAtraccion(String codigoAtraccion) {
        this.codigoAtraccion = codigoAtraccion;
    }

    //getter y setter del estado del juego mecanico (SIA 1.3)
    public boolean isActiva() {
        return this.activa;
    }
    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    //getter y setter de la descripcion del juego mecanico (SIA 1.3)
    public String getDescripcion() {
        return this.descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    //getter y setter de los horarios estandares.
    // En Atraccion.java
    public List<HorariosAtraccion> getHorariosEstandares() {
        if (this.horariosEstandares == null) {
            this.horariosEstandares = new ArrayList<>(); //Si no existe, lo crea, solo en caso que no se genere correctamente 
        }
        return this.horariosEstandares;
    }

    public void addHorario(HorariosAtraccion h) {
        getHorariosEstandares(); // asegura no-null
        // evita duplicados exactos inicio-fin
        boolean dup = this.horariosEstandares.stream() //Verifica si se encuentra duplicado el horario
            .anyMatch(x -> x.getHoraInicio().equals(h.getHoraInicio()) 
                        && x.getHoraFin().equals(h.getHoraFin()));
        if (!dup) {
            this.horariosEstandares.add(h);
        }
    }

    @Override
    public String toString() {
        return "Atracción: " + this.nombre +
                " | Clasificación: " + this.clasificacion +
                " | Código: " + this.codigoAtraccion +
                " | Activa: " + this.activa +
                " | " + this.descripcion;
    }
}
