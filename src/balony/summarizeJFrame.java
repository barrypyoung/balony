/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balony;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.inference.TTest;

/**
 *
 * @author Barry Young
 */
public class summarizeJFrame extends javax.swing.JFrame implements ClipboardOwner {

    public analysisData myAD;
    public HashMap<String, sumData> summaryData;
    public String[][][] orfs;
    public String[][][] genes;

    public final int COL_ORF = 0;
    public final int COL_GENE = COL_ORF + 1;
    public final int COL_CTRL_N = COL_GENE + 1;
    public final int COL_CTRL = COL_CTRL_N + 1;
    public final int COL_CTRL_SD = COL_CTRL + 1;
    public final int COL_EXP_N = COL_CTRL_SD + 1;
    public final int COL_EXP = COL_EXP_N + 1;
    public final int COL_EXP_SD = COL_EXP + 1;
    public final int COL_RATIO = COL_EXP_SD + 1;
    public final int COL_RATIO_SD = COL_RATIO + 1;
    public final int COL_DIFF = COL_RATIO_SD + 1;
    public final int COL_DIFF_SD = COL_DIFF + 1;
    public final int COL_PVAL = COL_DIFF_SD + 1;

    public final String[] columnNames = {"ORF", "Gene", "Ctrl n",
        "Ctrl", "Ctrl SD", "Exp n", "Exp", "Exp SD", "Ratio", "Ratio SD",
        "Diff", "Diff SD", "p-value"};

    public Object tableData[][];

    /**
     * Creates new form summarizeJFrame
     */
    public summarizeJFrame() {
        initComponents();
        summaryData = new HashMap<>();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        pairedSpotsJCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 =    new javax.swing.JTable() {
            public TableCellRenderer getCellRenderer(int row, int column) {

                TableCellRenderer t=new myRenderer(row,column);
                return t;
            }

        };

        ;

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Median", "Mean" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Spot averaging:");

