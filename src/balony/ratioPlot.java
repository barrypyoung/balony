/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ratioPlot.java
 *
 * Created on 16-Nov-2010, 12:52:02 PM
 */
package balony;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 *
 * @author Barry Young
 */
public class ratioPlot extends javax.swing.JFrame {

    public static final String PNG_FORMAT = "png";
    public static final String SVG_FORMAT = "svg";
    public dataTable dt;
    public double ymin, ymax;
    public ArrayList<HotSpot> hotSpot;
    public String lastTip;
    public int plotType;

    /** Creates new form ratioPlot */
    public ratioPlot() {
        initComponents();
    }

    public void externRefreshPlot() {
        refreshPlot(plotJPanel.getGraphics());
    }

    public void refreshPlot(Graphics ga) {
        Graphics2D g = (Graphics2D) ga;
        if (g == null) {
            return;
        }

        try {
            ymax = Double.parseDouble(ymaxJTextField.getText());
        } catch (NumberFormatException e) {
            ymax = 2.0;
        }

        if (plotType == 1) {
            ymin = -ymax;
        } else {
            ymin = 0;
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int width = plotJPanel.getWidth();
        int height = plotJPanel.getHeight();
        double mincutoff = 0d;
        double maxcutoff = 0d;

        g.setBackground(Color.white);
        g.clearRect(0, 0, width, height);

        int pointSize = pointSizeJComboBox.getSelectedIndex() + 1;

        float dash[] = {10.0f, 5.0f};
        g.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));

        // y = 1

        if (plotType == 0) {
            g.drawLine(40, height / 2, width - 20, height / 2);

            try {
                mincutoff = Double.parseDouble(dt.lowCutOffJTextField.getText());
                maxcutoff = Double.parseDouble(dt.highCutOffJTextField.getText());
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }

            if (mincutoff > 0) {
                double y = (height - 20) - ((mincutoff * (height - 40)) / ymax);
                g.setPaint(new Color(230, 255, 230));
                g.fillRect(40, (int) y, width - 60, (height - 20) - (int) y);
                g.setColor(Color.green);
                g.drawLine(40, (int) y, width - 20, (int) y);
            }

            if (maxcutoff > 0) {
                double y = (height - 20) - ((maxcutoff * (height - 40)) / ymax);
                g.setPaint(new Color(255, 230, 230));
                g.fillRect(40, 20, width - 60, (int) y - 20);
                g.setColor(Color.red);
                g.drawLine(40, (int) y, width - 20, (int) y);
            }
        }

        g.setColor(Color.black);
        g.setStroke(new BasicStroke(1));

        // y-axis
        g.drawLine(40, height - 20, 40, 20);

        // x-axis
        g.drawLine(40, (int) ((height - 20) + ((ymin * (height - 40)) / (ymax - ymin))),
                width - 20, (int) ((height - 20) + ((ymin * (height - 40)) / (ymax - ymin))));

        if (plotType == 1) {
            g.drawLine(40, height - 20, width - 20, height - 20);
        }

        int rows = dt.analysisTable.getRowCount();

        int xtick = 1000;
        double ytick = 0.2;

