package io.github.kings1990.plugin.fastrequest.view.component;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Used to create checkBox into the jTable header column
 *
 * @author Johnson
 */
public class CheckBoxHeader extends JCheckBox
        implements TableCellRenderer, MouseListener {
    protected CheckBoxHeader rendererComponent;
    protected int column;
    protected boolean mousePressed = false;

    public CheckBoxHeader(ItemListener itemListener) {
        rendererComponent = this;
        this.setSelected(true);
        rendererComponent.addItemListener(itemListener);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (table != null) {
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                header.addMouseListener(rendererComponent);
            }

        }
        setColumn(column);
        return rendererComponent;
    }

    protected void setColumn(int column) {
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    protected void handleClickEvent(MouseEvent e) {
        if (mousePressed) {
            mousePressed = false;
            JTableHeader header = (JTableHeader) (e.getSource());
            JTable tableView = header.getTable();
            TableColumnModel columnModel = tableView.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int checkBoxColumn = tableView.convertColumnIndexToModel(viewColumn);

            if (viewColumn == this.column && e.getClickCount() == 1 && checkBoxColumn != -1) {
                doClick();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        handleClickEvent(e);
        ((JTableHeader) e.getSource()).repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Do nothing here.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Do nothing here.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Do nothing here.
    }
}