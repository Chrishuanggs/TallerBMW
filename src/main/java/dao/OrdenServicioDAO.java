package dao;

import model.OrdenServicio;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenServicioDAO {

    public void insertar(OrdenServicio o) throws SQLException {
        String query = "INSERT INTO orden_servicio (fecha_entrada, descripcion, estado, costo_total, id_vehiculo, id_mecanico, id_recepcionista) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setTimestamp(1, Timestamp.valueOf(o.getFechaEntrada()));
        ps.setString(2, o.getDescripcion());
        ps.setString(3, o.getEstado());
        ps.setDouble(4, o.getCostoTotal());
        ps.setInt(5, o.getIdVehiculo());
        ps.setInt(6, o.getIdMecanico());
        ps.setInt(7, o.getIdRecepcionista());
        ps.executeUpdate();
        ps.close(); con.close();
    }

    public List<OrdenServicio> listar() throws SQLException {
        List<OrdenServicio> ordenes = new ArrayList<>();
        String query = "SELECT o.*, v.placa AS placa_vehiculo, c.nombre AS nombre_cliente, " +
                "m.nombre AS nombre_mecanico, r.nombre AS nombre_recepcionista " +
                "FROM orden_servicio o " +
                "JOIN vehiculo v ON o.id_vehiculo = v.id " +
                "JOIN cliente c ON v.id_cliente = c.id " +
                "LEFT JOIN usuario m ON o.id_mecanico = m.id " +
                "JOIN usuario r ON o.id_recepcionista = r.id " +
                "ORDER BY o.fecha_entrada DESC";
        Connection con = DBConnection.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            OrdenServicio o = mapear(rs);
            o.setPlacaVehiculo(rs.getString("placa_vehiculo"));
            o.setNombreCliente(rs.getString("nombre_cliente"));
            o.setNombreMecanico(rs.getString("nombre_mecanico"));
            o.setNombreRecepcionista(rs.getString("nombre_recepcionista"));
            ordenes.add(o);
        }
        rs.close(); con.close();
        return ordenes;
    }

    public List<OrdenServicio> listarPorEstado(String estado) throws SQLException {
        List<OrdenServicio> ordenes = new ArrayList<>();
        String query = "SELECT o.*, v.placa AS placa_vehiculo, c.nombre AS nombre_cliente, " +
                "m.nombre AS nombre_mecanico, r.nombre AS nombre_recepcionista " +
                "FROM orden_servicio o " +
                "JOIN vehiculo v ON o.id_vehiculo = v.id " +
                "JOIN cliente c ON v.id_cliente = c.id " +
                "LEFT JOIN usuario m ON o.id_mecanico = m.id " +
                "JOIN usuario r ON o.id_recepcionista = r.id " +
                "WHERE o.estado = ? ORDER BY o.fecha_entrada DESC";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, estado);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            OrdenServicio o = mapear(rs);
            o.setPlacaVehiculo(rs.getString("placa_vehiculo"));
            o.setNombreCliente(rs.getString("nombre_cliente"));
            o.setNombreMecanico(rs.getString("nombre_mecanico"));
            o.setNombreRecepcionista(rs.getString("nombre_recepcionista"));
            ordenes.add(o);
        }
        rs.close(); con.close();
        return ordenes;
    }

    public void actualizarEstado(int id, String estado) throws SQLException {
        String query = "UPDATE orden_servicio SET estado = ? WHERE id = ?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, estado);
        ps.setInt(2, id);
        ps.executeUpdate();
        ps.close(); con.close();
    }

    public void actualizarCosto(int id, double costo) throws SQLException {
        String query = "UPDATE orden_servicio SET costo_total = ? WHERE id = ?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setDouble(1, costo);
        ps.setInt(2, id);
        ps.executeUpdate();
        ps.close(); con.close();
    }

    public void cerrarOrden(int id) throws SQLException {
        String query = "UPDATE orden_servicio SET estado = 'COMPLETADA', fecha_salida = ? WHERE id = ?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
        ps.setInt(2, id);
        ps.executeUpdate();
        ps.close(); con.close();
    }

    private OrdenServicio mapear(ResultSet rs) throws SQLException {
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
        return o;
    }
}