        jButton1.setText("Export...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Clipboard copy"));

        jButton2.setText("Ctrls");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Exps");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Ratios");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Diffs");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel2.setText("Header:");

        jButton6.setText("p-values");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jButton6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pairedSpotsJCheckBox.setSelected(true);
        pairedSpotsJCheckBox.setText("Paired spots");
        pairedSpotsJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pairedSpotsJCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pairedSpotsJCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pairedSpotsJCheckBox))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setShowHorizontalLines(false);
        jTable1.setShowVerticalLines(false);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        tableWriter.writeTable(this, tableData, columnNames);
    }//GEN-LAST:event_jButton1ActionPerformed

    public void setSortByGene() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable1.getModel());
        jTable1.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys;
        sortKeys = new ArrayList<>();

        sortKeys.add(new RowSorter.SortKey(COL_GENE, SortOrder.ASCENDING));

        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        List<? extends RowSorter.SortKey> arl;
        try {
            if (jTable1.getRowSorter() != null
                    && jTable1.getRowSorter().getSortKeys().size() > 0) {
                arl = jTable1.getRowSorter().getSortKeys();
                setupData();
                jTable1.getRowSorter().setSortKeys(arl);
            } else {
                setupData();
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    public void clipboardCopy(int col) {
        String clip = "";

        if (!jTextField1.getText().isEmpty()) {
            clip = jTextField1.getText().concat("\n");
        }

        for (int i = 0; i < tableData.length; i++) {
            int j = jTable1.convertRowIndexToModel(i);
            clip = clip.concat(tableData[j][col].toString().concat("\n"));
        }

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(clip), this);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clipboardCopy(COL_CTRL);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clipboardCopy(COL_EXP);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        clipboardCopy(COL_RATIO);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        clipboardCopy(COL_DIFF);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void pairedSpotsJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pairedSpotsJCheckBoxActionPerformed
        setupData();
    }//GEN-LAST:event_pairedSpotsJCheckBoxActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        clipboardCopy(COL_PVAL);
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(summarizeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(summarizeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(summarizeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(summarizeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new summarizeJFrame().setVisible(true);
//            }
//        });
    }

    public void setupData() {
        if (myAD == null) {
            return;
        }

        summaryData = new HashMap<>();

        System.out.println("Setting up summary data");
        for (int i = 1; i <= myAD.getSets(); i++) {
            for (int j = 1; j <= myAD.getPlates(); j++) {
                for (int k = 1; k <= myAD.getRows(); k++) {
                    for (int l = 1; l <= myAD.getCols(); l++) {

                        String myOrf = orfs[j][k][l];

                        sumData sD;

                        if (!summaryData.keySet().contains(myOrf)) {
                            sD = new sumData();
                            sD.ctrlSpots = new ArrayList<>();
                            sD.expSpots = new ArrayList<>();
                            if (!genes[j][k][l].isEmpty()) {
                                sD.gene = genes[j][k][l];
                            } else {
                                sD.gene = myOrf;
                            }
                        } else {
                            sD = summaryData.get(myOrf);
                        }

                        // If paired spots, add if either > 0 because we need
                        // equal numbers of ctrl and exp
                        // Otherwise add if either > 0. Right?
                        if (pairedSpotsJCheckBox.isSelected()) {

                            if (myAD.ctrlSpots[i][j][k][l] > 0 || myAD.expSpots[i][j][k][l] > 0) {

                                sD.ctrlSpots.add(myAD.ctrlSpots[i][j][k][l]);
                                sD.expSpots.add(myAD.expSpots[i][j][k][l]);
                            }
                        } else {
                            if (myAD.ctrlSpots[i][j][k][l] > 0) {
                                sD.ctrlSpots.add(myAD.ctrlSpots[i][j][k][l]);
                            }

                            if (myAD.expSpots[i][j][k][l] > 0) {
                                sD.expSpots.add(myAD.expSpots[i][j][k][l]);
                            }

                        }

                        summaryData.put(myOrf, sD);

                    }
                }
            }
        }

        tableData = new Object[summaryData.size()][columnNames.length];

        int i = 0;

        for (String s : summaryData.keySet()) {

            double ctrl, exp, ratio, diff;

            tableData[i][COL_ORF] = s;
            tableData[i][COL_GENE] = summaryData.get(s).gene;
            tableData[i][COL_CTRL_N] = summaryData.get(s).ctrlSpots.size();
            tableData[i][COL_EXP_N] = summaryData.get(s).expSpots.size();

            ArrayList<Double> c = summaryData.get(s).ctrlSpots;
            int j = c.size();

            double ctrls[] = ArrayUtils.toPrimitive(c.toArray(new Double[j]));

            if (ctrls.length == 0) {
                tableData[i][COL_CTRL] = 0;
                tableData[i][COL_CTRL_SD] = 0;
                ctrl = 0;
            } else {

                if (jComboBox2.getSelectedItem().toString().equals("Median")) {
                    ctrl = StatUtils.percentile(ctrls, 50);
                } else {

                    ctrl = StatUtils.mean(ctrls);
                }
                tableData[i][COL_CTRL] = ctrl;
                tableData[i][COL_CTRL_SD] = Math.sqrt(StatUtils.variance(ctrls));
            }

            ArrayList<Double> e = summaryData.get(s).expSpots;
            j = e.size();
            double exps[] = ArrayUtils.toPrimitive(e.toArray(new Double[j]));

            if (exps.length == 0) {
                tableData[i][COL_EXP] = 0;
                tableData[i][COL_EXP_SD] = 0;
                exp = 0;
            } else {

                if (jComboBox2.getSelectedItem().toString().equals("Median")) {
                    exp = StatUtils.percentile(exps, 50);
                } else {

                    exp = StatUtils.mean(exps);
                }

                tableData[i][COL_EXP] = exp;
                tableData[i][COL_EXP_SD] = Math.sqrt(StatUtils.variance(exps));
            }

            if (pairedSpotsJCheckBox.isSelected()) {
                double ratios[] = new double[ctrls.length];
                double diffs[] = new double[ctrls.length];

                if (ctrls.length == 0) {
                    tableData[i][COL_RATIO_SD] = 0;
                    tableData[i][COL_DIFF_SD] = 0;
                    ratio = 0;
                    diff = 0;
                } else {

                    for (int k = 0; k < ctrls.length; k++) {
                        if (ctrls[k] > 0) {
                            ratios[k] = exps[k] / ctrls[k];
                        } else {
                            ratios[k] = 0;
                        }
                        diffs[k] = exps[k] - ctrls[k];
                    }

                    tableData[i][COL_RATIO_SD] = Math.sqrt(StatUtils.variance(ratios));
                    tableData[i][COL_DIFF_SD] = Math.sqrt(StatUtils.variance(diffs));

                    if (jComboBox2.getSelectedItem().toString().equals("Median")) {
                        ratio = StatUtils.percentile(ratios, 50);
                        diff = StatUtils.percentile(diffs, 50);
                    } else {
                        ratio = StatUtils.mean(ratios);
                        diff = StatUtils.mean(diffs);
                    }
                }

            } else {
                if (ctrl > 0) {
                    ratio = exp / ctrl;
                } else {
                    ratio = 0;
                }
                diff = exp - ctrl;
            }

            tableData[i][COL_RATIO] = ratio;
            tableData[i][COL_DIFF] = diff;

            double pval = 1d;
            if (ctrls.length > 1 && exps.length > 1) {

                TTest tt = new TTest();
                pval = tt.tTest(ctrls, exps);
                if (Double.isNaN(pval)) {
                    pval = 1d;
                }
            }
            tableData[i][COL_PVAL] = pval;

            i++;
        }

        jTable1.setModel(
                new MyTableModel());
        jTable1.setAutoCreateRowSorter(true);
        jTable1.repaint();
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }

    public String getHeader() {
        if (jTextField1.getText().isEmpty()) {
            return getTitle().substring(9);
        } else {
            return jTextField1.getText();

        }
    }

    public class sumData {

        public String gene;
        public ArrayList<Double> ctrlSpots;
        public ArrayList<Double> expSpots;

    }

    public class myRenderer extends DefaultTableCellRenderer {

        int r, c;

        public myRenderer(int row, int col) {
            super();
            r = row;
            c = col;
            setOpaque(true);
        }

        public void setAl(int al) {
            setHorizontalAlignment(al);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            try {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                int realRow = jTable1.convertRowIndexToModel(row);
                String s = table.getModel().getValueAt(realRow, 1).toString();
                if (s == null) {
                    return this;
                }

                int col = jTable1.convertColumnIndexToModel(column);

                switch (col) {

                    case COL_ORF:
                    case COL_GENE:
                        setHorizontalAlignment(LEFT);
                        break;
                    default:
                        setHorizontalAlignment(RIGHT);
                }

                if (isSelected) {
                    setForeground(Color.white);
                }

            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }

            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void setValue(Object value) {

            if (value == null) {
                return;
            }

            if (value instanceof Double) {
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(4);
                setText(nf.format(value));
            } else {
                setText(value.toString());
            }

        }
    }

    class MyTableModel extends AbstractTableModel {

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return tableData.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return tableData[row][col];
        }

        @Override
        public Class<?> getColumnClass(int c) {
//            if (c == 9 || c == 7 || c == 5 || c == COL_HIT) {
//                return Object.class;
//            }

            if (getValueAt(0, c) != null) {
//                if (c == COL_EXCLUDE) {
//                    return TreeSet.class;
//                }

                if (c == COL_RATIO || c == COL_PVAL) {
                    return Double.class;
                }

                return getValueAt(0, c).getClass();
            } else {
                return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    public javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    public javax.swing.JCheckBox pairedSpotsJCheckBox;
    // End of variables declaration//GEN-END:variables
}