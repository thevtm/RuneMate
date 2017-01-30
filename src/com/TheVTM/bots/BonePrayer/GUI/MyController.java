//package com.TheVTM.bots.BonePrayer.GUI;
//
//import com.TheVTM.bots.BonePrayer.BonePrayer;
//import com.TheVTM.bots.BonePrayer.Common;
//import com.TheVTM.bots.BonePrayer.Events.ConfigurationEvent;
//import com.TheVTM.bots.BonePrayer.UserConfiguration;
//import com.runemate.game.api.hybrid.location.Coordinate;
//import com.runemate.game.api.hybrid.region.Players;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// * Created by VTM on 4/4/2016.
// *
// */
//public class MyController implements Initializable {
//
//    /* FXML */
//    @FXML
//    private Button btnStart;
//
//    @FXML
//    public TextField tfMaxDistance;
//
//    /* LOGGER */
//    private static final Logger LOGGER = Logger.getLogger(MyController.class.getName());
//
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        EventHandler<ActionEvent> start;
//        btnStart.setOnAction(e -> start());
//    }
//
//    private void start() {
//        LOGGER.log(Level.INFO, "Starting script...");
//
//        Inputs inputs = validateInputs();
//        if(inputs != null) {
//            // create a UserConfiguration
//            UserConfiguration userConfiguration = createUserConfiguration(inputs);
//
//            // Verify if UserConfiguration is valid
//            if (userConfiguration == null)
//                return;
//
//            // Dispatch ConfigurationEvent
//            BonePrayer.GetInstance().dispatcher.publish(new ConfigurationEvent(userConfiguration));
//        }
//    }
//
//    private Inputs validateInputs() {
//        Integer maxDistance;
//
//        /* VALIDATE tfMaxDistance */
//        try {
//            // tfMaxDistance is Integer
//            maxDistance = Integer.parseInt(tfMaxDistance.getText());
//
//            // tfMaxDistance is between max and min
//            if(maxDistance > UserConfiguration.MAX_DISTANCE_DEFAULT ||
//                    maxDistance < UserConfiguration.MAX_DISTANCE_MIN) {
//
//                // Show error dialog
//                Common.createAndShowErrorDialog("Invalid input",
//                        String.format("Max distance from current location must be a value between %d and %d.",
//                                UserConfiguration.MAX_DISTANCE_MIN, UserConfiguration.MAX_DISTANCE_MAX));
//
//                return null;
//            }
//
//        } catch (NumberFormatException e) { // unable to parse tfMaxDistance to integer
//            // Show error dialog
//            Common.createAndShowErrorDialog("Invalid input",
//                    String.format("Max distance from current location \"%s\" must be an integer.", tfMaxDistance.getText()));
//
//            return null;
//        }
//
//        /* Return */
//        Inputs result = new Inputs();
//        result.maxDistance = maxDistance;
//
//        return result;
//    }
//
//    private UserConfiguration createUserConfiguration(Inputs inputs) {
//        Coordinate startingPosition;
//
//        /* VALIDATE startingPosition */
//        try {
//            startingPosition = Players.getLocal().getPosition();
//            if(startingPosition == null) {
//                Common.createAndShowErrorDialog("Invalid input", "Unable to retrieve your player location.");
//                LOGGER.log(Level.WARNING, "Unable to retrieve player location (null).");
//                return null;
//            }
//        } catch (Exception e) {
//            Common.createAndShowErrorDialog("Invalid input", "Unable to retrieve your player location.");
//            LOGGER.log(Level.WARNING, "Unable to retrieve player location.", e);
//            return null;
//        }
//
//        return new UserConfiguration(startingPosition, inputs.maxDistance);
//    }
//
//    private class Inputs {
//        public Integer maxDistance;
//    }
//}
