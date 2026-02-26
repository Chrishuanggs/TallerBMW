package dao;

import model.Vehiculo;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    public void insertar(Vehiculo v) throws SQLException {
        String query = "INSERT INTO vehiculo (placa, marca, modelo, anio, color, vin, id_cliente, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, v.getPlaca());
        ps.setString(2, v.getMarca());
        ps.setString(3, v.getModelo());
        ps.setInt(4, v.getAnio());
        ps.setString(5, v.getColor());
        ps.setString(6, v.getVin());
        ps.setInt(7, v.getIdCliente());
        ps.setBoolean(8, true);
        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public List<Vehiculo> listar() throws SQLException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        // Join para traer el nombre del cliente directamente
        String query = "SELECT v.*, c.nombre AS nombre_cliente FROM vehiculo v " +
                "JOIN cliente c ON v.id_cliente = c.id WHERE v.activo = true";
        Connection con = DBConnection.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Vehiculo v = mapear(rs);
            v.setNombreCliente(rs.getString("nombre_cliente"));
            vehiculos.add(v);
        }

        rs.close();
        con.close();
        return vehiculos;
    }

    public List<Vehiculo> listarPorCliente(int idCliente) throws SQLException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String query = "SELECT v.*, c.nombre AS nombre_cliente FROM vehiculo v " +
                "JOIN cliente c ON v.id_cliente = c.id WHERE v.id_cliente = ? AND v.activo = true";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, idCliente);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Vehiculo v = mapear(rs);
            v.setNombreCliente(rs.getString("nombre_cliente"));
            vehiculos.add(v);
        }

        rs.close();
        con.close();
        return vehiculos;
    }

    public boolean existePlaca(String placa, int idExcluir) throws SQLException {
        String query = "SELECT id FROM vehiculo WHERE placa = ? AND id != ? AND activo = true";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, placa);
        ps.setInt(2, idExcluir);
        ResultSet rs = ps.executeQuery();
        boolean existe = rs.next();
        rs.close(); con.close();
        return existe;
    }

    public void actualizar(Vehiculo v) throws SQLException {
        String query = "UPDATE vehiculo SET placa=?, marca=?, modelo=?, anio=?, color=?, vin=?, id_cliente=? WHERE id=?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, v.getPlaca());
        ps.setString(2, v.getMarca());
        ps.setString(3, v.getModelo());
        ps.setInt(4, v.getAnio());
        ps.setString(5, v.getColor());
        ps.setString(6, v.getVin());
        ps.setInt(7, v.getIdCliente());
        ps.setInt(8, v.getId());
        ps.executeUpdate();

        ps.close();
        con.close();
    }

    public void eliminar(int id) throws SQLException {
        String query = "UPDATE vehiculo SET activo = false WHERE id = ?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public boolean tieneOrdenes(int idVehiculo) throws SQLException {
        String query = "SELECT id FROM orden_servicio WHERE id_vehiculo = ?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, idVehiculo);
        ResultSet rs = ps.executeQuery();
        boolean tiene = rs.next();
        rs.close(); con.close();
        return tiene;
    }

    private Vehiculo mapear(ResultSet rs) throws SQLException {
        return new Vehiculo(
                rs.getInt("id"),
                rs.getString("placa"),
                rs.getString("marca"),
                rs.getString("modelo"),
                rs.getInt("anio"),
                rs.getString("color"),
                rs.getString("vin"),
                rs.getInt("id_cliente"),
                rs.getBoolean("activo")
        );
    }
}