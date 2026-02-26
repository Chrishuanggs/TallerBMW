package controller;

import dao.UsuarioDAO;
import model.Usuario;

import java.sql.SQLException;

public class UsuarioController {

    private UsuarioDAO usuarioDAO;

    public UsuarioController() {
        usuarioDAO = new UsuarioDAO();
    }

    public Usuario login(String usuario, String password) throws SQLException {
        if (usuario == null || usuario.trim().isEmpty())
            throw new IllegalArgumentException("El usuario es obligatorio");

        if (password == null || password.trim().isEmpty())
            throw new IllegalArgumentException("La contraseña es obligatoria");

        Usuario u = usuarioDAO.login(usuario.trim(), password.trim());

        if (u == null)
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");

        return u;
    }
}