package controller;

import dao.UsuarioDAO;
import model.Usuario;

import java.sql.SQLException;
import java.util.List;

public class UsuarioController {

    private UsuarioDAO usuarioDAO;

    public UsuarioController() {
        usuarioDAO = new UsuarioDAO();
    }

    public Usuario login(String usuario, String password) throws SQLException {
        if (usuario == null || usuario.trim().isEmpty())
            throw new IllegalArgumentException("El usuario es obligatorio");
        if (password == null || password.trim().isEmpty())
            throw new IllegalArgumentException("La contrasena es obligatoria");

        Usuario u = usuarioDAO.login(usuario.trim(), password.trim());
        if (u == null)
            throw new IllegalArgumentException("Usuario o contrasena incorrectos");
        return u;
    }

    public void registrar(String nombre, String usuario, String password, String rol) throws SQLException {
        validar(nombre, usuario, password, rol);

        if (usuarioDAO.existeUsuario(usuario.trim(), 0))
            throw new IllegalArgumentException("Ya existe un usuario con ese nombre de usuario");

        usuarioDAO.insertar(new Usuario(0, nombre.trim(), usuario.trim(), password.trim(), rol, true));
    }

    public List<Usuario> getUsuarios() throws SQLException {
        return usuarioDAO.listar();
    }

    public void actualizar(int id, String nombre, String usuario, String password, String rol) throws SQLException {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (usuario == null || usuario.trim().isEmpty())
            throw new IllegalArgumentException("El usuario es obligatorio");
        if (!List.of("ADMIN", "RECEPCIONISTA", "MECANICO").contains(rol))
            throw new IllegalArgumentException("Rol no valido");
        if (usuarioDAO.existeUsuario(usuario.trim(), id))
            throw new IllegalArgumentException("Ese nombre de usuario ya esta en uso");

        Usuario u = new Usuario(id, nombre.trim(), usuario.trim(), password, rol, true);

        if (password != null && !password.trim().isEmpty()) {
            if (password.trim().length() < 4)
                throw new IllegalArgumentException("La contrasena debe tener al menos 4 caracteres");
            u.setPassword(password.trim());
            usuarioDAO.actualizarConPassword(u);
        } else {
            usuarioDAO.actualizar(u);
        }
    }

    public void eliminar(int id, int idSesionActual) throws SQLException {
        if (id == idSesionActual)
            throw new IllegalArgumentException("No puedes eliminar tu propia cuenta");
        usuarioDAO.eliminar(id);
    }

    private void validar(String nombre, String usuario, String password, String rol) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (usuario == null || usuario.trim().isEmpty())
            throw new IllegalArgumentException("El usuario es obligatorio");
        if (password == null || password.trim().isEmpty())
            throw new IllegalArgumentException("La contrasena es obligatoria");
        if (password.trim().length() < 4)
            throw new IllegalArgumentException("La contrasena debe tener al menos 4 caracteres");
        if (!List.of("ADMIN", "RECEPCIONISTA", "MECANICO").contains(rol))
            throw new IllegalArgumentException("Rol no valido");
    }
}
