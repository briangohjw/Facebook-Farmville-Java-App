package cityfarmers.farmland;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import daos.CropDAO;
import daos.PlotDAO;
import daos.RankDAO;
import daos.UserDAO;
import entities.Crop;
import entities.Plot;
import entities.User;

/**
 * The FarmlandHarvester class facilitates the interaction between the database and
 * the FarmlandCtrl for Harvest Methods
 * 
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class FarmlandHarvester {

    /** CDM provides access to the Crop table in the database */
    private CropDAO CDM;

    /** PDM provides access to the Plot table in the database */
    private PlotDAO PDM;

    /** RDM provides access to the Rank table in the database */
    private RankDAO RDM;
    
    /** UDM provides access to the User table in the database */
    private UserDAO UDM;

    /** farmlandViewer provides access to FarmlandViewer methods */
    private FarmlandViewer farmlandViewer;

    /** Creates a FarmlandHarvest object with a default farmlandViewer, CDM, UCDM, RDM and PDM*/
    public FarmlandHarvester() {
        CDM = new CropDAO();
        PDM = new PlotDAO();
        RDM = new RankDAO();
        UDM = new UserDAO();
        farmlandViewer = new FarmlandViewer();
    }

    /**
     * The checkAnyHarvest method takes a list of plots and checks if there are any plots to harvest.
     * 
     * @param plots     a map of the corresponding amount of plots a user has
     * @return          true if there are plots to harvest and false otherwise
     */
    public List<Plot> checkAnyHarvest(List<Plot> plots) {
        List<Plot> plotsToHarvest = new ArrayList<Plot>();
        // Check ready to harvest for each plot
        for (int i = 0; i < plots.size(); i++) {
            Plot plot = plots.get(i);
            if (checkReadyToHarvest(plot)) {
                plotsToHarvest.add(plot);
            }
        }

        return plotsToHarvest;
    }

    /**
     * The checkReadyToHarvest method takes a plot and checks if it is ready to harvest.
     * 
     * @param plot  selected plot to check if ready to harvest
     * @return      true if plot is ready to harvest and false otherwise
     */
    public boolean checkReadyToHarvest(Plot plot) {
        Crop crop = CDM.getCrop(plot.getCropName());
        boolean readyToHarvest = false;

        if (crop != null) {
            // Time plotted and time to harvest.
            double timeToHarvest = crop.getTimeToHarvest();
            double timePlotted = (double) plot.getTimeCropPlanted().getTime();
            // Obtain time spent since time plotted.
            Date date = new Date();
            double currentTime = (double) date.getTime();
            double timeSpent = (currentTime - timePlotted)/60000;
            // If the plot is not wilted and ready for harvest
            if (timeSpent > timeToHarvest && timeSpent < 2 * timeToHarvest) {
                readyToHarvest = true;
            }
        }
        return readyToHarvest;
    }

    /**
     * The harvest method will harvest and add gold and Xp to the user. 
     * 
     * @param plots         a list of the user's plots
     * @param loggedInUser  the user
     */
    public void harvestPlots(List<Plot> plots, User loggedInUser) {
        Crop crop;
        String cropName;
        int totalYield = 0;
        int totalXpEarned = 0;
        int totalGoldEarned = 0;
        boolean harvested = false;
        Map<String, Integer> harvestedCrops = new HashMap<>();

        for (Plot plot : plots) {
            cropName = plot.getCropName();
            crop = CDM.getCrop(cropName);
            // Harvest by grabbing Remaining Percentage & Original Yield
            totalYield = (int) (plot.getRemainingPercentage()/100) * plot.getOriginalYield();
            
            if (harvestedCrops.containsKey(cropName)) {
                harvestedCrops.put(cropName, harvestedCrops.get(cropName) + totalYield);
            } else {
            harvestedCrops.put(cropName, totalYield);
            }

            // To get EXP To Add
            totalXpEarned += totalYield * crop.getXp();
            // To get Gold to Add
            totalGoldEarned += totalYield * crop.getSalePrice();
            PDM.clearPlot(plot);
            harvested = true;
        }

        // Output statement
        System.out.print("You have harvested");
        String harvestedCropsQuantity = "";
        for (Map.Entry<String,Integer> entry : harvestedCrops.entrySet()) {
            harvestedCropsQuantity += entry.getValue() + " " + entry.getKey() + ", ";
        }
        harvestedCropsQuantity = harvestedCropsQuantity.substring(0, harvestedCropsQuantity.length() - 2);
        System.out.println(" " + harvestedCropsQuantity + " for " + totalXpEarned + " XP, and " + totalGoldEarned + " gold.");
        
        // Update user if harvested 
        if (harvested){
            // use UserDAO to update User Object with added EXP and Gold
            int currentGold = loggedInUser.getGold();
            int currentXp = loggedInUser.getXp();
            int updatedXp = currentXp + totalXpEarned;

            // Update Gold and XP
            loggedInUser.setGold(currentGold + totalGoldEarned);
            loggedInUser.setXp(updatedXp);

            // use Updated EXP to check in RankDAO if there is an upgrade in Rank
            String updatedRank = RDM.updateRankAndPlots(loggedInUser, updatedXp);
            loggedInUser.setRank(updatedRank);
            
            UDM.update(loggedInUser);
            }
    }

    /**
     * Harvests the user's plots if valid. 
     * @param loggedInUser  the user
     */
    public void harvest(User loggedInUser) {
        List<Plot> plots = farmlandViewer.getPlotsOwnedByUser(loggedInUser.getUsername());
        List<Plot> plotsToHarvest = checkAnyHarvest(plots);
        if (!plotsToHarvest.isEmpty()) {
            harvestPlots(plotsToHarvest, loggedInUser);
        } else {
            System.out.println("You do not have any plots to harvest.");
        }
    }
}