/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * linkage.java
 *
 * Created on 25-Oct-2010, 3:24:48 PM
 */
package balony;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author Barry Young
 */
public class linkage extends javax.swing.JFrame {
    
    public int start;
    public int end;
    ArrayList<String> orfList;
    HashMap<String, Double> orfRatio;
    public Balony balony;
    public dataTable dt;
    public String myOrf;
    boolean repaint;
    public String oStart;
    public String oEnd;

    /** Creates new form linkage */
    public linkage() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        linkageJPanel = new javax.swing.JPanel() {
            @Override public void paintComponent(Graphics g) {

                super.paintComponent(g);    // paints background
                updatePanel(g);

            }
        }
        ;
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        linkageStartJSlider = new javax.swing.JSlider();
        linkageEndJSlider = new javax.swing.JSlider();
        windowJLabel = new javax.swing.JLabel();
        windowjTextField = new javax.swing.JTextField();
        startOrfJLabel = new javax.swing.JLabel();
        endOrfJLabel = new javax.swing.JLabel();
        cancelJButton = new javax.swing.JButton();
        applyJButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gene Linkage");

        linkageJPanel.setBackground(new java.awt.Color(255, 255, 255));
        linkageJPanel.setPreferredSize(new java.awt.Dimension(600, 300));

        jLabel1.setText("Chromosome:");

        jLabel2.setText("??");

