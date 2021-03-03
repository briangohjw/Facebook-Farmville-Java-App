package cityfarmers.visit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import daos.CropDAO;
import daos.FriendsDAO;
import daos.PlotDAO;
import daos.RankDAO;
import daos.StealDAO;
import daos.UserDAO;
import entities.Crop;
import entities.Plot;
import entities.User;

/**
 * The StealManager class facilitates the interaction between the database and the VisitCtrl
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class StealManager {
    /** UDM provides access to the User table in the database */
    private UserDAO UDM;

    /** PDM provides access to the Plot table in the database */
    private PlotDAO PDM;

    /** CDM provides access to the Crop table in the database */
    private CropDAO CDM;

    /** RDM provides access to the Rank table in the database */
    private RankDAO RDM;

    /** SDM provides access to the Steal table in the database */
    private StealDAO SDM;

    /** FDM provides access to the Friends table in the database */
    private FriendsDAO FDM;


    /** Creates a VisitCtrl object with a default UDM, PDM, CDM, FDM, SDM and RDM */
    public StealManager() {
        UDM = new UserDAO();
        PDM = new PlotDAO();
        CDM = new CropDAO();
        FDM = new FriendsDAO();
        SDM = new StealDAO();
        RDM = new RankDAO();
    }

    /**
     * Returns all of the user's friend's user object.
     * 
     * @param username  the friend's username
     * @return          all of the user's friends' user object
     */
    public List<User> getFriends(User username){
        return FDM.getFriends(username);
    }

    /**
     * The checkAnySteal method takes a plot and checks if any of the friend's plots are ready for stealing
     * 
     * @param loggedInUser  the user
     * @param chosenFriend  the friend
     * @return      list of plots ready for stealing, null otherwise
     */
    public List<Plot> checkAnySteal(User loggedInUser, User chosenFriend) {
        String friendUsername = chosenFriend.getUsername();
        List<Plot> allFriendPlots = PDM.getPlotsOwnedByUser(friendUsername);
        ArrayList<Plot> toSteal = new ArrayList<Plot>();
        Plot plot;

        // for loop the plots to check if plots got harvest
        for (int i = 0; i < allFriendPlots.size(); i++) {
            plot = allFriendPlots.get(i);
            if (checkReadyToSteal(loggedInUser, plot)){
                toSteal.add(plot);
            }
        }
        return toSteal;
    }

    /**
     * The checkReadyToSteal method takes a plot and checks if it is ready to harvest.
     * 
     * @param loggedInUser  the user
     * @param plot          the selected plot
     * @return              true if plot ready to steal, false otherwise
     */
    public boolean checkReadyToSteal(User loggedInUser, Plot plot) {
        Crop crop = CDM.getCrop(plot.getCropName());
        
        if (crop != null) {
            // Obtain time plotted and time to harvest.
            double timeToHarvest = crop.getTimeToHarvest();
            double timePlotted = (double) plot.getTimeCropPlanted().getTime();
            // Obtain time spent since time plotted.
            Date date = new Date();
            double currentTime = (double) date.getTime();
            double timeSpent = (currentTime - timePlotted)/60000;
            // If the plot is not wilted and ready for harvest
            if (timeSpent > timeToHarvest && timeSpent < 2 * timeToHarvest) {

                // if he already stole from 1 of the friend's plots
                if (!SDM.hasStolen(loggedInUser, plot)) {
                    // Check plot if plot remaining percentage is > 80%
                    int amountLeftToSteal = plot.getRemainingPercentage() - 80;
                    // if amount to steal is more than the amount left to steal, take the lower amount
                    if (amountLeftToSteal > 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * The steal method takes a list of plot and steals, updates the user and plots stolen.
     * 
     * @param loggedInUser      the user
     * @param plotsToBeStolen   plots to be stolen
     */
    public void steal(User loggedInUser, List<Plot> plotsToBeStolen) {
        int totalGoldStolen = 0;
        int totalXpStolen = 0;
        int stolenYield = 0;
        Crop crop;
        String cropName;
        Map<String, Integer> stolenCrops = new HashMap<>();

        for (Plot plot : plotsToBeStolen) {

            int amountLeftToSteal = plot.getRemainingPercentage() - 80;
            // generate random number (1-5%)
            Random rand = new Random();
            int amountToSteal = 1;
            amountToSteal += rand.nextInt(5);
            amountToSteal = Math.min(amountLeftToSteal, amountToSteal);

            cropName = plot.getCropName();
            crop = CDM.getCrop(cropName);
            
            // Steal by grabbing Remaining Percentage & Original Yield
            stolenYield = (int) (amountToSteal/100) * plot.getOriginalYield();
            stolenCrops.put(cropName, stolenYield);
            // To get EXP To Add
            totalXpStolen += stolenYield * crop.getXp();
            // To get Gold to Add
            totalGoldStolen += stolenYield * crop.getSalePrice();
            // PlotDAO to minus away stolen crops and update (reduce percentage left)
            plot.setRemainingPercentage(amountLeftToSteal - amountToSteal + 80);
            // to generate gold and EXP gained using CropDAO
            PDM.updatePlot(plot);
            // Add thief to thief list
            SDM.addThief(plot,loggedInUser);

        }

        // use UserDAO to update User Object with added EXP and Gold
        int currentGold = loggedInUser.getGold();
        int currentXp = loggedInUser.getXp();
        int updatedXp = currentXp + totalXpStolen;

        // Update Gold and XP
        loggedInUser.setGold(currentGold + totalGoldStolen);
        loggedInUser.setXp(updatedXp);

        // use Updated EXP to check in RankDAO if there is an upgrade in Rank
        String updatedRank = RDM.updateRankAndPlots(loggedInUser, updatedXp);
        loggedInUser.setRank(updatedRank);
        UDM.update(loggedInUser);
        
        String stolenCropsQuantity = "";
        System.out.print("You have successfully stolen");
        for (Map.Entry<String,Integer> entry : stolenCrops.entrySet()) {
            stolenCropsQuantity += entry.getValue() + " " + entry.getKey() + ", ";
        }
        stolenCropsQuantity = stolenCropsQuantity.substring(0, stolenCropsQuantity.length() - 2);
        System.out.println(" " + stolenCropsQuantity + " for " + totalXpStolen + " XP, and " + totalGoldStolen + " gold.");
    }
    
}
