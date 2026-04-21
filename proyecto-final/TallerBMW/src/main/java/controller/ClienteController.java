package controller;

import dao.ClienteDAO;
import model.Cliente;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClienteController {

    private ClienteDAO clienteDAO;

    public ClienteController() {
        clienteDAO = new ClienteDAO();
    }

    public void registrar(String nombre, String telefono, String correo, String direccion) throws SQLException {
        validar(nombre, telefono, correo);

        if (clienteDAO.existeTelefono(telefono.trim(), 0))
            throw new IllegalArgumentException("Ya existe un cliente con ese telefono");

        clienteDAO.insertar(new Cliente(0, nombre.trim(), telefono.trim(), correo.trim(), direccion.trim(), LocalDate.now(), true));
    }

    public List<Cliente> getClientes() throws SQLException {
        return clienteDAO.listar();
    }

    public void actualizar(int id, String nombre, String telefono, String correo, String direccion) throws SQLException {
        validar(nombre, telefono, correo);

        if (clienteDAO.existeTelefono(telefono.trim(), id))
            throw new IllegalArgumentException("Ese telefono ya esta registrado en otro cliente");

        clienteDAO.actualizar(new Cliente(id, nombre.trim(), telefono.trim(), correo.trim(), direccion.trim(), null, true));
    }

    public void eliminar(int id) throws SQLException {
        if (clienteDAO.tieneVehiculos(id))
            throw new IllegalArgumentException("No se puede eliminar un cliente que tiene vehiculos registrados");
        clienteDAO.eliminar(id);
    }

    private void validar(String nombre, String telefono, String correo) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (telefono == null || telefono.trim().isEmpty())
            throw new IllegalArgumentException("El telefono es obligatorio");
        if (!telefono.trim().matches("\\d{8,15}"))
            throw new IllegalArgumentException("El telefono debe tener entre 8 y 15 digitos");
        if (correo != null && !correo.trim().isEmpty() && !correo.trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("El correo no tiene un formato valido");
    }
}
