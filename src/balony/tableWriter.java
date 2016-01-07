/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package balony;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Barry Young
 */
public class tableWriter {

    public static void writeTable(JFrame parent, Object[][] tableData, String[] colNames) {
        String opts[] = {"Tab-delimited text (raw)", "Excel .xls", "Excel 2007+ .xlsx"};
        int i = JOptionPane.showOptionDialog(parent, "Choose output format:",
                "Export Table", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opts, opts[0]);
        if (i == JOptionPane.CLOSED_OPTION) {
            return;
        }

        JFileChooser jfc = new JFileChooser();
        if (i == 0) {
            jfc.setFileFilter(new FileNameExtensionFilter("Tab-delimited text files",
                    "txt"));
            jfc.setSelectedFile(new File(parent.getTitle().concat(".txt")));
        }

        if (i == 1) {
            jfc.setFileFilter(new FileNameExtensionFilter("Excel .xls files",
                    "xls"));
            jfc.setSelectedFile(new File(parent.getTitle().concat(".xls")));
        }

        if (i == 2) {
            jfc.setFileFilter(new FileNameExtensionFilter("Excel .xlsx files",
                    "xlsx"));
            jfc.setSelectedFile(new File(parent.getTitle().concat(".xlsx")));
        }

        int rv = jfc.showSaveDialog(parent);
        if (rv == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();

            try {

                if (i == 0) {
                    BufferedWriter out = new BufferedWriter(new FileWriter(f));
                    for (String columnName : colNames) {
                        out.write(columnName);
                        out.write("\t");
                    }
                    out.newLine();
                    for (Object[] tableData1 : tableData) {
                        for (int k = 0; k < colNames.length; k++) {
                            if (tableData1[k] != null) {
                                out.write(tableData1[k].toString());
                            }
                            out.write("\t");
                        }
                        out.newLine();
                    }
                    out.close();
                }

                if (i > 0) {

                    Workbook wb;

                    if (i == 1) {
                        wb = new HSSFWorkbook();
                    } else {
                        wb = new XSSFWorkbook();
                    }

                    String sheetName = parent.getTitle();
                    String[] chars = { ":", "[", "]" ,"/" ,"\\", "?" ,"*"};
                    
                    for(String c : chars) {
                        sheetName = sheetName.replace(c, "");
                    }                   
                    
                    Sheet sheet = wb.createSheet(sheetName);
                    Row r;
                    CellStyle style;
                    sheet.createFreezePane(0, 1);

                    for (int j = 0; j < tableData.length; j++) {
                        r = sheet.createRow(j + 1);
                        for (int k = 0; k < colNames.length; k++) {

                            if (tableData[j][k] != null) {
                                Cell c = r.createCell(k);

                                if (tableData[j][k] instanceof Integer) {
                                    c.setCellType(Cell.CELL_TYPE_NUMERIC);
                                    int v = ((Integer) tableData[j][k]);
                                    c.setCellValue(v);
                                } else {
                                    if (tableData[j][k] instanceof Double) {
                                        c.setCellType(Cell.CELL_TYPE_NUMERIC);
                                        double v = ((Double) tableData[j][k]);
                                        c.setCellValue(v);
                                    } else {

                                        c.setCellType(Cell.CELL_TYPE_STRING);
                                        c.setCellValue(tableData[j][k].toString());

                                    }
                                }
                            }
                        }
                    }

                    r = sheet.createRow(0);
                    style = wb.createCellStyle();
                    Font font = wb.createFont();
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                    style.setFont(font);

                    for (int k = 0; k < colNames.length; k++) {
                        Cell c = r.createCell(k);
                        c.setCellType(Cell.CELL_TYPE_STRING);
                        c.setCellStyle(style);
                        c.setCellValue(colNames[k]);
                    }

                    FileOutputStream fos = new FileOutputStream(f);
                    wb.write(fos);
                    fos.close();
                }

            } catch (Exception ex) {
                Logger.getLogger(dataTable.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(parent, "File error: ".concat(ex.getLocalizedMessage()),
                        "File Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int n = JOptionPane.showOptionDialog(parent, "File saved. Open in default application?", "Message",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    null, null);
            if (n == JOptionPane.YES_OPTION) {
                try {
                    Desktop d = Desktop.getDesktop();
                    d.open(f);
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
    }
}
