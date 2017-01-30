//package com.TheVTM.bots.BonePrayer.GUI;
//
//import com.TheVTM.bots.BonePrayer.BonePrayer;
//import com.TheVTM.bots.BonePrayer.Constants;
//import com.TheVTM.bots.BonePrayer.Events.ConfigurationEvent;
//import com.TheVTM.bots.BonePrayer.Events.StopBotEvent;
//import com.runemate.game.api.hybrid.util.Resources;
//import javafx.application.Platform;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import simpleeventbus.EventHandler;
//
///**
// * Created by VTM on 4/4/2016.
// *
// */
//public class MyStage extends Stage {
//
//    public void initialize () {
//        /* Register event handlers */
//        BonePrayer.GetInstance().dispatcher.addHandler(this);
//
//        /* Load fxml file */
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader();
//            fxmlLoader.setController(new MyController());
//            final Parent root = fxmlLoader.load(Resources.getAsStream(Constants.LAYOUT_FXML));
//
//            // Set up Scene
//            final Scene scene = new Scene(root);
//
//            // Initialize MyStage
//            setTitle("Bone Prayer");
//            setScene(scene);
//            show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @EventHandler
//    public void startEventHandler(ConfigurationEvent event) {
//        Platform.runLater(this::close);
//    }
//
//    @EventHandler
//    public void stopEventHandler(StopBotEvent event) {
//        Platform.runLater(this::close);
//    }
//}
