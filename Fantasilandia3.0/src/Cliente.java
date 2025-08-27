
//Clase cliente con tus respectivos atributos (SIA 1.2)
public class Cliente {
    private String nombre; //Nombre del cliente
    private String rut; //rut del cliente (con digito verificador)
    private String idCliente; //Id Unico del cliente dentro del sistema
    private String fechaNacimiento; //Fecha de nacimiento del cliente en formato (yyyy/mm/dd)

    // ====== Constructores ======

    public Cliente() {  // Constructor vacío -> permite crear un cliente sin dar datos de inmediato
    }

    // Constructor que inicializa todos los datos del cliente.
    public Cliente(String nombre, String rut, String idCliente, String fechaNacimiento) {
        this.nombre = nombre;
        this.rut = rut;
        this.idCliente = idCliente;
        this.fechaNacimiento = fechaNacimiento;
    }

    // ====== Métodos Getters y Setters (SIA 1.3)======
    // Estos métodos permiten acceder o modificar los atributos privados
    // Encapsulación: Como los atributos son privados, solo se accede a través de estos métodos

    //Leer o modificar el nombre Del cliente
    public String getNombre() {
        return this.nombre;
    } // Devuelve el nombre del cliente
    public void setNombre(String nombre) {
        this.nombre = nombre;
    } // Cambia el nombre del cliente

    //Leer o modificar el RUT Del cliente
    public String getRut() {
        return this.rut;
    } // Devuelve el RUT del cliente
    public void setRut(String rut) {
        this.rut = rut;
    }  // Cambia el RUT del cliente

    //Leer o modificar el ID unico Del cliente
    public String getIdCliente() {
        return this.idCliente;
    } // Devuelve el ID unico del cliente
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    } // Cambia el ID unico del cliente

    //Leer o modificar el Fecha de nacimiento Del cliente
    public String getFechaNacimiento() {
        return this.fechaNacimiento;
    } // Devuelve el Fecha de nacimiento del cliente
    public void setFechaNacimiento(String fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;} // Devuelve el Fecha de nacimiento del cliente

    @Override
    // Sobrescribimos toString para mostrar la información del cliente
    public String toString() {
        return "Cliente: " + this.nombre +
                " | RUT: " + this.rut +
                " | ID: " + this.idCliente +
                " | Nacimiento: " + this.fechaNacimiento;
    }
}
