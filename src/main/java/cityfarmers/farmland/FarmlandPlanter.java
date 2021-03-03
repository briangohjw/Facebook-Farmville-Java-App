package cityfarmers.farmland;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import cityfarmers.inventory.InventoryManager;
import daos.CropDAO;
import daos.PlotDAO;
import entities.Crop;
import entities.Plot;
import entities.User;
import util.Utility;

/**
 * The FarmlandPlanter class facilitates the interaction between the database and
 * the FarmlandCtrl for Planting Methods
 * 
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class FarmlandPlanter {

    /** UCDM provides access to the UserCrop table in the database */
    private InventoryManager inventoryManager;

    /** CDM provides access to the Crop table in the database */
    private CropDAO CDM;

    /** farmlandDisplay provides access to the FarmlandDisplay functions */
    private FarmlandViewer farmlandViewer;

    /** PDM provides access to the Plot table in the database */
    private PlotDAO PDM;

    /** Creates a WelcomeCtrl object with a default farmland Viewer, Inventory Manager, CDM and PDM*/
    public FarmlandPlanter() {
        farmlandViewer = new FarmlandViewer();
        inventoryManager = new InventoryManager();
        CDM = new CropDAO();
        PDM = new PlotDAO();
    }


    /**
    * The isEmpty method checks if the plot is empty.
     * 
     * @param plot  selected plot to check if empty
     * @return      true if plot is empty, false otherwise     
     */
    public boolean isEmpty(Plot plot) {
        String cropName = plot.getCropName();
        Crop crop = CDM.getCrop(cropName);

        if (crop != null) {
            return false;
        }

        return true;
    }
    
    /**
     * The chooseCrop method takes in the user crops and initialises
     * a do while loop to choose the selected crop for planting.
     * 
     * @param choice    the user's index choice of crop
     * @param userCrops a map of the corresponding amount of crops a user has
     * @return          the crop object corresponding to the user's choice
     */
    public Crop chooseCrop(int choice, Map<Crop, Integer> userCrops) {
        ArrayList<Crop> cropsList = new ArrayList<Crop>();

        for (Crop croptype : userCrops.keySet()) {
            cropsList.add(croptype);
        }
        System.out.println(choice);
        System.out.println(cropsList.size());
        return cropsList.get(choice);
    }

    /**
     * Plant the user's plots if valid. 
     * 
     * @param   input           user input
     * @param   loggedInUser    the user
     * @return                  character of user input, default is '-'
     */
    public char plant(String input, User loggedInUser) {
        List<Plot> plots = PDM.getPlotsOwnedByUser(loggedInUser.getUsername());
        Scanner sc = new Scanner(System.in);
        Plot plot;

        // If user enters P
        if (input.length() < 2) {
            System.out.println("Please enter a choice from | M | F | P + Valid Plot Number | C | H |");
            return '-';
        }
        try {
            plot = getPlotByInput(plots, input);


        } catch (InvalidPlotException e) {
            System.out.println(e.getMessage());
            return '-';
        }

        if (!checkPlant(plot)) {
            System.out.println("Plot " + input.substring(1) + " is not empty.");
            return '-';
        }

        Map<Crop, Integer> userCrops = farmlandViewer.getCropsOwnedByUser(loggedInUser);
        
        int cropNumber = 0;
        char choice;
        do{
            farmlandViewer.displayCrops(userCrops);
            input = Utility.getNonEmptyInput("[M]ain | City [F]armers | Select Choice > ", 
            sc, false);
            choice = Utility.getCharFromInput(input);

            if ((Character.isDigit(choice)) && (Utility.isNumeric(input))) {
                cropNumber = Integer.parseInt(input);
            }
        } while (choice != 'M' && choice != 'F' && (cropNumber > userCrops.size() || cropNumber < 1));

        if (choice == 'M' || choice == 'F') {
            return choice;
        }

        Crop chosenCrop = chooseCrop(cropNumber - 1, userCrops);
        plantPlot(loggedInUser, plot, chosenCrop);
        plots = PDM.getPlotsOwnedByUser(loggedInUser.getUsername());
        return '-';
    }

    /**
     * The checkPlant method checks if the selected plot is empty, 
     * and returns the plot.
     * @param plot      the plot
     * @return          plot ready to plant or null if plot is filled  
     */
    public boolean checkPlant(Plot plot) {
        if (isEmpty(plot)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether specified user's input is valid to get the chosen plot, if valid returns plot
     * If not valid, throws InvalidPlotException with error message
     * 
     * @param plots     the List of Plots to choose from
     * @param input     the user's input indicating the plot number
     * @return          the Plot object referred by input if valid
     * @throws InvalidPlotException     If invalid input, exception with error message is thrown
     */
    public static Plot getPlotByInput(List<Plot> plots, String input) throws InvalidPlotException {
        String plotString = input.substring(1);
        int plotNumber;
        
        try {
            plotNumber = Integer.parseInt(plotString);
        } catch (NumberFormatException e) {
            throw new InvalidPlotException("P needs to be followed by an integer.");
        }

        if (plotNumber < 1) {
            throw new InvalidPlotException(
                    "Invalid plot number! Min number allowed is 1.");
        }

        if (plotNumber > plots.size()) {
            throw new InvalidPlotException(
                    "Invalid plot number! Max number allowed is " + plots.size());
        }

        Plot plot = plots.get(plotNumber - 1);

        return plot;
    }

    /**
     * The plantPlot method updates the number of user's crops and plants the crops
     * 
     * @param loggedinUser  the user
     * @param plot          chosen plot
     * @param crop          chosen crop 
     */
    public void plantPlot(User loggedinUser, Plot plot, Crop crop) {
        // Use UserCropDAO to update Crop and Integer -1 via cropName
        inventoryManager.updateUserCrops(loggedinUser, crop, -1);

        // update Plot to have TimeCropPlanted and CropName
        plot.plantCrop(crop);
        // Use PlotDAO to update plot in DB
        PDM.updatePlot(plot);

    }

}