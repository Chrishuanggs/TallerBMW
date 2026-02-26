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

    // Para mostrar en tabla
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

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDateTime fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalDateTime getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDateTime fechaSalida) { this.fechaSalida = fechaSalida; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getCostoTotal() { return costoTotal; }
    public void setCostoTotal(double costoTotal) { this.costoTotal = costoTotal; }

    public int getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(int idVehiculo) { this.idVehiculo = idVehiculo; }

    public int getIdMecanico() { return idMecanico; }
    public void setIdMecanico(int idMecanico) { this.idMecanico = idMecanico; }

    public int getIdRecepcionista() { return idRecepcionista; }
    public void setIdRecepcionista(int idRecepcionista) { this.idRecepcionista = idRecepcionista; }

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNombreMecanico() { return nombreMecanico; }
    public void setNombreMecanico(String nombreMecanico) { this.nombreMecanico = nombreMecanico; }

    public String getNombreRecepcionista() { return nombreRecepcionista; }
    public void setNombreRecepcionista(String nombreRecepcionista) { this.nombreRecepcionista = nombreRecepcionista; }
}