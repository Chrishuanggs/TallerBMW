package model;

public class Vehiculo {
    private int id;
    private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private String color;
    private String vin;
    private int idCliente;
    private String nombreCliente;
    private boolean activo;

    public Vehiculo() {}

    public Vehiculo(int id, String placa, String marca, String modelo, int anio, String color, String vin, int idCliente, boolean activo) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.vin = vin;
        this.idCliente = idCliente;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}