/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package balony;

/**
 *
 * @author Barry Young
 */
public class spotData {

        private Integer[][][][] area;
        private Double[][][][] normArea;
        private String[][] imgfile;
        private int[][] xmin;
        private int[][] ymin;
        private float[][] xstep;
        private float[][] ystep;
        private int sets;
        private int plates;
        private int rows;
        private int cols;
        private String arrayID;
        private String name;

        /**
         * @return the area
         */
        public Integer[][][][] getArea() {
            return area;
        }

        /**
         * @param area the area to set
         */
        public void setArea(Integer[][][][] area) {
            this.area = area;
        }

        /**
         * @return the normArea
         */
        public Double[][][][] getNormArea() {
            return normArea;
        }

        /**
         * @param normArea the normArea to set
         */
        public void setNormArea(Double[][][][] normArea) {
            this.normArea = normArea;
        }

        /**
         * @return the imgfile
         */
        public String[][] getImgfile() {
            return imgfile;
        }

        /**
         * @param imgfile the imgfile to set
         */
        public void setImgfile(String[][] imgfile) {
            this.imgfile = imgfile;
        }

        /**
         * @return the xmin
         */
        public int[][] getXmin() {
            return xmin;
        }

        /**
         * @param xmin the xmin to set
         */
        public void setXmin(int[][] xmin) {
            this.xmin = xmin;
        }

        /**
         * @return the ymin
         */
        public int[][] getYmin() {
            return ymin;
        }

        /**
         * @param ymin the ymin to set
         */
        public void setYmin(int[][] ymin) {
            this.ymin = ymin;
        }

        /**
         * @return the xstep
         */
        public float[][] getXstep() {
            return xstep;
        }

        /**
         * @param xstep the xstep to set
         */
        public void setXstep(float[][] xstep) {
            this.xstep = xstep;
        }

        /**
         * @return the ystep
         */
        public float[][] getYstep() {
            return ystep;
        }

        /**
         * @param ystep the ystep to set
         */
        public void setYstep(float[][] ystep) {
            this.ystep = ystep;
        }

        /**
         * @return the sets
         */
        public int getSets() {
            return sets;
        }

        /**
         * @param sets the sets to set
         */
        public void setSets(int sets) {
            this.sets = sets;
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

        /**
         * @return the arrayID
         */
        public String getArrayID() {
            return arrayID;
        }

        /**
         * @param arrayID the arrayID to set
         */
        public void setArrayID(String arrayID) {
            this.arrayID = arrayID;
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
    }
