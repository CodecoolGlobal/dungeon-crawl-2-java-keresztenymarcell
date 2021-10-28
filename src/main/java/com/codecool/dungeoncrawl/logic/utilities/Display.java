package com.codecool.dungeoncrawl.logic.utilities;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class Display {

    private static int height = 260;
    private static int width = 268;

     public static void showGameOverMessage(String message){
         Dialog<String > losingMessage = new Dialog<>();
        losingMessage.setTitle("Message");
        losingMessage.setContentText(message);
        losingMessage.show();
        losingMessage.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        losingMessage.setHeight(height);
        losingMessage.setWidth(width);

    }
}
