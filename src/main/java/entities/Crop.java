package entities;

/**
 * The Crop class keeps track of the cropName, minYield, maxYield, salePrice,
 * cost, timeToHarvest and xp.
 *
 * @version 1.0 04 Apr 2020
 * @author Brian Goh
 */     
public class Crop {

    /** Name of crop */
    private String cropName;

    /** Minimum yield of crop */
    private int minYield;

    /** Maximum yield of crop */
    private int maxYield;

    /** Sale price of crop */
    private int salePrice;

    /** Cost of crop */
    private int cost;

    /** Harvest time of crop */
    private int timeToHarvest;

    /** Xp gained from crop */
    private int xp;
    
    /**
     * Creates a Crop with the specified cropName, minYield, maxYield, salePrice,
     * cost, timeToHarvest and xp
     * 
     * @param cropName      name of crop
     * @param minYield      minimum yield of crop
     * @param maxYield      maximum yield of crop
     * @param salePrice     sale price of crop
     * @param cost          cost of crop
     * @param timeToHarvest harvest time of crop
     * @param xp            xp gained from crop
     */
    public Crop(String cropName, int minYield, int maxYield, int salePrice, int cost, int timeToHarvest, int xp) {
        this.cropName = cropName;
        this.minYield = minYield;
        this.maxYield = maxYield;
        this.salePrice = salePrice;
        this.cost = cost;
        this.timeToHarvest = timeToHarvest;
        this.xp = xp;
    }

    /**
     * Retrieves name of crop
     * 
     * @return name of crop
     */
    public String getCropName() {
        return this.cropName;
    }

    /**
     * Retrieves minimum yield of crop
     * 
     * @return minimum yield of crop
     */
    public int getMinYield() {
        return this.minYield;
    }

    /**
     * Retrieves maximum yield of crop
     * 
     * @return maximum yield of crop
     */
    public int getMaxYield() {
        return this.maxYield;
    }

    /**
     * Retrieves sale price of crop
     * 
     * @return sale price of crop
     */
    public int getSalePrice() {
        return this.salePrice;
    }

    /**
     * Retrieves cost of crop
     * 
     * @return cost of crop
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * Retrieves harvest time of crop
     * 
     * @return harvest time of crop
     */
    public int getTimeToHarvest() {
        return this.timeToHarvest;
    }

    /**
     * Retrieves xp gained from crop
     * 
     * @return xp gained from crop
     */
    public int getXp() {
        return this.xp;
    }
}