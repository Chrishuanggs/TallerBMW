package view;

import com.formdev.flatlaf.FlatDarkLaf;
import controller.ClienteController;
import controller.VehiculoController;
import model.Cliente;
import model.Vehiculo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VehiculoView extends JFrame {

    private VehiculoController controller;
    private ClienteController clienteController;

    private JTextField txtPlaca, txtMarca, txtModelo, txtAnio, txtColor, txtVin, txtBuscar;
    private JComboBox<String> cmbCliente;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblStatus;
    private int idSeleccionado = -1;
    private List<Cliente> clientes;

    private static final Color ACCENT     = new Color(41, 128, 185);
    private static final Color DANGER     = new Color(192, 57, 43);
    private static final Color SUCCESS    = new Color(39, 174, 96);
    private static final Color BG_PANEL   = new Color(30, 30, 30);
    private static final Color BG_CARD    = new Color(40, 40, 40);
    private static final Color TEXT_MUTED = new Color(150, 150, 150);

    public VehiculoView() {
        controller = new VehiculoController();
        clienteController = new ClienteController();
        setTitle("Taller BMW — Vehiculos");
        setSize(1000, 640);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        cargarClientes();
        cargarTabla();
    }

    private void initUI() {
        getContentPane().setBackground(BG_PANEL);
        setLayout(new BorderLayout(0, 0));
        add(buildHeader(),    BorderLayout.NORTH);
        add(buildCentro(),    BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setBackground(BG_CARD);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("Gestion de Vehiculos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        JPanel buscarPanel = new JPanel(new BorderLayout(8, 0));
        buscarPanel.setBackground(BG_CARD);
        txtBuscar = styledField("Buscar por placa, marca o modelo...");
        JButton btnBuscar = roundButton("Buscar", ACCENT);
        btnBuscar.addActionListener(e -> buscar());
        buscarPanel.add(txtBuscar, BorderLayout.CENTER);
        buscarPanel.add(btnBuscar, BorderLayout.EAST);
        buscarPanel.setPreferredSize(new Dimension(380, 35));

        header.add(titulo,      BorderLayout.WEST);
        header.add(buscarPanel, BorderLayout.EAST);
        return header;
    }

    private JPanel buildCentro() {
        JPanel centro = new JPanel(new BorderLayout(15, 0));
        centro.setBackground(BG_PANEL);
        centro.setBorder(new EmptyBorder(15, 20, 10, 20));
        centro.add(buildFormulario(), BorderLayout.WEST);
        centro.add(buildTabla(),      BorderLayout.CENTER);
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

        JLabel lblForm = new JLabel("Datos del Vehiculo");
        lblForm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblForm.setForeground(Color.WHITE);
        lblForm.setAlignmentX(LEFT_ALIGNMENT);

        txtPlaca  = styledField(""); txtMarca  = styledField("");
        txtModelo = styledField(""); txtAnio   = styledField("");
        txtColor  = styledField(""); txtVin    = styledField("");
        cmbCliente = new JComboBox<>();
        cmbCliente.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        card.add(lblForm);
        card.add(Box.createVerticalStrut(15));
        card.add(buildFieldGroup("Placa *",    txtPlaca));
        card.add(Box.createVerticalStrut(8));
        card.add(buildFieldGroup("Marca *",    txtMarca));
        card.add(Box.createVerticalStrut(8));
        card.add(buildFieldGroup("Modelo *",   txtModelo));
        card.add(Box.createVerticalStrut(8));
        card.add(buildFieldGroup("Anio *",     txtAnio));
        card.add(Box.createVerticalStrut(8));
        card.add(buildFieldGroup("Color",      txtColor));
        card.add(Box.createVerticalStrut(8));
        card.add(buildFieldGroup("VIN",        txtVin));
        card.add(Box.createVerticalStrut(8));
        card.add(buildComboGroup("Cliente *",  cmbCliente));
        card.add(Box.createVerticalStrut(20));
        card.add(buildBotones());
        return card;
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

    private JPanel buildBotones() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.setBackground(BG_CARD);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JButton btnGuardar    = roundButton("Guardar",    SUCCESS);
        JButton btnActualizar = roundButton("Actualizar", ACCENT);
        JButton btnEliminar   = roundButton("Eliminar",   DANGER);
        JButton btnLimpiar    = roundButton("Limpiar",    new Color(80, 80, 80));

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        panel.add(btnGuardar); panel.add(btnActualizar);
        panel.add(btnEliminar); panel.add(btnLimpiar);
        return panel;
    }

    private JScrollPane buildTabla() {
        String[] columnas = {"ID", "Placa", "Marca", "Modelo", "Anio", "Color", "VIN", "Cliente"};
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

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (sel) { setBackground(ACCENT); setForeground(Color.WHITE); }
                else { setBackground(row % 2 == 0 ? BG_CARD : new Color(45, 45, 45)); setForeground(Color.WHITE); }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
                txtPlaca.setText((String) modeloTabla.getValueAt(fila, 1));
                txtMarca.setText((String) modeloTabla.getValueAt(fila, 2));
                txtModelo.setText((String) modeloTabla.getValueAt(fila, 3));
                txtAnio.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
                txtColor.setText((String) modeloTabla.getValueAt(fila, 5));
                txtVin.setText((String) modeloTabla.getValueAt(fila, 6));
                String nombreCliente = (String) modeloTabla.getValueAt(fila, 7);
                for (int i = 0; i < cmbCliente.getItemCount(); i++) {
                    if (cmbCliente.getItemAt(i).equals(nombreCliente)) { cmbCliente.setSelectedIndex(i); break; }
                }
                setStatus("Vehiculo seleccionado: " + txtPlaca.getText(), TEXT_MUTED);
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

    private void cargarClientes() {
        try {
            clientes = clienteController.getClientes();
            cmbCliente.removeAllItems();
            for (Cliente c : clientes) cmbCliente.addItem(c.getNombre());
        } catch (Exception ex) {
            setStatus("Error cargando clientes: " + ex.getMessage(), DANGER);
        }
    }

    private int getClienteIdSeleccionado() {
        int index = cmbCliente.getSelectedIndex();
        if (index < 0 || clientes == null) return -1;
        return clientes.get(index).getId();
    }

    private void guardar() {
        try {
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int idCliente = getClienteIdSeleccionado();
            if (idCliente == -1) throw new IllegalArgumentException("Selecciona un cliente");
            controller.registrar(txtPlaca.getText(), txtMarca.getText(), txtModelo.getText(), anio, txtColor.getText(), txtVin.getText(), idCliente);
            cargarTabla(); limpiar();
            setStatus("Vehiculo registrado correctamente", SUCCESS);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El anio debe ser un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), DANGER);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (idSeleccionado == -1) { JOptionPane.showMessageDialog(this, "Selecciona un vehiculo primero"); return; }
        try {
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int idCliente = getClienteIdSeleccionado();
            if (idCliente == -1) throw new IllegalArgumentException("Selecciona un cliente");
            controller.actualizar(idSeleccionado, txtPlaca.getText(), txtMarca.getText(), txtModelo.getText(), anio, txtColor.getText(), txtVin.getText(), idCliente);
            cargarTabla(); limpiar();
            setStatus("Vehiculo actualizado correctamente", SUCCESS);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El anio debe ser un numero valido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), DANGER);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) { JOptionPane.showMessageDialog(this, "Selecciona un vehiculo primero"); return; }
        int ok = JOptionPane.showConfirmDialog(this, "Eliminar este vehiculo?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            try {
                controller.eliminar(idSeleccionado);
                cargarTabla(); limpiar();
                setStatus("Vehiculo eliminado", TEXT_MUTED);
            } catch (Exception ex) {
                setStatus("Error: " + ex.getMessage(), DANGER);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        modeloTabla.setRowCount(0);
        try {
            List<Vehiculo> lista = controller.getVehiculos();
            for (Vehiculo v : lista) {
                if (v.getPlaca().toLowerCase().contains(texto) || v.getMarca().toLowerCase().contains(texto) || v.getModelo().toLowerCase().contains(texto))
                    agregarFila(v);
            }
            setStatus(modeloTabla.getRowCount() + " resultado(s)", TEXT_MUTED);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), DANGER);
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Vehiculo> lista = controller.getVehiculos();
            for (Vehiculo v : lista) agregarFila(v);
            setStatus(lista.size() + " vehiculo(s) registrado(s)", TEXT_MUTED);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), DANGER);
        }
    }

    private void agregarFila(Vehiculo v) {
        modeloTabla.addRow(new Object[]{v.getId(), v.getPlaca(), v.getMarca(), v.getModelo(), v.getAnio(), v.getColor(), v.getVin(), v.getNombreCliente()});
    }

    private void limpiar() {
        txtPlaca.setText(""); txtMarca.setText(""); txtModelo.setText("");
        txtAnio.setText(""); txtColor.setText(""); txtVin.setText(""); txtBuscar.setText("");
        if (cmbCliente.getItemCount() > 0) cmbCliente.setSelectedIndex(0);
        idSeleccionado = -1;
        tabla.clearSelection();
    }

    private JTextField styledField(String p) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.putClientProperty("JTextField.placeholderText", p);
        return f;
    }

    private JButton roundButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setStatus(String msg, Color color) { lblStatus.setText(msg); lblStatus.setForeground(color); }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> new VehiculoView().setVisible(true));
    }
}
