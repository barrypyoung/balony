/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package balony;

import java.util.ArrayList;

/**
 *
 * @author Barry Young
 */
    public class orfScores {

        private String gene;
        private ArrayList<Double> vals;

        public void orfScores() {
            setGene("");
            setVals(new ArrayList<Double>());
        }

        /**
         * @return the gene
         */
        public String getGene() {
            return gene;
        }

        /**
         * @param gene the gene to set
         */
        public void setGene(String gene) {
            this.gene = gene;
        }

        /**
         * @return the vals
         */
        public ArrayList<Double> getVals() {
            return vals;
        }

        /**
         * @param vals the vals to set
         */
        public void setVals(ArrayList<Double> vals) {
            this.vals = vals;
        }
    }
