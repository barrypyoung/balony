/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * Balony.java
 *
 * Created on 4-Aug-2009, 4:56:48 PM
 */
package balony;

import ij.ImagePlus;
import ij.gui.*;
import ij.io.FileSaver;
import ij.io.Opener;
import ij.measure.ResultsTable;
import ij.plugin.Zoom;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.AutoThresholder;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.process.TypeConverter;
import ij.text.TextWindow;
import ij.util.Tools;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import org.kohsuke.github.GHAsset;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

/**
 *
 * @author Barry Young
 */
public final class Balony extends javax.swing.JFrame implements ClipboardOwner {

    /**
     * Prefs value used to store the last folder accessed from the "Scan" tab.
     */
    public static final String PREFS_SCANFOLDER = "scanFolder";

    /**
     * Prefs value used to store the last folder accessed from the "Image" tab.
     */
    public static final String PREFS_IMAGEFOLDER = "imageFolder";

    /**
     * Prefs value used to store the last folder accessed from the "Analysis"
     * tab.
     */
    public static final String PREFS_ANALYSISFOLDER = "analysisFolder";

    /**
     * Prefs value used to store the last folder accessed from the "control"
     * section of the "Scoring" tab.
     */
    public static final String PREFS_CTRLFOLDER = "ctrlFolder";

    /**
     * Prefs value used to store the last folder accessed from the "experiment"
     * section of the "Scoring" tab.
     */
    public static final String PREFS_EXPFOLDER = "expFolder";

    /**
     * Prefs value used to store the last folder accessed from the "Restore
     * Table" button of the "Analysis" tab.
     */
    public static final String PREFS_TABLEFOLDER = "tableFolder";

    /**
     * Prefs value used to store the last selected value from the layout
     * drop-down of the "Image" tab.
     */
    public static final String PREFS_SCAN_LAYOUT = "scanLayout";

    /**
     * Prefs value used to store the last selected value from the "Plate A"
     * drop-down of the "Image" tab.
     */
    public static final String PREFS_SCAN_PLATE_A = "scanPlateA";

    /**
     * Prefs value used to store the last selected value from the "Plate B"
     * drop-down of the "Image" tab.
     */
    public static final String PREFS_SCAN_PLATE_B = "scanPlateB";

    /**
     * Prefs value used to store the last selected value from the "Plate C"
     * drop-down of the "Image" tab.
     */
    public static final String PREFS_SCAN_PLATE_C = "scanPlateC";

    /**
     * Prefs value used to store the last selected value from the "Plate D"
     * drop-down of the "Image" tab.
     */
    public static final String PREFS_SCAN_PLATE_D = "scanPlateD";

    /**
     * Prefs value used to store the last selected value from the "Rotate
     * Plates" drop-down of the "Image" tab.
     */
    public static final String PREFS_SCAN_ROTATE = "scanRotate";

    /**
     * Prefs value used to store the last selected value from the "Shrink
     * Plates" drop-down of the "Image" tab.
     */
    public static final String PREFS_SCAN_SHRINK = "scanShrink";

    /**
     *
     */
    public static final String PREFS_SCAN_GRAYSCALE = "scanGrayscale";

    /**
     *
     */
    public static final String PREFS_SCAN_CLOSE = "scanClose";

    /**
     *
     */
    public static final String PREFS_SCAN_AUTONAME = "scanAutoname";

    /**
     *
     */
    public static final String PREFS_SCAN_SUBFOLDER = "scanSubfolder";

    /**
     *
     */
    public static final String PREFS_SCAN_PREVIEW = "scanPreview";

    /**
     *
     */
    public static final String PREFS_IMAGE_PRESET = "imageLastPreset";

    /**
     *
     */
    public static final String PREFS_IMAGE_AUTONAME = "imageAutoName";

    /**
     *
     */
    public static final String PREFS_IMAGE_AUTOTHRESH = "imageAutoThresh";

    /**
     *
     */
    public static final String PREFS_IMAGE_AUTOGRID = "imgaeAutoGrid";

    /**
     *
     */
    public static final String PREFS_IMAGE_AUTOQUANT = "imageAutoQuant";

    /**
     *
     */
    public static final String PREFS_IMAGE_AUTOINVERT = "imageAutoInvert";

    /**
     *
     */
    public static final String PREFS_IMAGE_AUTOSAVE = "imageAutoSave";

    /**
     *
     */
    public static final String PREFS_IMAGE_THRESHMETHOD = "imageThreshMethod";

    /**
     *
     */
    public static final String PREFS_IMAGE_THRESVALUE = "imageThreshValue";

    /**
     *
     */
    public static final String PREFS_IMAGE_GRID_SINGLE_CORNER = "imageGridSingleCorner";

    /**
     *
     */
    public static final String THRESH_AUTO = "threshAuto";

    /**
     *
     */
    public static final String THRESH_MANUAL = "threshManual";

    /**
     *
     */
    public static final String PREFS_SCORE_NORM = "scoreNormalization";

    /**
     *
     */
    public static final String PREFS_SCORE_SCOREBY = "scoreScoreBy";

    /**
     *
     */
    public static final String PREFS_SCORE_KEYFILE = "LastKeyFile";

    /**
     *
     */
    public static final String PREFS_SCORE_AUTOANALYZE = "scoreAutoAnalyze";

    /**
     *
     */
    public static final String SCORE_BY_ARRAY = "scoreByArrayPos";

    /**
     *
     */
    public static final String SCORE_BY_ORF = "scoreByORF";

    /**
     *
     */
    public static final String PREFS_ANALYSIS_OPEN_TABLES = "analysisOpenTables";

    /**
     *
     */
    public static final String PREFS_ANALYSIS_LOWCUTOFF = "LowCutOff";

    /**
     *
     */
    public static final String PREFS_ANALYSIS_MAXSPOTSIZE = "MaxSpotSize";

    /**
     *
     */
    public static final String PREFS_ANALYSIS_MINSPOTSIZE = "MinSpotSize";

    /**
     *
     */
    public static final String PREFS_ANALYSIS_HIGHCUTOFF = "HightCutOff";

    /**
     *
     */
    public static final String PREFS_ANALYSIS_SICKCUTOFF = "SickCutOff";

    /**
     *
     */
    public static final String PREFS_ANALYSIS_SICKFILTER = "SickFilterComboBox";

    /**
     *
     */
    public static final String PREFS_ANALYSIS_OVERRIDE_KEYFILE = "analysisKeyFileOverride";

    /**
     *
     */
    public static final String PREFS_OPTIONS_OS_LOOK_AND_FEEL = "osLaF";

    /**
     *
     */
    public static final String PREFS_OPTIONS_UPDATE_CHECK = "optionsUpdateCheck";

    /**
     *
     */
    public static final String PREFS_OPTIONS_UPDATE_BETA_CHECK = "optionsUpdateBetaCheck";

    public static final String PREFS_BALONYVERSION = "BalonyVersion";

    /**
     *
     */
    public static final String PREFS_BALONYLATESTVERSION = "BalonyLatestVersion";

    /**
     *
     */
    public static final String PREFS_ANALYSIS_WIZARDMODE = "analysisWizardMode";

    /**
     *
     */
    public static final String PREFS_XML = "prefs.xml";

    public static final String PREFS_CTRL_TYPE = "scoringControlType";
    public static final String PREFS_ALT_ODD_EVEN = "scoringOddEven";
    public static final String PREFS_NORMALIZATION = "scoringNormMethod";

    /**
     *
     */
    public static final String AREA_COL = "Area";

    /**
     *
     */
    public static final String HEIGHT_COL = "Height";

    /**
     *
     */
    public static final String BALONY_RAW_DATA = "Balony raw data";

    /**
     *
     */
    public static final String BEGIN_DATA = "<Begin Data>";

    /**
     *
     */
    public static final String CALIBRATED = "caliibrated";

    /**
     *
     */
    public static final String KEYFILE = ".key";

    /**
     *
     */
    public static final String PRESET = ".preset";

    /**
     *
     */
    public static final String PLATE = "plate";

    /**
     *
     */
    public static final String RT_AREA = "Area";

    /**
     *
     */
    public static final String RT_HEIGHT = "Height";

    /**
     *
     */
    public static final String RT_WIDTH = "Width";

    /**
     *
     */
    public static final String RT_X = "X";

    /**
     *
     */
    public static final String RT_Y = "Y";

    /**
     *
     */
    public static final String SCORE_HEADER
            = "Screen\tORF Name\tGene Name\tPlate\tRow\tColumn\t\t\t\t\t\tCtrl Mean\tCtrl SD\tExp Mean\tExp SD\tSize Difference\t";

    /**
     *
     */
    public static final String SET = "set";

    /**
     *
     */
    public static final String AUTO_ANALYZE_LOG = "AutoAnalyze.log";
    public float minX,
            /**
             *
             */
            minY,
            /**
             *
             */
            maxX,
            /**
             *
             */
            maxY,
            /**
             *
             */
            stepX,
            /**
             *
             */
            stepY,
            /**
             *
             */
            circ,
            /**
             *
             */
            snap,
            /**
             *
             */
            dx,
            /**
             *
             */
            dy;

    /**
     *
     */
    public static double theta;
    public static int Area[][],
            /**
             *
             */
            /**
             *
             */
            /**
             *
             */
            /**
             *
             */
            xCoord[][], yCoord[][], width[][], height[][];
    public static int rows,
            /**
             *
             */
            cols,
            /**
             *
             */
            dpi;
    public static boolean loaded,
            /**
             *
             */
            rotated,
            /**
             *
             */
            gridded,
            /**
             *
             */
            threshed,
            /**
             *
             */
            quant,
            /**
             *
             */
            inverted,
            /**
             *
             */
            fullAuto;
    public ImagePlus loadedIm,
            /**
             *
             */
            oIm,
            /**
             *
             */
            oToCrop,
            /**
             *
             */
            toCrop,
            /**
             *
             */
            cropA,
            /**
             *
             */
            cropB,
            /**
             *
             */
            cropC,
            /**
             *
             */
            cropD,
            /**
             *
             */
            autoTmp,
            /**
             *
             */
            zoomBox;
    public static ColorProcessor oriCp,
            /**
             *
             */
            tcCp;

    /**
     *
     */
    public static File currFolder;
    public static Integer currSet,
            /**
             *
             */
            currPlate;
    public int lastThresh,
            /**
             *
             */
            badSpots,
            /**
             *
             */
            bestThresh,
            /**
             *
             */
            bestSpots;

    /**
     *
     */
    public static String message;

    /**
     *
     */
    public int rethresh;
    public File currFile,
            /**
             *
             */
            currScanFile;

    /**
     *
     */
    public platenameData[] ctrlplateData;

    /**
     *
     */
    public platenameData[] expplateData;

    /**
     *
     */
    public spotData ctrlData;

    /**
     *
     */
    public spotData expData;
    public int minCtrlSet,
            /**
             *
             */
            maxCtrlSet,
            /**
             *
             */
            minExpSet,
            /**
             *
             */
            maxExpSet;
    public int minCtrlPlate,
            /**
             *
             */
            maxCtrlPlate,
            /**
             *
             */
            minExpPlate,
            /**
             *
             */
            maxExpPlate;
    public int autoCheckPos,
            /**
             *
             */
            fullAutoFail;

    /**
     *
     */
    public StringBuilder messageText;

    /**
     *
     */
    public HashSet<dataTable> dataTables;

    /**
     *
     */
    public HashMap<String, analysisData> aD;

    /**
     *
     */
    public HashSet<arrayDefinition> arrayDefs;
    public HashSet<summarizeJFrame> sumTables;

    /**
     *
     */
    public dataTable currentDT;

    /**
     *
     */
    public TreeMap<String, plateArray> allArraysMap;

    /**
     *
     */
    public Properties prefs;

    /**
     *
     */
    public HashMap<String, File> keyFiles;

    /**
     *
     */
    public String keyOrfs[][][];

    /**
     *
     */
    public String keyGenes[][][];

    /**
     *
     */
    public HashMap<File, imageSettings> iSet;

    /**
     *
     */
    public HashMap<String, String> keyOrfList;

    /**
     *
     */
    public static HashMap<String, sgdInfo> allSGDInfo;

    /**
     *
     */
    public HashMap<String, Integer> genomeIndex;

    /**
     *
     */
    public gridPresets gridP;

    /**
     *
     */
    public colonySize cSize;

    /**
     *
     */
    public quantSettings quantP;

    /**
     *
     */
    public autoWorker fullAutoWorker;

    /**
     *
     */
    public processWorker pw;

    /**
     *
     */
    public gridWorker gw;

    /**
     *
     */
    public quantWorker qw;

    /**
     *
     */
    public myWindowListener mwl;

    /**
     *
     */
    public HashMap<String, Integer[]> grOval;

    /**
     *
     */
    public HashMap<String, Integer[]> mgOval;

    /**
     *
     */
    public HashMap<String, Integer[]> redRect;

    /**
     *
     */
    public ArrayList<Integer[]> yeOval;
    public boolean fileMod,
            /**
             *
             */
            norethresh,
            /**
             *
             */
            calibrated,
            /**
             *
             */
            incalibration,
            /**
             *
             */
            autoGridSetting;
    public QuantScan qs,
            /**
             *
             */
            qs1;

    /**
     *
     */
    public ImageWindow imWin;

    /**
     *
     */
    public int multiPAcount;

    /**
     *
     */
    public boolean stopped;

    /**
     *
     */
    public boolean resetGrid;

    /**
     *
     */
    public boolean useOSLaF;

    /**
     *
     */
    public File scanPreview;

    /**
     *
     */
    public JLabel tip;

    /**
     *
     */
    public Image balloonImage;

    /**
     *
     */
    public String BalonyVersion;

    /**
     *
     */
    public static final String SGD_FEATURES_FILE = "SGD_features.tab";

    /**
     *
     */
    public messageJFrame messageFrame;
    public File queryArrayFile;

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
    }

    /**
     * Creates new form Balony
     */
    public Balony() {
        quantP = new quantSettings();
        quantP.balony = this;

        gridP = new gridPresets();
        messageFrame = new messageJFrame();

        gridP.balony = this;
        cSize = new colonySize();
        prefs = new Properties();
        try {
            File f = new File(get_prefsfile_name());
            prefs.loadFromXML(new FileInputStream(f));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        String ps = "";

        // Load Prefs
        calibrated = prefs.containsKey(CALIBRATED);

        String p;
        quantP.circTextField.setText(prefs.getProperty(quantP.CIRC, "0.8"));
        quantP.snapTextField.setText(prefs.getProperty(quantP.SNAP, "0.25"));
        quantP.minpixelsTextField.setText(prefs.getProperty(quantP.MINPIXELS, "8"));
        p = prefs.getProperty(quantP.RETHRESH, "TRUE");
        if (p.equals("TRUE")) {
            quantP.reThreshJCheckBox.setSelected(true);
        } else {
            quantP.reThreshJCheckBox.setSelected(false);
        }

        stopped = false;
        initComponents();

        scanLayout.setSelectedItem(prefs.getProperty(PREFS_SCAN_LAYOUT,
                "2x2"));
        scanAComboBox.setSelectedItem(prefs.getProperty(PREFS_SCAN_PLATE_A,
                "2"));
        scanBComboBox.setSelectedItem(prefs.getProperty(PREFS_SCAN_PLATE_B,
                "1"));
        scanCComboBox.setSelectedItem(prefs.getProperty(PREFS_SCAN_PLATE_C,
                "4"));
        scanDComboBox.setSelectedItem(prefs.getProperty(PREFS_SCAN_PLATE_D,
                "3"));
        scanrotateComboBox.setSelectedItem(prefs.getProperty(PREFS_SCAN_ROTATE,
                "90° CW"));
        scanshrinkComboBox.setSelectedItem(prefs.getProperty(PREFS_SCAN_SHRINK,
                "None"));
        scangrayCheckBox.setSelected(prefs.getProperty(PREFS_SCAN_GRAYSCALE,
                "1").equals("1"));
        scancloseCheckBox.setSelected(prefs.getProperty(PREFS_SCAN_CLOSE,
                "1").equals("1"));
        scanAutoPlateNameJCheckBox.setSelected(prefs.getProperty(PREFS_SCAN_AUTONAME,
                "1").equals("1"));
        scansubfolderTextField.setText(prefs.getProperty(PREFS_SCAN_SUBFOLDER,
                "Cropped"));
        scanPreviewJCheckBox.setSelected(prefs.getProperty(PREFS_SCAN_PREVIEW,
                "1").equals("1"));

        autoNameJCheckBox.setSelected(prefs.getProperty(PREFS_IMAGE_AUTONAME,
                "1").equals("1"));
        autoThreshJCheckBox.setSelected(prefs.getProperty(PREFS_IMAGE_AUTOTHRESH,
                "1").equals("1"));
        autoGridJCheckBox.setSelected(prefs.getProperty(PREFS_IMAGE_AUTOGRID,
                "1").equals("1"));
        autoQuantJCheckBox.setSelected(prefs.getProperty(PREFS_IMAGE_AUTOQUANT,
                "1").equals("1"));
        autoInvertJCheckBox.setSelected(prefs.getProperty(PREFS_IMAGE_AUTOINVERT,
                "1").equals("1"));
        autoSaveJCheckBox.setSelected(prefs.getProperty(PREFS_IMAGE_AUTOSAVE,
                "1").equals("1"));

        if (prefs.getProperty(PREFS_IMAGE_THRESHMETHOD, THRESH_AUTO).equals(THRESH_AUTO)) {
            threshRadioAuto.setSelected(true);
        } else {
            threshRadioManual.setSelected(true);
        }

        String thr = prefs.getProperty(PREFS_IMAGE_THRESVALUE, "165");
        threshJSlider.setValue(Integer.parseInt(thr));
        imageThreshManualJTextField.setText(thr);
        updateSlider();

        plateCtrlRadioButton.setSelected(prefs.getProperty(PREFS_CTRL_TYPE, "1").equals("1"));
        altColRadioButton.setSelected(prefs.getProperty(PREFS_CTRL_TYPE, "1").equals("2"));
        altRowRadioButton.setSelected(prefs.getProperty(PREFS_CTRL_TYPE, "1").equals("3"));

        altOddRadioButton.setSelected(prefs.getProperty(PREFS_ALT_ODD_EVEN, "1").equals("1"));
        altEvenRadioButton.setSelected(prefs.getProperty(PREFS_ALT_ODD_EVEN, "1").equals("2"));

        for (Component c : ctrlPanel.getComponents()) {
            if (plateCtrlRadioButton.isSelected()) {

                c.setEnabled(true);
                altOddRadioButton.setEnabled(false);
                altEvenRadioButton.setEnabled(false);

            } else {
                c.setEnabled(false);
                altOddRadioButton.setEnabled(true);
                altEvenRadioButton.setEnabled(true);
            }
        }

        normPlateMedianButton.setSelected(prefs.getProperty(PREFS_NORMALIZATION, "1").equals("1"));
        normORFButton.setSelected(prefs.getProperty(PREFS_NORMALIZATION, "1").equals("2"));
        normNoneButton.setSelected(prefs.getProperty(PREFS_NORMALIZATION, "1").equals("3"));

        normButtonPressed();

        scoreRCComboBox.setSelectedItem(prefs.getProperty(PREFS_SCORE_NORM,
                "Median"));
        if (prefs.getProperty(PREFS_SCORE_SCOREBY, SCORE_BY_ARRAY).equals(SCORE_BY_ARRAY)) {
            scoreByArrayPosRadioButton.setSelected(true);
        } else {
            scoreByOrfRadioButton.setSelected(true);
        }

        autoAnalyzeJCheckBox.setSelected(prefs.getProperty(PREFS_SCORE_AUTOANALYZE,
                "1").equals("1"));
        wizardModeJCheckBox.setSelected(prefs.getProperty(PREFS_ANALYSIS_WIZARDMODE,
                "1").equals("1"));

        analysisOpenDataTablesJCheckBox.setSelected(prefs.getProperty(
                PREFS_ANALYSIS_OPEN_TABLES, "1").equals("1"));

        minSpotSizeJTextField.setText(prefs.getProperty(PREFS_ANALYSIS_MINSPOTSIZE, "0.05"));
        maxSpotSizeJTextField.setText(prefs.getProperty(PREFS_ANALYSIS_MAXSPOTSIZE, "100"));
        lowCutOffJTextField.setText(prefs.getProperty(PREFS_ANALYSIS_LOWCUTOFF, "0.85"));
        upperCutOffJTextField.setText(prefs.getProperty(PREFS_ANALYSIS_HIGHCUTOFF, "1.17"));
        sickCutOffTextJField.setText(prefs.getProperty(PREFS_ANALYSIS_SICKCUTOFF, "0.2"));
        analysisSickFliterJComboBox.setSelectedItem(prefs.getProperty(PREFS_ANALYSIS_SICKFILTER,
                "control spot"));

        updateCheckJCheckBox.setSelected(prefs.getProperty(PREFS_OPTIONS_UPDATE_CHECK,
                "1").equals("1"));
        updateCheckJCheckBox1.setSelected(prefs.getProperty(PREFS_OPTIONS_UPDATE_BETA_CHECK,
                "0").equals("1"));

        if (prefs.containsKey(PREFS_BALONYVERSION)) {
            BalonyVersion = prefs.getProperty(PREFS_BALONYVERSION);
            if (BalonyVersion.length() > 35) {
                currVersionJLabel.setText(BalonyVersion.substring(35, BalonyVersion.length() - 4));
            } else {
                currVersionJLabel.setText(BalonyVersion);
            }
        } else {
            BalonyVersion = "Unknown";
        }

        System.out.println("Prefs loaded");

        latestVersionJLabel.setText(prefs.getProperty(PREFS_BALONYLATESTVERSION, "Unknown"));

        if (prefs.containsKey(PREFS_IMAGE_PRESET)) {
            ps = prefs.getProperty(PREFS_IMAGE_PRESET);
        }

        balloonImage = new ImageIcon(main.class.getResource("/resources/balloon.png")).getImage();
        setIconImage(balloonImage);
        gridP.setIconImage(balloonImage);
        quantP.setIconImage(balloonImage);

        imageFileJList.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }
                JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
                try {
                    @SuppressWarnings("unchecked")
                    java.util.List<File> fileList = (java.util.List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    ArrayList<File> myFiles = new ArrayList<>();
                    fileList.stream().forEach((f) -> {
                        if (f.isDirectory()) {
                            File ftmp[] = f.listFiles();
                            for (File ff : ftmp) {
                                if (!ff.getAbsolutePath().contains("autocheck_images")) {
                                    myFiles.add(ff);
                                }
                            }
                        } else if (!f.getAbsolutePath().contains("autocheck_images")) {
                            myFiles.add(f);
                        }
                    });
                    ArrayList<File> fFiles = new ArrayList<>();
                    myFiles.stream().forEach((myF) -> {
                        String fn = myF.getName().toLowerCase();
                        if ((fn.endsWith(".jpg") || fn.endsWith(".png") || fn.endsWith(".tif")
                                || fn.endsWith(".jpeg")) && !fn.startsWith(".")) {
                            fFiles.add(myF);
                        }
                    });
                    loadImageFileList((File[]) fFiles.toArray(new File[fFiles.size()]));
                } catch (UnsupportedFlavorException | IOException ex) {
                    Logger.getLogger(Balony.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
                return true;
            }
        });

//        iSet = new HashMap<File, imageSettings>();
        allArraysMap = new TreeMap<>();
        keyFiles = new HashMap<>();
        dataTables = new HashSet<>();
        sumTables = new HashSet<>();
        aD = new HashMap<>();
        arrayDefs = new HashSet<>();
        loadPresets();
        loadKeyFiles();
        autoCheckPos = -1;

        if (!ps.equals("")) {
            gridChoicejComboBox.setSelectedItem(ps);
        }

        messageText = new StringBuilder();

        Action updateMessages = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (messageText != null) {
                    messageFrame.addText(messageText.toString());
                    messageText.setLength(0);
                }
            }
        };
        Timer t = new Timer(10, updateMessages);
        t.start();

        if (updateCheckJCheckBox.isSelected()) {

            updateWorker uwo = new updateWorker();
            uwo.background = true;
            uwo.execute();
        }

        // Change listeners:
        ChangeListener scoreChangeListener = (ChangeEvent e) -> {
            JTabbedPane jpane = (JTabbedPane) e.getSource();
            if (jpane.getSelectedComponent().equals(scoringtabaPanel)) {
                updateScoreTab();
            }
        };

        tabPane.addChangeListener(scoreChangeListener);

        if (System.getProperty("os.name").startsWith("Mac")) {
            osLAFJRadioButton.setVisible(false);
        }

        // Load SGD_features.tab
        loadSGDInfo();

        messageFrame.setIconImage(balloonImage);
        messageFrame.setVisible(true);

        if (getHeight() + messageFrame.getHeight() > getScreenHeight()) {
            messageFrame.setLocation(getWidth(), 0);
        } else {

            messageFrame.setLocation(0, getHeight());
        }
    }

    /**
     *
     * @return
     */
    public int getScreenHeight() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        return gd.getDisplayMode().getHeight();
    }

    /**
     *
     * @return
     */
    public int getScreenWidth() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        return gd.getDisplayMode().getWidth();
    }

    /**
     *
     * @param files
     */
    public void loadImageFileList(File[] files) {
        imageFileJList.removeAll();
        fileListCellRenderer flcr = new fileListCellRenderer();
        flcr.quantP = quantP;
        imageFileJList.setCellRenderer(flcr);
        imageFileJList.setListData(files);
    }
    // Read in gene info from SGD_feaures.tab

    /**
     *
     * @return
     */
    public String get_appdata_folder() {
        String roam = "";

        if (SystemUtils.IS_OS_WINDOWS) {
            roam = System.getenv("APPDATA") + "\\Balony\\";
            File af = new File(roam);
            if (!af.exists()) {
                af.mkdir();
            }
        }
        return roam;
    }

    /**
     *
     * @return
     */
    public String get_sgdfile_name() {
        return SGD_FEATURES_FILE;
    }

    /**
     *
     * @return
     */
    public static String get_prefsfile_name() {
        return PREFS_XML;
    }

    private void loadSGDInfo() {
        File f = new File(get_sgdfile_name());

        if (f.exists()) {
            Long l = f.lastModified();
            if (System.currentTimeMillis() - l > 8640000000l) {
                int n = JOptionPane.showOptionDialog(this,
                        "SGD Info is over 100 days old. Update?",
                        "Update SGD Info?", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (n == JOptionPane.YES_OPTION) {
                    new sgdUpdater().execute();
                    return;
                }
            }
            allSGDInfo = new HashMap<>();
            genomeIndex = new HashMap<>();
            try {
                BufferedReader in = new BufferedReader(new FileReader(f));
                String t;
                while ((t = in.readLine()) != null) {
                    String[] sg = t.split("\t");
                    if (sg.length > 15 && (sg[1].equals("ORF") || sg[1].equals("pseudogene"))) {
                        sgdInfo sgdi = new sgdInfo();
                        sgdi.sgdID = sg[0];
                        sgdi.qualifier = sg[2];
                        sgdi.gene = sg[4];
                        sgdi.strand = sg[11];
                        sgdi.desc = sg[15];
                        if (sg[5].length() > 0) {
                            sgdi.aliases = new ArrayList<>(Arrays.asList(sg[5].split("\\|")));
                        }
                        try {
                            sgdi.chr = Integer.parseInt(sg[8]);
                            sgdi.startCoord = Integer.parseInt(sg[9]);
                            sgdi.stopCoord = Integer.parseInt(sg[10]);
                        } catch (NumberFormatException e) {
                        }
                        int gI = sgdi.chr * 10000000 + sgdi.startCoord;
                        if (sgdi.aliases != null && sgdi.aliases.size() > 0) {
                            sgdi.aliases.stream().forEach((s) -> {
                                genomeIndex.put(s, gI);
                            });
                        }
                        genomeIndex.put(sg[3], gI);
                        allSGDInfo.put(sg[3], sgdi);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        } else {
            new sgdUpdater().execute();
        }
    }

    public spotCompare openSpotCompare(int plate, int row, int column, ArrayList<String> screens) {

        spotCompare spc = new spotCompare();
        spc.setIconImage(balloonImage);
        spc.setTitle("Spot Compare");

        ArrayList<String> ads = new ArrayList<>();

        if (screens.isEmpty()) {
            for (int i = 0; i < dataTablesComboBox.getItemCount(); i++) {
                ads.add(dataTablesComboBox.getItemAt(i).toString());
            }
        } else {
            ads = screens;
        }

        spc.setScreens(ads);
        spc.balony = this;
        spc.setPosition(plate, row, column);
        spc.setVisible(true);
        return spc;
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     */
    public class sgdUpdater extends SwingWorker<String, Void> {

        /**
         *
         */
        public static final String SGD_FEATURES_URL
                = "https://downloads.yeastgenome.org/curation/chromosomal_feature/SGD_features.tab";

        @Override
        protected String doInBackground() throws Exception {

            messageText.append("\nAttempting to update SGD_features.tab\nDownloading");
            FileOutputStream fos = null;
            BufferedInputStream buf = null;
            try {
                URL sgdURL = new URL(SGD_FEATURES_URL);

                buf = new BufferedInputStream(sgdURL.openStream());
                int size = sgdURL.openConnection().getContentLength();
                System.out.println("Size of SGD_features.tab: " + size);

                final String filename = get_sgdfile_name();
                fos = new FileOutputStream(filename);
                int r;
                int cnt = 0;
                int done = 0;
                while ((r = buf.read()) != -1) {
                    cnt++;
                    if (cnt == size / 4) {
                        done += 25;
                        messageText.append(done).append("%");
                        cnt = 0;
                    } else if (cnt % (size / 16) == 0) {
                        messageText.append(".");
                    }
                    fos.write(r);
                }

                messageText.append("\nSGD_features.tab successfully updated");

            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            } finally {
                if (fos != null) {
                    fos.close();
                }

                if (buf != null) {
                    buf.close();
                }
            }

            return "";
        }

        @Override
        protected void done() {
            super.done();
        }
    }

    /**
     *
     */
    public void updateSGDInfo() {
        new sgdUpdater().execute();
    }
    // Load key files (plate position -> ORF mappings)

    private void loadKeyFiles() {
        String keyName2 = "";
        String lkf = prefs.getProperty(PREFS_SCORE_KEYFILE);
        if (lkf != null) {
            File ff = new File(lkf);
            if (ff.getName().endsWith(KEYFILE)) {
                try {
                    keyName2 = ff.getName();
                    if (keyName2.contains(".")) {
                        keyName2 = keyName2.substring(0, keyName2.lastIndexOf("."));
                    }
                    keyFiles.put(keyName2, ff);
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
        File[] f = new File(".").listFiles();
        if (f != null) {
            for (File ff : f) {
                if (ff.getName().endsWith(KEYFILE)) {
                    try {
                        String keyName = ff.getName();
                        if (keyName.contains(".")) {
                            keyName = keyName.substring(0, keyName.lastIndexOf("."));
                        }
                        keyFiles.put(keyName, ff);
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
            }
        }
        scoreKeysComboBox.removeAllItems();
        new TreeSet<>(keyFiles.keySet()).stream().forEach((k) -> {
            try {
                if (new File(keyFiles.get(k).toString()).exists()) {
                    scoreKeysComboBox.addItem(k);
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        });
        if (keyName2 != null && !keyName2.equals("")) {
            scoreKeysComboBox.setSelectedItem(keyName2);
        }
    }
    // Load preset aray dimensions

    /**
     *
     */
    public void loadPresets() {
        Object o = gridChoicejComboBox.getSelectedItem();
        File[] f = null;
        try {
            f = new File(".").getCanonicalFile().listFiles();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        if (f != null) {
            for (File ff : f) {
                if (ff.getName().endsWith(PRESET)) {
                    Properties pr = new Properties();
                    try {
                        BufferedInputStream in = new BufferedInputStream(new FileInputStream(ff));
                        pr.loadFromXML(in);
                        plateArray p = new plateArray();
                        p.setCols(Integer.parseInt(pr.getProperty("cols")));
                        p.setRows(Integer.parseInt(pr.getProperty("rows")));
                        p.setName(pr.getProperty("name"));
                        p.setDpi(Integer.parseInt(pr.getProperty("dpi")));
                        p.setXstp(Float.parseFloat(pr.getProperty("dx")));
                        p.setYstp(Float.parseFloat(pr.getProperty("dy")));
                        allArraysMap.put(p.getName(), p);
                    } catch (IOException | NumberFormatException e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
            }
        }

        String[] aamka = (String[]) allArraysMap.keySet().toArray(new String[allArraysMap.size()]);

        DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<>(aamka);
        gridChoicejComboBox.setModel(dcbm);
        if (o != null) {
            gridChoicejComboBox.setSelectedItem(o);
        }
        gridChoicejComboBox.addItem("Custom...");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scoreSaveButtonGroup = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        analysisArrayPanel = new javax.swing.JPanel();
        analysisArrayComboBox = new javax.swing.JComboBox<>();
        buttonGroup2 = new javax.swing.ButtonGroup();
        scanprocessButton = new javax.swing.JButton();
        scoreRCComboBox = new javax.swing.JComboBox();
        updateCheckButton = new javax.swing.JButton();
        buttonGroup3 = new javax.swing.ButtonGroup();
        scoreCompetitionJCheckBox = new javax.swing.JCheckBox();
        normButtonGroup = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        tabPane = new javax.swing.JTabbedPane();
        scanPanel = new javax.swing.JPanel();
        scanPlateNamingPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        scanBTextField = new javax.swing.JTextField(20);
        scanCTextField = new javax.swing.JTextField(20);
        scanDTextField = new javax.swing.JTextField(20);
        scanbaseTextField = new javax.swing.JTextField(20);
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        scanaTextField = new javax.swing.JTextField(20);
        jLabel19 = new javax.swing.JLabel();
        scansaveBCheckBox = new javax.swing.JCheckBox();
        scansaveACheckBox = new javax.swing.JCheckBox();
        scansaveCCheckBox = new javax.swing.JCheckBox();
        scansaveDCheckBox = new javax.swing.JCheckBox();
        jLabel26 = new javax.swing.JLabel();
        scanPlateNumbersPanel = new javax.swing.JPanel();
        scanDPanel = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        scanDComboBox = new javax.swing.JComboBox();
        scanCPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        scanCComboBox = new javax.swing.JComboBox();
        scanAPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        scanAComboBox = new javax.swing.JComboBox();
        scanBPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        scanBComboBox = new javax.swing.JComboBox();
        scanSetLabel = new javax.swing.JLabel();
        scansetComboBox = new javax.swing.JComboBox();
        scanLayoutJLabel = new javax.swing.JLabel();
        scanLayout = new javax.swing.JComboBox();
        scanOffsetJComboBox = new javax.swing.JComboBox();
        scanOffsetJLabel = new javax.swing.JLabel();
        scanOptionsPanel = new javax.swing.JPanel();
        scanrotateComboBox = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        scanshrinkComboBox = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        scangrayCheckBox = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        scansubfolderTextField = new javax.swing.JTextField(10);
        scancloseCheckBox = new javax.swing.JCheckBox();
        scanAutoPlateNameJCheckBox = new javax.swing.JCheckBox();
        scanchoosefolderButton = new javax.swing.JButton();
        scanprocessallButton = new javax.swing.JButton();
        scanFolderJTextField = new javax.swing.JTextField();
        scanFileJScrollPane = new javax.swing.JScrollPane();
        scanFileJList = new javax.swing.JList<>();
        scanFilenameHelpJButton = new javax.swing.JButton();
        scanSelectAllButton = new javax.swing.JButton();
        scanPreviewPanel = new javax.swing.JPanel();
        scanPreviewJCheckBox = new javax.swing.JCheckBox();
        imagePanel = new javax.swing.JPanel();
        manualspotPanel = new javax.swing.JPanel();
        manualspotdefineButton = new javax.swing.JButton();
        manualspotResetButton = new javax.swing.JButton();
        gridPanel = new javax.swing.JPanel();
        definegridButton = new javax.swing.JButton();
        gridclearButton = new javax.swing.JButton();
        pointgridCheckBox = new javax.swing.JCheckBox();
        gridCentreButton = new javax.swing.JButton();
        autoPanel = new javax.swing.JPanel();
        autoQuantJCheckBox = new javax.swing.JCheckBox();
        autoGridJCheckBox = new javax.swing.JCheckBox();
        autoNameJCheckBox = new javax.swing.JCheckBox();
        autoInvertJCheckBox = new javax.swing.JCheckBox();
        autoThreshJCheckBox = new javax.swing.JCheckBox();
        autoSaveJCheckBox = new javax.swing.JCheckBox();
        platePanel = new javax.swing.JPanel();
        platenameLabel = new javax.swing.JLabel();
        setnumberLabel = new javax.swing.JLabel();
        platenumberLabel = new javax.swing.JLabel();
        plateNameJTextField = new javax.swing.JTextField();
        setNumberJTextField = new javax.swing.JTextField();
        plateNumberJTextField = new javax.swing.JTextField();
        threshPanel = new javax.swing.JPanel();
        threshRadioAuto = new javax.swing.JRadioButton();
        threshRadioManual = new javax.swing.JRadioButton();
        threshJSlider = new javax.swing.JSlider();
        imageThreshManualJTextField = new javax.swing.JTextField();
        zoomPanel = new javax.swing.JPanel();
        zoominButton = new javax.swing.JButton();
        zoomoutButton = new javax.swing.JButton();
        rotatePanel = new javax.swing.JPanel();
        rotatecorrectorButton = new javax.swing.JButton();
        resetrotateButton = new javax.swing.JButton();
        autocheckPanel = new javax.swing.JPanel();
        viewLogButton = new javax.swing.JButton();
        autocheckbackButton = new javax.swing.JButton();
        autocheckforwardButton = new javax.swing.JButton();
        choosefolderButton = new javax.swing.JButton();
        thresholdButton = new javax.swing.JButton();
        quantButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        fullautoButton = new javax.swing.JButton();
        advancedPanel = new javax.swing.JPanel();
        imageShowParamsButton = new javax.swing.JButton();
        imageFileJListScrollPane = new javax.swing.JScrollPane();
        imageFileJList = new javax.swing.JList<>();
        folderJTextField = new javax.swing.JTextField();
        autoGridButton = new javax.swing.JButton();
        presetPanel = new javax.swing.JPanel();
        gridChoicejComboBox = new javax.swing.JComboBox<>();
        imageShowPresetsButton = new javax.swing.JButton();
        imageStopButton = new javax.swing.JButton();
        toggleInputOutputButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();
        showQuantButton = new javax.swing.JButton();
        imageFolderJLabel = new javax.swing.JLabel();
        removeButton = new javax.swing.JButton();
        scoringtabaPanel = new javax.swing.JPanel();
        normalPanel = new javax.swing.JPanel();
        scoreRCJCheckBox = new javax.swing.JCheckBox();
        scoreSpatialJCheckBox = new javax.swing.JCheckBox();
        scoreUndergrowthJCheckBox = new javax.swing.JCheckBox();
        scoreNormPercentileLabel = new javax.swing.JLabel();
        scoreNormPercentileTextField = new javax.swing.JTextField();
        ctrlPanel = new javax.swing.JPanel();
        ctrldirTextField = new javax.swing.JTextField(40);
        ctrldirButton = new javax.swing.JButton();
        ctrlplateComboBox = new javax.swing.JComboBox<>();
        expPanel = new javax.swing.JPanel();
        expButton = new javax.swing.JButton();
        expdirTextField = new javax.swing.JTextField(40);
        expplateComboBox = new javax.swing.JComboBox<>();
        savescorePanel = new javax.swing.JPanel();
        saveScoreButton = new javax.swing.JButton();
        scorenameTextField = new javax.swing.JTextField(40);
        saveScoreAvgButton = new javax.swing.JButton();
        scoreByArrayPosRadioButton = new javax.swing.JRadioButton();
        scoreByOrfRadioButton = new javax.swing.JRadioButton();
        autoAnalyzeJCheckBox = new javax.swing.JCheckBox();
        scoreKeyfilePanel = new javax.swing.JPanel();
        scoreArraykeyLabel = new javax.swing.JLabel();
        scoreKeysComboBox = new javax.swing.JComboBox<>();
        keyFileLoadButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        useQueryKeyJCheckBox = new javax.swing.JCheckBox();
        scoringRefreshButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        plateCtrlRadioButton = new javax.swing.JRadioButton();
        altColRadioButton = new javax.swing.JRadioButton();
        altRowRadioButton = new javax.swing.JRadioButton();
        altOddRadioButton = new javax.swing.JRadioButton();
        altEvenRadioButton = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        normPlateMedianButton = new javax.swing.JRadioButton();
        normORFButton = new javax.swing.JRadioButton();
        normORFJTextField = new javax.swing.JTextField();
        normNoneButton = new javax.swing.JRadioButton();
        analysistabPanel = new javax.swing.JPanel();
        analysisTablesPanel = new javax.swing.JPanel();
        analysisLoadButton = new javax.swing.JButton();
        dataTablesComboBox = new javax.swing.JComboBox<>();
        analysisOpenDataTablesJCheckBox = new javax.swing.JCheckBox();
        restoreTableButton = new javax.swing.JButton();
        wizardModeJCheckBox = new javax.swing.JCheckBox();
        analysisTableRemoveButton = new javax.swing.JButton();
        analysisTableShowButton = new javax.swing.JButton();
        sgdFeaturesJPanel = new javax.swing.JPanel();
        analysisOverrideKeyFileCheckBox = new javax.swing.JCheckBox();
        downloadSGDInfoButton = new javax.swing.JButton();
        defaultTableSettingsJPanel = new javax.swing.JPanel();
        lowCutOffJTextField = new javax.swing.JTextField();
        sickCutOffTextJField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        upperCutOffJTextField = new javax.swing.JTextField();
        minSpotSizeJTextField = new javax.swing.JTextField();
        analysisSickFliterJComboBox = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        maxSpotSizeJTextField = new javax.swing.JTextField();
        analysisSummaryPanel = new javax.swing.JPanel();
        analysisGenerateSummaryTablesHiddenButton = new javax.swing.JButton();
        analysisGenerateSummaryTablesVisibleButton = new javax.swing.JButton();
        analysisShowSummaryTables = new javax.swing.JButton();
        analysisSummaryClipboardCopyTypeComboBox = new javax.swing.JComboBox();
        analysisSummaryDefaultsPanel = new javax.swing.JPanel();
        analysisSummaryMedianCheckBox = new javax.swing.JCheckBox();
        analysisSummaryPairedSpotsCheckBox = new javax.swing.JCheckBox();
        analysisSummaryCopyAllClipboardButton = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        optionsPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        javaLAFJRadioButton = new javax.swing.JRadioButton();
        osLAFJRadioButton = new javax.swing.JRadioButton();
        nimbusLAFJRadioButton = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        updaterJPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        currVersionJLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        latestVersionJLabel = new javax.swing.JLabel();
        updateCheckJCheckBox = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        updateCheckJCheckBox1 = new javax.swing.JCheckBox();
        contactJPanel = new javax.swing.JPanel();
        contactJLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        contactJLabel1 = new javax.swing.JLabel();

        analysisArrayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Array"));

        analysisArrayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Load..." }));
        analysisArrayComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisArrayComboBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout analysisArrayPanelLayout = new org.jdesktop.layout.GroupLayout(analysisArrayPanel);
        analysisArrayPanel.setLayout(analysisArrayPanelLayout);
        analysisArrayPanelLayout.setHorizontalGroup(
            analysisArrayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisArrayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(analysisArrayComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        analysisArrayPanelLayout.setVerticalGroup(
            analysisArrayPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, analysisArrayPanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(analysisArrayComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        scanprocessButton.setText("Process");
        scanprocessButton.setToolTipText("Click to save quantified data.");
        scanprocessButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanprocessButtonActionPerformed(evt);
            }
        });

        scoreRCComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Row / Column", "Row / Column + Spatial", "Spatial", "Off" }));
        scoreRCComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreRCComboBoxActionPerformed(evt);
            }
        });

        updateCheckButton.setText("Check for updates now");
        updateCheckButton.setEnabled(false);
        updateCheckButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCheckButtonActionPerformed(evt);
            }
        });

        scoreCompetitionJCheckBox.setText("Competition");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Balony");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(2000, 2000));

        tabPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPaneStateChanged(evt);
            }
        });

        scanPlateNamingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Plate naming"));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Save?");

        scanBTextField.setForeground(new java.awt.Color(0, 0, 204));
        scanBTextField.setToolTipText("File name for plate in position B");

        scanCTextField.setForeground(new java.awt.Color(0, 0, 204));
        scanCTextField.setToolTipText("File name for plate in position C");

        scanDTextField.setForeground(new java.awt.Color(0, 0, 204));
        scanDTextField.setToolTipText("File name for plate in position D");

        scanbaseTextField.setForeground(new java.awt.Color(255, 51, 51));
        scanbaseTextField.setToolTipText("File names of individual plates are based on this.");
        scanbaseTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanbaseTextFieldActionPerformed(evt);
            }
        });
        scanbaseTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scanbaseTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                scanbaseTextFieldKeyReleased(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("A:");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("C:");

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("D:");

        scanaTextField.setForeground(new java.awt.Color(0, 0, 204));
        scanaTextField.setToolTipText("File name for plate in position A");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("B:");

        scansaveBCheckBox.setSelected(true);
        scansaveBCheckBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scansaveBCheckBoxMouseClicked(evt);
            }
        });
        scansaveBCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scansaveBCheckBoxActionPerformed(evt);
            }
        });

        scansaveACheckBox.setSelected(true);
        scansaveACheckBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scansaveACheckBoxMouseClicked(evt);
            }
        });
        scansaveACheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scansaveACheckBoxActionPerformed(evt);
            }
        });

        scansaveCCheckBox.setSelected(true);
        scansaveCCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scansaveCCheckBoxActionPerformed(evt);
            }
        });

        scansaveDCheckBox.setSelected(true);
        scansaveDCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scansaveDCheckBoxActionPerformed(evt);
            }
        });

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Base Name:");

        org.jdesktop.layout.GroupLayout scanPlateNamingPanelLayout = new org.jdesktop.layout.GroupLayout(scanPlateNamingPanel);
        scanPlateNamingPanel.setLayout(scanPlateNamingPanelLayout);
        scanPlateNamingPanelLayout.setHorizontalGroup(
            scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanPlateNamingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(scanPlateNamingPanelLayout.createSequentialGroup()
                        .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel11)
                            .add(jLabel19)
                            .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel15, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(scanDTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                            .add(scanCTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                            .add(scanBTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                            .add(scanaTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(scansaveDCheckBox)
                            .add(scansaveCCheckBox)
                            .add(scansaveBCheckBox)
                            .add(scansaveACheckBox)))
                    .add(scanPlateNamingPanelLayout.createSequentialGroup()
                        .add(scanbaseTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jLabel9))
                    .add(jLabel26))
                .addContainerGap())
        );
        scanPlateNamingPanelLayout.setVerticalGroup(
            scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanPlateNamingPanelLayout.createSequentialGroup()
                .add(5, 5, 5)
                .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(scanPlateNamingPanelLayout.createSequentialGroup()
                        .add(jLabel26)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(scanbaseTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel9))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(scansaveACheckBox)
                    .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(scanaTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel11)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, scansaveBCheckBox)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(scanBTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel19)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, scansaveCCheckBox)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(scanCTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel14)))
                .add(10, 10, 10)
                .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(scanPlateNamingPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(scanDTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel15))
                    .add(scansaveDCheckBox))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scanPlateNumbersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Set plate numbers"));

        scanDPanel.setBackground(new java.awt.Color(0, 0, 0));
        scanDPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel18.setForeground(new java.awt.Color(255, 255, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("D:");

        scanDComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));
        scanDComboBox.setSelectedIndex(2);
        scanDComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanDComboBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout scanDPanelLayout = new org.jdesktop.layout.GroupLayout(scanDPanel);
        scanDPanel.setLayout(scanDPanelLayout);
        scanDPanelLayout.setHorizontalGroup(
            scanDPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanDPanelLayout.createSequentialGroup()
                .add(13, 13, 13)
                .add(jLabel18))
            .add(scanDComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        scanDPanelLayout.setVerticalGroup(
            scanDPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanDPanelLayout.createSequentialGroup()
                .add(3, 3, 3)
                .add(jLabel18)
                .add(3, 3, 3)
                .add(scanDComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        scanCPanel.setBackground(new java.awt.Color(0, 0, 0));
        scanCPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel17.setForeground(new java.awt.Color(255, 255, 51));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("C:");

        scanCComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));
        scanCComboBox.setSelectedIndex(3);
        scanCComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanCComboBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout scanCPanelLayout = new org.jdesktop.layout.GroupLayout(scanCPanel);
        scanCPanel.setLayout(scanCPanelLayout);
        scanCPanelLayout.setHorizontalGroup(
            scanCPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanCPanelLayout.createSequentialGroup()
                .add(13, 13, 13)
                .add(jLabel17))
            .add(scanCComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        scanCPanelLayout.setVerticalGroup(
            scanCPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanCPanelLayout.createSequentialGroup()
                .add(3, 3, 3)
                .add(jLabel17)
                .add(3, 3, 3)
                .add(scanCComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        scanAPanel.setBackground(new java.awt.Color(0, 0, 0));
        scanAPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setForeground(new java.awt.Color(255, 255, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("A:");

        scanAComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));
        scanAComboBox.setSelectedIndex(1);
        scanAComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanAComboBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout scanAPanelLayout = new org.jdesktop.layout.GroupLayout(scanAPanel);
        scanAPanel.setLayout(scanAPanelLayout);
        scanAPanelLayout.setHorizontalGroup(
            scanAPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanAPanelLayout.createSequentialGroup()
                .add(13, 13, 13)
                .add(jLabel10))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, scanAComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        scanAPanelLayout.setVerticalGroup(
            scanAPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanAPanelLayout.createSequentialGroup()
                .add(3, 3, 3)
                .add(jLabel10)
                .add(3, 3, 3)
                .add(scanAComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        scanBPanel.setBackground(new java.awt.Color(0, 0, 0));
        scanBPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel16.setForeground(new java.awt.Color(255, 255, 0));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("B:");

        scanBComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));
        scanBComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanBComboBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout scanBPanelLayout = new org.jdesktop.layout.GroupLayout(scanBPanel);
        scanBPanel.setLayout(scanBPanelLayout);
        scanBPanelLayout.setHorizontalGroup(
            scanBPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanBPanelLayout.createSequentialGroup()
                .add(13, 13, 13)
                .add(jLabel16))
            .add(scanBComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        scanBPanelLayout.setVerticalGroup(
            scanBPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanBPanelLayout.createSequentialGroup()
                .add(3, 3, 3)
                .add(jLabel16)
                .add(3, 3, 3)
                .add(scanBComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        scanSetLabel.setText("Set:");

        scansetComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40" }));
        scansetComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scansetComboBoxActionPerformed(evt);
            }
        });

        scanLayoutJLabel.setText("Layout:");

        scanLayout.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1x1", "1x2", "2x1", "2x2" }));
        scanLayout.setSelectedIndex(3);
        scanLayout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanLayoutActionPerformed(evt);
            }
        });

        scanOffsetJComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50" }));
        scanOffsetJComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanOffsetJComboBoxActionPerformed(evt);
            }
        });

        scanOffsetJLabel.setText("Offset:");

        org.jdesktop.layout.GroupLayout scanPlateNumbersPanelLayout = new org.jdesktop.layout.GroupLayout(scanPlateNumbersPanel);
        scanPlateNumbersPanel.setLayout(scanPlateNumbersPanelLayout);
        scanPlateNumbersPanelLayout.setHorizontalGroup(
            scanPlateNumbersPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanPlateNumbersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(scanPlateNumbersPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(scanAPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(scanCPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(scanPlateNumbersPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(scanPlateNumbersPanelLayout.createSequentialGroup()
                        .add(scanBPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(scanPlateNumbersPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(scanLayoutJLabel)
                            .add(scanPlateNumbersPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(scanOffsetJLabel)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, scanPlateNumbersPanelLayout.createSequentialGroup()
                                    .add(14, 14, 14)
                                    .add(scanSetLabel)))))
                    .add(scanDPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scanPlateNumbersPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(scanOffsetJComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(scansetComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(scanLayout, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        scanPlateNumbersPanelLayout.setVerticalGroup(
            scanPlateNumbersPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanPlateNumbersPanelLayout.createSequentialGroup()
                .add(scanPlateNumbersPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, scanPlateNumbersPanelLayout.createSequentialGroup()
                        .add(scanAPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(scanCPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(scanPlateNumbersPanelLayout.createSequentialGroup()
                        .add(scanBPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(8, 8, 8)
                        .add(scanDPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(scanPlateNumbersPanelLayout.createSequentialGroup()
                        .add(scanPlateNumbersPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(scanLayoutJLabel)
                            .add(scanLayout, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(scanPlateNumbersPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(scansetComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(scanSetLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(scanPlateNumbersPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(scanOffsetJComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(scanOffsetJLabel))))
                .addContainerGap())
        );

        scanOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Image processing options"));

        scanrotateComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "90° CW", "90° CCW", "180°", "None" }));
        scanrotateComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanrotateComboBoxActionPerformed(evt);
            }
        });

        jLabel21.setText("Rotate plates:");

        scanshrinkComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "50%", "25%" }));
        scanshrinkComboBox.setSelectedItem(scanshrinkComboBox);
        scanshrinkComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanshrinkComboBoxActionPerformed(evt);
            }
        });

        jLabel22.setText("Shrink plates:");

        scangrayCheckBox.setSelected(true);
        scangrayCheckBox.setText("Convert to grayscale");
        scangrayCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scangrayCheckBoxActionPerformed(evt);
            }
        });

        jLabel23.setText("Subfolder:");

        scansubfolderTextField.setText("Cropped");
        scansubfolderTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                scansubfolderTextFieldKeyReleased(evt);
            }
        });

        scancloseCheckBox.setSelected(true);
        scancloseCheckBox.setText("Close cropped images");
        scancloseCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scancloseCheckBoxActionPerformed(evt);
            }
        });

        scanAutoPlateNameJCheckBox.setSelected(true);
        scanAutoPlateNameJCheckBox.setText("Auto-name plates");
        scanAutoPlateNameJCheckBox.setToolTipText("");
        scanAutoPlateNameJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanAutoPlateNameJCheckBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout scanOptionsPanelLayout = new org.jdesktop.layout.GroupLayout(scanOptionsPanel);
        scanOptionsPanel.setLayout(scanOptionsPanelLayout);
        scanOptionsPanelLayout.setHorizontalGroup(
            scanOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanOptionsPanelLayout.createSequentialGroup()
                .add(scanOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(scanAutoPlateNameJCheckBox)
                    .add(scancloseCheckBox)
                    .add(scanOptionsPanelLayout.createSequentialGroup()
                        .add(jLabel21)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(scanrotateComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(scanOptionsPanelLayout.createSequentialGroup()
                        .add(4, 4, 4)
                        .add(jLabel22)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(scanshrinkComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(scangrayCheckBox)
                    .add(scanOptionsPanelLayout.createSequentialGroup()
                        .add(jLabel23)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scansubfolderTextField)))
                .addContainerGap())
        );
        scanOptionsPanelLayout.setVerticalGroup(
            scanOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanOptionsPanelLayout.createSequentialGroup()
                .add(scanOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel21)
                    .add(scanrotateComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(scanOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel22)
                    .add(scanshrinkComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(scangrayCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scancloseCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scanAutoPlateNameJCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 8, Short.MAX_VALUE)
                .add(scanOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel23)
                    .add(scansubfolderTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        scanchoosefolderButton.setText("Choose Folder...");
        scanchoosefolderButton.setToolTipText("Click to select the folder containing your scanned images.");
        scanchoosefolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanchoosefolderButtonActionPerformed(evt);
            }
        });

        scanprocessallButton.setText("Process Selected");
        scanprocessallButton.setToolTipText("Process Selected");
        scanprocessallButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanprocessallButtonActionPerformed(evt);
            }
        });

        scanFolderJTextField.setEditable(false);

        scanFileJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scanFileJListMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                scanFileJListMouseReleased(evt);
            }
        });
        scanFileJList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                scanFileJListKeyReleased(evt);
            }
        });
        scanFileJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                scanFileJListValueChanged(evt);
            }
        });
        scanFileJScrollPane.setViewportView(scanFileJList);

        scanFilenameHelpJButton.setText("Filename Help");
        scanFilenameHelpJButton.setToolTipText("Filename Help");
        scanFilenameHelpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanFilenameHelpJButtonActionPerformed(evt);
            }
        });

        scanSelectAllButton.setText("Select All");
        scanSelectAllButton.setToolTipText("Select All");
        scanSelectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanSelectAllButtonActionPerformed(evt);
            }
        });

        scanPreviewPanel.setBackground(new java.awt.Color(0, 0, 0));

        org.jdesktop.layout.GroupLayout scanPreviewPanelLayout = new org.jdesktop.layout.GroupLayout(scanPreviewPanel);
        scanPreviewPanel.setLayout(scanPreviewPanelLayout);
        scanPreviewPanelLayout.setHorizontalGroup(
            scanPreviewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        scanPreviewPanelLayout.setVerticalGroup(
            scanPreviewPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        scanPreviewJCheckBox.setSelected(true);
        scanPreviewJCheckBox.setText("Preview");
        scanPreviewJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanPreviewJCheckBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout scanPanelLayout = new org.jdesktop.layout.GroupLayout(scanPanel);
        scanPanel.setLayout(scanPanelLayout);
        scanPanelLayout.setHorizontalGroup(
            scanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(scanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(scanPanelLayout.createSequentialGroup()
                        .add(scanchoosefolderButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scanSelectAllButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scanprocessallButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scanFilenameHelpJButton))
                    .add(scanPanelLayout.createSequentialGroup()
                        .add(scanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(scanPlateNamingPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(scanPlateNumbersPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(scanOptionsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(scanPreviewPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(scanPreviewJCheckBox)))
                    .add(scanFolderJTextField)
                    .add(scanFileJScrollPane))
                .addContainerGap())
        );
        scanPanelLayout.setVerticalGroup(
            scanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(scanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(scanchoosefolderButton)
                    .add(scanprocessallButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(scanSelectAllButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(scanFilenameHelpJButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scanFolderJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scanFileJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(scanPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(scanPanelLayout.createSequentialGroup()
                        .add(scanPlateNumbersPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scanPlateNamingPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(scanPanelLayout.createSequentialGroup()
                        .add(scanOptionsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scanPreviewJCheckBox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scanPreviewPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabPane.addTab("Scan", scanPanel);

        imagePanel.setPreferredSize(new java.awt.Dimension(0, 0));
        imagePanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                imagePanelKeyPressed(evt);
            }
        });

        manualspotPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Manual spot"));

        manualspotdefineButton.setText("Add");
        manualspotdefineButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualspotdefineButtonActionPerformed(evt);
            }
        });

        manualspotResetButton.setText("Remove");
        manualspotResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualspotResetButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout manualspotPanelLayout = new org.jdesktop.layout.GroupLayout(manualspotPanel);
        manualspotPanel.setLayout(manualspotPanelLayout);
        manualspotPanelLayout.setHorizontalGroup(
            manualspotPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(manualspotPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(manualspotPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(manualspotdefineButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(manualspotResetButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        manualspotPanelLayout.setVerticalGroup(
            manualspotPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(manualspotPanelLayout.createSequentialGroup()
                .add(manualspotdefineButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(manualspotResetButton)
                .addContainerGap())
        );

        gridPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Manual Gridding", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 102, 0))); // NOI18N
        gridPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                gridPanelKeyPressed(evt);
            }
        });

        definegridButton.setText("Define");
        definegridButton.setToolTipText("Click to manually define the array grid.");
        definegridButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                definegridButtonActionPerformed(evt);
            }
        });

        gridclearButton.setText("Clear");
        gridclearButton.setToolTipText("Click to clear the current array grid.");
        gridclearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridclearButtonActionPerformed(evt);
            }
        });

        pointgridCheckBox.setText("Define from single corner colony");
        pointgridCheckBox.setToolTipText("When selected, manual gridding is determined by selecting a single corner of the array.");

        gridCentreButton.setText("Place");
        gridCentreButton.setToolTipText("Centre manual grid on the image.");
        gridCentreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridCentreButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout gridPanelLayout = new org.jdesktop.layout.GroupLayout(gridPanel);
        gridPanel.setLayout(gridPanelLayout);
        gridPanelLayout.setHorizontalGroup(
            gridPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gridPanelLayout.createSequentialGroup()
                .add(gridPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(gridPanelLayout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(pointgridCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, gridPanelLayout.createSequentialGroup()
                        .add(definegridButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gridclearButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gridCentreButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gridPanelLayout.setVerticalGroup(
            gridPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(gridPanelLayout.createSequentialGroup()
                .add(gridPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(definegridButton)
                    .add(gridclearButton)
                    .add(gridCentreButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pointgridCheckBox)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        autoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Auto-process"));

        autoQuantJCheckBox.setSelected(true);
        autoQuantJCheckBox.setText("Quant");
        autoQuantJCheckBox.setToolTipText("Automaticaly quantify image after gridding");
        autoQuantJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoQuantJCheckBoxActionPerformed(evt);
            }
        });

        autoGridJCheckBox.setSelected(true);
        autoGridJCheckBox.setText("Grid");
        autoGridJCheckBox.setToolTipText("Automatically grid image after thresholding");
        autoGridJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoGridJCheckBoxActionPerformed(evt);
            }
        });

        autoNameJCheckBox.setSelected(true);
        autoNameJCheckBox.setText("Name");
        autoNameJCheckBox.setToolTipText("Automatically name data file from input file");
        autoNameJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoNameJCheckBoxActionPerformed(evt);
            }
        });

        autoInvertJCheckBox.setSelected(true);
        autoInvertJCheckBox.setText("Invert");
        autoInvertJCheckBox.setToolTipText("Automatically invert image upon opening");
        autoInvertJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoInvertJCheckBoxActionPerformed(evt);
            }
        });

        autoThreshJCheckBox.setSelected(true);
        autoThreshJCheckBox.setText("Threshold");
        autoThreshJCheckBox.setToolTipText("Automatically threshold image upon opening");
        autoThreshJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoThreshJCheckBoxActionPerformed(evt);
            }
        });

        autoSaveJCheckBox.setSelected(true);
        autoSaveJCheckBox.setText("Save");
        autoSaveJCheckBox.setToolTipText("Automatically save data file upon quantification");
        autoSaveJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoSaveJCheckBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout autoPanelLayout = new org.jdesktop.layout.GroupLayout(autoPanel);
        autoPanel.setLayout(autoPanelLayout);
        autoPanelLayout.setHorizontalGroup(
            autoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(autoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(autoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(autoThreshJCheckBox)
                    .add(autoNameJCheckBox))
                .add(2, 2, 2)
                .add(autoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(autoGridJCheckBox)
                    .add(autoQuantJCheckBox))
                .add(18, 18, 18)
                .add(autoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(autoInvertJCheckBox)
                    .add(autoSaveJCheckBox))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        autoPanelLayout.setVerticalGroup(
            autoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(autoPanelLayout.createSequentialGroup()
                .add(autoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(autoPanelLayout.createSequentialGroup()
                        .add(autoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(autoNameJCheckBox)
                            .add(autoGridJCheckBox))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(autoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(autoQuantJCheckBox)
                            .add(autoThreshJCheckBox)))
                    .add(autoPanelLayout.createSequentialGroup()
                        .add(autoInvertJCheckBox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(autoSaveJCheckBox)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        platePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Plate ID"));

        platenameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        platenameLabel.setText("Name:");

        setnumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        setnumberLabel.setText("Set #");

        platenumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        platenumberLabel.setText("Plate #");

        org.jdesktop.layout.GroupLayout platePanelLayout = new org.jdesktop.layout.GroupLayout(platePanel);
        platePanel.setLayout(platePanelLayout);
        platePanelLayout.setHorizontalGroup(
            platePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(platePanelLayout.createSequentialGroup()
                .add(platePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(platePanelLayout.createSequentialGroup()
                        .add(setnumberLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(setNumberJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 35, Short.MAX_VALUE)
                        .add(platenumberLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(plateNumberJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(platePanelLayout.createSequentialGroup()
                        .add(platenameLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(plateNameJTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)))
                .addContainerGap())
        );
        platePanelLayout.setVerticalGroup(
            platePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(platePanelLayout.createSequentialGroup()
                .add(platePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(plateNameJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(platenameLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(platePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(setnumberLabel)
                    .add(platenumberLabel)
                    .add(plateNumberJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(setNumberJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        threshPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Threshold Method", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        buttonGroup1.add(threshRadioAuto);
        threshRadioAuto.setFont(threshRadioAuto.getFont());
        threshRadioAuto.setSelected(true);
        threshRadioAuto.setText("Auto");
        threshRadioAuto.setToolTipText("Auto");
        threshRadioAuto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                threshRadioAutoActionPerformed(evt);
            }
        });

        buttonGroup1.add(threshRadioManual);
        threshRadioManual.setText("Manual:");
        threshRadioManual.setToolTipText("Manual");
        threshRadioManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                threshRadioManualActionPerformed(evt);
            }
        });

        threshJSlider.setMaximum(255);
        threshJSlider.setMinorTickSpacing(16);
        threshJSlider.setValue(165);
        threshJSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                threshJSliderMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                threshJSliderMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                threshJSliderMouseReleased(evt);
            }
        });
        threshJSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                threshJSliderStateChanged(evt);
            }
        });

        imageThreshManualJTextField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                imageThreshManualJTextFieldPropertyChange(evt);
            }
        });
        imageThreshManualJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                imageThreshManualJTextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                imageThreshManualJTextFieldKeyTyped(evt);
            }
        });

        org.jdesktop.layout.GroupLayout threshPanelLayout = new org.jdesktop.layout.GroupLayout(threshPanel);
        threshPanel.setLayout(threshPanelLayout);
        threshPanelLayout.setHorizontalGroup(
            threshPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(threshPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(threshPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, threshJSlider, 0, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, threshPanelLayout.createSequentialGroup()
                        .add(threshRadioAuto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(threshRadioManual)
                        .add(6, 6, 6)
                        .add(imageThreshManualJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        threshPanelLayout.setVerticalGroup(
            threshPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(threshPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(threshPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(threshRadioAuto)
                    .add(threshRadioManual)
                    .add(imageThreshManualJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(threshJSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        zoomPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Zoom"));

        zoominButton.setText("+");
        zoominButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoominButtonActionPerformed(evt);
            }
        });

        zoomoutButton.setText("-");
        zoomoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomoutButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout zoomPanelLayout = new org.jdesktop.layout.GroupLayout(zoomPanel);
        zoomPanel.setLayout(zoomPanelLayout);
        zoomPanelLayout.setHorizontalGroup(
            zoomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(zoomPanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(zoomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, zoomoutButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, zoominButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        zoomPanelLayout.setVerticalGroup(
            zoomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(zoomPanelLayout.createSequentialGroup()
                .add(zoominButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(zoomoutButton)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        rotatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Plate Rotation"));

        rotatecorrectorButton.setText("Define");
        rotatecorrectorButton.setToolTipText("Click to define plate rotation");
        rotatecorrectorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotatecorrectorButtonActionPerformed(evt);
            }
        });

        resetrotateButton.setText("Reset");
        resetrotateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetrotateButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout rotatePanelLayout = new org.jdesktop.layout.GroupLayout(rotatePanel);
        rotatePanel.setLayout(rotatePanelLayout);
        rotatePanelLayout.setHorizontalGroup(
            rotatePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(rotatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(rotatePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, resetrotateButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, rotatecorrectorButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        rotatePanelLayout.setVerticalGroup(
            rotatePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(rotatePanelLayout.createSequentialGroup()
                .add(rotatecorrectorButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(resetrotateButton)
                .addContainerGap())
        );

        autocheckPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Review Analysis"));

        viewLogButton.setText("Log");
        viewLogButton.setToolTipText("View the log file for full auto analysis.");
        viewLogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewLogButtonActionPerformed(evt);
            }
        });

        autocheckbackButton.setText("<");
        autocheckbackButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        autocheckbackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autocheckbackButtonActionPerformed(evt);
            }
        });

        autocheckforwardButton.setText(">");
        autocheckforwardButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        autocheckforwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autocheckforwardButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout autocheckPanelLayout = new org.jdesktop.layout.GroupLayout(autocheckPanel);
        autocheckPanel.setLayout(autocheckPanelLayout);
        autocheckPanelLayout.setHorizontalGroup(
            autocheckPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(autocheckPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(autocheckPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(autocheckPanelLayout.createSequentialGroup()
                        .add(autocheckbackButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(autocheckforwardButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(viewLogButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                .addContainerGap())
        );
        autocheckPanelLayout.setVerticalGroup(
            autocheckPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(autocheckPanelLayout.createSequentialGroup()
                .add(viewLogButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(autocheckPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(autocheckbackButton)
                    .add(autocheckforwardButton))
                .addContainerGap())
        );

        choosefolderButton.setText("Choose Folder...");
        choosefolderButton.setToolTipText("Click to select the folder containing your scanned images.");
        choosefolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choosefolderButtonActionPerformed(evt);
            }
        });

        thresholdButton.setForeground(new java.awt.Color(0, 0, 255));
        thresholdButton.setText("Threshold");
        thresholdButton.setToolTipText("Click to apply thresholding (background removal).");
        thresholdButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thresholdButtonActionPerformed(evt);
            }
        });

        quantButton.setForeground(new java.awt.Color(255, 102, 0));
        quantButton.setText("Quantify");
        quantButton.setToolTipText("Click to quantify spot sizes.");
        quantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.setToolTipText("Click to save quantified data.");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        fullautoButton.setText("Analyze Selected");
        fullautoButton.setToolTipText("Click to automatically analyse and save selected images.");
        fullautoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullautoButtonActionPerformed(evt);
            }
        });

        advancedPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Advanced", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 102, 0))); // NOI18N

        imageShowParamsButton.setText("Settings...");
        imageShowParamsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageShowParamsButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout advancedPanelLayout = new org.jdesktop.layout.GroupLayout(advancedPanel);
        advancedPanel.setLayout(advancedPanelLayout);
        advancedPanelLayout.setHorizontalGroup(
            advancedPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(advancedPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(imageShowParamsButton)
                .addContainerGap())
        );
        advancedPanelLayout.setVerticalGroup(
            advancedPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(advancedPanelLayout.createSequentialGroup()
                .add(imageShowParamsButton)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        imageFileJList.setDragEnabled(true);
        imageFileJList.setDropMode(javax.swing.DropMode.ON);
        imageFileJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageFileJListMouseClicked(evt);
            }
        });
        imageFileJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                imageFileJListValueChanged(evt);
            }
        });
        imageFileJListScrollPane.setViewportView(imageFileJList);

        folderJTextField.setEditable(false);

        autoGridButton.setForeground(new java.awt.Color(0, 102, 0));
        autoGridButton.setText("AutoGrid");
        autoGridButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoGridButtonActionPerformed(evt);
            }
        });

        presetPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Grid Presets"));

        gridChoicejComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                gridChoicejComboBoxItemStateChanged(evt);
            }
        });
        gridChoicejComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridChoicejComboBoxActionPerformed(evt);
            }
        });

        imageShowPresetsButton.setText("Edit...");
        imageShowPresetsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageShowPresetsButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout presetPanelLayout = new org.jdesktop.layout.GroupLayout(presetPanel);
        presetPanel.setLayout(presetPanelLayout);
        presetPanelLayout.setHorizontalGroup(
            presetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, presetPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(gridChoicejComboBox, 0, 208, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(imageShowPresetsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        presetPanelLayout.setVerticalGroup(
            presetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(presetPanelLayout.createSequentialGroup()
                .add(presetPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(gridChoicejComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(imageShowPresetsButton))
                .addContainerGap())
        );

        imageStopButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        imageStopButton.setForeground(new java.awt.Color(255, 0, 0));
        imageStopButton.setText("STOP");
        imageStopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageStopButtonActionPerformed(evt);
            }
        });

        toggleInputOutputButton.setText("<html><center>Toggle<br> Input /<br> Output</center><html>");
        toggleInputOutputButton.setActionCommand("<html><center>Toggle<br>\nInput<br>\nOutput</center><html>");
        toggleInputOutputButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleInputOutputButtonActionPerformed(evt);
            }
        });

        selectAllButton.setText("Select All");
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllButtonActionPerformed(evt);
            }
        });

        showQuantButton.setText("Show Quant");
        showQuantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showQuantButtonActionPerformed(evt);
            }
        });

        imageFolderJLabel.setText("Image Folder:");

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout imagePanelLayout = new org.jdesktop.layout.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(imagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(imagePanelLayout.createSequentialGroup()
                        .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(fullautoButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(choosefolderButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(quantButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(thresholdButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(saveButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(autoGridButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(imageStopButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(toggleInputOutputButton))
                    .add(imagePanelLayout.createSequentialGroup()
                        .add(imageFolderJLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(folderJTextField))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, imagePanelLayout.createSequentialGroup()
                        .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, showQuantButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, removeButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, selectAllButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(imageFileJListScrollPane))
                    .add(imagePanelLayout.createSequentialGroup()
                        .add(threshPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(gridPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(imagePanelLayout.createSequentialGroup()
                        .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(imagePanelLayout.createSequentialGroup()
                                .add(autoPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(platePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(imagePanelLayout.createSequentialGroup()
                                .add(presetPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(advancedPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(imagePanelLayout.createSequentialGroup()
                                .add(manualspotPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(rotatePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(autocheckPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(zoomPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(imagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(imagePanelLayout.createSequentialGroup()
                        .add(choosefolderButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(fullautoButton))
                    .add(imagePanelLayout.createSequentialGroup()
                        .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(thresholdButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(autoGridButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(quantButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(saveButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(toggleInputOutputButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(imageStopButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(folderJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(imageFolderJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(imagePanelLayout.createSequentialGroup()
                        .add(selectAllButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(showQuantButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(removeButton))
                    .add(imageFileJListScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(presetPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(advancedPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(autoPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(platePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(gridPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(threshPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(zoomPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(autocheckPanel, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(rotatePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(manualspotPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tabPane.addTab("Image", null, imagePanel, "Quantify Images");

        scoringtabaPanel.setMaximumSize(new java.awt.Dimension(0, 0));
        scoringtabaPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        scoringtabaPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                scoringtabaPanelComponentShown(evt);
            }
        });

        normalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Spot Correction"));

        scoreRCJCheckBox.setSelected(true);
        scoreRCJCheckBox.setText("Row / Column");
        scoreRCJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreRCJCheckBoxActionPerformed(evt);
            }
        });

        scoreSpatialJCheckBox.setText("LOESS smoothing");
        scoreSpatialJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreSpatialJCheckBoxActionPerformed(evt);
            }
        });

        scoreUndergrowthJCheckBox.setText("Also adjust for under-growth");

        scoreNormPercentileLabel.setText("Normalization percentile:");

        scoreNormPercentileTextField.setText("50");

        org.jdesktop.layout.GroupLayout normalPanelLayout = new org.jdesktop.layout.GroupLayout(normalPanel);
        normalPanel.setLayout(normalPanelLayout);
        normalPanelLayout.setHorizontalGroup(
            normalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(normalPanelLayout.createSequentialGroup()
                .add(normalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(normalPanelLayout.createSequentialGroup()
                        .add(normalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(normalPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(scoreRCJCheckBox))
                            .add(normalPanelLayout.createSequentialGroup()
                                .add(15, 15, 15)
                                .add(scoreNormPercentileLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(scoreNormPercentileTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(0, 9, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, normalPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(normalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(scoreSpatialJCheckBox)
                            .add(scoreUndergrowthJCheckBox))))
                .addContainerGap())
        );
        normalPanelLayout.setVerticalGroup(
            normalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(normalPanelLayout.createSequentialGroup()
                .add(scoreRCJCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(normalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(scoreNormPercentileLabel)
                    .add(scoreNormPercentileTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scoreUndergrowthJCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scoreSpatialJCheckBox)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        ctrlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Control Plates"));
        ctrlPanel.setPreferredSize(new java.awt.Dimension(210, 99));

        ctrldirTextField.setEditable(false);

        ctrldirButton.setText("...");
        ctrldirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrldirButtonActionPerformed(evt);
            }
        });

        ctrlplateComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctrlplateComboBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout ctrlPanelLayout = new org.jdesktop.layout.GroupLayout(ctrlPanel);
        ctrlPanel.setLayout(ctrlPanelLayout);
        ctrlPanelLayout.setHorizontalGroup(
            ctrlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(ctrlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(ctrlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(ctrlplateComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(ctrlPanelLayout.createSequentialGroup()
                        .add(ctrldirTextField)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(ctrldirButton)))
                .addContainerGap())
        );
        ctrlPanelLayout.setVerticalGroup(
            ctrlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, ctrlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(ctrlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(ctrldirTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(ctrldirButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(ctrlplateComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        expPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Experimental Plates"));
        expPanel.setPreferredSize(new java.awt.Dimension(210, 99));

        expButton.setText("...");
        expButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expButtonActionPerformed(evt);
            }
        });

        expdirTextField.setEditable(false);

        expplateComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expplateComboBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout expPanelLayout = new org.jdesktop.layout.GroupLayout(expPanel);
        expPanel.setLayout(expPanelLayout);
        expPanelLayout.setHorizontalGroup(
            expPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(expPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(expPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(expplateComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(expPanelLayout.createSequentialGroup()
                        .add(expdirTextField)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(expButton)))
                .addContainerGap())
        );
        expPanelLayout.setVerticalGroup(
            expPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(expPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(expPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(expdirTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, expPanelLayout.createSequentialGroup()
                        .add(expButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)))
                .add(expplateComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        savescorePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Save"));

        saveScoreButton.setText("Save as separte files");
        saveScoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveScoreButtonActionPerformed(evt);
            }
        });

        saveScoreAvgButton.setText("Save as averaged file");
        saveScoreAvgButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveScoreAvgButtonActionPerformed(evt);
            }
        });

        scoreSaveButtonGroup.add(scoreByArrayPosRadioButton);
        scoreByArrayPosRadioButton.setSelected(true);
        scoreByArrayPosRadioButton.setText("Score by Array Position");
        scoreByArrayPosRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreByArrayPosRadioButtonActionPerformed(evt);
            }
        });

        scoreSaveButtonGroup.add(scoreByOrfRadioButton);
        scoreByOrfRadioButton.setText("Score by ORF");
        scoreByOrfRadioButton.setEnabled(false);
        scoreByOrfRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreByOrfRadioButtonActionPerformed(evt);
            }
        });

        autoAnalyzeJCheckBox.setSelected(true);
        autoAnalyzeJCheckBox.setText("Open in analysis mode after saving");
        autoAnalyzeJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoAnalyzeJCheckBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout savescorePanelLayout = new org.jdesktop.layout.GroupLayout(savescorePanel);
        savescorePanel.setLayout(savescorePanelLayout);
        savescorePanelLayout.setHorizontalGroup(
            savescorePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(savescorePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(savescorePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(autoAnalyzeJCheckBox)
                    .add(scoreByOrfRadioButton)
                    .add(scoreByArrayPosRadioButton)
                    .add(scorenameTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .add(savescorePanelLayout.createSequentialGroup()
                        .add(saveScoreButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(saveScoreAvgButton)))
                .addContainerGap())
        );
        savescorePanelLayout.setVerticalGroup(
            savescorePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(savescorePanelLayout.createSequentialGroup()
                .add(scoreByArrayPosRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scoreByOrfRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scorenameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(savescorePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(saveScoreButton)
                    .add(saveScoreAvgButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(autoAnalyzeJCheckBox))
        );

        scoreKeyfilePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Key File"));

        scoreArraykeyLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        scoreArraykeyLabel.setText("Array Key:");

        scoreKeysComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoreKeysComboBoxActionPerformed(evt);
            }
        });

        keyFileLoadButton.setText("Load...");
        keyFileLoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keyFileLoadButtonActionPerformed(evt);
            }
        });

        jLabel6.setText("Query Key:");

        jButton3.setText("Load...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        useQueryKeyJCheckBox.setText("Use Query Key");

        org.jdesktop.layout.GroupLayout scoreKeyfilePanelLayout = new org.jdesktop.layout.GroupLayout(scoreKeyfilePanel);
        scoreKeyfilePanel.setLayout(scoreKeyfilePanelLayout);
        scoreKeyfilePanelLayout.setHorizontalGroup(
            scoreKeyfilePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scoreKeyfilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(scoreKeyfilePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(scoreKeysComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(scoreKeyfilePanelLayout.createSequentialGroup()
                        .add(scoreKeyfilePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(scoreKeyfilePanelLayout.createSequentialGroup()
                                .add(scoreArraykeyLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(keyFileLoadButton))
                            .add(scoreKeyfilePanelLayout.createSequentialGroup()
                                .add(jLabel6)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton3))
                            .add(useQueryKeyJCheckBox))
                        .add(0, 44, Short.MAX_VALUE)))
                .addContainerGap())
        );
        scoreKeyfilePanelLayout.setVerticalGroup(
            scoreKeyfilePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scoreKeyfilePanelLayout.createSequentialGroup()
                .add(scoreKeyfilePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(scoreArraykeyLabel)
                    .add(keyFileLoadButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scoreKeysComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(useQueryKeyJCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scoreKeyfilePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(jButton3)))
        );

        scoringRefreshButton.setText("Refresh Data");
        scoringRefreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoringRefreshButtonActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Control Type"));

        buttonGroup3.add(plateCtrlRadioButton);
        plateCtrlRadioButton.setSelected(true);
        plateCtrlRadioButton.setText("Control Plates");
        plateCtrlRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plateCtrlRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup3.add(altColRadioButton);
        altColRadioButton.setText("Alternate columns");
        altColRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                altColRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup3.add(altRowRadioButton);
        altRowRadioButton.setText("Alternate rows");
        altRowRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                altRowRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup4.add(altOddRadioButton);
        altOddRadioButton.setSelected(true);
        altOddRadioButton.setText("Odd");
        altOddRadioButton.setEnabled(false);
        altOddRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                altOddRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup4.add(altEvenRadioButton);
        altEvenRadioButton.setText("Even");
        altEvenRadioButton.setEnabled(false);
        altEvenRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                altEvenRadioButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(altColRadioButton)
                    .add(plateCtrlRadioButton)
                    .add(altRowRadioButton))
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(altEvenRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(17, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(altOddRadioButton)
                        .add(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(plateCtrlRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(altColRadioButton)
                    .add(altOddRadioButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(altRowRadioButton)
                    .add(altEvenRadioButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Normalization"));

        normButtonGroup.add(normPlateMedianButton);
        normPlateMedianButton.setSelected(true);
        normPlateMedianButton.setText("Plate Median");
        normPlateMedianButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normPlateMedianButtonActionPerformed(evt);
            }
        });

        normButtonGroup.add(normORFButton);
        normORFButton.setText("ORF/Gene");
        normORFButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normORFButtonActionPerformed(evt);
            }
        });

        normORFJTextField.setEnabled(false);

        normButtonGroup.add(normNoneButton);
        normNoneButton.setText("None");
        normNoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normNoneButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(normPlateMedianButton)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(normORFButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(normORFJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(normNoneButton))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(normPlateMedianButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(normORFButton)
                    .add(normORFJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(normNoneButton)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout scoringtabaPanelLayout = new org.jdesktop.layout.GroupLayout(scoringtabaPanel);
        scoringtabaPanel.setLayout(scoringtabaPanelLayout);
        scoringtabaPanelLayout.setHorizontalGroup(
            scoringtabaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scoringtabaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(scoringtabaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(savescorePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(scoringtabaPanelLayout.createSequentialGroup()
                        .add(scoringtabaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(scoringtabaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, scoringRefreshButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(scoringtabaPanelLayout.createSequentialGroup()
                                    .add(scoringtabaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                        .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(ctrlPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(scoringtabaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(expPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .add(scoreKeyfilePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(normalPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        scoringtabaPanelLayout.setVerticalGroup(
            scoringtabaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scoringtabaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(scoringtabaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scoringtabaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(ctrlPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(expPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scoringRefreshButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scoringtabaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(normalPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(scoreKeyfilePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(savescorePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabPane.addTab("Scoring", scoringtabaPanel);

        analysistabPanel.setEnabled(false);
        analysistabPanel.setPreferredSize(new java.awt.Dimension(0, 0));

        analysisTablesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Tables"));

        analysisLoadButton.setText("Load Data");
        analysisLoadButton.setToolTipText("Select scored file(s) to analyze");
        analysisLoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisLoadButtonActionPerformed(evt);
            }
        });

        dataTablesComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataTablesComboBoxActionPerformed(evt);
            }
        });

        analysisOpenDataTablesJCheckBox.setSelected(true);
        analysisOpenDataTablesJCheckBox.setText("Open data tables after loading");
        analysisOpenDataTablesJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisOpenDataTablesJCheckBoxActionPerformed(evt);
            }
        });

        restoreTableButton.setText("Restore Table...");
        restoreTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restoreTableButtonActionPerformed(evt);
            }
        });

        wizardModeJCheckBox.setSelected(true);
        wizardModeJCheckBox.setText("Wizard Mode (sets options and parameters upon loading)");
        wizardModeJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wizardModeJCheckBoxActionPerformed(evt);
            }
        });

        analysisTableRemoveButton.setText("Remove");
        analysisTableRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisTableRemoveButtonActionPerformed(evt);
            }
        });

        analysisTableShowButton.setText("Show");
        analysisTableShowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisTableShowButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout analysisTablesPanelLayout = new org.jdesktop.layout.GroupLayout(analysisTablesPanel);
        analysisTablesPanel.setLayout(analysisTablesPanelLayout);
        analysisTablesPanelLayout.setHorizontalGroup(
            analysisTablesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisTablesPanelLayout.createSequentialGroup()
                .add(analysisTablesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, analysisTablesPanelLayout.createSequentialGroup()
                        .add(analysisLoadButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(analysisOpenDataTablesJCheckBox)
                        .add(18, 18, 18)
                        .add(restoreTableButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, analysisTablesPanelLayout.createSequentialGroup()
                        .add(analysisTablesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(analysisTablesPanelLayout.createSequentialGroup()
                                .add(wizardModeJCheckBox)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(analysisTablesPanelLayout.createSequentialGroup()
                                .add(dataTablesComboBox, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(18, 18, 18)))
                        .add(analysisTablesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(analysisTableShowButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(analysisTableRemoveButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        analysisTablesPanelLayout.setVerticalGroup(
            analysisTablesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisTablesPanelLayout.createSequentialGroup()
                .add(analysisTablesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(analysisLoadButton)
                    .add(analysisOpenDataTablesJCheckBox)
                    .add(restoreTableButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(analysisTablesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dataTablesComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(analysisTableShowButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(analysisTablesPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(wizardModeJCheckBox)
                    .add(analysisTableRemoveButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sgdFeaturesJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("SGD Features"));

        analysisOverrideKeyFileCheckBox.setSelected(true);
        analysisOverrideKeyFileCheckBox.setText("Override key-file gene names");
        analysisOverrideKeyFileCheckBox.setToolTipText("Use gene names from SGD rather than key file");
        analysisOverrideKeyFileCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisOverrideKeyFileCheckBoxActionPerformed(evt);
            }
        });

        downloadSGDInfoButton.setText("Download new SGD Info");
        downloadSGDInfoButton.setToolTipText("Update gene and ORF names and descriptions from SGD");
        downloadSGDInfoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadSGDInfoButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout sgdFeaturesJPanelLayout = new org.jdesktop.layout.GroupLayout(sgdFeaturesJPanel);
        sgdFeaturesJPanel.setLayout(sgdFeaturesJPanelLayout);
        sgdFeaturesJPanelLayout.setHorizontalGroup(
            sgdFeaturesJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(sgdFeaturesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(analysisOverrideKeyFileCheckBox)
                .add(18, 18, 18)
                .add(downloadSGDInfoButton)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sgdFeaturesJPanelLayout.setVerticalGroup(
            sgdFeaturesJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(sgdFeaturesJPanelLayout.createSequentialGroup()
                .add(sgdFeaturesJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(analysisOverrideKeyFileCheckBox)
                    .add(downloadSGDInfoButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        defaultTableSettingsJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Default Table Settings"));

        lowCutOffJTextField.setText("0.85");
        lowCutOffJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lowCutOffJTextFieldKeyReleased(evt);
            }
        });

        sickCutOffTextJField.setText("0.2");
        sickCutOffTextJField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sickCutOffTextJFieldKeyReleased(evt);
            }
        });

        jLabel13.setText("Discard data if size of");

        jLabel20.setText("Upper cut-off");

        jLabel25.setText("Min Spot Size");

        jLabel27.setText("Lower cut-off");

        upperCutOffJTextField.setText("1.17");
        upperCutOffJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                upperCutOffJTextFieldKeyReleased(evt);
            }
        });

        minSpotSizeJTextField.setText("0.05");
        minSpotSizeJTextField.setToolTipText("Minimum value to assign to all spots");
        minSpotSizeJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                minSpotSizeJTextFieldKeyReleased(evt);
            }
        });

        analysisSickFliterJComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "control spot", "experimental spot", "either spot" }));
        analysisSickFliterJComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                analysisSickFliterJComboBoxItemStateChanged(evt);
            }
        });
        analysisSickFliterJComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisSickFliterJComboBoxActionPerformed(evt);
            }
        });

        jLabel28.setText("is below");

        jLabel2.setText("Max Spot Size");

        maxSpotSizeJTextField.setText("100");
        maxSpotSizeJTextField.setToolTipText("Minimum value to assign to all spots");
        maxSpotSizeJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                maxSpotSizeJTextFieldKeyReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout defaultTableSettingsJPanelLayout = new org.jdesktop.layout.GroupLayout(defaultTableSettingsJPanel);
        defaultTableSettingsJPanel.setLayout(defaultTableSettingsJPanelLayout);
        defaultTableSettingsJPanelLayout.setHorizontalGroup(
            defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(defaultTableSettingsJPanelLayout.createSequentialGroup()
                .add(10, 10, 10)
                .add(defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(defaultTableSettingsJPanelLayout.createSequentialGroup()
                        .add(jLabel13)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(analysisSickFliterJComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel28)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(sickCutOffTextJField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(defaultTableSettingsJPanelLayout.createSequentialGroup()
                        .add(defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel2)
                            .add(jLabel25))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(minSpotSizeJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(maxSpotSizeJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(18, 18, 18)
                        .add(defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel27)
                            .add(jLabel20))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(upperCutOffJTextField)
                            .add(lowCutOffJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        defaultTableSettingsJPanelLayout.setVerticalGroup(
            defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(defaultTableSettingsJPanelLayout.createSequentialGroup()
                .add(defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(defaultTableSettingsJPanelLayout.createSequentialGroup()
                        .add(2, 2, 2)
                        .add(defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(defaultTableSettingsJPanelLayout.createSequentialGroup()
                                .add(3, 3, 3)
                                .add(jLabel25))
                            .add(minSpotSizeJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel2)
                            .add(maxSpotSizeJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(defaultTableSettingsJPanelLayout.createSequentialGroup()
                        .add(defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel27)
                            .add(lowCutOffJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(9, 9, 9)
                        .add(defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel20)
                            .add(upperCutOffJTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(defaultTableSettingsJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel13)
                    .add(sickCutOffTextJField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel28)
                    .add(analysisSickFliterJComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        analysisSummaryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Summary Tables"));

        analysisGenerateSummaryTablesHiddenButton.setText("Generate Summary Tables (hidden)");
        analysisGenerateSummaryTablesHiddenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisGenerateSummaryTablesHiddenButtonActionPerformed(evt);
            }
        });

        analysisGenerateSummaryTablesVisibleButton.setText("Generate Summary Tables (visible)");
        analysisGenerateSummaryTablesVisibleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisGenerateSummaryTablesVisibleButtonActionPerformed(evt);
            }
        });

        analysisShowSummaryTables.setText("Show Summary Tables");
        analysisShowSummaryTables.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisShowSummaryTablesActionPerformed(evt);
            }
        });

        analysisSummaryClipboardCopyTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ctrls", "Ctrl SDs", "Ctrl n", "Exps", "Exp SDs", "Exp n", "Ratios", "Ratio SDs", "Diffs", "Diff SDs", "p-values" }));
        analysisSummaryClipboardCopyTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisSummaryClipboardCopyTypeComboBoxActionPerformed(evt);
            }
        });

        analysisSummaryDefaultsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Defaults"));

        analysisSummaryMedianCheckBox.setSelected(true);
        analysisSummaryMedianCheckBox.setText("Use median values");
        analysisSummaryMedianCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisSummaryMedianCheckBoxActionPerformed(evt);
            }
        });

        analysisSummaryPairedSpotsCheckBox.setSelected(true);
        analysisSummaryPairedSpotsCheckBox.setText("Paired spots");

        org.jdesktop.layout.GroupLayout analysisSummaryDefaultsPanelLayout = new org.jdesktop.layout.GroupLayout(analysisSummaryDefaultsPanel);
        analysisSummaryDefaultsPanel.setLayout(analysisSummaryDefaultsPanelLayout);
        analysisSummaryDefaultsPanelLayout.setHorizontalGroup(
            analysisSummaryDefaultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisSummaryDefaultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(analysisSummaryDefaultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(analysisSummaryMedianCheckBox)
                    .add(analysisSummaryPairedSpotsCheckBox))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        analysisSummaryDefaultsPanelLayout.setVerticalGroup(
            analysisSummaryDefaultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisSummaryDefaultsPanelLayout.createSequentialGroup()
                .add(analysisSummaryMedianCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(analysisSummaryPairedSpotsCheckBox))
        );

        analysisSummaryCopyAllClipboardButton.setText("Copy to clipboard all:");
        analysisSummaryCopyAllClipboardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                analysisSummaryCopyAllClipboardButtonActionPerformed(evt);
            }
        });

        jButton4.setText("Consolidate (Excel)");

        org.jdesktop.layout.GroupLayout analysisSummaryPanelLayout = new org.jdesktop.layout.GroupLayout(analysisSummaryPanel);
        analysisSummaryPanel.setLayout(analysisSummaryPanelLayout);
        analysisSummaryPanelLayout.setHorizontalGroup(
            analysisSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisSummaryPanelLayout.createSequentialGroup()
                .add(analysisSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(analysisSummaryPanelLayout.createSequentialGroup()
                        .add(analysisSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(analysisShowSummaryTables, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(analysisSummaryCopyAllClipboardButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jButton4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(analysisSummaryClipboardCopyTypeComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(analysisSummaryDefaultsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(analysisSummaryPanelLayout.createSequentialGroup()
                        .add(analysisGenerateSummaryTablesHiddenButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(analysisGenerateSummaryTablesVisibleButton)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        analysisSummaryPanelLayout.setVerticalGroup(
            analysisSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysisSummaryPanelLayout.createSequentialGroup()
                .add(analysisSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(analysisGenerateSummaryTablesHiddenButton)
                    .add(analysisGenerateSummaryTablesVisibleButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(analysisSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(analysisSummaryPanelLayout.createSequentialGroup()
                        .add(analysisSummaryDefaultsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(analysisSummaryPanelLayout.createSequentialGroup()
                        .add(analysisShowSummaryTables)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(analysisSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(analysisSummaryClipboardCopyTypeComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(analysisSummaryCopyAllClipboardButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButton4))))
        );

        org.jdesktop.layout.GroupLayout analysistabPanelLayout = new org.jdesktop.layout.GroupLayout(analysistabPanel);
        analysistabPanel.setLayout(analysistabPanelLayout);
        analysistabPanelLayout.setHorizontalGroup(
            analysistabPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysistabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(analysistabPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, defaultTableSettingsJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, analysisTablesPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(sgdFeaturesJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(analysisSummaryPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        analysistabPanelLayout.setVerticalGroup(
            analysistabPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(analysistabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(analysisTablesPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(analysisSummaryPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(defaultTableSettingsJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sgdFeaturesJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(237, 237, 237))
        );

        tabPane.addTab("Analysis", analysistabPanel);

        optionsPanel.setPreferredSize(new java.awt.Dimension(0, 0));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("User Interface (Restart required upon change)"));

        buttonGroup2.add(javaLAFJRadioButton);
        javaLAFJRadioButton.setText("Stadard Java Look-and-Feel (alternative)");
        javaLAFJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javaLAFJRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup2.add(osLAFJRadioButton);
        osLAFJRadioButton.setSelected(true);
        osLAFJRadioButton.setText("Operating System Look-and-Feel (recommended for Windows, NOT for Mac OS X)");
        osLAFJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                osLAFJRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup2.add(nimbusLAFJRadioButton);
        nimbusLAFJRadioButton.setText("Nimbus Look-and-Feel (recommended for Mac OS X and Linux)");
        nimbusLAFJRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nimbusLAFJRadioButtonActionPerformed(evt);
            }
        });

        jButton1.setText("Show Message Window");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(osLAFJRadioButton)
                    .add(javaLAFJRadioButton)
                    .add(nimbusLAFJRadioButton)
                    .add(jButton1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(osLAFJRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(nimbusLAFJRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(javaLAFJRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jButton1)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        updaterJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Updater"));

        jLabel3.setText("Current Version:");

        currVersionJLabel.setText("Unknown");

        jLabel5.setText("Latest Version:");

        latestVersionJLabel.setText("Unknown");

        updateCheckJCheckBox.setSelected(true);
        updateCheckJCheckBox.setText("Automatically check for program updates on startup");
        updateCheckJCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCheckJCheckBoxActionPerformed(evt);
            }
        });

        jButton2.setText("Check for updates now");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        updateCheckJCheckBox1.setText("Include experimental pre-release versions (might not work!)");
        updateCheckJCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCheckJCheckBox1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout updaterJPanelLayout = new org.jdesktop.layout.GroupLayout(updaterJPanel);
        updaterJPanel.setLayout(updaterJPanelLayout);
        updaterJPanelLayout.setHorizontalGroup(
            updaterJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(updaterJPanelLayout.createSequentialGroup()
                .add(updaterJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(updaterJPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(updaterJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(updaterJPanelLayout.createSequentialGroup()
                                .add(updaterJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(jLabel5)
                                    .add(jLabel3))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(updaterJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(currVersionJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(latestVersionJLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .add(updaterJPanelLayout.createSequentialGroup()
                                .add(updateCheckJCheckBox)
                                .add(0, 0, Short.MAX_VALUE))))
                    .add(updaterJPanelLayout.createSequentialGroup()
                        .add(updaterJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(updaterJPanelLayout.createSequentialGroup()
                                .add(18, 18, 18)
                                .add(updateCheckJCheckBox1))
                            .add(updaterJPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(jButton2)))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        updaterJPanelLayout.setVerticalGroup(
            updaterJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(updaterJPanelLayout.createSequentialGroup()
                .add(updateCheckJCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(updateCheckJCheckBox1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(updaterJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(currVersionJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(updaterJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(latestVersionJLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton2)
                .add(20, 20, 20))
        );

        contactJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Contact"));

        contactJLabel.setText("<html><a href=\"mailto:barry.young@ubc.ca?subject=Balony\">barry.young@ubc.ca</mailto></html>");
        contactJLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        contactJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contactJLabelMouseClicked(evt);
            }
        });

        jLabel1.setText("Author:");

        jLabel4.setText("Online help:");

        contactJLabel1.setText("<html><a href=\"http://code.google.com/p/balony/wiki/Introduction?tm=6\">GoogleCode wiki</mailto></html>");
        contactJLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        contactJLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contactJLabel1MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout contactJPanelLayout = new org.jdesktop.layout.GroupLayout(contactJPanel);
        contactJPanel.setLayout(contactJPanelLayout);
        contactJPanelLayout.setHorizontalGroup(
            contactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(contactJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(contactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(contactJPanelLayout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(contactJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(contactJPanelLayout.createSequentialGroup()
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(contactJLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contactJPanelLayout.setVerticalGroup(
            contactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(contactJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(contactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(contactJLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(contactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(contactJLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout optionsPanelLayout = new org.jdesktop.layout.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
            optionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(optionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(updaterJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(contactJPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(updaterJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(contactJPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(158, Short.MAX_VALUE))
        );

        tabPane.addTab("Options", optionsPanel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(tabPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(tabPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );

        tabPane.getAccessibleContext().setAccessibleName("Image");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Prepare to load the image
     *
     * @param f The file to load
     * @param rG Reset the grid if true
     */
    public void imageLoad(File f, boolean rG) {
        if (cSize != null) {
            cSize.setVisible(false);
        }
        modCheck();

        currSet = 1;
        currPlate = 1;
        stopped = false;
        resetGrid = rG;
        // Close any open windows
        if (loadedIm != null && loadedIm.getWindow() != null) {
            loadedIm.getWindow().close();
        }
        loaded = false;
        if (f == null) {
            f = (File) imageFileJList.getSelectedValue();
        }
        if (f == null) {
            return;
        }
        currFile = f;
        messageText.append("\nLoading ").append(f.getName()).append("...");
        loadedIm = new Opener().openImage(f.getAbsolutePath());
        oIm = loadedIm.createImagePlus();
        loaded = true;
        messageText.append("done.");
        imageLoad2();
    }

    /**
     * Actually load the image
     */
    public void imageLoad2() {
        if (!calibrated) {
            autoGridSetting = autoGridJCheckBox.isSelected();
            // First time run - calibrate image
            calibrate calib = new calibrate(this, true);
            calib.jComboBox2.setModel((ComboBoxModel<String>) gridChoicejComboBox.getModel());
            calib.setVisible(true);
        }
        threshed = false;
        quant = false;
        inverted = false;
        if (resetGrid) {
            minX = 0;
            maxX = 0;
            minY = 0;
            maxY = 0;
            stepX = 0;
            stepY = 0;
            gridded = false;
        }
        if (rotated) {
            DecimalFormat df = new DecimalFormat("0.00");
            messageText.append("\nRotating image ").append(df.format(theta)).append("°");
            ImageProcessor ip = loadedIm.getProcessor();
            ip.setInterpolationMethod(ImageProcessor.BICUBIC);
            ip.rotate(theta);
        } else {
            theta = 0;
        }
        oriCp = new ColorProcessor(loadedIm.getImage());
        if (loadedIm.getType() != ImagePlus.GRAY8) {
            ImageProcessor ip = loadedIm.getProcessor();
            TypeConverter tp = new TypeConverter(ip, false);
            ip = tp.convertToByte();
            loadedIm = new ImagePlus(currFile.getAbsolutePath(), ip);
        }
        if (autoNameJCheckBox.isSelected()) {
            String ss = currFile.getName().toLowerCase(), t;
            int i, j, k;
            i = ss.lastIndexOf(PLATE);
            j = ss.lastIndexOf(".");
            k = ss.lastIndexOf(SET);
            if (i != -1 && j != -1 && j > i) {
                t = ss.substring(i + 5, j);

                currPlate = Integer.parseInt(getOnlyNumerics(t));
            } else {
                messageText.append("\nCan't decipher plate number.");
                currPlate = 1;
            }

            if (j != -1 && k != -1 && i != 1 && i > k && j > k) {
                t = ss.substring(k + 3, i);
                currSet = Integer.parseInt(getOnlyNumerics(t));

            } else {
                messageText.append("\nCan't decipher set number.");
                currSet = 1;
            }

            if (k != -1) {
                t = currFile.getName().substring(0, k - 1);
                char tt = t.charAt(t.length() - 1);
                while (tt == ' ' || tt == '_' || tt == '-') {
                    t = t.substring(0, t.length() - 2);
                    tt = t.charAt(t.length() - 1);
                }

            } else if (i != -1) {
                t = currFile.getName().substring(0, i - 1);
                char tt = t.charAt(t.length() - 1);
                while (tt == ' ' || tt == '_' || tt == '-') {
                    t = t.substring(0, t.length() - 2);
                    tt = t.charAt(t.length() - 1);
                }
//                System.out.println("Name: " + t);
            } else if (j != -1) {
//                System.out.println("Using default name");
                t = currFile.getName().substring(0, j);
            } else {
                t = currFile.getName();
            }

            setNumberJTextField.setText(currSet.toString());
            plateNumberJTextField.setText(currPlate.toString());
            plateNameJTextField.setText(t);
        }

        if (plateNameJTextField.getText().length() == 0) {
            String s = JOptionPane.showInputDialog("Enter plate name");
            plateNameJTextField.setText(s);
        }

        imWin = new ImageWindow(loadedIm);
        loadedIm.setTitle(currFile.getName() + " [input]");
        qs1 = new QuantScan();
        qs1.QuantScan(loadedIm.getCanvas());
        imWin.setVisible(true);
        imWin.setLocation(this.getWidth(), 0);
        imWin.setIconImage(balloonImage);
        if (imWin.getWidth() + this.getWidth()
                > Toolkit.getDefaultToolkit().getScreenSize().width) {
            Zoom z = new Zoom();
            z.run("out");
        }
        if (inverted == false && autoInvertJCheckBox.isSelected()) {
            loadedIm.getProcessor().invert();
            inverted = true;
        }

        if (fullAuto) {
            return;
        }

        if (incalibration) {
            try {
                ManualGrid mg = new ManualGrid();
                mg.ManualGrid(loadedIm.getCanvas());
                Robot r = new Robot();
                int x = imWin.getX();
                int x2 = imWin.getWidth();
                int y = imWin.getY();
                int y2 = imWin.getHeight();
                r.mouseMove(x + x2 / 8, y + y2 / 8);
            } catch (AWTException ex) {
                Logger.getLogger(Balony.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (autoThreshJCheckBox.isSelected() && rethresh < 1) {
                doThresh();
            } else if (rethresh > 0) {
                loadedIm.getProcessor().threshold(lastThresh - 5 * rethresh);
                threshed = true;
                loadedIm.repaintWindow();
            }
            if (threshed && autoGridJCheckBox.isSelected() && resetGrid) {
                gw = new gridWorker();
                gw.execute();
            } else if (threshed && gridded && autoQuantJCheckBox.isSelected()) {
                drawGrid(loadedIm.getCanvas(), Color.green);
                if (autoQuantJCheckBox.isSelected() && quant == false) {
                    qw = new quantWorker();
                    qw.execute();
                }
            }
        }
    }

    // Draws the current grid over an image
    /**
     *
     * @param ic
     * @param c
     */
    public void drawGrid(ImageCanvas ic, Color c) {
        float i;
        GeneralPath path = new GeneralPath();
        for (i = 0; i < cols; i++) {
            path.moveTo(minX + i * stepX, minY);
            path.lineTo(minX + i * stepX, maxY);
            ic.getImage().setOverlay(path, c, null);
        }
        for (i = 0; i < rows; i++) {
            path.moveTo(minX, minY + i * stepY);
            path.lineTo(maxX, minY + i * stepY);
            ic.getImage().setOverlay(path, c, null);
        }

        if (autoQuantJCheckBox.isSelected() && quant == false
                && !fullAuto && !autoGridJCheckBox.isSelected()) {
            qw = new quantWorker();
            qw.execute();
        }
    }

    // Find spots within the grid area and quantify them
    /**
     *
     */
    public void doQuant() {
        if (threshed == false) {
            messageText.append("\nQuantification requires thresholded image.");
            return;
        }
        if (gridded == false) {
            messageText.append("\nQuantification requires gridded image.");
            return;
        }
        if (loadedIm == null || gridded == false || threshed == false) {
            return;
        }
        circ = Float.parseFloat(quantP.circTextField.getText());
        if (circ < 0.01 || circ > 1.0) {
            circ = (float) 0.5;
            quantP.circTextField.setText("0.5");
        }

        snap = Float.parseFloat(quantP.snapTextField.getText());
        if (snap < 0.001 || snap > 0.5) {
            snap = (float) 0.5;
            quantP.snapTextField.setText("0.5");
        }
        int minpix = Integer.parseInt(quantP.minpixelsTextField.getText());
        if (minpix < 1) {
            minpix = 8;
            quantP.minpixelsTextField.setText("8");
        }
        loadedIm.setRoi((int) (minX - stepX * 0.5), (int) (minY - stepY * 0.5),
                (int) (stepX * cols), (int) (stepY * rows));
        ResultsTable rt = new ResultsTable();
        messageText.append("\nLocating spots...");
        ParticleAnalyzer pa = new ParticleAnalyzer(0, -1, rt, minpix, dx * dy, circ / 2, 1.0);
        pa.analyze(loadedIm);
        messageText.append("done.");
        int tmpIndex[][];
        Area = new int[cols + 1][rows + 1];
        xCoord = new int[cols + 1][rows + 1];
        yCoord = new int[cols + 1][rows + 1];
        height = new int[cols + 1][rows + 1];
        width = new int[cols + 1][rows + 1];
        tmpIndex = new int[cols + 1][rows + 1];
        float xs[], ys[], as[], ws[], hs[];
        xs = rt.getColumn(rt.getColumnIndex(RT_X));
        ys = rt.getColumn(rt.getColumnIndex(RT_Y));
        as = rt.getColumn(rt.getColumnIndex(RT_AREA));
        ws = rt.getColumn(rt.getColumnIndex(RT_WIDTH));
        hs = rt.getColumn(rt.getColumnIndex(RT_HEIGHT));
        int c = 0;
        messageText.append("\nAssigning grid positions...");
        oriCp.setLineWidth(2);
        grOval = new HashMap<>();
        mgOval = new HashMap<>();
        yeOval = new ArrayList<>();
        redRect = new HashMap<>();

        for (int i = 0; i < xs.length; i++) {
            Integer ov[] = new Integer[4];
            int x, y;
            float xm, ym;
            x = Math.round((xs[i] - minX) / stepX) + 1;
            xm = Math.abs(((xs[i] - minX) / stepX) + 1 - x);
            y = Math.round((ys[i] - minY) / stepY) + 1;
            ym = Math.abs(((ys[i] - minY) / stepY) + 1 - y);
            String coord = x + ":" + y;
            double d1, d2;
            d1 = Math.sqrt((double) (xm * xm + ym * ym));

            // Get area of spot currently assigned to this grid position
            int tmp = Area[x][y];

            // and the index for this spot
            int tmp2 = tmpIndex[x][y];

            // Check not too big:
            if (ws[i] < dx * 1.5 && hs[i] < dy * 1.5) {
                // Check deviation from grid
                if (xm < snap && ym < snap) {
                    ov[0] = (int) (xs[i] - ws[i] / 2);
                    ov[1] = (int) (ys[i] - hs[i] / 2);
                    ov[2] = (int) ws[i];
                    ov[3] = (int) hs[i];

                    // If there's already a spot assigned to this position:
                    if (tmp != 0) {
                        float xm2, ym2;
                        xm2 = Math.abs(((xs[tmp2] - minX) / stepX) + 1 - x);
                        ym2 = Math.abs(((ys[tmp2] - minY) / stepY) + 1 - y);
                        d2 = Math.sqrt((double) (xm2 * xm2 + ym2 * ym2));
                        c++;

                        if (d2 > d1) {
                            // New spot is closer match
                            yeOval.add(ov);
                            grOval.put(coord, ov);
                            Area[x][y] = (int) as[i];
                            xCoord[x][y] = (int) xs[i];
                            yCoord[x][y] = (int) ys[i];
                            width[x][y] = (int) ws[i];
                            height[x][y] = (int) hs[i];

                            tmpIndex[x][y] = i;  // Place closest spot in grid
                        } else {
                            yeOval.add(ov);
                        }
                    } else {
                        grOval.put(coord, ov);
                        Area[x][y] = (int) as[i];
                        xCoord[x][y] = (int) xs[i];
                        yCoord[x][y] = (int) ys[i];
                        width[x][y] = (int) ws[i];
                        height[x][y] = (int) hs[i];
                        tmpIndex[x][y] = i;
                    }
                }
            }
        }

        // Re-scan to see if we missed anything
        badSpots = 0;
        doQuant2(false, circ, snap, minpix);
        loadedIm.killRoi();
        messageText.append("\ndone.");
        int allowed = 0;
        try {
            allowed = Integer.parseInt(quantP.allowBadSpotsJTextField.getText());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        oIm = new ImagePlus(currFile.getName() + " [output]", oriCp.duplicate());
        switchToOutputImage();

        if (badSpots > 0) {

            int n = JOptionPane.showOptionDialog(this,
                    "Perform low-stringency second pass?", badSpots + " Bad Spots Found",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (n == JOptionPane.YES_OPTION) {
                doQuant2(true, circ, snap, minpix);
                drawShapes();
            }

        }

        if (badSpots > allowed) {
            messageText.append("\nWarning: ").append(badSpots).append(" bad spots found.");
            if (norethresh) {
                norethresh = false;
            } else if (quantP.reThreshJCheckBox.isSelected() && rethresh < 10) {
                if (badSpots < bestSpots || bestSpots == -1) {
//                    System.out.println("Best spots: " + badSpots);
                    bestSpots = badSpots;
                    bestThresh = rethresh;
                }
                rethresh++;
                messageText.append("\nAttempting with lower threshold (").append(lastThresh - (5 * rethresh)).append(").");
                if (loadedIm != null) {
                    loadedIm.changes = false;
                    loadedIm.close();
                }
                resetGrid = false;
                loadedIm.setImage(oriCp.createImage());
                switchToInputImage();
                imageLoad2();
                return;
            } else if (quantP.reThreshJCheckBox.isSelected()) {
                messageText.append("\nUsing best threshold.");
                rethresh = bestThresh;
                norethresh = true;
                if (loadedIm != null) {
                    loadedIm.changes = false;
                    loadedIm.close();
                }
                resetGrid = false;
                loadedIm = new ImagePlus(currFile.getName(), oriCp.duplicate());
                switchToInputImage();
                imageLoad2();
            }

        } else if (badSpots == 0) {
            messageText.append("\nNo bad spots found.");
        } else {
            messageText.append("\n").append(badSpots).append(" bad spots found (permissible).");
        }
        rethresh = 0;
        quant = true;

        // Check for empty rows/cols - means gridding might have been wrong.
        int leftCol, rightCol, topRow, botRow;
        leftCol = rightCol = topRow = botRow = 0;

        for (int i = 0; i < rows; i++) {
            leftCol += Area[1][i];
            rightCol += Area[cols][i];
        }

        for (int i = 0; i < cols; i++) {
            topRow += Area[i][1];
            botRow += Area[i][rows];
        }

        if (leftCol == 0 || rightCol == 0 || topRow == 0 || botRow == 0) {
//            System.out.println("Missing row/col!");
            drawGrid(oIm.getCanvas(), Color.red);
            final String STR_LEFT = "Left";
            final String STR_RIGHT = "Right";
            final String STR_UP = "Up";
            final String STR_DOWN = "Down";
            String[] opts = {STR_LEFT, STR_RIGHT, STR_UP, STR_DOWN};

            Object resp = JOptionPane.showInputDialog(this, "Empty row/column. Shift?",
                    "Warning", JOptionPane.WARNING_MESSAGE, null,
                    opts, opts[0]);

            if (resp != null) {

                String nudge = resp.toString();

                if (nudge.equals(STR_LEFT)) {
                    minX -= stepX;
                    maxX -= stepX;
                }

                if (nudge.equals(STR_RIGHT)) {
                    minX += stepX;
                    maxX += stepX;
                }

                if (nudge.equals(STR_UP)) {
                    minY -= stepY;
                    maxY -= stepY;
                }

                if (nudge.equals(STR_DOWN)) {
                    minY += stepY;
                    maxY += stepY;
                }

                doQuant();
            } else {
                drawShapes();
            }
        }

        if (!fullAuto) {
            if (autoSaveJCheckBox.isSelected()) {
                doSave(true);
            }
            qs = new QuantScan();
            qs.QuantScan(oIm.getCanvas());
        }

        if (quantP.sizePopupJCheckBox.isSelected()) {
            cSize.setVisible(true);
            cSize.setAlwaysOnTop(true);
        }
    }

    /**
     *
     * @param lowStrin
     * @param circ
     * @param snap
     * @param minpix
     */
    public void doQuant2(boolean lowStrin, float circ, float snap, int minpix) {
        float as[], ws[], hs[], xs[], ys[];

        redRect = new HashMap<>();
        badSpots = 0;
        ResultsTable rt;
        ParticleAnalyzer pa;
        for (int j = 1; j <= rows; j++) {
            for (int i = 1; i <= cols; i++) {

                if (stopped) {
                    stopped = false;
                    return;
                }

                Integer ov[] = new Integer[4];
                String coord = i + ":" + j;
                if (Area[i][j] == 0) {
                    loadedIm.setRoi((int) (minX + (i - 1.5) * stepX),
                            (int) (minY + (j - 1.5) * stepY), (int) (stepX),
                            (int) (stepY));
                    ImageProcessor ip = loadedIm.getProcessor().crop();
                    double px = 0;
                    int x1 = (ip.getHeight() / 4);
                    int y1 = (ip.getWidth() / 4);
                    for (int k = x1; k < x1 * 3; k++) {
                        for (int l = y1; l < y1 * 3; l++) {
                            px += 1 - (ip.getPixel(l, k) / 255);
                        }
                    }

                    int m = Integer.parseInt(quantP.minpixelsTextField.getText());
                    if (m > (ip.getHeight() * ip.getWidth())) {
                        m = ip.getHeight() * ip.getWidth();
                    }

                    // Is there anything in this cell?
                    if (px > m) {
                        loadedIm.setRoi((int) (minX + (i - 1.5) * stepX),
                                (int) (minY + (j - 1.5) * stepY),
                                (int) (stepX), (int) (stepY));
                        rt = new ResultsTable();

                        double mincirc = 0d;
                        if (!lowStrin) {
                            mincirc = circ;
                        }

                        pa = new ParticleAnalyzer(0, -1, rt, minpix, stepX * stepY, mincirc, 1.0);
                        pa.analyze(loadedIm);
                        messageText.append("\n").append(j).append(",").append(i).append(": rescanning");
                        if (rt.columnExists(1)) {

                            as = rt.getColumn(rt.getColumnIndex(RT_AREA));
                            xs = rt.getColumn(rt.getColumnIndex(RT_X));
                            ys = rt.getColumn(rt.getColumnIndex(RT_Y));
                            ws = rt.getColumn(rt.getColumnIndex(RT_WIDTH));
                            hs = rt.getColumn(rt.getColumnIndex(RT_HEIGHT));

                            float xr, yr, ar, wr, hr, xc, yc;
                            xr = minX + stepX * (i - 1);
                            yr = minY + stepY * (j - 1);
                            ar = 0;
                            wr = 0;
                            hr = 0;
                            xc = 0;
                            yc = 0;

                            for (int z = 0; z < as.length; z++) {
                                if (as[z] > ar) {
                                    ar = as[z];
                                    wr = ws[z];
                                    hr = hs[z];
                                    xc = xs[z];
                                    yc = ys[z];
                                }
                            }

                            Area[i][j] = (int) ar;
                            xCoord[i][j] = (int) xc;
                            yCoord[i][j] = (int) yc;
                            width[i][j] = (int) wr;
                            height[i][j] = (int) hr;
                            messageText.append(". Spot area: ").append(ar);
                            ov[0] = (int) (xr - wr / 2);
                            ov[1] = (int) (yr - hr / 2);
                            ov[2] = (int) wr;
                            ov[3] = (int) hr;
                            grOval.put(coord, ov);
                        } else {
                            ov[0] = (int) (minX + (i - 1.5) * stepX);
                            ov[1] = (int) (minY + (j - 1.5) * stepY);
                            ov[2] = (int) stepX;
                            ov[3] = (int) stepY;
                            badSpots++;
                            redRect.put(coord, ov);
                            messageText.append(". Nothing found.");
                        }
                    }
                }
            }
        }
    }

    // Draw shapes corresponding to measured colonies
    /**
     *
     */
    public void drawShapes() {
        Overlay o = new Overlay();

        if (!yeOval.isEmpty()) {
            yeOval.stream().map((ii) -> new OvalRoi(ii[0], ii[1], ii[2], ii[3])).map((r) -> {
                r.setStrokeColor(Color.yellow);
                return r;
            }).forEach((r) -> {
                o.add(r);
            });
        }
        grOval.values().stream().map((ii) -> new OvalRoi(ii[0], ii[1], ii[2], ii[3])).map((r) -> {
            r.setStrokeColor(Color.green);
            return r;
        }).forEach((r) -> {
            o.add(r);
        });
        mgOval.values().stream().map((ii) -> new OvalRoi(ii[0], ii[1], ii[2], ii[3])).map((r) -> {
            r.setStrokeColor(Color.magenta);
            return r;
        }).forEach((r) -> {
            o.add(r);
        });
        redRect.values().stream().map((ii) -> new Roi(ii[0], ii[1], ii[2], ii[3])).map((r) -> {
            r.setStrokeColor(Color.red);
            return r;
        }).forEach((r) -> {
            o.add(r);
        });
        oIm.setOverlay(o);
    }

    // Manual Threshold
    private void choosefolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choosefolderButtonActionPerformed

//        String osn = System.getProperty("os.name").toLowerCase();
//        if (osn.indexOf("mac") == -1) {
        JFileChooser jfc = new JFileChooser();
        String s = null;

        if (prefs.getProperty(PREFS_IMAGEFOLDER) != null) {
            jfc.setCurrentDirectory(new File(prefs.getProperty(PREFS_IMAGEFOLDER)));
        }
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            s = jfc.getSelectedFile().getAbsolutePath();
        }
        if (s == null) {
            return;
        }
        doSetFolder(s);
        prefs.setProperty(PREFS_IMAGEFOLDER, s);
        savePrefs();

//        } else {
//
//            System.setProperty("apple.awt.fileDialogForDirectories", "true");
//            FileDialog fd = new FileDialog(this);
//
//            if (prefs.getProperty(PREFS_IMAGEFOLDER) != null) {
//                fd.setDirectory(prefs.getProperty(PREFS_IMAGEFOLDER));
//            }
//            fd.setVisible(true);
////            File[] f = fd.getFiles();
////            if (f.length == 0) {
////                return;
////            }
//
//            String s = fd.getDirectory();
//
//            if (s == null) {
//                return;
//            }
//
//            String f = s + fd.getFile();
//
//            doSetFolder(f);
//            prefs.setProperty(PREFS_IMAGEFOLDER, f);
//            savePrefs();
//        }
    }

    public void doSetFolder(String s) {
        messageText.append("\nSelected folder: ").append(s);
        currFolder = new File(s);
        folderJTextField.setText(s);
        ctrldirTextField.setText(s);
        ctrldirTextField.setForeground(Color.black);
        expdirTextField.setText(s);
        expdirTextField.setForeground(Color.black);
        updateScoreTab();
        File folder = new File(s);
        File[] listoffiles = folder.listFiles();
        int cnt = 0;
        ArrayList<File> vFiles = new ArrayList<>();

        for (File myF : listoffiles) {
            String fn = myF.getName().toLowerCase();
            if ((fn.endsWith(".jpg") || fn.endsWith(".png") || fn.endsWith(".tif")
                    || fn.endsWith(".jpeg")) && !fn.startsWith(".")) {
                vFiles.add(myF);
                cnt++;
            }
        }

        loadImageFileList((File[]) vFiles.toArray(new File[vFiles.size()]));
        messageText.append("\nFound ").append(cnt).append(" image files.");
    }//GEN-LAST:event_choosefolderButtonActionPerformed

    private void quantButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantButtonActionPerformed
        qw = new quantWorker();
        qw.execute();
    }//GEN-LAST:event_quantButtonActionPerformed

    /**
     *
     */
    public class quantWorker extends SwingWorker<String, Void> {

        @Override
        protected String doInBackground() throws Exception {
            doQuant();
            return "";
        }
    }
    private void zoominButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoominButtonActionPerformed
        if (loadedIm != null) {
            Zoomer z = new Zoomer();
            ImageCanvas ic1 = loadedIm.getCanvas();
            z.Zoomer(ic1, 0);
        }
        if (oIm != null) {
            Zoomer z2 = new Zoomer();
            ImageCanvas ic2 = oIm.getCanvas();
            z2.Zoomer(ic2, 0);
        }
        messageText.append("\nLeft-click to zoom in, right-click to zoom out.");

    }//GEN-LAST:event_zoominButtonActionPerformed

    private void definegridButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_definegridButtonActionPerformed
        if (!loaded) {
            return;
        }
        switchToInputImage();
        ImageCanvas ic = loadedIm.getCanvas();
        if (maxX == 0 || maxY == 0) {
            zoomBox = new ImagePlus("",
                    new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB));
            zoomBox.show();
            zoomBox.setTitle("100% Zoom");
            zoomBox.getWindow().setIconImage(balloonImage);
            ManualGrid mg = new ManualGrid();
            mg.ManualGrid(ic);
        } else {
            drawGrid(ic, Color.green);
        }

    }//GEN-LAST:event_definegridButtonActionPerformed

    /**
     *
     */
    public void switchToInputImage() {
        if (imWin == null || imWin.getImagePlus() == null || loadedIm == null) {
            return;
        }
        if (imWin.getImagePlus() == loadedIm) {
            return;
        }
        double mag = imWin.getImagePlus().getCanvas().getMagnification();
        Rectangle r = imWin.getCanvas().getSrcRect();
        imWin.setImage(loadedIm);
        loadedIm.setOverlay(null);
        if (stepX != 0) {
            drawGrid(loadedIm.getCanvas(), Color.green);
        }
        loadedIm.show();
        loadedIm.getCanvas().setMagnification(mag);
        loadedIm.getCanvas().setSourceRect(r);
    }

    /**
     *
     */
    public void switchToOutputImage() {
        if (imWin == null || imWin.getImagePlus() == null || oIm == null) {
            return;
        }
        if (imWin.getImagePlus() == oIm) {
            return;
        }
        double mag = imWin.getImagePlus().getCanvas().getMagnification();
        Rectangle r = imWin.getCanvas().getSrcRect();
        imWin.setImage(oIm);
        oIm.show();
        oIm.getCanvas().setMagnification(mag);
        oIm.getCanvas().setSourceRect(r);
        oIm.setOverlay(null);
        drawShapes();
        qs = new QuantScan();
        qs.QuantScan(oIm.getCanvas());
    }

    private void gridclearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridclearButtonActionPerformed
        minX = 0;
        maxX = 0;
        minY = 0;
        maxY = 0;
        stepX = 0;
        stepY = 0;
        gridded = false;
        switchToInputImage();
        loadedIm.setOverlay(null);
    }//GEN-LAST:event_gridclearButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        doSave(true);
    }

    public void doSave(boolean owcheck) {
        try {
            currPlate = Integer.parseInt(plateNumberJTextField.getText());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        try {
            currSet = Integer.parseInt(setNumberJTextField.getText());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        if (quant == false || currPlate == 0 || currSet == 0) {
            return;
        }
        if (badSpots > 0 && owcheck) {
            int k = JOptionPane.showOptionDialog(null,
                    "Bad spots found. Are you sure you want to save?", "Warning!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, null, null);
            if (k == JOptionPane.NO_OPTION) {
                return;
            }
        }
        try {
            messageText.append("\nWriting file...");
            File outFile = new File(currFolder.getAbsolutePath() + File.separator
                    + plateNameJTextField.getText()
                    + "_RAW_set-" + Integer.toString(currSet) + "_plate-"
                    + Integer.toString(currPlate) + ".txt");
            if (outFile.exists() && owcheck) {
                int n = JOptionPane.showOptionDialog(null,
                        "File already exists. Overwrite?", "Warning!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, null, null);
                if (n == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            try (BufferedWriter out = new BufferedWriter(new FileWriter(outFile))) {
                out.write(BALONY_RAW_DATA);
                out.newLine();
                DateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date d = new Date();
                out.write("Date: " + sdf.format(d));
                out.newLine();
                out.write("Rows: " + Integer.toString(rows));
                out.newLine();
                out.write("Cols: " + Integer.toString(cols));
                out.newLine();
                out.write("Name: " + plateNameJTextField.getText());
                out.newLine();
                out.write("Set: " + Integer.toString(currSet));
                out.newLine();
                out.write("Plate: " + Integer.toString(currPlate));
                out.newLine();
                out.write("Source file: " + currFile.getAbsolutePath());
                out.newLine();
                if (badSpots > 0) {
                    out.write("Bad spots: " + badSpots);
                    out.newLine();
                }
                out.write("minX: " + minX);
                out.newLine();
                out.write("minY: " + minY);
                out.newLine();
                out.write("stepX: " + stepX);
                out.newLine();
                out.write("stepY: " + stepY);
                out.newLine();
                out.write("dpi: " + dpi);
                out.newLine();
                out.newLine();
                out.write(BEGIN_DATA);
                out.newLine();
                out.write("Row\tCol\tArea\tx\ty\twidth\theight");
                out.newLine();
                int i, j;
                for (i = 1; i <= rows; i++) {
                    for (j = 1; j <= cols; j++) {
                        out.write(Integer.toString(i) + "\t" + Integer.toString(j)
                                + "\t" + Integer.toString(Area[j][i])
                                + "\t" + Integer.toString(xCoord[j][i])
                                + "\t" + Integer.toString(yCoord[j][i])
                                + "\t" + Integer.toString(width[j][i])
                                + "\t" + Integer.toString(height[j][i]));
                        out.newLine();
                    }
                }
                messageText.append("done.");
                saveAutoCheckImage();
            }
            fileMod = false;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        imageFileJList.repaint();

    }//GEN-LAST:event_saveButtonActionPerformed

    /**
     *
     */
    public void modCheck() {
        if (fileMod) {
            if (oIm != null) {
                oIm.show();
            }
            int n = JOptionPane.showOptionDialog(null,
                    "File has been modified. Save changes?", "Warning!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, null, null);
            if (n == JOptionPane.NO_OPTION) {
                return;
            }
            doSave(false);
        }

    }

    /**
     *
     * @param infile
     * @return
     */
    public static String getSaveFileName(String infile) {
        String ss = infile.toLowerCase(), t;
        String set, plate, base;
        int i, j, k;
        i = ss.lastIndexOf(PLATE);
        j = ss.lastIndexOf(".");
        k = ss.lastIndexOf(SET);
        if (i != -1 && j != -1 && j > i) {
            t = ss.substring(i + 5, j);
            plate = getOnlyNumerics(t);
        } else {
            plate = "";
        }
        if (j != -1 && k != -1 && i != 1 && i > k && j > k) {
            t = ss.substring(k + 3, i);
            set = getOnlyNumerics(t);
        } else {
            set = "";
        }
        if (k != -1) {
            t = infile.substring(0, k - 1);
            char tt = t.charAt(t.length() - 1);
            while (tt == ' ' || tt == '_' || tt == '-') {
                t = t.substring(0, t.length() - 2);
                tt = t.charAt(t.length() - 1);
            }
            base = t;
        } else {
            base = "";
        }
        return base + "_RAW_set-" + set + "_plate-" + plate + ".txt";
    }

    private void gridChoicejComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_gridChoicejComboBoxItemStateChanged
    }//GEN-LAST:event_gridChoicejComboBoxItemStateChanged

    private void thresholdButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thresholdButtonActionPerformed
        if (loaded == false) {
            return;
        }

//        if (loaded == true && threshed == false) {
        doThresh();
        if (autoGridJCheckBox.isSelected()) {
            gw = new gridWorker();
            gw.execute();
        }
//        } else if (loaded == true && threshed == true) {
//            new imageLoader().execute();
//        }
    }//GEN-LAST:event_thresholdButtonActionPerformed

    private void threshRadioManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_threshRadioManualActionPerformed

        if (threshRadioAuto.isSelected()) {
            prefs.setProperty(PREFS_IMAGE_THRESHMETHOD, THRESH_AUTO);
        } else {
            prefs.setProperty(PREFS_IMAGE_THRESHMETHOD, THRESH_MANUAL);
        }

        savePrefs();

        if (threshRadioManual.isSelected()) {
            imageThreshManualJTextField.setEnabled(true);
            threshJSlider.setEnabled(true);
        }

    }//GEN-LAST:event_threshRadioManualActionPerformed

    private void gridChoicejComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridChoicejComboBoxActionPerformed
        updateGridType();
    }

    public void updateGridType() {
        String g = gridChoicejComboBox.getSelectedItem().toString();
        if (g.equals("Custom...")) {
            gridP.setVisible(true);
            gridP.gridnameTextField.setText("New Preset");
            gridP.griddpiTextField.setText(Integer.toString(dpi));
            gridP.gridcolsTextField.setText(Integer.toString(cols));
            gridP.gridrowsTextField.setText(Integer.toString(rows));
            gridP.griddxTextField.setText(Float.toString(dx));
            gridP.griddyTextField.setText(Float.toString(dy));
        } else {
            plateArray p = allArraysMap.get(g);
            dpi = p.getDpi();
            cols = p.getCols();
            rows = p.getRows();
            dx = p.getXstp();
            dy = p.getYstp();
            gridP.gridnameTextField.setText(g);
            gridP.griddpiTextField.setText(Integer.toString(dpi));
            gridP.gridcolsTextField.setText(Integer.toString(cols));
            gridP.gridrowsTextField.setText(Integer.toString(rows));
            gridP.griddxTextField.setText(Float.toString(dx));
            gridP.griddyTextField.setText(Float.toString(dy));
        }
        prefs.setProperty(PREFS_IMAGE_PRESET, gridP.gridnameTextField.getText());
        savePrefs();
    }//GEN-LAST:event_gridChoicejComboBoxActionPerformed

    /**
     *
     */
    public void savePrefs() {
        try {
            String outFile = get_prefsfile_name();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outFile)));
            prefs.storeToXML(out, null);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void rotatecorrectorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotatecorrectorButtonActionPerformed
        switchToInputImage();
        RotateCorrector rc = new RotateCorrector();
        rc.RotateCorrector(loadedIm.getCanvas());

    }//GEN-LAST:event_rotatecorrectorButtonActionPerformed

    private void manualspotdefineButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualspotdefineButtonActionPerformed
        if (oIm == null) {
            return;
        }
        switchToOutputImage();
        ManualSpot ms = new ManualSpot();
        ms.ManualSpot(oIm.getCanvas());
    }//GEN-LAST:event_manualspotdefineButtonActionPerformed

    private void zoomoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomoutButtonActionPerformed
        if (loadedIm != null) {
            Zoomer z = new Zoomer();
            ImageCanvas ic1 = loadedIm.getCanvas();
            z.Zoomer(ic1, 1);
        }
        if (oIm != null) {
            Zoomer z2 = new Zoomer();
            ImageCanvas ic2 = oIm.getCanvas();
            z2.Zoomer(ic2, 1);
        }
        messageText.append("\nLeft-click to zoom out, right-click to zoom in.");
    }//GEN-LAST:event_zoomoutButtonActionPerformed

    private void tabPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPaneStateChanged
    }//GEN-LAST:event_tabPaneStateChanged

    private void scoringtabaPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_scoringtabaPanelComponentShown
    }

    public void updateScoreTab() {
        String s = ctrldirTextField.getText();
        if (s != null && !s.isEmpty()) {

            File folder = new File(s);
            File[] listoffiles = folder.listFiles();
            if (listoffiles != null) {
                int j = listoffiles.length;
                ctrlplateData = new platenameData[j];
                int cnt = 0;
                for (File myF : listoffiles) {
                    s = myF.getName();
                    if (s.toLowerCase().endsWith(".txt") && myF.length() > 0) {
                        try {
                            BufferedReader in = new BufferedReader(new FileReader(myF));
                            String t = in.readLine();
                            if (t.equals(BALONY_RAW_DATA)) {
                                while (!(t = in.readLine()).equals(BEGIN_DATA)) {
                                    if (!t.isEmpty()) {
                                        if (t.startsWith("Name")) {
                                            String u = t.substring(6);
                                            platenameData p = new platenameData();
                                            int i = 0;
                                            if (cnt > 0) {
                                                for (platenameData pd : ctrlplateData) {
                                                    if (pd != null && pd.getName().equals(u)) {
                                                        p = pd;
                                                        i = 1;
                                                    }
                                                }
                                            }
                                            if (i != 1) {
                                                ctrlplateData[cnt] = new platenameData();
                                                ctrlplateData[cnt].setName(u);
                                                ctrlplateData[cnt].setFiles(new ArrayList<>());
                                                ctrlplateData[cnt].getFiles().add(myF);
                                                cnt++;
                                            } else {
                                                p.getFiles().add(myF);
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                    }
                }

                String ct = "";
                if (ctrlplateComboBox.getSelectedIndex() != -1) {
                    ct = ctrlplateComboBox.getSelectedItem().toString();
                }

                ctrlplateComboBox.removeAllItems();
                for (platenameData pd : ctrlplateData) {
                    if (pd != null) {
                        ctrlplateComboBox.addItem(pd.getName());
                    }
                }
                if (!ct.isEmpty()) {
                    ctrlplateComboBox.setSelectedItem(ct);
                }
            }
        }

        s = expdirTextField.getText();
        if (s == null || s.isEmpty()) {
            return;
        }

        File folder = new File(s);
        File[] listoffiles = folder.listFiles();
        if (listoffiles == null) {
            return;
        }

        int j = listoffiles.length;
        expplateData = new platenameData[j];
        int cnt = 0;
        for (File myF : listoffiles) {
            s = myF.getName();
            if (s.toLowerCase().endsWith("txt") && myF.length() > 0) {
                try {
                    BufferedReader in = new BufferedReader(new FileReader(myF));
                    String t = in.readLine();
                    if (t.equals(BALONY_RAW_DATA)) {
                        while (!(t = in.readLine()).startsWith(BEGIN_DATA)) {
                            if (!t.isEmpty()) {
                                if (t.startsWith("Name")) {
                                    String u = t.substring(6);
                                    platenameData p = new platenameData();
                                    int i = 0;
                                    if (cnt > 0) {
                                        for (platenameData pd : expplateData) {
                                            if (pd != null && pd.getName().equals(u)) {
                                                p = pd;
                                                i = 1;
                                            }
                                        }
                                    }
                                    if (i != 1) {
                                        expplateData[cnt] = new platenameData();
                                        expplateData[cnt].setName(u);
                                        expplateData[cnt].setFiles(new ArrayList<>());
                                        expplateData[cnt].getFiles().add(myF);
                                        cnt++;
                                    } else {
                                        p.getFiles().add(myF);
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }

        String et = "";
        if (expplateComboBox.getSelectedIndex() != -1) {
            et = expplateComboBox.getSelectedItem().toString();
        }

        expplateComboBox.removeAllItems();
        for (platenameData pd : expplateData) {
            if (pd != null) {
                expplateComboBox.addItem(pd.getName());
            }
        }

        if (!et.isEmpty()) {
            expplateComboBox.setSelectedItem(et);
        }
    }//GEN-LAST:event_scoringtabaPanelComponentShown

    private void scoreRCComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoreRCComboBoxActionPerformed
        prefs.setProperty(PREFS_SCORE_NORM, scoreRCComboBox.getSelectedItem().toString());
        savePrefs();
    }//GEN-LAST:event_scoreRCComboBoxActionPerformed

    private void saveScoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveScoreButtonActionPerformed

        doAddCtrl();
        doAddExp();

        ArrayList<String> myQueries = new ArrayList<>();
        boolean queryKey = useQueryKeyJCheckBox.isSelected();

        String[][][] queryArray;
        queryArray = new String[maxExpPlate + 1][expData.getRows() + 1][expData.getCols() + 1];

        if (queryKey) {

            // Read in query array file
            System.out.println("Plates: " + expData.getPlates());

            if (queryArrayFile == null) {
                return;
            }

            try {
                int p, r, c;
                BufferedReader in = new BufferedReader(new FileReader(queryArrayFile));
                String s;
                System.out.println("QAF: " + queryArrayFile.getAbsolutePath());
                while ((s = in.readLine()) != null) {
                    System.out.println("Inloop");
                    p = -1;
                    r = -1;
                    c = -1;
                    String[] d = s.split("\t");
                    if (d.length > 3) {
                        try {
                            p = Integer.parseInt(d[0]);
                            r = Integer.parseInt(d[1]);
                            c = Integer.parseInt(d[2]);

                            if (p > -1 && c > -1 && r > -1) {
                                queryArray[p][r][c] = d[3];
                                if (!myQueries.contains(d[3])) {
                                    myQueries.add(d[3]);
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println(e.getLocalizedMessage());
                        }

                    }
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }

        }

        if (scorenameTextField.getText() == null || scorenameTextField.getText().isEmpty()) {
            return;
        }

        JFileChooser jfc = new JFileChooser();
        String ss = null;

        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            ss = jfc.getSelectedFile().getAbsolutePath() + File.separator;
        }

        if (ss == null) {
            return;
        }

        File[] f = new File[maxCtrlSet - minCtrlSet + 1];

        if (!queryKey) {
            myQueries = new ArrayList<>();
            myQueries.add(scorenameTextField.getText());
        }

        for (String currentQuery : myQueries) {

            if (scoreByArrayPosRadioButton.isSelected()) {
                // One data file per set - assume all sets in range are present
                for (int s = minCtrlSet; s <= maxCtrlSet; s++) {
                    try {

                        String outFile = ss + scorenameTextField.getText()
                                + "_set-" + Integer.toString(s) + ".txt";

                        if (queryKey) {
                            outFile = ss + currentQuery
                                    + "_set-" + Integer.toString(s) + ".txt";
                        }

                        try (BufferedWriter out = new BufferedWriter(new FileWriter(outFile))) {
                            f[s - minCtrlSet] = new File(outFile);
                            out.write(SCORE_HEADER);
                            out.newLine();
                            for (int p = minCtrlPlate; p
                                    <= maxCtrlPlate; p++) {
                                for (int i = 1; i
                                        <= ctrlData.getRows(); i++) {
                                    for (int j = 1; j
                                            <= ctrlData.getCols(); j++) {
                                        if (ctrlData.getNormArea()[s][p][1][1] == null
                                                || expData.getNormArea()[s][p][1][1] == null) {
                                            JOptionPane.showMessageDialog(this, "Data missing for plate.",
                                                    "Warning", JOptionPane.WARNING_MESSAGE);
                                            return;
                                        }
                                        out.write(scorenameTextField.getText() + "\t");
                                        if (keyOrfs != null && keyOrfs.length > 0) {
                                            try {
                                                String orf = keyOrfs[p][i][j];
                                                out.write(orf);
                                            } catch (Exception e) {
                                                System.out.println(e.getLocalizedMessage());
                                            }
                                        }
                                        out.write("\t");
                                        if (keyGenes != null && keyGenes.length > 0) {
                                            try {
                                                String gene = keyGenes[p][i][j];
                                                out.write(gene);
                                            } catch (Exception e) {
                                                System.out.println(e.getLocalizedMessage());
                                            }
                                        }
                                        out.write("\t");
                                        out.write(Integer.toString(p));
                                        out.write("\t");
                                        out.write(Integer.toString(i));
                                        out.write("\t");
                                        out.write(Integer.toString(j));
                                        out.write("\t");
                                        out.write("\t\t\t\t\t");
                                        if (!queryKey || currentQuery.equals(queryArray[p][i][j])) {
                                            out.write(Double.toString(ctrlData.getNormArea()[s][p][i][j]));
                                        } else {
                                            out.write("0");
                                        }

                                        out.write("\t\t");
                                        if (!queryKey || currentQuery.equals(queryArray[p][i][j])) {
                                            out.write(Double.toString(expData.getNormArea()[s][p][i][j]));
                                        } else {
                                            out.write("0");
                                        }

                                        out.write("\t\t");

                                        if (!queryKey || currentQuery.equals(queryArray[p][i][j])) {

                                            out.write(Double.toString(ctrlData.getNormArea()[s][p][i][j]
                                                    - expData.getNormArea()[s][p][i][j]));
                                        } else {
                                            out.write("0");
                                        }

                                        out.newLine();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                messageText.append("\nScored data saved.");
            } else {
                // No key file? Abort.
                if (keyOrfs == null || keyOrfs.length == 0) {
                    messageText.append("\nKey File required for output by ORF.");
                    return;
                }
                HashMap<String, orfScores> cOrfs = new HashMap<>();
                HashMap<String, orfScores> eOrfs = new HashMap<>();
                for (int s = minCtrlSet; s
                        <= maxCtrlSet; s++) {
                    for (int p = minCtrlPlate; p
                            <= maxCtrlPlate; p++) {
                        for (int i = 1; i
                                <= ctrlData.getRows(); i++) {
                            for (int j = 1; j
                                    <= ctrlData.getCols(); j++) {
                                try {
                                    if (keyOrfs[p][i][j] != null) {
                                        String orf = keyOrfs[p][i][j];
                                        if (cOrfs.size() > 0 && cOrfs.keySet().contains(orf)) {
                                            orfScores os = cOrfs.get(orf);
                                            os.getVals().add(ctrlData.getNormArea()[s][p][i][j]);
                                            cOrfs.put(orf, os);
                                            os = eOrfs.get(orf);
                                            os.getVals().add(expData.getNormArea()[s][p][i][j]);
                                            eOrfs.put(orf, os);
                                        } else {
                                            orfScores os = new orfScores();
                                            os.setVals(new ArrayList<>());
                                            os.setGene(keyGenes[p][i][j]);
                                            os.getVals().add(ctrlData.getNormArea()[s][p][i][j]);
                                            cOrfs.put(orf, os);
                                            os = new orfScores();
                                            os.setVals(new ArrayList<>());
                                            os.setGene(keyGenes[p][i][j]);
                                            os.getVals().add(expData.getNormArea()[s][p][i][j]);
                                            eOrfs.put(orf, os);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println(e.getLocalizedMessage());
                                }
                            }
                        }
                    }
                    try {
                        String outFile = ss + scorenameTextField.getText() + "_set-"
                                + Integer.toString(s) + ".txt";
                        try (BufferedWriter out = new BufferedWriter(new FileWriter(outFile))) {
                            out.write(SCORE_HEADER);
                            out.newLine();
                            TreeSet<String> allOrfs = new TreeSet<>(cOrfs.keySet());
                            for (String orf : allOrfs) {
                                out.write(scorenameTextField.getText() + "\t");
                                out.write(orf);
                                out.write("\t");
                                out.write(keyOrfList.get(orf));
                                out.write("\t\t\t\t\t\t\t\t\t");

                                // Ctrl:
                                ArrayList<Double> v = cOrfs.get(orf).getVals();
                                Double d1 = 0d;
                                for (Double vd : v) {
                                    d1 += vd;
                                }
                                d1 /= v.size();
                                out.write(d1.toString());
                                out.write("\t");

                                double sum = 0d, sd;
                                if (v.size() > 1) {
                                    for (Double dd : v) {
                                        double dtmp = dd - d1;
                                        sum += dtmp * dtmp;
                                    }
                                    sd = Math.sqrt(sum / v.size());
                                    out.write(Double.toString(sd));
                                }
                                out.write("\t");
                                v = eOrfs.get(orf).getVals();
                                Double d2 = 0d;
                                for (Double vd : v) {
                                    d2 += vd;
                                }
                                d2 /= v.size();
                                out.write(d2.toString());
                                out.write("\t");
                                sum = 0d;
                                if (v.size() > 1) {
                                    for (Double dd : v) {
                                        double dtmp = dd - d2;
                                        sum += dtmp * dtmp;
                                    }
                                    sd = Math.sqrt(sum / v.size());
                                    out.write(Double.toString(sd));
                                }
                                out.write("\t");
                                out.write(Double.toString(d1 - d2));
                                out.newLine();
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
            }
        }

        if (autoAnalyzeJCheckBox.isSelected()) {
            loadAnalysisFiles(f);
        }
    }//GEN-LAST:event_saveScoreButtonActionPerformed

    private void ctrldirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrldirButtonActionPerformed
        JFileChooser jfc = new JFileChooser();
        String s = null;

        if (prefs.getProperty(PREFS_CTRLFOLDER) != null) {
            jfc.setCurrentDirectory(new File(prefs.getProperty(PREFS_CTRLFOLDER)));
        }
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            s = jfc.getSelectedFile().getAbsolutePath() + File.separator;
        }

        if (s == null) {
            return;
        }

        prefs.setProperty(PREFS_CTRLFOLDER, s);
        savePrefs();
        ctrldirTextField.setText(s);
        updateScoreTab();
    }//GEN-LAST:event_ctrldirButtonActionPerformed
    private void expButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expButtonActionPerformed
        JFileChooser jfc = new JFileChooser();
        String s = null;

        if (prefs.getProperty(PREFS_EXPFOLDER) != null) {
            jfc.setCurrentDirectory(new File(prefs.getProperty(PREFS_EXPFOLDER)));
        }
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            s = jfc.getSelectedFile().getAbsolutePath() + File.separator;
        }

        if (s == null) {
            return;
        }

        prefs.setProperty(PREFS_EXPFOLDER, s);
        savePrefs();
        expdirTextField.setText(s);
        updateScoreTab();
    }//GEN-LAST:event_expButtonActionPerformed

    /**
     *
     */
    public class scanLoader extends SwingWorker<Object, Object> {

        @Override
        protected Object doInBackground() throws Exception {
            scanLoad();
            return "";
        }

        @Override
        protected void done() {
            super.done();
        }
    }

    /**
     *
     */
    public void scanLoad() {
        if (oToCrop != null && oToCrop.getWindow() != null) {
            oToCrop.getWindow().close();
        }
        if (cropA != null && cropA.getWindow() != null) {
            cropA.getWindow().close();
        }
        if (cropB != null && cropB.getWindow() != null) {
            cropB.getWindow().close();
        }
        if (cropC != null && cropC.getWindow() != null) {
            cropC.getWindow().close();
        }
        if (cropD != null && cropD.getWindow() != null) {
            cropD.getWindow().close();
        }
        currScanFile = (File) scanFileJList.getSelectedValue();
        if (currScanFile == null) {
            return;
        }

        messageText.append("\nLoading...");
        toCrop = new Opener().openImage(currScanFile.getAbsolutePath());
        messageText.append("done.\nPreparing image...");
        tcCp = new ColorProcessor(toCrop.getImage());
        oToCrop = new ImagePlus(currScanFile.getName(), tcCp);
        setupCompositeImage();
        messageText.append("done.");
        setScanBaseNames(currScanFile);
        updateScanNames();
    }

    /**
     *
     */
    public void setupCompositeImage() {
        if (oToCrop == null) {
            return;
        }

        int layout = scanLayout.getSelectedIndex();
        int w = toCrop.getWidth();
        int h = toCrop.getHeight();

        GeneralPath path = new GeneralPath();

        if (layout == 1 || layout == 3) {
            path.moveTo(0, (double) h / 2);
            path.lineTo((double) w, (double) h / 2);
        }
        if (layout == 2 || layout == 3) {
            path.moveTo((double) w / 2, 0);
            path.lineTo((double) w / 2, h);
        }

        oToCrop.show();
        if (oToCrop.getWindow() == null) {
            return;
        }
        oToCrop.getWindow().setLocation(this.getWidth(), 0);
        oToCrop.getWindow().setIconImage(balloonImage);

        Font f = new Font("SansSerif", Font.BOLD, w * 6 / 100);

        int a = Integer.parseInt(scanAComboBox.getSelectedItem().toString())
                + Integer.parseInt(scanOffsetJComboBox.getSelectedItem().toString());
        int b = Integer.parseInt(scanBComboBox.getSelectedItem().toString())
                + Integer.parseInt(scanOffsetJComboBox.getSelectedItem().toString());
        int c = Integer.parseInt(scanCComboBox.getSelectedItem().toString())
                + Integer.parseInt(scanOffsetJComboBox.getSelectedItem().toString());
        int d = Integer.parseInt(scanDComboBox.getSelectedItem().toString())
                + Integer.parseInt(scanOffsetJComboBox.getSelectedItem().toString());

        String plateA = "     A\nPlate " + a;
        String plateB = "     B\nPlate " + b;
        String plateC = "     C\nPlate " + c;
        String plateD = "     D\nPlate " + d;

        Overlay o;
        o = new Overlay();
        ShapeRoi sr = new ShapeRoi(path);
        sr.setStrokeColor(Color.yellow);
        o.add(sr);
        TextRoi tr;

        switch (layout) {
            case 0:
                tr = new TextRoi(w * 4 / 10, h * 9 / 20, plateB, f);
                tr.setStrokeColor(Color.yellow);
                o.add(tr);
                break;
            case 1:
                tr = new TextRoi(w * 4 / 10, h / 5, plateB, f);
                tr.setStrokeColor(Color.yellow);
                o.add(tr);
                tr = new TextRoi(w * 4 / 10, h * 7 / 10, plateD, f);
                tr.setStrokeColor(Color.yellow);
                o.add(tr);
                break;
            case 2:
                tr = new TextRoi(w / 6, h * 9 / 20, plateB, f);
                tr.setStrokeColor(Color.yellow);
                o.add(tr);
                tr = new TextRoi(w * 2 / 3, h * 9 / 20, plateA, f);
                tr.setStrokeColor(Color.yellow);
                o.add(tr);
                break;
            case 3:
                tr = new TextRoi((w / 6), (h / 5), plateA, f);
                tr.setStrokeColor(Color.yellow);
                o.add(tr);
                tr = new TextRoi((int) (w / 1.5), (h / 5), plateB, f);
                tr.setStrokeColor(Color.yellow);
                o.add(tr);
                tr = new TextRoi(w / 6, h * 7 / 10, plateC, f);
                tr.setStrokeColor(Color.yellow);
                o.add(tr);

                tr = new TextRoi((w * 2 / 3), h * 7 / 10, plateD, f);
                tr.setStrokeColor(Color.yellow);
                o.add(tr);
                break;
        }

        oToCrop.setOverlay(o);
    }

    /**
     *
     * @param f
     */
    public void setScanBaseNames(File f) {
        String ss = f.getName().toLowerCase(), t;
        int j, k;
        j = ss.lastIndexOf(".");
        k = ss.lastIndexOf(SET);

        int x = ss.lastIndexOf("[");
        int y = ss.lastIndexOf("]");

        // Offset?
        if (x != -1 && y != -1 && y > x) {
            String off = getOnlyNumerics(ss.substring(x, y));
            scanOffsetJComboBox.setSelectedIndex(Integer.parseInt(off));
        } else {
            scanOffsetJComboBox.setSelectedIndex(0);
        }

        if (j != -1) {
            scanbaseTextField.setText(f.getName().substring(0, j));
        }

        if (j != -1 && k != -1 && j > k) {
            if (x != -1 && x < j && x > k) {
                j = x;
            }

            t = ss.substring(k + 2, j + 1);
            scansetComboBox.setSelectedIndex(Integer.parseInt(getOnlyNumerics(t)) - 1);
            t = f.getName().substring(0, j - 1);
            char tt = t.charAt(t.length() - 1);
            while (tt == ' ' || tt == '_' || tt == '-') {
                t = t.substring(0, t.length() - 1);
                tt = t.charAt(t.length() - 1);
            }
            scanbaseTextField.setText(t);
        }

        if (k != -1) {
            t = f.getName().substring(0, k);
            char tt = t.charAt(t.length() - 1);
            while (tt == ' ' || tt == '_' || tt == '-') {
                t = t.substring(0, t.length() - 1);
                tt = t.charAt(t.length() - 1);
            }
            scanbaseTextField.setText(t);
        }
    }

    /**
     *
     */
    public void updateScanNames() {
        if (scanAutoPlateNameJCheckBox.isSelected()) {
            String base = scanbaseTextField.getText();
            int a = Integer.parseInt(scanAComboBox.getSelectedItem().toString())
                    + Integer.parseInt(scanOffsetJComboBox.getSelectedItem().toString());
            int b = Integer.parseInt(scanBComboBox.getSelectedItem().toString())
                    + Integer.parseInt(scanOffsetJComboBox.getSelectedItem().toString());
            int c = Integer.parseInt(scanCComboBox.getSelectedItem().toString())
                    + Integer.parseInt(scanOffsetJComboBox.getSelectedItem().toString());
            int d = Integer.parseInt(scanDComboBox.getSelectedItem().toString())
                    + Integer.parseInt(scanOffsetJComboBox.getSelectedItem().toString());

            scanaTextField.setText(base + "_set-" + scansetComboBox.getSelectedItem()
                    + "_plate-" + a);
            scanBTextField.setText(base + "_set-" + scansetComboBox.getSelectedItem()
                    + "_plate-" + b);
            scanCTextField.setText(base + "_set-" + scansetComboBox.getSelectedItem()
                    + "_plate-" + c);
            scanDTextField.setText(base + "_set-" + scansetComboBox.getSelectedItem()
                    + "_plate-" + d);
            setupCompositeImage();
        }
    }

    private void scanprocessButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanprocessButtonActionPerformed
        doScanProcess();
    }

    public void doScanProcess() {
        String outdir = scanFolderJTextField.getText()
                + scansubfolderTextField.getText() + File.separator;
        new File(outdir).mkdir();
        messageText.append("\nProcessing images...");
        ImageProcessor ip = toCrop.getProcessor();
        ImageProcessor nip;
        int x = ip.getWidth();
        int y = ip.getHeight();
        FileSaver f;
        int xd = 0;
        int yd = 0;
        int xOff = 0;
        int yOff = 0;
        int layout = scanLayout.getSelectedIndex();
        switch (layout) {
            case 0:
                xd = x;
                yd = y;
                xOff = 0;
                yOff = 0;
                break;
            case 1:
                xd = x;
                yd = y / 2;
                xOff = 0;
                yOff = y / 2;
                break;
            case 2:
                xd = x / 2;
                yd = y;
                xOff = x / 2;
                yOff = 0;
                break;
            case 3:
                xd = x / 2;
                yd = y / 2;
                xOff = x / 2;
                yOff = y / 2;
                break;
        }
        if (scansaveACheckBox.isSelected() && scansaveACheckBox.isEnabled()) {
            ip.setRoi(0, 0, xd, yd);
            nip = ip.crop();
            if (scanrotateComboBox.getSelectedIndex() == 0) {
                nip = nip.rotateRight();
            }
            if (scanrotateComboBox.getSelectedIndex() == 1) {
                nip = nip.rotateLeft();
            }
            if (scanrotateComboBox.getSelectedIndex() == 2) {
                nip = nip.rotateRight();
                nip = nip.rotateRight();
            }
            if (scangrayCheckBox.isSelected()) {
                TypeConverter tp = new TypeConverter(nip, false);
                nip = tp.convertToByte();
            }
            if (scanshrinkComboBox.getSelectedIndex() == 1) {
                nip.setInterpolationMethod(ImageProcessor.BICUBIC);
                nip = nip.resize(nip.getWidth() / 2);
            }
            if (scanshrinkComboBox.getSelectedIndex() == 2) {
                nip.setInterpolationMethod(ImageProcessor.BICUBIC);
                nip = nip.resize(nip.getWidth() / 4);
            }
            cropA = new ImagePlus("A", nip);
            FileSaver.setJpegQuality(100);
            f = new FileSaver(cropA);
            f.saveAsJpeg(outdir + scanaTextField.getText() + ".jpg");
        }
        if (scansaveBCheckBox.isSelected() && scansaveBCheckBox.isEnabled()) {
            ip.resetRoi();
            ip.setRoi(xOff, 0, xd, yd);
            nip = ip.crop();
            if (scanrotateComboBox.getSelectedIndex() == 0) {
                nip = nip.rotateRight();
            }
            if (scanrotateComboBox.getSelectedIndex() == 1) {
                nip = nip.rotateLeft();
            }
            if (scanrotateComboBox.getSelectedIndex() == 2) {
                nip = nip.rotateRight();
                nip = nip.rotateRight();
            }
            if (scangrayCheckBox.isSelected()) {
                TypeConverter tp = new TypeConverter(nip, false);
                nip = tp.convertToByte();
            }
            if (scanshrinkComboBox.getSelectedIndex() == 1) {
                nip.setInterpolationMethod(ImageProcessor.BICUBIC);
                nip = nip.resize(nip.getWidth() / 2);
            }
            if (scanshrinkComboBox.getSelectedIndex() == 2) {
                nip.setInterpolationMethod(ImageProcessor.BICUBIC);
                nip = nip.resize(nip.getWidth() / 4);
            }
            cropB = new ImagePlus("B", nip);
            FileSaver.setJpegQuality(100);
            f = new FileSaver(cropB);
            f.saveAsJpeg(outdir + scanBTextField.getText() + ".jpg");
        }
        if (scansaveCCheckBox.isSelected() && scansaveCCheckBox.isEnabled()) {
            ip.resetRoi();
            ip.setRoi(0, y / 2, x / 2, y / 2);
            nip = ip.crop();
            if (scanrotateComboBox.getSelectedIndex() == 0) {
                nip = nip.rotateRight();
            }
            if (scanrotateComboBox.getSelectedIndex() == 1) {
                nip = nip.rotateLeft();
            }
            if (scanrotateComboBox.getSelectedIndex() == 2) {
                nip = nip.rotateRight();
                nip = nip.rotateRight();
            }
            if (scangrayCheckBox.isSelected()) {
                TypeConverter tp = new TypeConverter(nip, false);
                nip = tp.convertToByte();
            }
            if (scanshrinkComboBox.getSelectedIndex() == 1) {
                nip.setInterpolationMethod(ImageProcessor.BICUBIC);
                nip = nip.resize(nip.getWidth() / 2);
            }
            if (scanshrinkComboBox.getSelectedIndex() == 2) {
                nip.setInterpolationMethod(ImageProcessor.BICUBIC);
                nip = nip.resize(nip.getWidth() / 4);
            }
            cropC = new ImagePlus("C", nip);
            FileSaver.setJpegQuality(100);
            f = new FileSaver(cropC);
            f.saveAsJpeg(outdir + scanCTextField.getText() + ".jpg");
        }
        if (scansaveDCheckBox.isSelected() && scansaveDCheckBox.isEnabled()) {
            ip.resetRoi();
            ip.setRoi(xOff, yOff, xd, yd);
            nip = ip.crop();
            if (scanrotateComboBox.getSelectedIndex() == 0) {
                nip = nip.rotateRight();
            }
            if (scanrotateComboBox.getSelectedIndex() == 1) {
                nip = nip.rotateLeft();
            }
            if (scanrotateComboBox.getSelectedIndex() == 2) {
                nip = nip.rotateRight();
                nip = nip.rotateRight();
            }
            if (scangrayCheckBox.isSelected()) {
                TypeConverter tp = new TypeConverter(nip, false);
                nip = tp.convertToByte();
            }
            if (scanshrinkComboBox.getSelectedIndex() == 1) {
                nip.setInterpolationMethod(ImageProcessor.BICUBIC);
                nip = nip.resize(nip.getWidth() / 2);
            }
            if (scanshrinkComboBox.getSelectedIndex() == 2) {
                nip.setInterpolationMethod(ImageProcessor.BICUBIC);
                nip = nip.resize(nip.getWidth() / 4);
            }
            cropD = new ImagePlus("D", nip);
            FileSaver.setJpegQuality(100);
            f = new FileSaver(cropD);
            f.saveAsJpeg(outdir + scanDTextField.getText() + ".jpg");
        }
        if (!scancloseCheckBox.isSelected()) {
            if (cropA != null) {
                cropA.show();
            }
            if (cropB != null) {
                cropB.show();
            }
            if (cropC != null) {
                cropC.show();
            }
            if (cropD != null) {
                cropD.show();
            }
        }
        messageText.append("done.");
    }//GEN-LAST:event_scanprocessButtonActionPerformed
    private void scanchoosefolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanchoosefolderButtonActionPerformed
        JFileChooser jfc = new JFileChooser();
        String s = null;

        if (prefs.getProperty(PREFS_SCANFOLDER) != null) {
            jfc.setCurrentDirectory(new File(prefs.getProperty(PREFS_SCANFOLDER)));
        }
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            s = jfc.getSelectedFile().getAbsolutePath() + File.separator;
        }

        if (s == null) {
            return;
        }
        prefs.setProperty(PREFS_SCANFOLDER, s);
        savePrefs();
        scanFolderJTextField.setText(s);
        File folder = new File(s);
        File[] listoffiles = folder.listFiles();

        ArrayList<File> f = new ArrayList<>();
        int cnt = 0;
        for (File myF : listoffiles) {
            String fn = myF.getName().toLowerCase();
            if ((fn.endsWith(".jpg") || fn.endsWith(".png") || fn.endsWith(".tif")
                    || fn.endsWith(".jpeg")) && !fn.startsWith(".")) {
                f.add(myF);
                cnt++;
            }
        }
        scanFileJList.removeAll();
        scanFileJList.setListData((File[]) f.toArray(new File[f.size()]));
        scanFileJList.setCellRenderer(new fileList2CellRenderer());
        if (cnt > 0) {
            messageText.append("\nFound ").append(cnt).append(" image file").append(cnt > 1 ? "s" : "").append(".");
        }

    }//GEN-LAST:event_scanchoosefolderButtonActionPerformed

    private void scanAComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanAComboBoxActionPerformed
        updateScanNames();
        prefs.setProperty(PREFS_SCAN_PLATE_A, scanAComboBox.getSelectedItem().toString());
        savePrefs();
    }//GEN-LAST:event_scanAComboBoxActionPerformed
    private void scanBComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanBComboBoxActionPerformed
        updateScanNames();
        prefs.setProperty(PREFS_SCAN_PLATE_B, scanBComboBox.getSelectedItem().toString());
        savePrefs();
    }//GEN-LAST:event_scanBComboBoxActionPerformed
    private void scanCComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanCComboBoxActionPerformed
        updateScanNames();
        prefs.setProperty(PREFS_SCAN_PLATE_C, scanCComboBox.getSelectedItem().toString());
        savePrefs();
    }//GEN-LAST:event_scanCComboBoxActionPerformed
    private void scanDComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanDComboBoxActionPerformed
        updateScanNames();
        prefs.setProperty(PREFS_SCAN_PLATE_D, scanDComboBox.getSelectedItem().toString());
        savePrefs();
    }//GEN-LAST:event_scanDComboBoxActionPerformed
    private void scansetComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scansetComboBoxActionPerformed
        updateScanNames();
    }//GEN-LAST:event_scansetComboBoxActionPerformed

    /**
     *
     */
    public class processWorker extends SwingWorker<String, Void> {

        @Override
        public String doInBackground() {
            scancloseCheckBox.setSelected(true);

            int indices[] = scanFileJList.getSelectedIndices();
            if (indices.length == 0) {
                return "";
            }

            messageText.append("\nStarting to process ").append(indices.length).append(" files.");
            progress p = new progress();
            p.jProgressBar1.setMaximum(indices.length);
            p.setVisible(true);

            int cnt = 1;
            for (int i : indices) {
                p.jLabel1.setText("Processing " + cnt + " of " + indices.length + " images.");
                p.jProgressBar1.setValue(cnt);
                if (isCancelled()) {
                    return "";
                }
                scanFileJList.setSelectedIndex(i);
                scanLoad();
                if (isCancelled()) {
                    return "";
                }
                doScanProcess();
                cnt++;
            }

            if (oToCrop != null && oToCrop.getWindow() != null) {
                oToCrop.getWindow().close();
            }
            messageText.append("\nFinished processing files.");
            doSetFolder(scanFolderJTextField.getText()
                    + scansubfolderTextField.getText() + File.separator);
            p.dispose();
            pw = null;
            return "";
        }

        @Override
        public void done() {
            pw = null;
        }
    }
    private void scanprocessallButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanprocessallButtonActionPerformed
        if (pw != null) {
            pw.cancel(true);
            pw = null;
            return;
        }
        pw = new processWorker();
        pw.execute();
    }//GEN-LAST:event_scanprocessallButtonActionPerformed

    /**
     *
     */
    public class imageLoader extends SwingWorker<String, Void> {

        @Override
        public String doInBackground() {
            File f = (File) imageFileJList.getSelectedValue();
            imageLoad(f, true);
            return "";
        }

        @Override
        protected void done() {
            super.done();
        }
    }

    /**
     *
     */
    public class autoWorker extends SwingWorker<String, Void> {

        @Override
        public String doInBackground() {
            long t1 = System.currentTimeMillis();
            int k = imageFileJList.getSelectedValuesList().size();
            if (k > 1) {
                choosefolderButton.setEnabled(false);
                thresholdButton.setEnabled(false);
                quantButton.setEnabled(false);
                saveButton.setEnabled(false);
                imageFileJList.setEnabled(false);
            }

            if (k == 0) {
                return "";
            }

            progress p = new progress();
            if (k > 1) {
                p.setVisible(true);
            }
            p.jProgressBar1.setMaximum(k);

            for (int i = 0; i < k; i++) {
                p.jLabel1.setText("Processing: " + (i + 1) + " of " + k);
                if (k > 1) {
                    p.setVisible(true);
                }

                if (!isCancelled()) {
                    File f = (File) imageFileJList.getSelectedValuesList().toArray()[i];
                    rotated = false;
                    rethresh = 0;
                    bestSpots = -1;
                    currFolder = f.getParentFile();
                    imageLoad(f, true);

                    if (autoThreshJCheckBox.isSelected()) {

                        doThresh();

                        if (threshed && autoGridJCheckBox.isSelected()) {
                            doAutoGrid();

                            if (gridded && autoQuantJCheckBox.isSelected()) {
                                doQuant();
                                if (oIm != null && autoSaveJCheckBox.isSelected()) {
                                    p.jProgressBar1.setValue(i + 1);
                                    doSave(false);
                                }
                            }
                        }
                    }

                    messageText.append("\n---------------------------------\n");
                }
            }

            p.dispose();
            if (fullAutoFail > 0) {
                messageText.append("\nWARNING: AutoGrid failed ").append(fullAutoFail).append(" times.\nSee log file for details");
            } else {
                messageText.append("\nNo AutoGrid failures.");
            }
            try {
                String s = messageFrame.messageJTextArea.getText();
                String outFile = currFolder.getAbsolutePath() + File.separator + File.separator + AUTO_ANALYZE_LOG;
                try (BufferedWriter out = new BufferedWriter(new FileWriter(outFile))) {
                    out.write(s);
                }
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
                choosefolderButton.setEnabled(true);
                thresholdButton.setEnabled(true);
                quantButton.setEnabled(true);
                saveButton.setEnabled(true);
                imageFileJList.setEnabled(true);
            }
            fullAuto = false;
            choosefolderButton.setEnabled(true);
            thresholdButton.setEnabled(true);
            quantButton.setEnabled(true);
            saveButton.setEnabled(true);
            fullautoButton.setEnabled(true);
            imageFileJList.setEnabled(true);
            messageText.append("\nProcessed ").append(k).append(" files in ").append((int) ((System.currentTimeMillis() - t1) / 1000)).append(" seconds.");
            if (imWin != null) {
            }
            return "";
        }

        @Override
        protected void done() {
            choosefolderButton.setEnabled(true);
            thresholdButton.setEnabled(true);
            quantButton.setEnabled(true);
            saveButton.setEnabled(true);
            imageFileJList.setEnabled(true);
            imageFileJList.clearSelection();
        }
    }

    /**
     *
     */
    public void saveAutoCheckImage() {
        if (oIm != null) {
            String outdir = currFolder.getAbsolutePath() + File.separator + File.separator + "autocheck_images";
            File fff = new File(outdir);
            if (!fff.exists()) {
                fff.mkdir();
            }
            float w = oIm.getWidth() / 800;
            Roi[] r = oIm.getOverlay().toArray();
            for (Roi rr : r) {
                rr.setStrokeWidth(w);
            }
            ImagePlus tmpIm = oIm.flatten();
            ImageProcessor ip = tmpIm.getProcessor();
            ip.setInterpolationMethod(ImageProcessor.BICUBIC);
            ImagePlus tmpIm2 = new ImagePlus("", ip.resize(800));
            FileSaver f = new FileSaver(tmpIm2);
            FileSaver.setJpegQuality(100);
            File ff = new File(outdir + File.separator + currFile.getName());
            if (ff.exists()) {
                ff.delete();
            }
            f.saveAsJpeg(ff.getAbsolutePath());
            tmpIm.changes = false;
            tmpIm.close();
            tmpIm2.changes = false;
            tmpIm2.close();
            for (Roi rr : r) {
                rr.setStrokeWidth(0);
            }
        }
    }

    private void fullautoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullautoButtonActionPerformed
        if (fullAuto) {
            if (fullAutoWorker != null) {
                fullAutoWorker.cancel(inverted);
                fullAutoWorker = null;
                fullAuto = false;
            }
            return;
        }
        fullAuto = true;

        int k = imageFileJList.getSelectedIndices().length;

        // Are we processing multiple files? If so, automatically process everything.
        if (k > 1) {
            autoGridJCheckBox.setSelected(true);
            autoNameJCheckBox.setSelected(true);
            autoQuantJCheckBox.setSelected(true);
            autoThreshJCheckBox.setSelected(true);
            autoSaveJCheckBox.setSelected(true);
        }

        fullAutoFail = 0;
        messageFrame.messageJTextArea.setText("");
        fullAutoWorker = new autoWorker();
        fullAutoWorker.execute();

    }//GEN-LAST:event_fullautoButtonActionPerformed

    private void viewLogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewLogButtonActionPerformed
        TextWindow t = new TextWindow(currFolder.getAbsolutePath() + File.separator + AUTO_ANALYZE_LOG, 500, 600);
        t.setTitle(currFolder.getAbsolutePath() + File.separator + AUTO_ANALYZE_LOG);
    }//GEN-LAST:event_viewLogButtonActionPerformed
    private void autocheckforwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autocheckforwardButtonActionPerformed
        if (autoCheckPos == -1) {
            autoCheckPos = 0;
        } else {
            autoCheckPos++;
        }
        String s = currFolder.getAbsolutePath() + File.separator + "autocheck_images" + File.separator;
        File folder = new File(s);
        File[] listoffiles = folder.listFiles();
        if (listoffiles == null) {
            return;
        }
        String[] jpgfiles = new String[listoffiles.length];
        int cnt = 0;
        for (File f : listoffiles) {
            String ss = f.getName();
            String ext = ss.substring(ss.lastIndexOf('.') + 1, ss.length()).toLowerCase();
            if (ext.equals("jpg")) {
                jpgfiles[cnt] = f.getName();
                cnt++;
            }
        }
        String[] jpgfiles2 = Arrays.copyOf(jpgfiles, cnt);
        if (autoCheckPos == jpgfiles2.length) {
            autoCheckPos = 0;
        }
        if (autoTmp != null && autoTmp.getWindow() != null) {
            autoTmp.getWindow().close();
        }
        autoTmp = new Opener().openImage(s + jpgfiles2[autoCheckPos]);
        autoTmp.show();
        autoTmp.getWindow().setLocation(this.getWidth(), 0);
        autoTmp.getWindow().setTitle(autoTmp.getWindow().getTitle() + " - " + (autoCheckPos + 1) + "/" + jpgfiles2.length);
    }//GEN-LAST:event_autocheckforwardButtonActionPerformed
    private void autocheckbackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autocheckbackButtonActionPerformed
        if (autoCheckPos == -1) {
            autoCheckPos = 0;
        } else {
            autoCheckPos--;
        }
        String s = currFolder.getAbsolutePath() + File.separator + "autocheck_images" + File.separator;
        File folder = new File(s);
        File[] listoffiles = folder.listFiles();
        if (listoffiles == null) {
            return;
        }
        String[] jpgfiles = new String[listoffiles.length];
        int cnt = 0;
        for (File f : listoffiles) {
            String ss = f.getName();
            String ext = ss.substring(ss.lastIndexOf('.') + 1, ss.length()).toLowerCase();
            if (ext.equals("jpg")) {
                jpgfiles[cnt] = f.getName();
                cnt++;
            }
        }
        jpgfiles = Arrays.copyOf(jpgfiles, cnt);
        if (autoCheckPos == -1) {
            autoCheckPos = jpgfiles.length - 1;
        }
        if (autoTmp != null && autoTmp.getWindow() != null) {
            autoTmp.getWindow().close();
        }
        autoTmp = new Opener().openImage(s + jpgfiles[autoCheckPos]);
        autoTmp.show();
        autoTmp.getWindow().setLocation(this.getWidth(), 0);
        autoTmp.getWindow().setTitle(autoTmp.getWindow().getTitle() + " - "
                + (autoCheckPos + 1) + "/" + jpgfiles.length);


    }//GEN-LAST:event_autocheckbackButtonActionPerformed
    private void scansaveCCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scansaveCCheckBoxActionPerformed
        if (scansaveCCheckBox.isSelected()) {
            scanCTextField.setEnabled(true);
        } else {
            scanCTextField.setEnabled(false);
        }
    }//GEN-LAST:event_scansaveCCheckBoxActionPerformed

    /**
     *
     */
    public void updateSlider() {
        int i = -1;
        try {
            i = Integer.parseInt(imageThreshManualJTextField.getText());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        if (i > -1 && i < 256) {
            threshJSlider.setValue(i);
        }
    }

    private void analysisLoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisLoadButtonActionPerformed
        MultiFileChooser mfc = new MultiFileChooser();
        mfc.b = this;
        mfc.setIconImage(balloonImage);
        JFileChooser jfc = mfc.jFileChooser1;

        if (prefs.containsKey(PREFS_ANALYSISFOLDER)) {
            jfc.setCurrentDirectory(new File(prefs.getProperty(PREFS_ANALYSISFOLDER)));
        }

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Scored Data Sets (*.txt)", "txt");
        mfc.setTitle("Select files to include");
        jfc.setFileFilter(filter);
        jfc.setMultiSelectionEnabled(true);
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);

        mfc.setVisible(true);
    }

    public void loadAnalysisFiles(File[] f) {
        StringBuilder sb = new StringBuilder();
        for (File ff : f) {
            sb.append(ff.getName()).append(";");
        }
//        System.out.println("Files: " + sb);
        prefs.setProperty(PREFS_ANALYSISFOLDER, f[0].getParent());
        savePrefs();
        String orfs[][][];
        String genes[][][];
        analysisData ad;
        HashSet<File> goodFiles = new HashSet<>();

        for (File myF : f) {
            ad = null;
            int i = myF.getName().lastIndexOf(".");
            String expname = "";
            if (i != -1) {
                int s = 0;
                int i1 = myF.getName().lastIndexOf("_set-");
                int i2 = myF.getName().lastIndexOf(".txt");
                if (i1 == -1 || i2 == -1) {
                    continue;
                }

                expname = myF.getName().substring(0, i1);
                if (i1 != -1 && i2 != -1) {
                    try {
                        s = Integer.parseInt(myF.getName().substring(i1 + 5, i2));
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                    if (!aD.keySet().contains(expname) && s > 0) {
                        aD.put(expname, new analysisData());
                        ad = aD.get(expname);
                        ad.setMinSpotSize(Double.parseDouble(minSpotSizeJTextField.getText()));
                        ad.setMaxSpotSize(Double.parseDouble(maxSpotSizeJTextField.getText()));
                        ad.setSickSpotSize(Double.parseDouble(sickCutOffTextJField.getText()));
                        ad.setLowCutOff(Double.parseDouble(lowCutOffJTextField.getText()));
                        ad.setHighCutOff(Double.parseDouble(upperCutOffJTextField.getText()));
                        ad.setSickFilterType(analysisSickFliterJComboBox.getSelectedIndex());
                    }
                    ad = aD.get(expname);
                }
                try {
                    if (ad != null) {
                        if (ad.getSets() < s) {
                            ad.setSets(s);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
            BufferedReader in;
            boolean baddata = false;
            try {
                int p, r, c;
                in = new BufferedReader(new FileReader(myF));
                in.readLine();
                String s;
                while ((s = in.readLine()) != null) {
                    String[] d = s.split("\t");
                    p = Integer.parseInt(d[3]);
                    r = Integer.parseInt(d[4]);
                    c = Integer.parseInt(d[5]);
                    if (ad != null) {
                        if (ad.getPlates() < p) {
                            ad.setPlates(p);
                        }
                        if (ad.getRows() < r) {
                            ad.setRows(r);
                        }
                        if (ad.getCols() < c) {
                            ad.setCols(c);
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println(e.getMessage());
                baddata = true;
            }
            if (!baddata) {
                goodFiles.add(myF);
//                orfs = new String[ad.getPlates() + 1][ad.getRows() + 1][ad.getCols() + 1];
//                genes = new String[ad.getPlates() + 1][ad.getRows() + 1][ad.getCols() + 1];
                if (ad != null) {
                    ad.initData();
                    dataTable dt = new dataTable();
                    dt.setIconImage(balloonImage);
                    ad.setDt(dt);
                    aD.put(expname, ad);
                }
            }
        }

        for (File myF : goodFiles) {
            orfs = new String[1][1][1];
            genes = new String[1][1][1];
            ad = null;
            int i = myF.getName().lastIndexOf(".");
            int s = 0;
            if (i != -1) {
                int i1 = myF.getName().lastIndexOf("_set-");
                int i2 = myF.getName().lastIndexOf(".txt");
                if (i1 != -1 && i2 != -1) {
                    s = Integer.parseInt(myF.getName().substring(i1 + 5, i2));
                }
            }
            String expname = "";
            if (i != -1) {
                int i1 = myF.getName().lastIndexOf("_set-");
                if (i1 == -1) {
                    return;
                }
                expname = myF.getName().substring(0, i1);
            }
            BufferedReader in;
            try {
                int p, r, c;
                in = new BufferedReader(new FileReader(myF));
                if (expname.length() > 0) {
                    ad = aD.get(expname);
                }
                if (ad == null) {
                    continue;
                }
                in.readLine();
                String ss;
                orfs = new String[ad.getPlates() + 1][ad.getRows() + 1][ad.getCols() + 1];
                genes = new String[ad.getPlates() + 1][ad.getRows() + 1][ad.getCols() + 1];
                while ((ss = in.readLine()) != null) {
                    String[] d = ss.split("\t");
                    p = Integer.parseInt(d[3]);
                    r = Integer.parseInt(d[4]);
                    c = Integer.parseInt(d[5]);
                    orfs[p][r][c] = d[1];
                    if (analysisOverrideKeyFileCheckBox.isSelected()
                            && allSGDInfo.get(d[1]) != null && allSGDInfo.get(d[1]).gene != null) {
                        genes[p][r][c] = allSGDInfo.get(d[1]).gene;
                    } else {
                        genes[p][r][c] = d[2];
                    }
                    Double temp = Double.parseDouble(d[11]);
                    ad.ctrlSpots[s][p][r][c] = temp;
                    temp = Double.parseDouble(d[13]);
                    ad.expSpots[s][p][r][c] = temp;
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println(e.getMessage());
            }

            dataTable dt = new dataTable();

            if (ad != null) {
                dt = ad.getDt();
            }

            try {
                dt.setMinSpotSize(Double.parseDouble(minSpotSizeJTextField.getText()));
                dt.setSickCutOff(Double.parseDouble(sickCutOffTextJField.getText()));
            } catch (NumberFormatException e) {
                System.out.println(e.getLocalizedMessage());
            }

            Integer[] comar = new Integer[ad.getSets()];
            for (int com = 0; com < ad.getSets(); com++) {
                comar[com] = ad.getSets() - com;
            }

            dt.lcHitsJComboBox.setModel(new DefaultComboBoxModel<>(comar));
            dt.hcHitsJComboBox.setModel(new DefaultComboBoxModel<>(comar));

            Double d;
            if (ad != null) {
                d = ad.getLowCutOff();
                dt.setLowCutOff(d);
                dt.lowCutOffJTextField.setText(d.toString());
                d = ad.getHighCutOff();
                dt.setHighCutOff(d);
                dt.highCutOffJTextField.setText(d.toString());
                d = ad.getMinSpotSize();
                dt.setMinSpotSize(d);
                dt.minSpotSizeTextField1.setText(d.toString());
                d = ad.getMaxSpotSize();
                dt.setMaxSpotSize(d);
                dt.maxSpotSizeTextField1.setText(d.toString());
                d = ad.getSickSpotSize();
                dt.setSickCutOff(d);
                dt.sickCutOffTextField1.setText(d.toString());

                int j = ad.getSickFilterType();
                dt.setSickFilterType(j);
                dt.analysisSickFliterComboBox1.setSelectedIndex(j);
                dt.setFileDate(new Date(myF.lastModified()));
                dt.setupData(this, ad.getCtrlSpots(), ad.getExpSpots(), ad.getSets(), ad.getPlates(),
                        ad.getRows(), ad.getCols(), orfs, genes);
                j = myF.getName().lastIndexOf("_set");
                if (j > -1) {
                    dt.setTitle(myF.getName().substring(0, j));
                } else {
                    try {
                        dt.setTitle(myF.getName().substring(0, f[0].getName().length() - 10));
                    } catch (Exception e) {
                        dt.setTitle(myF.getName());
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                if (analysisOpenDataTablesJCheckBox.isSelected()) {
                    dt.setVisible(true);
                }
                ad.setDt(dt);
                dt.myAD = ad;

                aD.put(expname, ad);
                dataTables.add(dt);
                currentDT = dt;
                boolean found = false;
                for (int k = 0; k
                        < dataTablesComboBox.getItemCount(); k++) {
                    if (dataTablesComboBox.getItemAt(k) == dt) {
                        found = true;
                    }
                }
                if (!found) {
                    dataTablesComboBox.addItem(dt);
                }
            }
        }
        dataTables.stream().map((dt0) -> {
            dt0.compareJMenu.removeAll();
            return dt0;
        }).forEach((dt0) -> {
            dataTables.stream().map((dt1) -> {
                final compMenu m = new compMenu();
                m.setText(dt1.getTitle());
                m.thisTable = dt0;
                m.compTable = dt1;
                return m;
            }).map((m) -> {
                dt0.compareJMenu.add(m);
                return m;
            }).forEach((m) -> {
                m.addActionListener((java.awt.event.ActionEvent evt) -> {
                    m.thisTable.doComp(m.compTable);
                });
            });
        });

        if (wizardModeJCheckBox.isSelected()) {
            wizardJFrame wfj = new wizardJFrame();
            wfj.setVisible(true);
            wfj.dt = currentDT;
        }

    }//GEN-LAST:event_analysisLoadButtonActionPerformed

    /**
     *
     */
    public class compMenu extends JMenuItem {

        /**
         *
         */
        public dataTable thisTable;

        /**
         *
         */
        public dataTable compTable;

        /**
         *
         */
        public void compMenu() {
        }
    }
    private void dataTablesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dataTablesComboBoxActionPerformed
//        dataTable ob = (dataTable) dataTablesComboBox.getSelectedItem();
//        if (ob != null) {
//            ob.setVisible(true);
//            currentDT = ob;
//        }
    }//GEN-LAST:event_dataTablesComboBoxActionPerformed

    /**
     *
     * @param d
     * @return
     */
    public static Double mean(Double[] d) {
        int i = 0;
        Double tot = 0d;
        for (Double dd : d) {
            if (dd != null) {
                tot += dd;
                i++;
            }
        }
        if (i > 0) {
            return tot / i;
        } else {
            return null;
        }
    }

    /**
     *
     * @param d
     * @return
     */
    public static Double stdev(Double[] d) {
        int i = 0;
        Double tot = 0d;
        Double mean = Balony.mean(d);
        if (mean == null) {
            mean = 0d;
        }
        for (Double dd : d) {
            if (dd != null) {
                tot += ((dd - mean) * (dd - mean));
                i++;
            }
        }
        if (i > 0) {
            return Math.sqrt(tot / i);
        } else {
            return 0d;
        }
    }

    /**
     *
     * @param d
     * @return
     */
    public static float sum(float[] d) {
        int i = 0;
        float tot = 0f;
        for (float dd : d) {
            tot += dd;
            i++;
        }
        if (i > 0) {
            return tot;
        } else {
            return 0f;
        }
    }

    private void analysisArrayComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisArrayComboBoxActionPerformed
        if (analysisArrayComboBox.getSelectedIndex() == 0) {
            try {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle("Choose a tab-delimited array definition file");
                jfc.showOpenDialog(currentDT);
                File f = jfc.getSelectedFile();

                if (f == null) {
                    return;
                }

                BufferedReader in = new BufferedReader(new FileReader(f));
                String s;
                int p, r, c;
                arrayDefinition ardef = new arrayDefinition();
                in.readLine();

                while ((s = in.readLine()) != null) {
                    String[] d = s.split("\t");
                    p = Integer.parseInt(d[0]);
                    r = Integer.parseInt(d[1]);
                    c = Integer.parseInt(d[2]);
                    if (ardef.getPlates() < p) {
                        ardef.setPlates(p);
                    }
                    if (ardef.getRows() < r) {
                        ardef.setRows(r);
                    }
                    if (ardef.getCols() < c) {
                        ardef.setCols(c);
                    }
                }

                int j = f.getName().lastIndexOf(".");

                if (j > 1) {
                    ardef.setName(f.getName().substring(0, j));
                } else {
                    ardef.setName(f.getName());
                }

                ardef.initData();
                in = new BufferedReader(new FileReader(f));
                in.readLine();

                while ((s = in.readLine()) != null) {
                    String[] d = s.split("\t");
                    if (d.length > 3) {
                        p = Integer.parseInt(d[0]);
                        r = Integer.parseInt(d[1]);
                        c = Integer.parseInt(d[2]);
                        ardef.getOrf()[p][r][c] = d[3];
                    }
                }

                arrayDefs.add(ardef);
                analysisArrayComboBox.addItem(ardef.getName());
            } catch (HeadlessException | IOException | NumberFormatException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }

    }//GEN-LAST:event_analysisArrayComboBoxActionPerformed

    private void saveScoreAvgButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveScoreAvgButtonActionPerformed

        doAddCtrl();
        doAddExp();

        if (scoreByArrayPosRadioButton.isSelected()) {
            if (scorenameTextField.getText() == null || scorenameTextField.getText().length() == 0) {
                return;
            }

            JFileChooser jfc = new JFileChooser();
            String ss = null;

//        if (prefs.getProperty(PREFS_CTRLFOLDER) != null) {
//            jfc.setCurrentDirectory(new File(prefs.getProperty(PREFS_CTRLFOLDER)));
//        }
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                ss = jfc.getSelectedFile().getAbsolutePath() + File.separator;
            }

            if (ss == null) {
                return;
            }

            try {
                String outFile = ss + scorenameTextField.getText() + ".txt";
                try (BufferedWriter out = new BufferedWriter(new FileWriter(outFile))) {
                    out.write(SCORE_HEADER);
                    out.newLine();
                    for (int p = minCtrlPlate; p
                            <= maxCtrlPlate; p++) {
                        for (int i = 1; i
                                <= ctrlData.getRows(); i++) {
                            for (int j = 1; j
                                    <= ctrlData.getCols(); j++) {
                                out.write(scorenameTextField.getText() + "\t\t\t");
                                out.write(Integer.toString(p));
                                out.write("\t");
                                out.write(Integer.toString(i));
                                out.write("\t");
                                out.write(Integer.toString(j));
                                out.write("\t");
                                out.write("\t\t\t\t\t");
                                double d = 0d;
                                for (int s = minCtrlSet; s
                                        <= maxCtrlSet; s++) {
                                    d += ctrlData.getNormArea()[s][p][i][j];
                                }
                                d /= (maxCtrlSet - minCtrlSet + 1);
                                double d1 = d;
                                out.write(Double.toString(d));
                                double sum = 0d, sd;
                                if (maxCtrlSet > minCtrlSet) {
                                    for (int s = minCtrlSet; s
                                            <= maxCtrlSet; s++) {
                                        double v = ctrlData.getNormArea()[s][p][i][j] - d;
                                        sum += v * v;
                                    }
                                }
                                sd = Math.sqrt(sum / (maxCtrlSet - minCtrlSet + 1));
                                out.write("\t" + Double.toString(sd) + "\t");
                                d = 0d;
                                for (int s = minExpSet; s
                                        <= maxExpSet; s++) {
                                    d += expData.getNormArea()[s][p][i][j];
                                }
                                d /= (maxExpSet - minExpSet + 1);
                                out.write(Double.toString(d));
                                sum = 0d;
                                if (maxExpSet > minExpSet) {
                                    for (int s = minExpSet; s
                                            <= maxExpSet; s++) {
                                        double v = expData.getNormArea()[s][p][i][j] - d;
                                        sum += v * v;
                                    }
                                }
                                sd = Math.sqrt(sum / (maxCtrlSet - minCtrlSet + 1));
                                out.write("\t" + Double.toString(sd) + "\t");
                                out.write(Double.toString(d1 - d));
                                out.newLine();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
            messageText.append("\nScored data saved.");
        } else {
            if (keyGenes == null || keyGenes.length == 0) {
                messageText.append("\nKey File required for output by ORF.");
                return;
            }
            if (scorenameTextField.getText() == null || scorenameTextField.getText().length() == 0) {
                return;
            }

            JFileChooser jfc = new JFileChooser();
            String ss = null;

            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                ss = jfc.getSelectedFile().getAbsolutePath() + File.separator;
            }

            if (ss == null) {
                return;
            }

            HashMap<String, orfScores> cOrfs = new HashMap<>();
            HashMap<String, orfScores> eOrfs = new HashMap<>();
            for (int p = minCtrlPlate; p
                    <= maxCtrlPlate; p++) {
                for (int i = 1; i
                        <= ctrlData.getRows(); i++) {
                    for (int j = 1; j
                            <= ctrlData.getCols(); j++) {
                        try {
                            for (int s = minCtrlSet; s
                                    <= maxCtrlSet; s++) {
                                if (keyOrfs[p][i][j] != null) {
                                    String orf = keyOrfs[p][i][j];
                                    if (cOrfs.size() > 0 && cOrfs.keySet().contains(orf)) {
                                        orfScores os = cOrfs.get(orf);
                                        os.getVals().add(ctrlData.getNormArea()[s][p][i][j]);
                                        cOrfs.put(orf, os);
                                    } else {
                                        orfScores os = new orfScores();
                                        os.setVals(new ArrayList<>());
                                        os.setGene(keyGenes[p][i][j]);
                                        os.getVals().add(ctrlData.getNormArea()[s][p][i][j]);
                                        cOrfs.put(orf, os);
                                    }
                                }
                            }
                            for (int s = minExpSet; s
                                    <= maxExpSet; s++) {
                                if (keyOrfs[p][i][j] != null) {
                                    String orf = keyOrfs[p][i][j];
                                    if (eOrfs.size() > 0 && eOrfs.keySet().contains(orf)) {
                                        orfScores os = eOrfs.get(orf);
                                        os.getVals().add(expData.getNormArea()[s][p][i][j]);
                                        eOrfs.put(orf, os);
                                    } else {
                                        orfScores os = new orfScores();
                                        os.setVals(new ArrayList<>());
                                        os.setGene(keyGenes[p][i][j]);
                                        os.getVals().add(expData.getNormArea()[s][p][i][j]);
                                        eOrfs.put(orf, os);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                    }
                }
            }
            try {
                String outFile = ss + scorenameTextField.getText() + ".txt";
                try (BufferedWriter out = new BufferedWriter(new FileWriter(outFile))) {
                    out.write(SCORE_HEADER);
                    out.newLine();
                    TreeSet<String> allOrfs = new TreeSet<>(cOrfs.keySet());
                    for (String orf : allOrfs) {
                        out.write(scorenameTextField.getText() + "\t");
                        out.write(orf);
                        out.write("\t");
                        out.write(keyOrfList.get(orf));
                        out.write("\t\t\t\t\t\t\t\t\t");
                        ArrayList<Double> v = cOrfs.get(orf).getVals();
                        Double d1 = 0d;
                        for (Double vd : v) {
                            d1 += vd;
                        }
                        d1 /= v.size();
                        out.write(d1.toString());
                        out.write("\t");
                        double sum = 0d, sd;
                        if (v.size() > 1) {
                            for (Double dd : v) {
                                double dtmp = dd - d1;
                                sum += dtmp * dtmp;
                            }
                            sd = Math.sqrt(sum / v.size());
                            out.write(Double.toString(sd));
                        }
                        out.write("\t");
                        v = eOrfs.get(orf).getVals();
                        Double d2 = 0d;
                        for (Double vd : v) {
                            d2 += vd;
                        }
                        d2 /= v.size();
                        out.write(d2.toString());
                        out.write("\t");
                        sum = 0d;
                        if (v.size() > 1) {
                            for (Double dd : v) {
                                double dtmp = dd - d2;
                                sum += dtmp * dtmp;
                            }
                            sd = Math.sqrt(sum / v.size());
                            out.write(Double.toString(sd));
                        }
                        out.write("\t");
                        out.write(Double.toString(d1 - d2));
                        out.newLine();
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }//GEN-LAST:event_saveScoreAvgButtonActionPerformed

    private void scanshrinkComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanshrinkComboBoxActionPerformed
        prefs.setProperty(PREFS_SCAN_SHRINK, scanshrinkComboBox.getSelectedItem().toString());
    }//GEN-LAST:event_scanshrinkComboBoxActionPerformed

    private void gridCentreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridCentreButtonActionPerformed
        doCentreGrid();
        messageText.append("\nUse cursor keys to position grid.");
        loadedIm.getCanvas().requestFocus();
    }

    void doCentreGrid() {
        if (loadedIm == null) {
            return;
        }
        float cx = loadedIm.getWidth() / 2;
        float cy = loadedIm.getHeight() / 2;
        minX = cx - (dx * cols / 2) + dx / 2;
        maxX = minX + dx * (cols - 1);
        minY = cy - (dx * rows / 2) + dy / 2;
        maxY = minY + dy * (rows - 1);
        stepX = dx;
        stepY = dy;
        drawGrid(
                loadedIm.getCanvas(), Color.green);
        gridded = true;
    }//GEN-LAST:event_gridCentreButtonActionPerformed

    /**
     *
     * @param n
     */
    public void doGridNudgeRight(int n) {
        if (loadedIm == null) {
            return;
        }
        if (maxX == 0) {
            doCentreGrid();
        }
        if (maxX < loadedIm.getWidth() - n) {
            minX += n;
            maxX += n;
        }
        drawGrid(loadedIm.getCanvas(), Color.green);
    }

    /**
     *
     * @param n
     */
    public void doGridNudgeUp(int n) {
        if (loadedIm == null) {
            return;
        }
        if (maxX == 0) {
            doCentreGrid();
        }
        if (minY > n - 1) {
            minY -= n;
            maxY -= n;
        }
        drawGrid(loadedIm.getCanvas(), Color.green);
    }

    /**
     *
     * @param n
     */
    public void doGridNudgeDown(int n) {
        if (loadedIm == null) {
            return;
        }
        if (maxX == 0) {
            doCentreGrid();
        }
        if (maxY < loadedIm.getHeight() - n) {
            minY += n;
            maxY += n;
        }
        drawGrid(loadedIm.getCanvas(), Color.green);
    }

    /**
     *
     * @param n
     */
    public void doGridNudgeLeft(int n) {
        if (loadedIm == null) {
            return;
        }
        if (maxX == 0) {
            doCentreGrid();
        }
        if (minX > n - 1) {
            minX -= n;
            maxX -= n;
        }
        drawGrid(loadedIm.getCanvas(), Color.green);
    }

    private void scanLayoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanLayoutActionPerformed

        int i = scanLayout.getSelectedIndex();
        switch (i) {
            case 0:
                scanAComboBox.setEnabled(false);
                scanBComboBox.setEnabled(true);
                scanBComboBox.setSelectedIndex(0);
                scanCComboBox.setEnabled(false);
                scanDComboBox.setEnabled(false);
                scanaTextField.setEnabled(false);
                scanBTextField.setEnabled(true);
                scanCTextField.setEnabled(false);
                scanDTextField.setEnabled(false);
                scansaveACheckBox.setEnabled(false);
                scansaveBCheckBox.setEnabled(true);
                scansaveCCheckBox.setEnabled(false);
                scansaveDCheckBox.setEnabled(false);
                break;
            case 1:
                scanAComboBox.setEnabled(false);
                scanBComboBox.setEnabled(true);
                scanBComboBox.setSelectedIndex(0);
                scanCComboBox.setEnabled(false);
                scanDComboBox.setEnabled(true);
                scanDComboBox.setSelectedIndex(1);
                scanaTextField.setEnabled(false);
                scanBTextField.setEnabled(true);
                scanCTextField.setEnabled(false);
                scanDTextField.setEnabled(true);
                scansaveACheckBox.setEnabled(false);
                scansaveBCheckBox.setEnabled(true);
                scansaveCCheckBox.setEnabled(false);
                scansaveDCheckBox.setEnabled(true);
                break;
            case 2:
                scanAComboBox.setEnabled(true);
                scanAComboBox.setSelectedIndex(1);
                scanBComboBox.setEnabled(true);
                scanBComboBox.setSelectedIndex(0);
                scanCComboBox.setEnabled(false);
                scanDComboBox.setEnabled(false);
                scanaTextField.setEnabled(true);
                scanBTextField.setEnabled(true);
                scanCTextField.setEnabled(false);
                scanDTextField.setEnabled(false);
                scansaveACheckBox.setEnabled(true);
                scansaveBCheckBox.setEnabled(true);
                scansaveCCheckBox.setEnabled(false);
                scansaveDCheckBox.setEnabled(false);
                break;
            case 3:
                scanAComboBox.setEnabled(true);
                scanAComboBox.setSelectedIndex(1);
                scanBComboBox.setEnabled(true);
                scanBComboBox.setSelectedIndex(0);
                scanCComboBox.setEnabled(true);
                scanCComboBox.setSelectedIndex(3);
                scanDComboBox.setEnabled(true);
                scanDComboBox.setSelectedIndex(2);
                scanaTextField.setEnabled(true);
                scanBTextField.setEnabled(true);
                scanCTextField.setEnabled(true);
                scanDTextField.setEnabled(true);
                scansaveACheckBox.setEnabled(true);
                scansaveBCheckBox.setEnabled(true);
                scansaveCCheckBox.setEnabled(true);
                scansaveDCheckBox.setEnabled(true);
                break;
        }
        if (oToCrop != null) {
            scanLoad();
        }

        prefs.setProperty(PREFS_SCAN_LAYOUT, scanLayout.getSelectedItem().toString());
        savePrefs();

    }//GEN-LAST:event_scanLayoutActionPerformed

    private void gridPanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_gridPanelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            doGridNudgeUp(1);
        }
    }//GEN-LAST:event_gridPanelKeyPressed

    private void imagePanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_imagePanelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            doGridNudgeUp(1);
        }
    }//GEN-LAST:event_imagePanelKeyPressed

    private void scanbaseTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scanbaseTextFieldKeyPressed
    }//GEN-LAST:event_scanbaseTextFieldKeyPressed

    private void scanbaseTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scanbaseTextFieldKeyReleased
        updateScanNames();
    }//GEN-LAST:event_scanbaseTextFieldKeyReleased

    private void scoreKeysComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoreKeysComboBoxActionPerformed
        Object ok = scoreKeysComboBox.getSelectedItem();
        if (ok == null) {
            return;
        }
        String k = ok.toString();
        if (k != null) {
            try {
                File f = new File(keyFiles.get(k).toString());
                loadKeyFile(f);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }//GEN-LAST:event_scoreKeysComboBoxActionPerformed

    private void keyFileLoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keyFileLoadButtonActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileNameExtensionFilter("Balony Key Files (*.key)", "key"));
        jfc.showOpenDialog(this);
        File f = jfc.getSelectedFile();

        if (f == null) {
            return;
        }
        loadKeyFile(f);
        String keyName = f.getName();
        if (keyName.contains(".")) {
            keyName = keyName.substring(0, keyName.lastIndexOf("."));
        }
        keyFiles.put(keyName, f);
        scoreKeysComboBox.removeAllItems();
        new TreeSet<>(keyFiles.keySet()).stream().forEach((k) -> {
            try {
                if (new File(keyFiles.get(k).toString()).exists()) {
                    scoreKeysComboBox.addItem(k);
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        });
        scoreKeysComboBox.setSelectedItem(keyName);
    }

    public void loadKeyFile(File f) {
        int maxP, maxR, maxC;
        maxP = 0;
        maxR = 0;
        maxC = 0;
        BufferedReader in;
        try {
            int p, r, c;
            in = new BufferedReader(new FileReader(f));
            String s;
            while ((s = in.readLine()) != null) {
                String[] d = s.split("\t");
                if (d.length > 3) {
                    try {
                        p = Integer.parseInt(d[0]);
                        r = Integer.parseInt(d[1]);
                        c = Integer.parseInt(d[2]);
                        if (maxP < p) {
                            maxP = p;
                        }
                        if (maxR < r) {
                            maxR = r;
                        }
                        if (maxC < c) {
                            maxC = c;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        if (maxP > 0 && maxR > 0 && maxC > 0) {
            keyGenes = new String[maxP + 1][maxR + 1][maxC + 1];
            keyOrfs = new String[maxP + 1][maxR + 1][maxC + 1];
            keyOrfList = new HashMap<>();
            try {
                int p, r, c;
                in = new BufferedReader(new FileReader(f));
                String s;
                while ((s = in.readLine()) != null) {
                    String[] d = s.split("\t");
                    if (d.length > 3) {
                        try {
                            p = Integer.parseInt(d[0]);
                            r = Integer.parseInt(d[1]);
                            c = Integer.parseInt(d[2]);
                            keyOrfs[p][r][c] = d[3];
                            if (d.length > 4) {
                                keyGenes[p][r][c] = d[4];
                            } else {
                                keyGenes[p][r][c] = d[3];
                            }
                            keyOrfList.put(keyOrfs[p][r][c], keyGenes[p][r][c]);
                        } catch (NumberFormatException e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                    }
                }
                prefs.setProperty(PREFS_SCORE_KEYFILE, f.getAbsolutePath());
                savePrefs();
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }

    }//GEN-LAST:event_keyFileLoadButtonActionPerformed

    private void downloadSGDInfoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadSGDInfoButtonActionPerformed
        new sgdUpdater().execute();
    }//GEN-LAST:event_downloadSGDInfoButtonActionPerformed

    @SuppressWarnings("unchecked")
    private void restoreTableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restoreTableButtonActionPerformed

        // Refresh filters?
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileNameExtensionFilter("Balony Datatable Files (*.bdt)", "bdt"));

        if (prefs.getProperty(PREFS_TABLEFOLDER) != null) {
            jfc.setCurrentDirectory(new File(prefs.getProperty(PREFS_TABLEFOLDER)));
        }

        int rv = jfc.showOpenDialog(this);
        if (rv == JFileChooser.CANCEL_OPTION) {
            return;
        }

        File f = jfc.getSelectedFile();
        prefs.setProperty(PREFS_TABLEFOLDER, f.getParent());
        savePrefs();

        dataTable dt = new dataTable();
        dataTables.add(dt);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(f)));
            dt.myCtrl = (Double[][][][]) ois.readObject();
            System.out.println("myCtrl restored");
            dt.myExp = (Double[][][][]) ois.readObject();
            System.out.println("myExp restored");
            dt.sets = (Integer) ois.readObject();
            System.out.println("sets restored");
            dt.plates = (Integer) ois.readObject();
            System.out.println("plates restored");
            dt.rows = (Integer) ois.readObject();
            System.out.println("rows restored");
            dt.cols = (Integer) ois.readObject();
            System.out.println("cols restored");
            dt.myOrfs = (String[][][]) ois.readObject();
            System.out.println("myOrfs restored");
            dt.myGenes = (String[][][]) ois.readObject();
            System.out.println("myGenes restored");

            dt.minSpotSize = (Double) ois.readObject();
            dt.maxSpotSize = (Double) ois.readObject();
            dt.sickCutOff = (Double) ois.readObject();
            dt.lowCutOff = (Double) ois.readObject();
            dt.highCutOff = (Double) ois.readObject();

            dt.setTitle(ois.readObject().toString());
            dt.saveFile = ois.readObject().toString();

            dt.dtScreenName.setText(ois.readObject().toString());
            dt.hideExcludedJCheckBox.setSelected((Boolean) ois.readObject());
            dt.linkageJTextField.setText(ois.readObject().toString());
            dt.lowCutOffJTextField.setText(ois.readObject().toString());
            dt.highCutOffJTextField.setText(ois.readObject().toString());
            dt.lcHitsJComboBox.setModel((ComboBoxModel) ois.readObject());
            dt.lcHitsJComboBox.setSelectedItem(ois.readObject());
            dt.hcHitsJComboBox.setModel((ComboBoxModel) ois.readObject());
            dt.hcHitsJComboBox.setSelectedItem(ois.readObject());
            dt.lcpvalJTextField.setText(ois.readObject().toString());
            dt.hcpvalJTextField.setText(ois.readObject().toString());
            dt.analysisSickFliterComboBox1.setSelectedIndex((Integer) ois.readObject());
            dt.sickCutOffTextField1.setText(ois.readObject().toString());
            dt.minSpotSizeTextField1.setText(ois.readObject().toString());
            dt.maxSpotSizeTextField1.setText(ois.readObject().toString());
            dt.excl = (HashMap<Integer, TreeSet<String>>) ois.readObject();
            dt.setFileDate((Date) ois.readObject());

            dataTablesComboBox.addItem(dt);
            dt.balony = this;
            dt.reSetupData();
            dt.setIconImage(balloonImage);
            dt.setVisible(true);
            ois.close();
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "File read error.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            System.out.println(ex.getLocalizedMessage());
        }

        try {
//            dataTablesComboBox.addItem(dt);
//            dt.balony = this;
            dt.reSetupData();
            dt.setIconImage(balloonImage);
            dt.setVisible(true);
            if (ois != null) {
                ois.close();
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }


    }//GEN-LAST:event_restoreTableButtonActionPerformed

    private void autoGridJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoGridJCheckBoxActionPerformed

        prefs.setProperty(PREFS_IMAGE_AUTOGRID, autoGridJCheckBox.isSelected() ? "1" : "0");
        savePrefs();
        if (autoGridJCheckBox.isSelected() && threshed == true) {
            gw = new gridWorker();
            gw.execute();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_autoGridJCheckBoxActionPerformed

    private void autoThreshJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoThreshJCheckBoxActionPerformed
        prefs.setProperty(PREFS_IMAGE_AUTOTHRESH,
                autoThreshJCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_autoThreshJCheckBoxActionPerformed

    private void imageShowPresetsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageShowPresetsButtonActionPerformed
        gridP.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_imageShowPresetsButtonActionPerformed

    private void resetrotateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetrotateButtonActionPerformed
        rotated = false;
        new imageLoader().execute();
    }//GEN-LAST:event_resetrotateButtonActionPerformed

    private void imageShowParamsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageShowParamsButtonActionPerformed
        quantP.setVisible(true);
    }//GEN-LAST:event_imageShowParamsButtonActionPerformed

    private void imageFileJListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageFileJListMouseClicked
        if (evt.getClickCount() < 2) {
            return;
        }
        doImageLoad();
    }

    public void doImageLoad() {
        fullAuto = false;
        rotated = false;
        rethresh = 0;
        bestSpots = -1;
        new imageLoader().execute();
    }//GEN-LAST:event_imageFileJListMouseClicked

    private void scanFileJListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scanFileJListMouseClicked
        if (evt.getClickCount() == 2) {
            new scanLoader().execute();
//        } else {
//            updateScanJList();
        }
    }

    public void updateScanJList() {
        scanListUpdater sl = new scanListUpdater();
        sl.execute();
    }

    public class scanListUpdater extends SwingWorker<String, Void> {

        @Override
        public String doInBackground() {
            File f = (File) scanFileJList.getSelectedValue();
            if (f == null) {
                return "";
            }
            if (f != currScanFile && oToCrop != null) {
                oToCrop.close();
            }
            setScanBaseNames(f);
            updateScanNames();

            if (scanPreviewJCheckBox.isSelected()) {

                BufferedImage img = null;

                if ((File) scanFileJList.getSelectedValue() != scanPreview) {
                    messageText.append("\nLoading preview...");
                    scanPreview = (File) scanFileJList.getSelectedValue();
                    try {
                        img = ImageIO.read(f);
                    } catch (Exception e) {
                        System.out.println("Can't display preview: " + e.getLocalizedMessage());
                    }

                    double xoff;
                    double yoff;

                    if (img != null) {
                        double xs = img.getWidth() / scanPreviewPanel.getWidth();
                        double ys = img.getHeight() / scanPreviewPanel.getHeight();
                        double scaleFactor = ys;

                        if (xs > ys) {
                            scaleFactor = xs;
                        }

                        xoff = (scanPreviewPanel.getWidth() - (img.getWidth() / scaleFactor)) / 2;
                        yoff = (scanPreviewPanel.getHeight() - (img.getHeight() / scaleFactor)) / 2;

                        Graphics g = scanPreviewPanel.getGraphics();
                        g.setColor(Color.black);
                        g.fillRect(0, 0, scanPreviewPanel.getWidth(), scanPreviewPanel.getHeight());
                        g.drawImage(img, (int) xoff, (int) yoff, (int) (img.getWidth() / scaleFactor), (int) (img.getHeight() / scaleFactor), null);
                        messageText.append("done.");
                        scanPreview = null;
                    }
                }

            }
            return "";
        }
    }//GEN-LAST:event_scanFileJListMouseClicked

    private void autoGridButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoGridButtonActionPerformed
        quant = false;
        switchToInputImage();
        gw = new gridWorker();
        gw.execute();
    }//GEN-LAST:event_autoGridButtonActionPerformed

    private void manualspotResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualspotResetButtonActionPerformed

        if (oIm == null) {
            return;
        }
        switchToOutputImage();
        ManualSpotRemover msr = new ManualSpotRemover();
        msr.ManualSpot(oIm.getCanvas());
    }//GEN-LAST:event_manualspotResetButtonActionPerformed

    private void threshJSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_threshJSliderStateChanged

        imageThreshManualJTextField.setText(Integer.toString(threshJSlider.getValue()));
        imageThreshManualJTextField.setCaretPosition(imageThreshManualJTextField.getText().length());
        prefs.setProperty(PREFS_IMAGE_THRESVALUE, imageThreshManualJTextField.getText());
        savePrefs();
    }//GEN-LAST:event_threshJSliderStateChanged

    private void threshJSliderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_threshJSliderMouseClicked
    }//GEN-LAST:event_threshJSliderMouseClicked

    private void threshJSliderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_threshJSliderMouseReleased
    }//GEN-LAST:event_threshJSliderMouseReleased

    private void threshJSliderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_threshJSliderMousePressed
    }//GEN-LAST:event_threshJSliderMousePressed

    private void imageThreshManualJTextFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_imageThreshManualJTextFieldPropertyChange
        updateSlider();
    }//GEN-LAST:event_imageThreshManualJTextFieldPropertyChange

    private void imageThreshManualJTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_imageThreshManualJTextFieldKeyTyped
    }//GEN-LAST:event_imageThreshManualJTextFieldKeyTyped

    private void imageThreshManualJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_imageThreshManualJTextFieldKeyReleased
        int i;

        try {
            i = Integer.parseInt(imageThreshManualJTextField.getText());
        } catch (NumberFormatException ex) {
            i = 165;
        }

        if (i > 255) {
            i = 255;
        }

        if (i < 0) {
            i = 165;
        }

        imageThreshManualJTextField.setText("" + i);
        prefs.setProperty(PREFS_IMAGE_THRESVALUE, Integer.toString(i));
        savePrefs();
        updateSlider();        // TODO add your handling code here:
    }//GEN-LAST:event_imageThreshManualJTextFieldKeyReleased

    private void imageStopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageStopButtonActionPerformed
        if (gw != null) {
            gw.cancel(true);
        }

        if (fullAutoWorker != null) {
            fullAutoWorker.cancel(true);
        }
        stopped = true;
    }//GEN-LAST:event_imageStopButtonActionPerformed

    private void toggleInputOutputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleInputOutputButtonActionPerformed
        doToggleInputOutput();
    }

    public void doToggleInputOutput() {
        if (imWin != null && imWin.getImagePlus() != loadedIm) {
            switchToInputImage();
        } else if (imWin != null && imWin.getImagePlus() != oIm) {
            switchToOutputImage();
        }
    }//GEN-LAST:event_toggleInputOutputButtonActionPerformed

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        imageFileJList.setSelectionInterval(0, imageFileJList.getModel().getSize() - 1);
    }//GEN-LAST:event_selectAllButtonActionPerformed

    private void showQuantButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showQuantButtonActionPerformed
        Object[] o = imageFileJList.getSelectedValuesList().toArray();
        int x = 0;
        int y = 0;
        for (Object oo : o) {
            File f = (File) oo;
            String s = getSaveFileName(f.getName());
            File af = new File(f.getParentFile().getAbsolutePath() + File.separator + s);
            if (af.exists()) {
                quantInfo qI = new quantInfo();
                qI.setLocation(x, y);
                qI.setTitle(af.getName());
                qI.setIconImage(balloonImage);
                qI.setVisible(true);
                x += 24;
                y += 24;
                if (x + qI.getWidth() > Toolkit.getDefaultToolkit().getScreenSize().width) {
                    x = 0;
                }
                if (y + qI.getHeight() > getScreenHeight()) {
                    y = 0;
                }
                try {
                    String t;
                    BufferedReader in = new BufferedReader(new FileReader(af));

                    Object[][] tab;
                    while (!(t = in.readLine()).equals(BEGIN_DATA)) {
                        if (t.length() > 4) {

                            if (t.startsWith("Set:")) {
                                qI.setJTextField.setText(t.substring(5));
                            }
                            if (t.startsWith("Plate:")) {
                                qI.plateJTextField.setText(t.substring(7));
                            }
                            if (t.startsWith("Source file:")) {
                                qI.sourceJTextField.setText(t.substring(13));
                            }
                            if (t.startsWith("Date:")) {
                                qI.dateJTextField.setText(t.substring(6));
                            }
                            if (t.startsWith("Bad spots:")) {
                                qI.badSpotsJTextField.setText(t.substring(11));
                            }
                            if (t.startsWith("Name")) {
                            }
                        }
                    }
                    in.readLine();
                    int cnt = 0;
                    tab = new Object[rows * cols][3];
                    while ((t = in.readLine()) != null) {
                        String nums[] = t.split("\t");
                        if (nums.length > 2) {
                            tab[cnt][0] = nums[0];
                            tab[cnt][1] = nums[1];
                            tab[cnt][2] = nums[2];
                        }
                        cnt++;
                    }
                    String[] head = {"Row", "Col", AREA_COL};
                    DefaultTableModel dtm = new DefaultTableModel(tab, head) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    qI.jTable1.setModel(dtm);
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
    }//GEN-LAST:event_showQuantButtonActionPerformed

    private void imageFileJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_imageFileJListValueChanged
        if (imageFileJList.getSelectedIndices().length > 0) {
            currFolder = ((File) imageFileJList.getSelectedValue()).getParentFile();
            folderJTextField.setText(currFolder.getAbsolutePath());
        }
    }//GEN-LAST:event_imageFileJListValueChanged

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        ArrayList<File> al = new ArrayList<>();
        for (int i = 0; i < imageFileJList.getModel().getSize(); i++) {
            if (!imageFileJList.isSelectedIndex(i)) {
                al.add(imageFileJList.getModel().getElementAt(i));
            }
        }
        imageFileJList.setListData((File[]) al.toArray(new File[al.size()]));

    }//GEN-LAST:event_removeButtonActionPerformed

    private void scanOffsetJComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanOffsetJComboBoxActionPerformed
        updateScanNames();
    }//GEN-LAST:event_scanOffsetJComboBoxActionPerformed

    private void scanFilenameHelpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanFilenameHelpJButtonActionPerformed
        new filenameHelp(this, false).setVisible(true);
    }//GEN-LAST:event_scanFilenameHelpJButtonActionPerformed

    private void scansaveACheckBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scansaveACheckBoxMouseClicked
    }//GEN-LAST:event_scansaveACheckBoxMouseClicked

    private void scansaveBCheckBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scansaveBCheckBoxMouseClicked
    }//GEN-LAST:event_scansaveBCheckBoxMouseClicked

    private void scansaveACheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scansaveACheckBoxActionPerformed
        if (scansaveACheckBox.isSelected()) {
            scanaTextField.setEnabled(true);
        } else {
            scanaTextField.setEnabled(false);
        }
    }//GEN-LAST:event_scansaveACheckBoxActionPerformed

    private void scansaveBCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scansaveBCheckBoxActionPerformed
        if (scansaveBCheckBox.isSelected()) {
            scanBTextField.setEnabled(true);
        } else {
            scanBTextField.setEnabled(false);
        }
    }//GEN-LAST:event_scansaveBCheckBoxActionPerformed

    private void scansaveDCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scansaveDCheckBoxActionPerformed
        if (scansaveDCheckBox.isSelected()) {
            scanDTextField.setEnabled(true);
        } else {
            scanDTextField.setEnabled(false);
        }
    }//GEN-LAST:event_scansaveDCheckBoxActionPerformed

    private void scanSelectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanSelectAllButtonActionPerformed
        if (scanFileJList.getModel().getSize() == 0) {
            return;
        }
        scanFileJList.setSelectionInterval(0, scanFileJList.getModel().getSize() - 1);
    }//GEN-LAST:event_scanSelectAllButtonActionPerformed

    /**
     *
     */
    public void doAddCtrl() {
        if (!plateCtrlRadioButton.isSelected()) {
            return;
        }

        if (ctrlplateComboBox.getItemCount() == 0) {
            return;
        }

        String s = (String) ctrlplateComboBox.getSelectedItem();
        ArrayList<File> files = new ArrayList<>();
        for (platenameData pd : ctrlplateData) {
            if (pd != null) {
                if (pd.getName().equals(s)) {
                    files = pd.getFiles();
                }
            }
        }
        // Need initial scan to determine sets, plates, rows, cols
        ctrlData = new spotData();
        maxCtrlSet = 0;
        minCtrlSet = 1000000;
        maxCtrlPlate = 0;
        minCtrlPlate = 1000000;
        for (File f : files) {
            try {
                String t;
                BufferedReader in = new BufferedReader(new FileReader(f));
                int set, plate;
                while (!(t = in.readLine()).equals(BEGIN_DATA)) {
                    if (t.length() > 4) {
                        if (t.startsWith("Rows")) {
                            ctrlData.setRows(Integer.parseInt(t.substring(6)));
                        }
                        if (t.startsWith("Cols")) {
                            ctrlData.setCols(Integer.parseInt(t.substring(6)));
                        }
                        if (t.startsWith("Set:")) {
                            set = Integer.parseInt(t.substring(5));
                            if (set > maxCtrlSet) {
                                maxCtrlSet = set;
                            }
                            if (set < minCtrlSet) {
                                minCtrlSet = set;
                            }
                        }
                        if (t.startsWith("Plate")) {
                            plate = Integer.parseInt(t.substring(7));
                            if (plate > maxCtrlPlate) {
                                maxCtrlPlate = plate;
                            }
                            if (plate < minCtrlPlate) {
                                minCtrlPlate = plate;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        ctrlData.setArea(new Integer[maxCtrlSet + 1][maxCtrlPlate + 1][ctrlData.getRows() + 1][ctrlData.getCols() + 1]);
        ctrlData.setImgfile(new String[maxCtrlSet + 1][maxCtrlPlate + 1]);

        for (File f : files) {
            try {
                String t;
                BufferedReader in = new BufferedReader(new FileReader(f));
                int set, plate;
                set = 0;
                plate = 0;
                while (!(t = in.readLine()).equals(BEGIN_DATA)) {
                    if (t.length() > 4) {

                        if (t.startsWith("Set:")) {
                            set = Integer.parseInt(t.substring(5));
                        }

                        if (t.startsWith("Plate")) {
                            plate = Integer.parseInt(t.substring(7));
                        }
                        if (t.startsWith("Source") && set > 0 && plate > 0) {
                            ctrlData.getImgfile()[set][plate] = f.getName();
                        }
                        if (t.startsWith("Name")) {
                            ctrlData.setName(t.substring(6));
                        }
                    }
                }
                in.readLine();
                while ((t = in.readLine()) != null && set > 0 && plate > 0) {
                    String nums[] = t.split("\t");
                    if (nums.length > 2) {
                        int rowno = Integer.parseInt(nums[0]);
                        int colno = Integer.parseInt(nums[1]);
                        int areano = Integer.parseInt(nums[2]);
                        ctrlData.getArea()[set][plate][rowno][colno] = areano;
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        messageText.append("\nDone loading control data");
        messageText.append("\nSets: ").append(minCtrlSet).append("-").append(maxCtrlSet);
        messageText.append("\nPlates: ").append(minCtrlPlate).append("-").append(maxCtrlPlate);
        normalizeControl();
    }

    /**
     *
     */
    public void doAddExp() {

        if (expplateComboBox.getItemCount() == 0) {
            return;
        }

        String s = (String) expplateComboBox.getSelectedItem();
        ArrayList<File> files = new ArrayList<>();
        for (platenameData pd : expplateData) {
            if (pd != null) {
                if (pd.getName().equals(s)) {
                    files = pd.getFiles();
                }
            }
        }

        // Need initial scan to determine sets, plates, rows, cols
        if (!plateCtrlRadioButton.isSelected()) {
            ctrlData = new spotData();
            maxCtrlSet = 0;
            minCtrlSet = 100000;
            maxCtrlPlate = 0;
            minCtrlPlate = 100000;
        }

        expData = new spotData();
        maxExpSet = 0;
        minExpSet = 1000000;
        maxExpPlate = 0;
        minExpPlate = 1000000;

        for (File f : files) {
            try {
                String t;
                BufferedReader in = new BufferedReader(new FileReader(f));
                int set, plate;
                while (!(t = in.readLine()).equals(BEGIN_DATA)) {
                    if (t.length() > 4) {
                        if (t.startsWith("Rows")) {
                            int r = Integer.parseInt(t.substring(6));

                            expData.setRows(r);

                            if (altRowRadioButton.isSelected()) {
                                ctrlData.setRows(r / 2);
                                expData.setRows(r / 2);
                            }

                            if (altColRadioButton.isSelected()) {
                                ctrlData.setRows(r);
                            }
                        }

                        if (t.startsWith("Cols")) {
                            int c = Integer.parseInt(t.substring(6));

                            expData.setCols(c);

                            if (altColRadioButton.isSelected()) {
                                ctrlData.setCols(c / 2);
                                expData.setCols(c / 2);
                            }

                            if (altRowRadioButton.isSelected()) {
                                ctrlData.setCols(c);
                            }

                        }
                        if (t.startsWith("Set:")) {
                            set = Integer.parseInt(t.substring(5));
                            if (set > maxExpSet) {
                                maxExpSet = set;
                                if (!plateCtrlRadioButton.isSelected()) {
                                    maxCtrlSet = set;
                                }
                            }
                            if (set < minExpSet) {
                                if (!plateCtrlRadioButton.isSelected()) {
                                    minCtrlSet = set;
                                }
                                minExpSet = set;
                            }
                        }
                        if (t.startsWith("Plate")) {
                            plate = Integer.parseInt(t.substring(7));
                            if (plate > maxExpPlate) {
                                maxExpPlate = plate;
                                if (!plateCtrlRadioButton.isSelected()) {
                                    maxCtrlPlate = plate;
                                }
                            }
                            if (plate < minExpPlate) {
                                if (!plateCtrlRadioButton.isSelected()) {
                                    minCtrlPlate = plate;
                                }
                                minExpPlate = plate;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }

        expData.setArea(new Integer[maxExpSet + 1][maxExpPlate + 1][expData.getRows() + 1][expData.getCols() + 1]);
        expData.setImgfile(new String[maxExpSet + 1][maxExpPlate + 1]);

        if (!plateCtrlRadioButton.isSelected()) {
            ctrlData.setArea(new Integer[maxCtrlSet + 1][maxCtrlPlate + 1][ctrlData.getRows() + 1][ctrlData.getCols() + 1]);
            ctrlData.setImgfile(new String[maxCtrlSet + 1][maxCtrlPlate + 1]);
        }

        for (File f : files) {
            try {
                String t;
                BufferedReader in = new BufferedReader(new FileReader(f));
                int set, plate;
                set = 0;
                plate = 0;
                while (!(t = in.readLine()).equals(BEGIN_DATA)) {
                    if (t.length() > 4) {

                        if (t.startsWith("Set:")) {
                            set = Integer.parseInt(t.substring(5));
                        }

                        if (t.startsWith("Plate")) {
                            plate = Integer.parseInt(t.substring(7));
                        }
                        if (t.startsWith("Source") && set > 0 && plate > 0) {
                            expData.getImgfile()[set][plate] = f.getName();
                            if (!plateCtrlRadioButton.isSelected()) {
                                ctrlData.getImgfile()[set][plate] = f.getName();
                            }
                        }
                        if (t.startsWith("Name")) {
                            expData.setName(t.substring(6));
                            if (!plateCtrlRadioButton.isSelected()) {
                                ctrlData.setName(t.substring(6));
                            }
                        }
                    }
                }

                in.readLine();
                while ((t = in.readLine()) != null && set > 0 && plate > 0) {
                    String nums[] = t.split("\t");
                    if (nums.length > 2) {

                        int areano = Integer.parseInt(nums[2]);
                        int rowno = Integer.parseInt(nums[0]);
                        int colno = Integer.parseInt(nums[1]);

                        if (plateCtrlRadioButton.isSelected()) {
                            expData.getArea()[set][plate][rowno][colno] = areano;
                        }

                        if (altColRadioButton.isSelected()) {
                            if (altOddRadioButton.isSelected()) {
                                if (colno % 2 == 0) {
                                    expData.getArea()[set][plate][rowno][colno / 2] = areano;
                                } else {
                                    ctrlData.getArea()[set][plate][rowno][(colno + 1) / 2] = areano;
                                }
                            }

                            if (altEvenRadioButton.isSelected()) {
                                if (colno % 2 == 0) {
                                    ctrlData.getArea()[set][plate][rowno][colno / 2] = areano;
                                } else {
                                    expData.getArea()[set][plate][rowno][(colno + 1) / 2] = areano;
                                }
                            }
                        }

                        if (altRowRadioButton.isSelected()) {
                            if (altOddRadioButton.isSelected()) {
                                if (rowno % 2 == 0) {
                                    expData.getArea()[set][plate][rowno / 2][colno] = areano;
                                } else {
                                    ctrlData.getArea()[set][plate][(rowno + 1) / 2][colno] = areano;
                                }
                            }

                            if (altEvenRadioButton.isSelected()) {
                                if (rowno % 2 == 0) {
                                    ctrlData.getArea()[set][plate][rowno / 2][colno] = areano;
                                } else {
                                    expData.getArea()[set][plate][(rowno + 1) / 2][colno] = areano;
                                }
                            }
                        }

                    }
                }
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        messageText.append("\nDone loading control data");
        messageText.append("\nSets: ").append(minExpSet).append("-").append(maxExpSet);
        messageText.append("\nPlates: ").append(minExpPlate).append("-").append(maxExpPlate);

        if (!plateCtrlRadioButton.isSelected()) {
            normalizeControl();
        }

        normalizeExperiment();
    }

    private void minSpotSizeJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_minSpotSizeJTextFieldKeyReleased
        prefs.setProperty(PREFS_ANALYSIS_MINSPOTSIZE, minSpotSizeJTextField.getText());
        savePrefs();
    }//GEN-LAST:event_minSpotSizeJTextFieldKeyReleased

    private void maxSpotSizeJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_maxSpotSizeJTextFieldKeyReleased
        prefs.setProperty(PREFS_ANALYSIS_MAXSPOTSIZE, maxSpotSizeJTextField.getText());
        savePrefs();
    }//GEN-LAST:event_maxSpotSizeJTextFieldKeyReleased

    private void lowCutOffJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lowCutOffJTextFieldKeyReleased
        prefs.setProperty(PREFS_ANALYSIS_LOWCUTOFF, lowCutOffJTextField.getText());
        savePrefs();
    }//GEN-LAST:event_lowCutOffJTextFieldKeyReleased

    private void upperCutOffJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_upperCutOffJTextFieldKeyReleased
        prefs.setProperty(PREFS_ANALYSIS_HIGHCUTOFF, upperCutOffJTextField.getText());
        savePrefs();
    }//GEN-LAST:event_upperCutOffJTextFieldKeyReleased

    private void sickCutOffTextJFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sickCutOffTextJFieldKeyReleased
        prefs.setProperty(PREFS_ANALYSIS_SICKCUTOFF, sickCutOffTextJField.getText());
        savePrefs();
    }//GEN-LAST:event_sickCutOffTextJFieldKeyReleased

    private void analysisSickFliterJComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisSickFliterJComboBoxActionPerformed
        prefs.setProperty(PREFS_ANALYSIS_SICKFILTER, analysisSickFliterJComboBox.getSelectedItem().toString());
        savePrefs();
    }//GEN-LAST:event_analysisSickFliterJComboBoxActionPerformed

    private void analysisSickFliterJComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_analysisSickFliterJComboBoxItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_analysisSickFliterJComboBoxItemStateChanged

    private void scanbaseTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanbaseTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scanbaseTextFieldActionPerformed

    private void threshRadioAutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_threshRadioAutoActionPerformed

        if (threshRadioAuto.isSelected()) {
            prefs.setProperty(PREFS_IMAGE_THRESHMETHOD, THRESH_AUTO);
        } else {
            prefs.setProperty(PREFS_IMAGE_THRESHMETHOD, THRESH_MANUAL);
        }

        savePrefs();

        if (threshRadioAuto.isSelected()) {
            imageThreshManualJTextField.setEnabled(false);
            threshJSlider.setEnabled(false);
        }

    }//GEN-LAST:event_threshRadioAutoActionPerformed

    private void contactJLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactJLabelMouseClicked
        Desktop desktop = Desktop.getDesktop();
        URI mailto = URI.create("mailto:byoung@interchange.ubc.ca?subject=Balony");
        try {
            desktop.mail(mailto);

        } catch (IOException ex) {
            Logger.getLogger(Balony.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_contactJLabelMouseClicked

    private void updateCheckButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCheckButtonActionPerformed

        updateWorker uwo = new updateWorker();
        uwo.execute();

    }//GEN-LAST:event_updateCheckButtonActionPerformed

    private void nimbusLAFJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nimbusLAFJRadioButtonActionPerformed
        if (nimbusLAFJRadioButton.isSelected()) {
            useOSLaF = true;
            prefs.setProperty(PREFS_OPTIONS_OS_LOOK_AND_FEEL, "2");
            System.out.println("Setting nimbus as default");
            savePrefs();
        }
    }//GEN-LAST:event_nimbusLAFJRadioButtonActionPerformed

    private void osLAFJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_osLAFJRadioButtonActionPerformed
        if (osLAFJRadioButton.isSelected()) {
            useOSLaF = true;
            prefs.setProperty(PREFS_OPTIONS_OS_LOOK_AND_FEEL, "1");
            savePrefs();
        }
    }//GEN-LAST:event_osLAFJRadioButtonActionPerformed

    private void javaLAFJRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javaLAFJRadioButtonActionPerformed
        if (javaLAFJRadioButton.isSelected()) {
            useOSLaF = true;
            prefs.setProperty(PREFS_OPTIONS_OS_LOOK_AND_FEEL, "0");
            savePrefs();
        }
    }//GEN-LAST:event_javaLAFJRadioButtonActionPerformed

    private void contactJLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contactJLabel1MouseClicked
        Desktop desktop = Desktop.getDesktop();
        URI browseto = URI.create("http://code.google.com/p/balony/wiki/Introduction?tm=6");
        try {
            desktop.browse(browseto);

        } catch (IOException ex) {
            Logger.getLogger(Balony.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_contactJLabel1MouseClicked

    private void scanFileJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_scanFileJListValueChanged

        updateScanJList();

    }//GEN-LAST:event_scanFileJListValueChanged

    private void scanPreviewJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanPreviewJCheckBoxActionPerformed
        if (!scanPreviewJCheckBox.isSelected()) {
            Graphics g = scanPreviewPanel.getGraphics();
            g.dispose();
            scanPreviewPanel.repaint();
        } else {
            updateScanJList();
        }
    }//GEN-LAST:event_scanPreviewJCheckBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (messageFrame == null) {
            messageFrame = new messageJFrame();
        }

        messageFrame.setVisible(true);
        messageFrame.setLocation(0, 0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void scanrotateComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanrotateComboBoxActionPerformed
        prefs.setProperty(PREFS_SCAN_ROTATE, scanrotateComboBox.getSelectedItem().toString());
        savePrefs();
    }//GEN-LAST:event_scanrotateComboBoxActionPerformed

    private void scangrayCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scangrayCheckBoxActionPerformed
        prefs.setProperty(PREFS_SCAN_GRAYSCALE, scangrayCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_scangrayCheckBoxActionPerformed

    private void scancloseCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scancloseCheckBoxActionPerformed
        prefs.setProperty(PREFS_SCAN_CLOSE, scancloseCheckBox.isSelected() ? "1" : "0");
    }//GEN-LAST:event_scancloseCheckBoxActionPerformed

    private void scanAutoPlateNameJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanAutoPlateNameJCheckBoxActionPerformed
        prefs.setProperty(PREFS_SCAN_AUTONAME, scanAutoPlateNameJCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_scanAutoPlateNameJCheckBoxActionPerformed

    private void scansubfolderTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scansubfolderTextFieldKeyReleased
        prefs.setProperty(PREFS_SCAN_SUBFOLDER, scansubfolderTextField.getText());
        savePrefs();
    }//GEN-LAST:event_scansubfolderTextFieldKeyReleased

    private void autoNameJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoNameJCheckBoxActionPerformed
        prefs.setProperty(PREFS_IMAGE_AUTONAME,
                autoNameJCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_autoNameJCheckBoxActionPerformed

    private void autoQuantJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoQuantJCheckBoxActionPerformed
        prefs.setProperty(PREFS_IMAGE_AUTOQUANT,
                autoQuantJCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_autoQuantJCheckBoxActionPerformed

    private void autoInvertJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoInvertJCheckBoxActionPerformed
        prefs.setProperty(PREFS_IMAGE_AUTOINVERT,
                autoInvertJCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_autoInvertJCheckBoxActionPerformed

    private void autoSaveJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoSaveJCheckBoxActionPerformed
        prefs.setProperty(PREFS_IMAGE_AUTOSAVE,
                autoSaveJCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_autoSaveJCheckBoxActionPerformed

    private void autoAnalyzeJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoAnalyzeJCheckBoxActionPerformed
        prefs.setProperty(PREFS_SCORE_AUTOANALYZE,
                autoAnalyzeJCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_autoAnalyzeJCheckBoxActionPerformed

    private void scoreByArrayPosRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoreByArrayPosRadioButtonActionPerformed
        prefs.setProperty(PREFS_SCORE_SCOREBY,
                scoreByArrayPosRadioButton.isSelected() ? SCORE_BY_ARRAY : SCORE_BY_ORF);
        savePrefs();
    }//GEN-LAST:event_scoreByArrayPosRadioButtonActionPerformed

    private void scoreByOrfRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoreByOrfRadioButtonActionPerformed
        prefs.setProperty(PREFS_SCORE_SCOREBY,
                scoreByArrayPosRadioButton.isSelected() ? SCORE_BY_ARRAY : SCORE_BY_ORF);
        savePrefs();
    }//GEN-LAST:event_scoreByOrfRadioButtonActionPerformed

    private void analysisOpenDataTablesJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisOpenDataTablesJCheckBoxActionPerformed
        prefs.setProperty(PREFS_ANALYSIS_OPEN_TABLES,
                analysisOpenDataTablesJCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_analysisOpenDataTablesJCheckBoxActionPerformed

    private void analysisOverrideKeyFileCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisOverrideKeyFileCheckBoxActionPerformed
        prefs.setProperty(PREFS_ANALYSIS_OVERRIDE_KEYFILE,
                analysisOverrideKeyFileCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_analysisOverrideKeyFileCheckBoxActionPerformed

    private void updateCheckJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCheckJCheckBoxActionPerformed
        prefs.setProperty(PREFS_OPTIONS_UPDATE_CHECK,
                updateCheckJCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_updateCheckJCheckBoxActionPerformed

    private void scoringRefreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoringRefreshButtonActionPerformed
        updateScoreTab();
    }//GEN-LAST:event_scoringRefreshButtonActionPerformed

    private void scanFileJListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scanFileJListKeyReleased

        // TODO add your handling code here:
    }//GEN-LAST:event_scanFileJListKeyReleased

    private void scanFileJListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scanFileJListMouseReleased

        // TODO add your handling code here:
    }//GEN-LAST:event_scanFileJListMouseReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        updateWorker uwo = new updateWorker();
        uwo.execute();


    }//GEN-LAST:event_jButton2ActionPerformed

    private void wizardModeJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wizardModeJCheckBoxActionPerformed
        prefs.setProperty(PREFS_ANALYSIS_WIZARDMODE, wizardModeJCheckBox.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_wizardModeJCheckBoxActionPerformed

    private void analysisTableRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisTableRemoveButtonActionPerformed
        String scr = dataTablesComboBox.getSelectedItem().toString();
        dataTables.remove(aD.get(scr).getDt());
        dataTablesComboBox.removeItem(aD.get(scr).getDt());
        aD.get(scr).getDt().dispose();
        aD.remove(scr);
    }//GEN-LAST:event_analysisTableRemoveButtonActionPerformed

    private void updateCheckJCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCheckJCheckBox1ActionPerformed
        prefs.setProperty(PREFS_OPTIONS_UPDATE_BETA_CHECK,
                updateCheckJCheckBox1.isSelected() ? "1" : "0");
        savePrefs();
    }//GEN-LAST:event_updateCheckJCheckBox1ActionPerformed

    private void ctrlplateComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctrlplateComboBoxActionPerformed
        updateScoringFileName();
    }//GEN-LAST:event_ctrlplateComboBoxActionPerformed

    public void updateScoringFileName() {
//        if (ctrlplateComboBox.getSelectedItem() != null && expplateComboBox.getSelectedItem() != null) {
//            scorenameTextField.setText(ctrlplateComboBox.getSelectedItem().toString() + "--"
//                    + expplateComboBox.getSelectedItem().toString());
//        }

        String f = "";

        if (expplateComboBox.getSelectedItem() != null) {
            if (ctrlplateComboBox.getSelectedItem() != null) {
                f = ctrlplateComboBox.getSelectedItem().toString().concat("--");
            }
            f = f.concat(expplateComboBox.getSelectedItem().toString());
        }

        scorenameTextField.setText(f);
    }

    private void plateCtrlRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plateCtrlRadioButtonActionPerformed
        ctrlTypeChanged();
    }//GEN-LAST:event_plateCtrlRadioButtonActionPerformed

    private void altColRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_altColRadioButtonActionPerformed
        ctrlTypeChanged();
    }//GEN-LAST:event_altColRadioButtonActionPerformed

    private void altRowRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_altRowRadioButtonActionPerformed
        ctrlTypeChanged();
    }//GEN-LAST:event_altRowRadioButtonActionPerformed

    private void normPlateMedianButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normPlateMedianButtonActionPerformed
        normButtonPressed();
    }//GEN-LAST:event_normPlateMedianButtonActionPerformed

    private void normORFButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normORFButtonActionPerformed
        normButtonPressed();
    }//GEN-LAST:event_normORFButtonActionPerformed

    private void expplateComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expplateComboBoxActionPerformed
        updateScoringFileName();
    }//GEN-LAST:event_expplateComboBoxActionPerformed

    private void altOddRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_altOddRadioButtonActionPerformed
        oddEvenChanged();
    }//GEN-LAST:event_altOddRadioButtonActionPerformed

    private void altEvenRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_altEvenRadioButtonActionPerformed
        oddEvenChanged();
    }//GEN-LAST:event_altEvenRadioButtonActionPerformed

    private void scoreSpatialJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoreSpatialJCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scoreSpatialJCheckBoxActionPerformed

    private void scoreRCJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoreRCJCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scoreRCJCheckBoxActionPerformed

    private void normNoneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normNoneButtonActionPerformed
        normButtonPressed();
    }//GEN-LAST:event_normNoneButtonActionPerformed

    private void analysisGenerateSummaryTablesHiddenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisGenerateSummaryTablesHiddenButtonActionPerformed
        dataTables.stream().forEach((dt) -> {
            dt.makeSummaryTable(false);
        });
    }//GEN-LAST:event_analysisGenerateSummaryTablesHiddenButtonActionPerformed

    private void analysisGenerateSummaryTablesVisibleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisGenerateSummaryTablesVisibleButtonActionPerformed
        dataTables.stream().forEach((dt) -> {
            dt.makeSummaryTable(true);
        });
    }//GEN-LAST:event_analysisGenerateSummaryTablesVisibleButtonActionPerformed

    private void analysisShowSummaryTablesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisShowSummaryTablesActionPerformed
        sumTables.stream().forEach((sjf) -> {
            sjf.setVisible(true);
        });
    }//GEN-LAST:event_analysisShowSummaryTablesActionPerformed

    private void analysisSummaryClipboardCopyTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisSummaryClipboardCopyTypeComboBoxActionPerformed
        // Get list of summary frames
        // Create 2-d array [frame][value]
        // For each frame {
        //   Create array of string values
        //   First value is header
        //   Get values from frame (func in frame?)
        // }
        // stringbuilder:
        //  Outer loop increasing value ref 
        //    Inner loop through 2-d array [frame][value] - append +"\t"
        //                            [frame+1][value] - append +"\t"
        //  Outer loop append + "\n"
        // Add to clipboard
    }//GEN-LAST:event_analysisSummaryClipboardCopyTypeComboBoxActionPerformed

    private void analysisSummaryMedianCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisSummaryMedianCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_analysisSummaryMedianCheckBoxActionPerformed

    private void analysisTableShowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisTableShowButtonActionPerformed
        dataTable ob = (dataTable) dataTablesComboBox.getSelectedItem();
        if (ob != null) {
            ob.setVisible(true);
            currentDT = ob;
        }
    }//GEN-LAST:event_analysisTableShowButtonActionPerformed

    private void analysisSummaryCopyAllClipboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_analysisSummaryCopyAllClipboardButtonActionPerformed

        summarizeJFrame stmp = new summarizeJFrame();
        int myCol = 1;

        if (analysisSummaryClipboardCopyTypeComboBox.getSelectedItem().toString().equals("Ctrls")) {
            myCol = stmp.COL_CTRL;
        }

        if (analysisSummaryClipboardCopyTypeComboBox.getSelectedItem().toString().equals("Ctrl SDs")) {
            myCol = stmp.COL_CTRL_SD;
        }

        if (analysisSummaryClipboardCopyTypeComboBox.getSelectedItem().toString().equals("Ctrl n")) {
            myCol = stmp.COL_CTRL_N;
        }

        if (analysisSummaryClipboardCopyTypeComboBox.getSelectedItem().toString().equals("Exps")) {
            myCol = stmp.COL_EXP;
        }

        if (analysisSummaryClipboardCopyTypeComboBox.getSelectedItem().toString().equals("Exp SDs")) {
            myCol = stmp.COL_EXP_SD;
        }

        if (analysisSummaryClipboardCopyTypeComboBox.getSelectedItem().toString().equals("Exp n")) {
            myCol = stmp.COL_EXP_N;
        }

        if (analysisSummaryClipboardCopyTypeComboBox.getSelectedItem().toString().equals("Ratios")) {
            myCol = stmp.COL_RATIO;
        }

        if (analysisSummaryClipboardCopyTypeComboBox.getSelectedItem().toString().equals("Ratio SDs")) {
            myCol = stmp.COL_RATIO_SD;
        }

        if (analysisSummaryClipboardCopyTypeComboBox.getSelectedItem().toString().equals("Diffs")) {
            myCol = stmp.COL_DIFF;
        }

        if (analysisSummaryClipboardCopyTypeComboBox.getSelectedItem().toString().equals("Diff SDs")) {
            myCol = stmp.COL_DIFF_SD;
        }

        if (analysisSummaryClipboardCopyTypeComboBox.getSelectedItem().toString().equals("p-values")) {
            myCol = stmp.COL_PVAL;
        }

        int sumRows = 1;
        for (summarizeJFrame sfj : sumTables) {
            sumRows = sfj.tableData.length;
        }

        Object[][] sumData = new Object[sumTables.size() + 1][sumRows + 1];
        sumData[0][0] = "GENE";

        int i = 0;
        for (summarizeJFrame sfj : sumTables) {

//            sfj.jTable1.setRowSorter(new RowSorter<TableModel>);
            sfj.setSortByGene();
            if (i < 1) {
                for (int j = 1; j < sumRows + 1; j++) {
                    sumData[0][j] = sfj.jTable1.getValueAt(j - 1, sfj.COL_GENE);
                }

                i = 1;
            }

            sumData[i][0] = sfj.getHeader();
            for (int j = 1; j < sumRows + 1; j++) {
                sumData[i][j] = sfj.jTable1.getValueAt(j - 1, myCol);
            }

            i++;
        }

        StringBuilder sb = new StringBuilder();

//        System.out.println(sumData.length + "," + sumData[0].length);
        for (int j = 0; j < sumData[0].length; j++) {
            for (Object[] sumData1 : sumData) {
                if (sumData1[j] != null) {
                    sb.append(sumData1[j].toString()).append("\t");
                }
            }
            sb.append("\n");
        }

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(sb.toString()), this);

//        System.out.println(sb.toString());
        // Get list of summary frames
        // Create 2-d array [frame][value]
        // For each frame {
        //   Create array of string values
        //   First value is header
        //   Get values from frame (func in frame?)
        // }
        // stringbuilder:
        //  Outer loop increasing value ref 
        //    Inner loop through 2-d array [frame][value] - append +"\t"
        //                            [frame+1][value] - append +"\t"
        //  Outer loop append + "\n"
        // Add to clipboard
    }//GEN-LAST:event_analysisSummaryCopyAllClipboardButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileNameExtensionFilter("Array Key Files (*.key)", "key"));
        jfc.showOpenDialog(this);
        File f = jfc.getSelectedFile();
        if (f != null) {
            queryArrayFile = f;
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    public boolean summaryTableMedian() {
        return analysisSummaryMedianCheckBox.isSelected();
    }

    public boolean summaryTablePaired() {
        return analysisSummaryPairedSpotsCheckBox.isSelected();
    }

    public void oddEvenChanged() {
        if (altOddRadioButton.isSelected()) {
            prefs.setProperty(PREFS_ALT_ODD_EVEN, "1");
        }

        if (altEvenRadioButton.isSelected()) {
            prefs.setProperty(PREFS_ALT_ODD_EVEN, "2");
        }

    }

    public void normButtonPressed() {
        if (normPlateMedianButton.isSelected()) {
            normORFJTextField.setEnabled(false);
            prefs.setProperty(PREFS_NORMALIZATION, "1");
        } else {
            normORFJTextField.setEnabled(true);
        }

        if (normORFButton.isSelected()) {
            prefs.setProperty(PREFS_NORMALIZATION, "2");
        }

        if (normNoneButton.isSelected()) {
            normORFJTextField.setEnabled(false);
            prefs.setProperty(PREFS_NORMALIZATION, "3");
        }

        savePrefs();
    }

    private void ctrlTypeChanged() {

        for (Component c : ctrlPanel.getComponents()) {
            if (plateCtrlRadioButton.isSelected()) {

                c.setEnabled(true);
                altOddRadioButton.setEnabled(false);
                altEvenRadioButton.setEnabled(false);

            } else {
                c.setEnabled(false);
                altOddRadioButton.setEnabled(true);
                altEvenRadioButton.setEnabled(true);
            }
        }

        if (plateCtrlRadioButton.isSelected()) {
            prefs.setProperty(PREFS_CTRL_TYPE, "1");
        }

        if (altColRadioButton.isSelected()) {
            prefs.setProperty(PREFS_CTRL_TYPE, "2");
            JOptionPane.showMessageDialog(this,
                    "Warning: using alternate  columns as the control means that the\n"
                    + "scored array will have half the number of columns as the input and\n"
                    + "this MUST be reflected in the key file.",
                    PLATE, JOptionPane.WARNING_MESSAGE);
        }

        if (altRowRadioButton.isSelected()) {
            prefs.setProperty(PREFS_CTRL_TYPE, "3");
            JOptionPane.showMessageDialog(this,
                    "Warning: using alternate  rows as the control means that the\n"
                    + "scored array will have half the number of rows as the input and\n"
                    + "this MUST be reflected in the key file.",
                    PLATE, JOptionPane.WARNING_MESSAGE);
        }

        savePrefs();
    }

    /**
     *
     */
    public class updateWorker extends SwingWorker<String, Void> {

        public String newjar,
                /**
                 *
                 */
                newfile,
                /**
                 *
                 */
                newversion;

        /**
         *
         */
        public boolean background;

        @Override
        protected String doInBackground() throws Exception {

            long mostrecent = 0l;
            String mostrecentname = "";
            GHRelease recent = new GHRelease();
            String recenturl = "";
            boolean prereleaseOk = updateCheckJCheckBox1.isSelected();
            try {
//                System.out.println("Starting update check");
                GitHub github = GitHub.connectAnonymously();
                GHRepository repo = github.getRepository("barrypyoung/balony");
                PagedIterable<GHRelease> releases = repo.listReleases();
                for (GHRelease release : releases) {

                    if (release.getPublished_at().getTime() > mostrecent && !release.isDraft()
                            && (!release.isPrerelease() || prereleaseOk)
                            && !release.getAssets().isEmpty()) {

                        mostrecent = release.getPublished_at().getTime();
                        mostrecentname = release.getTagName();
                        recent = release;
                    }
//                    List<GHAsset> assets = release.getAssets();
                }

                if (mostrecent > 0) {
                    for (GHAsset asset : recent.getAssets()) {
                        if (asset.getName().endsWith(".jar")) {
                            recenturl = asset.getBrowserDownloadUrl();
                        }
                    }
                }

                prefs.setProperty(PREFS_BALONYLATESTVERSION, mostrecentname);
                latestVersionJLabel.setText(mostrecentname);
                savePrefs();

                int n = JOptionPane.NO_OPTION;

                if (!recenturl.isEmpty() && mostrecentname.equals(BalonyVersion) && !background) {
                    n = JOptionPane.showOptionDialog(null,
                            "You are already using latest version of Balony. Update anyway?",
                            "Warning", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null,
                            null, null);
                }

                if (!recenturl.isEmpty() && !mostrecentname.equals(BalonyVersion)) {

                    n = JOptionPane.showOptionDialog(null,
                            "A newer version of Balony is available. Update?",
                            "Warning", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null,
                            null, null);
                }

                if (n == JOptionPane.YES_OPTION) {

                    newjar = recenturl;
                    newversion = mostrecentname;

                }

            } catch (IOException | HeadlessException ex) {
                System.out.println(ex.getLocalizedMessage());
            }

            if (newjar != null) {
//                System.out.println("Starting update process");
                try {

                    URL balonyURL = new URL(newjar.replace(" ", "%20"));
                    FileOutputStream out;
                    try (InputStream in = balonyURL.openStream()) {
                        int size = balonyURL.openConnection().getContentLength();
                        out = new FileOutputStream("Balony.jar");
                        byte[] buffer = new byte[1024];
                        int len = in.read(buffer);
                        currVersionJLabel.setText("Update in progress, do not quit.");
                        messageText.append("\nInstalling new Balony.jar\nDownloading");
                        int cnt = 0;
                        int done = 0;
                        while (len >= 0) {

                            cnt++;
                            if (cnt == size / 4096) {
                                done += 25;
                                messageText.append(done).append("%");
                                cnt = 0;
                            } else if (cnt % (size / 16384) == 0) {
                                messageText.append(".");
                            }

                            out.write(buffer, 0, len);
                            len = in.read(buffer);
                        }
                    }
                    out.close();
                    JOptionPane.showMessageDialog(getParent(), "Restart Balony to use the new version.");
                    currVersionJLabel.setText("Update complete; restart required.");
                    messageText.append("\nDone.");
                    BalonyVersion = newversion;
                    prefs.setProperty(PREFS_BALONYVERSION, BalonyVersion);
                    savePrefs();

                } catch (IOException | HeadlessException e) {
                    JOptionPane.showMessageDialog(getParent(),
                            "Update failed - check your internet connection and "
                            + "that you have write permission to the installation "
                            + "folder.");
                    System.out.println(e.getLocalizedMessage());
                }
            }

            return "";
        }
    }

    /**
     *
     */
    public class gridWorker extends SwingWorker<String, Void> {

        @Override
        protected String doInBackground() throws Exception {
            doAutoGrid();
            if (autoQuantJCheckBox.isSelected() && !quant) {
                doQuant();
            }
            return "";
        }

        @Override
        protected void done() {
            super.done();
        }
    }

    /**
     *
     */
    public void doAutoGrid() {
        if (stopped) {
            stopped = false;
            return;
        }
        stopped = false;
        if (threshed == false) {
            messageText.append("\nGridding requires thresholded image.");
            return;
        }
        boolean ok = false;

        ResultsTable rt = new ResultsTable();
        ParticleAnalyzer pA;
        int myTry = 0;
        float xs[], ys[], as[];

        loadedIm.getProcessor().resetRoi();
        loadedIm.killRoi();

        pA = new ParticleAnalyzer(0, -1, rt, (dx * dy / 100), dx * dy * 2, 0.8, 1.0);
        pA.setHideOutputImage(true);
        pA.analyze(loadedIm);
        float xmn, ymn;
        xmn = ymn = 0;
        if (rt.getColumn(0).length < (rows * cols / 3) || rt.getColumn(0).length > (rows * cols * 3)
                || cols * dx > loadedIm.getWidth() || cols * dx < loadedIm.getWidth() / 2) {
            int k = JOptionPane.showOptionDialog(null,
                    "Image format may not match Grid Preset. Continue?", "Warning!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, null, null);
            if (k == JOptionPane.NO_OPTION) {
                return;
            }
        }

        // Find the mean x and y values of the spots; i.e the rough centre of the plate
        xs = rt.getColumn(rt.getColumnIndex(RT_X));
        ys = rt.getColumn(rt.getColumnIndex(RT_Y));
        for (int j = 0; j < xs.length; j++) {
            xmn += xs[j];
        }
        for (int j = 0; j < ys.length; j++) {
            ymn += ys[j];
        }
        float maxy = (float) Tools.getMinMax(ys)[1];
//        System.out.println("MaxY: " + maxy);
        xmn /= xs.length;
        ymn /= ys.length;
        boolean xok;
        boolean yok;
        int cnt = 1;
        float maxArea = 0;
        rt = new ResultsTable();
        pA = new ParticleAnalyzer(0, -1, rt, dx * dy / 50, dx * dy * 2, 0.8, 1.0);
        pA.setHideOutputImage(true);
        pA.analyze(loadedIm);
        float x_min = xmn - ((cols + 1) * dx / 2);
        float y_min = ymn - ((rows + 1) * dy / 2);
        boolean pass2 = false;
        while (!ok && myTry < 25 && (fullAutoWorker == null ? true
                : !fullAutoWorker.isCancelled()) && !stopped) {
//            System.out.println("My try: " + myTry);
            if (myTry == 99 && !pass2) {

//                System.out.println("Second pass");
                pass2 = true;
                myTry = 0;
                rt = new ResultsTable();
                pA = new ParticleAnalyzer(0, -1, rt, dx * dy / 50, dx * dy * 2, 0.95, 1.0);
                pA.setHideOutputImage(true);
                pA.analyze(loadedIm);
                x_min = xmn - ((cols + 1) * dx / 2);
                y_min = ymn - ((rows + 1) * dy / 2);
                maxArea = 0;
            }
            for (int j = 0; j < 4; j++) {
                if (stopped) {
                    continue;
                }
                if (ok || (myTry == 0 && j > 0)) {
                    continue;
                }
                int xf = 1;
                int yf = 1;
                int loop = 1;
                switch (j) {
                    case 0:
                        xf = (int) (dx / 8);
                        yf = 0;
                        loop = myTry * 2 - 1;
                        break;
                    case 1:
                        xf = 0;
                        yf = (int) (dy / 8);
                        loop = myTry * 2 - 1;
                        break;
                    case 2:
                        xf = (int) (dx / -8);
                        yf = 0;
                        loop = myTry * 2;
                        break;
                    case 3:
                        xf = 0;
                        yf = (int) (dy / -8);
                        loop = myTry * 2;
                        break;
                }

                if (myTry == 0) {
                    xf = 0;
                    yf = 0;
                }

                for (int k = 0; k < loop; k++) {
                    cnt++;
                    if (cnt % 100 == 1) {
                        messageText.append("\nAutogrid: finding particles #").append(cnt);
                    }
                    xs = rt.getColumn(rt.getColumnIndex(RT_X));
                    ys = rt.getColumn(rt.getColumnIndex(RT_Y));
                    as = rt.getColumn(rt.getColumnIndex(AREA_COL));
                    x_min += xf;
                    y_min += yf;
                    float x_max = x_min + (cols + 1) * dx;
                    float y_max = y_min + (rows + 1) * dy;
//                    System.out.println("xmin:" + x_min);
//                    System.out.println("ymin:" + y_min);
                    ArrayList<Float> alX, alY, alA;
                    alX = new ArrayList<>();
                    alY = new ArrayList<>();
                    alA = new ArrayList<>();
                    float totArea = 0;
                    for (int count = 0; count < rt.getColumn(0).length; count++) {
                        if (xs[count] > x_min && xs[count] < x_max
                                && ys[count] > y_min && ys[count] < y_max) {
                            alX.add(xs[count]);
                            alY.add(ys[count]);
                            alA.add(as[count]);
                            totArea += as[count];
                        }
                    }
                    minX = (float) BalonyTools.getMinMax(alX.toArray())[0];
                    maxX = (float) BalonyTools.getMinMax(alX.toArray())[1];
                    minY = (float) BalonyTools.getMinMax(alY.toArray())[0];
                    maxY = (float) BalonyTools.getMinMax(alY.toArray())[1];
//                    System.out.println("minY: " + minY);
//                    System.out.println("maxY: " + maxY);
                    stepX = (maxX - minX) / (cols - 1);
                    stepY = (maxY - minY) / (rows - 1);
                    if (totArea > maxArea) {
                        maxArea = totArea;
                    }
                    // x-check
                    int xoff = 0;
                    xoff = alX.stream().map((xx) -> {
                        int x1 = Math.round(((xx - minX) / stepX) + 1);
                        float xm = Math.abs(((xx - minX) / stepX) + 1 - x1);
                        return xm;
                    }).filter((xm) -> (xm > 0.3)).map((_item) -> 1).reduce(xoff, Integer::sum);
                    xok = xoff <= (alX.size() / 10);
                    // y-check
                    int yoff = 0;
                    yoff = alY.stream().map((yy) -> {
                        int y1 = Math.round(((yy - minY) / stepY) + 1);
                        float ym = Math.abs(((yy - minY) / stepY) + 1 - y1);
                        return ym;
                    }).filter((ym) -> (ym > 0.3)).map((_item) -> 1).reduce(yoff, Integer::sum);
                    yok = yoff <= (alY.size() / 10);
//                    System.out.println("Tot area: " + totArea);
//                    System.out.println("xratio: " + (stepX / dx));
//                    System.out.println("yratio: " + (stepY / dy));
//                    System.out.println("xok? " + xok);
//                    System.out.println("yok? " + yok);
                    if (stepX < (dx * 1.05) && stepY < (dy * 1.05)
                            && stepX > (dx / 1.05) && stepY > (dy / 1.05) && xok && yok
                            && (totArea / maxArea) > 0.99f) {

                        // Check for empty rows/cols?                        
                        ok = true;
                        // Average out min/max x; min/max y
                        ArrayList<Float> xv = new ArrayList<>();
                        for (Float alX1 : alX) {
                            if (Math.round((alX1 - minX) / stepX) == 0) {
                                xv.add(alX1);
                            }
                        }
                        float xav = 0;
                        for (Float o : xv) {
                            xav += o;
                        }
                        xav /= xv.size();
                        xv = new ArrayList<>();
                        for (Float alX1 : alX) {
                            if (Math.round((alX1 - minX) / stepX) == (cols - 1)) {
                                xv.add(alX1);
                            }
                        }
                        float xav2 = 0;
                        for (Float o : xv) {
                            xav2 += o;
                        }
                        xav2 /= xv.size();
                        xv = new ArrayList<>();
                        for (Float alY1 : alY) {
                            if (Math.round((alY1 - minY) / stepY) == 0) {
                                xv.add(alY1);
                            }
                        }
                        float yav = 0;
                        for (Float o : xv) {
                            yav += o;
                        }
                        yav /= xv.size();
                        xv = new ArrayList<>();
                        for (Float alY1 : alY) {
                            if (Math.round((alY1 - minY) / stepY) == (rows - 1)) {
                                xv.add(alY1);
                            }
                        }
                        float yav2 = 0;
                        for (Float o : xv) {
                            yav2 += o;
                        }
                        yav2 /= xv.size();
                        minX = xav;
                        maxX = xav2;
                        stepX = (xav2 - xav) / (cols - 1);
                        minY = yav;
                        maxY = yav2;
                        stepY = (yav2 - yav) / (rows - 1);
                    }
                }
            }
            myTry++;
        }
//        }
        if (ok) {
            gridded = true;
            theta = 0;
            drawGrid(loadedIm.getCanvas(), Color.green);
        } else {
            messageText.append("\nAutogrid failed.");
            // Try rotating plate a couple of times. Should take care of most minor cock-ups.
            if ((!rotated || theta != 3.0) && quantP != null
                    && quantP.autoRotateJCheckBox.isSelected()) {
                if (theta < 180) {
                    theta = 359.5 - theta;
                } else {
                    theta = 360 - theta;
                }
                rotated = true;
                imageLoad(currFile, true);
            } else {
                fullAutoFail++;
            }
        }
        loadedIm.getCanvas().requestFocus();
    }

    /**
     *
     */
    public void normalizeControl() {
//        double rav;
        ctrlData.setNormArea(new Double[maxCtrlSet + 1][maxCtrlPlate + 1][ctrlData.getRows() + 1][ctrlData.getCols() + 1]);
        normalizeData(ctrlData, minCtrlSet, maxCtrlSet, minCtrlPlate, maxCtrlPlate);

//        for (int s = minCtrlSet; s <= maxCtrlSet; s++) {
//            for (int p = minCtrlPlate; p <= maxCtrlPlate; p++) {
//                int median;
//                ArrayList<Integer> tmpSpots = new ArrayList<Integer>();
//                for (int i = 1; i <= ctrlData.getRows(); i++) {
//                    for (int j = 1; j <= ctrlData.getCols(); j++) {
//                        if (ctrlData.getArea()[s][p][i][j] == null) {
//                            JOptionPane.showMessageDialog(this, "Data missing "
//                                    + "for control plate (set " + s + ", plate "
//                                    + p + ").",
//                                    "Warning", JOptionPane.WARNING_MESSAGE);
//                            System.out.println("Row: " + i + ", Col " + j);
//                            return;
//                        }
//                        if (ctrlData.getArea()[s][p][i][j] != 0) {
//                            tmpSpots.add(ctrlData.getArea()[s][p][i][j]);
//                        }
//                    }
//                }
//                Collections.sort(tmpSpots);
//                median = tmpSpots.get(((tmpSpots.size() - 1) / 2));
//                for (int i = 1; i <= ctrlData.getRows(); i++) {
//                    for (int j = 1; j <= ctrlData.getCols(); j++) {
//                        ctrlData.getNormArea()[s][p][i][j] = (double) ctrlData.getArea()[s][p][i][j] / median;
//                    }
//                }
//            }
//        }
//        if (scoreRCJCheckBox.isSelected()) {
//            // Row/column correction
//
//            for (int s = minCtrlSet; s <= maxCtrlSet; s++) {
//                for (int p = minCtrlPlate; p <= maxCtrlPlate; p++) {
//                    double normFactor[][] = new double[ctrlData.getRows() + 1][ctrlData.getCols() + 1];
//                    for (int i = 1; i <= ctrlData.getRows(); i++) {
//                        ArrayList<Double> mdn2 = new ArrayList<Double>();
//                        for (int j = 1; j <= ctrlData.getCols(); j++) {
//                            if (ctrlData.getNormArea()[s][p][i][j] != 0) {
//                                mdn2.add(ctrlData.getNormArea()[s][p][i][j]);
//                            }
//                        }
//                        if (mdn2.size() > 0) {
//                            Collections.sort(mdn2);
//                            rav = mdn2.get(((mdn2.size() - 1) / 2));
//                            if (rav < 1.0) {
//                                rav = 1.0;
//                            }
//                            for (int j = 1; j <= ctrlData.getCols(); j++) {
//                                normFactor[i][j] = rav;
//                            }
//                        }
//                    }
//                    for (int i = 1; i <= ctrlData.getCols(); i++) {
//                        ArrayList<Double> mdn2 = new ArrayList<Double>();
//                        for (int j = 1; j <= ctrlData.getRows(); j++) {
//                            if (ctrlData.getNormArea()[s][p][j][i] != 0) {
//                                mdn2.add(ctrlData.getNormArea()[s][p][j][i]);
//                            }
//                        }
//                        if (mdn2.size() > 0) {
//                            Collections.sort(mdn2);
//                            rav = mdn2.get(((mdn2.size() - 1) / 2));
//                            if (rav < 1.0) {
//                                rav = 1.0;
//                            }
//                            for (int j = 1; j <= ctrlData.getRows(); j++) {
//                                normFactor[j][i] *= rav;
//                            }
//                        }
//                    }
//
//                    for (int i = 1; i <= ctrlData.getRows(); i++) {
//                        for (int j = 1; j <= ctrlData.getCols(); j++) {
//                            ctrlData.getNormArea()[s][p][i][j] /= normFactor[i][j];
//                        }
//                    }
//
//                    // Re-normalize
//                    ArrayList<Double> tmpSpots = new ArrayList<Double>();
//                    double median;
//                    for (int i = 1; i <= ctrlData.getRows(); i++) {
//                        for (int j = 1; j <= ctrlData.getCols(); j++) {
//                            if (ctrlData.getNormArea()[s][p][i][j] != 0) {
//                                tmpSpots.add(ctrlData.getNormArea()[s][p][i][j]);
//                            }
//                        }
//                    }
//                    Collections.sort(tmpSpots);
//                    median = tmpSpots.get(((tmpSpots.size() - 1) / 2));
//                    for (int i = 1; i <= ctrlData.getRows(); i++) {
//                        for (int j = 1; j <= ctrlData.getCols(); j++) {
//                            ctrlData.getNormArea()[s][p][i][j]
//                                    = ctrlData.getNormArea()[s][p][i][j] / median;
//                        }
//                    }
//                }
//            }
//        }
//        if (scoreSpatialJCheckBox.isSelected()) {
//            // Spatial corection
//
//            for (int s = minCtrlSet; s <= maxCtrlSet; s++) {
//                for (int p = minCtrlPlate; p <= maxCtrlPlate; p++) {
//
//                    double[] xval = new double[ctrlData.getCols()];
//                    double[] yval = new double[ctrlData.getCols()];
//
//                    // LOESS for horizontal direction
//                    for (int n = 0; n < ctrlData.getCols(); n++) {
//                        xval[n] = n;
//                        double[] cval = new double[ctrlData.getRows()];
//                        for (int nn = 0; nn < ctrlData.getRows(); nn++) {
//                            cval[nn] = ctrlData.getNormArea()[s][p][nn + 1][n + 1];
//                        }
//
//                        Arrays.sort(cval);
//
//                        yval[n] = cval[(cval.length - 1) / 2];
//
//                    }
//
//                    LoessInterpolator lin = new LoessInterpolator();
//                    double[] xloess = lin.smooth(xval, yval);
//
////                    StringBuilder outstring = new StringBuilder();
////                    for (int n = 0; n < xval.length; n++) {
////                        outstring.append(xloess[n]).append("\n");
////                    }
////                    System.out.println("Horiz Loess for 1,1,3,4:\n " + outstring);
//                    // LOESS for vert direction
//                    xval = new double[ctrlData.getRows()];
//                    yval = new double[ctrlData.getRows()];
//
//                    for (int n = 0; n < ctrlData.getRows(); n++) {
//                        xval[n] = n;
//
//                        double[] cval = new double[ctrlData.getCols()];
//                        for (int nn = 0; nn < ctrlData.getCols(); nn++) {
//                            cval[nn] = ctrlData.getNormArea()[s][p][n + 1][nn + 1];
//                        }
//
//                        Arrays.sort(cval);
//
//                        yval[n] = cval[(cval.length - 1) / 2];
//
//                    }
//
//                    lin = new LoessInterpolator();
//                    double[] yloess = lin.smooth(xval, yval);
//
////                    outstring = new StringBuilder();
////                    for (int n = 0; n < xval.length; n++) {
////                        outstring.append(yloess[n]).append("\n");
////                    }
////                    System.out.println("Vert Loess for 1,1,3,4:\n " + outstring);
//                    for (int i = 1; i <= ctrlData.getRows(); i++) {
//                        for (int j = 1; j <= ctrlData.getCols(); j++) {
//                            ctrlData.getNormArea()[s][p][i][j]
//                                    = ctrlData.getNormArea()[s][p][i][j] / (yloess[i - 1] * xloess[j - 1]);
//                        }
//                    }
//
//                }
//            }
//
//        }
//
//        if (scoreCompetitionJCheckBox.isSelected()) {
//            // Competition correction
//        }
        messageText.append("\nControl data normalized.");
//        if (ctrlData != null && expData != null) {
//            scorenameTextField.setText(ctrlData.getName() + "--" + expData.getName());
//        }
    }

    /**
     *
     */
    public void normalizeExperiment() {
//        double rav;
        // Normalize spot size
        expData.setNormArea(new Double[maxExpSet + 1][maxExpPlate + 1][expData.getRows() + 1][expData.getCols() + 1]);

        normalizeData(expData, minExpSet, maxExpSet, minExpPlate, maxExpPlate);

//        for (int s = minExpSet; s <= maxExpSet; s++) {
//            for (int p = minExpPlate; p <= maxExpPlate; p++) {
//                int median;
//                ArrayList<Integer> tmpSpots = new ArrayList<Integer>();
//                for (int i = 1; i <= expData.getRows(); i++) {
//                    for (int j = 1; j <= expData.getCols(); j++) {
//                        if (expData.getArea()[s][p][i][j] == null) {
//                            JOptionPane.showMessageDialog(this, "Data missing "
//                                    + "for experimental plate (set " + s + ", plate "
//                                    + p + ").", "Warning", JOptionPane.WARNING_MESSAGE);
//                            return;
//                        }
//                        if (expData.getArea()[s][p][i][j] != 0) {
//                            tmpSpots.add(expData.getArea()[s][p][i][j]);
//                        }
//                    }
//                }
//                Collections.sort(tmpSpots);
//                median = tmpSpots.get(((tmpSpots.size() - 1) / 2));
//                for (int i = 1; i <= expData.getRows(); i++) {
//                    for (int j = 1; j <= expData.getCols(); j++) {
//                        expData.getNormArea()[s][p][i][j] = (double) expData.getArea()[s][p][i][j] / median;
//                    }
//                }
//            }
//        }
        if (scoreRCJCheckBox.isSelected()) {
            // Do R/C correction

//
//            for (int s = minExpSet; s <= maxExpSet; s++) {
//                for (int p = minExpPlate; p <= maxExpPlate; p++) {
//                    double normFactor[][] = new double[expData.getRows() + 1][expData.getCols() + 1];
//                    for (int i = 1; i <= expData.getRows(); i++) {
//                        ArrayList<Double> mdn = new ArrayList<Double>();
//                        for (int j = 1; j <= expData.getCols(); j++) {
//                            if (expData.getNormArea()[s][p][i][j] != 0) {
//                                mdn.add(expData.getNormArea()[s][p][i][j]);
//                            }
//                        }
//                        if (mdn.size() > 0) {
//                            Collections.sort(mdn);
//                            rav = mdn.get(((mdn.size() - 1) / 2));
//                            if (rav < 1.0) {
//                                rav = 1.0;
//                            }
//                            for (int j = 1; j <= expData.getCols(); j++) {
//                                normFactor[i][j] = rav;
//                            }
//                        }
//                    }
//                    for (int i = 1; i <= expData.getCols(); i++) {
//                        ArrayList<Double> mdn = new ArrayList<Double>();
//                        for (int j = 1; j <= expData.getRows(); j++) {
//                            if (expData.getNormArea()[s][p][j][i] != 0) {
//                                mdn.add(expData.getNormArea()[s][p][j][i]);
//                            }
//                        }
//                        if (mdn.size() > 0) {
//                            Collections.sort(mdn);
//                            rav = mdn.get(((mdn.size() - 1) / 2));
//                            if (rav < 1.0) {
//                                rav = 1.0;
//                            }
//                            for (int j = 1; j <= expData.getRows(); j++) {
//                                normFactor[j][i] *= rav;
//                            }
//                        }
//                    }
//                    for (int i = 1; i <= expData.getRows(); i++) {
//                        for (int j = 1; j <= expData.getCols(); j++) {
//                            expData.getNormArea()[s][p][i][j] /= normFactor[i][j];
//                        }
//                    }
//                    ArrayList<Double> tmpSpots = new ArrayList<Double>();
//                    for (int i = 1; i <= expData.getRows(); i++) {
//                        for (int j = 1; j <= expData.getCols(); j++) {
//                            if (expData.getNormArea()[s][p][i][j] != 0) {
//                                tmpSpots.add(expData.getNormArea()[s][p][i][j]);
//                            }
//                        }
//                    }
//                    Collections.sort(tmpSpots);
//                    double median = tmpSpots.get(((tmpSpots.size() - 1) / 2));
//                    for (int i = 1; i <= expData.getRows(); i++) {
//                        for (int j = 1; j <= expData.getCols(); j++) {
//                            expData.getNormArea()[s][p][i][j]
//                                    = expData.getNormArea()[s][p][i][j] / median;
//                        }
//                    }
//                }
//            }
        }

//        if (scoreSpatialJCheckBox.isSelected()) {
//            // Spatial corection
//
//            for (int s = minExpSet; s <= maxExpSet; s++) {
//                for (int p = minExpPlate; p <= maxExpPlate; p++) {
//
//                    double[] xval = new double[expData.getCols()];
//                    double[] yval = new double[expData.getCols()];
//
//                    // LOESS for horizontal direction
//                    for (int n = 0; n < expData.getCols(); n++) {
//                        xval[n] = n;
//                        double[] cval = new double[expData.getRows()];
//                        for (int nn = 0; nn < expData.getRows(); nn++) {
//                            cval[nn] = expData.getNormArea()[s][p][nn + 1][n + 1];
//                        }
//
//                        Arrays.sort(cval);
//
//                        yval[n] = cval[(cval.length - 1) / 2];
//
//                    }
//
//                    LoessInterpolator lin = new LoessInterpolator();
//                    double[] xloess = lin.smooth(xval, yval);
//
//                    StringBuilder outstring = new StringBuilder();
//                    for (int n = 0; n < xval.length; n++) {
//                        outstring.append(xloess[n]).append("\n");
//                    }
//
////                    System.out.println("Horiz Loess for 1,1,3,4:\n " + outstring);
//                    // LOESS for vert direction
//                    xval = new double[expData.getRows()];
//                    yval = new double[expData.getRows()];
//
//                    for (int n = 0; n < expData.getRows(); n++) {
//                        xval[n] = n;
//
//                        double[] cval = new double[expData.getCols()];
//                        for (int nn = 0; nn < expData.getCols(); nn++) {
//                            cval[nn] = expData.getNormArea()[s][p][n + 1][nn + 1];
//                        }
//
//                        Arrays.sort(cval);
//
//                        yval[n] = cval[(cval.length - 1) / 2];
//
//                    }
//
//                    lin = new LoessInterpolator();
//                    double[] yloess = lin.smooth(xval, yval);
//
//                    outstring = new StringBuilder();
//                    for (int n = 0; n < xval.length; n++) {
//                        outstring.append(yloess[n]).append("\n");
//                    }
//
////                    System.out.println("Vert Loess for 1,1,3,4:\n " + outstring);
//                    for (int i = 1; i <= expData.getRows(); i++) {
//                        for (int j = 1; j <= expData.getCols(); j++) {
//                            expData.getNormArea()[s][p][i][j]
//                                    = expData.getNormArea()[s][p][i][j] / (yloess[i - 1] * xloess[j - 1]);
//                        }
//                    }
//
//                }
//            }
//
////        }
//        if (scoreCompetitionJCheckBox.isSelected()) {
//            // Competition correction
//        }
        messageText.append("\nExperiment data normalized.");
//        if (ctrlData != null && expData != null) {
//            scorenameTextField.setText(ctrlData.getName() + "--" + expData.getName());
//        }
    }

    private void normalizePlate(spotData data, int s, int p) {
        int median = 0;
        boolean forceMedian = false;

        if (normORFButton.isSelected()) {
            String ctrlORF = normORFJTextField.getText().toUpperCase().trim();
            ArrayList<Integer> ctrlSpots = new ArrayList<>();
            for (int i = 1; i <= data.getRows(); i++) {
                for (int j = 1; j <= data.getCols(); j++) {
                    if (keyOrfs[p][i][j].toUpperCase().equals(ctrlORF)
                            || keyGenes[p][i][j].toUpperCase().equals(ctrlORF)) {
                        if (data.getArea()[s][p][i][j] > 0) {
                            ctrlSpots.add(data.getArea()[s][p][i][j]);
                        }
                    }
                }
            }

            if (ctrlSpots.isEmpty()) {
                messageText.append("\nError on plate ").append(p).append(" - no control spots found; using plate median.");
                forceMedian = true;
            } else {
                Collections.sort(ctrlSpots);
                median = ctrlSpots.get((ctrlSpots.size() - 1) / 2);
            }
        }

        if (normPlateMedianButton.isSelected() || forceMedian) {
            // Get median value for plate

            ArrayList<Integer> tmpSpots = new ArrayList<>();
            for (int i = 1; i <= data.getRows(); i++) {
                for (int j = 1; j <= data.getCols(); j++) {
                    if (data.getArea()[s][p][i][j] == null) {
                        JOptionPane.showMessageDialog(this, "Data missing "
                                + "for experimental plate (set " + s + ", plate "
                                + p + ").", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (data.getArea()[s][p][i][j] != 0) {
                        tmpSpots.add(data.getArea()[s][p][i][j]);
                    }
                }
            }
            Collections.sort(tmpSpots);
            median = tmpSpots.get((tmpSpots.size() - 1) / 2);

        }

        if (normNoneButton.isSelected()) {
            median = 1;
        }

        if (median != 0) {
            for (int i = 1; i <= data.getRows(); i++) {
                for (int j = 1; j <= data.getCols(); j++) {
                    data.getNormArea()[s][p][i][j] = (double) data.getArea()[s][p][i][j] / median;
                }
            }
        }
    }

    // Normalize data on each plate
    private void normalizeData(spotData data, int minSet, int maxSet, int minPlate, int maxPlate) {
//        Double[][][][] newData = Arrays.copyOf(data.getNormArea(), data.getNormArea().length);
        double rav;

        // For each set...
        for (int s = minSet; s <= maxSet; s++) {

            // For each plate: per-plate normalization to convert from absolute to relative pixel sizes
            int median = 0;

            // Separate function in case we want to re-do this
            for (int p = minPlate; p <= maxPlate; p++) {
                normalizePlate(data, s, p);

            } // plate
        } // set

        // Do optional corrections:
        if (scoreRCJCheckBox.isSelected()) {
            // Row/column correction

            String np = scoreNormPercentileTextField.getText();
            int normPerc;

            try {
                normPerc = Integer.parseInt(np);
            } catch (NumberFormatException e) {
                normPerc = 50;
            }

            if (normPerc < 1 || normPerc > 100) {
                normPerc = 50;
                scoreNormPercentileTextField.setText("50");
            }

            for (int s = minSet; s <= maxSet; s++) {
                for (int p = minPlate; p <= maxPlate; p++) {
                    double normFactor[][] = new double[data.getRows() + 1][data.getCols() + 1];
                    for (int i = 1; i <= data.getRows(); i++) {
                        ArrayList<Double> mdn2 = new ArrayList<>();
                        for (int j = 1; j <= data.getCols(); j++) {
                            if (data.getNormArea()[s][p][i][j] != 0) {
                                mdn2.add(data.getNormArea()[s][p][i][j]);
                            }
                        }
                        if (mdn2.size() > 0) {
                            Collections.sort(mdn2);
                            int percentile = (mdn2.size() * normPerc) / 100;
                            rav = mdn2.get(percentile);
                            if (!scoreUndergrowthJCheckBox.isSelected()) {
                                if (rav < 1.0) {
                                    rav = 1.0;
                                }
                            }
//                            System.out.println(rav);

                            for (int j = 1; j <= data.getCols(); j++) {
                                normFactor[i][j] = rav;
                            }

                        }
                    }
                    for (int i = 1; i <= data.getCols(); i++) {
                        ArrayList<Double> mdn2 = new ArrayList<>();
                        for (int j = 1; j <= data.getRows(); j++) {
                            if (data.getNormArea()[s][p][j][i] != 0) {
                                mdn2.add(data.getNormArea()[s][p][j][i]);
                            }
                        }
                        if (mdn2.size() > 0) {
                            Collections.sort(mdn2);
                            int percentile = (mdn2.size() * normPerc) / 100;
                            rav = mdn2.get(percentile);
                            if (!scoreUndergrowthJCheckBox.isSelected()) {
                                if (rav < 1.0) {
                                    rav = 1.0;
                                }
//                                System.out.println(rav);
                            }
                            for (int j = 1; j <= data.getRows(); j++) {
                                normFactor[j][i] *= rav;

                            }
                        }
                    }

                    for (int i = 1; i <= data.getRows(); i++) {
                        for (int j = 1; j <= data.getCols(); j++) {
                            data.getNormArea()[s][p][i][j] /= normFactor[i][j];
                        }
                    }

                    // Re-normalize (centre)
                    if (normPlateMedianButton.isSelected()) {
                        ArrayList<Double> tmpSpots = new ArrayList<>();
                        double median;
                        for (int i = 1; i <= data.getRows(); i++) {
                            for (int j = 1; j <= data.getCols(); j++) {
                                if (data.getNormArea()[s][p][i][j] != 0) {
                                    tmpSpots.add(data.getNormArea()[s][p][i][j]);
                                }
                            }
                        }
                        Collections.sort(tmpSpots);
                        median = tmpSpots.get(((tmpSpots.size() - 1) / 2));
                        for (int i = 1; i <= data.getRows(); i++) {
                            for (int j = 1; j <= data.getCols(); j++) {
                                data.getNormArea()[s][p][i][j]
                                        = data.getNormArea()[s][p][i][j] / median;
                            }
                        }
                    }
                }
            }
        }

        if (scoreSpatialJCheckBox.isSelected()) {
            // Spatial corection

            for (int s = minSet; s <= maxSet; s++) {
                for (int p = minPlate; p <= maxPlate; p++) {

                    double[] xval = new double[data.getCols()];
                    double[] yval = new double[data.getCols()];

                    // LOESS for horizontal direction
                    for (int n = 0; n < data.getCols(); n++) {
                        xval[n] = n;
                        double[] cval = new double[data.getRows()];
                        for (int nn = 0; nn < data.getRows(); nn++) {
                            cval[nn] = data.getNormArea()[s][p][nn + 1][n + 1];
                        }

                        Arrays.sort(cval);

                        yval[n] = cval[(cval.length - 1) / 2];

                    }

                    LoessInterpolator lin = new LoessInterpolator();
                    double[] xloess = lin.smooth(xval, yval);

//                    StringBuilder outstring = new StringBuilder();
//                    for (int n = 0; n < xval.length; n++) {
//                        outstring.append(xloess[n]).append("\n");
//                    }
//                    System.out.println("Horiz Loess for 1,1,3,4:\n " + outstring);
                    // LOESS for vert direction
                    xval = new double[data.getRows()];
                    yval = new double[data.getRows()];

                    for (int n = 0; n < data.getRows(); n++) {
                        xval[n] = n;

                        double[] cval = new double[data.getCols()];
                        for (int nn = 0; nn < data.getCols(); nn++) {
                            cval[nn] = data.getNormArea()[s][p][n + 1][nn + 1];
                        }

                        Arrays.sort(cval);

                        yval[n] = cval[(cval.length - 1) / 2];

                    }

                    lin = new LoessInterpolator();
                    double[] yloess = lin.smooth(xval, yval);

//                    outstring = new StringBuilder();
//                    for (int n = 0; n < xval.length; n++) {
//                        outstring.append(yloess[n]).append("\n");
//                    }
//                    System.out.println("Vert Loess for 1,1,3,4:\n " + outstring);
                    for (int i = 1; i <= data.getRows(); i++) {
                        for (int j = 1; j <= data.getCols(); j++) {
                            data.getNormArea()[s][p][i][j]
                                    = data.getNormArea()[s][p][i][j] / (yloess[i - 1] * xloess[j - 1]);
                        }
                    }

                }
            }

        }

    }

    /**
     *
     */
    public void doThresh() {
        int i;
        if (loaded == false) {
            return;
        }
        if (threshed == true) {
            loadedIm.changes = false;
            loadedIm.close();
            imageLoad(currFile, false);
            if (!autoThreshJCheckBox.isSelected()) {
                doThresh();
            }
        }
        ImageProcessor ip;
        int l = loadedIm.getHeight() / 16;
        int w = loadedIm.getWidth() / 16;
        loadedIm.setRoi(w, l, w * 14, l * 14);
        ip = loadedIm.getProcessor().crop();
        if (threshRadioAuto.isSelected() == true) {

            int j[] = ip.getHistogram();
            messageText.append("\nGetting threshold");
            AutoThresholder aT = new AutoThresholder();
            i = aT.getThreshold(AutoThresholder.Method.Default, j);
            messageText.append(" = ").append(i);
//            }
            imageThreshManualJTextField.setText(Integer.toString(i));
            threshRadioAuto.setSelected(true);
            messageText.append("\nApplying threshold.");
            ip = loadedIm.getProcessor();
            ip.threshold(i);
            loadedIm.setOverlay(null);
            lastThresh = i;
            loadedIm.setWindow(imWin);
            loadedIm.repaintWindow();
            threshed = true;
        } else {
            i = Integer.parseInt(imageThreshManualJTextField.getText());
            if (i == 0) {
                loadedIm.killRoi();
                return;
            }

            if (i > 255) {
                i = 255;
            }

            ip.resetRoi();
            loadedIm.setOverlay(null);
            messageText.append("\nApplying threshold of ").append(i).append(".");
            ip = loadedIm.getProcessor();
            ip.threshold(i);
            lastThresh = i;
            threshed = true;
            loadedIm.repaintWindow();
        }

//        if (i != 0) {
//            imageSettings is = new imageSettings();
//            if (iSet.containsKey(currFile)) {
//                is = iSet.get(currFile);
//            }
//            is.thresh = i;
//            iSet.put(currFile, is);
//        }
        loadedIm.killRoi();
    }

    public static String getFancyDesc(String desc) {
        StringTokenizer st = new StringTokenizer(desc, " ,;:/\"().?[]{}");

        while (st.hasMoreTokens()) {
            String s = st.nextToken();

            for (String myorf : allSGDInfo.keySet()) {
                if (myorf.toLowerCase().equals(s.toLowerCase())) {
                    desc = desc.replace(s, "<u><font color='blue'>" + s + "</font></u>");
//                    sI.jEditorPane1.getDocument().putProperty(s, true);
                }
            }
        }

        st = new StringTokenizer(desc, " ,;:/\"().?[]{}-");

        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (s.endsWith("p")) {
                s = s.substring(0, s.length() - 1);
            }

            for (String myorf : allSGDInfo.keySet()) {
                if (allSGDInfo.get(myorf).gene.toLowerCase().equals(s.toLowerCase())) {
                    desc = desc.replace(s, "<u><font color='blue'>" + s + "</font></u>");
//                    sI.jEditorPane1.getDocument().putProperty(s, true);
                }
            }
        }
        return "<html><font face = 'Tahoma, Helvetica'>" + desc + "</font></html>";
    }

    /**
     * @param args the command line arguments
     */
    public void main(final String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            //                Balony b = new Balony();
            pack();
            setVisible(true);
            switch (args[0]) {
                case "1":
                    osLAFJRadioButton.setSelected(true);
                    break;
                case "2":
                    nimbusLAFJRadioButton.setSelected(true);
                    break;
                default:
                    javaLAFJRadioButton.setSelected(true);
                    break;
            }
        });
    }

    /**
     *
     * @param str
     * @return
     */
    public static String getOnlyNumerics(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder strBuff = new StringBuilder();
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Character.isDigit(c)) {
                strBuff.append(c);
            }
        }
        return strBuff.toString();

    }

    /**
     *
     * @author Barry Young
     */
    public class ManualGrid implements MouseListener, MouseMotionListener, Runnable, KeyListener {

        /**
         *
         */
        public ImageCanvas i;
        public int cnt,
                /**
                 *
                 */
                /**
                 *
                 */
                x[], y[];

        /**
         *
         */
        public GeneralPath path;

        /**
         *
         * @param ic
         */
        public void ManualGrid(ImageCanvas ic) {
            resetListeners(ic);
            ic.addMouseListener(this);
            ic.addMouseMotionListener(this);
            i = ic;
            cnt = 0;
            path = new GeneralPath();
            x = new int[2];
            y = new int[2];
            Cursor c = new Cursor(Cursor.CROSSHAIR_CURSOR);
            i.setCursor(c);
            messageText.append("\nManual gridding: Click one corner of the array.");
        }

        /**
         *
         * @param ic
         */
        public void reset(ImageCanvas ic) {
            Cursor c = new Cursor(Cursor.DEFAULT_CURSOR);
            ic.setCursor(c);
            ic.removeMouseListener(this);
        }

        @Override
        public void keyReleased(KeyEvent k) {
        }

        @Override
        public void keyPressed(KeyEvent k) {
        }

        @Override
        public void keyTyped(KeyEvent k) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (cnt < 2) {
                Cursor c = new Cursor(Cursor.CROSSHAIR_CURSOR);
                i.setCursor(c);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            x[cnt] = e.getX();
            x[cnt] = i.offScreenX(x[cnt]);
            y[cnt] = e.getY();
            y[cnt] = i.offScreenY(y[cnt]);
            cnt++;
            if (pointgridCheckBox.isSelected() == true && cnt == 1) {
                x[1] = (int) (x[0] - dx * (cols - 1));
                if (x[1] < 0) {
                    x[1] = (int) (x[0] + dx * (cols - 1));
                }
                y[1] = (int) (y[0] - dy * (rows - 1));
                if (y[1] < 0) {
                    y[1] = (int) (y[0] + dy * (rows - 1));
                }
                if (x[1] < loadedIm.getWidth() && y[1] < loadedIm.getHeight()) {
                    cnt++;
                } else {
                    messageText.append("\nOne-point gridding failed.");
                }
            }
            if (cnt == 2) {
                messageText.append("\nManual gridding: Grid defined.");
                reset(i);
                if (x[0] > x[1]) {
                    maxX = (float) x[0];
                    minX = (float) x[1];
                } else {
                    maxX = (float) x[1];
                    minX = (float) x[0];
                }
                if (y[0] > y[1]) {
                    maxY = (float) y[0];
                    minY = (float) y[1];
                } else {
                    maxY = (float) y[1];
                    minY = (float) y[0];
                }
                stepX = (maxX - minX) / (cols - 1);
                stepY = (maxY - minY) / (rows - 1);
                gridded = true;
                drawGrid(i, Color.green);
                if (incalibration) {
                    gridP.setVisible(true);
                    gridP.doCalibrate();
                    gridP.doSave();
                }

                if (zoomBox != null && zoomBox.getWindow() != null) {
                    zoomBox.close();
                }

            } else {
                messageText.append("\nManual gridding: Now click the diagonal opposite corner.");
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

            if (zoomBox != null) {
                float x0 = (float) (i.offScreenX(e.getX()));
                float y0 = (float) (i.offScreenY(e.getY()));
                int zx = (int) (x0 - 100);
                int zy = (int) (y0 - 100);
                int xh = 100;
                int yh = 100;

                if (zx < 0) {
                    xh += zx;
                    zx = 0;
                }

                if (zy < 0) {
                    yh += zy;
                    zy = 0;
                }

                ImageProcessor ip = loadedIm.getProcessor();
                ip.setRoi(zx, zy, 200, 200);
                zoomBox.setProcessor(ip.crop());
                GeneralPath xpath = new GeneralPath();
                xpath.moveTo(0, yh);
                xpath.lineTo(200, yh);
                xpath.moveTo(xh, 0);
                xpath.lineTo(xh, 200);
                zoomBox.setOverlay(xpath, Color.cyan, null);
            }

            if (pointgridCheckBox.isSelected() && cnt == 0) {
                float x0 = (float) (i.offScreenX(e.getX()));
                float y0 = (float) (i.offScreenY(e.getY()));
                float x1 = (x0 - dx * (cols - 1));

                if (x1 < 0) {
                    x1 = (x0 + dx * (cols - 1));
                }
                float y1 = (y0 - dy * (rows - 1));
                if (y1 < 0) {
                    y1 = (y0 + dy * (rows - 1));
                }
                path = new GeneralPath();
                for (int jj = 0; jj < cols; jj++) {
                    path.moveTo(x0 + jj * ((x1 - x0) / (cols - 1)), y0);
                    path.lineTo(x0 + jj * ((x1 - x0) / (cols - 1)), y1);
                }
                for (int jj = 0; jj < rows; jj++) {
                    path.moveTo(x0, y0 + jj * ((y1 - y0) / (rows - 1)));
                    path.lineTo(x1, y0 + jj * ((y1 - y0) / (rows - 1)));
                }
                i.getImage().setOverlay(path, Color.red, null);
            }
            if (cnt == 1) {
                int mX = i.offScreenX(e.getX());
                float myX = (float) mX;
                int mY = i.offScreenY(e.getY());
                float myY = (float) mY;
                path = new GeneralPath();
                for (int jj = 0; jj < cols; jj++) {
                    path.moveTo(x[0] + jj * ((myX - x[0]) / (cols - 1)), y[0]);
                    path.lineTo(x[0] + jj * ((myX - x[0]) / (cols - 1)), myY);
                }
                for (int jj = 0; jj < rows; jj++) {
                    path.moveTo(x[0], y[0] + jj * ((myY - y[0]) / (rows - 1)));
                    path.lineTo(myX, y[0] + jj * ((myY - y[0]) / (rows - 1)));
                }
                i.getImage().setOverlay(path, Color.red, null);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void run() {
        }
    }

    /**
     *
     */
    public class QuantScan implements MouseListener, MouseMotionListener, MouseWheelListener, Runnable, KeyListener {

        /**
         *
         */
        public ImageCanvas i;
        public int cnt,
                /**
                 *
                 */
                /**
                 *
                 */
                x[], y[];
        GeneralPath path;
        Cursor c;

        /**
         *
         * @param ic
         */
        public void QuantScan(ImageCanvas ic) {
            if (oIm != null && oIm.getWindow() != null) {
                mwl = new myWindowListener();
                oIm.getWindow().addWindowListener(mwl);
            }

            for (MouseListener m : ic.getMouseListeners()) {
                ic.removeMouseListener(m);
            }
            for (MouseWheelListener m : ic.getMouseWheelListeners()) {
                ic.removeMouseWheelListener(m);
            }
            for (MouseMotionListener m : ic.getMouseMotionListeners()) {
                ic.removeMouseMotionListener(m);
            }
            ic.addMouseMotionListener(this);
            ic.addMouseWheelListener(this);
            ic.addMouseListener(this);

            for (KeyListener k : ic.getKeyListeners()) {
                ic.removeKeyListener(k);
            }

            ic.addKeyListener(this);

            i = ic;
            cnt = 0;
            path = new GeneralPath();
            x = new int[2];
            y = new int[2];
        }

        /**
         *
         * @param ic
         */
        public void reset(ImageCanvas ic) {
            ic.removeMouseListener(this);
        }

        @Override
        public void keyPressed(KeyEvent k) {
            if (i.getImage() == loadedIm) {
                if (k.getKeyCode() == KeyEvent.VK_UP) {
                    doGridNudgeUp(1);
                }
                if (k.getKeyCode() == KeyEvent.VK_LEFT) {
                    doGridNudgeLeft(1);
                }
                if (k.getKeyCode() == KeyEvent.VK_DOWN) {
                    doGridNudgeDown(1);
                }
                if (k.getKeyCode() == KeyEvent.VK_RIGHT) {
                    doGridNudgeRight(1);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent k) {
        }

        @Override
        public void keyTyped(KeyEvent k) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            i.setCursor(c);
            if (e.getButton() == MouseEvent.BUTTON2) {
                doToggleInputOutput();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            c = i.getCursor();
            x[0] = i.offScreenX(e.getX());
            y[0] = i.offScreenY(e.getY());
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int xx, yy;
            xx = Math.round((i.offScreenX(e.getX()) - minX) / stepX) + 1;
            yy = Math.round((i.offScreenY(e.getY()) - minY) / stepY) + 1;
            if (xx > 0 && xx <= cols && yy > 0 && yy <= rows && cSize != null) {
                cSize.posrowTextField.setText(Integer.toString(yy));
                cSize.poscolTextField.setText(Integer.toString(xx));
                cSize.posareaTextField.setText(Integer.toString(Area[xx][yy]));
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            i.setCursor(new Cursor(Cursor.HAND_CURSOR));
            x[1] = i.offScreenX(e.getX());
            y[1] = i.offScreenY(e.getY());
            int dx = x[1] - x[0];
            int dy = y[1] - y[0];
            Rectangle r = i.getSrcRect();
            r.x -= dx;
            if (r.x < 0) {
                r.x = 0;
            }
            if (r.x + r.width > i.getImage().getWidth()) {
                r.x = i.getImage().getWidth() - r.width;
            }
            r.y -= dy;
            if (r.y < 0) {
                r.y = 0;
            }
            if (r.y + r.height > i.getImage().getHeight()) {
                r.y = i.getImage().getHeight() - r.height;
            }
            i.setSourceRect(r);
            oIm.repaintWindow();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int j = e.getWheelRotation();
            int x1 = e.getX();
            int y1 = e.getY();
            if (j > 0) {
                i.zoomOut(x1, y1);
            } else {
                i.zoomIn(x1, y1);
            }
        }

        @Override
        public void run() {
        }
    }

    /**
     *
     */
    public class myWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            modCheck();
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }

    /**
     *
     * @param i
     */
    public void resetListeners(ImageCanvas i) {
        try {
            if (i.getMouseListeners() != null) {
                for (MouseListener m : i.getMouseListeners()) {
                    i.removeMouseListener(m);
                }
            }
            for (MouseMotionListener m : i.getMouseMotionListeners()) {
                i.removeMouseMotionListener(m);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        if (oIm != null) {
            try {
                for (MouseListener m : oIm.getCanvas().getMouseListeners()) {
                    oIm.getCanvas().removeMouseListener(m);
                }
                for (MouseMotionListener m : oIm.getCanvas().getMouseMotionListeners()) {
                    oIm.getCanvas().removeMouseMotionListener(m);
                }
                for (MouseWheelListener m : oIm.getCanvas().getMouseWheelListeners()) {
                    oIm.getCanvas().removeMouseWheelListener(m);
                }
                qs = new QuantScan();
                qs.QuantScan(oIm.getCanvas());
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());

            }
        }
    }

    /**
     *
     */
    public class RotateCorrector implements MouseListener, MouseMotionListener, Runnable, KeyListener {

        /**
         *
         */
        public ImageCanvas i;

        /**
         *
         */
        public int cnt;
        public float x[],
                /**
                 *
                 */
                y[];
        GeneralPath path;

        /**
         *
         * @param ic
         */
        public void RotateCorrector(ImageCanvas ic) {
            resetListeners(ic);
            ic.addMouseListener(this);
            ic.addMouseMotionListener(this);
            i = ic;
            cnt = 0;
            path = new GeneralPath();
            x = new float[2];
            y = new float[2];
            Cursor c = new Cursor(Cursor.CROSSHAIR_CURSOR);
            i.setCursor(c);
        }

        /**
         *
         * @param ic
         */
        public void reset(ImageCanvas ic) {
            Cursor c = new Cursor(Cursor.DEFAULT_CURSOR);
            i.setCursor(c);
            ic.removeMouseListener(this);
            ic.removeMouseMotionListener(this);
        }

        @Override
        public void keyReleased(KeyEvent k) {
        }

        @Override
        public void keyPressed(KeyEvent k) {
        }

        @Override
        public void keyTyped(KeyEvent k) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (cnt < 2) {
                Cursor c = new Cursor(Cursor.CROSSHAIR_CURSOR);
                i.setCursor(c);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int myX = i.offScreenX(e.getX());
            int myY = i.offScreenY(e.getY());
            x[cnt] = myX;
            y[cnt] = myY;
            ImageProcessor ip = i.getImage().getProcessor();
            ip.resetRoi();
            loadedIm.setRoi((int) (myX - dx / 2), (int) (myY - dy / 2), (int) dx, (int) dy);
            if (threshed) {
                int minpix = Integer.parseInt(quantP.minpixelsTextField.getText());
                if (minpix < 1) {
                    minpix = 8;
                    quantP.minpixelsTextField.setText("8");
                }
                ResultsTable rt = new ResultsTable();
                ParticleAnalyzer pA;
                pA = new ParticleAnalyzer(0, -1, rt, minpix, dx * dy, 0.8, 1.0);
                pA.setHideOutputImage(true);
                pA.analyze(loadedIm);
                if (rt.getColumn(0) != null && rt.getColumn(0).length > 0 && threshed) {
                    float area = (float) Tools.getMinMax(rt.getColumn(rt.getColumnIndex(AREA_COL)))[0];
                    for (int ii = 0; ii < rt.getColumn(1).length; ii++) {
                        if (rt.getColumn(rt.getColumnIndex(AREA_COL))[ii] == area) {
                            x[cnt] = rt.getColumn(rt.getColumnIndex(RT_X))[ii];
                            y[cnt] = rt.getColumn(rt.getColumnIndex(RT_Y))[ii];
                        }
                    }
                }
            }
            double r, ar;
            path.moveTo(x[cnt], 0);
            path.lineTo(x[cnt], i.offScreenY(i.getHeight()));
            path.moveTo(0, y[cnt]);
            path.lineTo(i.offScreenX(i.getWidth()), y[cnt]);
            i.getImage().setOverlay(path, Color.magenta, null);
            cnt++;
            if (cnt == 1) {
                messageText.append("\nNow click another spot at the opposite end of the same row.");
            }
            if (cnt == 2) {
                r = (double) (y[0] - y[1]) / (x[1] - x[0]);
                ar = Math.abs(r);
                theta = Math.atan(ar);
                theta = Math.toDegrees(ar);
                ip = loadedIm.getProcessor();
                ip.setInterpolationMethod(ImageProcessor.BICUBIC);
                ip.setBackgroundValue((double) 0);
                if (ar != r) {
                    theta = 360 - theta;
                }
                rotated = true;
                new imageLoader().execute();
                reset(i);
                i.getImage().setOverlay(null);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (cnt == 1) {
                path = new GeneralPath();
                int myX = i.offScreenX(e.getX());
                int myY = i.offScreenY(e.getY());
                path.moveTo(x[0], y[0]);
                path.lineTo(myX, myY);
            }
            i.getImage().setOverlay(path, Color.cyan, null);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void run() {
        }
    }

    /**
     *
     */
    public class ManualSpot implements MouseListener, MouseMotionListener, Runnable, KeyListener {

        /**
         *
         */
        public ImageCanvas i;
        public int cnt,
                /**
                 *
                 */
                x[],
                /**
                 *
                 */
                y[];
        GeneralPath path;
        Overlay o;

        /**
         *
         * @param ic
         */
        public void ManualSpot(ImageCanvas ic) {
            resetListeners(ic);
            ic.addMouseListener(this);
            ic.addMouseMotionListener(this);
            i = ic;
            cnt = 0;
            path = new GeneralPath();
            x = new int[3];
            y = new int[3];
            Cursor c = new Cursor(Cursor.CROSSHAIR_CURSOR);
            i.setCursor(c);
        }

        /**
         *
         * @param ic
         */
        public void reset(ImageCanvas ic) {
            Cursor c = new Cursor(Cursor.DEFAULT_CURSOR);
            ic.setCursor(c);
            ic.removeMouseListener(this);
            ic.removeMouseMotionListener(this);
        }

        @Override
        public void keyReleased(KeyEvent k) {
        }

        @Override
        public void keyPressed(KeyEvent k) {
        }

        @Override
        public void keyTyped(KeyEvent k) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (cnt < 2) {
                Cursor c = new Cursor(Cursor.CROSSHAIR_CURSOR);
                i.setCursor(c);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            x[cnt] = e.getX();
            x[cnt] = i.offScreenX(x[cnt]);
            y[cnt] = e.getY();
            y[cnt] = i.offScreenY(y[cnt]);
            cnt++;
            if (cnt == 1) {
                o = oIm.getOverlay();
            }
            if (cnt == 2) {
                int dx = Math.abs(x[1] - x[0]);
                int dy = Math.abs(y[1] - y[0]);
                double mArea = Math.PI * dx * dy;
                int xc = Math.round(((x[0] - minX) / stepX) + 1);
                int yc = Math.round(((y[0] - minY) / stepY) + 1);
                Area[xc][yc] = (int) mArea;

                String coord = xc + ":" + yc;
                grOval.remove(coord);
                Integer[] ov = new Integer[4];
                ov[0] = (x[0] - dx);
                ov[1] = (y[0] - dy);
                ov[2] = dx * 2;
                ov[3] = dy * 2;

                xCoord[xc][yc] = (int) ov[0];
                yCoord[xc][yc] = (int) ov[1];
                width[xc][yc] = (int) ov[2];
                height[xc][yc] = (int) ov[3];

                mgOval.put(coord, ov);
                fileMod = true;
                if (redRect.keySet().contains(coord)) {
                    redRect.remove(coord);
                    badSpots--;
                }
                drawShapes();
                cnt = 0;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (i.getCursor().getType() != Cursor.CROSSHAIR_CURSOR) {
                i.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }
            if (cnt < 1) {
                return;
            }
            int dx, dy;
            dx = e.getX();
            dx = i.offScreenX(dx);
            dx = Math.abs(dx - x[0]);
            dy = e.getY();
            dy = i.offScreenY(dy);
            dy = Math.abs(dy - y[0]);
            Roi r = new OvalRoi(x[0] - dx, y[0] - dy, dx * 2, dy * 2);
            r.setStrokeColor(Color.magenta);
            Overlay oo = new Overlay();
            for (Roi rr : o.toArray()) {
                oo.add(rr);
            }
            oo.add(r);
            oIm.setOverlay(oo);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void run() {
        }
    }

    /**
     *
     */
    public class ManualSpotRemover implements MouseListener, MouseMotionListener, Runnable, KeyListener {

        /**
         *
         */
        public ImageCanvas i;
        public int x,
                /**
                 *
                 */
                y;

        /**
         *
         * @param ic
         */
        public void ManualSpot(ImageCanvas ic) {
            resetListeners(ic);
            ic.addMouseListener(this);
            i = ic;
            Cursor c = new Cursor(Cursor.CROSSHAIR_CURSOR);
            i.setCursor(c);
        }

        /**
         *
         * @param ic
         */
        public void reset(ImageCanvas ic) {
            Cursor c = new Cursor(Cursor.DEFAULT_CURSOR);
            ic.setCursor(c);
            ic.removeMouseListener(this);
            ic.removeMouseMotionListener(this);
        }

        @Override
        public void keyReleased(KeyEvent k) {
        }

        @Override
        public void keyPressed(KeyEvent k) {
        }

        @Override
        public void keyTyped(KeyEvent k) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Cursor c = new Cursor(Cursor.CROSSHAIR_CURSOR);
            i.setCursor(c);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            x = e.getX();
            x = i.offScreenX(x);
            y = e.getY();
            y = i.offScreenY(y);
            int xc = Math.round(((x - minX) / stepX) + 1);
            int yc = Math.round(((y - minY) / stepY) + 1);
            Area[xc][yc] = 0;
            String coord = xc + ":" + yc;
            grOval.remove(coord);
            mgOval.remove(coord);
            fileMod = true;
            if (redRect.keySet().contains(coord)) {
                redRect.remove(coord);
                badSpots--;
            }
            drawShapes();
            QuantScan qs = new QuantScan();
            qs.QuantScan(oIm.getCanvas());
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void run() {
        }
    }

    /**
     *
     */
    public class Zoomer implements MouseListener, MouseMotionListener, Runnable, KeyListener {

        /**
         *
         */
        public ImageCanvas imgCanvas;

        /**
         *
         */
        public int d;

        /**
         *
         * @param ic
         * @param dir
         */
        public void Zoomer(ImageCanvas ic, int dir) {
            resetListeners(ic);
            ic.addMouseListener(this);
            ic.addMouseMotionListener(this);
            imgCanvas = ic;
            d = dir;
            Cursor c = new Cursor(Cursor.CROSSHAIR_CURSOR);
            imgCanvas.setCursor(c);
        }

        /**
         *
         * @param ic
         */
        public void reset(ImageCanvas ic) {
            Cursor c = new Cursor(Cursor.DEFAULT_CURSOR);
            ic.setCursor(c);
            ic.removeMouseListener(this);
        }

        @Override
        public void keyReleased(KeyEvent k) {
        }

        @Override
        public void keyPressed(KeyEvent k) {
        }

        @Override
        public void keyTyped(KeyEvent k) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Cursor c = new Cursor(Cursor.CROSSHAIR_CURSOR);
            imgCanvas.setCursor(c);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if ((e.getButton() == MouseEvent.BUTTON1 && d == 0)
                    || (e.getButton() == MouseEvent.BUTTON3 && d == 1)) {
                int x = e.getX();
                int y = e.getY();
                imgCanvas.zoomIn(x, y);
            }
            if ((e.getButton() == MouseEvent.BUTTON1 && d == 1)
                    || (e.getButton() == MouseEvent.BUTTON3 && d == 0)) {
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void run() {
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel advancedPanel;
    private javax.swing.JRadioButton altColRadioButton;
    private javax.swing.JRadioButton altEvenRadioButton;
    private javax.swing.JRadioButton altOddRadioButton;
    private javax.swing.JRadioButton altRowRadioButton;
    private javax.swing.JComboBox<String> analysisArrayComboBox;
    private javax.swing.JPanel analysisArrayPanel;
    private javax.swing.JButton analysisGenerateSummaryTablesHiddenButton;
    private javax.swing.JButton analysisGenerateSummaryTablesVisibleButton;
    private javax.swing.JButton analysisLoadButton;
    private javax.swing.JCheckBox analysisOpenDataTablesJCheckBox;
    private javax.swing.JCheckBox analysisOverrideKeyFileCheckBox;
    private javax.swing.JButton analysisShowSummaryTables;
    private javax.swing.JComboBox analysisSickFliterJComboBox;
    private javax.swing.JComboBox analysisSummaryClipboardCopyTypeComboBox;
    private javax.swing.JButton analysisSummaryCopyAllClipboardButton;
    private javax.swing.JPanel analysisSummaryDefaultsPanel;
    private javax.swing.JCheckBox analysisSummaryMedianCheckBox;
    private javax.swing.JCheckBox analysisSummaryPairedSpotsCheckBox;
    private javax.swing.JPanel analysisSummaryPanel;
    private javax.swing.JButton analysisTableRemoveButton;
    private javax.swing.JButton analysisTableShowButton;
    private javax.swing.JPanel analysisTablesPanel;
    private javax.swing.JPanel analysistabPanel;
    private javax.swing.JCheckBox autoAnalyzeJCheckBox;
    private javax.swing.JButton autoGridButton;
    public javax.swing.JCheckBox autoGridJCheckBox;
    private javax.swing.JCheckBox autoInvertJCheckBox;
    private javax.swing.JCheckBox autoNameJCheckBox;
    private javax.swing.JPanel autoPanel;
    private javax.swing.JCheckBox autoQuantJCheckBox;
    private javax.swing.JCheckBox autoSaveJCheckBox;
    private javax.swing.JCheckBox autoThreshJCheckBox;
    private javax.swing.JPanel autocheckPanel;
    private javax.swing.JButton autocheckbackButton;
    private javax.swing.JButton autocheckforwardButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JButton choosefolderButton;
    private javax.swing.JLabel contactJLabel;
    private javax.swing.JLabel contactJLabel1;
    private javax.swing.JPanel contactJPanel;
    private javax.swing.JPanel ctrlPanel;
    private javax.swing.JButton ctrldirButton;
    private javax.swing.JTextField ctrldirTextField;
    private javax.swing.JComboBox<String> ctrlplateComboBox;
    private javax.swing.JLabel currVersionJLabel;
    private javax.swing.JComboBox<dataTable> dataTablesComboBox;
    private javax.swing.JPanel defaultTableSettingsJPanel;
    private javax.swing.JButton definegridButton;
    private javax.swing.JButton downloadSGDInfoButton;
    private javax.swing.JButton expButton;
    private javax.swing.JPanel expPanel;
    private javax.swing.JTextField expdirTextField;
    private javax.swing.JComboBox<String> expplateComboBox;
    private javax.swing.JTextField folderJTextField;
    private javax.swing.JButton fullautoButton;
    private javax.swing.JButton gridCentreButton;
    public javax.swing.JComboBox<String> gridChoicejComboBox;
    private javax.swing.JPanel gridPanel;
    private javax.swing.JButton gridclearButton;
    private javax.swing.JList<File> imageFileJList;
    private javax.swing.JScrollPane imageFileJListScrollPane;
    private javax.swing.JLabel imageFolderJLabel;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JButton imageShowParamsButton;
    private javax.swing.JButton imageShowPresetsButton;
    private javax.swing.JButton imageStopButton;
    private javax.swing.JTextField imageThreshManualJTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    public javax.swing.JRadioButton javaLAFJRadioButton;
    private javax.swing.JButton keyFileLoadButton;
    private javax.swing.JLabel latestVersionJLabel;
    private javax.swing.JTextField lowCutOffJTextField;
    private javax.swing.JPanel manualspotPanel;
    private javax.swing.JButton manualspotResetButton;
    private javax.swing.JButton manualspotdefineButton;
    private javax.swing.JTextField maxSpotSizeJTextField;
    private javax.swing.JTextField minSpotSizeJTextField;
    public javax.swing.JRadioButton nimbusLAFJRadioButton;
    private javax.swing.ButtonGroup normButtonGroup;
    private javax.swing.JRadioButton normNoneButton;
    private javax.swing.JRadioButton normORFButton;
    private javax.swing.JTextField normORFJTextField;
    private javax.swing.JRadioButton normPlateMedianButton;
    private javax.swing.JPanel normalPanel;
    private javax.swing.JPanel optionsPanel;
    public javax.swing.JRadioButton osLAFJRadioButton;
    private javax.swing.JRadioButton plateCtrlRadioButton;
    private javax.swing.JTextField plateNameJTextField;
    private javax.swing.JTextField plateNumberJTextField;
    private javax.swing.JPanel platePanel;
    private javax.swing.JLabel platenameLabel;
    private javax.swing.JLabel platenumberLabel;
    public javax.swing.JCheckBox pointgridCheckBox;
    private javax.swing.JPanel presetPanel;
    private javax.swing.JButton quantButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton resetrotateButton;
    private javax.swing.JButton restoreTableButton;
    private javax.swing.JPanel rotatePanel;
    private javax.swing.JButton rotatecorrectorButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveScoreAvgButton;
    private javax.swing.JButton saveScoreButton;
    private javax.swing.JPanel savescorePanel;
    private javax.swing.JComboBox scanAComboBox;
    private javax.swing.JPanel scanAPanel;
    private javax.swing.JCheckBox scanAutoPlateNameJCheckBox;
    private javax.swing.JComboBox scanBComboBox;
    private javax.swing.JPanel scanBPanel;
    private javax.swing.JTextField scanBTextField;
    private javax.swing.JComboBox scanCComboBox;
    private javax.swing.JPanel scanCPanel;
    private javax.swing.JTextField scanCTextField;
    private javax.swing.JComboBox scanDComboBox;
    private javax.swing.JPanel scanDPanel;
    private javax.swing.JTextField scanDTextField;
    private javax.swing.JList<File> scanFileJList;
    private javax.swing.JScrollPane scanFileJScrollPane;
    private javax.swing.JButton scanFilenameHelpJButton;
    private javax.swing.JTextField scanFolderJTextField;
    private javax.swing.JComboBox scanLayout;
    private javax.swing.JLabel scanLayoutJLabel;
    private javax.swing.JComboBox scanOffsetJComboBox;
    private javax.swing.JLabel scanOffsetJLabel;
    private javax.swing.JPanel scanOptionsPanel;
    private javax.swing.JPanel scanPanel;
    private javax.swing.JPanel scanPlateNamingPanel;
    private javax.swing.JPanel scanPlateNumbersPanel;
    private javax.swing.JCheckBox scanPreviewJCheckBox;
    private javax.swing.JPanel scanPreviewPanel;
    private javax.swing.JButton scanSelectAllButton;
    private javax.swing.JLabel scanSetLabel;
    private javax.swing.JTextField scanaTextField;
    private javax.swing.JTextField scanbaseTextField;
    private javax.swing.JButton scanchoosefolderButton;
    private javax.swing.JCheckBox scancloseCheckBox;
    private javax.swing.JCheckBox scangrayCheckBox;
    private javax.swing.JButton scanprocessButton;
    private javax.swing.JButton scanprocessallButton;
    private javax.swing.JComboBox scanrotateComboBox;
    private javax.swing.JCheckBox scansaveACheckBox;
    private javax.swing.JCheckBox scansaveBCheckBox;
    private javax.swing.JCheckBox scansaveCCheckBox;
    private javax.swing.JCheckBox scansaveDCheckBox;
    private javax.swing.JComboBox scansetComboBox;
    private javax.swing.JComboBox scanshrinkComboBox;
    private javax.swing.JTextField scansubfolderTextField;
    private javax.swing.JLabel scoreArraykeyLabel;
    private javax.swing.JRadioButton scoreByArrayPosRadioButton;
    private javax.swing.JRadioButton scoreByOrfRadioButton;
    private javax.swing.JCheckBox scoreCompetitionJCheckBox;
    private javax.swing.JPanel scoreKeyfilePanel;
    private javax.swing.JComboBox<String> scoreKeysComboBox;
    private javax.swing.JLabel scoreNormPercentileLabel;
    private javax.swing.JTextField scoreNormPercentileTextField;
    private javax.swing.JComboBox scoreRCComboBox;
    private javax.swing.JCheckBox scoreRCJCheckBox;
    private javax.swing.ButtonGroup scoreSaveButtonGroup;
    private javax.swing.JCheckBox scoreSpatialJCheckBox;
    private javax.swing.JCheckBox scoreUndergrowthJCheckBox;
    private javax.swing.JTextField scorenameTextField;
    private javax.swing.JButton scoringRefreshButton;
    private javax.swing.JPanel scoringtabaPanel;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JTextField setNumberJTextField;
    private javax.swing.JLabel setnumberLabel;
    private javax.swing.JPanel sgdFeaturesJPanel;
    private javax.swing.JButton showQuantButton;
    private javax.swing.JTextField sickCutOffTextJField;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JSlider threshJSlider;
    private javax.swing.JPanel threshPanel;
    private javax.swing.JRadioButton threshRadioAuto;
    private javax.swing.JRadioButton threshRadioManual;
    private javax.swing.JButton thresholdButton;
    private javax.swing.JButton toggleInputOutputButton;
    private javax.swing.JButton updateCheckButton;
    private javax.swing.JCheckBox updateCheckJCheckBox;
    private javax.swing.JCheckBox updateCheckJCheckBox1;
    private javax.swing.JPanel updaterJPanel;
    private javax.swing.JTextField upperCutOffJTextField;
    private javax.swing.JCheckBox useQueryKeyJCheckBox;
    private javax.swing.JButton viewLogButton;
    private javax.swing.JCheckBox wizardModeJCheckBox;
    private javax.swing.JPanel zoomPanel;
    private javax.swing.JButton zoominButton;
    private javax.swing.JButton zoomoutButton;
    // End of variables declaration//GEN-END:variables
}
