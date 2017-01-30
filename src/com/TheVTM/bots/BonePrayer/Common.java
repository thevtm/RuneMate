package com.TheVTM.bots.BonePrayer;

import javafx.scene.control.Alert;

/**
 * Created by VTM on 4/4/2016.
 */
public class Common {

    public static Alert createAndShowErrorDialog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();

        return alert;
    }
}
