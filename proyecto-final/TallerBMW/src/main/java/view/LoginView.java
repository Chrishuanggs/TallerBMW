package view;

import com.formdev.flatlaf.FlatDarkLaf;
import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginView extends JFrame {

    private UsuarioController controller;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JLabel lblError;

    private static final Color ACCENT     = new Color(41, 128, 185);
    private static final Color DANGER     = new Color(192, 57, 43);
    private static final Color BG_PANEL   = new Color(30, 30, 30);
    private static final Color BG_CARD    = new Color(40, 40, 40);
    private static final Color TEXT_MUTED = new Color(150, 150, 150);

    public LoginView() {
        controller = new UsuarioController();
        setTitle("Taller BMW — Login");
        setSize(420, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        getContentPane().setBackground(BG_PANEL);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                new EmptyBorder(35, 40, 35, 40)
        ));
        card.setPreferredSize(new Dimension(340, 380));

        JLabel lblTitulo = new JLabel("Taller BMW");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Sistema de Gestion");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(TEXT_MUTED);
        lblSub.setAlignmentX(CENTER_ALIGNMENT);

        txtUsuario  = styledField("Usuario");
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.putClientProperty("JTextField.placeholderText", "Contrasena");
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) intentarLogin();
            }
        });

        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setForeground(DANGER);
        lblError.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnLogin = roundButton("Ingresar", ACCENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.setAlignmentX(CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> intentarLogin());

        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(5));
        card.add(lblSub);
        card.add(Box.createVerticalStrut(30));
        card.add(buildFieldGroup("Usuario", txtUsuario));
        card.add(Box.createVerticalStrut(12));
        card.add(buildFieldGroup("Contrasena", txtPassword));
        card.add(Box.createVerticalStrut(8));
        card.add(lblError);
        card.add(Box.createVerticalStrut(15));
        card.add(btnLogin);

        add(card);
    }

    private void intentarLogin() {
        lblError.setText(" ");
        try {
            Usuario u = controller.login(txtUsuario.getText(), new String(txtPassword.getPassword()));
            new MainView(u).setVisible(true);
            dispose();
        } catch (Exception ex) {
            lblError.setText(ex.getMessage());
            txtPassword.setText("");
        }
    }

    private JPanel buildFieldGroup(String label, JComponent field) {
        JPanel group = new JPanel(new BorderLayout(0, 4));
        group.setBackground(BG_CARD);
        group.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        group.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);
        group.add(lbl, BorderLayout.NORTH);
        group.add(field, BorderLayout.CENTER);
        return group;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.putClientProperty("JTextField.placeholderText", placeholder);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return f;
    }

    private JButton roundButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
