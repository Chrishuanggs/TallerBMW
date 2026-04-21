package dao;

import com.bmwcomponents.dao.GenericDAO;
import model.Usuario;
import util.DBConnection;

import java.sql.*;
import java.util.List;

public class UsuarioDAO extends GenericDAO<Usuario> {

    @Override
    protected Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    @Override
    protected Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("usuario"),
                rs.getString("password"),
                rs.getString("rol"),
                rs.getBoolean("activo")
        );
    }

    public Usuario login(String usuario, String password) throws SQLException {
        List<Usuario> r = executeQuery(
                "SELECT * FROM usuario WHERE usuario = ? AND password = ? AND activo = true",
                usuario, password
        );
        return r.isEmpty() ? null : r.get(0);
    }

    public List<Usuario> listar() throws SQLException {
        return executeQuery("SELECT * FROM usuario WHERE activo = true ORDER BY nombre");
    }

    public List<Usuario> listarPorRol(String rol) throws SQLException {
        return executeQuery("SELECT * FROM usuario WHERE rol = ? AND activo = true", rol);
    }

    public void insertar(Usuario u) throws SQLException {
        executeUpdate(
                "INSERT INTO usuario (nombre, usuario, password, rol, activo) VALUES (?, ?, ?, ?, true)",
                u.getNombre(), u.getUsuario(), u.getPassword(), u.getRol()
        );
    }

    public void actualizar(Usuario u) throws SQLException {
        executeUpdate(
                "UPDATE usuario SET nombre=?, usuario=?, rol=? WHERE id=?",
                u.getNombre(), u.getUsuario(), u.getRol(), u.getId()
        );
    }

    public void actualizarConPassword(Usuario u) throws SQLException {
        executeUpdate(
                "UPDATE usuario SET nombre=?, usuario=?, password=?, rol=? WHERE id=?",
                u.getNombre(), u.getUsuario(), u.getPassword(), u.getRol(), u.getId()
        );
    }

    public void eliminar(int id) throws SQLException {
        executeUpdate("UPDATE usuario SET activo = false WHERE id = ?", id);
    }

    public boolean existeUsuario(String usuario, int idExcluir) throws SQLException {
        return exists("SELECT id FROM usuario WHERE usuario = ? AND id != ? AND activo = true", usuario, idExcluir);
    }
}