        javax.swing.GroupLayout linkageJPanelLayout = new javax.swing.GroupLayout(linkageJPanel);
        linkageJPanel.setLayout(linkageJPanelLayout);
        linkageJPanelLayout.setHorizontalGroup(
            linkageJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, linkageJPanelLayout.createSequentialGroup()
                .addContainerGap(383, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(135, 135, 135))
        );
        linkageJPanelLayout.setVerticalGroup(
            linkageJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(linkageJPanelLayout.createSequentialGroup()
                .addGroup(linkageJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(251, Short.MAX_VALUE))
        );

        linkageStartJSlider.setMaximum(101);
        linkageStartJSlider.setMinorTickSpacing(1);
        linkageStartJSlider.setPaintTicks(true);
        linkageStartJSlider.setSnapToTicks(true);
        linkageStartJSlider.setValue(25);
        linkageStartJSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                linkageStartJSliderStateChanged(evt);
            }
        });

        linkageEndJSlider.setMaximum(101);
        linkageEndJSlider.setMinorTickSpacing(1);
        linkageEndJSlider.setPaintTicks(true);
        linkageEndJSlider.setSnapToTicks(true);
        linkageEndJSlider.setValue(76);
        linkageEndJSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                linkageEndJSliderStateChanged(evt);
            }
        });

        windowJLabel.setText("Window size:");

        windowjTextField.setText("100");
        windowjTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                windowjTextFieldActionPerformed(evt);
            }
        });
        windowjTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                windowjTextFieldKeyReleased(evt);
            }
        });

        startOrfJLabel.setText("---------------");

        endOrfJLabel.setText("---------------");

        cancelJButton.setText("Cancel");
        cancelJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJButtonActionPerformed(evt);
            }
        });

        applyJButton.setText("Apply");
        applyJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyJButtonActionPerformed(evt);
            }
        });

        jButton1.setText("-");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("+");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Use the sliders below to highlight the chromosomal region to be excluded from analysis.");

        jLabel4.setText("Smoothing:");

        jTextField1.setText("3");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(windowJLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(windowjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                        .addComponent(applyJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelJButton)
                        .addGap(140, 140, 140))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(linkageJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(117, 117, 117))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                        .addGap(113, 113, 113))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(linkageEndJSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endOrfJLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(linkageStartJSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(startOrfJLabel)))
                        .addContainerGap(53, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(windowJLabel)
                    .addComponent(windowjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelJButton)
                    .addComponent(applyJButton)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(linkageJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(linkageStartJSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startOrfJLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(linkageEndJSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endOrfJLabel))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void windowjTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_windowjTextFieldKeyReleased
        String ss = oStart;
        String ee = oEnd;
        refreshPanel();
        updateSliders(ss, ee);

        // TODO add your handling code here:
    }//GEN-LAST:event_windowjTextFieldKeyReleased
    
    private void windowjTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_windowjTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_windowjTextFieldActionPerformed
    
    private void linkageStartJSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_linkageStartJSliderStateChanged
        
        refreshPanel();
        // TODO add your handling code here:
    }//GEN-LAST:event_linkageStartJSliderStateChanged
    
    private void linkageEndJSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_linkageEndJSliderStateChanged
        refreshPanel();
        // TODO add your handling code here:
    }//GEN-LAST:event_linkageEndJSliderStateChanged
    
    private void applyJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyJButtonActionPerformed
        ArrayList<String> linkedList = new ArrayList<String>();
        
        boolean add = false;
        for (String s : orfList) {
            if (s.equals(oStart)) {
                add = true;
            }
            
            if (add) {
                linkedList.add(s);
            }
            
            if (s.equals(oEnd)) {
                add = false;
            }
        }
        
        if (!linkedList.isEmpty()) {
            dt.setLinkedGenes(linkedList);
        }
        
        dispose();
    }//GEN-LAST:event_applyJButtonActionPerformed
    
    private void cancelJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelJButtonActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String ss = oStart;
        String ee = oEnd;
        try {
            int i = Integer.parseInt(windowjTextField.getText());
            if (i > 2) {
                i /= 2;
                windowjTextField.setText(Integer.toString(i));
                refreshPanel();
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getLocalizedMessage());
        }
        updateSliders(ss, ee);
    }//GEN-LAST:event_jButton1ActionPerformed
    
    public void updateSliders(String s, String e) {
        
        if (!oStart.isEmpty()) {
            for (int i = start; i < end; i++) {
                if (orfList.get(i).equals(s)) {
                    linkageStartJSlider.setValue(i - start);
                }
            }
        }
        if (!oEnd.isEmpty()) {
            for (int i = start; i < end; i++) {
                if (orfList.get(i).equals(e)) {
                    linkageEndJSlider.setValue(i - start + 1);
                }
            }
        }
        
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String ss = oStart;
        String ee = oEnd;
        try {
            int i = Integer.parseInt(windowjTextField.getText());
            if (i < 6000) {
                i *= 2;
                if (i > orfList.size()) {
                    i = orfList.size();
                }
                windowjTextField.setText(Integer.toString(i));
                refreshPanel();
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getLocalizedMessage());
        }
        updateSliders(ss, ee);
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        refreshPanel();
    }//GEN-LAST:event_jTextField1KeyReleased

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            public void run() {
//                new linkage().setVisible(true);
//            }
//        });
//    }
    public void updatePanel(Graphics ga) {
        
        Graphics2D g = (Graphics2D) ga;
        
        int cnt = 0;
        int width = linkageJPanel.getWidth();
        int height = linkageJPanel.getHeight();
        
        if (start == 0 && end == 0) {
            return;
        }
        
        if (start < 0) {
            start = 0;
        }
        
        if (end >= orfList.size()) {
            end = orfList.size() - 1;
        }
        
        ArrayList<String> aOrfs = new ArrayList<String>();
        int i = start;
        
        while (i <= end) {
            cnt++;
            aOrfs.add(orfList.get(i));
            i++;
        }
        
        if (cnt == 0) {
            return;
        }
        
        linkageStartJSlider.setMaximum(end - start);
        linkageEndJSlider.setMaximum(end - start);
        
        int rmin = linkageStartJSlider.getValue();
        int rmax = linkageEndJSlider.getValue();
        
        if (rmax < rmin) {
            int temp = rmin;
            rmin = rmax;
            rmax = temp;
        }
        
        i = start;
        int cnt2 = 0;
        
        g.setBackground(Color.white);
        g.clearRect(0, 0, width, height);
        
        ArrayList<Integer> x = new ArrayList<Integer>();
        ArrayList<Integer> y = new ArrayList<Integer>();

        Rectangle r;
        
        while (i <= end) {
            
            Double d = orfRatio.get(orfList.get(i));
            
            ArrayList<Double> ard = new ArrayList<Double>();
            
            int k;
            try {
                k = Integer.parseInt(jTextField1.getText());
            } catch (NumberFormatException e) {
                k = 3;
            }
            
            int j1 = i - k;
            int j2 = i + k;
            
            if (j1 < 0) {
                j1 = 0;
            }
            
            if (j2 >= orfList.size()) {
                j2 = orfList.size() - 1;
            }
            
            for (int j = j1; j < j2; j++) {
                ard.add(orfRatio.get(orfList.get(j)));
            }
            
            Double d2 = Balony.mean(ard.toArray(new Double[ard.size()]));
            
            if (d2 != null) {
                x.add((int) ((i - start + 0.5) * width) / cnt);
                y.add((int) (height - d2 * 150));
            }
            
            if (d == null) {
                d = 0d;
            }
            
            Color c;
            
            g.setColor(Color.black);
            if (orfList.get(i).equals(myOrf)) {
                g.setPaint(Color.blue);
            } else if (cnt2 >= rmin && cnt2 < rmax) {
                g.setPaint(Color.pink);
            } else {
                g.setPaint(Color.red);
            }
            if (cnt2 >= rmin && cnt2 < rmax) {
                c = new Color(220, 220, 220);
            } else {
                c = Color.white;
            }
            
            if (cnt2 == linkageStartJSlider.getValue()) {
                if (linkageStartJSlider.getValue() > linkageEndJSlider.getValue()) {
                    oEnd = aOrfs.get(cnt2 - 1);
                    startOrfJLabel.setText(oEnd);
                } else {
                    oStart = aOrfs.get(cnt2);
                    startOrfJLabel.setText(oStart);
                }
            }
            
            if (cnt2 == linkageEndJSlider.getValue()) {
                
                if (linkageEndJSlider.getValue() > linkageStartJSlider.getValue()) {
                    oEnd = aOrfs.get(cnt2 - 1);
                    endOrfJLabel.setText(oEnd);
                } else {
                    oStart = aOrfs.get(cnt2);
                    endOrfJLabel.setText(oStart);
                }
            }
            
            r = new Rectangle((cnt2 * width) / cnt, (int) (height - d * 150), (width / cnt), (int) (d * 150));
            g.draw(r);
            g.fill(r);
            
            g.setPaint(c);
            r = new Rectangle((cnt2 * width) / cnt, 20, (width / cnt), (int) (height - 20 - d * 150));
            
            g.draw(r);
            g.fill(r);
            
            cnt2++;
            i++;
        }
        
        g.setPaint(Color.blue);
        g.setColor(Color.blue);
        r = new Rectangle(width - orfList.size() / 10, 0, orfList.size() / 10, 20);
        g.draw(r);
        g.fill(r);
        
        g.setPaint(Color.green);
        g.setColor(Color.green);
        r = new Rectangle(width - orfList.size() / 10 + start / 10 + rmin / 10, 0,
                (rmax - rmin) / 10, 20);
        g.draw(r);
        g.fill(r);
        
        int s1 = linkageStartJSlider.getValue();
        int s2 = linkageEndJSlider.getValue();
        
        g.setPaint(Color.black);
        g.setColor(Color.black);
        
        g.drawLine((s1 * width) / cnt, height, (s1 * width) / cnt, 20);
        g.drawLine((s2 * width) / cnt, height, (s2 * width) / cnt, 20);
        
        g.setPaint(Color.lightGray);
        g.setColor(Color.lightGray);
        
        g.drawLine(0, height - 150, width, height - 150);
        
        g.setPaint(Color.black);
        g.setColor(Color.black);
        
        g.setStroke(new BasicStroke(2.0f));
        
        int[] xa = new int[cnt2 + 1];
        int[] ya = new int[cnt2 + 1];
        
        for (int j = 0; j < x.size(); j++) {
            xa[j] = x.get(j);
            ya[j] = y.get(j);
        }
        
        g.drawPolyline(xa, ya, x.size());
        
        jLabel1.repaint();
        jLabel2.repaint();
    }
    
    public void refreshPanel() {
        int window;
        int i = orfList.indexOf(myOrf);
        try {
            window = Integer.parseInt(windowjTextField.getText());
        } catch (NumberFormatException e) {
            window = 50;
            System.out.println(e.getLocalizedMessage());
        }
        
        if (window > orfList.size()) {
            window = orfList.size();
            windowjTextField.setText("" + window);
        }
        
        start = i - window / 2;
        end = start + window;
        
        if (start < 0) {
            start = 0;
            end = window;
        }
        
        if (end > orfList.size()) {
            end = orfList.size();
            start = end - window;
        }
        
        updatePanel(linkageJPanel.getGraphics());
    }
    
    public void setupLinkage(String orf) {
        
        myOrf = orf;
        int orfchr = balony.allSGDInfo.get(orf).chr;
        oStart = "";
        oEnd = "";
        
        jLabel2.setText("" + orfchr);

        // Key is start coord; value is arraylist of all orfs there
        TreeMap<Integer, String> chrom = new TreeMap<Integer, String>();
        
        for (String o : balony.allSGDInfo.keySet()) {
            try {
                if (balony.allSGDInfo.get(o).chr == orfchr) {
                    Integer i = (Integer) balony.allSGDInfo.get(o).startCoord;
                    if (!chrom.containsKey(i) && o.startsWith("Y")) {
                        chrom.put(i, o);
                    }
                }
                
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        
        orfList = new ArrayList<String>();
        
        for (Integer I : chrom.keySet()) {
            orfList.add(chrom.get(I));
        }
        
        orfRatio = new HashMap<String, Double>();
        
        for (String s : orfList) {
            Double d = dt.getRatioByOrf(s);
            if (d != null) {
                orfRatio.put(s, d);
            }
        }
        
        int window;
        try {
            window = Integer.parseInt(windowjTextField.getText());
        } catch (NumberFormatException e) {
            window = 100;
        }
        
        start = orfList.indexOf(orf) - window / 2;
        end = orfList.indexOf(orf) + window / 2;
        refreshPanel();
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyJButton;
    private javax.swing.JButton cancelJButton;
    private javax.swing.JLabel endOrfJLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JSlider linkageEndJSlider;
    private javax.swing.JPanel linkageJPanel;
    private javax.swing.JSlider linkageStartJSlider;
    private javax.swing.JLabel startOrfJLabel;
    private javax.swing.JLabel windowJLabel;
    private javax.swing.JTextField windowjTextField;
    // End of variables declaration//GEN-END:variables
}
