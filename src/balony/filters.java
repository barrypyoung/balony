/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package balony;

import java.io.File;

/**
 *
 * @author Barry Young
 */
public class filters {
    public class txtFileFilter implements java.io.FileFilter {

        @Override
        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".txt");
        }
    }

    public class jpgFileFilter implements java.io.FileFilter {

        @Override
        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".jpg");
        }
    }
}
