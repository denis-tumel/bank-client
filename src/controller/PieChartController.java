package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class PieChartController {

    @FXML
    private PieChart pieChart;

    public void setInformation(ObservableList<User> items) {
        List<PieChart.Data> list = new ArrayList<>();
        for(User client : items){
            if (!client.getLogin().equals("bank"))
                list.add(new PieChart.Data(client.getName(), client.getAccount()));
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(list);
        pieChart.setData(pieChartData);
    }
}
