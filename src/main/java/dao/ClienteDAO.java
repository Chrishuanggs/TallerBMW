package dao;

import model.Cliente;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void insertar(Cliente c) throws SQLException {
        String query = "INSERT INTO cliente (nombre, telefono, correo, direccion, fecha_registro, activo) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, c.getNombre());
        ps.setString(2, c.getTelefono());
        ps.setString(3, c.getCorreo());
        ps.setString(4, c.getDireccion());
        ps.setDate(5, Date.valueOf(c.getFechaRegistro()));
        ps.setBoolean(6, true);
        ps.executeUpdate();
        ps.close(); con.close();
    }

    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String query = "SELECT * FROM cliente WHERE activo = true";
        Connection con = DBConnection.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) clientes.add(mapear(rs));
        rs.close(); con.close();
        return clientes;
    }

    public boolean existeTelefono(String telefono, int idExcluir) throws SQLException {
        String query = "SELECT id FROM cliente WHERE telefono = ? AND id != ? AND activo = true";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, telefono);
        ps.setInt(2, idExcluir);
        ResultSet rs = ps.executeQuery();
        boolean existe = rs.next();
        rs.close(); con.close();
        return existe;
    }

    public void actualizar(Cliente c) throws SQLException {
        String query = "UPDATE cliente SET nombre=?, telefono=?, correo=?, direccion=? WHERE id=?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, c.getNombre());
        ps.setString(2, c.getTelefono());
        ps.setString(3, c.getCorreo());
        ps.setString(4, c.getDireccion());
        ps.setInt(5, c.getId());
        ps.executeUpdate();
        ps.close(); con.close();
    }

    public void eliminar(int id) throws SQLException {
        String query = "UPDATE cliente SET activo = false WHERE id = ?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close(); con.close();
    }

    public boolean tieneVehiculos(int idCliente) throws SQLException {
        String query = "SELECT id FROM vehiculo WHERE id_cliente = ? AND activo = true";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, idCliente);
        ResultSet rs = ps.executeQuery();
        boolean tiene = rs.next();
        rs.close(); con.close();
        return tiene;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
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
}