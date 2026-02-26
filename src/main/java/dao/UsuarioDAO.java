package dao;

import java.util.ArrayList;
import java.util.List;
import model.Usuario;
import util.DBConnection;

import java.sql.*;

public class UsuarioDAO {

    public Usuario login(String usuario, String password) throws SQLException {
        String query = "SELECT * FROM usuario WHERE usuario = ? AND password = ? AND activo = true";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, usuario);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        Usuario u = null;
        if (rs.next()) {
            u = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("rol"),
                    rs.getBoolean("activo")
            );
        }

        rs.close();
        con.close();
        return u;
    }
    public List<Usuario> listarPorRol(String rol) throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String query = "SELECT * FROM usuario WHERE rol = ? AND activo = true";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, rol);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            lista.add(new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("rol"),
                    rs.getBoolean("activo")
            ));
        }

        rs.close();
        con.close();
        return lista;
    }
}