        try {
            xtick = Integer.parseInt(xtickJTextField.getText());
            ytick = Double.parseDouble(ytickJTextField.getText());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        if (xtick < rows / 100) {
            xtick = 1000;
        }

        if (ytick < ymax / 100) {
            ytick = 0.2;
        }

        double i = 40;
        int j = 0;
        while (i <= width - 20) {

            g.drawLine((int) i, height - 20, (int) i, height - 15);
            String s = Integer.toString(j);
            j += xtick;

            FontMetrics f = getFontMetrics(g.getFont());
            Rectangle2D r = f.getStringBounds(s, ga);

            g.drawString(s, (int) (i - r.getWidth() / 2), height - 2);
            i += ((width - 60) * xtick) / rows;
        }

        i = height - 20;
        double k = ymin;
        while (i > 19) {
            g.drawLine(40, (int) i, 35, (int) i);

            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(2);
            String s = nf.format(k);
            k += ytick;
            if (s.length() > 4) {
                s = s.substring(0, 4);
            }

            if (s.endsWith(".")) {
                s = s.replace(".", "");
            }

            FontMetrics f = getFontMetrics(g.getFont());
            Rectangle2D r = f.getStringBounds(s, ga);

            g.drawString(s, (int) (30 - r.getWidth()), (int) i + 3);
            i -= ((height - 40) * ytick) / (ymax - ymin);
        }

        // Build up an ArrayList of the surrounding co-ordinates of each spot

        hotSpot = new ArrayList<>();
        for (int ii = 0; ii < rows; ii++) {
            try {

                j = dt.analysisTable.convertRowIndexToModel(ii);
                Double d = 0d;
                if (plotType == 0) {
                    d = (Double) dt.analysisTable.getModel().getValueAt(j, dt.COL_RATIO);
                }
                if (plotType == 1) {
                    d = (Double) dt.analysisTable.getModel().getValueAt(j, dt.COL_DIFF);
                }

                double y = (height - 20) - (((d - ymin) * (height - 40)) / (ymax - ymin));
                double x = (ii * (width - 60)) / rows + 40;

                g.setColor(Color.black);

                if (dt.isLCHit(ii)) {
                    g.setColor(Color.green);
                } else {
                    if (dt.isHCHit(ii)) {
                        g.setColor(Color.red);
                    }
                }

                if (d <= ymax && d >= ymin) {
                    g.drawLine((int) x - pointSize, (int) y, (int) x + pointSize, (int) y);
                    g.drawLine((int) x, (int) y - pointSize, (int) x, (int) y + pointSize);
                }

                Font f = g.getFont().deriveFont(Font.BOLD);
                g.setFont(f);

                if (dt.analysisTable.isRowSelected(ii)) {
                    if (selPointsJComboBox.getSelectedIndex() == 0 || selPointsJComboBox.getSelectedIndex() == 2) {
                        g.setColor(Color.blue);
                        g.drawOval((int) x - 4, (int) y - 4, 8, 8);
                    }
                    if (selPointsJComboBox.getSelectedIndex() == 1 || selPointsJComboBox.getSelectedIndex() == 2) {
                        g.setColor(Color.blue);
                        g.drawString(dt.analysisTable.getModel().getValueAt(j, dt.COL_GENE).toString(),
                                (int) x - 10, (int) y - 6);
                    }
                }

                HotSpot hs = new HotSpot();
                hs.xmin = (int) x - 2;
                hs.xmax = (int) x + 2;
                hs.ymin = (int) y - 2;
                hs.ymax = (int) y + 2;
                hs.gene = dt.analysisTable.getModel().getValueAt(j, dt.COL_GENE).toString();
                hs.row = ii;

                hotSpot.add(hs);

            } catch (Exception e) {
            }
        }
    }

    public class HotSpot {

        public int xmin, ymin, xmax, ymax, row;
        public String gene;

        public HotSpot() {
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        plotJPanel = new javax.swing.JPanel() {
            @Override public void paintComponent(Graphics g) {
                super.paintComponent(g);    // paints background
                refreshPlot(g);

                // do your drawing here
            }

            @Override
            public String getToolTipText(MouseEvent e) {

                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(4);

                Point pt = e.getPoint();

                int x = pt.x;
                int y = pt.y;

                double j = ymin+ (((ymax-ymin) * ((plotJPanel.getHeight() - 20) - y)) / (plotJPanel.getHeight() - 40));
                int k = (dt.analysisTable.getRowCount() * (x - 40)) / (plotJPanel.getWidth() - 60);

                StringBuilder s = new StringBuilder();

                ArrayList<HotSpot> ah = new ArrayList<HotSpot>();

                for(HotSpot h : hotSpot) {
                    if(x > h.xmin && x < h.xmax && y > h.ymin && y <h.ymax) {
                        ah.add(h);
                        s.append(h.gene).append(" ");
                    }
                }

                if(!s.toString().equals(lastTip)) {
                    externRefreshPlot();
                }

                for(HotSpot h : ah) {

                    Graphics2D g = (Graphics2D)plotJPanel.getGraphics();
                    g.setColor(Color.blue);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.drawOval(h.xmin, h.ymin, 4,4);
                }

                if(s.length()>0) {
                    lastTip=s.toString();
                    return s.toString();
                }

                return "x: "+(k>0 ? k : "")+" | y: "+(j>=ymin? nf.format(j) : "");

            }
        }
        ;
        settingsJPanel = new javax.swing.JPanel();
        refreshJButton = new javax.swing.JButton();
        pointSizeJLabel = new javax.swing.JLabel();
        pointSizeJComboBox = new javax.swing.JComboBox();
        selPointsJLabel = new javax.swing.JLabel();
        selPointsJComboBox = new javax.swing.JComboBox();
        xtickJLabel = new javax.swing.JLabel();
        xtickJTextField = new javax.swing.JTextField();
        ymaxJLabel = new javax.swing.JLabel();
        ymaxJTextField = new javax.swing.JTextField();
        ytickJLabel = new javax.swing.JLabel();
        ytickJTextField = new javax.swing.JTextField();
        svgExportJButton = new javax.swing.JButton();
        pngExportJButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        plotJPanel.setBackground(new java.awt.Color(255, 255, 255));
        plotJPanel.setToolTipText("test");
        plotJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                plotJPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout plotJPanelLayout = new javax.swing.GroupLayout(plotJPanel);
        plotJPanel.setLayout(plotJPanelLayout);
        plotJPanelLayout.setHorizontalGroup(
            plotJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );
        plotJPanelLayout.setVerticalGroup(
            plotJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 419, Short.MAX_VALUE)
        );

        settingsJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Plot Settings"));

        refreshJButton.setText("Refresh");
        refreshJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshJButtonActionPerformed(evt);
            }
        });

        pointSizeJLabel.setText("Point size:");

        pointSizeJComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5" }));
        pointSizeJComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pointSizeJComboBoxActionPerformed(evt);
            }
        });

        selPointsJLabel.setText("Selected points:");

        selPointsJComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Circles", "Gene Names", "Both" }));
        selPointsJComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selPointsJComboBoxActionPerformed(evt);
            }
        });

        xtickJLabel.setText("x-tick:");

        xtickJTextField.setText("1000");
        xtickJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                xtickJTextFieldKeyReleased(evt);
            }
        });

        ymaxJLabel.setText("y-max:");

        ymaxJTextField.setText("2.0");
        ymaxJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ymaxJTextFieldKeyReleased(evt);
            }
        });

        ytickJLabel.setText("y-tick:");

        ytickJTextField.setText("0.2");
        ytickJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ytickJTextFieldKeyReleased(evt);
            }
        });

        svgExportJButton.setText("Export as SVG...");
        svgExportJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svgExportJButtonActionPerformed(evt);
            }
        });

        pngExportJButton.setText("Export as PNG...");
        pngExportJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pngExportJButtonActionPerformed(evt);
            }
        });

        jButton1.setText("Auto-estimate ratio cut-offs");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ratio", "Difference" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout settingsJPanelLayout = new javax.swing.GroupLayout(settingsJPanel);
        settingsJPanel.setLayout(settingsJPanelLayout);
        settingsJPanelLayout.setHorizontalGroup(
            settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(refreshJButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, 108, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addGroup(settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsJPanelLayout.createSequentialGroup()
                        .addComponent(pointSizeJLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pointSizeJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(selPointsJLabel)
                .addGap(18, 18, 18)
                .addGroup(settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(settingsJPanelLayout.createSequentialGroup()
                        .addComponent(xtickJLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xtickJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(selPointsJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ymaxJLabel)
                    .addComponent(ytickJLabel))
                .addGroup(settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ytickJTextField)
                    .addComponent(ymaxJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pngExportJButton)
                    .addComponent(svgExportJButton))
                .addContainerGap())
        );
        settingsJPanelLayout.setVerticalGroup(
            settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsJPanelLayout.createSequentialGroup()
                .addGroup(settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pngExportJButton)
                    .addGroup(settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(settingsJPanelLayout.createSequentialGroup()
                            .addGroup(settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ymaxJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ymaxJLabel)
                                    .addComponent(selPointsJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pointSizeJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pointSizeJLabel)
                                    .addComponent(selPointsJLabel))
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ytickJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ytickJLabel)
                                .addComponent(refreshJButton)
                                .addComponent(xtickJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(xtickJLabel)
                                .addComponent(jButton1)))
                        .addComponent(svgExportJButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(settingsJPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(plotJPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(plotJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshJButtonActionPerformed
        externRefreshPlot();
    }//GEN-LAST:event_refreshJButtonActionPerformed

    private void pointSizeJComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pointSizeJComboBoxActionPerformed
        externRefreshPlot();
    }//GEN-LAST:event_pointSizeJComboBoxActionPerformed

    private void selPointsJComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selPointsJComboBoxActionPerformed
        externRefreshPlot();
    }//GEN-LAST:event_selPointsJComboBoxActionPerformed

    private void plotJPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_plotJPanelMouseClicked
        int x = evt.getX();
        int y = evt.getY();

        ArrayList<Integer> selRows = new ArrayList<>();

        hotSpot.stream().filter((h) -> (x > h.xmin && x < h.xmax && y > h.ymin && y < h.ymax)).forEach((h) -> {
            selRows.add(h.row);
        });

        if (selRows.size() > 0) {
            ListSelectionModel lm = new DefaultListSelectionModel();
            selRows.stream().forEach((i) -> {
                lm.addSelectionInterval(i, i);
            });
            dt.analysisTable.setSelectionModel(lm);
            externRefreshPlot();
            dt.analysisTable.scrollRectToVisible(dt.analysisTable.getCellRect(selRows.get(0),
                    selRows.get(0), false));
            if (evt.getClickCount() == 2) {
                dt.displayMultipleSpotInfo();
            }
        }


    }//GEN-LAST:event_plotJPanelMouseClicked

    private void ymaxJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ymaxJTextFieldKeyReleased
        externRefreshPlot();
    }//GEN-LAST:event_ymaxJTextFieldKeyReleased

    private void svgExportJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svgExportJButtonActionPerformed

        JFileChooser jfc = new JFileChooser();

        jfc.setFileFilter(new FileNameExtensionFilter("Scalable Vector Graphics files", SVG_FORMAT));
        jfc.setSelectedFile(new File(dt.getTitle().concat(".").concat(SVG_FORMAT)));

        int rv = jfc.showSaveDialog(this);
        if (rv == JFileChooser.CANCEL_OPTION) {
            return;
        }

        File f = jfc.getSelectedFile();

        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, SVG_FORMAT, null);

        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        refreshPlot(svgGenerator);

        try {
            svgGenerator.stream(f.getAbsolutePath());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }//GEN-LAST:event_svgExportJButtonActionPerformed

    private void pngExportJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pngExportJButtonActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileNameExtensionFilter("Portable Network Graphics files", PNG_FORMAT));
        jfc.setSelectedFile(new File(dt.getTitle().concat(".").concat(PNG_FORMAT)));

        int rv = jfc.showSaveDialog(this);
        if (rv == JFileChooser.CANCEL_OPTION) {
            return;
        }

        File f = jfc.getSelectedFile();

        try {
            int width = plotJPanel.getWidth();
            int height = plotJPanel.getHeight();
            BufferedImage bi = (BufferedImage) plotJPanel.createImage(width, height);
            refreshPlot(bi.getGraphics());
            ImageIO.write(bi, PNG_FORMAT, f);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }//GEN-LAST:event_pngExportJButtonActionPerformed

    private void ytickJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ytickJTextFieldKeyReleased
        externRefreshPlot();
    }//GEN-LAST:event_ytickJTextFieldKeyReleased

    private void xtickJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_xtickJTextFieldKeyReleased
        externRefreshPlot();
    }//GEN-LAST:event_xtickJTextFieldKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       dt.autoEstimateCutOffs();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        plotType = jComboBox1.getSelectedIndex();
        if (plotType == 0 && getTitle().startsWith("Difference")) {
            String s = getTitle();
            setTitle(s.replaceFirst("Difference", "Ratio"));
        }

        if (plotType == 1 && getTitle().startsWith("Ratio")) {
            String s = getTitle();
            setTitle(s.replaceFirst("Ratio", "Difference"));
        }

        externRefreshPlot();
    }//GEN-LAST:event_jComboBox1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JPanel plotJPanel;
    private javax.swing.JButton pngExportJButton;
    private javax.swing.JComboBox pointSizeJComboBox;
    private javax.swing.JLabel pointSizeJLabel;
    private javax.swing.JButton refreshJButton;
    private javax.swing.JComboBox selPointsJComboBox;
    private javax.swing.JLabel selPointsJLabel;
    private javax.swing.JPanel settingsJPanel;
    private javax.swing.JButton svgExportJButton;
    private javax.swing.JLabel xtickJLabel;
    private javax.swing.JTextField xtickJTextField;
    private javax.swing.JLabel ymaxJLabel;
    private javax.swing.JTextField ymaxJTextField;
    private javax.swing.JLabel ytickJLabel;
    private javax.swing.JTextField ytickJTextField;
    // End of variables declaration//GEN-END:variables
}
