package balony;

import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.RIGHT;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Barry Young
 */
public class comparisonTable extends javax.swing.JFrame implements screenCompare {

    public Balony balony;
    public Object[][] tableData;

    final String[] colNames = {"#", "Plate", "Row", "Col", "Orf", "Gene",
        "Ratio 1", "SD 1", "Ratio 1 < p", "Ratio 1 > q", "Ratio 1 Hit",
        "Ratio 2", "SD 2", "Ratio 2 < p", "Ratio 2 > q", "Ratio 2 Hit",
        "Diff", "p-value"};

    public final int COL_INDEX = 0;
    public final int COL_PLATE = COL_INDEX + 1;
    public final int COL_ROW = COL_PLATE + 1;
    public final int COL_COL = COL_ROW + 1;
    public final int COL_ORF = COL_COL + 1;
    public final int COL_GENE = COL_ORF + 1;
    public final int COL_RATIO_1 = COL_GENE + 1;
    public final int COL_RATIO_1_SD = COL_RATIO_1 + 1;
    public final int COL_RATIO_1_SL = COL_RATIO_1_SD + 1;
    public final int COL_RATIO_1_SR = COL_RATIO_1_SL + 1;
    public final int COL_RATIO_1_HIT = COL_RATIO_1_SR + 1;
    public final int COL_RATIO_2 = COL_RATIO_1_HIT + 1;
    public final int COL_RATIO_2_SD = COL_RATIO_2 + 1;
    public final int COL_RATIO_2_SL = COL_RATIO_2_SD + 1;
    public final int COL_RATIO_2_SR = COL_RATIO_2_SL + 1;
    public final int COL_RATIO_2_HIT = COL_RATIO_2_SR + 1;
    public final int COL_RATIO_DIFF = COL_RATIO_2_HIT + 1;
    public final int COL_RATIO_PVAL = COL_RATIO_DIFF + 1;

    /**
     * Creates new form comparisonTable
     */
    public comparisonTable() {
        initComponents();
    }

