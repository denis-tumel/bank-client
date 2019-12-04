package controller;

import config.DialogManager;
import connection.Connection;
import connection.FXMLLoad;
import connection.ServerMessage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Credit;
import model.User;

import java.util.List;

public class UserController {
    public TextField account;
    public Label accountValue;
    public Label helloLabel;
    public TableView<Credit> tableCredit;
    public TableColumn<Credit, Integer> money;
    public TableColumn<Credit, Integer> time;
    public TableColumn<Credit, String> nameColumn;
    public TableColumn<Credit, Integer> summa;
    public Button btnSum;
    public TextField updateCredit;

    private Credit credit;
    private User user;

    @FXML
    public void initialize(){
        tableCredit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown()) {
                    credit = tableCredit.getSelectionModel().getSelectedItem();
                    btnSum.setDisable(false);
                }
            }
        });
    }

    public void ActionLogout(ActionEvent actionEvent) {
        helloLabel.getScene().getWindow().hide();
        FXMLLoad.getInstance().open("/view/main.fxml");
    }

    public void ActionAccount(ActionEvent actionEvent) {
        if (!account.getText().equals("")) {
            try{
                user.setAccount(Integer.parseInt(account.getText())+user.getAccount());
                Connection.getInstance().post("account " + user.getId() + " " + user.getName() + " " + user.getRole() + " " + user.getLogin() + " " + user.getPassword() + " " + user.getAccount());
                if((Boolean) ServerMessage.get()){
                    setInformation(user);
                    account.clear();
                }
            }catch (Exception e){
                DialogManager.showErrorDialog( "Проверьте правильность введенных данных!");
            }

        }else DialogManager.showErrorDialog( "Введите сумму!");
    }

    public void setInformation(User user){
        money.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("summa"));
        time.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("month"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Credit, String>("name"));
        summa.setCellValueFactory(new PropertyValueFactory<Credit, Integer>("payment"));

        Connection.getInstance().post("allCreditUser " + user.getId() );

        tableCredit.setItems(FXCollections.observableArrayList((List<Credit>) ServerMessage.get()));
        tableCredit.refresh();

        this.user = user;
        accountValue.setText("Ваш счёт: " + String.valueOf(user.getAccount()));
        helloLabel.setText("Здравствуйте: " + user.getName());
    }

    public void ActionSum(ActionEvent actionEvent) {
        credit = tableCredit.getSelectionModel().getSelectedItem();
        if(credit != null){
            int temp = credit.getPayment()+Integer.parseInt(updateCredit.getText().trim());
            int temp2 = Integer.parseInt(updateCredit.getText().trim());

            Connection.getInstance().post("updateAccount " + user.getId() + " " + credit.getId() + " " + temp + " " + temp2);

            if((Boolean) ServerMessage.get()){
                user.setAccount(user.getAccount()-Integer.parseInt(updateCredit.getText().trim()));
                setInformation(user);
                updateCredit.clear();
                DialogManager.showInfoDialog("Оплата совершена!");
            }else DialogManager.showErrorDialog("Ошибка транзакции!");
        }else DialogManager.showErrorDialog("Выберите значение!");

    }

    public void ActionSelectCredit(ActionEvent actionEvent) {
        helloLabel.getScene().getWindow().hide();
        FXMLLoad.getInstance().openWithCredit("/view/credit.fxml", user);
    }
}
