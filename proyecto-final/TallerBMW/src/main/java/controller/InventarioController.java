package controller;

import dao.InventarioDAO;
import model.Inventario;

import java.sql.SQLException;
import java.util.List;

public class InventarioController {

    private InventarioDAO inventarioDAO;

    public InventarioController() {
        inventarioDAO = new InventarioDAO();
    }

    public void registrar(String nombre, String descripcion, String cantidadStr,
                          String precioStr, String stockMinStr) throws SQLException {
        int cantidad  = parseCantidad(cantidadStr);
        double precio = parsePrecio(precioStr);
        int stockMin  = parseStockMin(stockMinStr);
        validar(nombre, cantidad, precio, stockMin);

        if (inventarioDAO.existeNombre(nombre.trim(), 0))
            throw new IllegalArgumentException("Ya existe un item con ese nombre");

        inventarioDAO.insertar(new Inventario(0, nombre.trim(), descripcion.trim(), cantidad, precio, stockMin, true));
    }

    public List<Inventario> getItems() throws SQLException {
        return inventarioDAO.listar();
    }

    public List<Inventario> getItemsBajoStock() throws SQLException {
        return inventarioDAO.listarBajoStock();
    }

    public void actualizar(int id, String nombre, String descripcion, String cantidadStr,
                           String precioStr, String stockMinStr) throws SQLException {
        int cantidad  = parseCantidad(cantidadStr);
        double precio = parsePrecio(precioStr);
        int stockMin  = parseStockMin(stockMinStr);
        validar(nombre, cantidad, precio, stockMin);

        if (inventarioDAO.existeNombre(nombre.trim(), id))
            throw new IllegalArgumentException("Ese nombre ya esta registrado en otro item");

        inventarioDAO.actualizar(new Inventario(id, nombre.trim(), descripcion.trim(), cantidad, precio, stockMin, true));
    }

    public void ajustarStock(int id, String deltaStr, boolean esSuma) throws SQLException {
        int delta;
        try {
            delta = Integer.parseInt(deltaStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cantidad debe ser un numero entero");
        }
        if (delta <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        inventarioDAO.ajustarStock(id, esSuma ? delta : -delta);
    }

    public void eliminar(int id) throws SQLException {
        inventarioDAO.eliminar(id);
    }

    private void validar(String nombre, int cantidad, double precio, int stockMin) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (cantidad < 0)
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        if (precio < 0)
            throw new IllegalArgumentException("El precio no puede ser negativo");
        if (stockMin < 0)
            throw new IllegalArgumentException("El stock minimo no puede ser negativo");
    }

    private int parseCantidad(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("La cantidad debe ser un numero entero"); }
    }

    private double parsePrecio(String s) {
        try { return Double.parseDouble(s.trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("El precio debe ser un numero valido"); }
    }

    private int parseStockMin(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("El stock minimo debe ser un numero entero"); }
    }
}
