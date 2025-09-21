package fantasilandia;

import java.io.BufferedReader;
import java.io.IOException;

import Excepciones.FechaMalFormateadaException;
import Excepciones.RutMalFormateadoException;


public class MenuClientes {

    public static void menu(Fantasilandia parque, BufferedReader br) throws IOException, RutMalFormateadoException, FechaMalFormateadaException {
        int op;
        do {
            System.out.println("\n--- GESTIONAR CLIENTES ---");
            System.out.println("1) Agregar Cliente");
            System.out.println("2) Mostrar Clientes");
            System.out.println("3) Editar Cliente");
            System.out.println("4) Eliminar Cliente");
            System.out.println("0) Volver");
            System.out.print("Seleccione: ");
            op = leerInt(br);

            switch (op) {
                case 1:  
                	{agregarClienteUI(parque, br);
                	break;}
                case 2:  
                	{parque.mostrarClientes();
                	break;}
                case 3:  
                	{editarClienteUI(parque, br);
                	break;}
                case 4:  
                	{eliminarClienteUI(parque, br);
                	break;}
                case 0:  
                	{break;}
                default:  
                	{System.out.println("Opción inválida.");
                	break;}
            }
        } while (op != 0);
    }
    //actualizado para que calze con la UI
    private static void agregarClienteUI(Fantasilandia parque, BufferedReader br) throws IOException, RutMalFormateadoException, FechaMalFormateadaException {
        System.out.print("Nombre: ");
        String nombre = br.readLine().trim();
        System.out.print("RUT: ");
        String rut = br.readLine().trim();
        System.out.print("Fecha nacimiento (YYYY-MM-DD): ");
        String fecha = br.readLine().trim();

        String id = "CLI" + formatearNumero(String.valueOf(parque.contarClientes() + 1));
        Cliente c = new Cliente(nombre, rut, id, fecha);
        parque.addCliente(c);
        System.out.println("✓ Cliente agregado. ID = " + id);
    }

    private static void editarClienteUI(Fantasilandia parque, BufferedReader br) throws IOException, FechaMalFormateadaException {
        System.out.print("Ingrese RUT del cliente a editar: ");
        String rut = br.readLine().trim();
        Cliente c = parque.buscarCliente(rut);
        if (c == null) {
            System.out.println("✗ Cliente no encontrado.");
            return;
        }
        System.out.println("Cliente actual: " + c);
        System.out.print("Nuevo nombre (enter = conservar): ");
        String s = br.readLine();
        if (s != null && !s.trim().isEmpty()) c.setNombre(s.trim());

        System.out.print("Nueva fecha nacimiento (YYYY-MM-DD) (enter = conservar): ");
        s = br.readLine();
        if (s != null && !s.trim().isEmpty()) c.setFechaNacimiento(s.trim());

        System.out.println("✓ Cliente actualizado.");
    }

    private static void eliminarClienteUI(Fantasilandia parque, BufferedReader br) throws IOException {
        System.out.print("Ingrese RUT del cliente a eliminar: ");
        String rut = br.readLine().trim();
        boolean ok = parque.eliminarCliente(rut);
        System.out.println(ok ? "✓ Cliente eliminado." : "✗ Cliente no encontrado.");
    }

    // -------- Helpers --------
    private static int leerInt(BufferedReader br) throws IOException {
        try {
            String s = br.readLine();
            if (s == null) return -1;
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private static String formatearNumero(String codNum) {
        int n = Integer.parseInt(codNum);
        return String.format("%03d", n);
    }

    

}
