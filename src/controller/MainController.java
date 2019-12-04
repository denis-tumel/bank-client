package controller;

import connection.FXMLLoad;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class MainController {
    public Button login_button;

    public void ActionLogin(ActionEvent actionEvent) {
        login_button.getScene().getWindow().hide();
        FXMLLoad.getInstance().open("/view/login.fxml");
    }

    public void ActionRegister(ActionEvent actionEvent) {
        login_button.getScene().getWindow().hide();
        FXMLLoad.getInstance().open("/view/register.fxml");
    }
}
