package dao;

import com.bmwcomponents.dao.GenericDAO;
import model.OrdenServicio;
import util.DBConnection;

import java.sql.*;
import java.util.List;

public class OrdenServicioDAO extends GenericDAO<OrdenServicio> {

    private static final String SELECT_BASE =
            "SELECT o.*, v.placa AS placa_vehiculo, c.nombre AS nombre_cliente, " +
            "m.nombre AS nombre_mecanico, r.nombre AS nombre_recepcionista " +
            "FROM orden_servicio o " +
            "JOIN vehiculo v ON o.id_vehiculo = v.id " +
            "JOIN cliente c ON v.id_cliente = c.id " +
            "LEFT JOIN usuario m ON o.id_mecanico = m.id " +
            "JOIN usuario r ON o.id_recepcionista = r.id ";

    @Override
    protected Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    protected OrdenServicio mapear(ResultSet rs) throws SQLException {
        OrdenServicio o = new OrdenServicio();
        o.setId(rs.getInt("id"));
        o.setFechaEntrada(rs.getTimestamp("fecha_entrada").toLocalDateTime());
        Timestamp salida = rs.getTimestamp("fecha_salida");
        if (salida != null) o.setFechaSalida(salida.toLocalDateTime());
        o.setDescripcion(rs.getString("descripcion"));
        o.setEstado(rs.getString("estado"));
        o.setCostoTotal(rs.getDouble("costo_total"));
        o.setIdVehiculo(rs.getInt("id_vehiculo"));
        o.setIdMecanico(rs.getInt("id_mecanico"));
        o.setIdRecepcionista(rs.getInt("id_recepcionista"));
        try { o.setPlacaVehiculo(rs.getString("placa_vehiculo")); }        catch (SQLException ignored) {}
        try { o.setNombreCliente(rs.getString("nombre_cliente")); }        catch (SQLException ignored) {}
        try { o.setNombreMecanico(rs.getString("nombre_mecanico")); }      catch (SQLException ignored) {}
        try { o.setNombreRecepcionista(rs.getString("nombre_recepcionista")); } catch (SQLException ignored) {}
        return o;
    }

    public void insertar(OrdenServicio o) throws SQLException {
        executeUpdate(
                "INSERT INTO orden_servicio (fecha_entrada, descripcion, estado, costo_total, id_vehiculo, id_mecanico, id_recepcionista) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)",
                Timestamp.valueOf(o.getFechaEntrada()), o.getDescripcion(), o.getEstado(),
                o.getCostoTotal(), o.getIdVehiculo(), o.getIdMecanico(), o.getIdRecepcionista()
        );
    }

    public List<OrdenServicio> listar() throws SQLException {
        return executeQuery(SELECT_BASE + "ORDER BY o.fecha_entrada DESC");
    }

    public List<OrdenServicio> listarPorEstado(String estado) throws SQLException {
        return executeQuery(SELECT_BASE + "WHERE o.estado = ? ORDER BY o.fecha_entrada DESC", estado);
    }

    public void actualizarEstado(int id, String estado) throws SQLException {
        executeUpdate("UPDATE orden_servicio SET estado = ? WHERE id = ?", estado, id);
    }

    public void actualizarCosto(int id, double costo) throws SQLException {
        executeUpdate("UPDATE orden_servicio SET costo_total = ? WHERE id = ?", costo, id);
    }

    public void cerrarOrden(int id) throws SQLException {
        executeUpdate(
                "UPDATE orden_servicio SET estado = 'COMPLETADA', fecha_salida = ? WHERE id = ?",
                Timestamp.valueOf(java.time.LocalDateTime.now()), id
        );
    }
}
