package model;

public class Usuario {
    private int id;
    private String nombre;
    private String usuario;
    private String password;
    private String rol;
    private boolean activo;

    public Usuario() {}

    public Usuario(int id, String nombre, String usuario, String password, String rol, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
    }

    public int getId()                  { return id; }
    public void setId(int id)          { this.id = id; }

    public String getNombre()           { return nombre; }
    public void setNombre(String v)    { this.nombre = v; }

    public String getUsuario()          { return usuario; }
    public void setUsuario(String v)   { this.usuario = v; }

    public String getPassword()         { return password; }
    public void setPassword(String v)  { this.password = v; }

    public String getRol()              { return rol; }
    public void setRol(String v)       { this.rol = v; }

    public boolean isActivo()           { return activo; }
    public void setActivo(boolean v)   { this.activo = v; }
}
