/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package balony;

/**
 *
 * @author Barry Young
 */
 public class plateArray {

        private String name;
        private int dpi;
        private float xstp;
        private float ystp;
        private int rows;
        private int cols;

        public void plateArray(int d) {
            setDpi(d);
        }

        public void setdpi(int d) {
            setDpi(d);
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
         * @return the dpi
         */
        public int getDpi() {
            return dpi;
        }

        /**
         * @param dpi the dpi to set
         */
        public void setDpi(int dpi) {
            this.dpi = dpi;
        }

        /**
         * @return the xstp
         */
        public float getXstp() {
            return xstp;
        }

        /**
         * @param xstp the xstp to set
         */
        public void setXstp(float xstp) {
            this.xstp = xstp;
        }

        /**
         * @return the ystp
         */
        public float getYstp() {
            return ystp;
        }

        /**
         * @param ystp the ystp to set
         */
        public void setYstp(float ystp) {
            this.ystp = ystp;
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
