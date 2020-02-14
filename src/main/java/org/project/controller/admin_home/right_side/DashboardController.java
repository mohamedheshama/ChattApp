package org.project.Controller.admin_home.right_side;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private StackPane userGenderChartPane;
    @FXML
    private StackPane userstatusChartPane;
    @FXML
    private BarChart usresCountryChart;
    @FXML
    private PieChart usersStatusChart;


    private ObservableList<XYChart.Series<String, Number>> usersCountriestdata = FXCollections.observableArrayList();
    private ObservableList<PieChart.Data> usersGenderdata = FXCollections.observableArrayList();
    private ObservableList<PieChart.Data> usersStatusrdata = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawUsersCountryChart(usersCountriestdata);
        drawUsersStatusChart(userstatusChartPane, usersStatusrdata);
        drawUsersGenderChart(userGenderChartPane, usersGenderdata);

    }


    private void drawPieChart(StackPane stackPane, ObservableList<PieChart.Data> observableList, double usersNo) {
        PieChart pieChart = new PieChart();
        pieChart.setData(observableList);
        stackPane.getChildren().add(pieChart);


        for (PieChart.Data data : pieChart.getData()) {
            Node slice = data.getNode();
            double percent = (data.getPieValue() / usersNo * 100);
            String tip = data.getName() + " = " + String.format("%.2f", percent) + "%";
            Tooltip tooltip = new Tooltip(tip);
            Tooltip.install(slice, tooltip);

        }
    }

    private void drawUsersGenderChart(StackPane stackPane, ObservableList<PieChart.Data> usersGenderList) {
        usersGenderList.add(new PieChart.Data("Famale", 100));
        usersGenderList.add(new PieChart.Data("Male", 150));
        drawPieChart(stackPane, usersGenderList, 250);

    }

    private void drawUsersStatusChart(StackPane stackPane, ObservableList<PieChart.Data> usersGenderList) {
        usersGenderList.add(new PieChart.Data("ON-Line", 200));
        usersGenderList.add(new PieChart.Data("OFF-Line", 100));
        drawPieChart(stackPane, usersGenderList, 300);

    }

    private void drawUsersCountryChart(ObservableList<XYChart.Series<String, Number>> usersCountriesList) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Countries");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("(NO.of Users)");
        XYChart.Series countriesSeries = new XYChart.Series();
        countriesSeries.getData().add(new XYChart.Data("Eygpt", 100));
        countriesSeries.getData().add(new XYChart.Data("Brazil", 200));
        countriesSeries.getData().add(new XYChart.Data("France", 500));
        countriesSeries.getData().add(new XYChart.Data("Italy", 650));
        countriesSeries.getData().add(new XYChart.Data("USA", 900));
        usersCountriestdata.add(countriesSeries);
        usresCountryChart.setData(usersCountriestdata);
        usresCountryChart.setTitle("statistics about the users’ country");
        usresCountryChart.setBarGap(50);
        usresCountryChart.setAnimated(true);


    }
}
