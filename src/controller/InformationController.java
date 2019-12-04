package controller;

import config.DialogManager;
import connection.Connection;
import connection.ServerMessage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Credit;
import model.User;

import java.util.List;

public class InformationController {
    public TableView<Credit> tableCredit;
    public TableColumn<Credit, Integer> money;
    public TableColumn<Credit, Integer> time;
    public TableColumn<Credit, String> nameColumn;
    public TableColumn<Credit, Integer> summa;
    public Label userName;
    private User user;

    public void setInformation(User user) {
        this.user = user;
        userName.setText(user.getName());

        money.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("summa"));
        time.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("month"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Credit, String>("name"));
        summa.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("payment"));

        Connection.getInstance().post("allCreditUser " + user.getId() );

        tableCredit.setItems(FXCollections.observableArrayList((List<Credit>) ServerMessage.get()));
        tableCredit.refresh();
    }

    public void ActionCancel(ActionEvent actionEvent) {
        userName.getScene().getWindow().hide();
    }

    public void ActionOK(ActionEvent actionEvent) {
        Credit credit = tableCredit.getSelectionModel().getSelectedItem();

        if(credit != null){
            if(credit.getSumma().equals(credit.getPayment())){
                Connection.getInstance().post("deleteCredit " + credit.getId() + " " + user.getId() + " " + credit.getSumma());
                if((Boolean) ServerMessage.get()){
                    DialogManager.showInfoDialog("Кредит погашен!");
                    userName.getScene().getWindow().hide();
                }else DialogManager.showErrorDialog("Ошибка транзакции!");
            }else DialogManager.showErrorDialog("Кредит еще не погашен!");
        }else DialogManager.showErrorDialog("Ввыберите значение!");
    }
}
