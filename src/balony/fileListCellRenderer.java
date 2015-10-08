/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package balony;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Barry Young
 */
public class fileListCellRenderer extends DefaultListCellRenderer {

    public quantSettings quantP;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        File f = (File) value;
        File[] files;
        files = f.getParentFile().listFiles(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt") && name.contains("RAW");
            }
        });

        int allowed = 0;
        try {
            allowed = Integer.parseInt(quantP.allowBadSpotsJTextField.getText());
        } catch (Exception e) {
            System.out.println("Error getting bad spots");
            System.out.println(e.getLocalizedMessage());
        }

        Color fg = Color.black;
        String flag = "";
        boolean found=false;
        for(File ff : files) {

            if(found) {
                continue;
            }
            
            flag = " [*]";
            fg = new Color(0, 150, 0);
            try {
                BufferedReader bf = new BufferedReader(new FileReader(ff));
                String t;
                t = bf.readLine();
                while (t != null) {
                    if (t.startsWith("Bad spots: ")) {
                        String tt = t.substring(10, t.length()).trim();
                        int bs = Integer.parseInt(tt);
                        if (bs <= allowed) {
                            flag = " [?]";
                            fg = Color.orange;
                        } else {
                            flag = " [!]";
                            fg = Color.red;
                        }
                    }

                    if(t.startsWith("Source file:")) {
                        if(f.getAbsolutePath().equals(t.substring(13))) {
                            found=true;
                        }
                    }

                    t = bf.readLine();
                }
                bf.close();

            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (NumberFormatException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }

        if(!found) {
            flag = "";
            fg=Color.black;
        }
        
        Component c = super.getListCellRendererComponent(list,
                f.getName() + flag, index, isSelected,
                cellHasFocus);
        if (isSelected) {
            c.setForeground(Color.white);
        } else {

            c.setForeground(fg);
        }

        return c;
    }
}
