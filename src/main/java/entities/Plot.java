package entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

/**
 * The Plot class keeps track of the plotId, ownerUsername timeCropPlanted,
 * remainingPercentage, cropName and originalYield of a plot.
 *
 * @version 1.1 04 Apr 2020
 * @author Brian Goh
 */   
public class Plot {

    /** Id of plot */
    private int plotId;

    /** Username of plot owner */
    private String ownerUsername;

    /** Time that crop was planted */
    private java.util.Date timeCropPlanted;

    /** Remaining percentage of crop */
    private int remainingPercentage;

    /** Name of crop */
    private String cropName;

    /** Originally generated yield of crop */
    private int originalYield;

    /**
     * Creates a Plot with the specified plotId, ownerUsername, timeCropPlanted
     * remainingPercentage of crop, cropName and originally generated yield of crop.
     * Used when retrieving a plot from database
     * 
     * @param plotId                id of plot
     * @param ownerUsername         username of plot owner
     * @param timeCropPlanted       time that crop was planted
     * @param remainingPercentage   remaining percentage of crop
     * @param cropName              name of crop
     * @param originalYield         originally generated yield of crop
     */
    public Plot(int plotId, String ownerUsername, Date timeCropPlanted, int remainingPercentage, String cropName, int originalYield) {
        this.plotId = plotId;
        this.ownerUsername = ownerUsername;
        this.timeCropPlanted = timeCropPlanted;
        this.remainingPercentage = remainingPercentage;
        this.cropName = cropName;
        this.originalYield = originalYield;
    }

    /**
     * Creates a emptyPlot with the specified plotId and ownerUsername.
     * Used when resetting a plot to empty after harvesting or wilting
     * 
     * @param plotId        id of plot
     * @param ownerUsername username of plot owner
     */
    public Plot(int plotId, String ownerUsername) {
        this.plotId = plotId;
        this.ownerUsername = ownerUsername;
        this.timeCropPlanted = null;
        this.remainingPercentage = -1;
        this.cropName = null;
        this.originalYield = -1;
    }

    /**
     * Plants a crop on this plot
     * 
     * @param crop crop to plant on this plot
     */
    public void plantCrop(Crop crop) {
        java.util.Date date= new java.util.Date();
        long time = date.getTime();
        Timestamp timeCropPlanted = new Timestamp(time);

        this.timeCropPlanted = timeCropPlanted;
        this.remainingPercentage = 100;
        this.cropName = crop.getCropName();
        this.originalYield = crop.getMinYield() + new Random().nextInt(crop.getMaxYield() - crop.getMinYield() + 1);
    }
    
    /**
     * Returns plot id of this plot
     * 
     * @return plot id of this plot
     */
    public int getPlotId() {
        return this.plotId;
    }

    /**
     * Returns username of this plot's owner
     * 
     * @return username of this plot's owner
     */
    public String getOwnerUsername() {
        return this.ownerUsername;
    }

    /**
     * Returns time that this crop was planted
     * 
     * @return time that this crop was planted
     */
    public java.util.Date getTimeCropPlanted() {
        return this.timeCropPlanted;
    }

    /**
     * Returns remaining percentage of this crop
     * 
     * @return remaining percentage of this crop
     */
    public int getRemainingPercentage() {
        return this.remainingPercentage;
    }

    /**
     * Sets remaining percentage of this crop
     * 
     * @param remainingPercentage remaining percentage of this crop
     */
    public void setRemainingPercentage(int remainingPercentage) {
        this.remainingPercentage = remainingPercentage;
    }

    /**
     * Returns name of this crop
     * 
     * @return name of this crop
     */
    public String getCropName() {
        return this.cropName;
    }

    /**
     * Returns originally generated yield of this crop
     * 
     * @return originally generated yield of this crop
     */
    public int getOriginalYield() {
        return this.originalYield;
    }
}