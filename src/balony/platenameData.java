/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package balony;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Barry Young
 */
 public class platenameData {

        private int rows;
        private int cols;
        private String name;
        private int maxSets;
        private int maxPlates;
        private ArrayList<File> files;

        /**
         * @return the rows
         */
        public int getRows() {
            return rows;
        }

        /**
         * @param rows the rows to set
         */
        public void setRows(int rows) {
            this.rows = rows;
        }

        /**
         * @return the cols
         */
        public int getCols() {
            return cols;
        }

        /**
         * @param cols the cols to set
         */
        public void setCols(int cols) {
            this.cols = cols;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the maxSets
         */
        public int getMaxSets() {
            return maxSets;
        }

        /**
         * @param maxSets the maxSets to set
         */
        public void setMaxSets(int maxSets) {
            this.maxSets = maxSets;
        }

        /**
         * @return the maxPlates
         */
        public int getMaxPlates() {
            return maxPlates;
        }

        /**
         * @param maxPlates the maxPlates to set
         */
        public void setMaxPlates(int maxPlates) {
            this.maxPlates = maxPlates;
        }

        /**
         * @return the files
         */
        public ArrayList<File> getFiles() {
            return files;
        }

        /**
         * @param files the files to set
         */
        public void setFiles(ArrayList<File> files) {
            this.files = files;
        }
    }