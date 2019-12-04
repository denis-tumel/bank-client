package controller;

import config.DialogManager;
import connection.Connection;
import connection.FXMLLoad;
import connection.ServerMessage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Credit;
import model.User;

import java.util.List;

public class CreditController {

    public TableView<Credit> tableCredit;
    public TableColumn<Credit, String> nameColumn;
    public TableColumn<Credit, Integer> moneyColumn;
    public TableColumn<Credit, Integer> timeColumn;
    public TableColumn<Credit, Integer> decimalColumn;
    public Label money;
    public TextField moneyField;
    private User user;

    public void setInformation(User user) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Credit, String>("name"));
        moneyColumn.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("money"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("month"));
        decimalColumn.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("decimal"));

        Connection.getInstance().post("allCredit ");

        tableCredit.setItems(FXCollections.observableArrayList((List<Credit>) ServerMessage.get()));
        tableCredit.refresh();
        money.setText("счет: "+user.getAccount());
        this.user = user;
        moneyField.setText("0");
    }

    public void ActionOK(ActionEvent actionEvent) {
        Credit credit = tableCredit.getSelectionModel().getSelectedItem();
        if(!moneyField.getText().equals("")){
            Connection.getInstance().post("saveCredit " + credit.getId() + " " + user.getId() + " " + moneyField.getText());
            if ((Boolean)ServerMessage.get()){
                DialogManager.showInfoDialog("Кредит оформлен!");
            }else DialogManager.showErrorDialog("Ошибка, сбой оформления!");
            user.setAccount(user.getAccount()-Integer.parseInt(moneyField.getText()));
            tableCredit.getScene().getWindow().hide();
            FXMLLoad.getInstance().openWithParamUser("/view/client.fxml", user);
        }else DialogManager.showErrorDialog("Введите начальную сумму");

    }

    public void ActionCancel(ActionEvent actionEvent) {
        tableCredit.getScene().getWindow().hide();
        FXMLLoad.getInstance().openWithParamUser("/view/client.fxml", user);
    }
}
