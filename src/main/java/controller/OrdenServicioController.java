package controller;

import dao.OrdenServicioDAO;
import model.OrdenServicio;
import model.Usuario;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class OrdenServicioController {

    private OrdenServicioDAO ordenDAO;

    public OrdenServicioController() {
        ordenDAO = new OrdenServicioDAO();
    }

    public void crear(String descripcion, int idVehiculo, int idMecanico, Usuario recepcionista) throws SQLException {
        if (descripcion == null || descripcion.trim().isEmpty())
            throw new IllegalArgumentException("La descripción es obligatoria");

        if (idVehiculo <= 0)
            throw new IllegalArgumentException("Selecciona un vehículo");

        if (idMecanico <= 0)
            throw new IllegalArgumentException("Selecciona un mecánico");

        OrdenServicio o = new OrdenServicio();
        o.setDescripcion(descripcion.trim());
        o.setFechaEntrada(LocalDateTime.now());
        o.setEstado("PENDIENTE");
        o.setCostoTotal(0.0);
        o.setIdVehiculo(idVehiculo);
        o.setIdMecanico(idMecanico);
        o.setIdRecepcionista(recepcionista.getId());

        ordenDAO.insertar(o);
    }

    public List<OrdenServicio> getOrdenes() throws SQLException {
        return ordenDAO.listar();
    }

    public List<OrdenServicio> getOrdenesPorEstado(String estado) throws SQLException {
        return ordenDAO.listarPorEstado(estado);
    }

    public void cambiarEstado(int id, String nuevoEstado) throws SQLException {
        List<String> estadosValidos = List.of("PENDIENTE", "EN_PROCESO", "COMPLETADA", "CANCELADA");
        if (!estadosValidos.contains(nuevoEstado))
            throw new IllegalArgumentException("Estado no válido");

        ordenDAO.actualizarEstado(id, nuevoEstado);
    }

    public void actualizarCosto(int id, String costoStr) throws SQLException {
        double costo;
        try {
            costo = Double.parseDouble(costoStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El costo debe ser un número válido");
        }
        if (costo < 0)
            throw new IllegalArgumentException("El costo no puede ser negativo");

        ordenDAO.actualizarCosto(id, costo);
    }

    public void cerrarOrden(int id) throws SQLException {
        ordenDAO.cerrarOrden(id);
    }
}