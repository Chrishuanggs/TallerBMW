package view;

import controller.OrdenServicioController;
import controller.VehiculoController;
import dao.UsuarioDAO;
import model.OrdenServicio;
import model.Usuario;
import model.Vehiculo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrdenServicioView extends JPanel {

    private OrdenServicioController controller;
    private VehiculoController vehiculoController;

    private JTextArea txtDescripcion;
    private JComboBox<String> cmbVehiculo, cmbMecanico, cmbFiltroEstado;
    private JTextField txtCosto, txtBuscar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblStatus;
    private int idSeleccionado = -1;

    private List<Vehiculo> vehiculos;
    private List<Usuario> mecanicos;
    private Usuario usuarioActivo;

    private static final Color ACCENT      = new Color(41, 128, 185);
    private static final Color DANGER      = new Color(192, 57, 43);
    private static final Color SUCCESS     = new Color(39, 174, 96);
    private static final Color WARNING     = new Color(211, 84, 0);
    private static final Color BG_PANEL    = new Color(30, 30, 30);
    private static final Color BG_CARD     = new Color(40, 40, 40);
    private static final Color TEXT_MUTED  = new Color(150, 150, 150);

    public OrdenServicioView(Usuario usuario) {
        this.usuarioActivo = usuario;
        controller = new OrdenServicioController();
        vehiculoController = new VehiculoController();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_PANEL);
        initUI();
        cargarCombos();
        cargarTabla();
    }

    private void initUI() {
        add(buildHeader(), BorderLayout.NORTH);
        add(buildCentro(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setBackground(BG_CARD);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("Órdenes de Servicio");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filtros.setBackground(BG_CARD);

        cmbFiltroEstado = new JComboBox<>(new String[]{"TODOS", "PENDIENTE", "EN_PROCESO", "COMPLETADA", "CANCELADA"});
        cmbFiltroEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFiltroEstado.addActionListener(e -> filtrarPorEstado());

        JLabel lblFiltro = new JLabel("Filtrar:");
        lblFiltro.setForeground(TEXT_MUTED);
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        filtros.add(lblFiltro);
        filtros.add(cmbFiltroEstado);

        header.add(titulo, BorderLayout.WEST);
        header.add(filtros, BorderLayout.EAST);
        return header;
    }

    private JPanel buildCentro() {
        JPanel centro = new JPanel(new BorderLayout(15, 0));
        centro.setBackground(BG_PANEL);
        centro.setBorder(new EmptyBorder(15, 20, 10, 20));
        centro.add(buildFormulario(), BorderLayout.WEST);
        centro.add(buildTabla(), BorderLayout.CENTER);
        return centro;
    }

    private JPanel buildFormulario() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(300, 0));

        JLabel lblForm = new JLabel("Nueva Orden");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblForm.setForeground(Color.WHITE);
        lblForm.setAlignmentX(LEFT_ALIGNMENT);

        cmbVehiculo = new JComboBox<>();
        cmbVehiculo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cmbMecanico = new JComboBox<>();
        cmbMecanico.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        txtDescripcion = new JTextArea(4, 1);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(new Color(55, 55, 55));
        txtDescripcion.setForeground(Color.WHITE);
        txtDescripcion.setCaretColor(Color.WHITE);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        scrollDesc.setAlignmentX(LEFT_ALIGNMENT);

        txtCosto = styledField("0.00");

        card.add(lblForm);
        card.add(Box.createVerticalStrut(15));
        card.add(buildComboGroup("Vehículo *", cmbVehiculo));
        card.add(Box.createVerticalStrut(8));
        card.add(buildComboGroup("Mecánico *", cmbMecanico));
        card.add(Box.createVerticalStrut(8));
        card.add(buildLabelGroup("Descripción *"));
        card.add(Box.createVerticalStrut(4));
        card.add(scrollDesc);
        card.add(Box.createVerticalStrut(8));
        card.add(buildFieldGroup("Costo (₡)", txtCosto));
        card.add(Box.createVerticalStrut(20));
        card.add(buildBotones());

        return card;
    }

    private JPanel buildBotones() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBackground(BG_CARD);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JButton btnCrear     = roundButton("Crear Orden", SUCCESS);
        JButton btnEnProceso = roundButton("En Proceso", ACCENT);
        JButton btnCompletar = roundButton("Completar", new Color(39, 174, 96).darker());
        JButton btnCancelar  = roundButton("Cancelar", DANGER);
        JButton btnCosto     = roundButton("Actualizar Costo", WARNING);
        JButton btnLimpiar   = roundButton("Limpiar", new Color(80, 80, 80));

        btnCrear.addActionListener(e -> crearOrden());
        btnEnProceso.addActionListener(e -> cambiarEstado("EN_PROCESO"));
        btnCompletar.addActionListener(e -> completarOrden());
        btnCancelar.addActionListener(e -> cambiarEstado("CANCELADA"));
        btnCosto.addActionListener(e -> actualizarCosto());
        btnLimpiar.addActionListener(e -> limpiar());

        panel.add(btnCrear);    panel.add(btnLimpiar);
        panel.add(btnEnProceso); panel.add(btnCosto);
        panel.add(btnCompletar); panel.add(btnCancelar);
        return panel;
    }

    private JScrollPane buildTabla() {
        String[] columnas = {"ID", "Vehículo", "Cliente", "Mecánico", "Estado", "Costo", "Entrada", "Recepcionista"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(28);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getColumnModel().getColumn(0).setMaxWidth(45);
        tabla.getColumnModel().getColumn(4).setMinWidth(90);

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (sel) {
                    setBackground(ACCENT); setForeground(Color.WHITE);
                } else {
                    String estado = (String) modeloTabla.getValueAt(row, 4);
                    if ("COMPLETADA".equals(estado))      setBackground(new Color(30, 60, 40));
                    else if ("EN_PROCESO".equals(estado)) setBackground(new Color(30, 50, 70));
                    else if ("CANCELADA".equals(estado))  setBackground(new Color(60, 30, 30));
                    else setBackground(row % 2 == 0 ? BG_CARD : new Color(45, 45, 45));
                    setForeground(Color.WHITE);
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                txtCosto.setText(String.valueOf(modeloTabla.getValueAt(fila, 5)));
                setStatus("Orden #" + idSeleccionado + " seleccionada — Estado: " + modeloTabla.getValueAt(fila, 4), TEXT_MUTED);
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        return scroll;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        bar.setBackground(BG_CARD);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 60)));
        lblStatus = new JLabel("Listo");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(TEXT_MUTED);
        bar.add(lblStatus);
        return bar;
    }

    private void cargarCombos() {
        try {
            vehiculos = vehiculoController.getVehiculos();
            cmbVehiculo.removeAllItems();
            for (Vehiculo v : vehiculos)
                cmbVehiculo.addItem(v.getPlaca() + " — " + v.getNombreCliente());
        } catch (Exception ex) {
            setStatus("Error cargando vehículos: " + ex.getMessage(), DANGER);
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            mecanicos = usuarioDAO.listarPorRol("MECANICO");
            cmbMecanico.removeAllItems();
            for (Usuario u : mecanicos)
                cmbMecanico.addItem(u.getNombre());
        } catch (Exception ex) {
            setStatus("Error cargando mecánicos: " + ex.getMessage(), DANGER);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<OrdenServicio> ordenes = controller.getOrdenes();
            for (OrdenServicio o : ordenes) agregarFila(o);
            setStatus(ordenes.size() + " orden(es) registrada(s)", TEXT_MUTED);
        } catch (Exception ex) {
            setStatus("Error cargando órdenes: " + ex.getMessage(), DANGER);
        }
    }

    private void filtrarPorEstado() {
        String filtro = (String) cmbFiltroEstado.getSelectedItem();
        modeloTabla.setRowCount(0);
        try {
            List<OrdenServicio> ordenes = "TODOS".equals(filtro)
                    ? controller.getOrdenes()
                    : controller.getOrdenesPorEstado(filtro);
            for (OrdenServicio o : ordenes) agregarFila(o);
            setStatus(ordenes.size() + " orden(es) encontrada(s)", TEXT_MUTED);
        } catch (Exception ex) {
            setStatus("Error filtrando: " + ex.getMessage(), DANGER);
        }
    }

    private void agregarFila(OrdenServicio o) {
        modeloTabla.addRow(new Object[]{
                o.getId(),
                o.getPlacaVehiculo(),
                o.getNombreCliente(),
                o.getNombreMecanico() != null ? o.getNombreMecanico() : "—",
                o.getEstado(),
                String.format("₡%.2f", o.getCostoTotal()),
                o.getFechaEntrada().toLocalDate(),
                o.getNombreRecepcionista()
        });
    }

    private void crearOrden() {
        try {
            int idVehiculo = vehiculos.get(cmbVehiculo.getSelectedIndex()).getId();
            int idMecanico = mecanicos.get(cmbMecanico.getSelectedIndex()).getId();
            controller.crear(txtDescripcion.getText(), idVehiculo, idMecanico, usuarioActivo);
            cargarTabla(); limpiar();
            setStatus("Orden creada correctamente", SUCCESS);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), DANGER);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstado(String estado) {
        if (idSeleccionado == -1) { JOptionPane.showMessageDialog(this, "Selecciona una orden primero"); return; }
        try {
            controller.cambiarEstado(idSeleccionado, estado);
            cargarTabla();
            setStatus("Estado actualizado a: " + estado, SUCCESS);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), DANGER);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void completarOrden() {
        if (idSeleccionado == -1) { JOptionPane.showMessageDialog(this, "Selecciona una orden primero"); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Marcar esta orden como completada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.cerrarOrden(idSeleccionado);
                cargarTabla();
                setStatus("Orden completada", SUCCESS);
            } catch (Exception ex) {
                setStatus("Error: " + ex.getMessage(), DANGER);
            }
        }
    }

    private void actualizarCosto() {
        if (idSeleccionado == -1) { JOptionPane.showMessageDialog(this, "Selecciona una orden primero"); return; }
        try {
            controller.actualizarCosto(idSeleccionado, txtCosto.getText());
            cargarTabla();
            setStatus("Costo actualizado correctamente", SUCCESS);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), DANGER);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtDescripcion.setText("");
        txtCosto.setText("0.00");
        if (cmbVehiculo.getItemCount() > 0) cmbVehiculo.setSelectedIndex(0);
        if (cmbMecanico.getItemCount() > 0) cmbMecanico.setSelectedIndex(0);
        idSeleccionado = -1;
        tabla.clearSelection();
    }

    private JPanel buildFieldGroup(String label, JTextField field) {
        JPanel group = new JPanel(new BorderLayout(0, 4));
        group.setBackground(BG_CARD);
        group.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        group.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);
        group.add(lbl, BorderLayout.NORTH);
        group.add(field, BorderLayout.CENTER);
        return group;
    }

    private JPanel buildComboGroup(String label, JComboBox<String> combo) {
        JPanel group = new JPanel(new BorderLayout(0, 4));
        group.setBackground(BG_CARD);
        group.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        group.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);
        group.add(lbl, BorderLayout.NORTH);
        group.add(combo, BorderLayout.CENTER);
        return group;
    }

    private JLabel buildLabelGroup(String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.putClientProperty("JTextField.placeholderText", placeholder);
        return f;
    }

    private JButton roundButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setStatus(String msg, Color color) { lblStatus.setText(msg); lblStatus.setForeground(color); }
}