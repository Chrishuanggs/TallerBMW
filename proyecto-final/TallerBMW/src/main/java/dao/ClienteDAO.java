package dao;

import com.bmwcomponents.dao.GenericDAO;
import model.Cliente;
import util.DBConnection;

import java.sql.*;
import java.util.List;

public class ClienteDAO extends GenericDAO<Cliente> {

    @Override
    protected Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    protected Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getString("direccion"),
                rs.getDate("fecha_registro").toLocalDate(),
                rs.getBoolean("activo")
        );
    }

    public void insertar(Cliente c) throws SQLException {
        executeUpdate(
                "INSERT INTO cliente (nombre, telefono, correo, direccion, fecha_registro, activo) VALUES (?, ?, ?, ?, ?, true)",
                c.getNombre(), c.getTelefono(), c.getCorreo(), c.getDireccion(),
                Date.valueOf(c.getFechaRegistro())
        );
    }

    public List<Cliente> listar() throws SQLException {
        return executeQuery("SELECT * FROM cliente WHERE activo = true ORDER BY nombre");
    }

    public void actualizar(Cliente c) throws SQLException {
        executeUpdate(
                "UPDATE cliente SET nombre=?, telefono=?, correo=?, direccion=? WHERE id=?",
                c.getNombre(), c.getTelefono(), c.getCorreo(), c.getDireccion(), c.getId()
        );
    }

    public void eliminar(int id) throws SQLException {
        executeUpdate("UPDATE cliente SET activo = false WHERE id = ?", id);
    }

    public boolean existeTelefono(String telefono, int idExcluir) throws SQLException {
        return exists("SELECT id FROM cliente WHERE telefono = ? AND id != ? AND activo = true", telefono, idExcluir);
    }

    public boolean tieneVehiculos(int idCliente) throws SQLException {
        return exists("SELECT id FROM vehiculo WHERE id_cliente = ? AND activo = true", idCliente);
    }
}
