/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package balony;

/**
 *
 * @author Barry Young
 */
public class analysisData {

    private int sets;
    private int plates;
    private int rows;
    private int cols;
    private Double minSpotSize;
    private Double maxSpotSize;
    private Double sickSpotSize;
    private Double lowCutOff;
    private Double HighCutOff;
    public Double[][][][] ctrlSpots;
    public Double[][][][] expSpots;
    private int sickFilterType;
    private dataTable dt;

    void analysisData() {
    }

    public void initData() {
        setCtrlSpots(new Double[getSets() + 1][getPlates() + 1][getRows() + 1][getCols() + 1]);
        setExpSpots(new Double[getSets() + 1][getPlates() + 1][getRows() + 1][getCols() + 1]);
    }

    public int getSickFilterType() {
        return sickFilterType;
    }

    public void setSickFilterType(int sickFilterType) {
        this.sickFilterType = sickFilterType;
    }

    public Double getMaxSpotSize() {
        return maxSpotSize;
    }

    public void setMaxSpotSize(Double maxSpotSIze) {
        this.maxSpotSize = maxSpotSIze;
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
     * @return the minSpotSize
     */
    public Double getMinSpotSize() {
        return minSpotSize;
    }

    /**
     * @param minSpotSize the minSpotSize to set
     */
    public void setMinSpotSize(Double minSpotSize) {
        this.minSpotSize = minSpotSize;
    }

    /**
     * @return the sickSpotSize
     */
    public Double getSickSpotSize() {
        return sickSpotSize;
    }

    /**
     * @param sickSpotSize the sickSpotSize to set
     */
    public void setSickSpotSize(Double sickSpotSize) {
        this.sickSpotSize = sickSpotSize;
    }

    /**
     * @return the lowCutOff
     */
    public Double getLowCutOff() {
        return lowCutOff;
    }

    /**
     * @param lowCutOff the lowCutOff to set
     */
    public void setLowCutOff(Double lowCutOff) {
        this.lowCutOff = lowCutOff;
    }

    /**
     * @return the HighCutOff
     */
    public Double getHighCutOff() {
        return HighCutOff;
    }

    /**
     * @param HighCutOff the HighCutOff to set
     */
    public void setHighCutOff(Double HighCutOff) {
        this.HighCutOff = HighCutOff;
    }

    /**
     * @return the ctrlSpots
     */
    public Double[][][][] getCtrlSpots() {
        return ctrlSpots;
    }

    /**
     * @param ctrlSpots the ctrlSpots to set
     */
    public void setCtrlSpots(Double[][][][] ctrlSpots) {
        this.ctrlSpots = ctrlSpots;
    }

    /**
     * @return the expSpots
     */
    public Double[][][][] getExpSpots() {
        return expSpots;
    }

    /**
     * @param expSpots the expSpots to set
     */
    public void setExpSpots(Double[][][][] expSpots) {
        this.expSpots = expSpots;
    }

    /**
     * @return the dt
     */
    public dataTable getDt() {
        return dt;
    }

    /**
     * @param dt the dt to set
     */
    public void setDt(dataTable dt) {
        this.dt = dt;
    }
}
