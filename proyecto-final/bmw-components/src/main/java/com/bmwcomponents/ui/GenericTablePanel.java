package com.bmwcomponents.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel de tabla generica reutilizable con tema oscuro.
 * Soporta RowHighlighter funcional para colorear filas por logica del modulo.
 */
public class GenericTablePanel extends JPanel {

    protected JTable table;
    protected DefaultTableModel model;

    public GenericTablePanel(String[] columns) {
        setLayout(new BorderLayout());
        setBackground(UIFactory.BG_PANEL);

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        applyDefaultRenderer(null);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        add(scroll, BorderLayout.CENTER);
    }

    public void applyDefaultRenderer(RowHighlighter highlighter) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (sel) {
                    setBackground(UIFactory.ACCENT);
                    setForeground(Color.WHITE);
                } else {
                    Color rowBg = (highlighter != null) ? highlighter.getBackground(model, row) : null;
                    setBackground(rowBg != null ? rowBg : (row % 2 == 0 ? UIFactory.BG_CARD : new Color(45, 45, 45)));
                    setForeground(Color.WHITE);
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }

    public void setColumnMaxWidth(int col, int width) { table.getColumnModel().getColumn(col).setMaxWidth(width); }
    public void setColumnMinWidth(int col, int width) { table.getColumnModel().getColumn(col).setMinWidth(width); }

    public void addRow(Object[] row)            { model.addRow(row); }
    public void clearRows()                     { model.setRowCount(0); }
    public int  getSelectedRow()                { return table.getSelectedRow(); }
    public Object getValueAt(int row, int col)  { return model.getValueAt(row, col); }
    public int  getRowCount()                   { return model.getRowCount(); }
    public void clearSelection()                { table.clearSelection(); }

    public void addSelectionListener(ListSelectionListener l) {
        table.getSelectionModel().addListSelectionListener(l);
    }

    @FunctionalInterface
    public interface RowHighlighter {
        Color getBackground(DefaultTableModel model, int row);
    }
}
