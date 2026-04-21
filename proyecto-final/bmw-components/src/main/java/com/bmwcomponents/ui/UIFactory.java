package com.bmwcomponents.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Fabrica de componentes Swing reutilizables con tema oscuro.
 */
public class UIFactory {

    public static final Color ACCENT     = new Color(41, 128, 185);
    public static final Color DANGER     = new Color(192, 57, 43);
    public static final Color SUCCESS    = new Color(39, 174, 96);
    public static final Color WARNING    = new Color(211, 84, 0);
    public static final Color BG_PANEL   = new Color(30, 30, 30);
    public static final Color BG_CARD    = new Color(40, 40, 40);
    public static final Color TEXT_MUTED = new Color(150, 150, 150);

    private UIFactory() {}

    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JTextField createField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.putClientProperty("JTextField.placeholderText", placeholder);
        return f;
    }

    public static JPasswordField createPasswordField(String placeholder) {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.putClientProperty("JTextField.placeholderText", placeholder);
        return f;
    }

    public static JPanel createFieldGroup(String label, JComponent field) {
        JPanel group = new JPanel(new BorderLayout(0, 4));
        group.setBackground(BG_CARD);
        group.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        group.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);
        group.add(lbl, BorderLayout.NORTH);
        group.add(field, BorderLayout.CENTER);
        return group;
    }

    public static JPanel createComboGroup(String label, JComboBox<?> combo) {
        JPanel group = new JPanel(new BorderLayout(0, 4));
        group.setBackground(BG_CARD);
        group.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        group.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);
        group.add(lbl, BorderLayout.NORTH);
        group.add(combo, BorderLayout.CENTER);
        return group;
    }

    public static JLabel createStatusLabel() {
        JLabel lbl = new JLabel("Listo");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);
        return lbl;
    }

    public static JPanel createStatusBar(JLabel statusLabel) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        bar.setBackground(BG_CARD);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 60)));
        bar.add(statusLabel);
        return bar;
    }

    public static JPanel createCard(int preferredWidth) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                new EmptyBorder(20, 20, 20, 20)
        ));
        if (preferredWidth > 0)
            card.setPreferredSize(new Dimension(preferredWidth, 0));
        return card;
    }

    public static JPanel createHeader(String title, JComponent rightComponent) {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setBackground(BG_CARD);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.WEST);
        if (rightComponent != null)
            header.add(rightComponent, BorderLayout.EAST);
        return header;
    }
}
