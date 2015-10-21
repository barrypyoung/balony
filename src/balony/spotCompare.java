/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balony;

import java.awt.BasicStroke;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultListModel;

/**
 *
 * @author Barry Young
 */
public class spotCompare extends javax.swing.JFrame {

    Balony balony;
    HashMap<String, Double> val;
    HashMap<String, Double> err;
    HashMap<String, String> lowHits;
    HashMap<String, String> highHits;

    String plot = "Ratio";
    int currPlate, currRow, currCol;

    private static final Color[] colors = {
        new Color(237, 125, 49),
        new Color(91, 155, 213),
        new Color(112, 173, 71),
        new Color(255, 192, 0),
        new Color(158, 72, 14),
        new Color(165, 165, 165),
        new Color(241, 151, 90),
        new Color(255, 205, 51),
        new Color(105, 142, 208),
        new Color(140, 193, 104)
    };

    /**
     * Creates new form spotCompare
     */
    public spotCompare() {

        initComponents();
        jList1.setCellRenderer(new cellRend());
    }

    public class cellRend extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (index > -1 && index < colors.length) {
                c.setBackground(colors[index]);
            }
            return c;
        }

    }

    public void setScreens(ArrayList<String> screens) {
        DefaultListModel lmo = (DefaultListModel) jList1.getModel();
        for (String s : screens) {
            lmo.addElement(s);
        }
    }

    public void setPosition(int plate, int row, int column) {
        currPlate = plate;
        currRow = row;
        currCol = column;

        jLabel1.setText("Plate: " + plate);
        jLabel2.setText("Row:  " + row);
        jLabel3.setText("Column: " + column);

        val = new HashMap<String, Double>();
        err = new HashMap<String, Double>();
        lowHits = new HashMap<String, String>();
        highHits = new HashMap<String, String>();

        int j = 0;

        DefaultListModel lmo = (DefaultListModel) jList1.getModel();
        Object[] screens = lmo.toArray();

        for (Object a : screens) {
            dataTable dt = balony.aD.get(a.toString()).getDt();
            for (int i = 0; i < dt.analysisTable.getModel().getRowCount(); i++) {
                try {
                    if ((Integer) dt.analysisTable.getValueAt(i, dt.COL_PLATE) == plate
                            && (Integer) dt.analysisTable.getValueAt(i, dt.COL_ROW) == row
                            && (Integer) dt.analysisTable.getValueAt(i, dt.COL_COL) == column) {

                        dt.analysisTable.setRowSelectionInterval(i, i);
                        dt.analysisTable.scrollRectToVisible(dt.analysisTable.getCellRect(i, 0, true));

                        jLabel4.setText(dt.analysisTable.getValueAt(i, dt.COL_GENE).toString()
                                + " (" + dt.analysisTable.getValueAt(i, dt.COL_ORF).toString()
                                + ")");

                        // Get appropriate value, plot.
                        plot = jComboBox1.getSelectedItem().toString();

                        if (plot.equals("Ratio")) {
                            Object o = dt.analysisTable.getValueAt(i, dt.COL_RATIO);
                            if (o instanceof String) {
                                val.put(a.toString(), 0d);
                            } else {
                                val.put(a.toString(), (Double) o);
                            }

                            o = dt.analysisTable.getValueAt(i, dt.COL_RATIO_SD);
                            if (o instanceof String) {
                                err.put(a.toString(), 0d);
                            } else {
                                err.put(a.toString(), (Double) o);
                            }
                        }

                        if (plot.equals("Diff")) {
                            Object o = dt.analysisTable.getValueAt(i, dt.COL_DIFF);
                            if (o instanceof String) {
                                val.put(a.toString(), 0d);
                            } else {
                                val.put(a.toString(), (Double) o);
                            }
                            err.put(a.toString(), (Double) o);
                        }

                        if (plot.equals("Ctrl")) {
                            val.put(a.toString(), (Double) dt.analysisTable.getValueAt(i, dt.COL_CTRL));
                            err.put(a.toString(), (Double) dt.analysisTable.getValueAt(i, dt.COL_CTRL_SD));
                        }

                        if (plot.equals("Exp")) {
                            val.put(a.toString(), (Double) dt.analysisTable.getValueAt(i, dt.COL_EXP));
                            err.put(a.toString(), (Double) dt.analysisTable.getValueAt(i, dt.COL_EXP_SD));
                        }

                        Object o = dt.analysisTable.getValueAt(i, dt.COL_SL);
                        lowHits.put(a.toString(), o.toString());

                        o = dt.analysisTable.getValueAt(i, dt.COL_SR);
                        highHits.put(a.toString(), o.toString());

                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    val.put(a.toString(), 0d);
                    err.put(a.toString(), 0d);
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }

                j++;
            }

        }

        updatePanel(jPanel1.getGraphics());

    }

    public void updatePanel(Graphics ga) {

        Graphics2D g = (Graphics2D) ga;

        int width = jPanel1.getWidth();
        int height = jPanel1.getHeight();

        g.setBackground(Color.white);
        g.clearRect(0, 0, width, height);

        Double maxVal;
        Double minVal;

        if (plot.equals("Diff")) {
            maxVal = 1.0d;
            minVal = maxVal * -1;
        } else {
            maxVal = 2.0d;
            minVal = 0.0d;
        }

        Double range = maxVal - minVal;

        double colWidth = (width - 80) / (1.5 * val.size());
        System.out.println("Width: " + width + "; ColWidth: " + colWidth);
        double colHeight = (height - 80);

        int cnt = 0;
        int ccnt = 0;

        DefaultListModel lmo = (DefaultListModel) jList1.getModel();
        FontMetrics f = getFontMetrics(g.getFont());

        g.setColor(Color.BLACK);
        g.setPaint(Color.BLACK);

        g.drawLine(30, 40, 30, height - 40);
        if (plot.equals("Diff")) {
            g.drawLine(30, (int) (40 + colHeight * (maxVal / range)),
                    width - 30, (int) (40 + colHeight * (maxVal / range)));
        } else {
            g.drawLine(30, height - 40, width - 30, height - 40);
        }

        Rectangle2D r = f.getStringBounds(minVal.toString(), g);
        g.drawString(minVal.toString(), (int) (30 - r.getWidth()) - 3, height - 37);

        if (plot.equals("Diff")) {
            r = f.getStringBounds("0.0", g);
            g.drawString("0.0", (int) (30 - r.getWidth()) - 3, (int) (height - 37 - colHeight / 2));
        } else {
            r = f.getStringBounds(Double.toString((maxVal - minVal) / 2), g);
            g.drawString(Double.toString((maxVal - minVal) / 2), (int) (30 - r.getWidth()) - 3, (int) (height - 37 - colHeight / 2));
        }

        r = f.getStringBounds(maxVal.toString(), g);
        g.drawString(maxVal.toString(), (int) (30 - r.getWidth()) - 3, (int) (height - 37 - colHeight));

        if (!plot.equals("Diff")) {
            float[] dash = {10.0f, 5.0f};
            g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 10.0f, dash, 0.0f));
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(30,
                    (int) ((height - 40) - colHeight * (1.0 / range)),
                    width - 30,
                    (int) ((height - 40) - colHeight * (1.0 / range)));
        }

        for (Object o : lmo.toArray()) {

            Double v = val.get(o.toString());

            Rectangle r2;
            if (plot.equals("Diff") && v < 0) {
                r2 = new Rectangle(
                        (int) (40 + colWidth * 1.5 * cnt),
                        (int) (40 + (colHeight / range)),
                        (int) colWidth,
                        (int) (colHeight * v / range * -1));
            } else {

                r2 = new Rectangle(
                        (int) (40 + colWidth * 1.5 * cnt),
                        (int) (40 + colHeight * (maxVal / range) - (colHeight * v / range)),
                        (int) colWidth,
                        (int) (colHeight * v / range));
            }

            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1.0f));

            if (ccnt == colors.length) {
                ccnt = 0;
            }

            g.setPaint(colors[ccnt]);

            g.fill(r2);
            g.setPaint(Color.BLACK);
            g.draw(r2);

            g.setColor(Color.BLACK);
            g.setPaint(Color.BLACK);

            if (err.size() > 0 && !plot.equals("Diff")) {
                Double e = err.get(o.toString());

                g.drawLine(
                        (int) (40 + colWidth * 1.5 * cnt + colWidth * 0.5),
                        (int) (40 + (colHeight - colHeight * (v / range)) + colHeight * (e / range)),
                        (int) (40 + colWidth * 1.5 * cnt + colWidth * 0.5),
                        (int) (40 + (colHeight - colHeight * (v / range)) - colHeight * (e / range)));

                g.drawLine(
                        (int) (40 + colWidth * 1.5 * cnt + colWidth * 0.4),
                        (int) (40 + (colHeight - colHeight * (v / range)) + colHeight * (e / range)),
                        (int) (40 + colWidth * 1.5 * cnt + colWidth * 0.6),
                        (int) (40 + (colHeight - colHeight * (v / range)) + colHeight * (e / range))
                );

                g.drawLine(
                        (int) (40 + colWidth * 1.5 * cnt + colWidth * 0.4),
                        (int) (40 + (colHeight - colHeight * (v / range)) - colHeight * (e / range)),
                        (int) (40 + colWidth * 1.5 * cnt + colWidth * 0.6),
                        (int) (40 + (colHeight - colHeight * (v / range)) - colHeight * (e / range))
                );

                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(3);
                String s = nf.format(v) + " ±";

                Rectangle2D r3 = f.getStringBounds(s, g);
                g.drawString(s,
                        (int) (40 + colWidth * 1.5 * cnt + colWidth * 0.5 - r3.getWidth() / 2),
                        (int) (40 + (colHeight - colHeight * (v / range)) - colHeight * (e / range) - r3.getHeight() * 1.5)
                );

                s = nf.format(e);
                r3 = f.getStringBounds(s, g);
                g.drawString(s,
                        (int) (40 + colWidth * 1.5 * cnt + colWidth * 0.5 - r3.getWidth() / 2),
                        (int) (40 + (colHeight - colHeight * (v / range)) - colHeight * (e / range) - r3.getHeight() * 0.5)
                );
            } else {
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(3);
                String s = nf.format(v);

                Rectangle2D r3 = f.getStringBounds(s, g);
                g.drawString(s,
                        (int) (40 + colWidth * 1.5 * cnt + colWidth * 0.5 - r3.getWidth() / 2),
                        (int) (r2.getY() - r3.getHeight())
                );
            }

            String hits = lowHits.get(o.toString());
            Rectangle2D r3 = f.getStringBounds(hits, g);
            g.setPaint(Color.green.darker());
            g.drawString(hits,
                    (int) (40 + colWidth * 1.5 * cnt + colWidth * 0.5 - r3.getWidth() / 2),
                    (int) (height - 35 + r3.getHeight())
            );

            hits = highHits.get(o.toString());
            r3 = f.getStringBounds(hits, g);
            g.setPaint(Color.RED);
            g.drawString(hits,
                    (int) (40 + colWidth * 1.5 * cnt + colWidth * 0.5 - r3.getWidth() / 2),
                    (int) (height - 35 + r3.getHeight() * 2)
            );

            cnt++;
            ccnt++;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel() {

            @Override public void paintComponent(Graphics g) {

                super.paintComponent(g);    // paints background
                updatePanel(g);

            }

        };
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 254, Short.MAX_VALUE)
        );

        jList1.setModel(new DefaultListModel());
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ratio", "Diff", "Ctrl", "Exp" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Plate:");

        jLabel2.setText("Row:");

        jLabel3.setText("Column:");

        jButton1.setText("Prev");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Next");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Move up");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Move down");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel4.setText("Gene (ORF)");

        jButton5.setText("Remove");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(20, 20, 20)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton2)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        setPosition(currPlate, currRow, currCol);
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int sel = jList1.getSelectedIndex();

        if (sel == 0) {
            return;
        }

        DefaultListModel lm = (DefaultListModel) jList1.getModel();
        Object tmp = lm.getElementAt(sel);
        lm.set(sel, lm.getElementAt(sel - 1));
        lm.set(sel - 1, tmp);

        jList1.setModel(lm);
        jList1.setSelectedIndex(sel - 1);

        updatePanel(jPanel1.getGraphics());

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int sel = jList1.getSelectedIndex();

        if (sel == (jList1.getModel().getSize() - 1)) {
            return;
        }

        DefaultListModel lm = (DefaultListModel) jList1.getModel();
        Object tmp = lm.getElementAt(sel);
        lm.set(sel, lm.getElementAt(sel + 1));
        lm.set(sel + 1, tmp);

        jList1.setModel(lm);
        jList1.setSelectedIndex(sel + 1);

        updatePanel(jPanel1.getGraphics());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (jList1.getModel().getSize() == 0) {
            return;
        }

        if (jList1.getSelectedIndex() == -1) {
            jList1.setSelectedIndex(0);
        }
        Object o = jList1.getSelectedValue();

        String scr = o.toString();

        dataTable dt = balony.aD.get(scr).getDt();

        int thisRow = 0;

        for (int i = 0; i < dt.analysisTable.getRowCount(); i++) {
            if ((Integer) dt.analysisTable.getValueAt(i, dt.COL_PLATE) == currPlate
                    && (Integer) dt.analysisTable.getValueAt(i, dt.COL_ROW) == currRow
                    && (Integer) dt.analysisTable.getValueAt(i, dt.COL_COL) == currCol) {
                thisRow = i;
            }
        }

        if (thisRow == dt.analysisTable.getRowCount() - 1) {
            return;
        }

        currPlate = (Integer) dt.analysisTable.getValueAt(thisRow + 1, dt.COL_PLATE);
        currRow = (Integer) dt.analysisTable.getValueAt(thisRow + 1, dt.COL_ROW);
        currCol = (Integer) dt.analysisTable.getValueAt(thisRow + 1, dt.COL_COL);

        setPosition(currPlate, currRow, currCol);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (jList1.getSelectedIndex() == -1) {
            jList1.setSelectedIndex(0);
        }
        Object o = jList1.getSelectedValue();

        String scr = o.toString();

        dataTable dt = balony.aD.get(scr).getDt();

        int thisRow = 0;

        for (int i = 0; i < dt.analysisTable.getRowCount(); i++) {
            if ((Integer) dt.analysisTable.getValueAt(i, dt.COL_PLATE) == currPlate
                    && (Integer) dt.analysisTable.getValueAt(i, dt.COL_ROW) == currRow
                    && (Integer) dt.analysisTable.getValueAt(i, dt.COL_COL) == currCol) {
                thisRow = i;
            }
        }

        if (thisRow == 0) {
            return;
        }

        currPlate = (Integer) dt.analysisTable.getValueAt(thisRow - 1, dt.COL_PLATE);
        currRow = (Integer) dt.analysisTable.getValueAt(thisRow - 1, dt.COL_ROW);
        currCol = (Integer) dt.analysisTable.getValueAt(thisRow - 1, dt.COL_COL);

        setPosition(currPlate, currRow, currCol);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (jList1.getSelectedIndex() == -1) {
            return;
        }

        DefaultListModel lmo = (DefaultListModel) jList1.getModel();
        lmo.remove(jList1.getSelectedIndex());
        jList1.setModel(lmo);
        setPosition(currPlate, currRow, currCol);
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(spotCompare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(spotCompare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(spotCompare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(spotCompare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new spotCompare().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
