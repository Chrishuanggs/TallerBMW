package dao;

import com.bmwcomponents.dao.GenericDAO;
import model.Vehiculo;
import util.DBConnection;

import java.sql.*;
import java.util.List;

public class VehiculoDAO extends GenericDAO<Vehiculo> {

    @Override
    protected Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    protected Vehiculo mapear(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo(
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
        try { v.setNombreCliente(rs.getString("nombre_cliente")); } catch (SQLException ignored) {}
        return v;
    }

    public void insertar(Vehiculo v) throws SQLException {
        executeUpdate(
                "INSERT INTO vehiculo (placa, marca, modelo, anio, color, vin, id_cliente, activo) VALUES (?, ?, ?, ?, ?, ?, ?, true)",
                v.getPlaca(), v.getMarca(), v.getModelo(), v.getAnio(),
                v.getColor(), v.getVin(), v.getIdCliente()
        );
    }

    public List<Vehiculo> listar() throws SQLException {
        return executeQuery(
                "SELECT v.*, c.nombre AS nombre_cliente FROM vehiculo v " +
                "JOIN cliente c ON v.id_cliente = c.id WHERE v.activo = true ORDER BY v.placa"
        );
    }

    public List<Vehiculo> listarPorCliente(int idCliente) throws SQLException {
        return executeQuery(
                "SELECT v.*, c.nombre AS nombre_cliente FROM vehiculo v " +
                "JOIN cliente c ON v.id_cliente = c.id WHERE v.id_cliente = ? AND v.activo = true",
                idCliente
        );
    }

    public void actualizar(Vehiculo v) throws SQLException {
        executeUpdate(
                "UPDATE vehiculo SET placa=?, marca=?, modelo=?, anio=?, color=?, vin=?, id_cliente=? WHERE id=?",
                v.getPlaca(), v.getMarca(), v.getModelo(), v.getAnio(),
                v.getColor(), v.getVin(), v.getIdCliente(), v.getId()
        );
    }

    public void eliminar(int id) throws SQLException {
        executeUpdate("UPDATE vehiculo SET activo = false WHERE id = ?", id);
    }

    public boolean existePlaca(String placa, int idExcluir) throws SQLException {
        return exists("SELECT id FROM vehiculo WHERE placa = ? AND id != ? AND activo = true", placa, idExcluir);
    }

    public boolean tieneOrdenes(int idVehiculo) throws SQLException {
        return exists("SELECT id FROM orden_servicio WHERE id_vehiculo = ?", idVehiculo);
    }
}
