/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package balony;

/**
 *
 * @author Barry Young
 */
public class arrayDefinition {

        private String[][][] orf;
        private String name;
        private int plates;
        private int rows;
        private int cols;

        public void arrayDefinition() {
        }

        public void initData() {
            setOrf(new String[getPlates() + 1][getRows() + 1][getCols() + 1]);
        }

        /**
         * @return the orf
         */
        public String[][][] getOrf() {
            return orf;
        }

        /**
         * @param orf the orf to set
         */
        public void setOrf(String[][][] orf) {
            this.orf = orf;
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
         * @return the plates
         */
        public int getPlates() {
            return plates;
        }

        /**
         * @param plates the plates to set
         */
        public void setPlates(int plates) {
            this.plates = plates;
        }

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
    }
