package model;

import java.time.LocalDateTime;

public class OrdenServicio {
    private int id;
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;
    private String descripcion;
    private String estado;
    private double costoTotal;
    private int idVehiculo;
    private int idMecanico;
    private int idRecepcionista;

    private String placaVehiculo;
    private String nombreCliente;
    private String nombreMecanico;
    private String nombreRecepcionista;

    public OrdenServicio() {}

    public OrdenServicio(int id, LocalDateTime fechaEntrada, LocalDateTime fechaSalida,
                         String descripcion, String estado, double costoTotal,
                         int idVehiculo, int idMecanico, int idRecepcionista) {
        this.id = id;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.descripcion = descripcion;
        this.estado = estado;
        this.costoTotal = costoTotal;
        this.idVehiculo = idVehiculo;
        this.idMecanico = idMecanico;
        this.idRecepcionista = idRecepcionista;
    }

    public int getId()                                { return id; }
    public void setId(int id)                        { this.id = id; }

    public LocalDateTime getFechaEntrada()            { return fechaEntrada; }
    public void setFechaEntrada(LocalDateTime v)     { this.fechaEntrada = v; }

    public LocalDateTime getFechaSalida()             { return fechaSalida; }
    public void setFechaSalida(LocalDateTime v)      { this.fechaSalida = v; }

    public String getDescripcion()                    { return descripcion; }
    public void setDescripcion(String v)             { this.descripcion = v; }

    public String getEstado()                         { return estado; }
    public void setEstado(String v)                  { this.estado = v; }

    public double getCostoTotal()                     { return costoTotal; }
    public void setCostoTotal(double v)              { this.costoTotal = v; }

    public int getIdVehiculo()                        { return idVehiculo; }
    public void setIdVehiculo(int v)                 { this.idVehiculo = v; }

    public int getIdMecanico()                        { return idMecanico; }
    public void setIdMecanico(int v)                 { this.idMecanico = v; }

    public int getIdRecepcionista()                   { return idRecepcionista; }
    public void setIdRecepcionista(int v)            { this.idRecepcionista = v; }

    public String getPlacaVehiculo()                  { return placaVehiculo; }
    public void setPlacaVehiculo(String v)           { this.placaVehiculo = v; }

    public String getNombreCliente()                  { return nombreCliente; }
    public void setNombreCliente(String v)           { this.nombreCliente = v; }

    public String getNombreMecanico()                 { return nombreMecanico; }
    public void setNombreMecanico(String v)          { this.nombreMecanico = v; }

    public String getNombreRecepcionista()            { return nombreRecepcionista; }
    public void setNombreRecepcionista(String v)     { this.nombreRecepcionista = v; }
}
