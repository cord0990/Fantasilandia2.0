package fantasilandia;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets; //para el reporte
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.stream.Collectors;

import Excepciones.BloqueMalFormateadoException;
import Excepciones.FechaMalFormateadaException;
import Excepciones.HorarioMalFormateadoException;
import Excepciones.RutMalFormateadoException;


//Clase principal que administrara nuestro paeque de atracciones.
public class Fantasilandia {

    // ====== Listas principales ====== (SIA 1.2)
    private ArrayList<Cliente> clientes = new ArrayList<Cliente>(); // Guardamos los clientes
    private ArrayList<Atraccion> atracciones = new ArrayList<Atraccion>(); // Guardamos las atracciones

    // == Uso de mapas para una Búsqueda Rápida == (SIA1.7)
    private HashMap<String, Cliente> clientesPorRut = new HashMap<String, Cliente>(); // Buscar cliente por RUT
    private HashMap<String, Atraccion> atrPorCodigo = new HashMap<String, Atraccion>(); // Buscar atracción por su código
    private HashMap<String, DiasActivosAnuales> diasPorFecha = new HashMap<String, DiasActivosAnuales>(); // Guardar los días activos


    // constructor nuevo: si populateSample == true, se llenan los ejemplos
    public Fantasilandia(boolean populateSample) {
    }


    //SIA 2.9. APlicacion de Excepciones.
    //====== Constructor con datos de ejemplo ====== (SIA1.4)
    public Fantasilandia() throws RutMalFormateadoException, FechaMalFormateadaException, HorarioMalFormateadoException, BloqueMalFormateadoException {
        this(true);
        // Clientes de ejemplo
    }

    // Getter

