package model;

public class Inventario {
    private int id;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private double precioUnitario;
    private int stockMinimo;
    private boolean activo;

    public Inventario() {}

    public Inventario(int id, String nombre, String descripcion, int cantidad,
                      double precioUnitario, int stockMinimo, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.stockMinimo = stockMinimo;
        this.activo = activo;
    }

    public int getId()                       { return id; }
    public void setId(int id)               { this.id = id; }

    public String getNombre()                { return nombre; }
    public void setNombre(String v)         { this.nombre = v; }

    public String getDescripcion()           { return descripcion; }
    public void setDescripcion(String v)    { this.descripcion = v; }

    public int getCantidad()                 { return cantidad; }
    public void setCantidad(int v)          { this.cantidad = v; }

    public double getPrecioUnitario()        { return precioUnitario; }
    public void setPrecioUnitario(double v) { this.precioUnitario = v; }

    public int getStockMinimo()              { return stockMinimo; }
    public void setStockMinimo(int v)       { this.stockMinimo = v; }

    public boolean isActivo()                { return activo; }
    public void setActivo(boolean v)        { this.activo = v; }

    public boolean isBajoStock()             { return cantidad <= stockMinimo; }
}
