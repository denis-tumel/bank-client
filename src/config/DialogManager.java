package config;

import config.Const;
import javafx.scene.control.Alert;

public class DialogManager {

    public static void showInfoDialog(String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Const.INFO);
        alert.setContentText(text);
        alert.setHeaderText("");
        alert.showAndWait();
    }

    public static void showErrorDialog(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Const.ERROR);
        alert.setContentText(text);
        alert.setHeaderText("");
        alert.showAndWait();
    }
}
