import java.util.*;
import java.util.ArrayList;
import java.util.List;

//Clase principal que administrara nuestro paeque de atracciones.
public class Fantasilandia {
    // ====== Listas principales ======
    private List<Cliente> clientes = new ArrayList<>(); //Guardamos los clientes
    private List<Atraccion> atracciones = new ArrayList<>(); //Guardamos las atracciones

    //==Uso de mapas para una Busqueda Rapida== (SIA1.7)
    private Map<String, Cliente> clientesPorRut = new HashMap<>(); // Buscar cliente por RUT
    private Map<String, Atraccion> atrPorCodigo = new HashMap<>(); // Buscar atracción por su código
    private Map<String, DiasActivosAnuales> diasPorFecha = new HashMap<>(); // Guardar los días activos (con bloques y clientes)

    //====== Constructor con datos de ejemplo ====== (SIA1.4)
    public Fantasilandia() {
    	 // Clientes de ejemplo
        addCliente(new Cliente("Juan Perez", "12345678-9", "C001", "1990-05-10"));
        addCliente(new Cliente("Maria Soto", "98765432-1", "C002", "1995-11-22"));

        // Atracciones sintéticas reales
        addAtraccion(new Atraccion("Boomerang", "ADRENALINA", "A001", true, "Montaña rusa con bucles intensos"));
        addAtraccion(new Atraccion("Raptor", "ADRENALINA", "A002", true, "Montaña rusa invertida y rápida"));
        addAtraccion(new Atraccion("Tsunami", "FAMILIARES", "D001", true, "River rápido que moja al final"));
        addAtraccion(new Atraccion("Wild Mouse", "FAMILIARES", "D002", true, "Montaña rusa con giros cerrados"));
        addAtraccion(new Atraccion("Fly Over", "ADRENALINA", "A003", true, "Sillas voladoras desde gran altura"));
        addAtraccion(new Atraccion("Disko", "FAMILIARES", "D003", true, "Plataforma giratoria tipo disk'o"));
        addAtraccion(new Atraccion("Mini Splash", "ZONA KIDS", "Z001", true, "Juego acuático infantil"));
        addAtraccion(new Atraccion("Carrusel", "ZONA KIDS", "Z002", true, "Clásico carrusel infantil"));

        // Bloque de horario de ejemplo
        DiasActivosAnuales d = getODia("2025-08-24");
        BloqueDeAtraccion b = d.addBloque("B001", buscarAtraccion("A001"), new HorariosAtraccion("10:00","11:00"));
        b.addCliente(clientes.get(0));
        
        // NUEVO: otro día y bloque para poblar diasPorFecha
        DiasActivosAnuales d2 = getODia("2025-08-25");
        BloqueDeAtraccion b2 = d2.addBloque("B002", buscarAtraccion("A002"), new HorariosAtraccion("12:00", "13:00"));
        b2.addCliente(clientes.get(1)); // María
    }

    
    
    // ====== Métodos para agregar ==============

    // Agregar cliente al sistema
    public void addCliente(Cliente c) {
        clientes.add(c);
        clientesPorRut.put(c.getRut(), c); // lo guardamos en el mapa para encontrar al Cliente por RUT
    }

    // Agregar atracción al sistema
    public void addAtraccion(Atraccion a) {
        atracciones.add(a);
        atrPorCodigo.put(a.getCodigoAtraccion(), a); // lo guardamos en el mapa para encontrar la atraccion por codigo.
    }
    //==========================================

    // ====== Métodos para mostrar =====================

    // Mostrar todos los clientes dentro del sistema.
    public void mostrarClientes() {
        System.out.println("\n--- LISTA DE CLIENTES ---");
        for(Cliente c : clientes) {
            System.out.println(c); // Usa el toString() de Cliente
        }
    }
    // Muestra todas las atracciones dentro del Sistema
    public void mostrarAtracciones() {
        System.out.println("\n--- LISTA DE ATRACCIONES ---");

        for(Atraccion a : atracciones) {
            System.out.println(a); // Usa el toString() de Atraccion
        }
    }

    // ====== Buscar atracción (SIA1.6)======
    public Atraccion buscarAtraccion(String codigo) {
        return atrPorCodigo.get(codigo); // Buscar por código (más rápido con el mapa)
    }
    public Atraccion buscarAtraccion(int index) {
        // Buscar por número en la lista de atracciones
        if (index >= 0 && index < atracciones.size()) return atracciones.get(index);
        return null; // Si el índice no existe retornamos NULL
    }

    
    // ====== Metodos para Eliminar ==========
    
    // Eliminar cliente por RUT
    public boolean eliminarCliente(String rut) {
        Cliente c = clientesPorRut.remove(rut); 
        if (c != null) {
            clientes.remove(c);
            return true;
        }
        return false;
    }

    // Eliminar atracción por código
    public boolean eliminarAtraccion(String codigo) {
        Atraccion a = atrPorCodigo.remove(codigo);
        if (a != null) {
            atracciones.remove(a);
            return true;
        }
        return false;
    }

    // ====== Manejo de días y bloques de Horario======
    public DiasActivosAnuales getODia(String fecha) {
        // Si el día no existe en el mapa, lo crea automáticamente
        return diasPorFecha.computeIfAbsent(fecha, DiasActivosAnuales::new);
    }

    public BloqueDeAtraccion getOCrearBloque(String fecha, String codBloque, Atraccion atr, HorariosAtraccion h) {
        // Busca el día y si no existe lo crea
        DiasActivosAnuales dia = getODia(fecha);

        // Busca el bloque dentro del día
        BloqueDeAtraccion b = dia.buscarBloque(codBloque);
        // Si no existía el bloque, lo crea
        if (b == null) b = dia.addBloque(codBloque, atr, h);
        return b;
    }

    // ====== Inscribir clientes en bloques De Horarios======
    public boolean agregarClienteABloquePorRut(String fecha, String codBloque, String rut) {
        Cliente c = clientesPorRut.get(rut); // buscamos el cliente
        if (c == null) return false; // si no existe el cliente
        DiasActivosAnuales dia = getODia(fecha); // obtenemos el día
        BloqueDeAtraccion b = dia.buscarBloque(codBloque); // buscamos el bloque
        if (b == null) return false; // si no existe el bloque
        b.addCliente(c); // Con esto agregamos al cliente dentro del bloque
        return true;
    }

    // ====== Listar clientes de un bloque De Horario======
    public void listarClientesDeBloque(String fecha, String codBloque) {
        DiasActivosAnuales dia = diasPorFecha.get(fecha); // buscamos el día
        //En caso que no exista el dia
        if (dia == null) {
            System.out.println("No existe día activo " + fecha);
            return;
        }

        // buscamos el bloque de Horario
        BloqueDeAtraccion b = dia.buscarBloque(codBloque);
        //En caso que no exita el bloque
        if (b == null) {
            System.out.println("No existe el bloque " + codBloque + " en " + fecha);
            return;
        }

        // Mostramos clientes inscritos dentro del bloque
        System.out.println("\nClientes del " + b);
        for (Cliente c : b.getClientesParticipantes()) System.out.println(" - " + c);
    }
}
