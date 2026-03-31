package dao;

import model.Usuario;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario login(String usuario, String password) throws SQLException {
        String query = "SELECT * FROM usuario WHERE usuario = ? AND password = ? AND activo = true";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, usuario);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        Usuario u = null;
        if (rs.next()) u = mapear(rs);
        rs.close(); con.close();
        return u;
    }

    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String query = "SELECT * FROM usuario WHERE activo = true ORDER BY nombre";
        Connection con = DBConnection.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) lista.add(mapear(rs));
        rs.close(); con.close();
        return lista;
    }

    public List<Usuario> listarPorRol(String rol) throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String query = "SELECT * FROM usuario WHERE rol = ? AND activo = true";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, rol);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) lista.add(mapear(rs));
        rs.close(); con.close();
        return lista;
    }

    public void insertar(Usuario u) throws SQLException {
        String query = "INSERT INTO usuario (nombre, usuario, password, rol, activo) VALUES (?, ?, ?, ?, true)";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, u.getNombre());
        ps.setString(2, u.getUsuario());
        ps.setString(3, u.getPassword());
        ps.setString(4, u.getRol());
        ps.executeUpdate();
        ps.close(); con.close();
    }

    public void actualizar(Usuario u) throws SQLException {
        String query = "UPDATE usuario SET nombre=?, usuario=?, rol=? WHERE id=?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, u.getNombre());
        ps.setString(2, u.getUsuario());
        ps.setString(3, u.getRol());
        ps.setInt(4, u.getId());
        ps.executeUpdate();
        ps.close(); con.close();
    }

    public void actualizarConPassword(Usuario u) throws SQLException {
        String query = "UPDATE usuario SET nombre=?, usuario=?, password=?, rol=? WHERE id=?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, u.getNombre());
        ps.setString(2, u.getUsuario());
        ps.setString(3, u.getPassword());
        ps.setString(4, u.getRol());
        ps.setInt(5, u.getId());
        ps.executeUpdate();
        ps.close(); con.close();
    }

    public void eliminar(int id) throws SQLException {
        String query = "UPDATE usuario SET activo = false WHERE id = ?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close(); con.close();
    }

    public boolean existeUsuario(String usuario, int idExcluir) throws SQLException {
        String query = "SELECT id FROM usuario WHERE usuario = ? AND id != ? AND activo = true";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, usuario);
        ps.setInt(2, idExcluir);
        ResultSet rs = ps.executeQuery();
        boolean existe = rs.next();
        rs.close(); con.close();
        return existe;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("usuario"),
                rs.getString("password"),
                rs.getString("rol"),
                rs.getBoolean("activo")
        );
    }
}