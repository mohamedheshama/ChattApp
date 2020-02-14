package org.project.controller.admin_home.right_side;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.project.exceptions.UserAlreadyExistException;
import org.project.model.connection.MysqlConnection;
import org.project.model.dao.users.Users;
import org.project.model.dao.users.UsersDAOImpl;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersController implements Initializable {


    @FXML
    private Label valErrorlbl;

    @FXML
    private TextField phoneTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField emailTxt;

    @FXML
    private TextField passwordTxt;

    @FXML
    private TableColumn<Users, String> phoneCol;

    @FXML
    private TableColumn<Users, Integer> id;

    @FXML
    private TableColumn<Users, String> useCol;

    @FXML
    private TableColumn<Users, String> nameCol;

    @FXML
    private TableColumn<Users, String> emailCol;

    @FXML
    private TableColumn<Users, String> passwordCol;

    @FXML
    private TableView<Users> tableView;

    String originPhone;

    List<Users> list = null;
    ObservableList<Users> lists = FXCollections.observableArrayList();
    ObservableList<Users> selectedIndexes = FXCollections.observableArrayList();
    //    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    static final String DB_URL = "jdbc:mysql://localhost/first";
//    private Connection conn = null;
    MysqlConnection mysqlConnection = MysqlConnection.getInstance();
    UsersDAOImpl usersDAOImpl;

    {
        try {
            usersDAOImpl = new UsersDAOImpl(mysqlConnection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void Update(ActionEvent event) {

        selectedIndexes = tableView.getSelectionModel().getSelectedItems();
        usersDAOImpl.updateUser(selectedIndexes.get(0));

         System.out.println("phone" + selectedIndexes.get(0).getPhoneNumber());
        System.out.println("name" + selectedIndexes.get(0).getName());
        System.out.println("email" + selectedIndexes.get(0).getEmail());
        System.out.println("password" + selectedIndexes.get(0).getPassword());
        System.out.println("id" + selectedIndexes.get(0).getId());

         System.out.println("phone cell"+ phoneCol.getText());
        System.out.println("You clicked me!");
//        label.setText("Hello World!");
    }

    @FXML
    private void Insert(ActionEvent event) {
        Users user = new Users();
       if (validate(nameTxt.getText(),phoneTxt.getText(),emailTxt.getText(),passwordTxt.getText())) {


           user.setName(nameTxt.getText());
           user.setPhoneNumber(phoneTxt.getText());
           user.setEmail(emailTxt.getText());
           user.setPassword(passwordTxt.getText());
           try {
               usersDAOImpl.register(user);


           } catch (UserAlreadyExistException ex) {
               Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
           }
           valErrorlbl.setText("");
           lists.add(user);
       }else{


           valErrorlbl.setText("Error Data Evaluated");
           valErrorlbl.setStyle("-fx-border: 0px 0px 2px 0px ; -fx-border-color: #f60");
       }




    }

    @FXML
    private void delete(ActionEvent event) {
        Users user = tableView.getSelectionModel().getSelectedItem();
        usersDAOImpl.deleteUSer(user);


        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItems());

        System.out.println("deleted");
    }


    public boolean validate(String nameTxtval,String phoneTxtval,String emailTxtval,String passwordTxtval) {

        if (phoneTxtval.matches("^01[0125]{1}(\\-)?[^0\\D]{1}\\d{7}$") && nameTxtval.matches("^[a-zA-Z_-][ a-zA-Z0-9_-]{6,14}$") && emailTxtval.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+\\b(com|net|eg)\\b$") && passwordTxtval.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\\\\\\\s]).{6,}")) {
            return true;

        } else {

            return false;
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableView.setEditable(true);
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        // nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordCol.setCellFactory(TextFieldTableCell.forTableColumn());
        useCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //tableView.getColumns().add( dateOfBirthColumn);

        phoneCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("phoneNumber")
        );
//        nameCol.setCellValueFactory(
//                new PropertyValueFactory<Person, String>("test")
//        );
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("Email")
        );
        passwordCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("Password")
        );
        useCol.setCellValueFactory(
                new PropertyValueFactory<Users, String>("name")
        );

        //  tableView.getSelectionModel().get
        phoneCol.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) -> {
            TablePosition<Users, String> pos = event.getTablePosition();

            String newFullName = event.getNewValue();
            System.out.println("new val" + newFullName);

            int row = pos.getRow();
            Users person = event.getTableView().getItems().get(row);
            System.out.println("pass" + person.getPassword());
            System.out.println("phone" + person.getPhoneNumber());
            System.out.println("email" + person.getEmail());
            System.out.println("use" + person.getName());

            person.setPhoneNumber(newFullName);
        });

        useCol.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) -> {
            TablePosition<Users, String> pos = event.getTablePosition();

            String newFullName = event.getNewValue();
            System.out.println("new val" + newFullName);

            int row = pos.getRow();
            Users person = event.getTableView().getItems().get(row);
            System.out.println("pass" + person.getPassword());
           // System.out.println("phone" + person.getPhone());
            System.out.println("email" + person.getEmail());
           // System.out.println("use" + person.getUse());

            person.setName(newFullName);
        });



        emailCol.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) -> {
            TablePosition<Users, String> pos = event.getTablePosition();

            String newFullName = event.getNewValue();
            System.out.println("new val" + newFullName);

            int row = pos.getRow();
            Users person = event.getTableView().getItems().get(row);
            System.out.println("pass" + person.getPassword());
           // System.out.println("phone" + person.getPhone());
            System.out.println("email" + person.getEmail());
            //System.out.println("use" + person.getUse());

            person.setEmail(newFullName);
        });



        passwordCol.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) -> {
            TablePosition<Users, String> pos = event.getTablePosition();

            String newFullName = event.getNewValue();
            System.out.println("new val" + newFullName);

            int row = pos.getRow();
            Users person = event.getTableView().getItems().get(row);
            System.out.println("pass" + person.getPassword());
           // System.out.println("phone" + person.getPhone());
            System.out.println("email" + person.getEmail());
            //System.out.println("use" + person.getUse());

            person.setPassword(newFullName);
        });

        lists=  usersDAOImpl.getUsers();

        tableView.setItems(lists);



    }
}
