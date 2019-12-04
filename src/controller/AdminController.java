package controller;

import collections.CollectionsUser;
import config.DialogManager;
import connection.Connection;
import connection.FXMLLoad;
import connection.ServerMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;

import javax.sound.sampled.Line;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AdminController {
    public TableView<User> tableUserView;
    public TableColumn<User, String> columnName;
    public TableColumn<User, String> columnRole;
    public TableColumn<User, String> columnEmail;
    public TableColumn<User, Integer> columnAccount;
    public ChoiceBox<String> role;
    public TextField name;
    public TextField email;
    public TextField password;
    public TextField account;
    public Button btnAdd;
    public Button btnDelete;
    public Button loginButton;

    private CollectionsUser collectionsUser = new CollectionsUser();
    private User selectedUser;

    @FXML
    public void initialize(){

        tableUserView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown()) {
                    selectedUser = tableUserView.getSelectionModel().getSelectedItem();

                    if(event.getClickCount() == 2){
                        name.setText(selectedUser.getName());
                        email.setText(selectedUser.getLogin());
                        password.setText(selectedUser.getPassword());
                        role.setValue(selectedUser.getRole());
                        account.setText(String.valueOf(selectedUser.getAccount()));
                    }
                }
            }
        });

        role.setItems(FXCollections.observableArrayList("admin", "user", "worker"));

        columnName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        columnRole.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        columnAccount.setCellValueFactory(new PropertyValueFactory<User, Integer>("account"));

        collectionsUser.fillData();

        tableUserView.setItems(collectionsUser.getUsersList());
        tableUserView.refresh();
    }

    public void ActionLogout(ActionEvent actionEvent) {
        btnAdd.getScene().getWindow().hide();
        FXMLLoad.getInstance().open("/view/main.fxml");
    }

    public void ActionButtonPressed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if(!(source instanceof Button)){
            return;
        }
        Button clickedButton = (Button) source;

        switch (clickedButton.getId()){
            case "btnAdd":
                if(!name.getText().equals("")
                        && role.getValue() != null
                        && !email.getText().equals("")
                        && !password.getText().equals("")
                        && !account.getText().equals("")){
                    try {
                        if(selectedUser == null){
                            selectedUser = new User();
                        }
                        selectedUser.setName(name.getText());
                        selectedUser.setRole(role.getSelectionModel().getSelectedItem());
                        selectedUser.setLogin(email.getText());
                        selectedUser.setPassword(password.getText());
                        selectedUser.setAccount(Integer.parseInt(account.getText()));

                        Connection.getInstance().post("addUser " + selectedUser.getId() + " " + selectedUser.getName() + " " + selectedUser.getRole() + " " + selectedUser.getLogin() + " " + selectedUser.getPassword() + " " + selectedUser.getAccount());

                        if((Boolean) ServerMessage.get()){
                            DialogManager.showInfoDialog( "Добавление произошло успешно!");
                            name.clear();
                            email.clear();
                            password.clear();
                            account.clear();
                            selectedUser = null;
                            initialize();
                        }
                    }catch (Exception e){
                        DialogManager.showErrorDialog( "Проверьте правильность введенных данных!");
                    }

                }else {
                    DialogManager.showErrorDialog("Заполните поля!");

                }
                break;
            case "btnDelete":

                if(!(selectedUser == null)){
                    Connection.getInstance().post("deleteUser " + selectedUser.getId());
                    if((Boolean) ServerMessage.get()){
                        initialize();
                        DialogManager.showInfoDialog( "Удалено!");
                    }
                }else DialogManager.showErrorDialog( "Выберите пользователя!");
                selectedUser = null;
                break;
        }
    }

    public void ActionShowData(ActionEvent actionEvent) {
        selectedUser = tableUserView.getSelectionModel().getSelectedItem();

        if(selectedUser != null){
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/view/information.fxml"));
                Parent root = load.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                InformationController informationController = (InformationController) load.getController();
                informationController.setInformation(selectedUser);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            DialogManager.showErrorDialog("Выберите пользователя!");
        }

    }

    public void ActionBlock(ActionEvent actionEvent) {
        if (selectedUser != null){
            Connection.getInstance().post("blockUser " + selectedUser.getId());
            DialogManager.showInfoDialog( "Пользователь заблокирован!");
            initialize();
        }
        else
            DialogManager.showErrorDialog("Выберите пользователя из таблицы!");
    }

    public void ActionUnlock(ActionEvent actionEvent) {
        if (selectedUser != null){
            Connection.getInstance().post("unlockUser " + selectedUser.getId());
            DialogManager.showInfoDialog( "Пользователь разблокирован!");
            initialize();
        }
        else
            DialogManager.showErrorDialog("Выберите пользователя из таблицы!");
    }

    public void ActionGraff(ActionEvent actionEvent) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/view/pie_chart.fxml"));
            Parent root = load.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            PieChartController pieChartController = (PieChartController) load.getController();
            pieChartController.setInformation(tableUserView.getItems());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ActionFile(ActionEvent actionEvent) {
        saveInformationInFile(tableUserView.getItems());
    }

    private void saveInformationInFile(ObservableList<User> items) {
        int count = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("InformationClient.txt", true))) {
            for (User client : items) {
                count++;
                String out = count + "имя:" + client.getName() + ",\nденьги: " + client.getAccount() + ",\nлогин: " + client.getLogin() + " \n";
                writer.append(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
