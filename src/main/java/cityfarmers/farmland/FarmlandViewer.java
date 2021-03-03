package cityfarmers.farmland;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import daos.CropDAO;
import daos.PlotDAO;
import daos.UserCropDAO;
import entities.Crop;
import entities.Plot;
import entities.User;

/**
 * The FarmlandDisplay class facilitates the interaction between the database and
 * the FarmlandCtrl for Display Methods
 * 
 * @version 1.0 08 Apr 2020
 * @author Wan Ding Yang
 */
public class FarmlandViewer {
    /** CDM provides access to the Crop table in the database */
    private CropDAO CDM;

    /** UCDM provides access to the UserCrop table in the database */
    private UserCropDAO UCDM;

    /** PDM provides access to the Plot table in the database */
    private PlotDAO PDM;

    /** Creates a FarmlandDisplay object with a default CDM, UCDM and PDM*/
    public FarmlandViewer() {
        CDM = new CropDAO();
        UCDM = new UserCropDAO();
        PDM = new PlotDAO();
    }


    /**
     * Returns all of the user's plots.
     * 
     * @param username  thr username
     * @return          list of plots owned by user
     */
    public List<Plot> getPlotsOwnedByUser(String username){
        return PDM.getPlotsOwnedByUser(username);
    }

    /**
     *Returns all of the user's crops.
     *
     * @param loggedInUser  the user
     * @return              a map of the corresponding amount of crops a user has
     */
    public Map<Crop, Integer> getCropsOwnedByUser(User loggedInUser) {
        return UCDM.getUserCrops(loggedInUser);
    }
    
    /**
     * Displays all a user's plots with an index and harvest status. 
     * For example, 1. Banana [###-------] 37%
     * 
     * @param plots a list of the user's plots
     */
    public void displayPlots(List<Plot> plots) {
        for (int i = 0; i < plots.size(); i++) {

            if (plots.get(i).getCropName() == null) {
                System.out.println(i + 1 + ". " + "<empty>");
            } else {
                System.out.print(i + 1 + ". " + plots.get(i).getCropName() + "\t");
            
                // Time Planted from Plot
                Date timePlanted = plots.get(i).getTimeCropPlanted();
                String cropName = plots.get(i).getCropName();
                long timePlantedMinutes = timePlanted.getTime() / 60000;
                
                // Time to Complete from Crop
                int timeToComplete = CDM.getCrop(cropName).getTimeToHarvest();
                Date currentDate = new Date();
                long currentDateMinutes = currentDate.getTime() / 60000;
                double progressRatio = (currentDateMinutes - timePlantedMinutes) / (double)timeToComplete;
                int progressCount = (int)(progressRatio * 10);
    
                // Able to Harvest
                if (progressRatio > 2.0) {
                    System.out.println("[  wilted  ]");
                } else {
                    if (progressRatio > 1.0) {
                        progressCount = 10;
                        progressRatio = 1;
                    }
                    // Display status bar and percentage
                    System.out.print("[");
                    for (int x = 0; x < progressCount; x++) {
                        System.out.print("#");
                    }
                    for (int x = 0; x < 10 - progressCount; x++) {
                        System.out.print("-");
                    }
                    System.out.println("] " + (int)(progressRatio * 100) + "%");
                }
            }
        }
    }

    /**
     * Displays all a user's crops with an index.
     * 
     * @param userCrops a map of the corresponding amount of crops a user has
     */
    public void displayCrops(Map<Crop, Integer> userCrops) {

        // No crops to display
        if (userCrops == null || userCrops.size() == 0) {
            System.out.println("You do not have any crops.");
            System.out.print("[M]ain | City [F]armers | Select Choice > ");
            return;
        }

        ArrayList<Crop> cropsList = new ArrayList<Crop>();
        String cropName;
        int count = 0;

        for (Crop croptype : userCrops.keySet()) {
            cropsList.add(croptype);
            count += 1;
            cropName = croptype.getCropName();
            System.out.println(count + ": " + cropName);
        }
        System.out.print("[M]ain | City [F]armers | Select Choice > ");
    }

    /**
     * Displays friend's farmland and plots with an index.
     * 
     * @param chosenFriend the chosen friend
     */
    public void displayFriend(User chosenFriend) {
        System.out.println();
        System.out.println("Name: " + chosenFriend.getFullName());
        System.out.println("Title: " + chosenFriend.getRank());
        System.out.println("Gold: " + chosenFriend.getGold());
        // List number of plots
        List<Plot> plots = PDM.getPlotsOwnedByUser(chosenFriend.getUsername());
        displayPlots(plots);
        System.out.print("[M]ain | City [F]armers | [S]teal > ");
    }
}