package controller;

import dao.VehiculoDAO;
import model.Vehiculo;

import java.sql.SQLException;
import java.time.Year;
import java.util.List;

public class VehiculoController {

    private VehiculoDAO vehiculoDAO;

    public VehiculoController() {
        vehiculoDAO = new VehiculoDAO();
    }

    public void registrar(String placa, String marca, String modelo, int anio, String color, String vin, int idCliente) throws SQLException {
        validar(placa, marca, modelo, anio);

        if (vehiculoDAO.existePlaca(placa.toUpperCase().trim(), 0))
            throw new IllegalArgumentException("Ya existe un vehiculo con esa placa");

        vehiculoDAO.insertar(new Vehiculo(0, placa.toUpperCase().trim(), marca.trim(), modelo.trim(), anio, color.trim(), vin.trim(), idCliente, true));
    }

    public List<Vehiculo> getVehiculos() throws SQLException {
        return vehiculoDAO.listar();
    }

    public List<Vehiculo> getVehiculosPorCliente(int idCliente) throws SQLException {
        return vehiculoDAO.listarPorCliente(idCliente);
    }

    public void actualizar(int id, String placa, String marca, String modelo, int anio, String color, String vin, int idCliente) throws SQLException {
        validar(placa, marca, modelo, anio);

        if (vehiculoDAO.existePlaca(placa.toUpperCase().trim(), id))
            throw new IllegalArgumentException("Esa placa ya esta registrada en otro vehiculo");

        vehiculoDAO.actualizar(new Vehiculo(id, placa.toUpperCase().trim(), marca.trim(), modelo.trim(), anio, color.trim(), vin.trim(), idCliente, true));
    }

    public void eliminar(int id) throws SQLException {
        if (vehiculoDAO.tieneOrdenes(id))
            throw new IllegalArgumentException("No se puede eliminar un vehiculo que tiene ordenes de servicio");
        vehiculoDAO.eliminar(id);
    }

    private void validar(String placa, String marca, String modelo, int anio) {
        if (placa == null || placa.trim().isEmpty())
            throw new IllegalArgumentException("La placa es obligatoria");
        if (!placa.trim().matches("[A-Za-z0-9]{5,8}"))
            throw new IllegalArgumentException("La placa debe tener entre 5 y 8 caracteres alfanumericos");
        if (marca == null || marca.trim().isEmpty())
            throw new IllegalArgumentException("La marca es obligatoria");
        if (modelo == null || modelo.trim().isEmpty())
            throw new IllegalArgumentException("El modelo es obligatorio");
        int anioActual = Year.now().getValue();
        if (anio < 1990 || anio > anioActual + 1)
            throw new IllegalArgumentException("El anio debe estar entre 1990 y " + (anioActual + 1));
    }
}