    // Devuelve la lista de atracciones (mutable, para que la GUI pueda agregar/editar)
    public ArrayList<Atraccion> getAtracciones() {
        return this.atracciones;
    }
    // Devuelve la lista de clientes (mutable, para que la GUI pueda agregar/editar)
    public ArrayList<Cliente> getClientes() {
        return this.clientes;
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


    // ====== Metodos de contar =====
    public int contarClientes() {
        return this.clientes.size();
    }

    public int contarAtracciones() {
        return this.atracciones.size();
    }

    // ====== Metodos de busqueda ======

    // === Buscar Cliente por RUT usando el HashMap ===
    public Cliente buscarCliente(String rut) {
        if (rut == null) return null;
        return clientesPorRut.get(rut);
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



    // ====== Manejo de días y bloques de Horario======
    public DiasActivosAnuales getODia(String fecha) {
        // Si el día no existe en el mapa, lo crea automáticamente
        return diasPorFecha.computeIfAbsent(fecha, DiasActivosAnuales::new);
    }

    public BloqueDeAtraccion getOCrearBloque(String fecha, String codBloque, Atraccion atr, HorariosAtraccion h) throws FechaMalFormateadaException, BloqueMalFormateadoException, HorarioMalFormateadoException {
        // Busca el día y si no existe lo crea
        DiasActivosAnuales dia = getODia(fecha);

        // Busca el bloque dentro del día
        BloqueDeAtraccion b = dia.buscarBloque(codBloque);
        // Si no existía el bloque, lo crea
        if (b == null) b = dia.addBloque(codBloque, atr, h);
        return b;
    }

    // ====== Inscribir clientes en bloques De Horarios======
    public boolean agregarClienteABloquePorRut(String fecha, String codBloque, String rut) throws BloqueMalFormateadoException {
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

    // ==== Metodos del Menu de Horarios (SIA 1.8)
    // Lista todos los horarios de una atracción
    public void listarHorariosDeAtraccion(String codigo) {
        Atraccion a = buscarAtraccion(codigo);
        if (a == null) {
            System.out.println("✗ No existe atracción con código: " + codigo);
            return;
        }
        List<HorariosAtraccion> hs = a.getHorariosEstandares();
        if (hs.isEmpty()) {
            System.out.println("La atracción '" + a.getNombre() + "' no tiene horarios estándar cargados.");
            return;
        }
        System.out.println("\nHorarios de '" + a.getNombre() + "' (" + a.getCodigoAtraccion() + "):");
        int i = 1;
        for (HorariosAtraccion h : hs) {
            System.out.println((i++) + ") " + h);
        }
    }

    //==============Metodos de Horarios en atraccion.
    public boolean agregarHorarioAAtraccion(String codigo, String inicio, String fin) throws HorarioMalFormateadoException {
        Atraccion a = buscarAtraccion(codigo);
        if (a == null) {
            System.out.println("✗ No existe atracción con código: " + codigo);
            return false;
        }

        HorariosAtraccion h = new HorariosAtraccion(inicio, fin);
        a.addHorario(h);
        return true;
    }

    /**
     * Elimina un horario de una atracción identificado por su código y horas (inicio, fin).
     * Lanza Exception si la atracción o el horario no existen.
     */
    public void eliminarHorarioDeAtraccion(String codigo, String inicio, String fin) throws Exception {
        Atraccion a = buscarAtraccion(codigo);
        if (a == null) {
            throw new Exception("No existe atracción con código: " + codigo);
        }
        java.util.List<HorariosAtraccion> hs = a.getHorariosEstandares();
        HorariosAtraccion objetivo = null;
        for (HorariosAtraccion h : hs) {
            if (h.getHoraInicio().equals(inicio) && h.getHoraFin().equals(fin)) {
                objetivo = h;
                break;
            }
        }
        if (objetivo == null) {
            throw new Exception("No se encontró el horario especificado para la atracción.");
        }
        hs.remove(objetivo);
    }

    /**
     * Modifica un horario existente de una atracción: busca por inicio/fin antiguos y lo reemplaza por uno nuevo.
     * Valida formato usando HorariosAtraccion y evita duplicados.
     */
    public void modificarHorarioDeAtraccion(String codigo, String inicioViejo, String finViejo,
                                            String nuevoInicio, String nuevoFin) throws Exception {
        Atraccion a = buscarAtraccion(codigo);
        if (a == null) {
            throw new Exception("No existe atracción con código: " + codigo);
        }
        java.util.List<HorariosAtraccion> hs = a.getHorariosEstandares();
        HorariosAtraccion objetivo = null;
        for (HorariosAtraccion h : hs) {
            if (h.getHoraInicio().equals(inicioViejo) && h.getHoraFin().equals(finViejo)) {
                objetivo = h;
                break;
            }
        }
        if (objetivo == null) {
            throw new Exception("No se encontró el horario a modificar para la atracción.");
        }
        // Validar nuevo horario (lanza HorarioMalFormateadoException si hay problema)
        HorariosAtraccion nuevo = new HorariosAtraccion(nuevoInicio, nuevoFin);
        // Verificar duplicado
        boolean dup = hs.stream().anyMatch(x -> x.getHoraInicio().equals(nuevoInicio)
                && x.getHoraFin().equals(nuevoFin));
        if (dup) {
            throw new Exception("El nuevo horario ya existe para la atracción.");
        }
        // Reemplazar: quitar antiguo y agregar el nuevo
        hs.remove(objetivo);
        a.addHorario(nuevo);
    }



    //==== Setters y Getters para clase Horario=======

    // ======================
    // Métodos para manejar DiasActivosAnuales
    // ======================

    // Agregar un nuevo día activo por fecha */
    public boolean agregarDiaActivoAnual(String fecha) {
        if (diasPorFecha.containsKey(fecha)) return false;
        diasPorFecha.put(fecha, new DiasActivosAnuales(fecha));
        return true;
    }

    // Buscar un día activo existente */
    public DiasActivosAnuales buscarDiaActivoAnual(String fecha) {
        return diasPorFecha.get(fecha);
    }

    // Eliminar un día activo */
    public boolean eliminarDiaActivoAnual(String fecha) {
        return diasPorFecha.remove(fecha) != null;
    }

    // Devolver todos los días activos */
    public Map<String, DiasActivosAnuales> getDiasActivosAnuales() {
        return diasPorFecha;
    }

    // Modificar la fecha de un día activo */
    public boolean modificarDiaActivoAnual(String fechaAntigua, String nuevaFecha) {
        DiasActivosAnuales dia = diasPorFecha.get(fechaAntigua);
        if (dia == null || diasPorFecha.containsKey(nuevaFecha)) return false;

        diasPorFecha.remove(fechaAntigua);
        dia.setFecha(nuevaFecha);
        diasPorFecha.put(nuevaFecha, dia);

        return true;
    }



    //Cargar desde CSV (SIA 2.2)
    public void cargarDesdeCSV(String dir) throws RutMalFormateadoException, FechaMalFormateadaException, HorarioMalFormateadoException, BloqueMalFormateadoException {
        Path base = Paths.get(dir);
        try {
            // ATRACCIONES
            Path pa = base.resolve("atracciones.csv");
            if (Files.exists(pa)) {
                for (String line : Files.readAllLines(pa)) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split(",", -1);
                    // codigo,nombre,clasificacion,activa,descripcion
                    String codigo = p[0];
                    String nombre = p[1];
                    String clasif = p[2];
                    boolean activa = Boolean.parseBoolean(p[3]);
                    String desc = p.length > 4 ? p[4] : "";
                    if (buscarAtraccion(codigo) == null)
                        addAtraccion(new Atraccion(nombre, clasif, codigo, activa, desc));
                }
            }

            // CLIENTES
            Path pc = base.resolve("clientes.csv");
            if (Files.exists(pc)) {
                for (String line : Files.readAllLines(pc)) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split(",", -1);
                    // id,rut,nombre,fechaNacimiento
                    String id = p[0];
                    String rut = p[1];
                    String nombre = p[2];
                    String fecha = p.length > 3 ? p[3] : "";
                    if (buscarCliente(rut) == null)
                        addCliente(new Cliente(nombre, rut, id, fecha));
                }
            }

            // BLOQUES
            Path pb = base.resolve("bloques.csv");
            if (Files.exists(pb)) {
                for (String line : Files.readAllLines(pb)) {
                    if (line.trim().isEmpty()) continue;
                    String[] p = line.split(",", -1);
                    String codigoBloque = p[0];
                    String fecha = p[1];
                    String codigoAtr = p[2];
                    String horaIni = p[3];
                    String horaFin = p[4];
                    String ruts = p.length > 5 ? p[5] : "";
                    Atraccion a = buscarAtraccion(codigoAtr);
                    if (a == null) continue;
                    DiasActivosAnuales dia = getODia(fecha);
                    BloqueDeAtraccion b = dia.addBloque(codigoBloque, a, new HorariosAtraccion(horaIni, horaFin));

                    if (!ruts.isEmpty()) {
                        for (String rut : ruts.split(";")) {
                            Cliente c = buscarCliente(rut);
                            if (c != null) b.addCliente(c);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Guardar en CSV (SIA 2.2)
    public void guardarEnCSV(String dir) {
        Path base = Paths.get(dir);
        try {
            if (!Files.exists(base)) Files.createDirectories(base);

            // ATRACCIONES
            Path pa = base.resolve("atracciones.csv");
            try (BufferedWriter bw = Files.newBufferedWriter(pa)) {
                for (Atraccion a : atracciones) {
                    String line = String.join(",", a.getCodigoAtraccion(), a.getNombre(), a.getClasificacion(), String.valueOf(a.isActiva()), a.getDescripcion());
                    bw.write(line); bw.newLine();
                }
            }

            // CLIENTES
            Path pc = base.resolve("clientes.csv");
            try (BufferedWriter bw = Files.newBufferedWriter(pc)) {
                for (Cliente c : clientes) {
                    String line = String.join(",", c.getIdCliente(), c.getRut(), c.getNombre(), c.getFechaNacimiento());
                    bw.write(line); bw.newLine();
                }
            }

            // BLOQUES
            Path pb = base.resolve("bloques.csv");
            try (BufferedWriter bw = Files.newBufferedWriter(pb)) {
                for (DiasActivosAnuales dia : diasPorFecha.values()) {
                    for (BloqueDeAtraccion b : dia.getBloques()) {
                        String ruts = b.getClientesParticipantes().stream().map(Cliente::getRut).collect(Collectors.joining(";"));
                        String line = String.join(",", b.getCodigoBloque(), dia.getFecha(), b.getAtraccion().getCodigoAtraccion(), b.getHorario().getHoraInicio(), b.getHorario().getHoraFin(), ruts);
                        bw.write(line); bw.newLine();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // generar reporte SIA  2.10
    public void generarReporteTxt(String dir) {
        Path base = Paths.get(dir);
        Path report = base.resolve("reporte_parque.txt");
        try (BufferedWriter bw = Files.newBufferedWriter(report, StandardCharsets.UTF_8)) {
            bw.write("=== REPORTE FANTASILANDIA ===\n");
            bw.write("Clientes: " + clientes.size() + "\n");
            for (Cliente c : clientes) {
                bw.write(c.toString() + "\n");
            }
            bw.write("\nAtracciones: " + atracciones.size() + "\n");
            for (Atraccion a : atracciones) {
                bw.write(a.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
