package view;

import com.formdev.flatlaf.FlatDarkLaf;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainView extends JFrame {

    private Usuario usuarioActivo;
    private JPanel panelContenido;

    private static final Color BG_PANEL   = new Color(30, 30, 30);
    private static final Color BG_CARD    = new Color(40, 40, 40);
    private static final Color BG_SIDEBAR = new Color(25, 25, 25);
    private static final Color TEXT_MUTED = new Color(150, 150, 150);
    private static final Color HOVER      = new Color(50, 50, 50);

    public MainView(Usuario usuario) {
        this.usuarioActivo = usuario;
        setTitle("Taller BMW — " + usuario.getNombre());
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        getContentPane().setBackground(BG_PANEL);
        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        add(buildHeader(),  BorderLayout.NORTH);

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(BG_PANEL);
        panelContenido.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(panelContenido, BorderLayout.CENTER);

        mostrarBienvenida();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)),
                new EmptyBorder(12, 20, 12, 20)
        ));

        JLabel lblTitulo = new JLabel("Taller BMW");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        derecha.setBackground(BG_CARD);

        JLabel lblUsuario = new JLabel(usuarioActivo.getNombre() + "  |  " + usuarioActivo.getRol());
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsuario.setForeground(TEXT_MUTED);

        JButton btnCerrar = new JButton("Cerrar sesion");
        btnCerrar.setBackground(new Color(80, 80, 80));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> cerrarSesion());

        derecha.add(lblUsuario);
        derecha.add(btnCerrar);
        header.add(lblTitulo, BorderLayout.WEST);
        header.add(derecha,   BorderLayout.EAST);
        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(60, 60, 60)));

        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(menuLabel("MENU"));
        sidebar.add(Box.createVerticalStrut(10));

        sidebar.add(menuButton("Clientes",            () -> abrirClientes()));
        sidebar.add(menuButton("Vehiculos",           () -> abrirVehiculos()));
        sidebar.add(menuButton("Ordenes de Servicio", () -> abrirOrdenes()));

        if (!usuarioActivo.getRol().equals("RECEPCIONISTA")) {
            sidebar.add(menuButton("Inventario", () -> abrirInventario()));
        }

        if (usuarioActivo.getRol().equals("ADMIN")) {
            sidebar.add(menuButton("Usuarios", () -> abrirUsuarios()));
        }

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JLabel menuLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(TEXT_MUTED);
        lbl.setBorder(new EmptyBorder(0, 15, 5, 0));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton menuButton(String texto, Runnable accion) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(BG_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(HOVER); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(BG_SIDEBAR); }
        });
        btn.addActionListener(e -> accion.run());
        return btn;
    }

    private void mostrarBienvenida() {
        panelContenido.removeAll();
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(BG_PANEL);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                new EmptyBorder(40, 60, 40, 60)
        ));

        JLabel lbl1 = new JLabel("Bienvenido, " + usuarioActivo.getNombre());
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl1.setForeground(Color.WHITE);
        lbl1.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lbl2 = new JLabel("Rol: " + usuarioActivo.getRol());
        lbl2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl2.setForeground(TEXT_MUTED);
        lbl2.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lbl3 = new JLabel("Selecciona una opcion del menu para comenzar");
        lbl3.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl3.setForeground(TEXT_MUTED);
        lbl3.setAlignmentX(CENTER_ALIGNMENT);

        card.add(lbl1);
        card.add(Box.createVerticalStrut(8));
        card.add(lbl2);
        card.add(Box.createVerticalStrut(20));
        card.add(lbl3);

        centro.add(card);
        panelContenido.add(centro, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void abrirClientes() {
        panelContenido.removeAll();
        panelContenido.add(new ClienteView().getContentPane(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void abrirVehiculos() {
        panelContenido.removeAll();
        panelContenido.add(new VehiculoView().getContentPane(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void abrirOrdenes() {
        panelContenido.removeAll();
        panelContenido.add(new OrdenServicioView(usuarioActivo), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void abrirInventario() {
        panelContenido.removeAll();
        panelContenido.add(new InventarioView(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void abrirUsuarios() {
        panelContenido.removeAll();
        panelContenido.add(new UsuarioView(usuarioActivo).getContentPane(), BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void cerrarSesion() {
        int ok = JOptionPane.showConfirmDialog(this, "Cerrar sesion?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            new LoginView().setVisible(true);
            dispose();
        }
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        Usuario test = new Usuario(1, "Admin", "admin", "admin123", "ADMIN", true);
        SwingUtilities.invokeLater(() -> new MainView(test).setVisible(true));
    }
}