    public void setupTable() {
        if (balony == null) {
            return;
        }

        jComboBox1.removeAllItems();
        jComboBox2.removeAllItems();

        jComboBox1.addItem("Select...");
        jComboBox2.addItem("Select...");

        for (String s : balony.aD.keySet()) {
            jComboBox1.addItem(s);
            jComboBox2.addItem(s);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable() {
            public TableCellRenderer getCellRenderer(int row, int column) {

                TableCellRenderer t=new myRenderer(row,column);
                return t;
            }
        };
        jPanel1 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Screen Comparison");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setShowHorizontalLines(false);
        jTable1.setShowVerticalLines(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Screen 1"));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Screen 2"));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jButton1.setText("Export...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 343, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        setupTableData();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        setupTableData();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            int i = jTable1.getSelectedRow();
            int p = (Integer) jTable1.getValueAt(i, 1);
            int r = (Integer) jTable1.getValueAt(i, 2);
            int c = (Integer) jTable1.getValueAt(i, 3);
            ArrayList<String> scr = new ArrayList<String>();
            scr.add(jComboBox2.getSelectedItem().toString());
            scr.add(jComboBox1.getSelectedItem().toString());

            spotCompare sC = balony.openSpotCompare(p, r, c, scr);
            sC.cT = jTable1;
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        tableWriter.writeTable(this, tableData, colNames);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(comparisonTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(comparisonTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(comparisonTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(comparisonTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new comparisonTable().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    @Override
    public boolean isComparisonTable() {
        return true;
    }

    private void setupTableData() {
        Set<String> s = balony.aD.keySet();

        if (jComboBox1.getSelectedItem() == null || jComboBox2.getSelectedItem() == null) {
            return;
        }

        String s1 = jComboBox2.getSelectedItem().toString();
        String s2 = jComboBox1.getSelectedItem().toString();

        if (!s.contains(s1) || !s.contains(s2)) {
            setTitle(SCREEN__COMPARISON);
            return;
        }

        setTitle(SCREEN__COMPARISON + " - " + s1 + "vs. " + s2);

        Object[][] t1 = balony.aD.get(s1).getDt().tableData;
        Object[][] t2 = balony.aD.get(s2).getDt().tableData;

        dataTable dt1 = balony.aD.get(s1).getDt();
        dataTable dt2 = balony.aD.get(s2).getDt();

        tableData = new Object[t1.length][colNames.length];

        for (int i = 0; i < t1.length; i++) {
            tableData[i][COL_INDEX] = i + 1;
            tableData[i][COL_PLATE] = t1[i][dt1.COL_PLATE];
            tableData[i][COL_ROW] = t1[i][dt1.COL_ROW];
            tableData[i][COL_COL] = t1[i][dt1.COL_COL];
            tableData[i][COL_ORF] = t1[i][dt1.COL_ORF];
            tableData[i][COL_GENE] = t1[i][dt1.COL_GENE];
            tableData[i][COL_RATIO_1_SL] = t1[i][dt1.COL_SL];
            tableData[i][COL_RATIO_1_SR] = t1[i][dt1.COL_SR];
            tableData[i][COL_RATIO_1_HIT] = t1[i][dt1.COL_HIT];
            tableData[i][COL_RATIO_2_SL] = t2[i][dt2.COL_SL];
            tableData[i][COL_RATIO_2_SR] = t2[i][dt2.COL_SR];
            tableData[i][COL_RATIO_2_HIT] = t2[i][dt2.COL_HIT];

            Object r1, r2;
            try {
                r1 = (Double) t1[i][dt1.COL_RATIO];
            } catch (ClassCastException e) {
                r1 = null;
            }
            tableData[i][COL_RATIO_1] = r1;

            tableData[i][COL_RATIO_1_SD] = t1[i][dt1.COL_RATIO_SD];

            try {
                r2 = (Double) t2[i][dt2.COL_RATIO];
            } catch (ClassCastException e) {
                r2 = null;
            }
            tableData[i][COL_RATIO_2] = r2;

            tableData[i][COL_RATIO_2_SD] = t2[i][dt2.COL_RATIO_SD];

            if (r1 == null || r2 == null) {
                tableData[i][COL_RATIO_DIFF] = "";
            } else {
                tableData[i][COL_RATIO_DIFF] = (Double) r2 - (Double) r1;
            }

        }

        myTableModel model = new myTableModel();

        jTable1.setModel(model);
//        jTable1.setAutoCreateRowSorter(true);

        TableRowSorter<myTableModel> sorter;
        sorter = new TableRowSorter<myTableModel>(model);
        for (int i = 0; i < jTable1.getModel().getColumnCount(); i++) {
            sorter.setComparator(i, (Comparator<?>) new EmptyRowComparator(sorter, i));
        }

        sorter.addRowSorterListener(new MyRowSorterListener());
        jTable1.setRowSorter(sorter);

        TableColumnModel tcm = jTable1.getColumnModel();

        for (int i = 0; i < tcm.getColumnCount(); i++) {
            switch (i) {
                case 0:
                case 1:
                case 2:
                    tcm.getColumn(i).setPreferredWidth(50);
                    break;
                default:
                    tcm.getColumn(i).setPreferredWidth(100);

            }
        }

        jTable1.repaint();

    }
    public static final String SCREEN__COMPARISON = "Screen Comparison";

    public class myTableModel extends AbstractTableModel {

        @Override
        public Class<?> getColumnClass(int c) {

            if (c == COL_RATIO_1 || c == COL_RATIO_2 || c == COL_RATIO_DIFF //                        || c == COL_RATIO_1_SD || c == COL_RATIO_2_SD
                    ) {
                return Double.class;
            } else if (c == COL_PLATE || c == COL_ROW || c == COL_COL) {
                return Integer.class;
            } else if (c == COL_ORF || c == COL_GENE) {
                return String.class;
            } else {
                return Object.class;
            }
        }

        @Override
        public String getColumnName(int col) {
            return colNames[col];
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
        public Object getValueAt(int row, int col) {
            return tableData[row][col];
        }

        @Override
        public int getRowCount() {
            return tableData.length;
        }

        @Override
        public int getColumnCount() {
            return colNames.length;
        }
    }

    private class myRenderer extends DefaultTableCellRenderer {

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
                    case COL_INDEX:
                    case COL_ROW:
                    case COL_PLATE:
                    case COL_COL:
                        setHorizontalAlignment(CENTER);
                        break;
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

                if (column == COL_RATIO_1 || column == COL_RATIO_1_SD || column == COL_RATIO_1_SL
                        || column == COL_RATIO_1_SR || column == COL_RATIO_1_HIT) {

                    try {
                        Hit hit = (Hit) jTable1.getModel().getValueAt(realRow, COL_RATIO_1_HIT);
                        if (hit instanceof Hit) {
                            switch (hit.i) {
                                case 1:
                                    setBackground(new Color(100, 238, 100));
                                    break;
                                case 2:
                                    setBackground(new Color(238, 100, 100));
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }

                if (column == COL_RATIO_2 || column == COL_RATIO_2_SD || column == COL_RATIO_2_SL
                        || column == COL_RATIO_2_SR || column == COL_RATIO_2_HIT) {

                    try {
                        Hit hit = (Hit) jTable1.getModel().getValueAt(realRow, COL_RATIO_2_HIT);
                        if (hit instanceof Hit) {
                            switch (hit.i) {
                                case 1:
                                    setBackground(new Color(100, 238, 100));
                                    break;
                                case 2:
                                    setBackground(new Color(238, 100, 100));
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
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

            if (c == COL_INDEX) {
                setForeground(Color.white);
                setBackground(Color.black);
                setText("#");
            }

            if (value instanceof Double) {
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(4);
                setText(nf.format(value));
            } else if (value.getClass().equals(new TreeSet<String>().getClass())) {
                StringBuilder outString = new StringBuilder();

                for (String s : (TreeSet<String>) value) {
                    outString.append(s).append(", ");
                }

                if (outString.length() > 2) {
                    setText(outString.substring(0, outString.length() - 2));
                }
            } else {
                setText(value.toString());
            }

        }
    }

    public class MyRowSorterListener implements RowSorterListener {

        @Override
        public void sorterChanged(RowSorterEvent e) {

            for (int i = 0; i < jTable1.getRowCount(); i++) {
                int j = jTable1.convertRowIndexToModel(i);
                tableData[j][COL_INDEX] = i + 1;
            }

        }
    }

    class EmptyRowComparator implements Comparator<Object> {

        public final Object EMPTY_ROW = "";
        private final RowSorter<?> sorter;
        private final int column;

        public EmptyRowComparator(RowSorter<?> sorter, int column) {
            this.sorter = sorter;
            this.column = column;
        }

        @SuppressWarnings("unchecked")
        @Override
        public int compare(Object o1, Object o2) {
            Object orig1 = o1;
            Object orig2 = o2;
            if (o1.getClass().equals(new TreeSet<String>().getClass())) {
                o1 = o1.toString();
            }

            if (o2.getClass().equals(new TreeSet<String>().getClass())) {
                o2 = o2.toString();
            }

            if (o1.getClass().equals(new HitPair().getClass())) {
                o1 = ((HitPair) o1).a;
                o2 = ((HitPair) o2).a;
                if (((Integer) o1) != ((Integer) o2).intValue()) {
                } else {
                    o1 = ((HitPair) orig1).b;
                    o2 = ((HitPair) orig2).b;
                }
            }

            if (o1.getClass().equals(new Hit().getClass())) {
                Integer i1 = ((Hit) o1).i;
                Integer i2 = ((Hit) o2).i;

                if (i1 == 0) {
                    o1 = EMPTY_ROW;
                } else {
                    o1 = i1;
                }

                if (i2 == 0) {
                    o2 = EMPTY_ROW;
                } else {
                    o2 = i2;
                }
            }

            if (o2.getClass().equals(new HitPair().getClass())) {
            }

            boolean empty1 = o1 == EMPTY_ROW;
            boolean empty2 = o2 == EMPTY_ROW;
            if (empty1 && empty2) {
                return 0;
            } else if (empty1) {
                return 1 * getSortOrder();
            } else if (empty2) {
                return -1 * getSortOrder();
            }
            return ((Comparable<Object>) o1).compareTo(o2);
        }

        private int getSortOrder() {
            SortOrder order = SortOrder.ASCENDING;
            for (RowSorter.SortKey sortKey : sorter.getSortKeys()) {
                if (sortKey.getColumn() == column) {
                    order = sortKey.getSortOrder();
                    break;
                }
            }
            return order == SortOrder.ASCENDING ? 1 : -1;
        }
    }

}
