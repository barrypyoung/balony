/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package balony;

import java.awt.Component;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Barry Young
 */
    public class fileList2CellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            File f = (File) value;

            Component c = super.getListCellRendererComponent(list, f.getName(), index, isSelected, cellHasFocus);

            return c;
        }
    }
