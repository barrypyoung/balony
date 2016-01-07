/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package balony;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Barry Young
 */
public class main {

    static Balony b;
    
    public static void main(String args[]) {
        String[] os = new String[1];
        Properties prefs = new Properties();
        try {
            File f = new File(Balony.get_prefsfile_name());
            prefs.loadFromXML(new FileInputStream(f));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        if (prefs.containsKey(Balony.PREFS_OPTIONS_OS_LOOK_AND_FEEL)) {
            try {

                String p = prefs.getProperty(Balony.PREFS_OPTIONS_OS_LOOK_AND_FEEL);

                if (p.equals("2")) {
                    os[0] = "2";
                    try {
                        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                            if ("Nimbus".equals(info.getName())) {
                                UIManager.setLookAndFeel(info.getClassName());
                                break;
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        System.out.println("Nimbus not available");
                    } catch (InstantiationException e) {
                        System.out.println("Nimbus not available");
                    } catch (IllegalAccessException e) {
                        System.out.println("Nimbus not available");
                    } catch (UnsupportedLookAndFeelException e) {
                        System.out.println("Nimbus not available");
                    }
                } else if (p.equals("1")) {
                    os[0] = "1";
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    System.out.println("Using Operating System Look-and-Feel");

                } else {
                    os[0] = "3";
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    System.out.println("Using default look-and-feel");
                }
            } catch (ClassNotFoundException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (InstantiationException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (IllegalAccessException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (UnsupportedLookAndFeelException e) {
                System.out.println(e.getLocalizedMessage());
            }
        } else {
            try {
                String osn = System.getProperty("os.name").toLowerCase();
                if (osn.contains("win")) {
                    os[0] = "1";
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } else {
                    try {
                        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                            if ("Nimbus".equals(info.getName())) {
                                os[0] = "2";
                                UIManager.setLookAndFeel(info.getClassName());
                                break;
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                        System.out.println("Nimbus not available");
                    } catch (InstantiationException e) {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                        System.out.println("Nimbus not available");
                    } catch (IllegalAccessException e) {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                        System.out.println("Nimbus not available");
                    } catch (UnsupportedLookAndFeelException e) {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                        System.out.println("Nimbus not available");
                    }

                }
            } catch (ClassNotFoundException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (InstantiationException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (IllegalAccessException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (UnsupportedLookAndFeelException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }

        b = new Balony();
        b.main(os);

    }
   
}
