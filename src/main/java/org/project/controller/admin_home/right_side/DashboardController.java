package org.project.controller.admin_home.right_side;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import org.project.model.dao.users.UsersDAOImpl;

import java.net.URL;
import java.util.Map;
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
    private UsersDAOImpl usersDAO;

    public void setUsersDAO(UsersDAOImpl usersDAO) {
        this.usersDAO = usersDAO;
        drawUsersCountryChart(usersCountriestdata);
        drawUsersStatusChart(userstatusChartPane, usersStatusrdata);
        drawUsersGenderChart(userGenderChartPane, usersGenderdata);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }


    private void drawPieChart(StackPane stackPane, ObservableList<PieChart.Data> observableList, double usersNo, boolean genderChart) {
        String tooltipText = "";
        PieChart pieChart = new PieChart();
        pieChart.setData(observableList);
        stackPane.getChildren().clear();
        stackPane.getChildren().add(pieChart);
        System.out.println("drawPieChart");
        for (PieChart.Data data : pieChart.getData()) {
            Node slice = data.getNode();
            double percent = (data.getPieValue() / usersNo * 100);
            if (genderChart)
                tooltipText = data.getName() + " = " + String.format("%.2f", percent) + "%";
            else
                tooltipText = data.getName() + " = " + (int) data.getPieValue();
            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(slice, tooltip);

        }
    }

    private void drawUsersGenderChart(StackPane stackPane, ObservableList<PieChart.Data> usersGenderList) {
        int UsersNum = 0;
        Map<String, Integer> map = usersDAO.getUsersByGender();
        for (Map.Entry m : map.entrySet()) {
            usersGenderList.add(new PieChart.Data(m.getKey().toString(), Integer.parseInt(m.getValue().toString())));
            UsersNum += Integer.parseInt(m.getValue().toString());
        }
        drawPieChart(stackPane, usersGenderList, UsersNum, true);

    }


    public void drawUsersStatusChart(StackPane stackPane, ObservableList<PieChart.Data> usersStatusList) {
        int UsersNum = 0;
        Map<String, Integer> map = usersDAO.getUsersByStatus();
        for (Map.Entry m : map.entrySet()) {
            int statusValy =Integer.parseInt(m.getValue().toString());
            if (m.getKey().toString().equals("Offline") && statusValy > 0) {
                usersStatusList.add(new PieChart.Data("OFF-Line", statusValy));
            }else if(m.getKey().toString().equals("Online")  && statusValy > 0) {
                usersStatusList.add(new PieChart.Data("ON-Line",statusValy));
            }
            UsersNum += Integer.parseInt(m.getValue().toString());
        }
        drawPieChart(stackPane, usersStatusList, UsersNum, false);

    }

    public void drawUsersCountryChart(ObservableList<XYChart.Series<String, Number>> usersCountriesList) {

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Countries");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("(NO.of Users)");
        XYChart.Series<String, Number> countriesSeries = new XYChart.Series();
        Map<String, Integer> map = usersDAO.getUsersNumByCountry();
        System.out.println(map);
        if (map.size() > 0) {
            for (Map.Entry m : map.entrySet()) {
                if (m.getKey() != null)
                    countriesSeries.getData().add(new XYChart.Data(m.getKey(), m.getValue()));
            }

            usersCountriesList.add(countriesSeries);
            usresCountryChart.setData(usersCountriesList);
            usresCountryChart.setTitle("statistics about the users’ country");
            usresCountryChart.setLegendVisible(false);

        }
    }
}
