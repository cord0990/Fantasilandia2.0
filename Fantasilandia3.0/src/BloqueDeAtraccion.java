//importamos librerias
import java.util.ArrayList;
import java.util.List;

//Atributos que tendra cada bloque de horario de una atraccion (SIA 1.2)
public class BloqueDeAtraccion {
    private String fecha; // YYYY-MM-DD
    private String codigoBloque; // ej: B001
    private Atraccion atraccion;
    private HorariosAtraccion horario;
    private List<Cliente> clientesParticipantes; //lista dinamicas de clientes (comienza vacia)


    //Constructor
    public BloqueDeAtraccion(String fecha, String codigoBloque, Atraccion atraccion, HorariosAtraccion horario) {
        this.fecha = fecha;
        this.codigoBloque = codigoBloque;
        this.atraccion = atraccion;
        this.horario = horario;
        this.clientesParticipantes = new ArrayList<>(); //lista generica
    }

    //Getters - Accedemos a los atributos manteniendo el encapsulamiento(SIA 1.3)
    public String getFecha() { return fecha; }
    public String getCodigoBloque() { return codigoBloque; }
    public Atraccion getAtraccion() { return atraccion; }
    public HorariosAtraccion getHorario() { return horario; }
    public List<Cliente> getClientesParticipantes() { return new ArrayList<>(this.clientesParticipantes); }
    
    //setters(SIA 1.3)
    public void setFecha(String newFecha) { this.fecha = newFecha;}
    public void setCodigoBloque(String newCodigoBloque) { this.codigoBloque = newCodigoBloque;}
    public void setAtraccion(Atraccion newAtraccion) { this.atraccion = newAtraccion;}
    public void setHorario(HorariosAtraccion newHorario) { this.horario = newHorario;}
    public void setClientesParticipantes(ArrayList<Cliente> newClientesParticipantes) {this.clientesParticipantes = newClientesParticipantes;}
    

    //metodo para agregar clientes a la lista de participantes
    public void addCliente(Cliente c) {
        if (!clientesParticipantes.contains(c)) {
            clientesParticipantes.add(c);
        }
    }
    

    @Override
    public String toString() {
        return "Bloque " + codigoBloque +
                " [" + fecha + "] - " +
                atraccion.getNombre()
                + " | Horario: " + horario +
                " | Clientes: " + clientesParticipantes.size();
    }
}
