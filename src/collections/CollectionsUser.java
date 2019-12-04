package collections;

import connection.Connection;
import connection.ServerMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.util.List;

public class CollectionsUser {
    private ObservableList<User> usersList = FXCollections.observableArrayList();

    public ObservableList<User> getUsersList() {
        return usersList;
    }

    public void fillData() {
        Connection.getInstance().post("usersTable ");
        usersList.clear();
        List<User> users = (List<User>)ServerMessage.get();
        usersList.addAll(users);
    }
}
