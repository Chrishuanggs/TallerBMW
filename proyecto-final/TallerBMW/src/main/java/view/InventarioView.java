package view;

import com.bmwcomponents.ui.GenericTablePanel;
import com.bmwcomponents.ui.UIFactory;
import controller.InventarioController;
import model.Inventario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class InventarioView extends JPanel {

    private InventarioController controller;
    private GenericTablePanel tablePanel;

    private JTextField txtNombre, txtDescripcion, txtCantidad, txtPrecio, txtStockMin, txtBuscar;
    private JLabel lblStatus;
    private int idSeleccionado = -1;

    public InventarioView() {
        controller = new InventarioController();
        setLayout(new BorderLayout(0, 0));
        setBackground(UIFactory.BG_PANEL);
        initUI();
        cargarTabla();
    }

    private void initUI() {
        add(buildHeader(),    BorderLayout.NORTH);
        add(buildCentro(),    BorderLayout.CENTER);
        lblStatus = UIFactory.createStatusLabel();
        add(UIFactory.createStatusBar(lblStatus), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel buscarPanel = new JPanel(new BorderLayout(8, 0));
        buscarPanel.setBackground(UIFactory.BG_CARD);
        txtBuscar = UIFactory.createField("Buscar por nombre...");
        JButton btnBuscar = UIFactory.createButton("Buscar", UIFactory.ACCENT);
        btnBuscar.addActionListener(e -> buscar());
        buscarPanel.add(txtBuscar, BorderLayout.CENTER);
        buscarPanel.add(btnBuscar, BorderLayout.EAST);
        buscarPanel.setPreferredSize(new Dimension(320, 35));
        return UIFactory.createHeader("Inventario de Repuestos", buscarPanel);
    }

    private JPanel buildCentro() {
        JPanel centro = new JPanel(new BorderLayout(15, 0));
        centro.setBackground(UIFactory.BG_PANEL);
        centro.setBorder(new EmptyBorder(15, 20, 10, 20));
        centro.add(buildFormulario(), BorderLayout.WEST);
        centro.add(buildTabla(),      BorderLayout.CENTER);
        return centro;
    }

    private JPanel buildFormulario() {
        JPanel card = UIFactory.createCard(290);

        JLabel lblForm = new JLabel("Datos del Item");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblForm.setForeground(Color.WHITE);
        lblForm.setAlignmentX(LEFT_ALIGNMENT);

        txtNombre      = UIFactory.createField("");
        txtDescripcion = UIFactory.createField("");
        txtCantidad    = UIFactory.createField("0");
        txtPrecio      = UIFactory.createField("0.00");
        txtStockMin    = UIFactory.createField("5");

        card.add(lblForm);
        card.add(Box.createVerticalStrut(15));
        card.add(UIFactory.createFieldGroup("Nombre *",              txtNombre));
        card.add(Box.createVerticalStrut(8));
        card.add(UIFactory.createFieldGroup("Descripcion",           txtDescripcion));
        card.add(Box.createVerticalStrut(8));
        card.add(UIFactory.createFieldGroup("Cantidad",              txtCantidad));
        card.add(Box.createVerticalStrut(8));
        card.add(UIFactory.createFieldGroup("Precio Unitario (₡)",   txtPrecio));
        card.add(Box.createVerticalStrut(8));
        card.add(UIFactory.createFieldGroup("Stock Minimo",          txtStockMin));
        card.add(Box.createVerticalStrut(20));
        card.add(buildBotones());
        return card;
    }

    private JPanel buildBotones() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBackground(UIFactory.BG_CARD);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JButton btnGuardar    = UIFactory.createButton("Guardar",       UIFactory.SUCCESS);
        JButton btnActualizar = UIFactory.createButton("Actualizar",    UIFactory.ACCENT);
        JButton btnEliminar   = UIFactory.createButton("Eliminar",      UIFactory.DANGER);
        JButton btnLimpiar    = UIFactory.createButton("Limpiar",       new Color(80, 80, 80));
        JButton btnEntrada    = UIFactory.createButton("+ Entrada",     new Color(30, 120, 70));
        JButton btnSalida     = UIFactory.createButton("- Salida",      UIFactory.WARNING);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
        btnEntrada.addActionListener(e -> ajustarStock(true));
        btnSalida.addActionListener(e -> ajustarStock(false));

        panel.add(btnGuardar);  panel.add(btnActualizar);
        panel.add(btnEntrada);  panel.add(btnSalida);
        panel.add(btnEliminar); panel.add(btnLimpiar);
        return panel;
    }

    private GenericTablePanel buildTabla() {
        String[] columnas = {"ID", "Nombre", "Descripcion", "Cantidad", "Precio (₡)", "Stock Min", "Estado"};
        tablePanel = new GenericTablePanel(columnas);

        tablePanel.applyDefaultRenderer((model, row) -> {
            Object estado = model.getValueAt(row, 6);
            if ("BAJO STOCK".equals(estado)) return new Color(70, 30, 30);
            return null;
        });

        tablePanel.setColumnMaxWidth(0, 45);
        tablePanel.setColumnMinWidth(3, 70);
        tablePanel.setColumnMinWidth(4, 100);

        tablePanel.addSelectionListener(e -> {
            int fila = tablePanel.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = (int) tablePanel.getValueAt(fila, 0);
                txtNombre.setText((String) tablePanel.getValueAt(fila, 1));
                txtDescripcion.setText((String) tablePanel.getValueAt(fila, 2));
                txtCantidad.setText(String.valueOf(tablePanel.getValueAt(fila, 3)));
                txtStockMin.setText(String.valueOf(tablePanel.getValueAt(fila, 5)));
                String raw = tablePanel.getValueAt(fila, 4).toString().replaceAll("[^\\d.]", "");
                txtPrecio.setText(raw);
                setStatus("Item seleccionado: " + tablePanel.getValueAt(fila, 1), UIFactory.TEXT_MUTED);
            }
        });

        return tablePanel;
    }

    private void cargarTabla() {
        tablePanel.clearRows();
        try {
            List<Inventario> items = controller.getItems();
            for (Inventario item : items) agregarFila(item);
            long bajoStock = items.stream().filter(Inventario::isBajoStock).count();
            String msg = items.size() + " item(s) en inventario";
            if (bajoStock > 0) msg += "  —  ⚠ " + bajoStock + " con stock bajo";
            setStatus(msg, bajoStock > 0 ? UIFactory.WARNING : UIFactory.TEXT_MUTED);
        } catch (Exception ex) {
            setStatus("Error cargando inventario: " + ex.getMessage(), UIFactory.DANGER);
        }
    }

    private void agregarFila(Inventario item) {
        tablePanel.addRow(new Object[]{
                item.getId(), item.getNombre(), item.getDescripcion(),
                item.getCantidad(), String.format("%.2f", item.getPrecioUnitario()),
                item.getStockMinimo(), item.isBajoStock() ? "BAJO STOCK" : "OK"
        });
    }

    private void guardar() {
        try {
            controller.registrar(txtNombre.getText(), txtDescripcion.getText(),
                    txtCantidad.getText(), txtPrecio.getText(), txtStockMin.getText());
            cargarTabla(); limpiar();
            setStatus("Item registrado correctamente", UIFactory.SUCCESS);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), UIFactory.DANGER);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (idSeleccionado == -1) { JOptionPane.showMessageDialog(this, "Selecciona un item primero"); return; }
        try {
            controller.actualizar(idSeleccionado, txtNombre.getText(), txtDescripcion.getText(),
                    txtCantidad.getText(), txtPrecio.getText(), txtStockMin.getText());
            cargarTabla(); limpiar();
            setStatus("Item actualizado correctamente", UIFactory.SUCCESS);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), UIFactory.DANGER);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) { JOptionPane.showMessageDialog(this, "Selecciona un item primero"); return; }
        int ok = JOptionPane.showConfirmDialog(this, "Eliminar este item del inventario?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            try {
                controller.eliminar(idSeleccionado);
                cargarTabla(); limpiar();
                setStatus("Item eliminado", UIFactory.TEXT_MUTED);
            } catch (Exception ex) {
                setStatus("Error: " + ex.getMessage(), UIFactory.DANGER);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ajustarStock(boolean esSuma) {
        if (idSeleccionado == -1) { JOptionPane.showMessageDialog(this, "Selecciona un item primero"); return; }
        String tipo = esSuma ? "ENTRADA (agregar)" : "SALIDA (retirar)";
        String delta = JOptionPane.showInputDialog(this, "Cantidad para " + tipo + ":", "Ajuste de Stock", JOptionPane.PLAIN_MESSAGE);
        if (delta == null || delta.trim().isEmpty()) return;
        try {
            controller.ajustarStock(idSeleccionado, delta, esSuma);
            cargarTabla();
            setStatus("Stock ajustado correctamente", UIFactory.SUCCESS);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), UIFactory.DANGER);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        tablePanel.clearRows();
        try {
            List<Inventario> items = controller.getItems();
            for (Inventario item : items) {
                if (item.getNombre().toLowerCase().contains(texto) ||
                    (item.getDescripcion() != null && item.getDescripcion().toLowerCase().contains(texto)))
                    agregarFila(item);
            }
            setStatus(tablePanel.getRowCount() + " resultado(s)", UIFactory.TEXT_MUTED);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), UIFactory.DANGER);
        }
    }

    private void limpiar() {
        txtNombre.setText(""); txtDescripcion.setText(""); txtCantidad.setText("0");
        txtPrecio.setText("0.00"); txtStockMin.setText("5"); txtBuscar.setText("");
        idSeleccionado = -1;
        tablePanel.clearSelection();
    }

    private void setStatus(String msg, Color color) { lblStatus.setText(msg); lblStatus.setForeground(color); }
}
