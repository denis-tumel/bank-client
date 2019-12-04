import connection.FXMLLoad;
import javafx.application.Application;
import javafx.stage.Stage;

public class Start extends Application {

    @Override
    public void start(Stage primaryStage){
        FXMLLoad.getInstance().open("/view/main.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
