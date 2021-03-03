package cityfarmers.farmland;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import daos.CropDAO;
import daos.PlotDAO;
import daos.UserDAO;
import entities.Crop;
import entities.Plot;
import entities.User;

/**
 * The FarmlandClearer class facilitates the interaction between the database and
 * the FarmlandCtrl for Clearing Methods
 * 
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class FarmlandClearer {

    /** UDM provides access to the User table in the database */
    private UserDAO UDM;

    /** PDM provides access to the Plot table in the database */
    private PlotDAO PDM;

    /** CDM provides access to the Crop table in the database */
    private CropDAO CDM;

    /** Cost of removing withered crop from a plot*/
    final int clearingCost = 50;

    /** Creates a FarmlandClear object with a default UDM, PDM and CDM*/
    public FarmlandClearer() {
        UDM = new UserDAO();
        PDM = new PlotDAO();
        CDM = new CropDAO();
    }

    /**
     * The checkClear method checks if the selected plot is wilted.
     * 
     * @param plot  chosen plot to check for clearing
     * @return      true if plot can be cleared, false otherwise
     */
    public boolean checkClear(Plot plot) {
        String cropName = plot.getCropName();
        Crop crop = CDM.getCrop(cropName);

        if (crop != null) {
            int timeToHarvest = crop.getTimeToHarvest();
            double timePlotted = (double) plot.getTimeCropPlanted().getTime();
            Date date = new Date();
            double currentTime = (double) date.getTime();
            double timeSpent = (currentTime - timePlotted) / 60000;
            if (timeSpent  > (2 * timeToHarvest)) { 
                return true;
            }
        }

        return false;
    }

    /**
     * The checkAnyClear method checks if any of a user's plots is wilted.
     * 
     * @param username  the username
     * @return          true if any of the user's plots can be cleared, false otherwise
     */
    public List<Plot> checkAnyClear(String username) {
        // grab all the plots
        List<Plot> plots = PDM.getPlotsOwnedByUser(username); 
        List<Plot> plotsToHarvest = new ArrayList<Plot>();
        // for loop the plots to check if plots got wither
        for (int i = 0; i < plots.size(); i++) { 
            Plot plot = plots.get(i);

            if (checkClear(plot)){
                plotsToHarvest.add(plot);
            }
        }
        return plotsToHarvest;
    }

    /**
     * The clearPlots method clears all of a user's wilted plots.
     * 
     * @param plotsToClear  plots to clear
     * @param loggedInUser  the user
     */
    public void clearPlots(User loggedInUser, List<Plot> plotsToClear) {
        // use PlotDAO to get all Plots and check if plots can clear
        for (Plot plot : plotsToClear) {
            if (checkClear(plot)) {
            PDM.clearPlot(plot); // use UserDAO to -50 Gold to clear
            int currentGold = loggedInUser.getGold();
            loggedInUser.setGold(currentGold-clearingCost);
            }
        }
        UDM.update(loggedInUser);
        System.out.println("You have cleared your plots.");
    }

    /**
     * Clears the user's plots if valid. 
     * 
     * @param loggedInUser  the user
     */
    public void clear(User loggedInUser) {
        List<Plot> plotsToClear = checkAnyClear(loggedInUser.getUsername());
        if (!plotsToClear.isEmpty()) {
            clearPlots(loggedInUser, plotsToClear);
        } else {
            System.out.println("You do not have any plots to clear.");
        }
    }
}