package connection;

import controller.AdminController;
import controller.CreditController;
import controller.UserController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class FXMLLoad {
    private static FXMLLoad instance;
    private static ReentrantLock lock = new ReentrantLock();
    private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    private FXMLLoad() {
    }

    public static FXMLLoad getInstance() {
        lock.lock();
        if (!atomicBoolean.get()) {
            instance = new FXMLLoad();
            atomicBoolean.set(true);
        }
        lock.unlock();
        return instance;
    }


    public void open(String url) {
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource(url));
        try {
            load.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = load.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void openWithParamUser(String url, User user) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource(url));
            Parent root = load.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            UserController userController = (UserController) load.getController();
            userController.setInformation(user);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openWithCredit(String url, User user) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource(url));
            Parent root = load.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            CreditController creditController = (CreditController) load.getController();
            creditController.setInformation(user);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
