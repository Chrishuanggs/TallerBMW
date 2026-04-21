package model;

import java.time.LocalDate;

public class Cliente {
    private int id;
    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;
    private LocalDate fechaRegistro;
    private boolean activo;

    public Cliente() {}

    public Cliente(int id, String nombre, String telefono, String correo, String direccion, LocalDate fechaRegistro, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
    }

    public int getId()                         { return id; }
    public void setId(int id)                 { this.id = id; }

    public String getNombre()                  { return nombre; }
    public void setNombre(String v)           { this.nombre = v; }

    public String getTelefono()                { return telefono; }
    public void setTelefono(String v)         { this.telefono = v; }

    public String getCorreo()                  { return correo; }
    public void setCorreo(String v)           { this.correo = v; }

    public String getDireccion()               { return direccion; }
    public void setDireccion(String v)        { this.direccion = v; }

    public LocalDate getFechaRegistro()        { return fechaRegistro; }
    public void setFechaRegistro(LocalDate v) { this.fechaRegistro = v; }

    public boolean isActivo()                  { return activo; }
    public void setActivo(boolean v)          { this.activo = v; }
}
