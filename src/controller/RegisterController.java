package controller;

import config.DialogManager;
import connection.Connection;
import connection.FXMLLoad;
import connection.ServerMessage;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;

public class RegisterController {
    public TextField firstName;
    public TextField emailField;
    public PasswordField passwordField;
    public PasswordField confirmPassword;
    public TextField account;

    public void ActionRegister(ActionEvent actionEvent) {
        if (!emailField.getText().equals("") && !passwordField.getText().equals("") && !firstName.getText().equals("") && !confirmPassword.getText().equals("") && !account.getText().equals("")) {
            Connection.getInstance().post("sighUp " + firstName.getText().trim() + " " + emailField.getText().trim() + " " + passwordField.getText().trim() + " " + account.getText().trim());
            User user = (User) ServerMessage.get();
            if(user == null){
                emailField.clear();
                passwordField.clear();
                DialogManager.showErrorDialog("Пользователь с таким логином уже есть!");
            } else{
                emailField.getScene().getWindow().hide();
                FXMLLoad.getInstance().openWithParamUser("/view/client.fxml", user);
            }
        } else DialogManager.showErrorDialog("Заполните все поля!");
    }

    public void ActionBack(ActionEvent actionEvent) {
        emailField.getScene().getWindow().hide();
        FXMLLoad.getInstance().open("/view/main.fxml");
    }

    public void ActionLogin(ActionEvent actionEvent) {
        emailField.getScene().getWindow().hide();
        FXMLLoad.getInstance().open("/view/login.fxml");
    }
}
