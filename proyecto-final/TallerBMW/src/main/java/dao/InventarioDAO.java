package dao;

import com.bmwcomponents.dao.GenericDAO;
import model.Inventario;
import util.DBConnection;

import java.sql.*;
import java.util.List;

public class InventarioDAO extends GenericDAO<Inventario> {

    @Override
    protected Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    protected Inventario mapear(ResultSet rs) throws SQLException {
        return new Inventario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getInt("cantidad"),
                rs.getDouble("precio_unitario"),
                rs.getInt("stock_minimo"),
                rs.getBoolean("activo")
        );
    }

    public void insertar(Inventario item) throws SQLException {
        executeUpdate(
                "INSERT INTO inventario (nombre, descripcion, cantidad, precio_unitario, stock_minimo, activo) VALUES (?, ?, ?, ?, ?, true)",
                item.getNombre(), item.getDescripcion(), item.getCantidad(),
                item.getPrecioUnitario(), item.getStockMinimo()
        );
    }

    public List<Inventario> listar() throws SQLException {
        return executeQuery("SELECT * FROM inventario WHERE activo = true ORDER BY nombre");
    }

    public List<Inventario> listarBajoStock() throws SQLException {
        return executeQuery(
                "SELECT * FROM inventario WHERE activo = true AND cantidad <= stock_minimo ORDER BY nombre"
        );
    }

    public void actualizar(Inventario item) throws SQLException {
        executeUpdate(
                "UPDATE inventario SET nombre=?, descripcion=?, cantidad=?, precio_unitario=?, stock_minimo=? WHERE id=?",
                item.getNombre(), item.getDescripcion(), item.getCantidad(),
                item.getPrecioUnitario(), item.getStockMinimo(), item.getId()
        );
    }

    public void ajustarStock(int id, int delta) throws SQLException {
        executeUpdate("UPDATE inventario SET cantidad = cantidad + ? WHERE id = ?", delta, id);
    }

    public void eliminar(int id) throws SQLException {
        executeUpdate("UPDATE inventario SET activo = false WHERE id = ?", id);
    }

    public boolean existeNombre(String nombre, int idExcluir) throws SQLException {
        return exists("SELECT id FROM inventario WHERE nombre = ? AND id != ? AND activo = true", nombre, idExcluir);
    }
}
